package com.jd.majors.mp4_processor;

import com.jd.majors.mp4_processor.AtomClasses.Classes.DrefAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.ElstAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.FtypAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.HdlrAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.MdhdAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.MoovAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.MvhdAtom;
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
    public static void main( String[] args ) throws Exception
    {
    	 // Open a file for read and write
        RandomAccessFile file = new RandomAccessFile("/home/jd/eclipse-workspace/mp4-processor/test-videos/test_red_720p.mp4", "r");

        // Get the file channel
        FileChannel channel = file.getChannel();
       
        Mp4File mp4file = Mp4File.parse(channel);
        
        for (Box atom : mp4file.topLevelAtoms())
		{
			System.out.println(atom.toString());
			
			if (atom instanceof MoovAtom)
			{
				MoovAtom moov = (MoovAtom) atom;
				
				for (Box child : moov.childAtoms())
				{
					System.out.println("  " + child.toString());
					if (child instanceof ContainerBox)
					{
						ContainerBox container = (ContainerBox) child;
						
						for (Box grandChild : container.childAtoms())
						{
							System.out.println("    " + grandChild.toString());
							
							if (grandChild instanceof ContainerBox)
							{
								ContainerBox grandContainer = (ContainerBox) grandChild;
								
								for (Box greatGrandChild : grandContainer.childAtoms())
								{
									System.out.println("      " + greatGrandChild.toString());
								}
							}
						}
					}
				}
			}
		}
        
        file.close();
    }
}
