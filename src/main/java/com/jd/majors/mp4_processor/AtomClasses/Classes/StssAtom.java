package com.jd.majors.mp4_processor.AtomClasses.Classes;


import java.util.Arrays;
import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class StssAtom implements FullAtom, NestedAtom 
{
	private GeneralAtom parentAtom;
    private final int size;
    private final String name;
    private final short version;
    private final byte[] flags;
    private int entryCount;
    private int[] sampleIndexes;
    private byte[] payload;
    
    public StssAtom(int size, String name, short version, byte[] flags, int entryCount,
			int[] sampleIndexes) 
    {
		this.size = size;
		this.name = name;
		this.version = version;
		this.flags = flags;
		this.entryCount = entryCount;
		this.sampleIndexes = sampleIndexes;
		this.payload = null;
	}

	public StssAtom(int s, String n, short version, byte[] f, byte[] payload) 
    {
        this.size = s;
        this.name = n;
        this.version = version;
        this.flags = f;
        this.entryCount = 0;
        this.sampleIndexes =  null;
        this.payload = payload;
    }

    public StssAtom(int s, String n, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = payload[0];
        this.flags = Arrays.copyOfRange(payload, 1, 4);
        this.entryCount = 0;
        this.sampleIndexes =  null;
        this.payload = Arrays.copyOfRange(payload, 4, payload.length);
    }

    // TODO fill this out
    public StssAtom parse() throws Exception
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
        sampleIndexes = new int[entryCount];

        // push pointer away from entry count
    	int atomOffset = 4;
    	
    	for (int i = 0; i < entryCount; i++)
        {
        	eightMultiple = 3;
        	for (int j = atomOffset; j < atomOffset + 4; j++) 
            {
        		sampleIndexes[i] = sampleIndexes[i] | (payload[j] & 0xFF) << 8 * eightMultiple;
            	eightMultiple = eightMultiple - 1;
    		}
        	// advance atomOffset to next 4-byte entry
        	atomOffset += 4;
        }
    	
    	payload = null;
    	
    	return this;
    }
    
    public GeneralAtom parentAtom() { return parentAtom; }
    public int size() { return size; }
    public String name() { return name; }
    public short version() { return version; }
    public byte[] flags() { return flags; }
    public int entryCount() { return entryCount; }
    public int[] sampleIndexes() { return sampleIndexes; }
    public byte[] payload() { return payload; }

    public void setParent(GeneralAtom atom)
    {
    	this.parentAtom = atom;
    }

	@Override
	public String toString() {
		return "StssAtom [parentAtom=" + parentAtom + ", size=" + size + ", name=" + name + ", version=" + version
				+ ", flags=" + Arrays.toString(flags) + ", entryCount=" + entryCount + ", sampleIndexes="
				+ Arrays.toString(sampleIndexes) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + Arrays.hashCode(sampleIndexes);
		result = prime * result + Objects.hash(entryCount, name, parentAtom, size, version);
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
		StssAtom other = (StssAtom) obj;
		return entryCount == other.entryCount && Arrays.equals(flags, other.flags) && Objects.equals(name, other.name)
				&& Objects.equals(parentAtom, other.parentAtom) && Arrays.equals(sampleIndexes, other.sampleIndexes)
				&& size == other.size && version == other.version;
	}
}