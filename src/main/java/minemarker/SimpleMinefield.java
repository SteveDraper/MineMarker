package minemarker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Simple implementation of a minefield with no particular concern for
 * performance scaling, since the basic problem is inherently small
 * given the current input format
 * @author steve
 *
 */
public class SimpleMinefield implements Minefield
{
  private final Set<Point>  mMines = new HashSet<>();
  private Cuboid            mBoundingCuboid = null;

  //  We use a private class to perform projection of the minefield onto the Z=0 plane
  //  which is useful in generatingn the output format in a single pass
  private class ZProjection
  {
    private final Map<Point,Point> mProjectionMap = new HashMap<>();

    ZProjection()
    {
      for(Point mine : mMines)
      {
        mProjectionMap.put(new Point(mine.getX(), mine.getY(), 0), mine);
      }
    }

    Point getMineAt(int x, int y)
    {
      return mProjectionMap.get(new Point(x,y,0));
    }
  }

  @Override
  public void addMine(Point xiAtCoordinates)
  {
    mMines.add(xiAtCoordinates);
    if ( mBoundingCuboid == null || !mBoundingCuboid.contains(xiAtCoordinates))
    {
      updateBoundingCuboid();
    }
  }

  @Override
  public Cuboid getBoundingCuboid()
  {
    return mBoundingCuboid;
  }

  @Override
  public void clearRegion(Cuboid xiRegion)
  {
    List<Point> toRemove = new ArrayList<>();

    for(Point mine : mMines)
    {
      if ( xiRegion.contains(mine))
      {
        toRemove.add(mine);
      }
    }

    mMines.removeAll(toRemove);

    updateBoundingCuboid();
  }

  @Override
  public int getNumMines()
  {
    return mMines.size();
  }

  @Override
  public List<String> toOutputFormat(Point xiCenterOn) throws ModelException
  {
    List<String> result = new ArrayList<>();

    if ( mMines.isEmpty() )
    {
      //  Nothing here but the implicit ship
      result.add(".");
    }
    else
    {
      assert(mBoundingCuboid != null);

      //  First calculate the bounds of the displayed projection to make the ship the center
      int minMineX = mBoundingCuboid.getNorthWestTop().getX();
      int maxMineX = mBoundingCuboid.getSouthEastBottom().getX();
      int radiusX = Math.max(xiCenterOn.getX() - minMineX, maxMineX - xiCenterOn.getX());
      int minMineY = mBoundingCuboid.getNorthWestTop().getY();
      int maxMineY = mBoundingCuboid.getSouthEastBottom().getY();
      int radiusY = Math.max(xiCenterOn.getY() - minMineY, maxMineY - xiCenterOn.getY());

      //  Construct a projection of the minefield onto the Z==0 plane.  Using this we can then easily
      //  construct the output point by point in the determined display rectangle
      ZProjection zProjection = new ZProjection();

      //  This is not exactly wildly efficient, but the scale is small so it'll be fine
      //  form the problem space as defined
      for(int y = xiCenterOn.getY() - radiusY; y <= xiCenterOn.getY() + radiusY; y++)
      {
        StringBuilder outputLine = new StringBuilder();
        for(int x = xiCenterOn.getX() - radiusX; x <= xiCenterOn.getX() + radiusX; x++)
        {
          char outputChar;
          Point mine = zProjection.getMineAt(x, y);
          if ( mine == null )
          {
            outputChar = '.';
          }
          else
          {
            int depth = mine.getZ() - xiCenterOn.getZ();
            if ( depth < 1 )
            {
              throw new ModelException("Unexpected model state for output generation - mines at or above ship");
            }
            else if ( depth <= 26 )
            {
              outputChar = (char)('a' + (depth-1));
            }
            else if ( depth <= 52 )
            {
              outputChar = (char)('A' + (depth-27));
            }
            else
            {
              throw new ModelException("Unexpected model state for output generation - mines deeper than max representation depth");
            }
          }

          outputLine.append(outputChar);
        }

        result.add(outputLine.toString());
      }
    }

    return result;
  }

  //  Update the bounding cuboid from the current list of mines
  private void updateBoundingCuboid()
  {
    if ( mMines.isEmpty() )
    {
      mBoundingCuboid = null;
    }
    else
    {
      int lowestX = Integer.MAX_VALUE;
      int lowestY = Integer.MAX_VALUE;
      int lowestZ = Integer.MAX_VALUE;
      int highestX = -Integer.MAX_VALUE;
      int highestY = -Integer.MAX_VALUE;
      int highestZ = -Integer.MAX_VALUE;

      for(Point mine : mMines)
      {
        if ( mine.getX() < lowestX )
        {
          lowestX = mine.getX();
        }
        if ( mine.getX() > highestX )
        {
          highestX = mine.getX();
        }

        if ( mine.getY() < lowestY )
        {
          lowestY = mine.getY();
        }
        if ( mine.getY() > highestY )
        {
          highestY = mine.getY();
        }

        if ( mine.getZ() < lowestZ )
        {
          lowestZ = mine.getZ();
        }
        if ( mine.getZ() > highestZ )
        {
          highestZ = mine.getZ();
        }
      }

      mBoundingCuboid = new Cuboid(new Point(lowestX, lowestY, lowestZ),
                                   new Point(highestX, highestY, highestZ));
    }
  }
}
