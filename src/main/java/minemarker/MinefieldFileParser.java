package minemarker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
            z = mineChar - 'a';
          }
          else if ( mineChar >= 'A' && mineChar <= 'Z' )
          {
            z = mineChar - 'A' + 26;
          }
          else
          {
            throw new MinefieldFileParseException("Minefield specification contains illegal character '" + mineChar + "' in line " + (y+1) + ": " + line);
          }

          result.addMine(new Point(x,y,z));
        }
      }
    }

    return result;
  }
}
