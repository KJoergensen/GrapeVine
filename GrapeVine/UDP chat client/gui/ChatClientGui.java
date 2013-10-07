package gui;

import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import client.DatagramListener;
import client.MulticastListener;

public class ChatClientGui
{
	public JFrame frame;
	private JButton btnConnect;
	private JButton btnDisconnect;
	private JLabel lblOnlineUsers;
    private JTextField txtName;
	private JTextField txtPort;
	private JTextField txtIP;
	private JTextField txtChat;
	private List connectionList;
	private List chatList;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenu mnOption;
	private JMenu mnHelp;
	private JMenuItem mntmNewClient;
	private JMenuItem mntmClose;
    private byte[] dataBuffer;
    private DatagramSocket UDPsocket;
    private DatagramPacket sendPacket;
    private int portNumber;
    private InetAddress IPaddress;
    private Thread datagramListener;
    private Thread multicastListener;
    private JCheckBox chboxPriv;

	/**
	 * Create the application.
	 */
	public ChatClientGui()
	{
		dataBuffer = new byte[1024];
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame("Chat Client");
		frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
            	disconnect();
            }
        });
		frame.setBounds(100, 100, 596, 533);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		connectionList = new List();
		connectionList.setBounds(448, 67, 124, 335);
		frame.getContentPane().add(connectionList);
		
		chatList = new List();
		chatList.setBounds(10, 67, 432, 364);
		frame.getContentPane().add(chatList);
		
		lblOnlineUsers = new JLabel("Offline");
		lblOnlineUsers.setHorizontalAlignment(SwingConstants.CENTER);
		lblOnlineUsers.setBounds(448, 47, 124, 14);
		frame.getContentPane().add(lblOnlineUsers);
		
		JLabel lblChat = new JLabel("Chat");
		lblChat.setBounds(10, 47, 46, 14);
		frame.getContentPane().add(lblChat);
		
		JLabel lblIPAddress = new JLabel("Connect to:");
		lblIPAddress.setBounds(161, 11, 90, 14);
		frame.getContentPane().add(lblIPAddress);
		
		txtIP = new JTextField();
		try
		{
			txtIP.setText(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e1)
		{
			txtIP.setText("Unknown IP!?");
		}
		txtIP.setBounds(230, 8, 112, 20);
		frame.getContentPane().add(txtIP);
		txtIP.setColumns(10);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(359, 11, 38, 14);
		frame.getContentPane().add(lblPort);
		
		txtPort = new JTextField();
		txtPort.setText("9999");
		txtPort.setBounds(393, 8, 57, 20);
		frame.getContentPane().add(txtPort);
		txtPort.setColumns(10);
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				if(checkUsername(txtName.getText()))
				{
					try
					{
						UDPsocket = new DatagramSocket();
						IPaddress = InetAddress.getByName(txtIP.getText());
						portNumber = Integer.parseInt(txtPort.getText());
						dataBuffer = ("JOIN "+txtName.getText()).getBytes("UTF-8");
						sendPacket = new DatagramPacket(dataBuffer, dataBuffer.length, IPaddress, portNumber);
						UDPsocket.send(sendPacket);
						addMessage("Connecting to server ...");
						startDatagramListener();
					} catch (UnknownHostException e)
					{
						addMessage("Unable to get IP-Address"+e.getMessage());
					} catch (IOException e)
					{
						addMessage("Unable to connect"+e.getMessage());
					}
				}
				else
					addMessage("please use letters only");
			}
		});
		btnConnect.setBounds(460, 7, 112, 23);
		frame.getContentPane().add(btnConnect);
		
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				disconnect();
				btnConnect.setVisible(true);
				btnDisconnect.setVisible(false);
			}
		});
		btnDisconnect.setBounds(460, 7, 112, 23);
		frame.getContentPane().add(btnDisconnect);
		
		txtChat = new JTextField();
		txtChat.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				String message = "";
				if(chboxPriv.isSelected())
				{
					int tries = 0;
					while(tries<3)
					{
						String usernameTo = connectionList.getSelectedItem();
						if(!usernameTo.equals(null))
						{
							message = "MSG PRIVATE "+txtName.getText()+" "+usernameTo+" "+txtChat.getText();
							addMessage("Private message to "+usernameTo+": "+txtChat.getText());
							break;
						}
						else
							tries++;
					}
				}
				else
					message = "MSG PUBLIC "+txtName.getText()+" "+txtChat.getText();
				sendPacket(message);
				txtChat.setText("");
			}
		});
		txtChat.setBounds(10, 443, 562, 20);
		frame.getContentPane().add(txtChat);
		txtChat.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(10, 11, 46, 14);
		frame.getContentPane().add(lblName);
		
		txtName = new JTextField();
		txtName.setText("Testsubject");
		txtName.setBounds(52, 8, 90, 20);
		frame.getContentPane().add(txtName);
		txtName.setColumns(10);
		
		chboxPriv = new JCheckBox("Private message");
		chboxPriv.setBounds(448, 408, 124, 23);
		frame.getContentPane().add(chboxPriv);
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmNewClient = new JMenuItem("New Client");
		mnFile.add(mntmNewClient);
		
		mntmClose = new JMenuItem("Close");
		mnFile.add(mntmClose);
		
		mnOption = new JMenu("Option");
		menuBar.add(mnOption);
		
		mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
	}
	
	public void startDatagramListener()
	{
		try
		{
			datagramListener = new Thread (new DatagramListener(UDPsocket, ChatClientGui.this, txtName.getText()));
			datagramListener.start();
		} catch(Exception e)
		{
			System.out.println("error - startDatagramListener");
		}
	}
	
	public void startMulticastListener()
	{
		try
		{
			multicastListener = new Thread(new MulticastListener(IPaddress, portNumber, ChatClientGui.this, UDPsocket));
			multicastListener.start();
		} catch(Exception e)
		{
			System.out.println("error - startMulticastListener");
		}
	}
	
	public void connectionStatusOK()
	{
		startMulticastListener();
		addMessage("Connected to "+IPaddress.toString()+":"+portNumber);
		btnConnect.setVisible(false);
		btnDisconnect.setVisible(true);
	}
	
	public void connectionStatusError(String message)
	{
		addMessage("Error - "+message);
	}
	
	public void sendPacket(String message)
	{
        try
		{
        	dataBuffer = message.getBytes("UTF-8");
        	sendPacket = new DatagramPacket(dataBuffer, dataBuffer.length, IPaddress, portNumber);
			UDPsocket.send(sendPacket);
		} catch (IOException e1)
		{
			System.out.println("Unable to send Packet");
		}
	}

    public void disconnect()
	{
    	try
    	{
    		sendPacket("QUIT");
    		datagramListener.interrupt();
    		multicastListener.interrupt();
    		UDPsocket.close();
    		addMessage("Connection closed");
    		clearConnectionList();
    		lblOnlineUsers.setText("Offline");
    	}
    	catch (Exception e)
    	{
    		System.out.println("Error trying to close connection");
    	}
	}

	public void addMessage(String message)
    {
    	chatList.add(message);
    	gotoBottom();
    }
    
    public void gotoBottom()
    {
		chatList.select(chatList.getItemCount() - 1);
		chatList.deselect(chatList.getItemCount() - 1);
    }
	
	public void clearConnectionList()
	{
		connectionList.removeAll();
	}
	
	public void addConnectionToList(String name)
	{
		connectionList.add(name);
	}
	
	public void updateClientList(ArrayList<String> nameList)
	{
		lblOnlineUsers.setText("Online User(s): "+nameList.size());
		String selectedName = connectionList.getSelectedItem();
		clearConnectionList();
		int selectedIndex=-1;
		for(String name : nameList)
		{
			addConnectionToList(name.trim());
			if(name.equals(selectedName))
				selectedIndex = connectionList.getItemCount()-1;
		}
		if(selectedIndex!=-1)
			connectionList.select(selectedIndex);
	}
	
	public boolean checkUsername(String username)
	{
		if(username.isEmpty())
			return false;
		for(char c : username.toLowerCase().toCharArray())
		{
			int tmp = c;
			if(!(tmp > 96 && tmp < 123) || tmp==230 || tmp==248 || tmp==229) // Checking username letters
				return false;
		}
		return true;
	}
}
