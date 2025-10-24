package com.jd.majors.mp4_processor.Parsing;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;

public class FileParser 
{
	// this assumes the offset is set correctly at the start of the atom
	// throws error or causes logic issues if not, which is good imo. puts responsibility on function user
	public byte[] getRawAtom(FileChannel fileChannel, long offset) throws IOException
	{
		// can significantly speed reads
		MappedByteBuffer mappedBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, offset, fileChannel.size());
		
		// atom sizes are stored over 4 bytes
		int atomSize = 0;
		for (int i = 0; i < 4;  i++)
		{
			atomSize = atomSize + mappedBuffer.get();
		}
			
		mappedBuffer.position(0);
			
		// iterate over whole atom and gather
		byte[] atomData = new byte[atomSize];
		for (int i = 0; i < atomSize; i++)
		{
			atomData[i] = mappedBuffer.get();
		}
			
		return atomData;
	}

	public GeneralAtom createAtom(FileChannel fileChannel, long offset) throws IOException
	{
		byte[] rawAtom = getRawAtom(fileChannel, offset);

		// size, name, version, flags and payload finding for atom
		// version and flags only used for full atoms
		int size = 0;
		for (int i = 0; i < 4;  i++)
		{
			size = size + rawAtom[i];
		}
		String name = new String(Arrays.copyOfRange(rawAtom, 4, 8));
		byte[] payload = Arrays.copyOfRange(rawAtom, 8, rawAtom.length + 1);
		
		GeneralAtom atom = AtomRegistry.createAtom(size, name, payload);
		return atom;
	}
	
	private FileParser() {}
}
