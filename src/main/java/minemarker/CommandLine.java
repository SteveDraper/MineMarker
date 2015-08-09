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
  private String  mMinefieldFilename;
  private String  mScriptFilename;

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
    } while(!argsIterator.hasNext());

    return true;
  }

  /**
   * @return whether the command specified includes a request to perform marking
   */
  public boolean getShouldMark()
  {
    return true;
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
      //  -mark option takes two filenames as sub-parameters
      //  so consume them
      mMinefieldFilename = consumeStringArg(argsIterator);
      mScriptFilename = consumeStringArg(argsIterator);

      //  Both filenames must be specified
      if ( mMinefieldFilename == null || mScriptFilename == null )
      {
        return false;
      }

      return true;
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
