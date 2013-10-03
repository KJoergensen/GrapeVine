package server;

public class Protocol
{
	final static String DELIMITER = " ";
	final static String GET_PING = "PING";
	final static String GET_QUIT = "QUIT";
	final static String GET_JOIN = "JOIN";
	final static String GET_MESSAGE = "MSG";
	final static String GET_MESSAGE_PRIVATE = "PRIVATE";
	final static String GET_MESSAGE_PUBLIC = "PUBLIC";
	final static String SEND_JOIN_USERNAMETAKEN = "JOIN TAKEN";
	final static String SEND_JOIN_ACCEPT = "JOIN OK";
	final static String SEND_JOIN_DECLINED = "JOIN DECLINE";
}
