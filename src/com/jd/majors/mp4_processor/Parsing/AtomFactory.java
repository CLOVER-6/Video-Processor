package com.jd.majors.mp4_processor.Parsing;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;

@FunctionalInterface
public interface AtomFactory 
{
	Box createAtom(int size, String name, byte[] payload);
}
