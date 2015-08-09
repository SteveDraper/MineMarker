package minemarker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import minemarker.Ship.ShipAction;

public class ScriptFileParser
{
  /**
   * Parse the specified input file, which is assumed to match the ship script definition
   * format
   * @param filename where to find the specification file
   * @return ShipOrders instance representing the specified script actions
   * @throws IOException
   * @throws ScriptException
   */
  public static ShipOrders parse(String filename) throws IOException, ScriptException
  {
    return parse(FileHelper.readLines(filename));
  }

  /**
   * Parse the specified input, which is assumed to match the minefield definition
   * format.  Provided for ease of use with junit tests
   * @param ordersSpecification orders in the format described for ship script files
   * @return ShipOrders instance representing the specified script actions
   * @throws ScriptException
   */
  public static ShipOrders parseString(String ordersSpecification) throws ScriptException
  {
    String[] lines = ordersSpecification.split("\\r?\\n", -1);

    //  We actually need to be sensitive to empty lines in a script, so trailing linefeed
    //  sequences matter.  However, ignore the virtual null string the above split will give us
    //  after the last linefeed if thats how the string ends or we'll convert a single line feed
    //  into 2 empty lines rather than just 1
    List<String> linesList = new ArrayList<>(Arrays.asList(lines));

    if ( linesList.size() > 0 && linesList.get(linesList.size()-1).length() == 0 )
    {
      linesList.remove(linesList.size()-1);
    }

    return parse(linesList);
  }

  private static ShipOrders parse(List<String> ordersSpecification) throws ScriptException
  {
    ShipOrders result = new ShipOrders();
    int turnNumber = 0;

    for(String turnOrderString : ordersSpecification)
    {
      ShipTurnOrders orders = null;

      if ( !turnOrderString.isEmpty() )
      {
        String[] actions = turnOrderString.split("\\s+");

        for(String actionString : actions)
        {
          try
          {
            ShipAction action = ShipAction.valueOf(actionString);

            if ( orders == null )
            {
              orders = new ShipTurnOrders();
            }
            orders.AddAction(action);
          }
          catch(IllegalArgumentException e)
          {
            //  Re-throw this as a more precise exception
            throw new ScriptException("Illegal action specified on line " + (turnNumber+1) + ": " + actionString);
          }
        }
      }

      result.setTurnOrders(turnNumber, orders);
      turnNumber++;
    }

    return result;
  }
}
