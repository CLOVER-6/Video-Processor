package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;
import com.jd.majors.mp4_processor.Parsing.AtomRegistry;

/**
 * Data reference atom (dref).
 *
 * Notes:
 * - Contains references to other data atoms (url/urn boxes).
 * - Parses children using `AtomRegistry.createAtom` and integrates them into the tree.
 * - Payload is cleared after parsing to prevent re-parsing.
 */
public class DrefAtom implements FullAtom, ContainerAtom, NestedAtom
{
	private GeneralAtom parentAtom;
    private final int size;
    private final String name;
    private final short version;
    private final byte[] flags;
    private int entryCount;
    private List<GeneralAtom> dataReferences;
    private byte[] payload;

    public DrefAtom(GeneralAtom parentAtom, int size, String name, short version, byte[] flags, int entryCount, List<GeneralAtom> dataReferences) 
    {
    	this.parentAtom = parentAtom;
		this.size = size;
		this.name = name;
		this.version = version;
		this.flags = flags;
		this.entryCount = entryCount;
		this.dataReferences = dataReferences;
		this.payload = null;
	}

	public DrefAtom(int s, String n, short version, byte[] f, byte[] payload) 
    {
		this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = version;
        this.flags = f;
        this.entryCount = 0;
        this.dataReferences = null;
        this.payload = payload;
    }

    public DrefAtom(int s, String n, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = payload[0];
        this.flags = Arrays.copyOfRange(payload, 1, 4);
        this.entryCount = 0;
        this.dataReferences = null;
        this.payload = Arrays.copyOfRange(payload, 4, payload.length);
    }

    // atm, this being public is an issue because urls are contained and set in file
    // but, i am extending this to be writing mp4s too in future
    public void addAtom(NestedAtom atom)
    {
    	if (!(atom instanceof UrxAtom)) 
    	{
    		throw new IllegalArgumentException();
    	}
    	
    	atom.setParent(this);
    	dataReferences.add(atom);
    }
    
    public DrefAtom parse() throws Exception
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

    	int dataRefSize = 0;
    	String dataRefName = "";
    	byte[] dataRefPayload = null;
    	GeneralAtom dataRef = null;
    	
    	int atomOffset = 4;
    	for (int i = 0; i < entryCount; i++)
    	{
    		// reset per-entry temp
    		dataRefSize = 0;
    		int localEight = 3;
    		for (int j = atomOffset; j < atomOffset + 4; j++)
    		{
    			dataRefSize = dataRefSize | (payload[j] & 0xFF) << 8 * localEight;
    			localEight = localEight - 1;
    		}
    		
    		dataRefName = new String(Arrays.copyOfRange(payload, atomOffset + 4, atomOffset + 8));
    		dataRefPayload = Arrays.copyOfRange(payload, atomOffset + 8, atomOffset + dataRefSize);
    		
    		dataRef = AtomRegistry.createAtom(dataRefSize, dataRefName, dataRefPayload);
    		
    		if (dataRef instanceof FullAtom)
    		{
    			dataRef = ((FullAtom) dataRef).parse();
    		}
    		
    		if (dataRef instanceof NestedAtom)
    		{
    			this.addAtom((NestedAtom) dataRef);
    		}
    			
    		atomOffset = atomOffset + dataRefSize;
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
    public List<GeneralAtom> childAtoms() { return dataReferences; }
    public byte[] payload() { return payload; }
	
    public void setParent(GeneralAtom atom)
    {
    	this.parentAtom = atom;
    }
    
	@Override
	public String toString() {
		return "DrefAtom [parentAtom=" + parentAtom + ", size=" + size + ", name=" + name + ", version=" + version
				+ ", flags=" + Arrays.toString(flags) + ", entryCount=" + entryCount + ", dataReferences="
				+ dataReferences + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + Objects.hash(dataReferences, entryCount, name, parentAtom, size, version);
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
		DrefAtom other = (DrefAtom) obj;
		return Objects.equals(dataReferences, other.dataReferences) && entryCount == other.entryCount
				&& Arrays.equals(flags, other.flags) && Objects.equals(name, other.name)
				&& Objects.equals(parentAtom, other.parentAtom) && size == other.size && version == other.version;
	}
}