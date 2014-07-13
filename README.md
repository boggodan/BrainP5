BrainP5 is an easy to use Neural Networks library for Processing. The aim of this library is to abstract the complex workings on NNs and expose some readily usable functions for training and using neural networks.

I made this when I started my PhD, so I didn't know a lot about really advanced neural network methods, so it's really bare-bones. No it's not a deep learning library. No it doesn't do stochastic gradient descent. 

It allows the creation of a 3-layer feedforward neural network with any amount of inputs and outputs. The network is automatically formatted and populated with connections, and you can set the range of weights.

Usecase ideas:

- Turret targeting for games.
- Artificial Life Simulation (see my sketch Neural Seekers http://youtu.be/ZlbWlDsF-Uw)
- Pretty much anything. I mostly use it for time series prediction.

There are also classes provided for creating custom training sets and samples for training and processing, including some early functionality for creating samples from images (for pattern detection), but you will still have to do your own dimensionality reduction (such as PCA) as running thousands of inputs through the network will be slow and inefficient with large images.

The library is perhaps currently best suited for prediction, such as targetting or anticipating movement of objects. 
