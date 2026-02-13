package com.jd.majors.mp4_processor.Parsing;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.TopLevelAtom;

/**
 * Represents and parses an MP4 file, exposing its top-level atoms and
 * providing access to nested atom hierarchies.
 *
 * <p>{@code Mp4File} is responsible for reading an MP4/ISO Base Media File
 * Format stream via a {@link java.nio.channels.FileChannel} and constructing
 * a tree of {@link Box} instances, including both container and leaf atoms.
 * It handles atom size validation, parent–child nesting, and automatic parsing
 * of payloads and child containers.</p>
 *
 * <p>Parsing is performed in a single pass using memory-mapped I/O to efficiently
 * access file contents. The class supports the following behaviors:</p>
 * <ul>
 *   <li>Top-level atom detection and ordering, exposed via
 *       {@link #topLevelAtoms()}.</li>
 *   <li>Automatic nesting of {@link NestedAtom} instances into appropriate
 *       {@link ContainerBox} parents.</li>
 *   <li>Delegation to {@link AtomRegistry} for instantiating concrete atom
 *       types based on four-character atom identifiers.</li>
 *   <li>Parsing of {@link Leaf} atoms and recursive parsing of
 *       {@link ContainerBox} children when requested.</li>
 *   <li>Comprehensive validation of atom size, name, and boundaries to prevent
 *       invalid memory access.</li>
 * </ul>
 *
 * <p>Construction is private; instances must be obtained via the static
 * {@link #parse(java.nio.channels.FileChannel)} factory method. This ensures
 * that all parsing logic is applied and top-level atoms are populated before
 * the object is exposed.</p>
 *
 * <p>Implementation notes:</p>
 * <ul>
 *   <li>Atom sizes and multi-byte values are interpreted in big-endian order.</li>
 *   <li>Memory-mapped buffers are used to efficiently read large files.</li>
 *   <li>Offset tracking ensures proper nesting and prevents duplicate
 *       insertion into multiple containers.</li>
 *   <li>After parsing, {@link Leaf} payloads are processed via their
 *       {@link com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf#parse()} method,
 *       and {@link ContainerBox} children are recursively parsed.</li>
 * </ul>
 */

public class Mp4File
{
	private final FileChannel fileChannel;
	private List<Box> topLevelAtoms;

	private Mp4File(FileChannel fileChannel)
	{
		this.fileChannel = fileChannel;
	}

	private byte[] getRawAtom(long offset) throws IOException
	{
		// can significantly speed reads
		MappedByteBuffer mappedBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, offset, fileChannel.size() - offset);

		// atom sizes are stored over 4 bytes
		int atomSize = 0;
		int eightMultiple = 3;
		for (int i = 0; i < 4; i++)
		{
			// & 0xFF masks so can deal with only one byte
			// << left shifts by decrementing multiples of 8 to place bits in respective spots
			// using long because java ints are signed whilst minor version is unsigned
			atomSize = atomSize | (mappedBuffer.get() & 0xFF) << 8 * eightMultiple;
			eightMultiple = eightMultiple - 1;
		}

		mappedBuffer.position(0);

		// iterate over whole atom and gather atom data
		byte[] atomData = new byte[atomSize];
		for (int i = 0; i < atomSize; i++)
		{
			atomData[i] = mappedBuffer.get();
		}

