package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.TopLevelAtom;

/**
 * Representation of the 'mdat' (media data) atom which contains the raw media
 * payload (samples) of tracks.
 *
 * <p>The {@code mdat} box can be very large and holds encoded audio/video/data
 * sample bytes. This class currently retains the raw payload but intentionally
 * does not eagerly parse or copy media contents since sample-level parsing is
 * handled by higher-level demuxers or streaming APIs.</p>
 *
 * <p>{@code parse()} will validate the existence of the payload but does not
 * transform the media bytes; future implementations may provide streaming or
 * memory-mapped accessors to avoid large heap allocations.</p>
 */
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

		// we intentionally leave the media data in payload for now - it may be
		// large and handled by streaming APIs in future versions.
		return this;
	}

	public int size() { return size; }
	public String name() { return name; }
	public byte[] payload() { return payload; }

	@Override
	public String toString() 
	{
		return "MdatAtom [size=" + size + ", name=" + name + ", payload=" + Arrays.toString(payload) + "]";
	}

	@Override
	public int hashCode() 
	{
		return Objects.hash(name, size);
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
		MdatAtom other = (MdatAtom) obj;
		return Objects.equals(name, other.name) && size == other.size;
	}

}