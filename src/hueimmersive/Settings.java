package hueimmersive;

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
	
	public void checkSettings(int LightID) throws Exception // setup default light settings if it doesn't have
	{
		if (prefs.get(LightID + " active", null) == null)
		{
			setActive(LightID, true);
		}
		if (prefs.get(LightID + " bri", null) == null)
		{
			setBrightness(LightID, 100);
		}
		if (prefs.get(LightID + " alg", null) == null)
		{
			setAlgorithm(LightID, nexAlg);
			nexAlg++;
			if (nexAlg > maxAlg)
			{
				nexAlg = 0;
			}
		}
	}
	
	public void setBrightness(int LightID, int bri)
	{
		prefs.putInt(LightID + " bri", bri);
	}
	public void setActive(int LightID, boolean active)
	{
		prefs.putBoolean(LightID + " active", active);
	}
	public void setAlgorithm(int LightID, int alg)
	{
		prefs.putInt(LightID + " alg", alg);
	}
	
	public boolean getActive(int LightID)
	{
		return prefs.getBoolean(LightID + " active", true);
	}
	public int getAlgorithm(int LightID)
	{
		return prefs.getInt(LightID + " alg", -1);
	}
	public int getBrightness(int LightID)
	{
		return prefs.getInt(LightID + " bri", -1);
	}
}
