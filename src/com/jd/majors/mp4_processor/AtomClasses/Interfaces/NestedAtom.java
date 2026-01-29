package com.jd.majors.mp4_processor.AtomClasses.Interfaces;

/**
 * NestedAtom marks atoms that have a parent (they live inside container atoms).
 * Implementers must allow getting and setting the parent atom.
 */
public non-sealed interface NestedAtom extends Box
{
	public Box parentAtom();
	public void setParent(Box parentAtom);
}