package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.BasicAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.TopLevelAtom;

public class FtypAtom implements BasicAtom, TopLevelAtom
{
    private int size;
    private final String name;
    private String majorBrand;
    private long minorVersion;
    private String[] compatibleBrands;
    private byte[] payload;
    
    public FtypAtom(int size, String name, String majorBrand, long minorVersion, String[] compatibleBrands,
			byte[] payload) 
    {
		this.size = size;
		this.name = name;
		this.majorBrand = majorBrand;
		this.minorVersion = minorVersion;
		this.compatibleBrands = compatibleBrands;
		this.payload = null;
	}

	public FtypAtom(int size, String name, byte[] payload) 
    {
        this.size = size;
        this.name = name;
        this.majorBrand = "";
        this.minorVersion = 0;
        this.compatibleBrands = new String[size - 16];
        this.payload = payload;
    }

    public void parse() throws Exception
    {
    	if (payload == null)
    	{
    		throw new Exception("Empty Payload - Cannot parse");
    	}
    	
        majorBrand = new String(Arrays.copyOfRange(payload, 0, 4));
        
        int eightMultiple = 3;
        for (int i = 4; i < 8; i++)
        {
        	// bytes are inherently signed in java so "& 0xFF" unsigns
        	// << left shifts by decrementing multiples of 8 to place bits in respective spots
        	// using long because java ints are signed whilst minor version is unsigned
        	minorVersion = minorVersion | (payload[i] & 0xFF) << 8 * eightMultiple;
        	eightMultiple = eightMultiple - 1;
        }

        // gather compatible brands
        compatibleBrands = new String[(payload.length - 8) / 4];
        
        int cbPointer = 0;
        for (int i = 8; i < payload.length; i = i + 4)
        {
        	compatibleBrands[cbPointer] = new String(Arrays.copyOfRange(payload, i, i + 4));
        	cbPointer = cbPointer + 1;
        }
        
        // not allow parsing of atom multiple times
        payload = null;
    }
    
    public int size() { return size; }
    public String name() { return name; }
    public String majorBrand() { return majorBrand; }
    public long minorVersion() { return minorVersion; }
    public String[] compatibleBrands() { return compatibleBrands; }

    @Override
    public String toString() 
    {
        return "FtypAtom [size=" + size + ", name=" + name + ", payloadLength=" +
               (payload != null ? payload.length : 0) + "]";
    }

    @Override
    public int hashCode() 
    {
        return Objects.hash(size, name, java.util.Arrays.hashCode(payload));
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) return true;
        if (!(obj instanceof FtypAtom)) return false;
        FtypAtom other = (FtypAtom) obj;
        return size == other.size && Objects.equals(name, other.name)
            && java.util.Arrays.equals(payload, other.payload);
    }
}
