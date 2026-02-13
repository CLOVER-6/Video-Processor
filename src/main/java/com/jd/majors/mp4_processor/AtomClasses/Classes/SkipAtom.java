package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.TopLevelAtom;

/**
 * Representation of the 'skip' atom used for padding or ignored regions inside
 * an MP4 file.
 *
 * <p>The {@code skip} box is semantically equivalent to {@code free} in that
 * it carries uninterpreted bytes which should be ignored by decoders. It is
 * useful for reserving space or marking portions of a file for tools.</p>
 *
 * <p>This class retains a raw {@code payload} until {@code parse()} is called
 * at which point the buffer is cleared. It implements both top-level and
 * nested semantics so it can appear in multiple locations within the file
 * hierarchy.</p>
 */
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