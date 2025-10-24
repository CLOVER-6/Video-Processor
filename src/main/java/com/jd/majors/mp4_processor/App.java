package com.jd.majors.mp4_processor;

import com.jd.majors.mp4_processor.AtomClasses.Interfaces.GeneralAtom;
import com.jd.majors.mp4_processor.Parsing.AtomRegistry;

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
        
        
       
        GeneralAtom atom = AtomRegistry.createAtom(channel, 0);
        
        System.out.println(atom.size());
        
        file.close();
    }
}
