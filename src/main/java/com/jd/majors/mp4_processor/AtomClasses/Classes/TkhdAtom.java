package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class TkhdAtom implements FullAtom, NestedAtom 
{
	private GeneralAtom parentAtom;
    private final int size;
    private final String name;
    private final short version;
    private final byte[] flags;
    private long creationTime;
    private long modificationTime;
    private long trackID;
    private long duration;
    private int layer;
    private int alternateGroup;
    private int volume;
    private byte[][] matrix;
    private long width;
    private long height;
    private byte[] payload;

	public TkhdAtom(GeneralAtom parentAtom, int size, String name, short version, byte[] flags, long creationTime, long modificationTime,
			long trackID, long duration, int layer, int alternateGroup, int volume, byte[][] matrix, long width,
			long height, byte[] payload) 
	{
		this.parentAtom = parentAtom;
		this.size = size;
		this.name = name;
		this.version = version;
		this.flags = flags;
		this.creationTime = creationTime;
		this.modificationTime = modificationTime;
		this.trackID = trackID;
		this.duration = duration;
		this.layer = layer;
		this.alternateGroup = alternateGroup;
		this.volume = volume;
		this.matrix = matrix;
		this.width = width;
		this.height = height;
		this.payload = null;
	}

	public TkhdAtom(int s, String n, short version, byte[] f, byte[] payload) 
    {
		this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = version;
        this.flags = f;
		this.creationTime = 0;
		this.modificationTime = 0;
		this.trackID = 0;
		this.duration = 0;
		this.layer = 0;
		this.alternateGroup = 0;
		this.volume = 0;
		this.matrix = new byte[9][4];
		this.width = 0;
		this.height = 0;
        this.payload = payload;
    }

    public TkhdAtom(int s, String n, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = payload[0];
        this.flags = Arrays.copyOfRange(payload, 1, 4);
		this.creationTime = 0;
		this.modificationTime = 0;
		this.trackID = 0;
		this.duration = 0;
		this.layer = 0;
		this.alternateGroup = 0;
		this.volume = 0;
		this.matrix = new byte[9][4];
		this.width = 0;
		this.height = 0;
        this.payload = Arrays.copyOfRange(payload, 4, payload.length);
    }

    public TkhdAtom parse() throws Exception
    {
    	if (payload == null)
    	{
    		throw new Exception("Empty Payload - Cannot parse");
    	}
    	
    	// version one makes creation time, modification time, and duration 8 bytes.
    	// this is cleanest way ive worked out so far
    	int eightMultiple = (version == 0) ? 3 : 7;
        for (int i = 0; i < ((version == 0) ? 4 : 8); i++)
        {
        	// bytes are inherently signed in java so "& 0xFF" unsigns
        	// << left shifts by decrementing multiples of 8 to place bits in respective spots
        	// using long because java ints are signed whilst minor version is unsigned
        	creationTime = creationTime | (payload[i] & 0xFF) << 8 * eightMultiple;
        	eightMultiple = eightMultiple - 1;
        } 

        eightMultiple = (version == 0) ? 3 : 7;
        for (int i = (version == 0) ? 4 : 8 ; i < ((version == 0) ? 8 : 16); i++)
        {
        	modificationTime = modificationTime | (payload[i] & 0xFF) << 8 * eightMultiple;
        	eightMultiple = eightMultiple - 1;
        }
      
        eightMultiple = 3;
        for (int i = (version == 0) ? 8 : 16; i < ((version == 0) ? 12 : 20); i++)
        {
        	trackID = trackID | (payload[i] & 0xFF) << 8 * eightMultiple;
        	eightMultiple = eightMultiple - 1;
        }
        
        eightMultiple = (version == 0) ? 3 : 7;
        for (int i = (version == 0) ? 16 : 24; i < ((version == 0) ? 20 : 32); i++)
        {
        	duration = duration | (payload[i] & 0xFF) << 8 * eightMultiple;
        	eightMultiple = eightMultiple - 1;
        }
        
        layer = (payload[((version == 0) ? 28 : 40)] & 0xFF) << 8 | (payload[((version == 0) ? 29 : 41)] & 0xFF);
        alternateGroup = (payload[((version == 0) ? 30 : 42)] & 0xFF) << 8 | (payload[((version == 0) ? 31 : 43)] & 0xFF);
        volume = (payload[((version == 0) ? 32 : 44)] & 0xFF) << 8 | (payload[((version == 0) ? 33 : 45)] & 0xFF);
        
        int matrixPointer = 0;
        for (int i = (version == 0) ? 36 : 48; i < ((version == 0) ? 72 : 84); i = i + 4)
        {
        	matrix[matrixPointer] = Arrays.copyOfRange(payload, i, i + 4);
        	matrixPointer = matrixPointer + 1;
        }
        
        eightMultiple = 3;
        for (int i = (version == 0) ? 72 : 84; i < ((version == 0) ? 76 : 88); i++)
        {
        	width = width | (payload[i] & 0xFF) << 8 * eightMultiple;
        	eightMultiple = eightMultiple - 1;
    	}
        
        eightMultiple = 3;
        for (int i = (version == 0) ? 76 : 88; i < ((version == 0) ? 80 : 92); i++)
        {
        	height = height | (payload[i] & 0xFF) << 8 * eightMultiple;
        	eightMultiple = eightMultiple - 1;
    	}
    	
    	// dont allow atom to be parsed multiple times
    	payload = null;
    	
    	return this;
    }

    public GeneralAtom parentAtom() { return parentAtom; }
	public int size() { return size; }
	public String name() { return name; }
	public short version() { return version; }
	public byte[] flags() { return flags; }
	public long creationTime() { return creationTime; }
	public long modificationTime() { return modificationTime; }
	public long trackID() { return trackID; }
	public long duration() { return duration; }
	public int layer() { return layer; }
	public int alternateGroup() { return alternateGroup; }
	public int volume() { return volume; }
	public byte[][] matrix() { return matrix; }
	public long width() { return width; }
	public long height() { return height; }

	public void setParent(GeneralAtom atom)
	{
		this.parentAtom = atom;
	}
	
	@Override
	public String toString() 
	{
		return "TkhdAtom [size=" + size + ", name=" + name + ", version=" + version + ", flags="
				+ Arrays.toString(flags) + ", creationTime=" + creationTime + ", modificationTime=" + modificationTime
				+ ", trackID=" + trackID + ", duration=" + duration + ", layer=" + layer + ", alternateGroup="
				+ alternateGroup + ", volume=" + volume + ", matrix=" + Arrays.toString(matrix) + ", width=" + width
				+ ", height=" + height;
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + Arrays.deepHashCode(matrix);
		result = prime * result + Objects.hash(alternateGroup, creationTime, duration, height, layer, modificationTime,
				name, size, trackID, version, volume, width);
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
		TkhdAtom other = (TkhdAtom) obj;
		return alternateGroup == other.alternateGroup && creationTime == other.creationTime
				&& duration == other.duration && Arrays.equals(flags, other.flags) && height == other.height
				&& layer == other.layer && Arrays.deepEquals(matrix, other.matrix)
				&& modificationTime == other.modificationTime && Objects.equals(name, other.name) && size == other.size
				&& trackID == other.trackID && version == other.version && volume == other.volume
				&& width == other.width;
	}
    
	
}
