package com.jd.majors.mp4_processor.AtomClasses.Interfaces;

import java.util.List;

/**
 * Represents a container atom capable of holding child {@link Box} instances
 * within the MP4/ISO Base Media File Format hierarchy.
 *
 * <p>A {@code ContainerBox} is a structural node that may contain zero or more
 * child atoms, which can themselves be either container or leaf atoms. Typical
 * examples in the format include top-level and intermediate containers such as
 * {@code moov}, {@code trak}, or {@code mdia}. Implementations are responsible
 * for maintaining parent–child relationships and preserving atom ordering as
 * defined in the source stream.</p>
 *
 * <p>The {@link #parseChildren(boolean)} method enables parsing of contained
 * atoms from their raw payload. When {@code recursiveParseFlag} is {@code true},
 * implementations should recursively invoke parsing on nested container atoms
 * and call {@code parse()} on leaf atoms where appropriate. When {@code false},
 * only immediate children should be parsed without descending further.</p>
 *
 * <p>The {@link #addAtom(NestedAtom)} method attaches a child atom to this
 * container and should ensure the child’s parent reference is set accordingly.
 * Implementations may enforce structural validation rules (e.g., allowed child
 * types or cardinality constraints).</p>
 *
 * <p>The {@link #childAtoms()} method exposes the ordered list of direct child
 * atoms contained within this box.</p>
 */

public non-sealed interface ContainerBox extends Box
{
	public void parseChildren(boolean recursiveParseFlag) throws Exception;
	public void addAtom(NestedAtom atom) throws Exception;
	public List<Box> childAtoms();
}