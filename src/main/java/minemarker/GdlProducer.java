package minemarker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates production of a GDL puzzle from a minefield
 * configuration
 * @author steve
 *
 */
public class GdlProducer
{
  private final Minefield mMinefield;

  /**
   * Create a new GDL producer for a specified minefield config
   * @param xiMinefield
   */
  public GdlProducer(Minefield xiMinefield)
  {
    mMinefield = xiMinefield;
  }

  /**
   * @return GDL for a puzzle based on the given minefield configuration
   * @throws IOException
   * @throws ModelException
   */
  public String getGDL() throws IOException, ModelException
  {
    StringBuilder resultBuilder = new StringBuilder();

    resultBuilder.append(getTemplate());
    resultBuilder.append("\n\n;;;; Generated section:\n\n");
    resultBuilder.append(getInserts());

    return resultBuilder.toString();
  }

  private String getInserts() throws ModelException
  {
    StringBuilder result = new StringBuilder();

    Cuboid minefieldBounds = mMinefield.getBoundingCuboid();

    //  We need index values up to the larger of the bounding x/y re-based to 1
    int projectionSquareSideLength = Math.max(minefieldBounds.getSouthEastBottom().getX(), minefieldBounds.getSouthEastBottom().getY()) + 1;
    for(int index = 1; index <= projectionSquareSideLength; index++)
    {
      result.append("(index " + index + ")");
      result.append("\n");
    }

    result.append("\n");

    //  We need to define legal depth markings up to the max required
    for(int depth = minefieldBounds.getSouthEastBottom().getZ(); depth > 0; depth--)
    {
      result.append("(minemarking " + depthEncoding(depth) + ")");
      result.append("\n");
    }

    result.append("\n");

    //  We need the seq relation up to the max of the bounding rectangle size and its depth*4 (max fire count)
    int maxSeq = Math.max(projectionSquareSideLength, minefieldBounds.getSouthEastBottom().getZ()*4);
    for(int index = 0; index < maxSeq; index++)
    {
      result.append("(successor " + index + " " + (index+1) + ")");
      result.append("\n");
    }

    //  We need letter-progression succession from max depth mine to *
    for(int depth = minefieldBounds.getSouthEastBottom().getZ(); depth > 0; depth--)
    {
      result.append("(successor " + depthEncoding(depth) + " " + depthEncoding(depth-1) + ")");
      result.append("\n");
    }

    result.append("\n");

    int middleX = (minefieldBounds.getSouthEastBottom().getX())/2 + 1;  //  1-based
    int middleY = (minefieldBounds.getSouthEastBottom().getY())/2 + 1;  //  1-based

    //  Initial ship position
    result.append("(init (ship " + middleX + " " + middleY + "))");
    result.append("\n");

    //  We need initial cell states
    List<String> minefieldStringRep = mMinefield.toOutputFormat(new Point(middleX-1, middleY-1, 0));
    assert(minefieldStringRep.size() <= projectionSquareSideLength);

    for(int y = 1; y <= projectionSquareSideLength; y++)
    {
      String line = (y <= minefieldStringRep.size() ? minefieldStringRep.get(y-1) : "");
      assert(line.length() <= projectionSquareSideLength);
      for(int x = 1; x <= projectionSquareSideLength; x++)
      {
        if ( x <= line.length() )
        {
          char repChar = line.charAt(x-1);
          if ( repChar == '.' )
          {
            result.append("(init (cell " + x + " " + y + " dot))");
          }
          else
          {
            result.append("(init (cell " + x + " " + y + " " + depthEncoding(toDepth(repChar)) + "))");
          }
        }
        else
        {
          result.append("(init (cell " + x + " " + y + " dot))");
        }
        result.append("\n");
      }
    }

    //  Output the goals - these are the tricky part
    int numMines = mMinefield.getNumMines();
    int scoreUpperBound = numMines*10 - 5*((numMines+3)/4);

    //  We'll want to scale to [0-100] roughly for GGP compatibility
    //  Build a map
    Map<Integer,Integer> scoreMap = new HashMap<>();

    for(int shotsFired = (numMines+3)/4; shotsFired < numMines; shotsFired++)
    {
      int scoreCeiling = numMines*10 - 5*shotsFired;

      for(int movesMade = 0; movesMade < (numMines*3)/2; movesMade++)
      {
        int score = scoreCeiling - 2*movesMade;

        result.append("(<= (goal sweeper ?s)\n");
        result.append("    (scoremap " + score + " ?s)\n");
        result.append("    (not minesremaining)\n");
        result.append("    (true (moved " + movesMade + "))\n");
        result.append("    (true (fired " + shotsFired + ")))\n");

        scoreMap.put(score, (100*score)/scoreUpperBound);
      }

      result.append("(<= (goal sweeper ?s)\n");
      result.append("    (scoremap " + (scoreCeiling-3*numMines) + " ?s)\n");
      result.append("    (not minesremaining)\n");
      result.append("    (true (moved ?m))\n");
      for(int i = 0; i < (numMines*3)/2; i++)
      {
        result.append("    (distinct ?m " + i + ")\n");
      }
      result.append("    (true (fired " + shotsFired + ")))\n");

      scoreMap.put(scoreCeiling-3*numMines, (100*(scoreCeiling-3*numMines))/scoreUpperBound);
    }

    //  Case when more shots were fired than half the mine count
    {
      for(int movesMade = 0; movesMade < (numMines*3)/2; movesMade++)
      {
        int score = numMines*5 - 2*movesMade;

        result.append("(<= (goal sweeper ?s)\n");
        result.append("    (scoremap " + score + " ?s)\n");
        result.append("    (not minesremaining)\n");
        result.append("    (true (moved " + movesMade + "))\n");
        for(int i = 0; i < numMines; i++)
        {
          result.append("    (distinct ?f " + i + ")\n");
        }
        result.append("    (true (fired ?f)))\n");

        scoreMap.put(score, (100*score)/scoreUpperBound);
      }

      result.append("(<= (goal sweeper ?s)\n");
      result.append("    (scoremap " + (numMines*5-3*numMines) + " ?s)\n");
      result.append("    (not minesremaining)\n");
      result.append("    (true (moved ?m))\n");
      for(int i = 0; i < (numMines*3)/2; i++)
      {
        result.append("    (distinct ?m " + i + ")\n");
      }
      for(int i = 0; i < numMines; i++)
      {
        result.append("    (distinct ?f " + i + ")\n");
      }
      result.append("    (true (fired ?f)))\n");

      scoreMap.put(numMines*2, (100*(2*numMines))/scoreUpperBound);
    }

    result.append("\n");

    //  Output the score map
    for(Integer score : scoreMap.keySet())
    {
      result.append("(scoremap " + score + " " + scoreMap.get(score) + ")\n");
    }

    result.append("\n");

    return result.toString();
  }

