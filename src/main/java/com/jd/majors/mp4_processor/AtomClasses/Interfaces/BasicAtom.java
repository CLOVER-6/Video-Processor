package com.jd.majors.mp4_processor.AtomClasses.Interfaces;

public non-sealed interface BasicAtom extends GeneralAtom
{
	byte[] payload();
	GeneralAtom parse();
}
