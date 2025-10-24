package com.jd.majors.mp4_processor.AtomClasses.Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;

public class MdiaAtom implements ContainerAtom {
    private final int size;
    private final String name;
    private final List<GeneralAtom> childBoxes;

    public MdiaAtom(int size, String name, List<GeneralAtom> childBoxes) {
        this.size = size;
        this.name = name;
        this.childBoxes = childBoxes;
    }

    public MdiaAtom(int size, String name, byte[] payload) {
        this.size = size;
        this.name = name;
        this.childBoxes = new ArrayList<GeneralAtom>();
    }

    public int size() { return size; }
    public String name() { return name; }
    public List<GeneralAtom> childBoxes() { return childBoxes; }

    @Override
    public String toString() {
        return "MdiaAtom [size=" + size + ", name=" + name + ", childBoxes=" + childBoxes + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, name, childBoxes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MdiaAtom)) return false;
        MdiaAtom other = (MdiaAtom) obj;
        return size == other.size && Objects.equals(name, other.name) &&
               Objects.equals(childBoxes, other.childBoxes);
    }
}
