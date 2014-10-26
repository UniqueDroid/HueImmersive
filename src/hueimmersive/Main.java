package hueimmersive;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


public class Main
{	
	public static UserInterface ui;
	public static Control hueControl;
	
	public static final String version = "0.4.3.1";
	public static final int build = 15;
	
	public static boolean updateAvailable;
	
	public static void main(String[] args) throws Exception
	{		
		for (String arg : args) // check program arguments
		{
			if (arg.equals("debug"))
			{
				Debug.activateDebugging();
			}
			else if(arg.equals("log"))
			{
				Debug.activateLogging();
			}
			else if (arg.equals("reset"))
			{
				Settings.reset(true);
			}
		}
		
		Debug.info("program parameters", "version: " + version, "build: " + build, "OS: " + System.getProperty("os.name"));
		Debug.info(null, "hue immersive started");
		
		checkForUpdate();
		
		ui = new UserInterface();
		hueControl = new Control();
	}
	
	private static void checkForUpdate() throws Exception // check for new updates
	{		
		try
		{
			URL versionUrl = new URL("https://raw.githubusercontent.com/Blodjer/HueImmersive/master/VERSION"); // get version and build number from GitHub
			BufferedReader versionIn = new BufferedReader(new InputStreamReader(versionUrl.openStream()));
			
			String lBuild = versionIn.readLine();	// get latest build
			String lVersion = versionIn.readLine();	// get latest version
			
			versionIn.close();
		    
		    if(Main.build < Integer.valueOf(lBuild)) // check if latest build is higher than this build
		    {
		    	updateAvailable = true;
		    	Debug.info("check updates", "update available...", "version: " + lVersion + "  build: " + lBuild);
		    }
		    else
		    {
		    	updateAvailable = false;
		    	Debug.info("check updates", "no update available");
		    }
		}
		catch(Exception e)
		{
			Debug.exception(e);
		}
			
	}
	
}
