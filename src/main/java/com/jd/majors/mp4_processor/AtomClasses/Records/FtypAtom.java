package com.jd.majors.mp4_processor.AtomClasses.Records;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.BasicAtom;

public record FtypAtom(String name, int size, byte[] payload) implements BasicAtom
{

}
