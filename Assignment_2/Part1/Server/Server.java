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
		//Get a reference to Socket's input and output stream
		InputStream is = ClientSocket.getInputStream();
		DataOutputStream os = new DataOutputStream(ClientSocket.getOutputStream());
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));

		//Get the requestLine from client
		String RequestLine = inFromClient.readLine();
		System.out.println();
		System.out.println(RequestLine);
		//Get and display the header lines
		String HeaderLien = null;
		while((HeaderLien = inFromClient.readLine()).length()!=0){
			System.out.print(HeaderLien);
		}
//		is.close();
		os.close();
		inFromClient.close();
		ClientSocket.close();
	}
}