package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

/**
 * Media header atom (mdhd).
 *
 * Notes:
 * - Creation/modification times and duration are version-dependent (32-bit vs 64-bit).
 * - Language is stored as three 5-bit characters per ISO spec and decoded during parsing.
 */
public class MdhdAtom implements FullBox, NestedAtom , Leaf
{
	private Box parentAtom;
	private final int size;
    private final String name;
    private final short version;
    private final byte[] flags;
    private long creationTime;
    private long modificationTime;
    private long timescale;
    private long duration;
    private String language;
    private byte[] payload;
    
    public MdhdAtom(int size, String name, short version, byte[] flags, long creationTime, long modificationTime,
			long timescale, long duration, String language) 
    {
    	this.parentAtom = null;
		this.size = size;
		this.name = name;
		this.version = version;
		this.flags = flags;
		this.creationTime = creationTime;
		this.modificationTime = modificationTime;
		this.timescale = timescale;
		this.duration = duration;
		this.language = language;
		this.payload = null;
	}

	public MdhdAtom(int s, String n, short version, byte[] f, byte[] payload) 
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
        this.language = "";
        this.payload = payload;
    }

    public MdhdAtom(int s, String n, byte[] payload) 
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
        this.language = "";
        this.payload = Arrays.copyOfRange(payload, 4, payload.length);
    }
    
    public MdhdAtom parse() throws Exception
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

        // each char of the language code is 5 bits, 15 bits and one reserved bit total
        // to deal in 5 bit parts, make both bytes an integer
        // 0x1F is 00011111, which is used to isolate the first 5 bits. right shift down in order to deal with what 5 bit section we want
        // ISO define each char in the code as x + 0x60
        int languageInt = 0;
        languageInt = languageInt | (payload[version == 0 ? 16 : 28] & 0xFF) << 8 | payload[version == 0 ? 17 : 29] & 0xFF;
        char[] languageChars = new char[3];
        languageChars[0] = (char) (((languageInt >> 10) & 0x1F) + 0x60);
        languageChars[1] = (char) (((languageInt >> 5) & 0x1F) + 0x60);
        languageChars[2] = (char) (((languageInt) & 0x1F) + 0x60);
        language = new String(languageChars);
    	
    	// dont allow atom to be parsed multiple times
    	payload = null;
    	
    	return this;
    }

    public Box parentAtom() { return parentAtom; }
	public int size() { return size; }
	public String name() { return name; }
	public short version() { return version; }
	public byte[] flags() { return flags; }
	public long creationTime() { return creationTime; }
	public long modificationTime() { return modificationTime;  }
	public long timescale() { return timescale; }
	public long duration() { return duration; }
	public String language() { return language; }

	public void setParent(Box atom)
	{
		this.parentAtom = atom;
	}
	
	@Override
	public String toString() 
	{
		return "MdhdAtom [size=" + size + ", name=" + name + ", version=" + version + ", flags="
				+ Arrays.toString(flags) + ", creationTime=" + creationTime + ", modificationTime=" + modificationTime
				+ ", timescale=" + timescale + ", duration=" + duration + ", language=" + language + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result
				+ Objects.hash(creationTime, duration, language, modificationTime, name, size, timescale, version);
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
		MdhdAtom other = (MdhdAtom) obj;
		return creationTime == other.creationTime && duration == other.duration && Arrays.equals(flags, other.flags)
				&& Objects.equals(language, other.language) && modificationTime == other.modificationTime
				&& Objects.equals(name, other.name) && size == other.size && timescale == other.timescale
				&& version == other.version;
	}
}
