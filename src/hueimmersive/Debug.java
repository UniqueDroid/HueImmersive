package hueimmersive;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
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
			handler = new FileHandler(logpath + "/HueImmersive.log");
			
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
	
	public static void info(String header, Object ... msg) // log/debug a information message
	{
		ArrayList<Object> list = new ArrayList<Object>();
		for (Object object : msg)
		{
			if (object.getClass() == ArrayList.class)
			{
				ArrayList<Object> subList = (ArrayList<Object>) object;
				for (Object subObject : subList)
				{
					list.add(subObject);
				}
			}
			else
			{
				list.add(object);
			}
		}
		msg = list.toArray();
		
		// format message for debug output
		if (debugging == true)
		{
			String dText = "\n";
			
			if (header != null && header != "")
			{
				dText += "- - - - - " + header.toUpperCase() + " - - - - -\n";
				
				if (msg.length == 0)
				{
					dText += "\n";
				}
			}
			
			for (int i = 0; i < msg.length; i++)
			{
				if (i != msg.length - 1)
				{
					dText += msg[i] + "\n";
				}
				else
				{
					dText += msg[i];
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
				
				if (msg.length == 0)
				{
					lText += "\n";
				}
			}
			
			for (int i = 0; i < msg.length; i++)
			{
				if (i != msg.length - 1)
				{
					lText += "\n >	" + msg[i];
				}
				else
				{
					lText += "\n >	" + msg[i] + "\n";
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
