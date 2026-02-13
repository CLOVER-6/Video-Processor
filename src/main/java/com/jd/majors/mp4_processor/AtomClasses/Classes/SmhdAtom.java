package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

/**
 * Representation of the 'smhd' (sound media header) full box which provides
 * simple audio renderer hints for a track.
 *
 * <p>The box contains fields that may be used for simple audio mixing or
 * renderer configuration. For most audio decoding tasks the fields are hints
 * rather than strict decoder parameters.</p>
 *
 * <p>This class stores a raw {@code payload} until {@code parse()} is called;
 * parsing currently clears the payload and implementations may later expose
 * parsed fields if needed.</p>
 *
 * <p>Implementation notes: {@code SmhdAtom} implements {@code FullBox} and
 * {@code NestedAtom} and preserves {@code version} and {@code flags} from the
 * header.</p>
 */
public class SmhdAtom implements FullBox, NestedAtom, Leaf
{
	private Box parentAtom;
    private final int size;
    private final String name;
    private final short version;
    private final byte[] flags;
    private byte[] payload;

    public SmhdAtom(int s, String n, short version, byte[] f, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = version;
        this.flags = f;
        this.payload = null;
    }

    public SmhdAtom(int s, String n, byte[] payload) 
    {
    	this.parentAtom = null;
        this.size = s;
        this.name = n;
        this.version = payload[0];
        this.flags = Arrays.copyOfRange(payload, 1, 4);
        this.payload = Arrays.copyOfRange(payload, 4, payload.length);
    }

    public SmhdAtom parse() throws Exception
    {
        if (payload == null)
        {
            throw new Exception("Empty Payload - Cannot parse");
        }
        // parsing logic would go here
        // audio shit
        payload = null;
        return this;
    }    
    
    public Box parentAtom() { return parentAtom; }
    public int size() { return size; }
    public String name() { return name; }
    public short version() { return version; }
    public byte[] flags() { return flags; }
    public byte[] payload() { return payload; }

    public void setParent(Box atom)
    {
    	this.parentAtom = atom;
    }
    
    @Override
    public String toString() 
    {
        return "SmhdAtom [size=" + size + ", name=" + name + ", version=" + version +
               ", flags=" + Arrays.toString(flags) + "]";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + Objects.hash(name, size, version);
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
		SmhdAtom other = (SmhdAtom) obj;
		return Arrays.equals(flags, other.flags) && Objects.equals(name, other.name) && size == other.size
				&& version == other.version;
	}

	
}