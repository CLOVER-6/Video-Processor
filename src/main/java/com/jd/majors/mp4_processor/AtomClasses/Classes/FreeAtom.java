package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.TopLevelAtom;

/**
 * Representation of the 'free' atom which provides padding or placeholder space
 * inside an MP4 file.
 *
 * <p>The {@code free} box contains uninterpreted bytes that are typically used
 * for alignment or to reserve space for later edits. Media tools should ignore
 * its payload; its presence does not affect decoding semantics.</p>
 *
 * <p>This class keeps a raw {@code payload} until {@code parse()} is invoked,
 * at which point the buffer is discarded. Implementations should avoid
 * exposing the raw payload for external processing since it is semantically
 * opaque.</p>
 *
 * <p>Implementation notes: this atom can be used as a nested child or a
 * top-level atom and therefore implements both {@code NestedAtom} and
 * {@code TopLevelAtom}.</p>
 */
public class FreeAtom implements Leaf, TopLevelAtom, NestedAtom
{
	Box parentAtom;
    private final int size;
    private final String name;
    private byte[] payload;

    public FreeAtom(int size, String name, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = size;
        this.name = name;
        this.payload = payload;
    }

    public FreeAtom parse() throws Exception
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
        return "FreeAtom [size=" + size + ", name=" + name + "]";
    }

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(payload);
		result = prime * result + Objects.hash(name, size);
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
		FreeAtom other = (FreeAtom) obj;
		return Objects.equals(name, other.name) && Arrays.equals(payload, other.payload) && size == other.size;
	}

   
}