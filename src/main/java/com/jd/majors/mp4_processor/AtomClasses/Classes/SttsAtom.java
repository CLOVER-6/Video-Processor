package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class SttsAtom implements FullAtom, NestedAtom
{
	private GeneralAtom parentAtom;
	private final int size;
	private final String name;
	private final short version;
	private final byte[] flags;
	private int entryCount;
	private int[][] sampleEntries;
	private byte[] payload;

	public SttsAtom(int size, String name, short version, byte[] flags, int entryCount,
			int[][] sampleEntries) 
	{
		this.size = size;
		this.name = name;
		this.version = version;
		this.flags = flags;
		this.entryCount = entryCount;
		this.sampleEntries = sampleEntries;
		this.payload = null;
	}

	public SttsAtom(int s, String n, short version, byte[] f, byte[] payload) 
	{
		this.size = s;
		this.name = n;
		this.version = version;
		this.flags = f;
		this.entryCount = 0;
		this.sampleEntries = null;
		this.payload = payload;
	}

	public SttsAtom(int s, String n, byte[] payload) 
	{
		this.parentAtom = null;
		this.size = s;
		this.name = n;
		this.version = payload[0];
		this.flags = Arrays.copyOfRange(payload, 1, 4);
		this.entryCount = 0;
		this.sampleEntries = null;
		this.payload = Arrays.copyOfRange(payload, 4, payload.length);
	}

	public SttsAtom parse() throws Exception
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

		sampleEntries = new int[entryCount][2];
		
		// push pointer away from entry count
		int atomOffset = 4;

		for (int i = 0; i < entryCount; i++)
		{
			// sample count
			eightMultiple = 3;
			for (int j = atomOffset; j < atomOffset + 4; j++) 
			{
				sampleEntries[i][0] = sampleEntries[i][0] | (payload[j] & 0xFF) << 8 * eightMultiple;
				eightMultiple = eightMultiple - 1;
			}

			// sample delta (length of samples)
			eightMultiple = 3;
			for (int j = atomOffset + 4; j < atomOffset + 8; j++) 
			{
				sampleEntries[i][1] = sampleEntries[i][1] | (payload[j] & 0xFF) << 8 * eightMultiple;
				eightMultiple = eightMultiple - 1;
			}
			
			atomOffset = atomOffset + 8;
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
	public int[][] sampleEntries() { return sampleEntries; }
	public byte[] payload() { return payload; }

	public void setParent(GeneralAtom atom)
	{
		this.parentAtom = atom;
	}

	@Override
	public String toString() {
		return "SttsAtom [parentAtom=" + parentAtom + ", size=" + size + ", name=" + name + ", version=" + version
				+ ", flags=" + Arrays.toString(flags) + ", entryCount=" + entryCount + ", sampleEntries="
				+ Arrays.toString(sampleEntries) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + Arrays.deepHashCode(sampleEntries);
		result = prime * result + Objects.hash(entryCount, name, parentAtom, size, version);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SttsAtom other = (SttsAtom) obj;
		return entryCount == other.entryCount && Arrays.equals(flags, other.flags) && Objects.equals(name, other.name)
				&& Objects.equals(parentAtom, other.parentAtom) && Arrays.deepEquals(sampleEntries, other.sampleEntries)
				&& size == other.size && version == other.version;
	}
}
