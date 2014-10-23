package hueimmersive;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.*;

public class HRequest
{

	public static JsonObject GET(String APIurl) throws Exception // GET Request
	{
		URL apiUrl = new URL(APIurl);
		HttpURLConnection apiConnection = (HttpURLConnection) apiUrl.openConnection();
		apiConnection.setRequestMethod("GET");
		String input = null;
		if (connectTest(apiConnection) == true)
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));
			input = in.readLine();
			in.close();
			
			return extractJObject(input);
		}
		else
		{
			return null;
		}
	}
	
	public static JsonObject PUT(String APIurl, String data) throws Exception // PUT Request
	{
		URL apiUrl = new URL(APIurl);
		HttpURLConnection apiConnection = (HttpURLConnection) apiUrl.openConnection();
		apiConnection.setRequestMethod("PUT");
		apiConnection.setDoOutput(true);
		
		String input = null;
		if (connectTest(apiConnection) == true)
		{
			OutputStreamWriter out = new OutputStreamWriter(apiConnection.getOutputStream());
			out.write(data);
	        out.close();
	        
			BufferedReader in = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));
			input = in.readLine();
			in.close();
		}
		
		return extractJObject(input);
	}
	
	public static JsonObject POST(String APIurl, String data) throws Exception // POST Request
	{
		URL apiUrl = new URL(APIurl);
		HttpURLConnection apiConnection = (HttpURLConnection) apiUrl.openConnection();
		apiConnection.setRequestMethod("POST");
		apiConnection.setDoOutput(true);

		String input = null;
		if (connectTest(apiConnection) == true)
		{
			OutputStreamWriter out = new OutputStreamWriter(apiConnection.getOutputStream());
			out.write(data);
	        out.close();
	        
			BufferedReader in = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));
			input = in.readLine();
			in.close();
		}

		return extractJObject(input);
	}
	
	public static void DELETE()
	{
		// not needed yet
	}
	
	private static boolean connectTest(HttpURLConnection connection) // test a connection
	{
		try // to connect to URL
		{
			connection.setConnectTimeout(400);
			return true;
		} 
		catch (Exception e)
		{
			Debug.exception(e);
			return false;
		}
	}
	
	private static JsonObject extractJObject(String response) // make the response message to a JsonObject
	{
		JsonElement jElement = new JsonParser().parse(response);
		
		JsonObject jObject = new JsonObject();
		if(jElement.isJsonArray())
		{
			jObject = jElement.getAsJsonArray().get(0).getAsJsonObject();
		}
		else if(jElement.isJsonObject())
		{
			jObject = jElement.getAsJsonObject();
		}
		
		return jObject;
	}
	
	public static String responseCheck(JsonObject response) // check what the type from the response message
	{
		String result;
		
		if(response == null)
		{
			result = null;
			//Debug.info("NULL");
		}
		else if (response.has("success"))
		{
			result = "success";
			//Debug.info("SUCCESS");
		}
		else if(response.has("error"))
		{
			result = "error";
			//Debug.info("ERROR");
		}
		else
		{
			result = "data";
			//Debug.info("DATA");
		}
		
		return result;
	}
}
