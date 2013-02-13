package bv.brainP5;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * This is a feedforward, 3-layer neural network with variable
 * neuron numbers for each layer. 
 * 
 * IMPORTANT: Input values must be scaled 0.0-1.0 to work. 
 * @author bv301
 *
 */
public class FeedForwardNN 
{

	PApplet pApplet;
	ArrayList<Neuron> inputNeurons, hiddenNeurons,outputNeurons,globalNeurons;
	ArrayList<Connection> globalConnections;
	//learning constant
	float lc; 
	float lowWeightRange,highWeightRange;
	
	/**
	 * 
	 * @param pApplet reference to the top level pApplet. Just pass 'this'
	 * @param learningRate Learning rate. You might want to keep this 0.0-1.0, but you probably have to experiment with this.
	 * @param lowWeightRange Low limit for generating random weights.
	 * @param highWeightRange High limit for generating random weights.
	 */
	public FeedForwardNN(PApplet pApplet, float learningRate, float lowWeightRange, float highWeightRange)
	{
		
		lc = learningRate;
		this.pApplet = pApplet;
		inputNeurons = new ArrayList<Neuron>();
		hiddenNeurons = new ArrayList<Neuron>();
		outputNeurons = new ArrayList<Neuron>();
		globalNeurons = new ArrayList<Neuron>();
		globalConnections = new ArrayList<Connection>();
		this.lowWeightRange = lowWeightRange;
		this.highWeightRange = highWeightRange;
		welcome();
	}
	
	
	
	 Neuron addInputNode()
	{
	  Neuron n = new Neuron(this, new PVector(0,0));
	  inputNeurons.add(n);
	  globalNeurons.add(n);
	  
	//  pApplet.incrementInput();
	  return n;
	}
	
	 Neuron addHiddenNode()
	{
       Neuron n = new Neuron(this, new PVector(0,0));
	   hiddenNeurons.add(n);
	   globalNeurons.add(n);
	   
	  //pApplet.incrementHidden();
	   return n;
	}
	
	 Neuron addOutputNode()
	{
	   Neuron n = new Neuron(this, new PVector(0,0));
	   outputNeurons.add(n);
	   globalNeurons.add(n);
	   
	   //pApplet.incrementOutput();
	   return n;
	}
	
	 
	public Neuron getInputNeuron(int i)
	{
		return inputNeurons.get(i);
	}
	
	public Neuron getHiddenNeuron(int i)
	{
		return hiddenNeurons.get(i);
	}
	
	public Neuron outputNeuron(int i)
	{
		return outputNeurons.get(i);
	}
	
	/**
	 * Get a neuron from the global list by index.
	 * @param i
	 * @return
	 */
	public Neuron getGlobalNeuron(int i )
	{
		return globalNeurons.get(i);
	}
	
	/**
	 * Get a connection from the global list by index.
	 * @param i
	 * @return
	 */
	public Connection getGlobalConnection(int i )
	{
		return globalConnections.get(i);
	}
	
	
	/**
	 * Populates the network with nodes and connects them properly.
	 * @param input Number of input nodes.
	 * @param hidden Number of hidden nodes.
	 * @param output Number of output nodes.
	 */
	public void populate(int input, int hidden, int output)
	{
		//add neurons
		for(int i=0;i<input; i++)
		 {
		 
		 addInputNode();
		 
		 }
		 
		 for(int i=0;i<hidden; i++)
		 {
		 
		 Neuron newron = addHiddenNode();
		 
			 //add connections from previous layer
			 for(Neuron n : inputNeurons)
			 {
			 n.addConnection(newron);
		 	 }
		 
		 }
		 
		 for(int i=0;i<output; i++)
		 {
		 
			 Neuron newron = addOutputNode();
		 	
			 //add connections from previous layer
			 for(Neuron n : hiddenNeurons)
			 {
				 n.addConnection(newron);
			 }
		 }
	}
	
	
	/**
	 * Resets all the sums and prepares for new process.
	 */
	public void prepare()
	{
		for (Neuron n : globalNeurons)
		{
			n.clear();
		}
	}
	
	/**
	 * Resets all the weights to random and outputs to 0.0f;
	 */
	public void reset()
	{
		for (Neuron n : globalNeurons)
		{
			n.clear();
		}
		
		for (Connection c : globalConnections)
		{
			c.weight = pApplet.random(lowWeightRange,highWeightRange);
		}
	}
	
