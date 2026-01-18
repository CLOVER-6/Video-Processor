package com.jd.majors.mp4_processor;

import com.jd.majors.mp4_processor.AtomClasses.Classes.DrefAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.ElstAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.FtypAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.HdlrAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.MdhdAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.MvhdAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.StcoAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.StscAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.StsdAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.StssAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.StszAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.SttsAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.TkhdAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.VmhdAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.BasicAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.Parsing.AtomRegistry;
import com.jd.majors.mp4_processor.Parsing.Mp4File;

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
       
        Mp4File mp4file = new Mp4File(channel);
        
        StcoAtom test = (StcoAtom) mp4file.createAtom(14564);

        test = test.parse();    
        
        System.out.println(test.size());
        System.out.println(test.name());
        
        System.out.println(test.toString());
        
        file.close();
    }
}
