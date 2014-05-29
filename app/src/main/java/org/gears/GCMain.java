package org.gears;

import org.gears.network.GCCommunicationServer;

public class GCMain
{
	public static void main(String[] args)
	{
		try
		{
			GCCommunicationServer server = new GCCommunicationServer(8081);
			server.start();
		}
		catch (Exception e)
		{
			
		}
	}
}
