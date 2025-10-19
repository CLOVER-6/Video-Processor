package com.jd.majors.mp4_processor.AtomClasses.Records;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.FullAtom;

public record MdhdAtom(String name, int size, byte[] payload) implements FullAtom
{

}
