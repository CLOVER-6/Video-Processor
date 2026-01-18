package com.jd.majors.mp4_processor.AtomClasses.Interfaces;

/**
 * FullAtom represents atoms that include version and flags fields in their
 * payload (also known as 'full' atoms in the ISO/IEC spec). Implementations
 * must provide accessors for the version/flags and a parse() that returns the
 * typed FullAtom after decoding its payload.
 */
public non-sealed interface FullBox extends Box 
{
	short version(); 
	byte[] flags();
}