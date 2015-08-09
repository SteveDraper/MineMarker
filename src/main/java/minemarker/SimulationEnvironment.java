package minemarker;

/**
 * Representation of the simulated environment at an instant in time
 * @author steve
 *
 */
public class SimulationEnvironment
{
  private final Ship      mShip;
  private final Minefield mMinefield;

  /**
   * Set up an initial simulation environment with the ship at a specified location
   * and with a specified minefield
   * @param xiMinefield minefield we're going to be clearing
   * @param xiShipStartingCoordinates where the ship should start out at
   */
  public SimulationEnvironment(Minefield xiMinefield, Point xiShipStartingCoordinates)
  {
    mMinefield = xiMinefield;
    mShip = new Ship(xiShipStartingCoordinates, this);
  }

  /**
   * @return the minefield
   */
  public Minefield getMinefield()
  {
    return mMinefield;
  }

  /**
   * @return the ship
   */
  public Ship getShip()
  {
    return mShip;
  }
}
