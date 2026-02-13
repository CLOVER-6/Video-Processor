package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

/**
 * Representation of the 'meta' metadata container which groups file- or track-
 * level metadata atoms and item lists ({@code ilst}).
 *
 * <p>The {@code meta} box is used to carry descriptive metadata (title, artist,
 * artwork, etc.) and commonly contains an {@code ilst} child that in turn
 * contains individual metadata entries. This class preserves {@code version}
 * and {@code flags} per the full box format.</p>
 *
 * <p>As a container it exposes {@code parseChildren(boolean)} to recurse into
 * child atoms and to call {@code parse()} on leaf children. Implementations
 * should clear raw payload buffers after materializing children.</p>
 *
 * <p>Implementation notes: this class implements {@code FullBox},
 * {@code ContainerBox} and {@code NestedAtom} and maintains parent linkage.
 * The {@code size} field is authoritative for on-disk layout.</p>
 */
public class MetaAtom implements ContainerBox, NestedAtom, FullBox
{
	private Box parentAtom;
	private final int size;
	private final String name;
	private final short version;
	private final byte[] flags;
	private final List<Box> childAtoms;

	public MetaAtom(int size, String name, short version, byte[] flags, List<Box> childAtoms) 
	{
		this.parentAtom = null;
		this.size = size;
		this.name = name;
		this.version = version;
		this.flags = flags;
		this.childAtoms = childAtoms;
	}

	public MetaAtom(int size, String name, byte[] payload) 
	{
		this.parentAtom = null;
		this.size = size;
		this.name = name;
		this.version = payload[0];
		this.flags = Arrays.copyOfRange(payload, 1, 4);
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
	public short version() { return version; }
	public byte[] flags() { return flags; }
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
		return "MetaAtom [size=" + size + ", name=" + name + "]";
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
		MetaAtom other = (MetaAtom) obj;
		return Objects.equals(name, other.name) && size == other.size;
	}


}