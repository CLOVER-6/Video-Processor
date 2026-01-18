package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;
import com.jd.majors.mp4_processor.Parsing.AtomRegistry;

/**
 * Sample description atom (stsd).
 *
 * Notes:
 * - Contains a list of sample description atoms (e.g. AVC codec boxes).
 * - This implementation enforces that sample descriptions are `AvcAtom` instances.
 * - The internal payload is cleared after parsing to prevent re-parsing.
 */
public class StsdAtom implements FullBox, NestedAtom, ContainerBox , Leaf
{
	private Box parentAtom;
    private final int size;
    private final String name;
    private final short version;
    private final byte[] flags;
    private int entryCount;
    private final List<Box> sampleDescs;
    private byte[] payload;

    public StsdAtom(Box parentAtom, int s, String n, short version, byte[] f, int entryCount, List<Box> sampleDescs, byte[] payload) 
    {
    	this.parentAtom = parentAtom;
        this.size = s;
        this.name = n;
        this.version = version;
        this.flags = f;
        this.entryCount = entryCount;
        this.sampleDescs = sampleDescs;
        // not allowing instantiation with other types
        for (Box sampleDesc : sampleDescs)
        {
        	if (!(sampleDesc instanceof AvcAtom))
        	{
        		throw new IllegalArgumentException();
        	}
        }
        this.payload = null;
    }

    public StsdAtom(int s, String n, short version, byte[] flags, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = version;
        this.flags = flags;
        this.entryCount = 0;
        this.sampleDescs = new ArrayList<Box>();
        this.payload = payload;
    }
    
    public StsdAtom(int s, String n, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = payload[0];
        this.flags = Arrays.copyOfRange(payload, 1, 4);
        this.entryCount = 0;
        this.sampleDescs = new ArrayList<Box>();
        this.payload = Arrays.copyOfRange(payload, 4, payload.length);
    }

    // check drefAtom for note on the privacy controls of this func
    public void addAtom(NestedAtom atom)
    {
    	if (!(atom instanceof AvcAtom)) 
    	{
    		throw new IllegalArgumentException();
    	}
    	
    	atom.setParent(this);
    	sampleDescs.add(atom);
    }
    
    // TODO fill this out
    public StsdAtom parse() throws Exception
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

        // push pointer away from entry count
    	int atomOffset = 4;
        
        int sampleDecSize = 0;
        String sampleDecName = "";
        byte[] sampleDecPayload = null;
        Box sampleDec = null;
       
        for (int i = 0; i < entryCount; i++)
        {
        	eightMultiple = 3;
        	for (int j = atomOffset; j < atomOffset + 4; j++) 
            {
        		sampleDecSize = sampleDecSize | (payload[j] & 0xFF) << 8 * eightMultiple;
            	eightMultiple = eightMultiple - 1;
        	}
        	
        	sampleDecName = new String(Arrays.copyOfRange(payload, atomOffset + 4, atomOffset + 8));
        	sampleDecPayload = Arrays.copyOfRange(payload, atomOffset + 8, sampleDecSize + atomOffset);
        	sampleDec = AtomRegistry.createAtom(sampleDecSize, sampleDecName, sampleDecPayload);
        	
//        	if (sampleDec instanceof FullAtom)
//        	{
//        		((FullAtom) sampleDec).parse();
//        	}
        	
        	if (sampleDec instanceof NestedAtom)
        	{
        		this.addAtom((NestedAtom) sampleDec);
        	}
        	
        	atomOffset = atomOffset + sampleDecSize;
        }
        
    	// ensure payload is nulled so subsequent parse calls can't re-parse unexpectedly
    	payload = null;
    	
    	return this;
    }
    
    public Box parentAtom() { return parentAtom; }
    public int size() { return size; }
    public String name() { return name; }
    public short version() { return version; }
    public byte[] flags() { return flags; }
    public int entryCount() { return entryCount; }
    public List<Box> childAtoms() { return sampleDescs; }
    public byte[] payload() { return payload; }
    
    public void setParent(Box atom)
    {
    	this.parentAtom = atom;
    }

	@Override
	public String toString() {
		return "StsdAtom [size=" + size + ", name=" + name + ", version=" + version
				+ ", flags=" + Arrays.toString(flags) + ", entryCount=" + entryCount + ", sampleDescs=" + sampleDescs
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + Objects.hash(entryCount, name, sampleDescs, size, version);
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
		StsdAtom other = (StsdAtom) obj;
		return entryCount == other.entryCount && Arrays.equals(flags, other.flags) && Objects.equals(name, other.name)
				&& Objects.equals(sampleDescs, other.sampleDescs) && size == other.size && version == other.version;
	}

	
}