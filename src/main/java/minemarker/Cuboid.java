package minemarker;

/**
 * Representation of a spatial cuboid
 * @author steve
 *
 */
public class Cuboid
{
  private final Point mNorthWestTop;
  private final Point mSouthEastBottom;

  /**
   * @param northWestTop Coordinates NW top corner
   * @param southEastBottom Coordinates SE bottom corner
   */
  public Cuboid(Point northWestTop, Point southEastBottom)
  {
    mNorthWestTop = northWestTop;
    mSouthEastBottom = southEastBottom;
  }

  /**
   * @return the northWestTop
   */
  public Point getNorthWestTop()
  {
    return mNorthWestTop;
  }

  /**
   * @return the southEastBottom
   */
  public Point getSouthEastBottom()
  {
    return mSouthEastBottom;
  }

  /**
   * @param point Point to check for being within the interior of the cuboid
   * @return true if the specified Point is within the cuboid
   */
  public boolean contains(Point point)
  {
    return (mNorthWestTop.getX() <= point.getX() &&
            mNorthWestTop.getY() <= point.getY() &&
            mNorthWestTop.getZ() <= point.getZ() &&
            mSouthEastBottom.getX() >= point.getX() &&
            mSouthEastBottom.getY() >= point.getY() &&
            mSouthEastBottom.getZ() >= point.getZ());
}
}
