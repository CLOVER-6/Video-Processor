package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.TopLevelAtom;

public class MdatAtom implements Leaf, TopLevelAtom
{
    private final int size;
    private final String name;
    private byte[] payload;

    public MdatAtom(int size, String name, byte[] payload) 
    {
        this.size = size;
        this.name = name;
        this.payload = payload;
    }

    public MdatAtom parse() throws Exception
    {
        if (payload == null)
        {
            throw new Exception("Empty Payload - Cannot parse");
        }
        
        // avc codec data - parsing in future
        
        payload = null;
        
        return this;
    }
    
    public int size() { return size; }
    public String name() { return name; }
    public byte[] payload() { return payload; }

    @Override
    public String toString() 
    {
        return "MdatAtom [size=" + size + ", name=" + name + "]";
    }

    @Override
    public int hashCode() 
    {
        return Objects.hash(size, name);
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) return true;
        if (!(obj instanceof MdatAtom)) return false;
        MdatAtom other = (MdatAtom) obj;
        return size == other.size && Objects.equals(name, other.name);
    }
}