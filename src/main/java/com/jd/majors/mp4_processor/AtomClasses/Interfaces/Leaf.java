package com.jd.majors.mp4_processor.AtomClasses.Interfaces;

/**
 * BasicAtom covers simple atoms that expose a parse() method without
 * version/flags. These are generally top-level or payload-bearing atoms
 * where the payload is interpreted by parse().
 */
public non-sealed interface Leaf extends Box
{
	Box parse() throws Exception;
}