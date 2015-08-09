package minemarker;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of the simulation state at any given step
 * @author steve
 *
 */
public class SimulationState
{
  private final SimulationEnvironment mEnvironment;
  private final ShipOrders            mOrders;
  private int                         mIteration = 0;
  private final int                   mInitialMineCount;

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
    mOrders = shipOrders;
    mInitialMineCount = minefield.getNumMines();
  }

  /**
   * Execute the simulation and mark it, returning the required full output format as a string
   * @return the required marking output
   * @throws ModelException
   */
  public List<String> runAndMark() throws ModelException
  {
    List<String> output = new ArrayList<>();

    do
    {
      //  Output step counter
      output.add("Step " + (mIteration+1));
      output.add(""); //  Blank line
      // Output current minefield
      output.addAll(mEnvironment.getMinefield().toOutputFormat(mEnvironment.getShip().getCoordinates()));
      output.add(""); //  Blank line
      ShipTurnOrders turnOrders = mOrders.getOrdersForTurn(mIteration);
      //  Output current orders
      output.add(turnOrders.toString());
      output.add(""); //  Blank line

      mEnvironment.getShip().executeTurnOrders(turnOrders);

      //  Drop the ship 1 Z-unit
      mEnvironment.getShip().setCoordinates(mEnvironment.getShip().getCoordinates().displace(new Point(0,0,1)));
      // Output resulting minefield
      output.addAll(mEnvironment.getMinefield().toOutputFormat(mEnvironment.getShip().getCoordinates()));
      output.add(""); //  Blank line

      mIteration++;
    } while( !terminal() );

    int score = calculateScore();
    if ( score > 0 )
    {
      output.add("pass (" + score + ")");
    }
    else
    {
      output.add("fail (0)");
    }

    return output;
  }

  /**
   * @return Output format representation of the current minefield state
   * @throws ModelException
   */
  public List<String> getMinefieldProjectionString() throws ModelException
  {
    return mEnvironment.getMinefield().toOutputFormat(mEnvironment.getShip().getCoordinates());
  }

  private boolean terminal()
  {
    return mEnvironment.getMinefield().getNumMines() == 0 ||
           mEnvironment.getMinefield().getBoundingCuboid().getNorthWestTop().getZ() <= mEnvironment.getShip().getCoordinates().getZ() ||
           mIteration >= mOrders.getNumTurnsCovered();
  }

  private int calculateScore()
  {
    if ( mEnvironment.getMinefield().getNumMines() > 0 )
    {
      return 0;
    }

    if ( mIteration < mOrders.getNumTurnsCovered() )
    {
      return 1;
    }

    return 10*mInitialMineCount -
           Math.min(5*mInitialMineCount, 5*mEnvironment.getShip().getNumTorpedosFired()) -
           Math.min(3*mInitialMineCount, 2*mEnvironment.getShip().getNumMovesEnacted());
  }
}
