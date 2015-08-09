package minemarker;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Commandline syntax parser
 * @author steve
 *
 */
public class CommandLine
{
  private String  mMinefieldFilename = null;
  private String  mScriptFilename = null;
  private boolean mDoMark = true;

  /**
   * Parse a commandline, populating properties of this object
   * from it
   * @param args commandline arguments
   * @return true if the commandline syntax was valid
   */
  public boolean parse(String[] args)
  {
    List<String> argList = Arrays.asList(args);
    Iterator<String> argsIterator = argList.iterator();

    do
    {
      if ( !parseOption(argsIterator))
      {
        return false;
      }
    } while(argsIterator.hasNext());

    return true;
  }

  /**
   * @return whether the command specified includes a request to perform marking
   */
  public boolean getShouldMark()
  {
    return mDoMark;
  }

  /**
   * @return name of the file to retrieve minefield data from
   */
  public String getMinefieldFilename()
  {
    return mMinefieldFilename;
  }

  /**
   * @return name of the file to retrieve ship script data from
   */
  public String getScriptFilename()
  {
    return mScriptFilename;
  }

  private boolean parseOption(Iterator<String> argsIterator)
  {
    if ( !argsIterator.hasNext())
    {
      return false;
    }

    String option = argsIterator.next();

    if ( option.equalsIgnoreCase("-mark") )
    {
      // Nothing to do here as mark is the default action
      return true;
    }
    if ( option.equalsIgnoreCase("-minefield"))
    {
      mMinefieldFilename = consumeStringArg(argsIterator);
      return (mMinefieldFilename != null);
    }
    if ( option.equalsIgnoreCase("-script"))
    {
      mScriptFilename = consumeStringArg(argsIterator);
      return (mScriptFilename != null);
    }

    return false;
  }

  private String consumeStringArg(Iterator<String> argsIterator)
  {
    if ( argsIterator.hasNext())
    {
      return argsIterator.next();
    }

    return null;
  }
}
