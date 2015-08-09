package minemarker;

/**
 * Abstract interface for manipulating a minefield
 * This interface defines the operations supported, but leaves the
 * implementation to concrete implementaing classes so as to allow for future
 * implementations with differing scalability/performance characteristics

 * @author steve
 *
 */
public interface Minefield
{
  /**
   * Add a mine to the minefield
   * @param atCoordinates where to add it
   */
  public abstract void addMine(Point atCoordinates);
  /**
   * @return minimal bounding cuboid for all extant mines - if there are no mines retruns null
   */
  public abstract Cuboid getBoundingCuboid();
  /**
   * Clear a specified region of mines
   * @param region region to clear
   */
  public abstract void clearRegion(Cuboid region);
  /**
   * @return number of extant mines
   */
  public abstract int getNumMines();
}
