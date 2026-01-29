package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.TopLevelAtom;

public class MoovAtom implements TopLevelAtom, ContainerBox 
{
    private final int size;
    private final String name;
    private final List<Box> childAtoms;

    public MoovAtom(int size, String name, List<Box> childAtoms) {
        this.size = size;
        this.name = name;
        this.childAtoms = childAtoms;
    }

    public MoovAtom(int size, String name, byte[] payload) {
        this.size = size;
        this.name = name;
        this.childAtoms = new ArrayList<Box>();
    }
    
    public void addAtom(NestedAtom atom)
    {
    	atom.setParent(this);
    	childAtoms.add(atom);
    }

    public int size() { return size; }
    public String name() { return name; }
    public List<Box> childAtoms() { return childAtoms; }

    @Override
    public String toString() {
        return "MoovAtom [size=" + size + ", name=" + name + "]";
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
		MoovAtom other = (MoovAtom) obj;
		return Objects.equals(name, other.name) && size == other.size;
	}

   
}