package hueimmersive;

import com.google.gson.JsonObject;


public class HLight
{
	public final int id;
	public final String name;
	private boolean active;
	private int[] storedLightColor = new int[3];
	
	public HLight(int LightID) throws Exception
	{
		id = LightID;
		
		JsonObject response = HRequest.GET("http://" + HBridge.internalipaddress + "/api/" + HBridge.username + "/lights/" + id);
		name = response.get("name").getAsString();
		
		Settings.Light.checkSettings(id);
	}
	
	public boolean isOn() throws Exception
	{
		JsonObject response = HRequest.GET("http://" + HBridge.internalipaddress + "/api/" + HBridge.username + "/lights/" + id);
		
		return response.get("state").getAsJsonObject().get("on").getAsBoolean();
	}
	
	public void turnOn() throws Exception
	{
		String APIurl = "http://" + HBridge.internalipaddress + "/api/" + HBridge.username + "/lights/" + id + "/state/";
		String data = "{\"on\": true, \"transitiontime\":4}";
		
		HRequest.PUT(APIurl, data);
	}
	
	public void turnOff() throws Exception
	{
		String APIurl = "http://" + HBridge.internalipaddress + "/api/" + HBridge.username + "/lights/" + id + "/state/";
		String data = "{\"on\": false, \"transitiontime\":4}";
		
		HRequest.PUT(APIurl, data);
	}
	
	public void storeLightColor() throws Exception
	{
		JsonObject response = HRequest.GET("http://" + HBridge.internalipaddress + "/api/" + HBridge.username + "/lights/" + id);

		storedLightColor[0] = response.get("state").getAsJsonObject().get("hue").getAsInt();
		storedLightColor[1] = response.get("state").getAsJsonObject().get("sat").getAsInt();
		storedLightColor[2] = response.get("state").getAsJsonObject().get("bri").getAsInt();
	}
	
	public void restoreLightColor() throws Exception
	{
		String APIurl = "http://" + HBridge.internalipaddress + "/api/" + HBridge.username + "/lights/" + id + "/state/";
		String data = "{\"hue\":" + storedLightColor[0] + ", \"sat\":" + storedLightColor[1] + ", \"bri\":" + storedLightColor[2] + ", \"transitiontime\":4}";
		
		HRequest.PUT(APIurl, data);
	}
	
	public void setActive(boolean b)
	{
		active = b;
	}
	
	public boolean isActive()
	{
		return active;
	}
}
