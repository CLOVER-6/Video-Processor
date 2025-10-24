package com.jd.majors.mp4_processor.Parsing;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;

@FunctionalInterface
public interface AtomFactory 
{
	GeneralAtom createAtom(int size, String name, byte[] payload);
}
