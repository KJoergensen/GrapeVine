package server;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.StringTokenizer;

import controller.ServerController;

public class ServerListener extends Thread
{
	private DatagramPacket sendPacket;
	private DatagramPacket receivePacket;
	private InetAddress IPaddress;
	private int port;
	private ServerController sc;
	private byte[] byteArray;
	
	public ServerListener()
	{
		sc = ServerController.getInstance();
	}
	
	public void run()
	{
		byteArray = new byte[1024];
		try
		{
			while (true)
			{
				byteArray = new byte[1024];
				receivePacket = new DatagramPacket(byteArray, byteArray.length);
				sc.getUDPsocket().receive(receivePacket);
				IPaddress = receivePacket.getAddress();
				port = receivePacket.getPort();
				byteArray = receivePacket.getData();
				String data = new String(byteArray, "UTF-8").trim();

				StringTokenizer st = new StringTokenizer(data.trim());
				String request = st.nextToken();

				if(request.equals(Protocol.GET_PING))
					sc.addUserRespond(port);
				else if(request.equals(Protocol.GET_QUIT))
					sc.removeUser(port);
				else if(request.equals(Protocol.GET_JOIN))
				{
					String username = st.nextToken().trim();
					boolean usernameTaken = false;
					for(Integer userPort : sc.getUserMap().keySet())
						if(sc.getUser(userPort).getName().equals(username))
							usernameTaken = true;
					if (usernameTaken)
						reply(Protocol.SEND_JOIN_USERNAMETAKEN);
					else if(!usernameTaken)
					{
						reply(Protocol.SEND_JOIN_ACCEPT);
						sc.addUserToMap(new User(username, IPaddress, port));
						sc.getGUI().addMessage("New connection: "+username);
					}
					else
						reply(Protocol.SEND_JOIN_DECLINED);
				}
				
				else if(request.equals(Protocol.GET_MESSAGE))
				{
					String message = "";
					request = st.nextToken();
					if(request.equals(Protocol.GET_MESSAGE_PUBLIC))
					{
						String username = st.nextToken();
						while(st.hasMoreTokens())
							message += st.nextToken()+" ";
						sendPublicMessage(username, message);
					}
					else if(request.equals(Protocol.GET_MESSAGE_PRIVATE))
					{
						String usernameFrom = st.nextToken();
						String usernameTo = st.nextToken();
						while(st.hasMoreTokens())
							message += st.nextToken()+" ";
						sendPrivateMessage(usernameFrom, usernameTo, message);
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void reply(String message) throws Exception
	{
		byteArray = message.getBytes("UTF-8");
		sendPacket = new DatagramPacket(byteArray, byteArray.length, IPaddress, port);
		sc.getUDPsocket().send(sendPacket);
	}
	
	public void sendPublicMessage(String from, String message) throws Exception
	{
		for(Integer port : sc.getUserMap().keySet())
		{
			byteArray = (from+": "+message).getBytes("UTF-8");
			sendPacket = new DatagramPacket(byteArray, byteArray.length, sc.getUser(port).getIp(), port);
			sc.getUDPsocket().send(sendPacket);
		}
	}
	
	public void sendPrivateMessage(String from, String to, String message) throws Exception
	{
		for(Integer port : sc.getUserMap().keySet())
		{
			User user = sc.getUser(port);
			if(user.getName().equals(to))
			{
				byteArray = ("Private message from "+from+": "+message).getBytes("UTF-8");
				sendPacket = new DatagramPacket(byteArray, byteArray.length, user.getIp(), user.getPortNr());
				sc.getUDPsocket().send(sendPacket);
				break;
			}
		}
	}
}
