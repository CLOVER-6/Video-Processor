package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class Avc1Atom implements NestedAtom, Leaf
{
	private Box parentAtom;
	private final int size;
	private final String name;
	private short dataReferenceIndex;
	private int width;
	private int height;
	private long horizresolution;
	private long vertresolution;
	private short frameCount;
	private String compressorName;
	private short depth;
	private byte[] payload;

	public Avc1Atom(int size, String name, short dataReferenceIndex, int width, int height, 
			long horizresolution, long vertresolution, short frameCount, String compressorName, short depth, byte[] payload)
	{
		this.parentAtom = null;
		this.size = size;
		this.name = name;
		this.dataReferenceIndex = dataReferenceIndex;
		this.width = width;
		this.height = height;
		this.horizresolution = horizresolution;
		this.vertresolution = vertresolution;
		this.frameCount = frameCount;
		this.compressorName = compressorName;
		this.depth = depth;
		this.payload = null;
	}

	public Avc1Atom(int s, String n, byte[] payload) 
	{
		this.parentAtom = null;
		this.size = s;
		this.name = n;
		this.payload = payload;
	}

	public Avc1Atom parse() throws Exception
	{
		if (payload == null)
		{
			throw new Exception("Empty Payload - Cannot parse");
		}
		
		dataReferenceIndex = (short) (((payload[6] & 0xFF) << 8) | (payload[7] & 0xFF));
		width = ((payload[24] & 0xFF) << 8) | (payload[25] & 0xFF);
		height = ((payload[26] & 0xFF) << 8) | (payload[27] & 0xFF);        

		int eightMultiple = 3;
		for (int i = 28; i < 32; i++)
		{
			horizresolution = horizresolution | (long) (payload[i] & 0xFF) << 8 * eightMultiple;
			eightMultiple = eightMultiple - 1;
		}

		eightMultiple = 3;
		for (int i = 32; i < 36; i++)
		{
			vertresolution = vertresolution | (long) (payload[i] & 0xFF) << 8 * eightMultiple;
			eightMultiple = eightMultiple - 1;
		}

		frameCount = (short) (((payload[40] & 0xFF) << 8) | (payload[41] & 0xFF));

		// compressor name is stored as a Pascal string with a length and max length of 31 bytes + 1 byte for length
		compressorName = new String(Arrays.copyOfRange(payload, 43, 43 + payload[42]), StandardCharsets.ISO_8859_1);

		depth = (short) (((payload[74] & 0xFF) << 8) | (payload[75] & 0xFF));

		payload = null;
		return this;
	}

	public Box parentAtom() { return parentAtom; }
	public int size() { return size; }
	public String name() { return name; }
	public short dataReferenceIndex() { return dataReferenceIndex; }
	public int width() { return width; }
	public int height() { return height; }
	public long horizresolution() { return horizresolution; }
	public long vertresolution() { return vertresolution; }
	public short frameCount() { return frameCount; }
	public String compressorName() { return compressorName; }
	public short depth() { return depth; }
	public byte[] payload() { return payload; }

	public void setParent(Box atom)
	{
		this.parentAtom = atom;
	}

	@Override
	public String toString() 
	{
		return "Avc1Atom [size=" + size + ", name=" + name + ", dataReferenceIndex=" + dataReferenceIndex + ", width="
				+ width + ", height=" + height + ", horizresolution=" + horizresolution + ", vertresolution="
				+ vertresolution + ", frameCount=" + frameCount + ", compressorName=" + compressorName + ", depth="
				+ depth + ", payload=" + Arrays.toString(payload) + "]";
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(payload);
		result = prime * result + Objects.hash(compressorName, dataReferenceIndex, depth, frameCount, height,
				horizresolution, name, size, vertresolution, width);
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
		Avc1Atom other = (Avc1Atom) obj;
		return Objects.equals(compressorName, other.compressorName) && dataReferenceIndex == other.dataReferenceIndex
				&& depth == other.depth && frameCount == other.frameCount && height == other.height
				&& horizresolution == other.horizresolution && Objects.equals(name, other.name)
				&& Arrays.equals(payload, other.payload) && size == other.size && vertresolution == other.vertresolution
				&& width == other.width;
	}
}