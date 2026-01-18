package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

public class HdlrAtom implements FullAtom, NestedAtom 
{
	private GeneralAtom parentAtom;
    private final int size;
    private final String name;
    private final short version;
    private final byte[] flags;
    private String handlerType;
    private String handlerName;
    private byte[] payload;

    public HdlrAtom(GeneralAtom parentAtom, int size, String name, short version, byte[] flags, String handlerType, String handlerName,
				byte[] payload) 
    {
		this.parentAtom = parentAtom;
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
     
     // handlerType lives at original payload bytes 8..11; after stripping first 4 bytes, it's at 4..7
     handlerType = new String(Arrays.copyOfRange(payload, 4, 8));
     
     int payloadPointer = 16; // name starts at original offset 20, which is 16 after we've stripped the first 4 bytes
     // guard: ensure pointer within bounds
     if (payloadPointer >= payload.length) {
         handlerName = "";
     } else {
         int nameEnd = payloadPointer;
         while (nameEnd < payload.length && payload[nameEnd] != 0x00) {
             nameEnd++;
         }
         handlerName = new String(Arrays.copyOfRange(payload, payloadPointer, nameEnd));
     }
     
     payload = null;
     
     return this;
    }

    public GeneralAtom parentAtom() { return parentAtom; }
	public int size() { return size; }
	public String name() { return name; }
	public short version() { return version; }
	public byte[] flags() { return flags; }
	public String handlerType() { return handlerType; }
	public String handlerName() { return handlerName; }

	public void setParent(GeneralAtom atom)
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
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(flags);
		result = prime * result + Objects.hash(handlerName, handlerType, name, size, version);
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
		HdlrAtom other = (HdlrAtom) obj;
		return Arrays.equals(flags, other.flags) && Objects.equals(handlerName, other.handlerName)
				&& Objects.equals(handlerType, other.handlerType) && Objects.equals(name, other.name)
				&& size == other.size && version == other.version;
	}
}