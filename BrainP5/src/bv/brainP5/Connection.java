package bv.brainP5;

import processing.core.PApplet;

public class Connection 
{

	Neuron sNeuron,dNeuron;
	float weight;
	FeedForwardNN pNetwork;
	PApplet pApplet;
	String identifier;
	
	/**
	 * A connection, its source and destination neuron and its weight.
	 * Connections also have 'unique' identifiers, but these are not used yet.
	 * @param sNeuron Source neuron. 
	 * @param dNeuron Destination neuron.
	 * @param pNetwork Parent network.
	 */
	public Connection(Neuron sNeuron, Neuron dNeuron, FeedForwardNN pNetwork)
	{
		
		this.pNetwork = pNetwork;
		pApplet = pNetwork.pApplet;
		weight = pApplet.random(pNetwork.lowWeightRange,pNetwork.highWeightRange);
		this.sNeuron = sNeuron;
		this.dNeuron = dNeuron;
		identifier = "" + pApplet.random(300000);
	}
	
	public float getWeight()
	{
		return weight;
	}
	
}
