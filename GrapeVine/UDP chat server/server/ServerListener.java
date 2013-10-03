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
	byte[] byteArray;
	ServerController sc;
	
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
				ServerController.getInstance().getUDPsocket().receive(receivePacket);
				IPaddress = receivePacket.getAddress();
				port = receivePacket.getPort();
				byteArray = receivePacket.getData();
				String data = new String(byteArray, "UTF-8").trim();
				
				StringTokenizer st = new StringTokenizer(data.trim());
				String request = st.nextToken();

				if(request.equals("PING"))
				{
//					String username = st.nextToken().trim();
					ServerController.getInstance().addUserRespond(port);
//					ServerController.getInstance().addUserRespond(new User(username, IPaddress, port));
				}
				else if(request.equals("QUIT"))
				{
					for(User user : ServerController.getInstance().getUserList())
					{
						if(user.getPortNr().equals(port))
							ServerController.getInstance().getUserList().remove(user);
						ServerController.getInstance().getGUI().addMessage(user.getName()+" disconnected");
						break;
					}
				}
				
				else if(request.equals("JOIN"))
				{
					String username = st.nextToken().trim();
					boolean userExist = false;
					boolean usernameTaken = false;
					for(Integer userPort : sc.getUserMap().keySet())
					{
						if(port == userPort)
						{
							sc.addUserRespond(port);
							userExist = true;
						}
						if(sc.getUser(userPort).getName().equals(username))
							usernameTaken = true;
					}
					if (usernameTaken)
						reply("JOIN TAKEN");
					
					else if(!userExist)
					{
						reply("JOIN OK");
						ServerController.getInstance().addUserToMap(new User(username, IPaddress, port));
						ServerController.getInstance().getGUI().addMessage("New connection: "+username);
					}else if(userExist)
					{
						reply("JOIN OK");
						ServerController.getInstance().getGUI().addMessage(username+" has reconnected");
					}
				}
				
				else if(request.equals("MSG"))
				{
					String message = "";
					request = st.nextToken();
					if(request.equals("PUBLIC"))
					{
						String username = st.nextToken();
						while(st.hasMoreTokens())
							message += st.nextToken()+" ";
						sendPublicMessage(username, message);
					}
					else if(request.equals("PRIVATE"))
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
			
		}
	}
	
	public void reply(String message) throws Exception
	{
		byteArray = message.getBytes("UTF-8");
		sendPacket = new DatagramPacket(byteArray, byteArray.length, IPaddress, port);
		ServerController.getInstance().getUDPsocket().send(sendPacket);
	}
	
	public void sendPublicMessage(String from, String message) throws Exception
	{
		for(Integer port : sc.getUserMap().keySet())
		{
			byteArray = (from+": "+message).getBytes("UTF-8");
			sendPacket = new DatagramPacket(byteArray, byteArray.length, sc.getUser(port).getIp(), port);
			ServerController.getInstance().getUDPsocket().send(sendPacket);
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
				ServerController.getInstance().getUDPsocket().send(sendPacket);
				break;
			}
		}
	}
	
	public void clearBytes()
	{
//		for (int i = 0; i < byteArray.length; i++)
//			byteArray[i] = '\0';
	}
}
