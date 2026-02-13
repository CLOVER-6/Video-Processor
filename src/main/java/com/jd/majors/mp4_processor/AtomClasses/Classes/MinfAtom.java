package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

/**
 * Representation of the 'minf' (media information) container atom which
 * groups media-subsystem atoms such as sample table, data information and
 * handlers needed to interpret track samples.
 *
 * <p>The {@code minf} container organizes track-level boxes (for example
 * {@code stbl}, {@code dinf}, {@code vmhd}/{@code smhd}) that describe how the
 * media samples for the track are structured and where they are stored.</p>
 *
 * <p>As a container this class provides {@code parseChildren(boolean)} to
 * optionally recurse into nested containers and to call {@code parse()} on
 * leaf children. Clearing raw payload buffers after parsing is recommended to
 * avoid retaining large intermediate byte arrays.</p>
 *
 * <p>Implementation notes: this atom implements {@code ContainerBox} and
 * {@code NestedAtom} and maintains parent linkage when child atoms are added.
 * The {@code size} field is authoritative for on-disk layout.</p>
 */
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