package minemarker;

import java.io.BufferedReader;
import java.io.FileReader;
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
    List<String> lines = new ArrayList<String>();

    BufferedReader input =  new BufferedReader(new FileReader(filename));
    try
    {
        String line = null;
        while (( line = input.readLine()) != null)
        {
            lines.add(line);
        }
    }
    finally
    {
        input.close();
    }

    return parse(lines);
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
    String[] lines = ordersSpecification.split("\\r?\\n");

    return parse(Arrays.asList(lines));
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

      if ( orders != null )
      {
        result.setTurnOrders(turnNumber, orders);
      }
      turnNumber++;
    }

    return result;
  }
}
