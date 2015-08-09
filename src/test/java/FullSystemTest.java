import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import minemarker.FileHelper;
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
public class FullSystemTest extends Assert
{
  /**
   * @return Iterable set of test cases
   */
  @Parameters(name="{1}")
  public static Iterable<? extends Object> data()
  {
    LinkedList<Object[]> lTests = new LinkedList<>();

    lTests.add(new Object[]
    {
      "src/test/data/minefield1.txt",
      "src/test/data/script1.txt",
      "src/test/data/output1.txt"
    });

    lTests.add(new Object[]
    {
      "src/test/data/minefield2.txt",
      "src/test/data/script2.txt",
      "src/test/data/output2.txt"
    });

    lTests.add(new Object[]
    {
      "src/test/data/minefield3.txt",
      "src/test/data/script3.txt",
      "src/test/data/output3.txt"
    });

    lTests.add(new Object[]
    {
      "src/test/data/minefield4.txt",
      "src/test/data/script4.txt",
      "src/test/data/output4.txt"
    });

    lTests.add(new Object[]
    {
      "src/test/data/minefield5.txt",
      "src/test/data/script5.txt",
      "src/test/data/output5.txt"
    });

    lTests.add(new Object[]
    {
      "src/test/data/minefield6.txt",
      "src/test/data/script6.txt",
      "src/test/data/output6.txt"
    });

    lTests.add(new Object[]
    {
      "src/test/data/minefield7.txt",
      "src/test/data/script7.txt",
      "src/test/data/output7.txt"
    });

    return lTests;
  }

  /**
   * String specifying the minefield specification file
   */
  @Parameter(value = 0) public String mMinefieldSpecFile;
  /**
   * String specifying the script file
   */
  @Parameter(value = 1) public String mScriptSpecFile;
  /**
   * String specifying the expected output file
   */
  @Parameter(value = 2) public String mExpectedOutputFile;


  /**
   * Run full-system test cases.  This test uses canned input and output files
   * and verifies the results produced from the input files match the output files
   * The data folder includes all the examples provided in the PDF spec
   */
  @Test
  public void test()
  {
    try
    {
      Minefield minefield = MinefieldFileParser.parse(mMinefieldSpecFile);
      ShipOrders orders = ScriptFileParser.parse(mScriptSpecFile);
      SimulationState simulator = new SimulationState(minefield, orders);

      List<String> simulationResult = simulator.runAndMark();

      //  Check the output format
      List<String> checkLines = FileHelper.readLines(mExpectedOutputFile);
      int lineNum = 0;

      assertEquals(checkLines.size(), simulationResult.size());
      for(String outputLine : simulationResult)
      {
        assertEquals(checkLines.get(lineNum++), outputLine );
      }
    }
    catch (MinefieldFileParseException | ModelException | ScriptException | IOException e)
    {
      e.printStackTrace();
      fail("Unexpected exception");
    }
  }
}
