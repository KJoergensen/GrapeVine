package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controller.ServerController;

import java.awt.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChatServerGui
{

	public JFrame frame;
	public ArrayList<String> clientNameArray;
	public ArrayList<InetAddress> clientAddressArray;
	public ArrayList<Integer> clientPortArray;
	private JTextField txtSocket;
	private List chatList;
	private JButton btnStopServer;
	private JButton btnStartServer;

	/**
	 * Create the application.
	 */
	public ChatServerGui()
	{
		initialize();
		clientNameArray = new ArrayList<String>();
		clientAddressArray = new ArrayList<InetAddress>();
		clientPortArray = new ArrayList<Integer>();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame = new JFrame();
		frame.setTitle("Chat Server");
		frame.setBounds(100, 100, 440, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setLocation(dim.width/3-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		
		JLabel lblIPAddress;
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(new URL("http://checkip.amazonaws.com").openStream()));
			String ip = in.readLine();
                	
			lblIPAddress = new JLabel("Your IP Address: "+ip);
		} catch (IOException e1)
		{
			try
			{
				lblIPAddress = new JLabel("Your IP Address: "+InetAddress.getLocalHost().getHostAddress());
			} catch (UnknownHostException e2)
			{
				lblIPAddress = new JLabel("Your IP Address: Unknown");
			}
		}
		lblIPAddress.setBounds(10, 11, 213, 14);
		
		frame.getContentPane().add(lblIPAddress);

		JLabel lblLocalAddress;
		try
		{
			lblLocalAddress = new JLabel("Your Local Address: "+InetAddress.getLocalHost().getHostAddress());
			lblLocalAddress.setHorizontalAlignment(SwingConstants.RIGHT);
		} catch (IOException e1)
		{
			lblLocalAddress = new JLabel("Your Local Address: Unknown");
		}
		lblLocalAddress.setBounds(221, 11, 193, 14);
		frame.getContentPane().add(lblLocalAddress);
		
		btnStartServer = new JButton("Start Server");
		btnStartServer.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				try
				{
					ServerController.getInstance().startServer(Integer.parseInt(txtSocket.getText()));
					addMessage("Server running on socket "+txtSocket.getText());
					ServerController.getInstance().startPing();
					
					btnStartServer.setVisible(false);
					btnStopServer.setVisible(true);
				} catch (NumberFormatException e)
				{
					System.out.println("Unable to start server on socket: "+txtSocket.getText());
//					e.printStackTrace();
				}
			}
		});
		btnStartServer.setBounds(304, 39, 110, 23);
		frame.getContentPane().add(btnStartServer);
		JLabel lblSocketNo = new JLabel("Socket No.");
		lblSocketNo.setBounds(10, 43, 73, 14);
		frame.getContentPane().add(lblSocketNo);
		txtSocket = new JTextField();
		txtSocket.setText("9999");
		txtSocket.setBounds(79, 40, 41, 20);
		frame.getContentPane().add(txtSocket);
		txtSocket.setColumns(10);
		
		chatList = new List();
		chatList.setBounds(10, 72, 404, 180);
		frame.getContentPane().add(chatList);
		
		btnStopServer = new JButton("Stop Server");
		btnStopServer.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				ServerController.getInstance().stopServer();
				addMessage("Server stopped");
				btnStartServer.setVisible(true);
				btnStopServer.setVisible(false);
			}
		});
		btnStopServer.setBounds(304, 39, 110, 23);
		frame.getContentPane().add(btnStopServer);
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
}
