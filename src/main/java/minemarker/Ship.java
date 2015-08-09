package minemarker;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of the sweeping ship in a simulation
 * @author steve
 *
 */
public class Ship
{
  public enum ShipAction
  {
    //  Movement actions
    /**
     * Move north
     */
    north,
    /**
     * Move south
     */
    south,
    /**
     * Move east
     */
    east,
    /**
     * Move west
     */
    west,

    //  Firing actions
    /**
     * Fire torpedos in pattern (Riker) alpha
     */
    alpha,
    /**
     * Fire torpedos in pattern beta
     */
    beta,
    /**
     * Fire torpedos in pattern gamma
     */
    gamma,
    /**
     * Fire torpedos in pattern delta
     */
    delta;

    /**
     * @return whether the action is a movement action
     */
    public boolean isMovement()
    {
      return (this == north || this == south || this == east || this == west);
    }

    /**
     * @return whether the action is a torpedo launch action
     */
    public boolean isTorpedoLaunch()
    {
      return (this == alpha || this == beta || this == gamma || this == delta);
    }
  }

  private final SimulationEnvironment mEnvironment;
  private Point mCoordinates = null;
  private int   mNumTorpedosFired = 0;

  /**
   * Construct a new Ship at a given starting location
   * @param atCoordinates where the ship starts out at
   * @param xiEnvironment the environment it is embedded in
   */
  public Ship(Point atCoordinates, SimulationEnvironment xiEnvironment)
  {
    mEnvironment = xiEnvironment;
    setCoordinates(atCoordinates);
  }

  /**
   * @return the numTorpedosFired
   */
  public int getNumTorpedosFired()
  {
    return mNumTorpedosFired;
  }

  /**
   * @return the coordinates
   */
  public Point getCoordinates()
  {
    return mCoordinates;
  }

  /**
   * @param xiCoordinates the coordinates to set
   */
  public void setCoordinates(Point xiCoordinates)
  {
    mCoordinates = xiCoordinates;
  }

  //  Execute a single action
  private void executeAction(ShipAction action) throws ModelException
  {
    if ( action.isMovement() )
    {
      int deltaX = 0;
      int deltaY = 0;

      switch(action)
      {
        case north:
          deltaY = -1;
          break;
        case south:
          deltaY = 1;
          break;
        case east:
          deltaX = 1;
          break;
        case west:
          deltaX = -1;
          break;
        default:
          throw new ModelException("Unknown movement action: " + action);
      }

      setCoordinates(new Point(mCoordinates.getX() + deltaX, mCoordinates.getY() + deltaY, mCoordinates.getZ()));
    }
    else if ( action.isTorpedoLaunch() )
    {
      List<Point> torpedoLaunchPoints = new ArrayList<>();
      switch(action)
      {
        case alpha:
          torpedoLaunchPoints.add(mCoordinates.displace(new Point(-1,1,0)));
          torpedoLaunchPoints.add(mCoordinates.displace(new Point(-1,-1,0)));
          torpedoLaunchPoints.add(mCoordinates.displace(new Point(1,1,0)));
          torpedoLaunchPoints.add(mCoordinates.displace(new Point(1,-1,0)));
          break;
        case beta:
          torpedoLaunchPoints.add(mCoordinates.displace(new Point(-1,0,0)));
          torpedoLaunchPoints.add(mCoordinates.displace(new Point(0,1,0)));
          torpedoLaunchPoints.add(mCoordinates.displace(new Point(0,-1,0)));
          torpedoLaunchPoints.add(mCoordinates.displace(new Point(1,0,0)));
          break;
        case gamma:
          torpedoLaunchPoints.add(mCoordinates.displace(new Point(-1,0,0)));
          torpedoLaunchPoints.add(mCoordinates.displace(new Point(0,0,0)));
          torpedoLaunchPoints.add(mCoordinates.displace(new Point(1,0,0)));
          break;
        case delta:
          torpedoLaunchPoints.add(mCoordinates.displace(new Point(0,1,0)));
          torpedoLaunchPoints.add(mCoordinates.displace(new Point(0,-1,0)));
          torpedoLaunchPoints.add(mCoordinates.displace(new Point(0,0,0)));
          break;
        default:
          throw new ModelException("Unknown firing action: " + action);
      }

      //  It's very slightly ambiguous what precisely torpedos do in the spec.
      //  They definitely shoot down (increasing z) to an infinite extent, taking out
      //  all torpedos below us in their 'column'.  It is not clear if they also shoot
      //  upwards.  However, it is not a relevant ambiguity for the specific problem
      //  defined in the current spec, as the simulation anyway terminates if there are
      //  ever any mines that are NOT strictly below us in z-coordinate
      //  We have them just shooting down (seems more logical).  If they turn out to be quantum
      //  torpedos that can be in multiple places at once we may need to revise this for
      //  solving a more general problem space ;-)
      int minefieldRemainingDepth = mEnvironment.getMinefield().getBoundingCuboid().getSouthEastBottom().getZ() - mCoordinates.getZ();
      for(Point torpedoLaunchPoint : torpedoLaunchPoints)
      {
        //  The region cleared by a torpedo is the 1X1 column at the launch coordinates
        //  extending down to the end of the minefield.
        Cuboid clearedRegion = new Cuboid(torpedoLaunchPoint, torpedoLaunchPoint.displace(new Point(0,0,minefieldRemainingDepth)));

        mEnvironment.getMinefield().clearRegion(clearedRegion);
      }
    }
  }

  /**
   * Execute the actions specified for one turn
   * @param turnOrders actiosn to take on this turn
   * @throws ModelException
   */
  public void executeTurnOrders(ShipTurnOrders turnOrders) throws ModelException
  {
    for(ShipAction action : turnOrders.getActions())
    {
      executeAction(action);
    }
  }
}
