package com.jd.majors.mp4_processor.AtomClasses.Interfaces;

/**
 * Marker interface representing a top-level atom in an MP4/ISO Base Media
 * File Format structure.
 *
 * <p>A {@code TopLevelAtom} exists directly under the file root and does not
 * have a parent container within the logical atom hierarchy (e.g.,
 * {@code ftyp}, {@code moov}, {@code mdat}). Unlike {@link NestedAtom}
 * implementations, top-level atoms are not owned by another {@link Box}
 * and therefore conceptually have no parent.</p>
 *
 * <p>This interface serves primarily as a structural distinction within the
 * type system, allowing parsing logic to differentiate between root-level
 * atoms and those that must maintain parent references. The declared
 * {@code parentAtom} field is implicitly {@code public static final} and
 * remains {@code null}, reflecting the absence of a containing box.</p>
 *
 * <p>Implementations should treat top-level atoms as entry points into the
 * atom tree and ensure they are managed appropriately by file-level
 * parsing logic.</p>
 */

public non-sealed interface TopLevelAtom extends Box
{
	Box parentAtom = null;
}