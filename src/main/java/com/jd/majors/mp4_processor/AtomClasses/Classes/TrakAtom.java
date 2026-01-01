package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class TrakAtom implements ContainerAtom, NestedAtom 
{
	private GeneralAtom parentAtom;
    private final int size;
    private final String name;
    private List<GeneralAtom> childAtoms;

    public TrakAtom(GeneralAtom parentAtom, int size, String name, List<GeneralAtom> childAtoms) 
    {
    	this.parentAtom = parentAtom;
        this.size = size;
        this.name = name;
        this.childAtoms = childAtoms;
    }

    public TrakAtom(int size, String name, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = size;
        this.name = name;
        this.childAtoms = new ArrayList<GeneralAtom>();
    }

    public void addAtom(NestedAtom atom)
    {
    	atom.setParent(this);
    	childAtoms.add(atom);
    }
    
    public GeneralAtom parentAtom() { return parentAtom; }
    public int size() { return size; }
    public String name() { return name; }
    public List<GeneralAtom> childAtoms() { return childAtoms; }

    public void setParent(GeneralAtom atom) 
    {
    	this.parentAtom = atom;
    }
    
    @Override
    public String toString() 
    {
        return "TrakAtom [size=" + size + ", name=" + name + ", childAtoms=" + childAtoms + "]";
    }

    @Override
    public int hashCode() 
    {
        return Objects.hash(size, name, childAtoms);
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) return true;
        if (!(obj instanceof TrakAtom)) return false;
        TrakAtom other = (TrakAtom) obj;
        return size == other.size && Objects.equals(name, other.name) &&
               Objects.equals(childAtoms, other.childAtoms);
    }
}
