package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class MetaAtom implements ContainerBox, NestedAtom, FullBox
{
	private Box parentAtom;
    private final int size;
    private final String name;
    private final short version;
    private final byte[] flags;
    private final List<Box> childAtoms;

    public MetaAtom(int size, String name, short version, byte[] flags, List<Box> childAtoms) 
    {
        this.parentAtom = null;
    	this.size = size;
        this.name = name;
        this.version = version;
        this.flags = flags;
        this.childAtoms = childAtoms;
    }

    public MetaAtom(int size, String name, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = size;
        this.name = name;
        this.version = payload[0];
        this.flags = Arrays.copyOfRange(payload, 1, 4);
        this.childAtoms = new ArrayList<Box>();
    }

    public void addAtom(NestedAtom atom)
    {
    	atom.setParent(this);
    	childAtoms.add(atom);
    }
    
    public Box parentAtom() { return parentAtom; }
    public int size() { return size; }
    public String name() { return name; }
    public short version() { return version; }
    public byte[] flags() { return flags; }
    public List<Box> childAtoms() { return childAtoms; }

    public void setParent(Box atom)
    {
    	this.parentAtom = atom;
    }
    
    @Override
    public String toString() 
    {
        return "MetaAtom [size=" + size + ", name=" + name + "]";
    }

	@Override
	public int hashCode() {
		return Objects.hash(name, size);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MetaAtom other = (MetaAtom) obj;
		return Objects.equals(name, other.name) && size == other.size;
	}

   
}