package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;

public class MvhdAtom implements FullAtom 
{
    private final int size;
    private final String name;
    private final short version;
    private final byte[] flags;
    private final byte[] payload;

    public MvhdAtom(int s, String n, short version, byte[] f, byte[] payload) 
    {
        this.size = s;
        this.name = n;
        this.version = version;
        this.flags = f;
        this.payload = payload;
    }

    public MvhdAtom(int s, String n, byte[] payload) 
    {
        this.size = s;
        this.name = n;
        this.version = payload[0];
        this.flags = Arrays.copyOfRange(payload, 1, 4);
        this.payload = Arrays.copyOfRange(payload, 5, payload.length);
    }

    // TODO fill this out
    public GeneralAtom parse() 
    {
    	return null;
    }
    
    public int size() { return size; }
    public String name() { return name; }
    public short version() { return version; }
    public byte[] flags() { return flags; }
    public byte[] payload() { return payload; }

    @Override
    public String toString() 
    {
        return "MvhdAtom [size=" + size + ", name=" + name + ", version=" + version +
               ", flags=" + flags + ", payloadLength=" + (payload != null ? payload.length : 0) + "]";
    }

    @Override
    public int hashCode() 
    {
        return Objects.hash(size, name, version, Arrays.hashCode(flags), java.util.Arrays.hashCode(payload));
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) return true;
        if (!(obj instanceof MvhdAtom)) return false;
        MvhdAtom other = (MvhdAtom) obj;
        return size == other.size && version == other.version && Arrays.equals(flags, other.flags)
            && Objects.equals(name, other.name) && java.util.Arrays.equals(payload, other.payload);
    }
}
