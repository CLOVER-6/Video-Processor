package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

/**
 * Representation of the 'dinf' (data information) container atom.
 *
 * <p>This container groups data-reference related child atoms (for example
 * {@code dref}) that describe where track media data is stored. It acts as a
 * logical grouping used by track/media handler implementations.</p>
 *
 * <p>As a container this class holds a {@code List<Box>} of child atoms and
 * implements {@code parseChildren(boolean)} to optionally recurse into child
 * containers and invoke {@code parse()} on leaf children. Callers may choose
 * whether child containers are parsed recursively.</p>
 *
 * <p>Implementation details: this class implements {@code NestedAtom} and
 * {@code ContainerBox}; parent linkage should be set when the container is
 * attached. The {@code size} field includes the container header and is
 * authoritative for on-disk layout.</p>
 */
public class DinfAtom implements NestedAtom, ContainerBox 
{
	private Box parentAtom;
    private final int size;
    private final String name;
    private final List<Box> childAtoms;

    public DinfAtom(int size, String name, List<Box> childAtoms) 
    {
    	this.parentAtom = null;
        this.size = size;
        this.name = name;
        this.childAtoms = childAtoms;
    }

    public DinfAtom(int size, String name, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = size;
        this.name = name;
        this.childAtoms = new ArrayList<Box>();
    }

    // flag to signify if a parse of children container is wanted too
    public void parseChildren(boolean recursiveParseFlag) throws Exception
    {
    	// guard
    	if (this.childAtoms == null || this.childAtoms.isEmpty())
    	{
    		return;
    	}
    	
    	for (Box childAtom : this.childAtoms)
    	{
    		if (childAtom instanceof Leaf)
    		{
    			((Leaf) childAtom).parse();
    		}
    		
    		if (childAtom instanceof ContainerBox && recursiveParseFlag)
    		{
    			((ContainerBox) childAtom).parseChildren(recursiveParseFlag);
    		}
    	}
    }
    
    public Box parentAtom() { return parentAtom; }
    public int size() { return size; }
    public String name() { return name; }
    public List<Box> childAtoms() { return childAtoms; }

    public void setParent(Box atom)
	{
		this.parentAtom = atom;
	}

    public void addAtom(NestedAtom atom) throws Exception
    {
    	atom.setParent(this);
    	childAtoms.add(atom);
    }
    
	@Override
	public String toString() 
	{
		return "DinfAtom [size=" + size + ", name=" + name + "]";
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
		DinfAtom other = (DinfAtom) obj;
		return Objects.equals(name, other.name) && size == other.size;
	}

	
}