package com.jd.majors.mp4_processor.AtomClasses.Interfaces;

public sealed interface GeneralAtom permits FullAtom, BasicAtom, NestedAtom, TopLevelAtom, ContainerAtom
{
	String name();
	int size();
}
