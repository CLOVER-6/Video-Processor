package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

/**
 * Representation of the 'iods' (initial object descriptor) full box used in
 * some MP4 files to carry object descriptor information for systems that use
 * MPEG-4 Systems descriptors alongside media tracks.
 *
 * <p>The box contains a binary object descriptor blob which is semantically
 * opaque to generic media decoders but necessary for some authoring and
 * streaming systems that interoperate with MPEG-4 Systems.</p>
 *
 * <p>This class stores the raw {@code payload} until {@code parse()} is
 * invoked; parsing currently discards the payload after validation to avoid
 * holding the raw descriptor when only higher-level metadata is needed.</p>
 *
 * <p>Implementation notes: this is a {@code FullBox} and a {@code NestedAtom};
 * {@code version} and {@code flags} are preserved and callers should set the
 * parent reference when attaching the atom to a container.</p>
 */
public class IodsAtom implements Leaf, NestedAtom, FullBox
{
	private Box parentAtom;
	private final int size;
	private final String name;
	private final short version;
	private final byte[] flags;
	private byte[] payload;

	public IodsAtom(int size, String name, short version, byte[] flags,
			byte[] payload) 
	{
		this.parentAtom = null;
		this.size = size;
		this.name = name;
		this.version = version;
		this.flags = flags;
		this.payload = null;
	}

	
	public IodsAtom(int s, String n, byte[] payload) 
	{
		this.parentAtom = null;
		this.size = s;
		this.name = n;
		this.version = payload[0];
		this.flags = Arrays.copyOfRange(payload, 1, 4);
		this.payload = Arrays.copyOfRange(payload, 4, payload.length);
	}

	// TODO fill this out
	public IodsAtom parse() throws Exception 
	{
		if (payload == null)
		{
			throw new Exception("Empty Payload - Cannot parse");
		}
		payload = null;

		return this;
	}

	public Box parentAtom() { return parentAtom; }
	public int size() { return size; }
	public String name() { return name; }
	public short version() { return version; }
	public byte[] flags() { return flags; }
	public byte[] payload() { return payload; }

	public void setParent(Box atom)
	{
		this.parentAtom = atom;
	}

}