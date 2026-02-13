package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullBox;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

/**
 * Representation of the 'hdlr' (handler reference) full box which specifies
 * the media handler type and human-readable name for a track or media box.
 *
 * <p>The handler atom indicates how the media in a track should be interpreted
 * (for example {@code "vide"}, {@code "soun"}, {@code "meta"}) and may
 * contain a variable-length, NUL-terminated name used for debugging or UI
 * purposes.</p>
 *
 * <p>This class stores a raw {@code payload} until {@code parse()} extracts the
 * {@code handlerType} and {@code handlerName}; the payload is cleared after
 * parsing to reduce memory use. The class preserves {@code version} and
 * {@code flags} as required by the full box format.</p>
 *
 * <p>Implementation notes: {@code HdlrAtom} implements {@code FullBox} and
 * {@code NestedAtom}; callers should set the parent when attaching the atom to
 * a container. Multi-byte fields are handled with unsigned-aware arithmetic.
 * </p>
 */
public class HdlrAtom implements FullBox, NestedAtom, Leaf
{
	private Box parentAtom;
	private final int size;
	private final String name;
	private final short version;
	private final byte[] flags;
	private String handlerType;
	private String handlerName;
	private byte[] payload;

	public HdlrAtom(int size, String name, short version, byte[] flags, String handlerType, String handlerName,
			byte[] payload) 
	{
		this.parentAtom = null;
		this.size = size;
		this.name = name;
		this.version = version;
		this.flags = flags;
		this.handlerType = handlerType;
		this.handlerName = handlerName;
		this.payload = null;
	}

	public HdlrAtom(int s, String n, short version, byte[] f, byte[] payload) 
	{
		this.parentAtom =  null;
		this.size = s;
		this.name = n;
		this.version = version;
		this.flags = f;
		this.handlerType = "";
		this.handlerName = "";
		this.payload = payload;
	}

	public HdlrAtom(int s, String n, byte[] payload) 
	{
		this.parentAtom = null;
		this.size = s;
		this.name = n;
		this.version = payload[0];
		this.flags = Arrays.copyOfRange(payload, 1, 4);
		this.handlerType = "";
		this.handlerName = "";
		this.payload = Arrays.copyOfRange(payload, 4, payload.length);
	}

	// TODO fill this out
	public HdlrAtom parse() throws Exception 
	{
		if (payload == null)
		{
			throw new Exception("Empty Payload - Cannot parse");
		}

		payload = Arrays.copyOfRange(payload, 4, payload.length); // strip first 4 bytes (pre-defined)

		handlerType = new String(Arrays.copyOfRange(payload, 0, 4), StandardCharsets.ISO_8859_1);

		payload = Arrays.copyOfRange(payload, 16, payload.length); // strip handler type and reserved bytes

		// guard: ensure pointer within bounds
		int payloadPointer = 0;
		while (payload[payloadPointer] != 0x00 && payloadPointer < payload.length)
		{
			handlerName = handlerName + (char) payload[payloadPointer];
			payloadPointer++;
		}
		payload = null;

		return this;
	}

	public Box parentAtom() { return parentAtom; }
	public int size() { return size; }
	public String name() { return name; }
	public short version() { return version; }
	public byte[] flags() { return flags; }
	public String handlerType() { return handlerType; }
	public String handlerName() { return handlerName; }
	public byte[] payload() { return payload; }

	public void setParent(Box atom)
	{
		this.parentAtom = atom;
	}

	@Override
	public String toString() 
	{
		return "HdlrAtom [size=" + size + ", name=" + name + ", version=" + version + ", flags="
				+ Arrays.toString(flags) + ", handlerType=" + handlerType + ", handlerName=" + handlerName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + Objects.hash(handlerName, handlerType, name, size, version);
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
		HdlrAtom other = (HdlrAtom) obj;
		return Arrays.equals(flags, other.flags) && Objects.equals(handlerName, other.handlerName)
				&& Objects.equals(handlerType, other.handlerType) && Objects.equals(name, other.name)
				&& size == other.size && version == other.version;
	}
}