package com.jd.majors.mp4_processor.Parsing;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.TopLevelAtom;

/**
 * Mp4File is a lightweight reader for extracting raw atom byte ranges from
 * an open FileChannel and for constructing atom instances via the
 * AtomRegistry. It exposes a convenience `createAtom` that reads a single
 * atom at the provided file offset and returns an un-parsed atom object.
 *
 * Implementation notes:
 * - getRawAtom uses memory-mapping (MappedByteBuffer) for performance on
 *   large files. Consumers must ensure offsets passed in are correct; the
 *   method assumes the offset points to the start of an MP4 atom.
 */
public class Mp4File
{
	private final FileChannel fileChannel;
	private List<Box> topLevelAtoms;

	private Mp4File(FileChannel fileChannel)
	{
		this.fileChannel = fileChannel;
	}

	// this assumes the offset is set correctly at the start of the atom
	// throws error or causes logic issues if not, which is good imo. puts responsibility on function user
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

		String name = new String(Arrays.copyOfRange(rawAtom, 4, 8));
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
		HashMap<ContainerBox, Integer> containerAtoms = new HashMap<ContainerBox, Integer>();

		while (offset < fileChannel.size())
		{
			Box atom = createAtom(offset);

			// error handling
			if (atom == null)
			{
				break;
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

			if (atom instanceof Leaf)
			{
				((Leaf) atom).parse();
			}
			if (atom instanceof TopLevelAtom)
			{
				topLevelAtoms.add(atom);
			}
			if (atom instanceof ContainerBox)
			{
				// add to container atom map with end offset
				containerAtoms.put((ContainerBox) atom, offset + atom.size());
			}
			if (atom instanceof NestedAtom)
			{
				List<ContainerBox> containerAtomsList = new java.util.ArrayList<ContainerBox>(containerAtoms.keySet());
				containerAtomsList = containerAtomsList.reversed(); // reverse to check innermost containers first
				// nesting management
				for (ContainerBox containerAtom : containerAtomsList)
				{
					int containerEnd = containerAtoms.get(containerAtom);
					// stopping condition: offset is within container range and not the same atom
					if (offset < containerEnd && containerAtom != atom)
					{
						// atom is within container
						containerAtom.addAtom((NestedAtom) atom);
						break; // only add to the innermost container
					}
				}
			}

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
	}
	
	public List<Box> topLevelAtoms() { return topLevelAtoms; }
}