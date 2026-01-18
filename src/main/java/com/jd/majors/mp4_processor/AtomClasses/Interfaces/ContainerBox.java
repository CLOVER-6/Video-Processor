package com.jd.majors.mp4_processor.AtomClasses.Interfaces;

import java.util.List;

/**
 * ContainerAtom represents atoms that contain other atoms (child atoms).
 * Implementations should provide an addAtom method and expose the child list.
 */
public non-sealed interface ContainerBox extends Box
{
	public void addAtom(NestedAtom atom);
	public List<Box> childAtoms();
}