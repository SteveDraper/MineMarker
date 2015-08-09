package minemarker;

import java.io.IOException;
import java.util.List;

/**
 *  Shell class for the application JAR
 * @author steve
 */
public class MineMarker
{
	  /**
	   * App main
	   * @param args commandline arguments
	   */
	  public static void main(String[] args)
	  {
	    CommandLine commandLine = new CommandLine();

	    if ( !commandLine.parse(args) )
	    {
	      printUsage();
	    }
	    else
	    {
	      // We have a valid commandline.  What's it asking us to do
	      if ( commandLine.getShouldMark() )
	      {
	        try
          {
	          String minefieldFile = commandLine.getMinefieldFilename();
	          String scriptFile = commandLine.getScriptFilename();

	          // The marking action requires both minefield and script files to have been supplied
	          if ( minefieldFile == null || scriptFile == null )
	          {
	            System.out.println("The mark action requires both minefield layout and script filenames to be supplied via -minefield and -script respectively");
	            return;
	          }

            Minefield minefield = MinefieldFileParser.parse(minefieldFile);
            ShipOrders orders = ScriptFileParser.parse(scriptFile);

            //  Create the simulation
            SimulationState simulation = new SimulationState(minefield, orders);
            List<String> simulationResult = simulation.runAndMark();

            //  Echo the output to stdout
            for(String outputLine : simulationResult)
            {
              System.out.println(outputLine);
            }
          }
          catch (IOException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          catch (MinefieldFileParseException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          catch (ScriptException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          catch (ModelException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
	      }
	    }
	  }

	  private static void printUsage()
	  {
	    System.out.println("java -jar MineMarker.jar [-mark] [-minefield <minefield def filename>] [-script <ship script filename>]");
      System.out.println("\t-mark - Perform marking.  This action is assumed if no other actions are specified.");
      System.out.println("\t-minefield <filename> - specifies the minefield layout file to use.");
      System.out.println("\t-script <filename> - specifies the ship action script file to use.");
	  }
}
