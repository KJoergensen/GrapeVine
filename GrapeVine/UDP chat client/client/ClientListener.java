package client;

import gui.ChatClientGui;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ClientListener extends Thread
{
	private DatagramSocket ds;
	private DatagramPacket receivePacket;
	private byte[] byteArray;
	private ChatClientGui clientGUI;
	private StringTokenizer st;

	public ClientListener(DatagramSocket ds, ChatClientGui client, String username)
	{
		this.clientGUI = client;
		this.ds = ds;
		byteArray = new byte[1024];
		new ArrayList<String>();
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
				String data = new String(receivePacket.getData(), "UTF-8").trim();
				st = new StringTokenizer(data.trim(), Protocol.DELIMITER);
				String request = st.nextToken();
				if(request.equals(Protocol.GET_JOIN))
				{
					String status = st.nextToken();
					if(status.equals(Protocol.GET_JOIN_USERNAMETAKEN))
						clientGUI.connectionStatusError("Username taken");
					if(status.equals(Protocol.GET_JOIN_ACCEPT))
						clientGUI.connectionStatusOK();
					if(status.equals(Protocol.GET_JOIN_DECLINED))
						clientGUI.connectionStatusError("Your request was declined");
				}
				else
				 	clientGUI.addMessage(data.trim());
			} catch (IOException e)
			{
				System.out.println("Unable to receive Datagram.");
				ds.close();
				break;
			}
		}
	}
}