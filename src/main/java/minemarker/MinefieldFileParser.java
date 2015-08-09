package minemarker;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Parser responsible for taking a minefield specification file
 * and producing a minefield object from it
 * @author steve
 *
 */
public class MinefieldFileParser
{
  /**
   * Parse the specified input file, which is assumed to match the minefield definition
   * format
   * @param filename where to find the specification file
   * @return Minefield instance representing the specified minefield configuration
   * @throws IOException
   * @throws MinefieldFileParseException
   */
  public static Minefield parse(String filename) throws IOException, MinefieldFileParseException
  {
    return parse(FileHelper.readLines(filename));
  }

  /**
   * Parse the specified input, which is assumed to match the minefield definition
   * format.  Provided for ease of use with junit tests
   * @param minefieldSpecification specification as one string with linefeed characters separating the lines
   * @return Minefield instance representing the specified minefield configuration
   * @throws MinefieldFileParseException
   */
  public static Minefield parseString(String minefieldSpecification) throws MinefieldFileParseException
  {
    String[] lines = minefieldSpecification.split("\\r?\\n");

    return parse(Arrays.asList(lines));
  }

  private static Minefield parse(List<String> minefieldSpecification) throws MinefieldFileParseException
  {
    int xExtent = -1;
    int yExtent = minefieldSpecification.size();

    if ( yExtent == 0 )
    {
      throw new MinefieldFileParseException("No minefield specified!");
    }

    Minefield result = new SimpleMinefield();

    for(int y = 0; y < minefieldSpecification.size(); y++)
    {
      String line = minefieldSpecification.get(y);
      int lineXExtent = line.length();

      if ( xExtent == -1 )
      {
        if ( lineXExtent == 0 )
        {
          throw new MinefieldFileParseException("Minefield specification contains blank lines");
        }
        xExtent = lineXExtent;
      }

      if ( lineXExtent != xExtent )
      {
        throw new MinefieldFileParseException("Minefield specification contains lines with an inconsistent width");
      }

      for(int x = 0; x < line.length(); x++)
      {
        char mineChar = line.charAt(x);
        if ( mineChar != '.' )
        {
          int z;

          //  We have a mine.  What is its z-depth?
          if ( mineChar >= 'a' && mineChar <= 'z' )
          {
            z = mineChar - 'a' + 1;
          }
          else if ( mineChar >= 'A' && mineChar <= 'Z' )
          {
            z = mineChar - 'A' + 27;
          }
          else
          {
            throw new MinefieldFileParseException("Minefield specification contains illegal character '" + mineChar + "' in line " + (y+1) + ": " + line);
          }

          result.addMine(new Point(x,y,z));
        }
      }
    }

    //  We don't allow minefields with no mines as the spec. definition doesn't really provide for sensible
    //  (or at least useful) handling of this case
    if ( result.getNumMines() == 0 )
    {
      throw new MinefieldFileParseException("Illegal minefield specification defines no mines");
    }

    return result;
  }
}
