package com.jd.majors.mp4_processor;

import com.jd.majors.mp4_processor.AtomClasses.Classes.AvcAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.DrefAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.ElstAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.FtypAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.HdlrAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.MdhdAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.MoovAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.MvhdAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.StblAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.StcoAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.StscAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.StsdAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.StssAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.StszAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.SttsAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.TkhdAtom;
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

public class App 
{
	private static void printBox(Box box, int depth) {
		// Indentation based on depth
		String indent = "  ".repeat(depth);
		System.out.println(indent + box);

		// If the box contains children, print them recursively
		if (box instanceof ContainerBox container) {
			for (Box child : container.childAtoms()) {
				printBox(child, depth + 1);
			}
		}
	}


	public static void main( String[] args ) throws Exception
	{

		AtomRegistry.registerAtom("\u00A9too", (s, n, p) -> new AvcAtom(s, n, p));
		
		RandomAccessFile file = new RandomAccessFile("Z:\\smpte.mp4", "r");
		
		FileChannel channel = file.getChannel();
		Mp4File mp4file = Mp4File.parse(channel);

		for (Box atom : mp4file.topLevelAtoms()) 
		{
			printBox(atom, 0);
		}


		file.close();
	}
}
