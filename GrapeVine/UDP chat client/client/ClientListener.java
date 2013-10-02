package client;

import gui.ChatClientGui;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class ClientListener extends Thread
{
	private String username;
	private DatagramSocket ds;
	private DatagramPacket sendPacket;
	private DatagramPacket receivePacket;
	private InetAddress IPaddress;
	private int port;
	private byte[] byteArray;
	ChatClientGui clientGUI;
	private ArrayList<String> clients;
	private StringTokenizer st;

	public ClientListener(DatagramSocket ds, ChatClientGui client, String username)
	{
		this.clientGUI = client;
		this.ds = ds;
		byteArray = new byte[1024];
		this.username = username;
		clients = new ArrayList<String>();
		
	}

	public void run()
	{
		while (true)
		{
			try
			{
//				clearBytes();
				byteArray= new byte[1024];
				System.out.println(Arrays.toString(byteArray));
				receivePacket = new DatagramPacket(byteArray, byteArray.length);
				ds.receive(receivePacket);
				IPaddress = receivePacket.getAddress();
				port = receivePacket.getPort();
				String data = new String(receivePacket.getData());
				System.out.println(Arrays.toString(receivePacket.getData()));
				
				st = new StringTokenizer(data, " ");
				String request = st.nextToken();
				if(request.trim().equals("PING"))
				{
					//clearBytes();
//					client.addMessage("PONG");
					byteArray = ("PING "+username).getBytes("UTF-8");
					sendPacket = new DatagramPacket(byteArray, byteArray.length, IPaddress, port);
					ds.send(sendPacket);
				}
				else if(request.trim().equals("CLIENTS"))
				{
					while(st.hasMoreTokens())
						clients.add(st.nextToken().trim());		
					clientGUI.updateClientList(clients);
					clients.removeAll(clients);
				}
				else
				{
				 	clientGUI.addMessage(data);
				}
			} catch (IOException e)
			{
				System.out.println("Unable to receive message.");
				break;
//				e.printStackTrace();
			}
		}
		ds.close();
	}
	
	public void clearBytes()
	{
		for (int i = 0; i < byteArray.length; i++)
			byteArray[i] = '\0';
	}
}