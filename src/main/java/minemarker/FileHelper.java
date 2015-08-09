package minemarker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * File read utility methods
 * @author steve
 *
 */
public class FileHelper
{
  /**
   * Read the specified input file into a list of Strings
   * @param filename where to find the specification file
   * @return List of lines as Strings
   * @throws IOException
   */
  public static List<String> readLines(String filename) throws IOException
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

    return lines;
  }
}
