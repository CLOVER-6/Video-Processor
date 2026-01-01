package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.TopLevelAtom;

public class MoovAtom implements TopLevelAtom, ContainerAtom 
{
    private final int size;
    private final String name;
    private final List<GeneralAtom> childAtoms;

    public MoovAtom(int size, String name, List<GeneralAtom> childAtoms) {
        this.size = size;
        this.name = name;
        this.childAtoms = childAtoms;
    }

    public MoovAtom(int size, String name, byte[] payload) {
        this.size = size;
        this.name = name;
        this.childAtoms = new ArrayList<GeneralAtom>();
    }
    
    public void addAtom(NestedAtom atom)
    {
    	atom.setParent(this);
    	childAtoms.add(atom);
    }

    public int size() { return size; }
    public String name() { return name; }
    public List<GeneralAtom> childAtoms() { return childAtoms; }

    @Override
    public String toString() {
        return "MoovAtom [size=" + size + ", name=" + name + ", childAtoms=" + childAtoms + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, name, childAtoms);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MoovAtom)) return false;
        MoovAtom other = (MoovAtom) obj;
        return size == other.size && Objects.equals(name, other.name) &&
               Objects.equals(childAtoms, other.childAtoms);
    }
}
