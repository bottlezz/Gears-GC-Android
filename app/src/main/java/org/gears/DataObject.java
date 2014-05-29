package org.gears;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;

public class DataObject 
{
	@Expose
	protected String action;
	
	@Expose
	protected String timestamp;
	
	@Expose
	protected int userID;
	
	@Expose
	protected String name;
	
	@Expose
	protected JsonObject body;
	
	public String getAction() 
	{
		return action;
	}
	
	public String getTimestamp() 
	{
		return timestamp;
	}
	
	public int getUserID() 
	{
		return userID;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public JsonObject getBody() 
	{
		return body;
	}
	
	public void setAction(String action)
	{
		this.action = action;
	}
	
	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}
	
	public void setUserID(int userID)
	{
		this.userID = userID;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setBody(JsonObject body)
	{
		this.body = body;
	}
	
	public void parseJson(String json)
	{
		Gson gson = new Gson();
		try
		{
			String temp;
			JsonObject obj = gson.fromJson(json, JsonObject.class);
			this.action = obj.get("action").isJsonNull() ? null: obj.get("action").getAsString();
			this.timestamp = obj.get("timestamp").isJsonNull() ? null : obj.get("timestamp").getAsString();
			temp = obj.get("userID").isJsonNull() ? null : obj.get("userID").getAsString();
			this.userID = temp == null ? -1 : Integer.parseInt(temp);
			this.name = obj.get("name").isJsonNull() ? null : obj.get("name").getAsString();
			this.body = obj.get("body").isJsonNull() ? null : obj.get("body").getAsJsonObject();
		}
		catch(JsonSyntaxException jse)
		{
			this.action = null;
			this.timestamp = null;
			this.name = null;
			this.userID = -1;
			this.body = null;
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
