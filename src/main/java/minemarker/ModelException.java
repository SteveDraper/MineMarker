package minemarker;

/**
 * Exceptions pertaining the the model of the simulation data
 * @author steve
 *
 */
public class ModelException extends Exception
{
  private static final long serialVersionUID = -5988796544949195106L;

  /**
   * @param message description of the exception condition
   */
  public ModelException(String message)
  {
    super(message);
  }
}
