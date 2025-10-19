package com.jd.majors.mp4_processor.Parsing;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.*;
import com.jd.majors.mp4_processor.AtomClasses.Records.*;

public class AtomFactory 
{
	public GeneralAtom createAtom(String name, int size, byte[] payload)
	{
		 switch (name) {
	        case "ftyp":
	            return new FtypAtom(name, size, payload);
	        case "moov":
	            return new MoovAtom(name, size, payload);
	        case "mvhd":
	            return new MvhdAtom(name, size, payload);
	        case "trak":
	            return new TrakAtom(name, size, payload);
	        case "tkhd":
	            return new TkhdAtom(name, size, payload);
	        case "edts":
	            return new EdtsAtom(name, size, payload);
	        case "elst":
	            return new ElstAtom(name, size, payload);
	        case "mdia":
	            return new MdiaAtom(name, size, payload);
	        case "mdhd":
	            return new MdhdAtom(name, size, payload);
	        case "hdlr":
	            return new HdlrAtom(name, size, payload);
	        case "minf":
	            return new MinfAtom(name, size, payload);
	        case "vmhd":
	            return new VmhdAtom(name, size, payload);
	        case "smhd":
	            return new SmhdAtom(name, size, payload);
	        case "dinf":
	            return new DinfAtom(name, size, payload);
	        case "dref":
	        case "url":
	            return new DrefAtom(name, size, payload);
	        case "stbl":
	            return new StblAtom(name, size, payload);
	        case "stsd":
	            return new StsdAtom(name, size, payload);
	        case "stts":
	            return new SttsAtom(name, size, payload);
	        case "ctts":
	            return new CttsAtom(name, size, payload);
	        case "stss":
	            return new StssAtom(name, size, payload);
	        case "stsc":
	            return new StscAtom(name, size, payload);
	        case "stsz":
	            return new StszAtom(name, size, payload);
	        case "stco":
	            return new StcoAtom(name, size, 32, payload);
	        case "co64":
	        	return new StcoAtom(name, size, 64, payload);
	        case "udta":
	            return new UdtaAtom(name, size, payload);
	        case "meta":
	            return new MetaAtom(name, size, payload);
	        case "free":
	            return new SkipAtom(name, size, payload);
	        case "skip":
	            return new SkipAtom(name, size, payload);
	        default:
	        	return null;
		 }
	}
}
