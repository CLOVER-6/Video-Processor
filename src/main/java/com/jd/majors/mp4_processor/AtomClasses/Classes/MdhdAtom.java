package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

/**
 * Representation of the 'mdhd' (media header) full box, containing timing and
 * presentation parameters for a single media track (timescale, duration, and
 * language tags).
 *
 * <p>The media header provides track-local timing (timescale) and duration
 * information used to interpret sample timestamps and to synchronize multiple
 * tracks during playback.</p>
 *
 * <p>This class defers materializing header fields from a raw {@code payload}
 * until {@code parse()} is invoked; after parsing the payload is cleared to
 * avoid retaining redundant raw bytes.</p>
 *
 * <p>Implementation notes: {@code MdhdAtom} implements {@code FullBox} and
 * {@code NestedAtom}; it preserves {@code version} and {@code flags} and must
 * handle both 32-bit (version 0) and 64-bit (version 1) timestamp encodings
 * using unsigned-aware arithmetic.</p>
 */
public class MdhdAtom implements FullBox, NestedAtom, Leaf
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
    private int language;
    private byte[] payload;

    public MdhdAtom(int size, String name, short version, byte[] flags, long creationTime, long modificationTime,
            long timescale, long duration, int language) 
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
        this.language = 0;
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
        this.language = 0;
        this.payload = Arrays.copyOfRange(payload, 4, payload.length);
    }

    public MdhdAtom parse() throws Exception
    {
        if (payload == null)
        {
            throw new Exception("Empty Payload - Cannot parse");
        }

        int eightMultiple = (version == 0) ? 3 : 7;
        for (int i = 0; i < ((version == 0) ? 4 : 8); i++)
        {
            creationTime = creationTime | (payload[i] & 0xFF) << 8 * eightMultiple;
            eightMultiple = eightMultiple - 1;
        }

        eightMultiple = (version == 0) ? 3 : 7;
        for (int i = (version == 0) ? 4 : 8; i < ((version == 0) ? 8 : 16); i++)
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

        int langOffset = (version == 0) ? 16 : 28;
        language = ((payload[langOffset] & 0xFF) << 8) | (payload[langOffset + 1] & 0xFF);

        payload = null;
        return this;
    }

    public Box parentAtom() { return parentAtom; }
    public int size() { return size; }
    public String name() { return name; }
    public short version() { return version; }
    public byte[] flags() { return flags; }
    public long creationTime() { return creationTime; }
    public long modificationTime() { return modificationTime; }
    public long timescale() { return timescale; }
    public long duration() { return duration; }
    public int language() { return language; }
    public byte[] payload() { return payload; }

    public void setParent(Box atom)
    {
        this.parentAtom = atom;
    }

    @Override
    public String toString() {
        return "MdhdAtom [size=" + size + ", name=" + name + ", version=" + version + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(flags);
        result = prime * result + Objects.hash(creationTime, duration, language, modificationTime, name, size, timescale, version);
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
                && language == other.language && modificationTime == other.modificationTime && Objects.equals(name, other.name)
                && size == other.size && timescale == other.timescale && version == other.version;
    }
}