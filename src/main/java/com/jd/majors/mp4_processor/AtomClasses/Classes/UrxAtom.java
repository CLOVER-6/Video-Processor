package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.Arrays;
import java.util.Objects;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.NestedAtom;

//can be url or urn, hence ur(x)
	public class UrxAtom implements NestedAtom, FullAtom
	{
		private GeneralAtom parentAtom;
		private final int size;
		private final String name;
		private final short version;
		private final byte[] flags;
		private String urx;
		private byte[] payload;
		
		public UrxAtom(int size, String name, short version, byte[] flags, String urx)
		{
			this.size = size;
			this.name = name;
			this.version = version;
			this.flags = flags;
			this.urx = urx;
			this.payload = null;
		}

		public UrxAtom(int size, String name, short version, byte[] flags, byte[] payload)
		{
			this.parentAtom = null;
			this.size = size;
			this.name = name;
			this.version = version;
			this.flags = flags;
			this.urx = "";
			this.payload = payload;
		}

		public UrxAtom(int size, String name, byte[] payload)
		{
			this.parentAtom = null;
			this.size = size;
			this.name = name;
			this.version = payload[0];
	        this.flags = Arrays.copyOfRange(payload, 1, 4);
			this.urx = "";
			this.payload = Arrays.copyOfRange(payload, 4, payload.length);
		}
		
		public UrxAtom parse() throws Exception
		{	
			if (payload == null)
			{
				throw new Exception("Empty Payload - Cannot parse");
			}
			
    		urx = new String(Arrays.copyOfRange(payload, 0, payload.length));
    		payload = null;
    		
    		return this;
		}
		
		public GeneralAtom parentAtom() { return parentAtom; }
		public int size() { return size; }
		public String name() { return name; }
		public short version() { return version; }
		public byte[] flags() { return flags; }
		public String urx() { return urx; }

		public void setParent(GeneralAtom atom)
		{
			this.parentAtom = atom;
		}

		@Override
		public String toString() 
		{
			return "UrxAtom [parentAtom=" + parentAtom + ", size=" + size + ", name=" + name + ", flags="
					+ Arrays.toString(flags) + ", urx=" + urx + "]";
		}

		@Override
		public int hashCode() 
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(flags);
			result = prime * result + Objects.hash(name, parentAtom, size, urx, version);
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
			UrxAtom other = (UrxAtom) obj;
			return Arrays.equals(flags, other.flags) && Objects.equals(name, other.name)
					&& Objects.equals(parentAtom, other.parentAtom) && size == other.size
					&& Objects.equals(urx, other.urx) && version == other.version;
		}
		
		
		}
	
	