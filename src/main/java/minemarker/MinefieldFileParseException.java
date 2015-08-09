package minemarker;

/**
 * Exception covering conditions that prevent parsing of the minefield
 * specification file
 * @author steve
 *
 */
public class MinefieldFileParseException extends Exception
{
  private static final long serialVersionUID = 9131488664603355764L;

  /**
   * @param xiString message describing the exception condition
   */
  public MinefieldFileParseException(String xiString)
  {
    super(xiString);
  }
}
