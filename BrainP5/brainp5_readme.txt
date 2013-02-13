BrainP5 is an easy to use Neural Networks library for Processing. The aim of this library is to abstract the complex workings on NNs and expose some readily usable functions for training and using neural networks.

I am developing this to aid in my PhD work, so it's currently aimed primarily at prediction, and I am by no means a neural network expert, so I am improving it as I go along. The need for it to work really well for my own work means that I will keep improving it, though.

Currently the library is very bare bones and allows the creation of a 3-layer feedforward neural network with any amount of inputs and outputs. The network is automatically formatted and populated with connections, and you can set the range of weights. 

There are also classes provided for creating custom training sets and samples for training and processing, including some early functionality for creating samples from images (for pattern detection), but you will still have to do your own PCA as running thousands of inputs through the network will be slow and inefficient with large images.

The library is perhaps currently best suited for prediction, such as targetting or anticipating movement of objects. 