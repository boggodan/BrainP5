package bv.brainP5;

import java.util.ArrayList;
import processing.core.*;
public class Neuron 
{
	
	float output = 0.0f;
	PApplet pApplet;
	PVector pos;
	ArrayList<Connection> connections;
	FeedForwardNN pNetwork;
	
	String identifier;
	
	/**
	 * Instantiates a neuron. This is called by the NN populate function
	 * and you should not have to deal with any of this unless you are
	 * trying to make something custom.
	 * @param pNetwork Parent network
	 * @param pos Deprecated.
	 */
	public Neuron(FeedForwardNN pNetwork, PVector pos)
	{
	
		
		
		this.pNetwork=pNetwork;
		this.pApplet = pNetwork.pApplet;
		this.pos = new PVector(pos.x, pos.y);
		identifier = "" + pApplet.random(300000);
		connections = new ArrayList<Connection>();
	}	
	
	/**
	 * Adds a connection from this to another neuron.
	 * @param s
	 * @param d
	 */
	public void addConnection(Neuron d)
	{
		Connection c = new Connection(this,d, pNetwork);
		
		connections.add(c);
	    pNetwork.globalConnections.add(c);
		
	}
	
	
	
	/*public void drawConnectionsFromNeuron()
	{
		for(Connection c : connections)
		{
			pApplet.stroke(pApplet.abs(c.weight*255.0f), 100,100,10);
			pApplet.strokeWeight(pApplet.abs(c.weight*5.0f));
			pApplet.line(pos.x, pos.y, c.dNeuron.pos.x, c.dNeuron.pos.y);
		}
	}
	*/
	
	/**
	 * Logistic sigmoid function with range 0-1.
	 * @param in
	 * @return
	 */
	public float logistic(float in)
	{
		float out = 1.0f /  (1.0f + (float)Math.exp(-(double)in));
		return out;
	}
	
	
	/*NN process functions. Remember to reset
	 * sum between activations.*/
	
	
	/**
	 * Applies the sigmoid function and passes to connected neurons. 
	 * Use for hidden and output layer.
	 */
	public void outputAndSigmoid()
	{
		output = (float)logistic(output);
		
		/* go through connections from this neuron and call input with this output
		 * on all destination neurons.
		 */
		
		for(Connection c : connections)
		{
			c.dNeuron.input(output, c.weight);
		}
		
	}
	
	/**
	 * Output from input nodes, therefore no sigmoid.
	 */
	public void outputNoSigmoid()
	{
		
		/* go through connections from this neuron and call input with this output
		 * on all destination neurons.
		 */
		
		for(Connection c : connections)
		{
			c.dNeuron.input(output, c.weight);
		}
		
	}
	
	/**
	 * get a connection by index
	 * @param i
	 * @return
	 */
	public Connection getConnection(int i )
	{
		return connections.get(i);
	}
	
	
	// *adds weighted input to sum
	public void input(float in, float weight)
	{
		output += in*weight;
		
	}
	
	/**
	 * sets neuron output to 0.0f.
	 */
	public void clear()
	{
		output = 0.0f;
	}
	
	/**
	 * returns this neuron's current output as a float.
	 * @return
	 */
	public float getOutput()
	{
	return output;	
	}
	
}
