package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class SmhdAtom implements FullAtom, NestedAtom
{
	private GeneralAtom parentAtom;
    private final int size;
    private final String name;
    private final short version;
    private final byte[] flags;
    private byte[] payload;

    public SmhdAtom(GeneralAtom parentAtom, int s, String n, short version, byte[] f, byte[] payload) 
    {
    	this.parentAtom = parentAtom;
        this.size = s;
        this.name = n;
        this.version = version;
        this.flags = f;
        this.payload = null; // full-value constructor should not keep payload
    }

    public SmhdAtom(int s, String n, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = payload[0];
        this.flags = Arrays.copyOfRange(payload, 1, 4);
        this.payload = Arrays.copyOfRange(payload, 4, payload.length); // fixed start index
    }

    public SmhdAtom parse() throws Exception
    {
        if (payload == null)
        {
            throw new Exception("Empty Payload - Cannot parse");
        }
        // no additional parsing here; clear payload
        payload = null;
        return this;
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
    
    @Override
    public String toString() 
    {
        return "SmhdAtom [parentAtom=" + parentAtom + ", size=" + size + ", name=" + name + ", version=" + version +
               ", flags=" + Arrays.toString(flags) + "]";
    }

    @Override
    public int hashCode() 
    {
        return Objects.hash(size, name, version, Arrays.hashCode(flags));
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) return true;
        if (!(obj instanceof SmhdAtom)) return false;
        SmhdAtom other = (SmhdAtom) obj;
        return size == other.size && version == other.version && Arrays.equals(flags, other.flags)
            && Objects.equals(name, other.name);
    }
}