/**
 * Representation of an MP4 atom (ISO Base Media File Format "box").
 *
 * <p>An MP4 atom is the fundamental on-disk unit: a header (32-bit size and
 * four-character type) followed by either typed payload data or a sequence of
 * child atoms. This interface models that unit and exposes the atom type as a
 * {@code String} and the on-disk length in bytes.</p>
 *
 * <p>Container atoms should support recursive parsing: child {@code Box}
 * instances are created from the container payload and may themselves be
 * parsed to populate typed fields. Implementations should avoid retaining
 * raw payload bytes after a successful parse to reduce memory pressure and
 * must maintain a parent reference for nested atoms.</p>
 *
 * <p>Implementation notes: treat the {@code size} value as authoritative for
 * on-disk layout (it includes header bytes), use unsigned-aware arithmetic
 * when manipulating lengths/offsets, and prefer clearing payload buffers
 * after materializing fields to prevent double-parsing and lower memory use.</p>
 */
package com.jd.majors.mp4_processor.AtomClasses.Interfaces;

public sealed interface Box permits FullBox, Leaf, NestedAtom, TopLevelAtom, ContainerBox
{
	String name();
	int size();
}