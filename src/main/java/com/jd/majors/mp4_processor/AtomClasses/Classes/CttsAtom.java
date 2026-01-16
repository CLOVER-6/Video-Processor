package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class CttsAtom implements FullAtom, NestedAtom
{
	private GeneralAtom parentAtom;
    private final int size;
    private final String name;
    private final short version;
    private final byte[] flags;
    private int entryCount;
    private long[][] entries;
    private byte[] payload;
    
    public CttsAtom(GeneralAtom parentAtom, int size, String name, short version, byte[] flags, int entryCount, long[][] entries,
			byte[] payload) 
    {
		this.parentAtom = parentAtom;
		this.size = size;
		this.name = name;
		this.version = version;
		this.flags = flags;
		this.entryCount = entryCount;
		this.entries = entries;
		this.payload = null;
	}

	public CttsAtom(int s, String n, short version, byte[] f, byte[] payload) 
    {
		this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = version;
        this.flags = f;
        this.entryCount = 0;
        this.entries = null;
        this.payload = payload;
    }

    public CttsAtom(int s, String n, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = payload[0];
        this.entryCount =  0;
        this.entries = null;
        this.flags = Arrays.copyOfRange(payload, 1, 4);
        this.payload = Arrays.copyOfRange(payload, 4, payload.length);
    }
    
    public CttsAtom parse() throws Exception
    {
    	if (payload == null)
    	{
    		throw new Exception("Empty Payload - Cannot parse");
    	}
    	
    	int eightMultiple = 3;
    	for (int i = 0; i  < 4; i++)
    	{
    		entryCount = entryCount | (payload[i] & 0xFF) << 8 * eightMultiple;
    		eightMultiple = eightMultiple - 1;
    	}
    	
    	entries = new long[entryCount][2];
    	
    	int entryOffset = 4;
    	for (int i = 0; i < entryCount; i++)
    	{
    		eightMultiple = 3;
    		for (int j = entryOffset; j < entryOffset + 4; j++)
    		{
    			entries[i][0] = (payload[j] & 0xFF) << 8 * eightMultiple;
    			eightMultiple = eightMultiple - 1;
    		}
    		
    		eightMultiple = 3;
    		for (int j = entryOffset + 4; j < entryOffset + 8; j++)
    		{
    			entries[i][1] = (payload[j] & 0xFF) << 8 * eightMultiple;
    			eightMultiple = eightMultiple - 1;
    		}
    		
    		entryOffset = entryOffset + 8;
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
	public long[][] entries() { return entries; }
	public byte[] payload() { return payload; }

	public void setParent(GeneralAtom atom)
	{
		this.parentAtom = atom;
	}
	
	@Override
	public String toString() 
	{
		return "CttsAtom [size=" + size + ", name=" + name + ", version=" + version + ", flags="
				+ Arrays.toString(flags) + ", entryCount=" + entryCount + ", entries=" + Arrays.toString(entries) + "]";
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(entries);
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + Objects.hash(entryCount, name, size, version);
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
		CttsAtom other = (CttsAtom) obj;
		return Arrays.deepEquals(entries, other.entries) && entryCount == other.entryCount
				&& Arrays.equals(flags, other.flags) && Objects.equals(name, other.name) && size == other.size
				&& version == other.version;
	}
}
