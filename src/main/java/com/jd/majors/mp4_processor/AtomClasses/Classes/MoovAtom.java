package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.TopLevelAtom;

/**
 * Representation of the top-level 'moov' (movie) container atom which groups
 * all movie-level metadata (tracks, movie header, and related containers).
 *
 * <p>The {@code moov} container contains child atoms such as {@code mvhd}
 * (movie header) and one or more {@code trak} (track) containers. It is the
 * canonical location for metadata needed before or during playback.</p>
 *
 * <p>As a container this class exposes {@code parseChildren(boolean)} to
 * recurse into child containers and to call {@code parse()} on leaf children
 * when desired. Implementations should set parent references for child atoms
 * when they are added.</p>
 *
 * <p>Implementation notes: treat the {@code size} field as authoritative and
 * prefer clearing raw payloads for parsed child atoms to reduce memory
 * retention.</p>
 */
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

	public int size() { return size; }
	public String name() { return name; }
	public List<Box> childAtoms() { return childAtoms; }

	public void addAtom(NestedAtom atom) throws Exception
	{
		atom.setParent(this);
		childAtoms.add(atom);
	}

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