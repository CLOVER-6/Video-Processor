package com.jd.majors.mp4_processor.AtomClasses.Interfaces;

/**
 * Represents an atom that is structurally nested within a container atom
 * in the MP4/ISO Base Media File Format hierarchy.
 *
 * <p>A {@code NestedAtom} maintains a reference to its immediate parent
 * {@link Box}, enabling upward traversal of the atom tree. This supports
 * structural validation, contextual parsing, and hierarchical navigation
 * (e.g., resolving track-level atoms within a {@code moov} container).</p>
 *
 * <p>The {@link #parentAtom()} method returns the container that directly
 * owns this atom, or {@code null} if the atom has not yet been attached to
 * a parent. The {@link #setParent(Box)} method assigns the parent reference
 * and is typically invoked by container implementations when adding child
 * atoms.</p>
 *
 * <p>Implementations should ensure parent references remain consistent with
 * the actual container structure and avoid creating cyclic relationships
 * outside the defined atom tree.</p>
 */

public non-sealed interface NestedAtom extends Box
{
	public Box parentAtom();
	public void setParent(Box parentAtom);
}