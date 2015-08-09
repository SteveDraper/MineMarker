package minemarker;

/**
 * Represents coordinates in 3 dimensions on a quantized grid
 * @author steve
 *
 */
public class Point
{
  /**
   * x coordinate of the point.  Increasing x corresponds to scanning to the right on an input/output format line
   */
  private final int mX;
  /**
   * y coordinate of the point.  Increasing y corresponds to moving to a later line in the input/output format
   */
  private final int mY;
  /**
   * z coordinate of the point
   */
  private final int mZ;

  /**
   * @param x value for new Point's x-coordinate
   * @param y value for new Point's y-coordinate
   * @param z value for new Point's z-coordinate
   */
  public Point(int x, int y, int z)
  {
    mX = x;
    mY = y;
    mZ = z;
  }

  /**
   * @return the x
   */
  public int getX()
  {
    return mX;
  }

  /**
   * @return the y
   */
  public int getY()
  {
    return mY;
  }

  /**
   * @return the z
   */
  public int getZ()
  {
    return mZ;
  }

  /**
   * Calculate the coordinates of the point displaced by a specified amount from this Point
   * @param displacement displacement from here
   * @return Point with coordinates displaced from ours by the specified displacement
   */
  public Point displace(Point displacement)
  {
    return new Point(mX + displacement.mX, mY + displacement.mY, mZ + displacement.mZ);
  }

  @Override
  public int hashCode()
  {
    int result = 1;

    result = accumulateHash(result, mX);
    result = accumulateHash(result, mY);
    result = accumulateHash(result, mZ);

    return result;
  }

  @Override
  public boolean equals(Object other)
  {
    if ( !(other instanceof Point) )
    {
      return false;
    }

    Point otherPoint = (Point)other;
    return (mX == otherPoint.mX && mY == otherPoint.mY && mZ == otherPoint.mZ);
  }

  private int accumulateHash(int accumulator, int toAdd)
  {
    return accumulator*37 + toAdd;
  }
}
