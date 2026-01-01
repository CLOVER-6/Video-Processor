package com.jd.majors.mp4_processor;

import com.jd.majors.mp4_processor.AtomClasses.Classes.DrefAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.ElstAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.FtypAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.HdlrAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.MdhdAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.MvhdAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.TkhdAtom;
import com.jd.majors.mp4_processor.AtomClasses.Classes.VmhdAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.BasicAtom;
import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.Parsing.AtomRegistry;
import com.jd.majors.mp4_processor.Parsing.Mp4File;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import com.jd.majors.mp4_processor.AtomClasses.Classes.DrefAtom.UrxAtom;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
    	 // Open a file for read and write
        RandomAccessFile file = new RandomAccessFile("/home/jd/eclipse-workspace/mp4-processor/test-videos/test_red_720p.mp4", "r");

        // Get the file channel
        FileChannel channel = file.getChannel();
       
        GeneralAtom mvhd = Mp4File.createAtom(channel, 11263);
        
        System.out.println(mvhd.size());
        System.out.println(mvhd.name());
        
        file.close();
    }
}
