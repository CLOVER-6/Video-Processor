package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

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
    public AvcAtom parse() 
    {
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
}
