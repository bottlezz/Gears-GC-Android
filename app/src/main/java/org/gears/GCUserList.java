package org.gears;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;

public class GCUserList extends DataObject
{
	@Expose
	protected Collection<GCUser> userList;
	
	public Collection<GCUser> getUserList()
	{
		return this.userList;
	}
	
	public void setUserList(Collection<GCUser> list)
	{
		this.userList = list;
	}
	
	public GCUserList()
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		this.action = "user_list";
		this.timestamp = df.format(new Date(System.currentTimeMillis()));
		this.name = "user_list";
	}
	
	public String getJson()
	{
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
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
