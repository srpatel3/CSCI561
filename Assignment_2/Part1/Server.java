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
		boolean fileExists = false;
		try{
		fis = new FileInputStream(fileName);
		fileExists = true;
		}
		catch(FileNotFoundException e){
			fileExists = false;
		}
		//Constructing Error Message
		String statusLine = null;
		String contentTypeLine = null;
		String entityBody = null;
		String responseLine = null;
		if(fileExists){
			statusLine = "HTTP/1.1 200 OK"+CRLF;
			contentTypeLine = "Content-Type: " +contentType(fileName)+CRLF;
			responseLine = statusLine+contentTypeLine;
		}
		else{
			statusLine = "HTTP/1.1 404 Not Found"+CRLF;
			contentTypeLine = "";
			entityBody = "<HTML>" +
					"<HEAD><TITLE>Not Found</TITLE></HEAD>" +
					"<BODY>Not Found</BODY></HTML>"+CRLF;
			responseLine = statusLine+entityBody;
		}
		//Sending Status line
		//os.writeBytes(statusLine);
		//Sending contentTypeLine
		//os.writeBytes(contentTypeLine);
		os.writeBytes(responseLine);
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
				else if(fileName.endsWith("jpeg")||fileName.endsWith("jpg"))
					return "jpeg/jpg";
				else if(fileName.endsWith("pdf"))
					return "pdf";
				else
					return "application/octet-stream";

			}

}
