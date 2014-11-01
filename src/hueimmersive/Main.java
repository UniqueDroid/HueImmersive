package hueimmersive;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class Main
{	
	public static UserInterface ui;
	public static Control hueControl;
	
	public static final String version = "0.4.5";
	public static final int build = 17;
	
	public static boolean updateAvailable;
	
	public static ArrayList<String> arguments = new ArrayList<String>();
	
	public static void main(String[] args) throws Exception
	{
		arguments.addAll(Arrays.asList(args));
		arguments.addAll(Arrays.asList(Settings.getArguments()));
		
		// check program arguments
		if(arguments.contains("debug"))
		{
			Debug.activateDebugging();
		}
		if(arguments.contains("log"))
		{
			Debug.activateLogging();
		}
		if (arguments.contains("reset"))
		{
			Settings.reset(true);
		}
		
		Debug.info("program arguments", (Object[])arguments.toArray());
		
		Debug.info("program parameters",
				"version: " + version,
				"build: " + build,
				"os: " + System.getProperty("os.name"),
				"java version: " + System.getProperty("java.version"));
		
		Settings.debug();
		Settings.Bridge.debug();
		Settings.Light.debug();
		
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
