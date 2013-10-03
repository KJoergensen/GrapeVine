package controller;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import server.*;
import gui.ChatServerGui;

public class ServerController
{
	private HashMap<Integer, User> userMap = new HashMap<>();
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
	
	public void addUserToMap(User user)
	{
		userMap.put(user.getPortNr(), user);
	}

	public User getUser(int port)
	{
		return userMap.get(port);
	}

	public HashMap<Integer, User> getUserMap()
	{
		return userMap;
	}

	public void startServer(int port)
	{
		try
		{
			UDPsocket = new DatagramSocket(port);
			listener = new ServerListener();
			ping = new Thread(new Ping());
			listener.start();
			ping.start();
			
			GUI.addMessage("Server running on socket "+port);
			GUI.btnStartServer.setVisible(false);
			GUI.btnStopServer.setVisible(true);
		} catch (SocketException e)
		{
			GUI.addMessage("Unable to start server on socket "+port+". Please try another");
			e.printStackTrace();
		}
	}
	
	public void addUserRespond(int port)
	{
		userMap.get(port).setLastSeen();
	}
	
	public void updateUserMap()
	{
		ArrayList<Integer> offlineList = new ArrayList<>();
		for(Integer port : userMap.keySet())
			if(!userMap.get(port).isOnline())
				offlineList.add(port);
		for(Integer userPort: offlineList)
			removeUser(userPort);
	}
	
	public void removeUser(Integer userPort)
	{
		GUI.addMessage(userMap.get(userPort).getName()+" has disconnected");
		userMap.remove(userPort);
	}

	public void stopServer()
	{
		try{
			UDPsocket.close();
			userMap.clear();
			listener.interrupt();
			ping.interrupt();
			
			GUI.addMessage("Server stopped");
			GUI.btnStartServer.setVisible(true);
			GUI.btnStopServer.setVisible(false);
		}
		catch(Exception e)
		{
			GUI.addMessage("Unable to stop server: "+e.getMessage());
			e.printStackTrace();
		}
	}
}