package server;

import java.net.InetAddress;

public class User
{

	private String name;
	private InetAddress ip;
	private Integer portNr;

	public User(String name, InetAddress ip, Integer portNr)
	{
		this.name = name;
		this.ip = ip;
		this.portNr = portNr;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public InetAddress getIp()
	{
		return ip;
	}

	public void setIp(InetAddress ip)
	{
		this.ip = ip;
	}

	public Integer getPortNr()
	{
		return portNr;
	}

	public void setPortNr(Integer portNr)
	{
		this.portNr = portNr;
	}

}