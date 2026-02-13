package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

/**
 * Representation of the 'edts' (edit list) container atom.
 *
 * <p>The edit list container groups edit-related child atoms such as {@code
 * elst} which define how a track maps segments of media to the presentation
 * timeline (edits, empty edit placeholders, and trimming information).</p>
 *
 * <p>As a container this class stores child atoms and exposes
 * {@code parseChildren(boolean)} to optionally recurse into nested containers
 * and to call {@code parse()} on leaf children. After parsing, implementations
 * typically clear raw payload buffers to reduce memory usage.</p>
 *
 * <p>Implementation details: this class implements {@code NestedAtom} and
 * {@code ContainerBox} and therefore maintains a parent reference; the
 * {@code size} field is treated as authoritative for on-disk byte layout.</p>
 */
public class EdtsAtom implements NestedAtom, ContainerBox 
{
	private Box parentAtom;
	private final int size;
	private final String name;
	private final List<Box> childAtoms;

	public EdtsAtom(int size, String name, List<Box> childAtoms) 
	{
		this.parentAtom = null;
		this.size = size;
		this.name = name;
		this.childAtoms = childAtoms;
	}

	public EdtsAtom(int size, String name, byte[] payload) 
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
		if (!(atom instanceof ElstAtom)) 
		{
			throw new IllegalArgumentException();
		}

		atom.setParent(this);
		childAtoms.add(atom);
	}

	@Override
	public String toString() 
	{
		return "EdtsAtom [size=" + size + ", name=" + name + "]";
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
		EdtsAtom other = (EdtsAtom) obj;
		return Objects.equals(name, other.name) && size == other.size;
	}
}