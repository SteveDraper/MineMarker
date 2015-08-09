package minemarker;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the orders for a ship to execute during the simulation
 * @author steve
 *
 */
public class ShipOrders
{
  private final List<ShipTurnOrders> mTurnOrderList = new ArrayList<>();

  /**
   * Set the orders for a given turn
   * Note - if a turn has no orders set, or null orders are explicitly set for it
   * then it will execute no actions on that turn
   * @param turnNumber turn number orders are being set for (0-based)
   * @param orders orders for that turn (may be null for none)
   * @throws ScriptException
   */
  public void setTurnOrders(int turnNumber, ShipTurnOrders orders) throws ScriptException
  {
    if ( mTurnOrderList.size() > turnNumber )
    {
      if ( mTurnOrderList.get(turnNumber) != null )
      {
        throw new ScriptException("Attempt to set orders for turn " + turnNumber + " multiple times");
      }
    }

    mTurnOrderList.add(turnNumber, orders);
  }

  /**
   * @param turnNumber turn to retrieve orders for (0-based)
   * @return orders for that turn or null if there aren't any
   */
  public ShipTurnOrders getOrdersForTurn(int turnNumber)
  {
    if ( turnNumber < mTurnOrderList.size() )
    {
      return mTurnOrderList.get(turnNumber);
    }

    return null;
  }

  /**
   * @return number of turns that the entire script will cover
   */
  public int getNumTurnsCovered()
  {
    return mTurnOrderList.size();
  }
}
