package gui;

import controller.ServerController;

public class Main
{
	public static void main(String[] args)
	{
		ServerController.getInstance().startGUI();
	}
}
