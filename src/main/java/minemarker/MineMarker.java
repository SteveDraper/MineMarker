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
            Minefield minefield = MinefieldFileParser.parse(commandLine.getMinefieldFilename());
            ShipOrders orders = ScriptFileParser.parse(commandLine.getScriptFilename());

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
	    System.out.println("MineMarker [-mark <minefield def filename> <ship script filename>]");
	    System.out.println("\t-mark - (awaiting implementation) Mark a provided script against a provided mine layout. Takes two filenames for the mine pattern and ship script respectively");
	  }
}
