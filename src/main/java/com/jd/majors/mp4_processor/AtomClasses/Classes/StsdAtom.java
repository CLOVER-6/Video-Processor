package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class StsdAtom implements FullAtom, NestedAtom, ContainerAtom 
{
	public class AvcAtom implements FullAtom, NestedAtom
	{
		private GeneralAtom parentAtom;
	    private final int size;
	    private final String name;
	    private final short version;
	    private final byte[] flags;
	    private final byte[] payload;

	    public AvcAtom(GeneralAtom parentAtom, int s, String n, short version, byte[] f, byte[] payload) 
	    {
	    	this.parentAtom = parentAtom;
	        this.size = s;
	        this.name = n;
	        this.version = version;
	        this.flags = f;
	        this.payload = payload;
	    }

	    public AvcAtom(int s, String n, byte[] payload) 
	    {
	    	this.parentAtom = null;
	        this.size = s;
	        this.name = n;
	        this.version = payload[0];
	        this.flags = Arrays.copyOfRange(payload, 1, 4);
	        this.payload = Arrays.copyOfRange(payload, 5, payload.length);
	    }

	    // TODO fill this out
	    public void parse() 
	    {

	    }
	    
	    public GeneralAtom parentAtom() { return parentAtom; }
	    public int size() { return size; }
	    public String name() { return name; }
	    public short version() { return version; }
	    public byte[] flags() { return flags; }
	    public byte[] payload() { return payload; }
	    
	    public void setParent(GeneralAtom atom)
	    {
	    	this.parentAtom = atom;
	    }
	}
	// avc atom end
	
	private GeneralAtom parentAtom;
    private final int size;
    private final String name;
    private final short version;
    private final byte[] flags;
    private int entryCount;
    private final List<GeneralAtom> sampleDescs;
    private final byte[] payload;

    public StsdAtom(GeneralAtom parentAtom, int s, String n, short version, byte[] f, int entryCount, List<GeneralAtom> sampleDescs) 
    {
    	this.parentAtom = parentAtom;
        this.size = s;
        this.name = n;
        this.version = version;
        this.flags = f;
        this.entryCount = entryCount;
        this.sampleDescs = sampleDescs;
        // not allowing instantiation with other types
        for (GeneralAtom sampleDesc : sampleDescs)
        {
        	if (!(sampleDesc instanceof AvcAtom))
        	{
        		throw new IllegalArgumentException();
        	}
        }
        this.payload = null;
    }

    public StsdAtom(int s, String n, short version, byte[] f, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = version;
        this.flags = f;
        this.entryCount = 0;
        this.sampleDescs = new ArrayList<GeneralAtom>();
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
        this.sampleDescs = new ArrayList<GeneralAtom>();
        this.payload = Arrays.copyOfRange(payload, 5, payload.length);
    }

    // check drefAtom for note on the privacy controls of this func
    public void addAtom(NestedAtom atom)
    {
    	if (!(atom instanceof AvcAtom)) 
    	{
    		throw new IllegalArgumentException();
    	}
    	
    	atom.setParent(atom);
    	sampleDescs.add(atom);
    }
    
    // TODO fill this out
    public void parse() 
    {

    }
    
    public GeneralAtom parentAtom() { return parentAtom; }
    public int size() { return size; }
    public String name() { return name; }
    public short version() { return version; }
    public byte[] flags() { return flags; }
    public int entryCount() { return entryCount; }
    public List<GeneralAtom> childAtoms() { return sampleDescs; }
    public byte[] payload() { return payload; }
    
    public void setParent(GeneralAtom atom)
    {
    	this.parentAtom = atom;
    }

	@Override
	public String toString() {
		return "StsdAtom [parentAtom=" + parentAtom + ", size=" + size + ", name=" + name + ", version=" + version
				+ ", flags=" + Arrays.toString(flags) + ", entryCount=" + entryCount + ", sampleDescs=" + sampleDescs
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + Objects.hash(entryCount, name, parentAtom, sampleDescs, size, version);
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
				&& Objects.equals(parentAtom, other.parentAtom) && Objects.equals(sampleDescs, other.sampleDescs)
				&& size == other.size && version == other.version;
	}
    
    
}
