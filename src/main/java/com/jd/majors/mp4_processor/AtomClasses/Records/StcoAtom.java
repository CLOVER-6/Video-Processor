package com.jd.majors.mp4_processor.AtomClasses.Records;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullAtom;

public record StcoAtom(String name, int size, int chunkBits, byte[] payload) implements FullAtom
{

}
