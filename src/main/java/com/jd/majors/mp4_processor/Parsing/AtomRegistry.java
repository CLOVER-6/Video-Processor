package com.jd.majors.mp4_processor.Parsing;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.*;
import com.jd.majors.mp4_processor.AtomClasses.Classes.*;

import java.util.HashMap;
import java.util.Map;

/**
 * AtomRegistry holds a mapping from four-character atom names to factory
 * functions that construct atom instances. This central registry makes it
 * easy to extend supported atom types by registering a factory at
 * initialization time.
 */
public class AtomRegistry 
{
	private static final Map<String, AtomFactory> registry = new HashMap<String, AtomFactory>();
	
	// put new atom in registry, along with those given at compile time
	public static void registerAtom(String atomName, AtomFactory atomFactory)
	{
		if (!registry.containsKey(atomName))
		{
			registry.put(atomName, atomFactory);
		}
		else
		{
			throw new IllegalArgumentException("Atom name already registered: " + atomName);
		}
	}
	
	// give atom to user
	public static Box createAtom(int size, String name, byte[] payload)
	{
		AtomFactory atomFactory = registry.get(name);
		
		if (atomFactory != null)
		{
			Box atom = atomFactory.createAtom(size, name, payload);
			return atom;
		}
		else
		{
			return null;
		}
	}
	
	 /**
     * Static initialization — register all known MP4 atoms here.
     */
    static {
        // Core top-level boxes
        registerAtom("ftyp", (s, n, p) -> new FtypAtom(s, n, p));
        registerAtom("moov", (s, n, p) -> new MoovAtom(s, n, p));
        registerAtom("mdat", (s, n, p) -> new MdatAtom(s, n, p));
        registerAtom("free", (s, n, p) -> new SkipAtom(s, n, p));
        registerAtom("skip", (s, n, p) -> new SkipAtom(s, n, p));

        // Movie-level boxes
        registerAtom("mvhd", (s, n, p) -> new MvhdAtom(s, n, p));
        registerAtom("trak", (s, n, p) -> new TrakAtom(s, n, p));
        registerAtom("tkhd", (s, n, p) -> new TkhdAtom(s, n, p));
        registerAtom("edts", (s, n, p) -> new EdtsAtom(s, n, p));
        registerAtom("elst", (s, n, p) -> new ElstAtom(s, n, p));
        registerAtom("ctts", (s, n, p) -> new CttsAtom(s, n, p));

        // Media boxes
        registerAtom("mdia", (s, n, p) -> new MdiaAtom(s, n, p));
        registerAtom("mdhd", (s, n, p) -> new MdhdAtom(s, n, p));
        registerAtom("hdlr", (s, n, p) -> new HdlrAtom(s, n, p));
        registerAtom("minf", (s, n, p) -> new MinfAtom(s, n, p));
        registerAtom("dinf", (s, n, p) -> new DinfAtom(s, n, p));
        registerAtom("dref", (s, n, p) -> new DrefAtom(s, n, p));
        registerAtom("vmhd", (s, n, p) -> new VmhdAtom(s, n, p));

        // Sample tables
        registerAtom("stbl", (s, n, p) -> new StblAtom(s, n, p));
        registerAtom("stsd", (s, n, p) -> new StsdAtom(s, n, p));
        registerAtom("avc1", (s, n, p) -> new AvcAtom(s, n, p));
        registerAtom("stts", (s, n, p) -> new SttsAtom(s, n, p));
        registerAtom("stsc", (s, n, p) -> new StscAtom(s, n, p));
        registerAtom("stsz", (s, n, p) -> new StszAtom(s, n, p));
        registerAtom("stco", (s, n, p) -> new StcoAtom(s, n, p));
        registerAtom("co64", (s, n, p) -> new Co64Atom(s, n, p));
        registerAtom("stss", (s, n, p) -> new StssAtom(s, n, p));

        // User data / metadata
        registerAtom("udta", (s, n, p) -> new UdtaAtom(s, n, p));
        registerAtom("meta", (s, n, p) -> new MetaAtom(s, n, p));
        registerAtom("ilst", (s, n, p) -> new IlstAtom(s, n, p));

    }
    
    // prevents object instantiation
    private AtomRegistry() {}
}