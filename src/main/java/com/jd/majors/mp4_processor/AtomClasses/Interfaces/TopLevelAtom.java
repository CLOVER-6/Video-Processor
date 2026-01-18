package com.jd.majors.mp4_processor.AtomClasses.Interfaces;

/**
 * TopLevelAtom is a marker for atoms that are directly contained in the file
 * top-level (they do not have a meaningful parent within the MP4 hierarchy).
 */
public non-sealed interface TopLevelAtom extends GeneralAtom
{
	GeneralAtom parentAtom = null;
}