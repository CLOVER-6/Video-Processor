package com.jd.majors.mp4_processor.Parsing;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;

/**
 * Functional interface representing a factory for creating {@link Box} instances.
 *
 * <p>{@code AtomFactory} defines a single method, {@link #createAtom(int, String, byte[])},
 * which constructs a concrete atom implementation given its size, four-character
 * type identifier, and raw payload. This interface enables the {@link AtomRegistry}
 * to decouple atom instantiation from parsing logic.</p>
 *
 * <p>Implementations typically return instances of classes implementing
 * {@link com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf},
 * {@link com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerBox}, or
 * {@link com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullBox}, depending on
 * the atom type.</p>
 *
 * <p>This interface is marked with {@code @FunctionalInterface}, allowing the
 * use of lambda expressions or method references for concise registration
 * of atom creation logic in {@link AtomRegistry}.</p>
 */

@FunctionalInterface
public interface AtomFactory 
{
	Box createAtom(int size, String name, byte[] payload);
}
