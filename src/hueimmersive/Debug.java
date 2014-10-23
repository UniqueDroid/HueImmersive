package hueimmersive;

import java.io.File;
import java.net.URLDecoder;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Debug
{
	private static boolean debugging = false;
	private static boolean logging = false;
	private static Logger logger;
	private static FileHandler handler;
	
	public static void activateDebugging()
	{
		debugging = true;
	}
	
	public static void activateLogging()
	{
		logging = true;
		
	    try // to create a log file
		{
	    	String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String decodedPath = URLDecoder.decode(path, "UTF-8");
			String logpath = new File(decodedPath).getParentFile().getPath();
			logpath = logpath.replace("\\", "/");
	    	
			logger = Logger.getLogger("global");		
			handler = new FileHandler(logpath + "/hue.log");
			
	        logger.addHandler(handler);
	        logger.setUseParentHandlers(false);
	        SimpleFormatter formatter = new SimpleFormatter();
	        handler.setFormatter(formatter);
		} 
	    catch (Exception e)
		{
			Debug.exception(e);
		}
	}
	
	public static void closeLog() // close and unlock log
	{
		if (logging == true)
		{
			info(null, "log closed");
	        logger.setUseParentHandlers(false);
	        handler.close();
		}
	}
	
	public static void info(String header, String ... messages) // log/debug a information message
	{
		// format message for debug output
		if (debugging == true)
		{
			String dText = "\n";
			if (header != null && header != "")
			{
				dText += "- - - - - " + header.toUpperCase() + " - - - - -\n";
			}
			for (int i = 0; i < messages.length; i++)
			{
				if (i != messages.length - 1)
				{
					dText += messages[i] + "\n";
				}
				else
				{
					dText += messages[i];
					if (header != null && header != "")
					{
						dText += "\n- - - - -";
					}
				}
			}
			System.out.println(dText);
		}
		
		// format message for log
		if (logging == true)
		{
			String lText = "";
			if (header != null && header != "")
			{
				lText += header.toUpperCase();
			}
			for (int i = 0; i < messages.length; i++)
			{
				if (i != messages.length - 1)
				{
					lText += "\n # " + messages[i];
				}
				else
				{
					lText += "\n # " + messages[i] + "\n";
				}
			}
			logger.log(Level.INFO, lText);
		}
	}
	
	public static void exception(Exception e) // log/debug exception
	{
		// format message for debug output
		if (debugging == true)
		{
			System.out.println();
			e.printStackTrace();
		}
		
		// format message for log
		if (logging == true)
		{
			logger.log(Level.SEVERE, "", e);
		}
	}
}
