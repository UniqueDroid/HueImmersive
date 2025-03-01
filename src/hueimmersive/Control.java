package hueimmersive;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;


public class Control
{
	private Timer captureLoop;
	private double autoOffBri = 0.0;
	
	public Control() throws Exception
	{
		HBridge.setup();
	}
	
	public void setLight(int LightID, Color color) throws Exception // calculate color and send it to light
	{		
		float[] colorHSB1 = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null); // unmodified color
		
		color = Color.getHSBColor(colorHSB1[0], colorHSB1[1], (float)(colorHSB1[2] * (Main.ui.slider_Brightness.getValue() / 100.0) * (Settings.Light.getBrightness(LightID) / 100.0))); // calculate brightness set by user
		float[] colorHSB2 = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null); // modified color
		
		double[] xy = HColor.translate(color, Settings.getBoolean("gammacorrection")); // converted color
		
		String APIurl = "http://" + HBridge.internalipaddress + "/api/" + HBridge.username + "/lights/" + LightID + "/state";	
		String data = "{\"xy\":[" + xy[0] + ", " + xy[1] + "], \"bri\":" + Math.round(colorHSB2[2] * 255) + ", \"transitiontime\":4}"; //\"sat\":" + Math.round(HSBcolor[1] * 255) + ", 
		
		// turn light off automatically if the brightness is very low
		if (Settings.getBoolean("autoswitch"))
		{
			if (colorHSB1[2] > autoOffBri + 0.1 && HBridge.getLight(LightID).isOn() == false)
			{
				data = "{\"on\":true, \"xy\":[" + xy[0] + ", " + xy[1] + "], \"bri\":" + Math.round(colorHSB2[2] * 255) + ", \"transitiontime\":4}";
			}
			else if (colorHSB1[2] <= 0.0627451f && HBridge.getLight(LightID).isOn() == true)
			{
				data = "{\"on\":false, \"transitiontime\":3}";
				autoOffBri = colorHSB1[2];
			}
		}
		else if (Settings.getBoolean("autoswitch") == false && HBridge.getLight(LightID).isOn() == false)
		{
			data = "{\"on\":true, \"xy\":[" + xy[0] + ", " + xy[1] + "], \"bri\":" + Math.round(colorHSB2[2] * 255) + ", \"transitiontime\":2}";
		}
			
		HRequest.PUT(APIurl, data);
	}
	
	public void startImmersiveProcess() throws Exception
	{
		Main.ui.button_Off.setEnabled(false);
		Main.ui.button_On.setEnabled(false);
		
		for(HLight light : HBridge.lights)
		{
			light.storeLightColor();
		}
		
		// create a loop to execute the immersive process
		captureLoop = new Timer();
		TimerTask task = new TimerTask()
		{
			public void run()
			{
				try
				{
					ImmersiveProcess.execute();
				}
				catch (Exception e)
				{
					Debug.exception(e);
				}
			}
		};
		captureLoop.scheduleAtFixedRate(task, 0, 300);
	}
	
	public void stopImmersiveProcess() throws Exception
	{
		captureLoop.cancel();
		captureLoop.purge();
		
		Main.ui.setupOnOffButton();
		
		Thread.sleep(250);
		ImmersiveProcess.setStandbyOutput();
		
		if (Settings.getBoolean("restorelight"))
		{
			Thread.sleep(750);
			for(HLight light : HBridge.lights)
			{
				light.restoreLightColor();
			}
		}
	}
	
	public void onceImmersiveProcess() throws Exception
	{
		ImmersiveProcess.execute();
	}
	
	public void turnAllLightsOn() throws Exception
	{
		for(HLight light : HBridge.lights)
		{
			if (Settings.Light.getActive(light.id))
			{
				light.turnOn();
			}
		}
	}

	public void turnAllLightsOff() throws Exception
	{
		for(HLight light : HBridge.lights)
		{
			if (Settings.Light.getActive(light.id))
			{
				light.turnOff();
			}
		}
	}

}
