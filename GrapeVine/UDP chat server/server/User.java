package server;

import java.net.InetAddress;
import java.util.Date;

public class User
{
	private long lastSeen;
	private String name;
	private InetAddress ip;
	private Integer portNr;

	public User(String name, InetAddress ip, Integer portNr)
	{
		lastSeen = new Date().getTime();
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

	public boolean isOnline()
	{
		System.out.println("LAST SEEN: "+(new Date().getTime()-lastSeen));
		if((new Date().getTime()-lastSeen)<500)
			return true;
		else
			return false;
	}

	public int getLastSeen()
	{
		long now = new Date().getTime();
		return (int)(lastSeen-now);
	}

	public void setLastSeen()
	{
		this.lastSeen = new Date().getTime();
	}

}