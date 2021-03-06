package controller;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import server.*;
import gui.ChatServerGui;

/**
 * Singleton class that handles connection between GUI and serverlayer
 * 
 */
public class ServerController
{
	private HashMap<Integer, User> userMap = new HashMap<>();
	private Thread ping;
	private DatagramSocket UDPsocket;
	private MulticastSocket ms;
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

	public MulticastSocket getMulticastSocket()
	{
		return ms;
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
			ms = new MulticastSocket();
			UDPsocket = new DatagramSocket(port);
			listener = new ServerListener();
			ping = new Thread(new Ping());
			listener.start();
			ping.start();

			GUI.addMessage("Server running on socket " + port);
			GUI.btnStartServer.setVisible(false);
			GUI.btnStopServer.setVisible(true);
		} catch (SocketException e)
		{ // Default message if port is already in use
			GUI.addMessage("Unable to start server on socket " + port
					+ ". Please try another");
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void addUserRespond(int port)
	{
		try
		{
			userMap.get(port).setLastSeen();
		} catch (NullPointerException e)
		{
			System.out.println("User does not exist");
		}
	}

	/**
	 * Method for updating the userlist
	 */
	public void updateUserMap()
	{
		ArrayList<Integer> offlineList = new ArrayList<>();
		for (Integer port : userMap.keySet())
			// 'If user is not online'
			if (!userMap.get(port).isOnline())
				// Add user to offline list
				offlineList.add(port);
		for (Integer userPort : offlineList)
			removeUser(userPort);
	}

	/**
	 * Method used to removed the chosen user from user map
	 * 
	 * @param userPort
	 */
	public void removeUser(Integer userPort)
	{
		try
		{
			GUI.addMessage(userMap.get(userPort).getName()
					+ " has disconnected");
			userMap.remove(userPort);
		} catch (NullPointerException e)
		{
			System.out.println("User does not exist");
			e.printStackTrace();
		}
	}

	/**
	 * Method used to stop the server
	 */
	public void stopServer()
	{
		try
		{
			UDPsocket.close();
			userMap.clear();
			listener.interrupt();
			ping.interrupt();

			GUI.addMessage("Server stopped");
			GUI.btnStartServer.setVisible(true);
			GUI.btnStopServer.setVisible(false);
		} catch (Exception e)
		{
			GUI.addMessage("Unable to stop server: " + e.getMessage());
			e.printStackTrace();
		}
	}
}