  private int toDepth(char outputMineChar) throws ModelException
  {
    if ( outputMineChar >= 'a' && outputMineChar <= 'z')
    {
      return (outputMineChar - 'a') + 1;
    }
    if ( outputMineChar >= 'A' && outputMineChar <= 'Z')
    {
      return (outputMineChar - 'A') + 27;
    }

    throw new ModelException("Badly formatted mine output rep");
  }

  private String depthEncoding(int depth) throws ModelException
  {
    if ( depth <= 0 )
    {
      return "star";
    }
    else if ( depth <= 26 )
    {
      return Character.toString((char)('a' + (depth - 1)));
    }
    else if ( depth <= 52 )
    {
      return '_' + depthEncoding(depth-26);
    }

    throw new ModelException("Depth of mine exceeds maximum representable depth");
  }

  private String getTemplate() throws IOException
  {
    StringBuilder result = new StringBuilder();
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream templateStream = classLoader.getResourceAsStream(
        "minesweeper.kif");

    try
    {
      BufferedReader reader = new BufferedReader(new InputStreamReader(templateStream));
      String line = null;

      while (( line = reader.readLine()) != null)
      {
          result.append(line);
          result.append("\n");
      }
    }
    finally
    {
      templateStream.close();
    }

    return result.toString();
  }
}
