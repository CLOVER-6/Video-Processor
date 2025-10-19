package com.jd.majors.mp4_processor.AtomClasses.Records;

import java.util.ArrayList;
import java.util.List;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;

public record MdiaAtom(String name, int size, List<GeneralAtom> childAtoms) implements ContainerAtom
{
	public MdiaAtom(String name, int size, byte[] payload)
	{
		this(name, size, new ArrayList<GeneralAtom>());
	}
}
