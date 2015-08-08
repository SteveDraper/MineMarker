package minemarker;

/**
 * @author steve
 *  Shell class for the application JAR
 */
public class MineMarker
{
	  /**
	   * App main
	   * @param args
	   */
	  public static void main(String[] args)
	  {
		  printUsage();
	  }

	  private static void printUsage()
	  {
	    System.out.println("mineMarker [-mark <minefield def filename>,<ship script filename>]");
	    System.out.println("\t-mark - (awaiting implementation) Mark a provided script against a provided mine layout. Takes (comma-separated) filenames for the mine pattern and ship script");
	  }
}
