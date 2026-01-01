package com.jd.majors.mp4_processor.AtomClasses.Interfaces;

import java.util.List;

public non-sealed interface ContainerAtom extends GeneralAtom
{
	public void addAtom(NestedAtom atom);
	public List<GeneralAtom> childAtoms();
}
