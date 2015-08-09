import java.util.LinkedList;
import java.util.List;

import minemarker.Cuboid;
import minemarker.Minefield;
import minemarker.MinefieldFileParseException;
import minemarker.MinefieldFileParser;
import minemarker.ModelException;
import minemarker.ScriptException;
import minemarker.ScriptFileParser;
import minemarker.ShipOrders;
import minemarker.SimulationState;

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
  /**
   * @return Iterable set of test cases
   */
  @Parameters(name="{0}")
  public static Iterable<? extends Object> data()
  {
    LinkedList<Object[]> lTests = new LinkedList<>();

    lTests.add(new Object[]
    {
       "Simple",

       ".e." + "\n" +
       "..a" + "\n" +
       "A..",

       "gamma",

       2,3,23,2,

       "Step 1" + "\n\n" +
       ".e." + "\n" +
       "..a" + "\n" +
       "A..\n\n" +
       "gamma\n\n" +
       ".d." + "\n" +
       "..." + "\n" +
       "z..\n\n" +
       "fail (0)"
    });
    lTests.add(new Object[]
    {
       "Empty orders",

       ".e." + "\n" +
       "..a" + "\n" +
       "A..",

       "\n",

       3,3,27,3,

       "Step 1" + "\n\n" +
       ".e." + "\n" +
       "..a" + "\n" +
       "A..\n\n" +
       "\n\n" +
       ".d." + "\n" +
       "..*" + "\n" +
       "z..\n\n" +
       "fail (0)"
    });

    return lTests;
  }

  /**
   * Name for the test instance
   */
  @Parameter(value = 0) public String mTestName;
  /**
   * String specifying the minefield in input file format
   */
  @Parameter(value = 1) public String mMinefieldSpec;
  /**
   * String specifying the script in input file format
   */
  @Parameter(value = 2) public String mScriptSpec;
  /**
   * Expected X size  of the resulting field
   */
  @Parameter(value = 3) public int mXSize;
  /**
   * Expected Y size  of the resulting field
   */
  @Parameter(value = 4) public int mYSize;
  /**
   * Expected Y size  of the resulting field
   */
  @Parameter(value = 5) public int mZSize;
  /**
   * Expected number of mines in the field
   */
  @Parameter(value = 6) public int mNumMines;
  /**
   * String specifying the resulting minefield in output file format
   */
  @Parameter(value = 7) public String mResultingOutput;


  /**
   * Run simple tests on the simulation model
   */
  @Test
  public void test()
  {
    try
    {
      Minefield minefield = MinefieldFileParser.parseString(mMinefieldSpec);
      ShipOrders orders = ScriptFileParser.parseString(mScriptSpec);
      SimulationState simulator = new SimulationState(minefield, orders);

      //  In this test we JUST apply a single turn
      assertEquals("Test case with only one turn", 1, orders.getNumTurnsCovered());

      List<String> simulationResult = simulator.runAndMark();

      //  Check the resulting minefield state
      Cuboid extent = minefield.getBoundingCuboid();

      assertEquals(mXSize-1, extent.getSouthEastBottom().getX() - extent.getNorthWestTop().getX());
      assertEquals(mYSize-1, extent.getSouthEastBottom().getY() - extent.getNorthWestTop().getY());
      assertEquals(mZSize-1, extent.getSouthEastBottom().getZ() - extent.getNorthWestTop().getZ());
      assertEquals(mNumMines, minefield.getNumMines());

      //  Check the output format
      String[] checkLines = mResultingOutput.split("\\r?\\n");
      int lineNum = 0;

      assertEquals(checkLines.length, simulationResult.size());
      for(String outputLine : simulationResult)
      {
        assertEquals(checkLines[lineNum++], outputLine);
      }
    }
    catch (MinefieldFileParseException | ModelException | ScriptException e)
    {
      e.printStackTrace();
      fail("Unexpected exception");
    }
  }
}

