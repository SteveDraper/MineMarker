# Design Notes

This document is a place to record any significant design decisions where there were obvioius choices, and why the chosen approach was picked.

##Overall Structure

It strikes me that there are two possible approaches to take to the simulation model representation, with differing pros and cons:

1. Model the 'real world' environment of the problem space - i.e. - a 3-dimensional volume in which there are fixed mines and a moving ship

2. Model only the 2-dimensional projection which is essentially what the input and output format is, with script instructions operating directly on the projection.  In fact you could (I think) go even further with this approach and simply consider the state string (the printout format of the current minefield state at each step) as a more-or-less opaque string with a bunch of production rules operating directly as string transforms.  For instance the 'drop down' of the ship that happens at each step would simply act as a string transform on the letters that represent extant mines (essentially moving backwards one step in alphabetic sequence)

The advantage of the first approach is that it is robust to possible future changes in the problem spec, and could readily be extended to support things like:

* Change of input/output format
* Scaling up of the problem such that mine depths can no longer be represented by single characters
* More complex torpedo behavior, such as diagonal movement or even individually scripted torpedos
* Addition of mobile mines or enemy ships
* Multiple sweeping ships
* Sweeping ship that can move freely in all 3 dimensions

The advantage of the second approach is that it requires less translation of inputs and outputs, and could quite possibly result in considerably smaller code.  However, it has very little flexibility, and although the resulting code might be small, it would probably not be intuitive to interpret

I will take approach (1)

##Detailed Decisions

1. Use of abstract base class for the minefield representation.  The operators I need are failrly obvious, but it is reasonable to ask what happens if the problem were to be scaled up.  At small scales (as per the actual problem spec as given) just keeping lists of mines and iterating over them fairly frequently is not a major issue, so we just need a simple representation.  However, at larger scales we may want to introduce more complex structures to keep things scaling better (for example in length-scale of the minefield cuboid rather than in total number of mines).  Introducing an abstract class with a simple concrete implementation is just an allowance for this and seems like good practise.
