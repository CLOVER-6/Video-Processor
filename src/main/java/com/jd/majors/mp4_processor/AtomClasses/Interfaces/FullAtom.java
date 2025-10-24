package com.jd.majors.mp4_processor.AtomClasses.Interfaces;

public non-sealed interface FullAtom extends GeneralAtom 
{
	short version(); 
	byte[] flags();
	byte[] payload();
	GeneralAtom parse();
}
