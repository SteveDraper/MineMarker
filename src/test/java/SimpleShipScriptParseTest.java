
import java.util.LinkedList;

import minemarker.ScriptException;
import minemarker.ScriptFileParser;
import minemarker.Ship.ShipAction;
import minemarker.ShipOrders;
import minemarker.ShipTurnOrders;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;


/**
 * Simple tests for minefield parsing and access to the parsed data
 * @author steve
 *
 */
@RunWith(Parameterized.class)
public class SimpleShipScriptParseTest extends Assert
{
  /**
   * @return Iterable set of test cases
   */
  @Parameters(name="{0}")
  public static Iterable<? extends Object> data()
  {
    LinkedList<Object[]> lTests = new LinkedList<>();

    lTests.add(new Object[]
    {
       "Simple Scrtipt with noop",

       "north alpha" + "\n" +
       "\n" +
       "beta south",

       3, false
    });

    return lTests;
  }

  /**
   * Name for the test case
   */
  @Parameter(value = 0) public String mTestName;
  /**
   * String specifying the script in input file format
   */
  @Parameter(value = 1) public String mScript;
  /**
   * Number of turns this script covers
   */
  @Parameter(value = 2) public int mNumTurns;
  /**
   * Whether this test should result in a parse exception
   */
  @Parameter(value = 3) public boolean mShouldExcept;

  /**
   * Run tests on the script file parser anmd associated bits of the model
   */
  @Test
  public void test()
  {
    try
    {
      ShipOrders orders = ScriptFileParser.parseString(mScript);

      assertEquals(mNumTurns, orders.getNumTurnsCovered());

      //  Check the actions for each turn are as expected
      String[] turnOrderStrings = mScript.split("\\r?\\n");

      assert(mNumTurns == turnOrderStrings.length); //  Check test data self-consistency

      for(int turn = 0; turn < mNumTurns; turn++)
      {
        ShipTurnOrders turnOrders = orders.getOrdersForTurn(turn);

        if ( turnOrders == null )
        {
          assertEquals("Orders missing for turn " + turn, 0, turnOrderStrings[turn].length());
        }
        else
        {
          String[] turnActions = turnOrderStrings[turn].split("\\s+");

          assertEquals(turnOrders.getActions().size(), turnActions.length);
          for(int actionIndex = 0; actionIndex < turnActions.length; actionIndex++)
          {
            assertEquals(ShipAction.valueOf(turnActions[actionIndex]), turnOrders.getActions().get(actionIndex));
          }
        }
      }
    }
    catch (ScriptException e)
    {
      if ( !mShouldExcept )
      {
        e.printStackTrace();
        fail("Unexpected exception");
      }
    }
  }
}
