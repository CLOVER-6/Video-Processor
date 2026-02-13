package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

/**
 * Representation of the 'ilst' (item list) metadata container used in the
 * metadata (meta) atom to group individual metadata items.
 *
 * <p>The {@code ilst} container holds a sequence of child atoms that each
 * represent a metadata entry (for example title, artist or artwork). Parsers
 * use the contained child atoms to construct high-level metadata structures.</p>
 *
 * <p>As a container this class provides {@code parseChildren(boolean)} to
 * optionally recurse into nested containers and to call {@code parse()} on
 * leaf children. Implementations should clear raw payload buffers after
 * materializing children to avoid duplicate data retention.</p>
 *
 * <p>Implementation notes: this atom implements {@code ContainerBox} and
 * {@code NestedAtom} and therefore maintains and exposes a parent reference.</p>
 */
public class IlstAtom implements ContainerBox, NestedAtom
{
	private Box parentAtom;
	private final int size;
	private final String name;
	private final List<Box> childAtoms;

	public IlstAtom(int size, String name, List<Box> childAtoms) 
	{
		this.parentAtom = null;
		this.size = size;
		this.name = name;
		this.childAtoms = childAtoms;
	}

	public IlstAtom(int size, String name, byte[] payload) 
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
		return "IlstAtom [size=" + size + ", name=" + name + "]";
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
		IlstAtom other = (IlstAtom) obj;
		return Objects.equals(name, other.name) && size == other.size;
	}
}