package hueimmersive;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.*;


public class HBridge
{
	public static String name;
	public static String id;
	public static String internalipaddress = Settings.Bridge.getInternalipaddress();
	public static String macaddress;

	public static final String username = "hueimmersiveuser";
	public static final String devicetype = "hueimmersive";
	
	public static List<HLight> lights = new ArrayList<HLight>();
	
	public static void setup() throws Exception
	{
		if (internalipaddress != null)
		{
			fastConnect();
		}
		else
		{
			newConnect();
		}
	}
	
	private static void fastConnect() throws Exception // try to connect to the saved ip
	{
		Debug.info(null, "try fast connect...");
		
		JsonObject response = HRequest.GET("http://" + internalipaddress + "/api/" + username + "/config/");
		
		if (HRequest.responseCheck(response) == "data")
		{
			name = response.get("name").getAsString();
			macaddress = response.get("mac").getAsString();
			
			Debug.info(null, "fast connect successfull");
			Debug.info("bridge infos", "name: " + name, "internalipaddress: " + internalipaddress, "macaddress: " + "hidden");
			
			getLights();
			Main.ui.loadMainInterface();
		}
		else
		{
			newConnect();
		}
	}

	private static void newConnect() throws Exception // find a new bridge
	{
		Debug.info(null, "setup new connection...");
		
		Main.ui.loadConnectionInterface();
		Main.ui.setConnectState(1);
		
		final Timer timer = new Timer();
		TimerTask addUserLoop = new TimerTask()
		{
			int tries = 0;
			public void run()
			{
				try // to get the bridge ip
				{					
					JsonObject response = HRequest.GET("https://www.meethue.com/api/nupnp");
					
					if (response != null)
					{
						timer.cancel();
						timer.purge();
						
						name = response.get("name").getAsString();
						id = response.get("id").getAsString();
						internalipaddress = response.get("internalipaddress").getAsString();
						macaddress = response.get("macaddress").getAsString();

						Debug.info("bridge infos", "id: " + "hidden", "name: " + name, "internalipaddress: " + internalipaddress, "macaddress: " + "hidden");
						
						Settings.Bridge.setInternalipaddress(internalipaddress);
						
						login();
					}
				} 
				catch (Exception e)
				{
					Debug.exception(e);
				}
				
				if (tries > 6) // abort after serval tries
				{
					try
					{	
						timer.cancel();
						timer.purge();
						Main.ui.setConnectState(4);
						Debug.info(null, "connection to bridge timeout");
					}
					catch (Exception e)
					{
						Debug.exception(e);
					}
				}
				
				tries++;
			}
		};
		timer.scheduleAtFixedRate(addUserLoop, 0, 1500);
	}
	
	private static void login() throws Exception // try to login
	{
		JsonObject response = HRequest.GET("http://" + internalipaddress + "/api/" + username);
		if (HRequest.responseCheck(response) == "data")
		{
			Debug.info(null, "login successfull");
			getLights();
			Main.ui.setConnectState(2);
		}
		else if (HRequest.responseCheck(response) == "error")
		{
			createUser();
		}
	}
	
	private static void getLights() throws Exception
	{
		Debug.info(null, "get lights...");
		JsonObject response = HRequest.GET("http://" + internalipaddress + "/api/" + username + "/lights/");
		
		for (int i = 1; i < 50; i++)
		{
			if (response.has(String.valueOf(i)))
			{
				lights.add(new HLight(i));
			}
		}

		Debug.info(null, countLights() + " lights found");
	}
	
	public static int countLights()
	{
		return lights.size();
	}
	
	private static void createUser() throws Exception // create a new bridge user
	{
		Debug.info(null, "create new user...");
		Main.ui.setConnectState(3);
		
		final Timer timer = new Timer();
		TimerTask addUserLoop = new TimerTask()
		{
			String body = "{\"devicetype\": \"" + devicetype + "\", \"username\": \"" + username + "\"}";
			int tries = 0;
			public void run()
			{
				try // to register a new bridge user (user must press the link button)
				{
					tries++;
					JsonObject response = HRequest.POST("http://" + internalipaddress + "/api/", body);
					if (HRequest.responseCheck(response) == "success")
					{
						timer.cancel();
						timer.purge();
						Debug.info(null, "new user created");
						login();
					}
					else if (tries > 20) // abort after serval tries
					{
						timer.cancel();
						timer.purge();
						Main.ui.setConnectState(4);
						Debug.info(null, "link button not pressed");
					}
				} 
				catch (Exception e)
				{
					Debug.exception(e);
				}
			}
		};
		timer.scheduleAtFixedRate(addUserLoop, 1500, 1500);
	}

}
