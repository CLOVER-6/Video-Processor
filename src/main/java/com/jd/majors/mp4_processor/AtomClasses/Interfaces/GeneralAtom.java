package com.jd.majors.mp4_processor.AtomClasses.Interfaces;

public sealed interface GeneralAtom permits ContainerAtom, FullAtom, BasicAtom
{
	String name();
	int size();
}
