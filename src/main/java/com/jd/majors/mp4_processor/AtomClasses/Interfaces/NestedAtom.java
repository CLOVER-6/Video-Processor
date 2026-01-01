package com.jd.majors.mp4_processor.AtomClasses.Interfaces;

public non-sealed interface NestedAtom extends GeneralAtom
{
	public GeneralAtom parentAtom();
	public void setParent(GeneralAtom parentAtom);
}
