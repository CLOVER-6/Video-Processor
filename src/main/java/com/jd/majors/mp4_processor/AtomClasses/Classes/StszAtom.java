package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class StszAtom implements FullAtom, NestedAtom
{
	private GeneralAtom parentAtom;
	private final int size;
	private final String name;
	private final short version;
	private final byte[] flags;
	private int sampleSize;
	private int entryCount;
	private int[] sampleSizes;
	private byte[] payload;

	public StszAtom(int size, String name, short version, byte[] flags, int sampleSize,
				int entryCount, int[] sampleSizes) 
	{
		this.parentAtom = null;
		this.size = size;
		this.name = name;
		this.version = version;
		this.flags = flags;
		this.sampleSize = sampleSize;
		this.entryCount = entryCount;
		this.sampleSizes = sampleSizes;
		this.payload = null;
	}

	public StszAtom(int s, String n, short version, byte[] f, byte[] payload) 
	{
		this.size = s;
		this.name = n;
		this.version = version;
		this.flags = f;
		this.sampleSize = 0;
		this.entryCount = 0;
		this.sampleSizes = null;
		this.payload = payload;
	}

	public StszAtom(int s, String n, byte[] payload) 
	{
		this.parentAtom = null;
		this.size = s;
		this.name = n;
		this.version = payload[0];
		this.flags = Arrays.copyOfRange(payload, 1, 4);
		this.payload = Arrays.copyOfRange(payload, 4, payload.length);
	}

	// TODO fill this out
	public StszAtom parse() throws Exception
	{
		if (payload == null)
	    {
	    	throw new Exception("Empty Payload - Cannot parse");
	    }
		
		int eightMultiple = 3;
		for (int i = 0; i < 4; i++) 
		{
			sampleSize = sampleSize | (payload[i] & 0xFF) << 8 * eightMultiple;
			eightMultiple = eightMultiple - 1;
		}

		// move pointer away from sample size
		int atomOffset = 4;
		
		if (sampleSize == 0)
		{
			eightMultiple = 3;
			for (int i = atomOffset; i < atomOffset + 4; i++) 
			{
				entryCount = entryCount | (payload[i] & 0xFF) << 8 * eightMultiple;
				eightMultiple = eightMultiple - 1;
			}
			
			sampleSizes = new int[entryCount];
			
			// move pointer away from entry count
			atomOffset = atomOffset + 4;
			
			for (int i = 0; i < entryCount; i++)
			{
				// reset eightMultiple for each 4-byte read
				eightMultiple = 3;
				for (int j = atomOffset; j < atomOffset + 4; j++) 
				{
					sampleSizes[i] = sampleSizes[i] | (payload[j] & 0xFF) << 8 * eightMultiple;
					eightMultiple = eightMultiple - 1;
				}
				
				// push pointer ahead to new entry
				atomOffset = atomOffset + 4;
			}
		}
		
		payload = null;
		
		return this;
	}

	public GeneralAtom parentAtom() { return parentAtom; }
	public int size() { return size; }
	public String name() { return name; }
	public short version() { return version; }
	public byte[] flags() { return flags; }
	public int sampleSize() { return sampleSize; }
	public int entryCount() { return entryCount; }
	public int[] sampleSizes() { return sampleSizes; }
	public byte[] payload() { return payload; }

	public void setParent(GeneralAtom atom)
	{
		this.parentAtom = atom;
	}

	@Override
	public String toString() 
	{
		return "StszAtom [parentAtom=" + parentAtom + ", size=" + size + ", name=" + name + ", version=" + version
				+ ", flags=" + Arrays.toString(flags) + ", sampleSize=" + sampleSize + ", entryCount=" + entryCount
				+ ", sampleSizes=" + Arrays.toString(sampleSizes) + "]";
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + Arrays.hashCode(sampleSizes);
		result = prime * result + Objects.hash(entryCount, name, parentAtom, sampleSize, size, version);
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StszAtom other = (StszAtom) obj;
		return entryCount == other.entryCount && Arrays.equals(flags, other.flags) && Objects.equals(name, other.name)
				&& Objects.equals(parentAtom, other.parentAtom) && sampleSize == other.sampleSize
				&& Arrays.equals(sampleSizes, other.sampleSizes) && size == other.size && version == other.version;
	}
}