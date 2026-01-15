package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class MvhdAtom implements FullAtom, NestedAtom
{
	private GeneralAtom parentAtom;
    private final int size;
    private final String name;
    private final short version;
    private final byte[] flags;
    private long creationTime;
    private long modificationTime;
    private long timescale;
    private long duration;
    private long rate;
    private int volume;
    private short[][] matrix;
    private long nextTrackID;
    private byte[] payload;
    
    public MvhdAtom(GeneralAtom parentAtom, int size, String name, short version, byte[] flags, long creationTime, long modificationTime,
			long timescale, long duration, long rate, int volume, short[][] matrix, long nextTrackID) 
    {
    	this.parentAtom = parentAtom;
		this.size = size;
		this.name = name;
		this.version = version;
		this.flags = flags;
		this.creationTime = creationTime;
		this.modificationTime = modificationTime;
		this.timescale = timescale;
		this.duration = duration;
		this.rate = rate;
		this.volume = volume;
		this.matrix = matrix;
		this.nextTrackID = nextTrackID;
		this.payload = null;
	}

	public MvhdAtom(int s, String n, short version, byte[] f, byte[] payload) 
    {
		this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = version;
        this.flags = f;
        this.creationTime = 0;
        this.modificationTime = 0;
        this.timescale = 0;
        this.duration = 0;
        this.rate = 0;
        this.volume = 0;
        this.matrix = new short[9][4];
        this.nextTrackID =  0;
        this.payload = payload;
    }

    public MvhdAtom(int s, String n, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = payload[0];
        this.flags = Arrays.copyOfRange(payload, 1, 4);
        this.creationTime = 0;
        this.modificationTime = 0;
        this.timescale = 0;
        this.duration = 0;
        this.rate = 0;
        this.volume = 0;
        this.matrix = new short[9][4];
        this.nextTrackID =  0;
        this.payload = Arrays.copyOfRange(payload, 4, payload.length);
    }
    
    public MvhdAtom parse() throws Exception
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
        	timescale = timescale | (payload[i] & 0xFF) << 8 * eightMultiple;

        	eightMultiple = eightMultiple - 1;
        }
        
        eightMultiple = (version == 0) ? 3 : 7;
        for (int i = (version == 0) ? 12 : 20; i < ((version == 0) ? 16 : 28); i++)
        {
        	duration = duration | (payload[i] & 0xFF) << 8 * eightMultiple;
        	eightMultiple = eightMultiple - 1;
        }
        
        eightMultiple = 3;
        for (int i = (version == 0) ? 16 : 28; i < ((version == 0) ? 20 : 32); i++)
        {
        	rate = rate | (payload[i] & 0xFF) << 8 * eightMultiple;
        	eightMultiple = eightMultiple - 1;
        }
        
        volume = (payload[((version == 0) ? 20 : 32)] & 0xFF) << 8 | (payload[((version == 0) ? 21 : 33)] & 0xFF);
        
        int matrixPointer = 0;
        for (int i = (version == 0) ? 32 : 44; i < ((version == 0) ? 68 : 80); i = i + 4)
        {
        	for (int j = 0; j < 4; j++)
        	{
            	matrix[matrixPointer][j] = (short) payload[i + j];
        	}
        	
        	matrixPointer = matrixPointer + 1;
        }
        
        eightMultiple = 3;
        for (int i = (version == 0) ? 92 : 104; i < ((version == 0) ? 96 : 108); i++)
        {
        	nextTrackID = nextTrackID | (payload[i] & 0xFF) << 8 * eightMultiple;
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
	public long timescale() { return timescale; }
	public long duration() { return duration; }
	public long rate() { return rate; }
	public int nolume() { return volume; }
	public short[][] matrix() { return matrix; }
	public long nextTrackID() { return nextTrackID; }
	
	public void setParent(GeneralAtom atom)
	{
		this.parentAtom = atom;
	}
	
	@Override
	public String toString() {
		return "MvhdAtom [size=" + size + ", name=" + name + ", version=" + version + ", flags="
				+ Arrays.toString(flags) + ", creationTime=" + creationTime + ", modificationTime=" + modificationTime
				+ ", timescale=" + timescale + ", duration=" + duration + ", rate=" + rate + ", volume=" + volume
				+ ", matrix=" + Arrays.toString(matrix) + ", nextTrackID=" + nextTrackID + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + Arrays.deepHashCode(matrix);
		result = prime * result + Objects.hash(creationTime, duration, modificationTime, name, nextTrackID, rate, size,
				timescale, version, volume);
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
		MvhdAtom other = (MvhdAtom) obj;
		return creationTime == other.creationTime && duration == other.duration && Arrays.equals(flags, other.flags)
				&& Arrays.deepEquals(matrix, other.matrix) && modificationTime == other.modificationTime
				&& Objects.equals(name, other.name) && nextTrackID == other.nextTrackID && rate == other.rate 
				&& size == other.size && timescale == other.timescale && version == other.version && volume == other.volume;
	}

	
}
