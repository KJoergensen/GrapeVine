package server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.util.ArrayList;

import controller.ServerController;

public class Ping extends Thread
{
	private final String PINGMSG = "PING";
	private DatagramPacket sendPacket;
	private byte[] byteArray;
	
	public Ping()
	{
		byteArray = new byte[1024];
	}
	
	public void run()
	{
		try
		{
			while(true)
			{
				clearBytes();
				for(User user : ServerController.getInstance().getUserList())
				{
					byteArray = PINGMSG.getBytes("UTF-8");
					sendPacket = new DatagramPacket(byteArray, byteArray.length, user.getIp(), user.getPortNr());
					ServerController.getInstance().getUDPsocket().send(sendPacket);
				}
				sleep(100);
				sendListToClients();
				sleep(2500);
			}
		}catch(InterruptedException e)
		{
			System.out.println("InterruptedException in class Ping: Trying to interrupt sleeping thread");
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
		ServerController.getInstance().updateUserList();
		String clientList = "CLIENTS";
		ArrayList<String> clientList2 = new ArrayList<String>();
		int length = 4;
		for(User user : ServerController.getInstance().getUserList())
		{
//			clientList += " "+user.getName();
			clientList2.add(user.getName().trim());
			if(user.getName().length()<length)
				length = user.getName().length();
		}
		clientList = Shellsort.sort(clientList2, length);
//		System.out.println(clientList);
//		System.out.println("List2="+test);
		for(User user : ServerController.getInstance().getUserList())
		{
			clearBytes();
			byteArray = clientList.getBytes("UTF-8");
			sendPacket = new DatagramPacket(byteArray, byteArray.length, user.getIp(), user.getPortNr());
			ServerController.getInstance().getUDPsocket().send(sendPacket);
		}
	}
	
	public void clearBytes()
	{
		for (int i = 0; i < byteArray.length; i++)
			byteArray[i] = '\0';
	}
}