	/**
	 * Takes input and processes it through the network. The outputs 
	 * are saved into each neuron and you can get the final output from the output neurons with getOutput().
	 * 
	 */
	public void process(Sample s)
	{
		
		prepare();
		
		//input layer
		for ( int i = 0; i <inputNeurons.size(); i++)
		{
			Neuron n = inputNeurons.get(i);
			
			//plug the inputs into the right neuron
			n.input(s.ins.get(i), 1.0f);
			
			//output from this neuron to the connected ones
			
			n.outputNoSigmoid();
		}
		
		//hidden layer
		for ( int i = 0; i <hiddenNeurons.size(); i++)
		{
			Neuron n = hiddenNeurons.get(i);
						
			//output from this neuron to the connected ones

			n.outputAndSigmoid();
		}
		
		//output layer
		for ( int i = 0; i <outputNeurons.size(); i++)
		{
			Neuron n = outputNeurons.get(i);
								
			//output from this neuron to the connected ones

			n.outputAndSigmoid();
		}
		
		//outputs should now be available throughout the network
		
	}
	
	/**
	 * Takes in a training set and trains the network with backpropagation.
	 * This function stops the program while training, and may halt the process if there are very 
	 * many samples. If you want to see the training happen in real time use the trainLive() function. To train sample by sample.
	 * 
	 * @param t Pass a full training set to process at once.
	 */
	public 	void train(TrainingSet t)
	{
		for(Sample s : t.samples)
		{
			trainLive(s);
		}
	}
	
	
	/**
	 * Trains sample by sample using back propagation.
	 * @param s Pass a sample. Remember that the sample must have the same number of inputs and outputs as the network.
	 */
	public void trainLive(Sample s)
	{
	//	lc*=0.9999f;
		//clear the network
		this.prepare();
		//do a run through with the current values
		process(s);
		
		//make an array of output errors
		
		float[] outputErrors = new float[s.outs.size()];
		
		
		//calculate output errors
		for(int i =0; i <outputErrors.length; i++)
		{
			float o = outputNeurons.get(i).getOutput();
			outputErrors[i] = o*(1.0f-o)*(s.outs.get(i) - o);
		} 
		
		//change output layer weights
		//get weights from hidden to output layer
		
		for(Neuron n : hiddenNeurons)
		{
			/*
			 * this counts which of the destination neurons we are dealing with 
			 * in this cycle so we can access the error from the errors vector
			 */
			int count = 0;
			for(Connection c : n.connections)
			{
				c.weight = c.weight + lc*outputErrors[count]*n.output;
				count++;
			}
		}
		
		/**
		 * Propagate hidden layer errors. This means another vector of errors, one for each hidden neuron.
		 */
		
		float[] hiddenErrors = new float[hiddenNeurons.size()];
		
		//calculate output errors
			for(int i =0; i <hiddenErrors.length; i++)
			{
				float o = hiddenNeurons.get(i).getOutput();
				
				//do the error summation
				float sum = 0;
				int count = 0;
				for(Connection c : hiddenNeurons.get(i).connections)
				{
					sum+=outputErrors[count]*c.weight;
					count++;
				}
				
				hiddenErrors[i] = o*(1.0f-o) * sum;
			}
			
		/**
		 * Change hidden layer weights.
		 */
		
			//get weights from input to hidden layer
			
			for(Neuron n : inputNeurons)
			{
				/*
				 * this counts which of the destination neurons we are dealing with 
				 * in this cycle so we can access the error from the errors vector
				 */
				int count = 0;
				for(Connection c : n.connections)
				{
					//remember that the input layer outputs don't go through a sigmoid, so they are the same as the inputs
					c.weight = c.weight + lc*hiddenErrors[count]*n.output;
					count++;
				}
			}
			
			//the network is now trained!
			
			
	}
	
public final static String VERSION = "##library.prettyVersion##";
	

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the library.
	 * 
	 * @example Hello
	 * @param theParent
	 */
	
	
	private void welcome() {
		System.out.println("##library.name## ##library.prettyVersion## by ##author##");
	}
	
	
	public String sayHello() {
		return "Lol neural networks.";
	}
	/**
	 * return the version of the library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}
	
}
