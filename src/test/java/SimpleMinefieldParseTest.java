import java.util.LinkedList;
import java.util.List;

import minemarker.Cuboid;
import minemarker.Minefield;
import minemarker.MinefieldFileParseException;
import minemarker.MinefieldFileParser;
import minemarker.ModelException;
import minemarker.Point;

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
public class SimpleMinefieldParseTest extends Assert
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

       3,3,27,3,false
    });
    lTests.add(new Object[]
    {
       ".e." + "\n" +
       "..a" + "\n" +
       "?..",

       3,3,27,3,true
    });
    lTests.add(new Object[]
    {
       ".e." + "\n" +
       "..a" + "\n" +
       "..",

       3,3,27,3,true
    });
    lTests.add(new Object[]
    {
       "",

       3,3,27,3,true
    });
    lTests.add(new Object[]
        {
         ".",

         3,3,27,3,true
      });
      lTests.add(new Object[]
      {
         "",

         3,3,27,3,true
      });

    return lTests;
  }

  /**
   * String specifying the minefield in input file format
   */
  @Parameter(value = 0) public String mMinefieldSpec;
  /**
   * Expected X size  of the resulting field
   */
  @Parameter(value = 1) public int mXSize;
  /**
   * Expected Y size  of the resulting field
   */
  @Parameter(value = 2) public int mYSize;
  /**
   * Expected Y size  of the resulting field
   */
  @Parameter(value = 3) public int mZSize;
  /**
   * Expected number of mines in the field
   */
  @Parameter(value = 4) public int mNumMines;
  /**
   * Whether this test should result in a parse exception
   */
  @Parameter(value = 5) public boolean mShouldExcept;

  @Test
  public void test()
  {
    try
    {
      Minefield minefield = MinefieldFileParser.parseString(mMinefieldSpec);
      Cuboid extent = minefield.getBoundingCuboid();

      assertEquals(0, extent.getNorthWestTop().getX());
      assertEquals(0, extent.getNorthWestTop().getY());
      assertEquals(1, extent.getNorthWestTop().getZ());
      assertEquals(mXSize-1, extent.getSouthEastBottom().getX());
      assertEquals(mYSize-1, extent.getSouthEastBottom().getY());
      assertEquals(mZSize, extent.getSouthEastBottom().getZ());
      assertEquals(mNumMines, minefield.getNumMines());

      //  Reconstruct the output form, which (since it has not be altered in any way)
      //  should be the same as the input we parsed
      List<String> outputLines = minefield.toOutputFormat(new Point(extent.getSouthEastBottom().getX()/2,extent.getSouthEastBottom().getY()/2,0));
      String[] inputLines = mMinefieldSpec.split("\\r?\\n");
      int inputLine = 0;

      assertEquals(inputLines.length, outputLines.size());
      for(String outputLine : outputLines)
      {
        assertEquals(outputLine, inputLines[inputLine++]);
      }
    }
    catch (MinefieldFileParseException e)
    {
      if ( !mShouldExcept )
      {
        e.printStackTrace();
        fail("Unexpected exception");
      }
    }
    catch (ModelException e)
    {
      //  Model exceptions are always unexpected
      e.printStackTrace();
      fail("Unexpected exception");
    }
  }
}
