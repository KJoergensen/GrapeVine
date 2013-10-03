package client;

public class Protocol 
{
	final static String DELIMITER = " ";
	
	final static String GET_CLIENTLIST = "CLIENTS";
	
	final static String GET_JOIN = "JOIN";
	final static String GET_JOIN_USERNAMETAKEN = "TAKEN";
	final static String GET_JOIN_ACCEPT = "OK";
	final static String GET_JOIN_DECLINED = "DECLINE";
	
	final static String GET_PING = "PING";
	final static String SEND_PING = "PING";
	
	final static String GET_MESSAGE = "MSG";
	final static String SEND_MESSAGE_PRIVATE = "MSG PRIVATE";
	final static String SEND_MESSAGE_PUBLIC = "MSG PUBLIC";
}
