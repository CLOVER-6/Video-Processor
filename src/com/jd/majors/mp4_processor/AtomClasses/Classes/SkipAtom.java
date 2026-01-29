package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.TopLevelAtom;

public class SkipAtom implements Leaf, TopLevelAtom, NestedAtom
{
	private Box parentAtom;
    private final int size;
    private final String name;
    private byte[] payload;

    public SkipAtom(int size, String name, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = size;
        this.name = name;
        this.payload = payload;
    }
    
    public SkipAtom parse() throws Exception
    {
        if (payload == null)
        {
            throw new Exception("Empty Payload - Cannot parse");
        }
        payload = null;
        
        return this;
    }

    public Box parentAtom() { return parentAtom; }
    public int size() { return size; }
    public String name() { return name; }
    public byte[] payload() { return payload; }

    public void setParent(Box parentAtom) 
	{
    	this.parentAtom = parentAtom;
	}
    
    @Override
    public String toString() 
    {
        return "SkipAtom [size=" + size + ", name=" + name + "]";
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
        if (!(obj instanceof SkipAtom)) return false;
        SkipAtom other = (SkipAtom) obj;
        return size == other.size && Objects.equals(name, other.name);
    }
}