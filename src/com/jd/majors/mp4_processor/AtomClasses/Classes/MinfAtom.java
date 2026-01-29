package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class MinfAtom implements ContainerBox, NestedAtom 
{
	private Box parentAtom;
    private final int size;
    private final String name;
    private final List<Box> childAtoms;

    public MinfAtom(int size, String name, List<Box> childAtoms) 
    {
    	this.parentAtom = null;
        this.size = size;
        this.name = name;
        this.childAtoms = childAtoms;
    }

    public MinfAtom(int size, String name, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = size;
        this.name = name;
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
    public List<Box> childAtoms() { return childAtoms; }

    public void setParent(Box atom)
    {
    	this.parentAtom = atom;
    }
    
    @Override
    public String toString() 
    {
        return "MinfAtom [size=" + size + ", name=" + name + "]";
    }

	@Override
	public int hashCode() 
	{
		return Objects.hash(name, size);
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
		MinfAtom other = (MinfAtom) obj;
		return Objects.equals(name, other.name) && size == other.size;
	}

   
}