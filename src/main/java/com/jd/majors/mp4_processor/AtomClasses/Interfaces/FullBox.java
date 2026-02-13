package com.jd.majors.mp4_processor.AtomClasses.Interfaces;

/**
 * Represents a FullBox as defined by the ISO Base Media File Format.
 *
 * <p>A {@code FullBox} extends the basic {@link Box} structure by adding
 * a one-byte {@code version} field and a three-byte {@code flags} field
 * immediately following the standard size and type fields. Many MP4 atoms
 * (e.g., {@code mvhd}, {@code tkhd}, {@code elst}) follow this extended
 * structure.</p>
 *
 * <p>The {@code version} field allows format evolution while maintaining
 * backward compatibility, potentially altering the interpretation or width
 * of subsequent fields. The {@code flags} field contains bit-level modifiers
 * that refine the semantics or behavior of the box.</p>
 *
 * <p>Implementations should treat the {@code flags} array as a 24-bit
 * unsigned value stored in big-endian order. Care must be taken when
 * parsing, as Java bytes are signed and may require masking with
 * {@code 0xFF} when assembling multi-byte values.</p>
 */

public non-sealed interface FullBox extends Box 
{
	short version(); 
	byte[] flags();
}
