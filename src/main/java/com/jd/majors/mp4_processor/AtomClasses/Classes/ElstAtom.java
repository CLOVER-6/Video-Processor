package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class ElstAtom implements FullAtom, NestedAtom
{
	private GeneralAtom parentAtom;
    private final int size;
    private final String name;
    private final short version;
    private final byte[] flags;
    private int entryCount;
    private long[][] entries;
    private byte[] payload;

    public ElstAtom(GeneralAtom parentAtom, int size, String name, short version, byte[] flags, int entryCount, long[][] entries,
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

	public ElstAtom(int s, String n, short version, byte[] f, byte[] payload) 
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

    public ElstAtom(int s, String n, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = payload[0];
        this.flags = Arrays.copyOfRange(payload, 1, 4);
        this.entryCount = 0;
        this.entries = null;
        this.payload = Arrays.copyOfRange(payload, 4, payload.length);
    }
    
    public ElstAtom parse() throws Exception
    {
    	if (payload == null)
    	{
    		throw new Exception("Empty Payload - Cannot parse");
    	}
    	
    	int eightMultiple = 3;
        for (int i = 0; i < 4; i++)
        {
        	// bytes are inherently signed in java so "& 0xFF" unsigns
        	// << left shifts by decrementing multiples of 8 to place bits in respective spots
        	// using long because java ints are signed whilst minor version is unsigned
        	entryCount = entryCount | (payload[i] & 0xFF) << 8 * eightMultiple;
        	eightMultiple = eightMultiple - 1;
        }
        
        entries = new long[entryCount][4];
        eightMultiple = 3;
        int entriesPointer = 0;
        for (int i = 4; i < (entryCount * 12) + 4; i = i + 12)
        {
        	// segment duration
        	for (int j = 0; j < 4; j++)
        	{
        		entries[entriesPointer][0] = entries[entriesPointer][0] | (payload[i + j] & 0xFF) << 8 * eightMultiple;
        		eightMultiple = eightMultiple - 1;
        	}
        		
        	// media time
        	eightMultiple = 3;
        	for (int j = 4; j < 8; j++)
        	{
        		entries[entriesPointer][1] = entries[entriesPointer][1] | (payload[i + j] & 0xFF) << 8 * eightMultiple;
        		eightMultiple = eightMultiple - 1;
        	}
        		
        	// media rate
        	eightMultiple = 1;
        	for (int j = 8; j < 10; j++)
        	{
        		entries[entriesPointer][2] = entries[entriesPointer][2] | (payload[i + j] & 0xFF) << 8 * eightMultiple;
        		eightMultiple = eightMultiple - 1;
        	}
        		
        	// media rate fraction
        	eightMultiple = 1;
        	for (int j = 10; j < 12; j++)
        	{
        		entries[entriesPointer][3] = entries[entriesPointer][3] | (payload[i + j] & 0xFF) << 8 * eightMultiple;
        		eightMultiple = eightMultiple - 1;
        	}
        	
        	entriesPointer = entriesPointer + 1;
        }
        
        // ensure payload cannot be re-parsed
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

	public void setParent(GeneralAtom atom)
	{
		this.parentAtom = atom;
	}
	
	@Override
	public String toString() 
	{
		return "ElstAtom [parentAtom=" + parentAtom + ", size=" + size + ", name=" + name + ", version=" + version + ", flags="
				+ Arrays.toString(flags) + ", entryCount=" + entryCount + ", entries=" + Arrays.toString(entries) + "]";
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(entries);
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + Objects.hash(entryCount, name, parentAtom, size, version);
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
		ElstAtom other = (ElstAtom) obj;
		return Arrays.deepEquals(entries, other.entries) && entryCount == other.entryCount
				&& Arrays.equals(flags, other.flags) && Objects.equals(name, other.name) && size == other.size
				&& version == other.version && Objects.equals(parentAtom, other.parentAtom);
	}

    
}
