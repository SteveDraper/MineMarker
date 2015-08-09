package minemarker;

/**
 * Exceptions relating to invalid script definition or usage
 * @author steve
 *
 */
public class ScriptException extends Exception
{
  private static final long serialVersionUID = -4947962257983540368L;

  /**
   * @param message Message describing the exception condition
   */
  public ScriptException(String message)
  {
    super(message);
  }
}
