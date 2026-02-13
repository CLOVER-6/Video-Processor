package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

/**
 * Representation of the 'mdia' (media) container which groups media-related
 * boxes for a track such as {@code mdhd}, {@code hdlr} and {@code minf}.
 *
 * <p>The media container provides track-specific metadata and references used
 * by decoders and demuxers (timescale, handler information and the media
 * information container).</p>
 *
 * <p>As a container the class exposes {@code parseChildren(boolean)} to
 * recurse into contained atoms and to call {@code parse()} on leaf children
 * when required. Implementations should clear raw payloads after parsing to
 * reduce memory footprint.</p>
 *
 * <p>Implementation notes: this class implements {@code ContainerBox} and
 * {@code NestedAtom} and therefore maintains a parent pointer; the
 * {@code size} field includes the header bytes and is authoritative.</p>
 */
public class MdiaAtom implements ContainerBox, NestedAtom
{
	private Box parentAtom;
	private final int size;
	private final String name;
	private final List<Box> childAtoms;

	public MdiaAtom(int size, String name, List<Box> childAtoms) 
	{
		this.parentAtom = null;
		this.size = size;
		this.name = name;
		this.childAtoms = childAtoms;
	}

	public MdiaAtom(int size, String name, byte[] payload) 
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
		return "MdiaAtom [size=" + size + ", name=" + name + "]";
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
		MdiaAtom other = (MdiaAtom) obj;
		return Objects.equals(name, other.name) && size == other.size;
	}


}