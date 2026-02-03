package com.jd.majors.mp4_processor;

import com.jd.majors.mp4_processor.AtomClasses.Classes.DrefAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.ElstAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.FtypAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.HdlrAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.MdhdAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.MdiaAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.MoovAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.MvhdAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.PlaceholderAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.StblAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.StcoAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.StscAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.StsdAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.StssAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.StszAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.SttsAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.TkhdAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.TrakAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.VmhdAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Leaf;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.Box;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.ContainerBox;
import com.jd.majors.mp4_processor.Parsing.AtomRegistry;
import com.jd.majors.mp4_processor.Parsing.Mp4File;

import java.awt.Container;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.List;

public class App 
{
	public static void printMp4Structure(Mp4File mp4File) {
		for (Box atom : mp4File.topLevelAtoms()) {
			printAtom(atom, 0);
		}
	}

	private static void printAtom(Box atom, int depth) {
		// indentation based on nesting depth
		String indent = "  ".repeat(depth);

		System.out.println(
				indent + "- " + atom.toString()
				);

		// recurse into children if this is a container
		if (atom instanceof ContainerBox container) {
			List<? extends Box> children = container.childAtoms();
			for (Box child : children) {
				printAtom(child, depth + 1);
			}
		}
	}

	public static void main( String[] args ) throws Exception
	{
		RandomAccessFile file = new RandomAccessFile("Z:\\smpte.mp4", "r");

		// Get the file channel
		FileChannel channel = file.getChannel();

		AtomRegistry.registerAtom("\u00A9too", (s, n, p) -> new PlaceholderAtom(s, n, p));
		AtomRegistry.registerAtom("gsst", (s, n, p) -> new PlaceholderAtom(s, n, p));
		AtomRegistry.registerAtom("gstd", (s, n, p) -> new PlaceholderAtom(s, n, p));
		AtomRegistry.registerAtom("gssd", (s, n, p) -> new PlaceholderAtom(s, n, p));
		AtomRegistry.registerAtom("gspu", (s, n, p) -> new PlaceholderAtom(s, n, p));
		AtomRegistry.registerAtom("gspm", (s, n, p) -> new PlaceholderAtom(s, n, p));
		AtomRegistry.registerAtom("gshh", (s, n, p) -> new PlaceholderAtom(s, n, p));
		AtomRegistry.registerAtom("data", (s, n, p) -> new PlaceholderAtom(s, n, p));
		
		Mp4File mp4file = Mp4File.parse(channel);

		printMp4Structure(mp4file);

		file.close();
	}

}
