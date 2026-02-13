package com.jd.majors.mp4_processor.AtomClasses.Interfaces;

/**
 * Represents a leaf atom in the MP4/ISO Base Media File Format hierarchy.
 *
 * <p>A {@code Leaf} is a {@link Box} that does not contain child atoms.
 * Instead, it encapsulates raw payload data that may be parsed into
 * structured fields specific to the concrete atom type (e.g., timing,
 * sample tables, or metadata records).</p>
 *
 * <p>The {@link #parse()} method interprets the internal payload and
 * populates the atom’s structured state. Implementations should treat
 * parsing as a one-time operation and may clear or release the raw
 * {@code payload} after successful parsing to reduce memory retention
 * and prevent re-parsing.</p>
 *
 * <p>The {@link #payload()} method exposes the raw, unparsed byte content
 * of the atom. Once {@code parse()} has been invoked, this value may be
 * {@code null} depending on the implementation.</p>
 *
 * <p>Implementations are responsible for honoring the {@code size} field
 * defined by {@link Box} and for interpreting multi-byte values using
 * big-endian order unless otherwise specified by the format.</p>
 */

public non-sealed interface Leaf extends Box
{
	Leaf parse() throws Exception;
	byte[] payload();
}
