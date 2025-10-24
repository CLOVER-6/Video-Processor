package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.BasicAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;

public class SkipAtom implements BasicAtom 
{
    private final int size;
    private final String name;
    private final byte[] payload;

    public SkipAtom(int size, String name, byte[] payload) 
    {
        this.size = size;
        this.name = name;
        this.payload = payload;
    }
    
    // TODO fill this out
    public GeneralAtom parse() 
    {
    	return null;
    }

    public int size() { return size; }
    public String name() { return name; }
    public byte[] payload() { return payload; }

    @Override
    public String toString() 
    {
        return "SkipAtom [size=" + size + ", name=" + name + ", payloadLength=" +
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
        if (!(obj instanceof SkipAtom)) return false;
        SkipAtom other = (SkipAtom) obj;
        return size == other.size && Objects.equals(name, other.name)
            && java.util.Arrays.equals(payload, other.payload);
    }
}
