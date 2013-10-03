package client;

import gui.ChatClientGui;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
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
	private ChatClientGui clientGUI;
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
				byteArray = new byte[1024];
				receivePacket = new DatagramPacket(byteArray, byteArray.length);
				ds.receive(receivePacket);
				IPaddress = receivePacket.getAddress();
				port = receivePacket.getPort();
				String data = new String(receivePacket.getData(), "UTF-8");
				
				st = new StringTokenizer(data.trim(), " ");
				String request = st.nextToken();
				if(request.equals("PING"))
				{
					byteArray = ("PING "+username).getBytes("UTF-8");
					sendPacket = new DatagramPacket(byteArray, byteArray.length, IPaddress, port);
					ds.send(sendPacket);
				}
				else if(request.equals("CLIENTS"))
				{
					while(st.hasMoreTokens())
						clients.add(st.nextToken().trim());		
					clientGUI.updateClientList(clients);
					clients.removeAll(clients);
				}
				else if(request.equals("JOIN"))
				{
					String status = st.nextToken();
					if(status.equals("TAKEN"))
						clientGUI.connectionStatusError("Username taken");
					if(status.equals("OK"))
						clientGUI.connectionStatusOK();
				}
				else
				{
				 	clientGUI.addMessage(data.trim());
				}
			} catch (IOException e)
			{
				System.out.println("Unable to receive message.");
//				e.printStackTrace();
				break;
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