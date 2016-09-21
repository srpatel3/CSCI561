import java.io.*;
import java.net.*;
import java.util.*;

public class Server{

	public static void main(String args[]) throws Exception{
		//set the port number
		int port = 6789;
		//Establish the listen socket
		ServerSocket welcomeSocket = new ServerSocket(port);
		
		//Process Http request in an infinite loop
		while(true){
			//Listen for a TCP connection request
			Socket ClientSocket = welcomeSocket.accept();
			//Construct an Object to Process an http request
			HttpRequest request = new HttpRequest(ClientSocket);
			//Create new thread to process the request
			Thread thread = new Thread(request);
			//start the thread
			thread.start();

		}
	}
}


final class HttpRequest implements Runnable{
	Socket ClientSocket;
	final static String CRLF = "\r\n";

	//Constructor
	public HttpRequest(Socket ClientSocket){
		this.ClientSocket = ClientSocket;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			processRequest();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

	private void processRequest()throws Exception{
		Socket ServerSocket = null;
		InputStream fromClient = ClientSocket.getInputStream();
		OutputStream outToClient = ClientSocket.getOutputStream();
		
		
	}
}
