
import java.util.LinkedList;

import minemarker.ScriptException;
import minemarker.ScriptFileParser;

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
  @Parameters(name="{1}")
  public static Iterable<? extends Object> data()
  {
    LinkedList<Object[]> lTests = new LinkedList<>();

    lTests.add(new Object[]
    {
       "north alpha" + "\n" +
       "\n" +
       "beta south",

       false
    });

    return lTests;
  }

  /**
   * String specifying the script in input file format
   */
  @Parameter(value = 0) public String mScript;
  /**
   * Whether this test should result in a parse exception
   */
  @Parameter(value = 1) public boolean mShouldExcept;

  @Test
  public void test()
  {
    try
    {
      ScriptFileParser.parseString(mScript);
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
