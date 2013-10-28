package server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import controller.ServerController;

/**
 * Thread class that is used to continuously update the online users.
 * 
 * Updates every second.
 */
public class Ping extends Thread
{
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
				if(!sc.getUserMap().keySet().isEmpty())
				{
					byteArray = new byte[1024];
					byteArray = Protocol.GET_PING.getBytes("UTF-8");
					sendPacket = new DatagramPacket(byteArray, byteArray.length, InetAddress.getByName("234.5.6.7"), 9998);
					sc.getMulticastSocket().send(sendPacket);
					sendListToClients();
				}
				sleep(1000);
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
	
	/**
	 * Sending the user list to the clients
	 */
	public void sendListToClients() throws UnsupportedEncodingException, IOException
	{
		sc.updateUserMap();
		ArrayList<String> clientList = new ArrayList<String>();
		int length = 4;
		for(Integer port : sc.getUserMap().keySet())
		{
			User user = sc.getUser(port);
			clientList.add(user.getName().trim());
			if(user.getName().length()<length)
				length = user.getName().length();
		}
		
		clearBytes();
		// Sorting the clientlist using Shellsort
		byteArray = Shellsort.sort(clientList, length).getBytes("UTF-8");
		sendPacket = new DatagramPacket(byteArray, byteArray.length, InetAddress.getByName("234.5.6.7"), 9998);
		// Using the ServerController to send the datagrampacket
		sc.getMulticastSocket().send(sendPacket);
	}
	
	/**
	 * Clearing the bytearray
	 */
	public void clearBytes()
	{
		for (int i = 0; i < byteArray.length; i++)
			byteArray[i] = '\0';
	}
}
