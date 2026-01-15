package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;
import com.jd.majors.mp4_processor.Parsing.AtomRegistry;

public class StsdAtom implements FullAtom, NestedAtom, ContainerAtom 
{
	private GeneralAtom parentAtom;
    private final int size;
    private final String name;
    private final short version;
    private final byte[] flags;
    private int entryCount;
    private final byte[] payload;
    private final List<GeneralAtom> sampleDescs;

    public StsdAtom(GeneralAtom parentAtom, int s, String n, short version, byte[] f, int entryCount, List<GeneralAtom> sampleDescs, byte[] payload) 
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
        this.payload = payload;
    }

    public StsdAtom(int s, String n, short version, byte[] flags, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = version;
        this.flags = flags;
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
    	
        int sampleDecSize = 0;
        String sampleDecName = "";
        byte[] sampleDecPayload = null;
        FullAtom sampleDec = null;
        
        for (int i = 0; i < entryCount; i++)
        {
        	int atomOffset = 4;
        	eightMultiple = 3;
        	for (int j = atomOffset; j < atomOffset + 4; i++) 
            {
        		sampleDecSize = sampleDecSize | (payload[i] & 0xFF) << 8 * eightMultiple;
            	eightMultiple = eightMultiple - 1;
    		}
        	
        	sampleDecName = new String(Arrays.copyOfRange(payload, atomOffset + 4, atomOffset + 8));
        	sampleDecPayload = Arrays.copyOfRange(payload, atomOffset + 8, sampleDecSize + atomOffset);
        	sampleDec = (FullAtom) AtomRegistry.createAtom(sampleDecSize, sampleDecName, sampleDecPayload);
        	
        	sampleDec = sampleDec.parse();
        	
        	atomOffset = atomOffset + sampleDecSize;
        }
        
    	return this;
    }
    
    public GeneralAtom parentAtom() { return parentAtom; }
    public int size() { return size; }
    public String name() { return name; }
    public short version() { return version; }
    public byte[] flags() { return flags; }
    public byte[] payload() { return payload; }
    public List<GeneralAtom> childAtoms() { return sampleDescs; }
    
    public void setParent(GeneralAtom atom)
    {
    	this.parentAtom = atom;
    }
    
    @Override
    public String toString() 
    {
        return "StsdAtom [size=" + size + ", name=" + name + ", version=" + version +
               ", flags=" + flags + ", payloadLength=" + (payload != null ? payload.length : 0) + "]";
    }

    @Override
    public int hashCode() 
    {
        return Objects.hash(size, name, version, Arrays.hashCode(flags), java.util.Arrays.hashCode(payload));
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) return true;
        if (!(obj instanceof StsdAtom)) return false;
        StsdAtom other = (StsdAtom) obj;
        return size == other.size && version == other.version && Arrays.equals(flags, other.flags)
            && Objects.equals(name, other.name) && java.util.Arrays.equals(payload, other.payload);
    }
}
