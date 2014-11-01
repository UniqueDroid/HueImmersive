package hueimmersive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.prefs.Preferences;


public class Settings
{
	private static Preferences prefs = Preferences.userRoot();
	
	public static SettingsLight Light = new SettingsLight();
	public static SettingsBridge Bridge = new SettingsBridge();
	
	static // independent setup
	{
		try
		{
			if (prefs.node("/hueimmersive").keys().length != 0)
			{
				prefs = Preferences.userRoot().node("/hueimmersive");
			}
			else
			{
				prefs = Preferences.userRoot().node("/hueimmersive");
				setDefaultSettings();
			}
		}
		catch(Exception e)
		{
			Debug.exception(e);
		}
	}
	
	public static void debug() throws Exception
	{
		String[] keys;
		ArrayList<String> settings;
		keys = prefs.keys();
		Arrays.sort(keys);
		settings = new ArrayList<String>();
		for (String s : keys)
		{
			settings.add(s + " = " + prefs.get(s, null));
		}
		Debug.info("settings general", settings);
	}
	
	public static void setDefaultSettings()
	{
		Debug.info(null, "set default settings");
		
		prefs.putInt("ui_x", 250);
		prefs.putInt("ui_y", 200);
		prefs.putInt("cpi_x", 600);
		prefs.putInt("cpi_y", 200);
		prefs.putInt("oi_x", 250);
		prefs.putInt("oi_y", 450);
		prefs.putInt("chunks", 12);
		prefs.putInt("brightness", 100);
		prefs.putInt("format", 0);
		prefs.putBoolean("colorgrid", false);
		prefs.putBoolean("restorelight", true);
		prefs.putBoolean("autoswitch", false);
		prefs.putBoolean("gammacorrection", true);
		prefs.putInt("screen", 0);
	}

	public static void reset(boolean exit) throws Exception // delete all settings and exit the program
	{
		Debug.info(null, "reset all settings");
		prefs.node("/hueimmersive").removeNode();
		if(exit == true)
		{
			Debug.closeLog();
			System.exit(0);
		}
	}
	
	public static String[] getArguments()
	{
		String args = prefs.get("arguments", null);
		
		String[] arrArgs = {};
		if (args != null)
		{
			arrArgs = args.split(",");
		}
		
		return arrArgs;
	}
	
	public static int getInteger(String key)
	{
		return prefs.getInt(key, 0);
	}
	public static boolean getBoolean(String key)
	{
		return prefs.getBoolean(key, false);
	}
	
	public static void set(String key, int value)
	{
		 prefs.putInt(key, value);
	}
	public static void set(String key, boolean value)
	{
		 prefs.putBoolean(key, value);
	}
}

class SettingsBridge // bridge settings
{
	private Preferences prefs = Preferences.userRoot().node("/hueimmersive/bridge");
	
	public void debug() throws Exception
	{
		String[] keys = prefs.keys();
		Arrays.sort(keys);
		ArrayList<String> settings = new ArrayList<String>();
		for (String k : keys)
		{
			settings.add(k + " = " + prefs.get(k, null));
		}
		Debug.info("settings bridge", settings);
	}
	
	public void setInternalipaddress(String internalipaddress)
	{
		prefs.put("internalipaddress", internalipaddress);
	}
	
	public String getInternalipaddress()
	{
		return prefs.get("internalipaddress", null);
	}
}

class SettingsLight // light settings
{
	private Preferences prefs = Preferences.userRoot().node("/hueimmersive/lights");
	
	private int nexAlg = 0;
	private int maxAlg = ImmersiveProcess.algorithms;
	
	public void checkSettings(String uniqueid) throws Exception // setup default light settings if it doesn't have
	{
		Preferences lprefs = Preferences.userRoot().node(prefs.absolutePath() + "/" + uniqueid);
		if (prefs.get("active", null) == null)
		{
			lprefs.putBoolean("active", true);
		}
		if (prefs.get("bri", null) == null)
		{
			lprefs.putInt("bri", 100);
		}
		if (prefs.get("alg", null) == null)
		{
			lprefs.putInt("alg", nexAlg);
			nexAlg++;
			if (nexAlg > maxAlg)
			{
				nexAlg = 0;
			}
		}
	}
	
	public void debug() throws Exception
	{
		ArrayList<String> settings = new ArrayList<String>();
		for (String node : prefs.childrenNames())
		{	
			settings.add(node + "");
			String[] keys = prefs.node(node).keys();
			Arrays.sort(keys);
			for (String s : keys)
			{
				settings.add("  " + s + " = " + prefs.node(node).get(s, null));
			}
		}
		Debug.info("settings lights", settings);
	}
	
	public void setBrightness(String uniqueid, int bri)
	{
		Preferences lprefs = Preferences.userRoot().node(prefs.absolutePath() + "/" + uniqueid);
		lprefs.putInt("bri", bri);
	}
	public void setActive(String uniqueid, boolean active)
	{
		Preferences lprefs = Preferences.userRoot().node(prefs.absolutePath() + "/" + uniqueid);
		lprefs.putBoolean("active", active);
	}
	public void setAlgorithm(String uniqueid, int alg)
	{
		Preferences lprefs = Preferences.userRoot().node(prefs.absolutePath() + "/" + uniqueid);
		lprefs.putInt("alg", alg);
	}
	
	public boolean getActive(String uniqueid)
	{
		Preferences lprefs = Preferences.userRoot().node(prefs.absolutePath() + "/" + uniqueid);
		return lprefs.getBoolean("active", true);
	}
	public int getAlgorithm(String uniqueid)
	{
		Preferences lprefs = Preferences.userRoot().node(prefs.absolutePath() + "/" + uniqueid);
		return lprefs.getInt("alg", -1);
	}
	public int getBrightness(String uniqueid)
	{
		Preferences lprefs = Preferences.userRoot().node(prefs.absolutePath() + "/" + uniqueid);
		return lprefs.getInt("bri", -1);
	}
}
