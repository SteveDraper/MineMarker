package minemarker;

/**
 * Representation of the simulation state at any given step
 * @author steve
 *
 */
public class SimulationState
{
  private final SimulationEnvironment mEnvironment;
  int                                 mIteration = 0;

  /**
   * Create a new simulation state representing the starting state for a simulation
   * @param minefield the minefield we will be clearing
   * @param shipOrders the orders the sweeping ship has been given
   */
  public SimulationState(Minefield minefield, ShipOrders shipOrders)
  {
    //  The ship, starts 'in the middle' of the (x,y-plane of the) minefield
    //  It is unclear what this means for minefields of even size in x or y
    //  dimensions, but the specification does not state that those are illegal
    //  I will interpret by rounding down, which has the obvious result for
    //  the (expected) odd sizes, and seems reasonable for even sizes
    int middleX = (minefield.getBoundingCuboid().getNorthWestTop().getX() + minefield.getBoundingCuboid().getSouthEastBottom().getX())/2;
    int middleY = (minefield.getBoundingCuboid().getNorthWestTop().getY() + minefield.getBoundingCuboid().getSouthEastBottom().getY())/2;

    mEnvironment = new SimulationEnvironment(minefield, new Point(middleX, middleY, 0));
  }

  /**
   * Execute the simulation and mark it, returning the required full output format as a string
   * @return the required marking output
   */
  public String RunAndMark()
  {
    return "Not yet implemented";
  }
}
