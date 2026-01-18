package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class VmhdAtom implements FullBox, NestedAtom, Leaf
{
	private Box parentAtom;
    private final int size;
    private final String name;
    private final short version;
    private final byte[] flags;
    private short graphicsMode;
    private short[] opcolours;
    private byte[] payload;

    public VmhdAtom(int size, String name, short version, byte[] flags, short graphicsMode, short[] opcolours,
			byte[] payload) {
		this.parentAtom = null;
		this.size = size;
		this.name = name;
		this.version = version;
		this.flags = flags;
		this.graphicsMode = graphicsMode;
		this.opcolours = opcolours;
		this.payload = null;
	}

	public VmhdAtom(int s, String n, short version, byte[] f, byte[] payload) 
    {
		this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = version;
        this.flags = f;
        this.graphicsMode = 0;
        this.opcolours = new short[3];
        this.payload = payload;
    }

    public VmhdAtom(int s, String n, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = payload[0];
        this.flags = Arrays.copyOfRange(payload, 1, 4);
        this.graphicsMode = 0;
        this.opcolours = new short[3];
        this.payload = Arrays.copyOfRange(payload, 4, payload.length);
    }
    
    // TODO fill this out
    public VmhdAtom parse() throws Exception
    {
    	if (payload == null)
    	{
    		throw new Exception("Empty Payload - Cannot parse");
    	}
    	
    	graphicsMode = (short) ((payload[0] & 0xFF) << 8 | payload[1] & 0xFF);
    	
    	opcolours[0] = (short) ((payload[2] & 0xFF) << 8 | payload[3] & 0xFF);
    	opcolours[1] = (short) ((payload[4] & 0xFF) << 8 | payload[5] & 0xFF);
    	opcolours[2] = (short) ((payload[6] & 0xFF) << 8 | payload[7] & 0xFF);
    	
    	payload = null;
    	
    	return this;
    }

    public Box parentAtom() { return parentAtom; }
    public int size() { return size; }
    public String name() { return name; }
    public short version() { return version; }
    public byte[] flags() { return flags; }
    public short graphicsMode() { return graphicsMode; }
    public short[] opcolours() { return opcolours; }
    public byte[] payload() { return payload; }

    public void setParent(Box atom)
    {
    	this.parentAtom = atom;
    }
    
	@Override
	public String toString() 
	{
		return "VmhdAtom [size=" + size + ", name=" + name + ", version=" + version + ", flags="
				+ Arrays.toString(flags) + ", graphicsMode=" + graphicsMode + ", opcolours="
				+ Arrays.toString(opcolours) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + Arrays.hashCode(opcolours);
		result = prime * result + Objects.hash(graphicsMode, name, size, version);
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
		VmhdAtom other = (VmhdAtom) obj;
		return Arrays.equals(flags, other.flags) && graphicsMode == other.graphicsMode
				&& Objects.equals(name, other.name) && Arrays.equals(opcolours, other.opcolours) && size == other.size
				&& version == other.version;
	}

    
}
