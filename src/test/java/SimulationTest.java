import java.util.LinkedList;

import minemarker.Cuboid;
import minemarker.Minefield;
import minemarker.MinefieldFileParseException;
import minemarker.MinefieldFileParser;
import minemarker.ModelException;
import minemarker.Point;
import minemarker.ScriptException;
import minemarker.ScriptFileParser;
import minemarker.ShipOrders;
import minemarker.ShipTurnOrders;
import minemarker.SimulationEnvironment;

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
public class SimulationTest extends Assert
{
  @Parameters(name="{1}")
  public static Iterable<? extends Object> data()
  {
    LinkedList<Object[]> lTests = new LinkedList<>();

    lTests.add(new Object[]
    {
       ".e." + "\n" +
       "..a" + "\n" +
       "A..",

       "delta",

       3,2,27,2
    });

    return lTests;
  }

  /**
   * String specifying the minefield in input file format
   */
  @Parameter(value = 0) public String mMinefieldSpec;
  /**
   * String specifying the script in input file format
   */
  @Parameter(value = 1) public String mScriptSpec;
  /**
   * Expected X size  of the resulting field
   */
  @Parameter(value = 2) public int mXSize;
  /**
   * Expected Y size  of the resulting field
   */
  @Parameter(value = 3) public int mYSize;
  /**
   * Expected Y size  of the resulting field
   */
  @Parameter(value = 4) public int mZSize;
  /**
   * Expected number of mines in the field
   */
  @Parameter(value = 5) public int mNumMines;


  @Test
  public void test()
  {
    try
    {
      Minefield minefield = MinefieldFileParser.parseString(mMinefieldSpec);
      Point shipStartPoint = new Point(minefield.getBoundingCuboid().getSouthEastBottom().getX()/2,
                                       minefield.getBoundingCuboid().getSouthEastBottom().getY()/2,
                                       0);
      SimulationEnvironment simulator = new SimulationEnvironment(minefield, shipStartPoint);
      ShipOrders orders = ScriptFileParser.parseString(mScriptSpec);

      //  In this test we JUST apply a single turn - the simuylationState class handles
      //  the progression of 'time' and is not the subject of this test
      assertEquals("Test case with only one turn", 1, orders.getNumTurnsCovered());
      ShipTurnOrders turnOrders = orders.getOrdersForTurn(0);

      simulator.getShip().executeTurnOrders(turnOrders);

      //  Check the results
      Cuboid extent = minefield.getBoundingCuboid();

      assertEquals(mXSize-1, extent.getSouthEastBottom().getX() - extent.getNorthWestTop().getX());
      assertEquals(mYSize-1, extent.getSouthEastBottom().getY() - extent.getNorthWestTop().getY());
      assertEquals(mZSize-1, extent.getSouthEastBottom().getZ() - extent.getNorthWestTop().getZ());
      assertEquals(mNumMines, minefield.getNumMines());
    }
    catch (MinefieldFileParseException | ModelException | ScriptException e)
    {
      e.printStackTrace();
      fail("Unexpected exception");
    }
  }
}