		return atomData;
	}

	// TO-DO: make private. will be switching just to a parse() function to parse whole mp4 file
	private Box createAtom(long offset) throws IOException
	{
		byte[] rawAtom = getRawAtom(offset);

		// size, name, and payload finding for atom
		// other fields parsed in atoms
		int size = 0;
		int eightMultiple = 3;
		for (int i = 0; i < 4; i++)
		{
			size = size | (rawAtom[i] & 0xFF) << 8 * eightMultiple;
			eightMultiple = eightMultiple - 1;
		}
		
		// use ISO_8859_1 to get 1:1 byte to char mapping
		// utf-8 would mangle non-ascii chars 
		String name = new String(Arrays.copyOfRange(rawAtom, 4, 8), StandardCharsets.ISO_8859_1);
		byte[] payload = Arrays.copyOfRange(rawAtom, 8, rawAtom.length);

		Box atom = AtomRegistry.createAtom(size, name, payload);
		return atom;
	}

	// factory method that prevents direct instantiation
	// used via Mp4File test = Mp4File.parse(fileChannel)
	public static Mp4File parse(FileChannel fileChannel) throws Exception
	{
		Mp4File internal = new Mp4File(fileChannel);

		internal.internalParse();

		return internal;
	}

	// actually does the parsing
	private void internalParse() throws Exception
	{
		int offset = 0;
		topLevelAtoms = new java.util.ArrayList<Box>();
		
		// a hashmap to hold container atoms and their end offsets in order to manage nesting
		// linked hashmap keeps order when calling keyset 
		LinkedHashMap<ContainerBox, Integer> containerAtoms = new LinkedHashMap<ContainerBox, Integer>();

		while (offset < fileChannel.size())
		{
			Box atom = createAtom(offset);

			// some atoms can be in a container or top-level
			// flag to track if atom has been added to a container to prevent adding to top-level list
			boolean isInContainer = false;
			
			// error handling
			if (atom == null)
			{
				throw new Exception("Unknown atom found at offset " + offset);
			}
			if (atom.size() <= 0)
			{
				throw new Exception("Invalid atom size encountered at offset " + offset);
			}
			if (atom.size() + offset > fileChannel.size())
			{
				throw new Exception("Atom size exceeds file size at offset " + offset);
			}
			if (atom.name() == null || atom.name().isEmpty())
			{
				throw new Exception("Invalid atom name encountered at offset " + offset);
			}

			if (atom instanceof NestedAtom)
			{
				List<ContainerBox> containerAtomsList = new java.util.ArrayList<ContainerBox>(containerAtoms.keySet());
				List<ContainerBox> containerAtomsListReversed = new java.util.ArrayList<ContainerBox>();
				
				// reverse to check innermost containers first
				for (ContainerBox box : containerAtomsList)
				{
					containerAtomsListReversed.add(box); 
				}
				Collections.reverse(containerAtomsListReversed);
				
				// nesting management
				for (ContainerBox containerAtom : containerAtomsListReversed)
				{
					int containerEnd = containerAtoms.get(containerAtom);
					// stopping condition: offset is within container range and not the same atom
					if (offset < containerEnd && containerAtom != atom)
					{
						// atom is within container
						containerAtom.addAtom((NestedAtom) atom);
						isInContainer = true;
						break; // only add to the innermost container
					}
				}
			}
			
			if (atom instanceof TopLevelAtom && isInContainer == false)
			{
				topLevelAtoms.add(atom);
			}
			
			if (atom instanceof ContainerBox)
			{
				// add to container atom map with end offset
				containerAtoms.put((ContainerBox) atom, offset + atom.size());
			}
			
			// move offset
			if (atom instanceof Leaf)
			{
				offset = offset + atom.size();
			}
			// container atoms have 8 byte header, full boxes have 12 byte header
			// some atoms are both container and full box
			else if ((atom instanceof ContainerBox) && (atom instanceof FullBox))
			{
				offset = offset + 12;
			}
			else
			{
				offset = offset + 8;
			}
		}
		
		boolean recursiveParseFlag = true;
		for (Box topLevelAtom : topLevelAtoms)
		{
			if (topLevelAtom instanceof Leaf)
			{
				((Leaf) topLevelAtom).parse();
			}
			
			if (topLevelAtom instanceof ContainerBox)
			{
				((ContainerBox) topLevelAtom).parseChildren(recursiveParseFlag);;
			}
		}
	}
	
	public List<Box> topLevelAtoms() { return topLevelAtoms; }
}