package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class SmhdAtom implements FullBox, NestedAtom, Leaf
{
	private Box parentAtom;
    private final int size;
    private final String name;
    private final short version;
    private final byte[] flags;
    private byte[] payload;

    public SmhdAtom(int s, String n, short version, byte[] f, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = version;
        this.flags = f;
        this.payload = null;
    }

    public SmhdAtom(int s, String n, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = payload[0];
        this.flags = Arrays.copyOfRange(payload, 1, 4);
        this.payload = Arrays.copyOfRange(payload, 4, payload.length);
    }

    public SmhdAtom parse() throws Exception
    {
        if (payload == null)
        {
            throw new Exception("Empty Payload - Cannot parse");
        }
        // parsing logic would go here
        // audio shit
        payload = null;
        return this;
    }    
    
    public Box parentAtom() { return parentAtom; }
    public int size() { return size; }
    public String name() { return name; }
    public short version() { return version; }
    public byte[] flags() { return flags; }
    public byte[] payload() { return payload; }

    public void setParent(Box atom)
    {
    	this.parentAtom = atom;
    }
    
    @Override
    public String toString() 
    {
        return "SmhdAtom [size=" + size + ", name=" + name + ", version=" + version +
               ", flags=" + Arrays.toString(flags) + "]";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + Objects.hash(name, size, version);
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
		SmhdAtom other = (SmhdAtom) obj;
		return Arrays.equals(flags, other.flags) && Objects.equals(name, other.name) && size == other.size
				&& version == other.version;
	}

	
}