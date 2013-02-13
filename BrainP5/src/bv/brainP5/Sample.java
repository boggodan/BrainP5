package bv.brainP5;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Stores information about an input sample with n inputs and potentially m known outputs (for training).
 * In this version of the library samples can only contain floats, but this should in theory be enough for most applications.
 * @author bv301
 *
 */
public class Sample 
{
	
	ArrayList<Float> ins;;
	ArrayList<Float> outs;
	PApplet pApplet;
	/**
	 * Makes an empty sample
	 */
	public Sample(PApplet pApplet)
	{
		this.pApplet = pApplet;
		ins = new ArrayList<Float>();
		outs = new ArrayList<Float>();
	}
	
	
	
	/**
	 * Creates a training sample from an image by converting it to greyscale first.
	 * You pass it the applet, the image, the number of outputs in the NN, 
	 * the output neuron you'd like to assign to this image and the value you'd
	 * like to train it towards.
	 * @param pApplet
	 * @param img
	 * @param numOutputs
	 * @param outputNeuron
	 * @param value
	 */
	public Sample(PApplet pApplet, PImage img, int numOutputs, int outputNeuron, float value)
	{
		this.pApplet = pApplet;
		ins = new ArrayList<Float>();
		outs = new ArrayList<Float>();
		
		for(int i =0; i <img.height*img.width; i++)
		{
			float inRange = (float)(pApplet.red(img.pixels[i]) + pApplet.blue(img.pixels[i]) + pApplet.green(img.pixels[i]))/3.0f;
			inRange = inRange/255.0f;
			
			addInput(inRange);
			
		}

		for(int i =0; i<numOutputs;i++)
		{
			if(i==outputNeuron)
			addOutput(value);
			else
			addOutput(0.0f);
		}
	}
	
	/**
	 * Creates a test sample from an image.
	 * You pass it the applet and the image.
	 * @param pApplet
	 * @param img
	 */
	public Sample(PApplet pApplet, PImage img)
	{
		this.pApplet = pApplet;
		ins = new ArrayList<Float>();
		outs = new ArrayList<Float>();
		
		for(int i =0; i <img.height*img.width; i++)
		{
			float inRange = (float)(pApplet.red(img.pixels[i]) + pApplet.blue(img.pixels[i]) + pApplet.green(img.pixels[i]))/3.0f;
			inRange = inRange/255.0f;
			
			addInput(inRange);
			
		}

	
	}
	
	public void addInput(float in)
	{
		ins.add(in);
	}
	
	/**
	 * A sample only has output values if it's used for training.
	 * @param out
	 */
	public void addOutput(float out)
	{
		outs.add(out);
	}
	
	public float getInput( int i )
	{
		return ins.get(i);
	}
	
	public float getOutput( int i )
	{
		return outs.get(i);
	}
}
