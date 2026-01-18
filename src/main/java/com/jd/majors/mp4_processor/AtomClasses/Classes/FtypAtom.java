package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.TopLevelAtom;

public class FtypAtom implements Leaf, TopLevelAtom
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
        this.payload = null; // full-value constructor should not keep payload
    }

    public FtypAtom(int size, String name, byte[] payload) 
    {
        this.size = size;
        this.name = name;
        this.majorBrand = "";
        this.minorVersion = 0;
        this.compatibleBrands = null;
        this.payload = payload;
    }

    public FtypAtom parse() throws Exception
    {
        if (payload == null)
        {
            throw new Exception("Empty Payload - Cannot parse");
        }
        
        majorBrand = new String(Arrays.copyOfRange(payload, 0, 4));
        
        int eightMultiple = 3;
        for (int i = 4; i < 8; i++)
        {
            minorVersion = minorVersion | ((long)(payload[i] & 0xFF) << (8 * eightMultiple));
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
        
        return this;
    }
    
    public int size() { return size; }
    public String name() { return name; }
    public String majorBrand() { return majorBrand; }
    public long minorVersion() { return minorVersion; }
    public String[] compatibleBrands() { return compatibleBrands; }

    @Override
    public String toString() 
    {
        return "FtypAtom [size=" + size + ", name=" + name + ", majorBrand=" + majorBrand + ", minorVersion=" + minorVersion + ", compatibleBrands=" + Arrays.toString(compatibleBrands) + "]";
    }

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(compatibleBrands);
		result = prime * result + Objects.hash(majorBrand, minorVersion, name, size);
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
		FtypAtom other = (FtypAtom) obj;
		return Arrays.equals(compatibleBrands, other.compatibleBrands) && Objects.equals(majorBrand, other.majorBrand)
				&& minorVersion == other.minorVersion && Objects.equals(name, other.name) && size == other.size;
	}
}