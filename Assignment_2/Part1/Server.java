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
		String requestLine = inFromClient.readLine();
		System.out.println();
		System.out.println(requestLine);
		//Get and display the header lines
		String HeaderLien = null;
		while((HeaderLien = inFromClient.readLine()).length()!=0){
			System.out.print(HeaderLien);
		}
		//Extract the filename from the request line
		StringTokenizer tokens = new StringTokenizer(requestLine);
		tokens.nextToken();
		String fileName ="." + tokens.nextToken();
		//Open and read file
		FileInputStream fis = null;
		boolean fileExists = true;
		try{
		fis = new FileInputStream(fileName);
		}
		catch(FileNotFoundException e){
			fileExists = false;
		}
		//Constructing Error Message
		String statusLine = null;
		String contentTypeLine = null;
		String entityBody = null;
		if(fileExists){
			statusLine = "OK";
			contentTypeLine = "Content Type: " +contentType(fileName)+CRLF;
		}
		else{
			statusLine = "404 File Not Found";
			contentTypeLine = "File Not Found";
			entityBody = "<HTML>" +
					"<HEAD><TITLE>Not Found</TITLE></HEAD>" +
					"<BODY>Not Found</BODY></HTML>";
		}
		//Sending Status line
		os.writeBytes(statusLine);
		//Sending contentTypeLine
		os.writeBytes(contentTypeLine);
		//Sending a blank line to indicate the end of header lines
		os.writeBytes(CRLF);
		//Sending the entityBody
		if (fileExists)
			{
				sendBytes(fis, os);
				fis.close();
		}
		else {
			os.writeBytes(entityBody);
		}
				os.close();
		inFromClient.close();
		ClientSocket.close();
	}
	//Implementation of sendBytes method
			private static void sendBytes(FileInputStream fis1, OutputStream os1)throws Exception{
				//Constructing 1K buffer to hold bytes on their way to the Socket
				byte[] buffer = new byte[1024];
				int bytes = 0;
				//Copying requested file in Socket's output stream
				while((bytes = fis1.read(buffer)) != -1 ) {
						os1.write(buffer, 0, bytes);
				}
				
			}
			//Implementing Content Type method
			private static String contentType(String fileName){
				if(fileName.endsWith("html")||fileName.endsWith("htm"))
					return "text/html";
				else
					return "application/octet-stream";
					
			}

}
