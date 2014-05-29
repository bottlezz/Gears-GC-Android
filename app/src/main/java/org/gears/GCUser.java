package org.gears;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;

public class GCUser {
	@Expose
	private int userId;
	@Expose
	private String isHost;
	
	@Expose
	private String name;
	@Expose
	private JsonObject properties;
	
	private static int count = 0;
	
	private static synchronized int getNextID()
	{
		return count++;
	}
	
	public GCUser()
	{
		this.userId = GCUser.getNextID();
	}
	
	public int getUserID() {
		return userId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIsHost() {
		return isHost;
	}
	
	public void setIsHost(String isHost) {
		this.isHost = isHost;
	}
	
	public JsonObject getProperties() {
		return properties;
	}
	
	public void setProperties(JsonObject properties) {
		this.properties = properties;
	}
	
	public void parseJson(String json)
	{
		if (json == null)
			return;
		
		Gson gson = new Gson();
		try
		{
			JsonObject obj = gson.fromJson(json, JsonObject.class);
			
			JsonObject body = obj.get("body").isJsonNull() ? null : obj.get("body").getAsJsonObject();
			
			if (body != null)
			{
				this.name = body.get("name").isJsonNull() ? null : body.get("name").getAsString();
				this.properties = body.get("properties").isJsonNull() ? null : body.get("properties").getAsJsonObject();
			}
		}
		catch(JsonSyntaxException jse)
		{
			this.name = null;
			this.properties = null;
		}
	}
	
	public String getJson()
	{
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
		try
		{
			return gson.toJson(this);
		}
		catch(JsonSyntaxException jse)
		{
			return null;
		}
	}
}
