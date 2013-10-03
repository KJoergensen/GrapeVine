package controller;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import server.*;
import gui.ChatServerGui;

public class ServerController
{
	private ArrayList<User> userList = new ArrayList<User>();
	private ArrayList<User> tempUserList = new ArrayList<User>();
	private Thread ping;
	private DatagramSocket UDPsocket;
	private ServerListener listener;
	private ChatServerGui GUI;

	// /////////////
	// Singleton //
	private static ServerController controller = new ServerController();

	public static ServerController getInstance()
	{
		return controller;
	}
	
	public void startGUI()
	{
		GUI = new ChatServerGui();
		GUI.frame.setVisible(true);
	}
	
	public ChatServerGui getGUI()
	{
		return GUI;
	}
	
	public DatagramSocket getUDPsocket()
	{
		return UDPsocket;
	}
	
	public void addUserToList(User user)
	{
		userList.add(user);
	}

	public ArrayList<User> getUserList()
	{
		return userList;
	}

	public void startServer(int port)
	{
		// Setup socket
		// Create serverListener
		try
		{
			UDPsocket = new DatagramSocket(port);
			listener = new ServerListener();
			listener.start();
		} catch (SocketException e)
		{
			e.printStackTrace();
		}
	}
	
	public void startPing()
	{
		ping = new Thread(new Ping());
		ping.start();
	}
	
	public void addUserRespond(User user)
	{
		tempUserList.add(user);
	}
	
	public void updateUserList()
	{
		userList.removeAll(userList);
		for(User user : tempUserList)
		{
			userList.add(user);
		}
		tempUserList.removeAll(tempUserList);
	}

	public void stopServer()
	{
		UDPsocket.close();
		userList.removeAll(userList);
		tempUserList.removeAll(tempUserList);
		listener.interrupt();
		ping.interrupt();
	}
}