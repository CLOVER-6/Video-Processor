package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class Co64Atom implements FullBox, NestedAtom, Leaf
{
	private Box parentAtom;
	private final int size;
	private final String name;
	private final short version;
	private final byte[] flags;
	private int entryCount;
	private long[] chunkOffsets;
	private byte[] payload;

	public Co64Atom(int size, String name, short version, byte[] flags, int entryCount, long[] chunkOffsets) 
	{
		this.parentAtom = null;
		this.size = size;
		this.name = name;
		this.version = version;
		this.flags = flags;
		this.entryCount = entryCount;
		this.chunkOffsets = chunkOffsets;
		this.payload = null;
	}

	public Co64Atom(int s, String n, short version, byte[] f, byte[] payload) 
	{
		this.parentAtom = null;
		this.size = s;
		this.name = n;
		this.version = version;
		this.flags = f;
		this.entryCount = 0;
		this.chunkOffsets = null;
		this.payload = payload;
	}

	public Co64Atom(int s, String n, byte[] payload) 
	{
		this.parentAtom = null;
		this.size = s;
		this.name = n;
		this.version = payload[0];
		this.flags = Arrays.copyOfRange(payload, 1, 4);
		this.entryCount = 0;
		this.chunkOffsets = null;
		this.payload = Arrays.copyOfRange(payload, 4, payload.length);
	}

	// TODO fill this out
	public Co64Atom parse() throws Exception
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

		chunkOffsets = new long[entryCount];

		// push pointer away from entry count
		int atomOffset = 4;

		for (int i = 0; i < entryCount; i++)
		{
			int localEight = 7; // 8 bytes -> shift 7..0
			for (int j = atomOffset; j < atomOffset + 8; j++) 
			{
				chunkOffsets[i] = chunkOffsets[i] | ((long)(payload[j] & 0xFF) << (8 * localEight));
				localEight = localEight - 1;
			}

			atomOffset = atomOffset + 8;
		}

		payload = null;

		return this;
	}

	public Box parentAtom() { return parentAtom; }
	public int size() { return size; }
	public String name() { return name; }
	public short version() { return version; }
	public byte[] flags() { return flags; }
	public int entryCount() { return entryCount; }
	public long[] chunkOffsets() { return chunkOffsets; }
	public byte[] payload() { return payload; }

	public void setParent(Box atom)
	{
		this.parentAtom = atom;
	}

	@Override
	public String toString() 
	{
		return "Co64Atom [size=" + size + ", name=" + name + ", version=" + version
				+ ", flags=" + Arrays.toString(flags) + ", entryCount=" + entryCount + ", chunkOffsets="
				+ Arrays.toString(chunkOffsets) + "]";
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(chunkOffsets);
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + entryCount;
		result = prime * result + Objects.hash(name, size, version);
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
		Co64Atom other = (Co64Atom) obj;
		return Arrays.equals(chunkOffsets, other.chunkOffsets) && Arrays.equals(flags, other.flags)
				&& entryCount == other.entryCount && Objects.equals(name, other.name) && size == other.size
				&& version == other.version;
	}
}