package minemarker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
