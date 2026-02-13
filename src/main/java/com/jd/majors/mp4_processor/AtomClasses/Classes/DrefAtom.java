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
import com.jd.majors.mp4_processor.Parsing.AtomRegistry;

/**
 * Representation of the 'dref' (data reference) full box and its contained
 * reference entries.
 *
 * <p>The {@code dref} box holds a table of data reference entries (typically
 * {@code url } or {@code urn} child atoms) that indicate the storage location
 * of media data for a track. Each entry is itself an atom; therefore this
 * class behaves as both a container and (in some constructors) as a leaf
 * until the payload is parsed into child atoms.</p>
 *
 * <p>{@code parseChildren(boolean)} will iterate child atoms and optionally
 * recurse into nested containers. The raw {@code payload} is cleared after
 * parsing to prevent re-parsing and to reduce memory footprint.</p>
 *
 * <p>Implementation notes: this class implements {@code FullBox},
 * {@code ContainerBox}, {@code NestedAtom} and {@code Leaf} to support both
 * pre-parsed and on-demand parsing workflows; callers should supply or set
 * the parent when attaching instances to a container.</p>
 */
public class DrefAtom implements FullBox, ContainerBox, NestedAtom, Leaf
{
	private Box parentAtom;
	private final int size;
	private final String name;
	private final short version;
	private final byte[] flags;
	private int entryCount;
	private List<Box> dataReferences;
	private byte[] payload;

	public DrefAtom(Box parentAtom, int size, String name, short version, byte[] flags, int entryCount, List<Box> dataReferences) 
	{
		this.parentAtom = parentAtom;
		this.size = size;
		this.name = name;
		this.version = version;
		this.flags = flags;
		this.entryCount = entryCount;
		this.dataReferences = dataReferences;
		this.payload = null;
	}

	public DrefAtom(int s, String n, short version, byte[] f, byte[] payload) 
	{
		this.parentAtom = null;
		this.size = s;
		this.name = n;
		this.version = version;
		this.flags = f;
		this.entryCount = 0;
		this.dataReferences = new ArrayList<Box>();
		this.payload = payload;
	}

	public DrefAtom(int s, String n, byte[] payload) 
	{
		this.parentAtom = null;
		this.size = s;
		this.name = n;
		this.version = payload[0];
		this.flags = Arrays.copyOfRange(payload, 1, 4);
		this.entryCount = 0;
		this.dataReferences = new ArrayList<Box>();
		this.payload = Arrays.copyOfRange(payload, 4, payload.length);
	}

	public DrefAtom parse() throws Exception
	{
		if (payload == null)
		{
			throw new Exception("Empty Payload - Cannot parse");
		}

		int eightMultiple = 3;
		for (int i = 0; i < 4; i++)
		{
			entryCount = entryCount | (payload[i] & 0xFF) << 8 * eightMultiple;
			eightMultiple = eightMultiple - 1;
		}

		int dataRefSize = 0;
		String dataRefName = "";
		byte[] dataRefPayload = null;
		Box dataRef = null;

		int atomOffset = 4;
		for (int i = 0; i < entryCount; i++)
		{
			// reset per-entry temp
			dataRefSize = 0;
			int localEight = 3;
			for (int j = atomOffset; j < atomOffset + 4; j++)
			{
				dataRefSize = dataRefSize | (payload[j] & 0xFF) << 8 * localEight;
				localEight = localEight - 1;
			}

			dataRefName = new String(Arrays.copyOfRange(payload, atomOffset + 4, atomOffset + 8));
			dataRefPayload = Arrays.copyOfRange(payload, atomOffset + 8, atomOffset + dataRefSize);

			dataRef = AtomRegistry.createAtom(dataRefSize, dataRefName, dataRefPayload);

			if (dataRef instanceof NestedAtom)
			{
				this.addAtom((NestedAtom) dataRef);
			}

			atomOffset = atomOffset + dataRefSize;
		}

		payload = null;

		return this;
	}

	// flag to signify if a parse of children container is wanted too
	public void parseChildren(boolean recursiveParseFlag) throws Exception
	{
		// guard
		if (this.dataReferences == null || this.dataReferences.isEmpty())
		{
			return;
		}

		for (Box childAtom : this.dataReferences)
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
	public int entryCount() { return entryCount; }
	public List<Box> childAtoms() { return dataReferences; }
	public byte[] payload() { return payload; }

	public void setParent(Box atom)
	{
		this.parentAtom = atom;
	}

	// atm, this being public is an issue because urls are contained and set in file
	// but, i am extending this to be writing mp4s too in future
	public void addAtom(NestedAtom atom)
	{
		if (!(atom instanceof UrxAtom)) 
		{
			throw new IllegalArgumentException();
		}

		atom.setParent(this);
		dataReferences.add(atom);
	}

	@Override
	public String toString() {
		return "DrefAtom [size=" + size + ", name=" + name + ", version=" + version
				+ ", flags=" + Arrays.toString(flags) + ", entryCount=" + entryCount + "]";
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + Objects.hash(entryCount, name, size, version);
		return result;
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
		DrefAtom other = (DrefAtom) obj;
		return entryCount == other.entryCount && Arrays.equals(flags, other.flags) && Objects.equals(name, other.name)
				&& size == other.size && version == other.version;
	}   
}