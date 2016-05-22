package com.rowanskewes.CLI;

import com.rowanskewes.filters.Filter;
import com.rowanskewes.filters.Lpf;

import java.util.*;
import java.io.*;

/**
 * Command line low pass filter, creates 16 bit noise of variable frequency profile
 * when white noise is input.
 *
 * Applications include sound masking for concentrating in noisy environments.
 *
 * To use, grab random data from /dev/urandom and pipe output to aplay like so:
 *     java -cp bin com.rowanskewes.CLI.BrownianNoise 0.95 3 < /dev/urandom | aplay -f cd
 * @author Rowan Skewes <rowan.skewes@gmail.com>
 * @version 1.0
 */

public class BrownianNoise {

	// Terminal width (for printing output stream; static)
	private final static int WIDTH = 220;

	// Maximum signal level
	private final static int CLIP_LEVEL = 128;

	// Wait period when printing to terminal
	private final static int SLEEPMS = 10;

	// Signal gain for adjusting volume output
	private final static int GAIN = 9;


	public static void main(String[] args){

		DataInputStream inStream = new DataInputStream(System.in);
		PrintStream outStream =  new PrintStream(System.out);

		// Check command line argument validity
		try{
			Integer.parseInt(args[1]);
			Float.parseFloat(args[0]);
		} catch (Exception e){
			printHelpMessage();
			System.exit(0);
		}

		// Read number of filter poles from command line arg
		int npoles = Integer.parseInt(args[1]);

		// Initialise low pass filters
		Filter[] lpfs = new Lpf[npoles];
		for(int i=0; i<npoles; i++){
			lpfs[i] = new Lpf(0, Float.parseFloat(args[0]));
		}

		// Initialize process variables
		float input = 0;
		float filtered = 0;

		while(true){
			// Get input byte
			try{
				input = (float)inStream.readByte()/4;
			}catch(IOException e){
				e.printStackTrace();
			}

			// Filter it
			for(Filter lpf: lpfs){
				filtered = lpf.processSample(input);

				// Pass filtered value to the next filter
				input = filtered;
			}

			// Output filtered byte
			if(args.length < 3){
				// No special instructions, send out raw bytes
				outStream.write((byte)(filtered * GAIN));

			}else{
				// Special instructions, print output to terminal
				printFilterOut(filtered, outStream);

				// Pause execution to slow down terminal output to a visual speed 
				try{
					Thread.sleep(SLEEPMS);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Print the output of the filter to the terminal as a vertical stream
	 * of hashes. Horizontal position indicates the output magnitude.
	 */
	private static void printFilterOut(float filterval, PrintStream outStream){
		// Currently using static window size
		float position = filterval*(WIDTH/CLIP_LEVEL) + WIDTH/2;

		// Print spaces to pad out to the hash
		for(int i=0; i<(int)position; i++){
			outStream.print(" ");
		}

		outStream.print("#\n");
	}

	/**
	 * Print the help message to the command line.
	 */
	private static void printHelpMessage(){
			System.out.println("BrownianNoise: a command line number stream (audio) filter.\n" +
					"Usage: BrownianNoise [RESPONSE] [N_POLES] [OPTIONS]\n" +
					"where [RESPONSE] determines the response of the filter\n" +
					"    with a 'memory constant' between zero and 1, so\n" +
					"    higher values gives a lower cutoff,\n" +
					"[N_POLES] gives the number of poles or filter stages.\n" +
					"The only [OPTION] at the moment is to print the output\n" +
					"    as a vertical stream to the terminal. Any arg will do this.\n\n" +
					"Takes input stream via command line redirection, so you need to\n" +
					"    pass it some data like this:\n" +
					"    BrownianNoise 0.7 3 print < /dev/urandom\n" +
					"    or BrownianNoise 0.95 3 < /dev/urandom | aplay -f cd");
	}
}
