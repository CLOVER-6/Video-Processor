package com.jd.majors.mp4_processor.Parsing;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;

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
	
	public Mp4File(FileChannel fileChannel)
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
	public GeneralAtom createAtom(long offset) throws IOException
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

		GeneralAtom atom = AtomRegistry.createAtom(size, name, payload);
		return atom;
	}
	
	
}