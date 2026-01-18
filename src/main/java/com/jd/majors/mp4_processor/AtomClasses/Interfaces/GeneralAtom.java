package com.jd.majors.mp4_processor.AtomClasses.Interfaces;

/**
 * GeneralAtom is the base interface for all MP4 atom types in this project.
 * It defines the minimal contract every atom must provide: its four-character
 * name and its size in bytes. Specialized atom kinds extend this interface
 * (FullAtom, BasicAtom, NestedAtom, ContainerAtom, TopLevelAtom).
 */
public sealed interface GeneralAtom permits FullAtom, BasicAtom, NestedAtom, TopLevelAtom, ContainerAtom
{
	String name();
	int size();
}