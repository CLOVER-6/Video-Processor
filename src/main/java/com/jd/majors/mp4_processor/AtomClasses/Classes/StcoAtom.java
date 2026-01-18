package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class StcoAtom implements FullBox, NestedAtom, Leaf
{
	private Box parentAtom;
    private final int size;
    private final String name;
    private final short version;
    private final byte[] flags;
    private int entryCount;
    private int[] chunkOffsets;
    private byte[] payload;

    public StcoAtom(int size, String name, short version, byte[] flags, int entryCount, int[] chunkOffsets)
    {
		this.parentAtom = null;
		this.size = size;
		this.name = name;
		this.version = version;
		this.flags = flags;
		this.entryCount = entryCount;
		this.chunkOffsets = chunkOffsets;
		this.payload = null;
	}

	public StcoAtom(int s, String n, short version, byte[] f, byte[] payload) 
    {
		this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = version;
        this.flags = f;
        this.payload = payload;
    }

    public StcoAtom(int s, String n, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = payload[0];
        this.flags = Arrays.copyOfRange(payload, 1, 4);
        this.payload = Arrays.copyOfRange(payload, 4, payload.length);
    }

    // TODO fill this out
    public StcoAtom parse() throws Exception
    {
    	if (payload == null)
		{
			throw new Exception("Empty Payload - Cannot parse");
		}
	    
    	int eightMultiple = 3;
		for (int i = 0; i < 4; i++)
		{
			entryCount = entryCount | (payload[i] & 0xFF) << 8 * eightMultiple;
			eightMultiple = eightMultiple - 1;
		}

		chunkOffsets = new int[entryCount];
		
		// push pointer away from entry count
		int atomOffset = 4;

		for (int i = 0; i < entryCount; i++)
		{
			eightMultiple = 3;
			for (int j = atomOffset; j < atomOffset + 4; j++) 
			{
				chunkOffsets[i] = chunkOffsets[i] | (payload[j] & 0xFF) << 8 * eightMultiple;
				eightMultiple = eightMultiple - 1;
			}
			
			atomOffset = atomOffset + 4;
		}
		
		payload = null;
    	
    	return this;
    }
    
    public Box parentAtom() { return parentAtom; }
    public int size() { return size; }
    public String name() { return name; }
    public short version() { return version; }
    public byte[] flags() { return flags; }
    public int entryCount() { return entryCount; }
    public int[] chunkOffsets() { return chunkOffsets; }
    public byte[] payload() { return payload; }

    public void setParent(Box atom)
    {
    	this.parentAtom = atom;
    }

	@Override
	public String toString() 
	{
		return "StcoAtom [size=" + size + ", name=" + name + ", version=" + version
				+ ", flags=" + Arrays.toString(flags) + ", entryCount=" + entryCount + ", chunkOffsets="
				+ Arrays.toString(chunkOffsets) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(chunkOffsets);
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + Objects.hash(entryCount, name, size, version);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StcoAtom other = (StcoAtom) obj;
		return Arrays.equals(chunkOffsets, other.chunkOffsets) && entryCount == other.entryCount
				&& Arrays.equals(flags, other.flags) && Objects.equals(name, other.name) && size == other.size
				&& version == other.version;
	}

	
}