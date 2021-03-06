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

1. Use of abstract base class for the minefield representation.  The operators I need are fairly obvious, but it is reasonable to ask what happens if the problem were to be scaled up.  At small scales (as per the actual problem spec as given) just keeping lists of mines and iterating over them fairly frequently is not a major issue, so we just need a simple representation.  However, at larger scales we may want to introduce more complex structures to keep things scaling better (for example in length-scale of the minefield cuboid rather than in total number of mines).  Introducing an abstract class with a simple concrete implementation is just an allowance for this and seems like good practise.
2. The spec states that the ship starts 'in the middle' of the planar projection, but it does not state that the provided minefield will always be of an odd size.  Rather than assuming even sizes are illegal I have chosen to interpret this by rounding down, to get the expected result for odd sizes and something that seems reasonable for even sizes
3. Coordinates are only used internally (they do not feature in input/output formats directly) so internally I will use 0-based coordinates (simplifies taking things in and out of lists) where the Y-direction starts from 0 at the first line of the input/output stringized format.  This actually reverses the Y direction (0 is at the top, with increasing Y as you go to successive lines), so we need to make sure to reverse the Y-sense of the firing patterns too so as to be internally consistent.  I am making this decision for simplified parsing, since it means line numbers in the input/output format directly correspond to y-coordinates
4. There is some ambiguity about handling the null-minefield case (minefield spec string is just '.').  Logically it should be handled by the null scipt (no actions needed), but there are two issues with this.  Firstly the output format does not lend itself (there are no steps, and since the minefield state printouts are part of the steps the result would be no state printout, but just the single line saying it passed).  Secondly, the scoring function would actually generate a score of 0 even though you passed (and indeed 0 would be the only possible score for ALL scripts)!  As such this seems to be the Kobayashi Maru scenario for this test.  I will therefore take the same approach Kirk did (redefine the test) and simply consider null minefields to be illegal, treating them as format errors in the minefield layout input.
5. Not sure if/when I'll get to it, but it occurs to me that creating a GDL puzzle from a minefield spec, so that a GGP player can act as a solver would be nice to do.  Accordingly I'd like to be able to add a -gdl action to the JAR to have it rpoduce puzzle GDL.  This means that marking might not be the only action verb I will want, so restructuring the commandline syntax a bit to make it easier to add future options
