package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class StscAtom implements FullAtom, NestedAtom
{
	private GeneralAtom parentAtom;
	private final int size;
	private final String name;
	private final short version;
	private final byte[] flags;
	private int entryCount;
	private int[][] chunks;
	private byte[] payload;

	public StscAtom(int size, String name, short version, byte[] flags, int entryCount, int[][] chunks) 
	{
		this.parentAtom = null;
		this.size = size;
		this.name = name;
		this.version = version;
		this.flags = flags;
		this.entryCount = entryCount;
		this.chunks = chunks;
		this.payload = null;
	}

	public StscAtom(int s, String n, short version, byte[] f, byte[] payload) 
	{
		this.parentAtom = null;
		this.size = s;
		this.name = n;
		this.version = version;
		this.flags = f;
		this.payload = payload;
	}

	public StscAtom(int s, String n, byte[] payload) 
	{
		this.parentAtom = null;
		this.size = s;
		this.name = n;
		this.version = payload[0];
		this.flags = Arrays.copyOfRange(payload, 1, 4);
		this.payload = Arrays.copyOfRange(payload, 4, payload.length);
	}

	// TODO fill this out
	public StscAtom parse() throws Exception
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

		chunks = new int[entryCount][3];

		// push pointer away from entry count
		int atomOffset = 4;

		for (int i = 0; i < entryCount; i++)
		{
			// first chunk index
			eightMultiple = 3;
			for (int j = atomOffset; j < atomOffset + 4; j++) 
			{
				chunks[i][0] = chunks[i][0] | (payload[j] & 0xFF) << 8 * eightMultiple;
				eightMultiple = eightMultiple - 1;
			}

			// number of samples
			eightMultiple = 3;
			for (int j = atomOffset + 4; j < atomOffset + 8; j++) 
			{
				chunks[i][1] = chunks[i][1] | (payload[j] & 0xFF) << 8 * eightMultiple;
				eightMultiple = eightMultiple - 1;
			}

			// sample description index that applies to chunk
			eightMultiple = 3;
			for (int j = atomOffset + 8; j < atomOffset + 12; j++) 
			{
				chunks[i][2] = chunks[i][2] | (payload[j] & 0xFF) << 8 * eightMultiple;
				eightMultiple = eightMultiple - 1;
			}

			// advance the atomOffset for the next entry (each entry is 12 bytes)
			atomOffset += 12;
		}

		payload = null;
		
		return this;
	}

	public GeneralAtom parentAtom() { return parentAtom; }
	public int size() { return size; }
	public String name() { return name; }
	public short version() { return version; }
	public byte[] flags() { return flags; }
	public int entryCount() { return entryCount; }
	public int[][] chunks() { return chunks; }
	public byte[] payload() { return payload; }

	public void setParent(GeneralAtom atom)
	{
		this.parentAtom = atom;
	}

	@Override
	public String toString() 
	{
		return "StscAtom [parentAtom=" + parentAtom + ", size=" + size + ", name=" + name + ", version=" + version
				+ ", flags=" + Arrays.toString(flags) + ", entryCount=" + entryCount + ", chunks="
				+ Arrays.toString(chunks) + "]";
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(chunks);
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + Objects.hash(entryCount, name, parentAtom, size, version);
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
		StscAtom other = (StscAtom) obj;
		return Arrays.deepEquals(chunks, other.chunks) && entryCount == other.entryCount
				&& Arrays.equals(flags, other.flags) && Objects.equals(name, other.name)
				&& Objects.equals(parentAtom, other.parentAtom) && size == other.size && version == other.version;
	}
}