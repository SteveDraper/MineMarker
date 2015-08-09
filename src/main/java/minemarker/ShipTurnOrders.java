package minemarker;

import java.util.ArrayList;
import java.util.List;

import minemarker.Ship.ShipAction;

/**
 * Represents the orders given to a ship for one turn of the simulation
 * @author steve
 *
 */
public class ShipTurnOrders
{
  private final List<ShipAction> mActions = new ArrayList<>();

  /**
   * Add an action to those to be taken on the turn this instance represents orders for
   * @param action action to add
   * @throws ScriptException thrown if the added action is illegal
   */
  public void AddAction(ShipAction action) throws ScriptException
  {
    //  It is only legal to have at most one movement and one fire action (in either order)
    if ( mActions.size() > 1 )
    {
      throw new ScriptException("Too many actions in one turn");
    }

    for(ShipAction existingAction : mActions)
    {
      if ( existingAction.isMovement() && action.isMovement() )
      {
        throw new ScriptException("Only a single movement action per turn is allowed");
      }
      if ( existingAction.isTorpedoLaunch() && action.isTorpedoLaunch() )
      {
        throw new ScriptException("Only a single firing action per turn is allowed");
      }
    }

    mActions.add(action);
  }

  /**
   * @return the (ordered) actions
   */
  public List<ShipAction> getActions()
  {
    return mActions;
  }

  @Override
  public String toString()
  {
    StringBuilder result = new StringBuilder();

    for(ShipAction action : mActions)
    {
      if ( result.length() > 0 )
      {
        result.append(" ");
      }
      result.append(action);
    }

    return result.toString();
  }
}
