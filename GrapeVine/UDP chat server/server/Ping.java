package server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;

import controller.ServerController;

public class Ping extends Thread
{
	private final String PINGMSG = "PING";
	private DatagramPacket sendPacket;
	private byte[] byteArray;
	private ServerController sc;
	
	public Ping()
	{
		byteArray = new byte[1024];
		sc = ServerController.getInstance();
	}
	
	public void run()
	{
		try
		{
			while(true)
			{
				clearBytes();
				for(Integer port : ServerController.getInstance().getUserMap().keySet())
				{
					byteArray = PINGMSG.getBytes("UTF-8");
					sendPacket = new DatagramPacket(byteArray, byteArray.length, sc.getUser(port).getIp(), port);
					sc.getUDPsocket().send(sendPacket);
				}
				sleep(1000);
				sendListToClients();
			}
		}catch(InterruptedException e)
		{
			System.out.println("InterruptedException in class Ping: Unable to interrupt sleeping thread");
			interrupt();
//			e.printStackTrace();
		} catch (UnsupportedEncodingException e)
		{
			System.out.println("UnsupportedEncodingException in class Ping: Unable to convert message to byteArray!");
		} 
		catch (IOException e)
		{
			System.out.println("IOException in class Ping: Unable to send packet");
		}
	}
	
	public void sendListToClients() throws UnsupportedEncodingException, IOException
	{
		sc.updateUserMap();
		ArrayList<String> clientList2 = new ArrayList<String>();
		int length = 4;
		for(Integer port : sc.getUserMap().keySet())
		{
			User user = sc.getUser(port);
			clientList2.add(user.getName().trim());
			if(user.getName().length()<length)
				length = user.getName().length();
		}
		for(Integer port : sc.getUserMap().keySet())
		{
			clearBytes();
			byteArray = Shellsort.sort(clientList2, length).getBytes("UTF-8");
			sendPacket = new DatagramPacket(byteArray, byteArray.length, sc.getUser(port).getIp(), port);
			ServerController.getInstance().getUDPsocket().send(sendPacket);
		}
	}
	
	public void clearBytes()
	{
		for (int i = 0; i < byteArray.length; i++)
			byteArray[i] = '\0';
	}
}
