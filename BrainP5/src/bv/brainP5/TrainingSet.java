package bv.brainP5;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;


/**
 * A training set is essentially a list of inputs with known outputs. The input and output count must match the NN's
 * architecture or you will probably get an error or worse. Use with the train() function which takes
 * in a training set.
 * @author bv301
 *
 */
public class TrainingSet 
{

	
	ArrayList<Sample> samples;
	int numIn,numOut;
	PApplet pApplet;
	public 	TrainingSet(int numIn, int numOut, PApplet pApplet)
	{
		this.pApplet = pApplet;
		this.numIn = numIn;
		this.numOut = numOut;
		samples = new ArrayList<Sample>();
	}
	
	/**
	 * You will have to format your own sample and pass it via this function.
	 * @param s
	 */
	public void addSample(Sample s)
	{
		samples.add(s);
		
	}
	
	/**
	 * Not used yet, but will be thrown when bad format.
	 * @author bv301
	 *
	 */
	public class InvalidSampleFormatException extends Exception
	{
		String error;
		public InvalidSampleFormatException() {
			// TODO Auto-generated constructor stub
			super();
			error = "The added sample had an invalid format (input, output count).";
		}
	}
	
	
}
