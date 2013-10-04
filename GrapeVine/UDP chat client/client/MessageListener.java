package client;

import gui.ChatClientGui;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MessageListener extends Thread 
{
	private String username;
	private DatagramSocket dSocket;
	private MulticastSocket mSocket;
	private DatagramPacket packet;
	private InetAddress IPaddress;
	private int port;
	private byte[] byteArray;
	private StringTokenizer st;
	private ArrayList<String> clients;
	private ChatClientGui clientGUI;
	
	public MessageListener(InetAddress IPaddress, int port, ChatClientGui clientGUI, DatagramSocket dSocket)
	{
		this.IPaddress = IPaddress;
		this.port = port;
		this.clientGUI = clientGUI;
		this.dSocket = dSocket;
		this.IPaddress = IPaddress;
		clients = new ArrayList<String>();
		try {
			mSocket = new MulticastSocket(9998);
			mSocket.joinGroup(InetAddress.getByName("234.5.6.7"));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		while(true)
		{
			try {
				byteArray = new byte[1024];
				packet = new DatagramPacket(byteArray, byteArray.length);
				mSocket.receive(packet);
				String data = new String(packet.getData(), "UTF-8").trim();
				st = new StringTokenizer(data.trim(), Protocol.DELIMITER);
				String request = st.nextToken();
				if(request.equals(Protocol.GET_PING))
					sendPing(Protocol.SEND_PING+Protocol.DELIMITER+username);
				else if(request.equals(Protocol.GET_CLIENTLIST))
				{
					while(st.hasMoreTokens())
						clients.add(st.nextToken().trim());
					clientGUI.updateClientList(clients);
					clients.removeAll(clients);
				}
				else
				 	clientGUI.addMessage(data.trim());
			} catch (IOException e) {
				System.out.println("Unable to receive Multicast");
				e.printStackTrace();
				break;
			}
		}
	}

	private void sendPing(String message) throws IOException 
	{
		byteArray = message.getBytes("UTF-8");
		packet = new DatagramPacket(byteArray, byteArray.length, IPaddress, port);
		dSocket.send(packet);
	}
}
