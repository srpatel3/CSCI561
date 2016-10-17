import java.io.*;
import java.net.*;
import java.util.*;

public class Server{

	public static void main(String args[]) throws Exception{
		//set the port number
		int port = 6789;
		//Establish the listen socket
		System.out.println("Server is up and running on port: 6789");
		ServerSocket welcomeSocket = new ServerSocket(port);
		//Process Http request in an infinite loop
		while(true){
			//Listen for a TCP connection request
			Socket ClientSocket = welcomeSocket.accept();
			System.out.println("Connection with client established...");
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
		DataOutputStream  outToClient = new DataOutputStream(ClientSocket.getOutputStream());
		//Get the requestLine from client
		String requestLine1 = inFromClient.readLine()+"\r\n";
		//System.out.println();
		//System.out.println();
		String requestLine = "GET /index.html HTTP/1.1\r\n";
		//System.out.println(requestLine);
		//Get and display the header lines
		String HeaderLien = null;
		while((HeaderLien = inFromClient.readLine()).length()!=0){
			requestLine = requestLine.concat(HeaderLien)+"\r\n";
			//System.out.print(HeaderLien);
		}
		requestLine = requestLine+"\r\n";
		byte[] Byte = requestLine.getBytes();
		byte[] Byte1 = new byte[1024];
		int len = Byte.length;
		String hostName=null;
		System.out.println("request line is:");
		System.out.println(requestLine);
		String[] splitArray = requestLine.split("\r\n");
		for(int i=0;i<splitArray.length;i++){
			if(splitArray[i].contains("Host")){
				String[] finalHostName = splitArray[i].split(":");
	            for(int i1=0;i1<finalHostName.length;i1++){
	            	//System.out.println("Value of Split "+i +" is "+finalHostName[i]);
	            	if(finalHostName[i1].contains("Host")){
	            		System.out.println("Again host vali string foind");
	            		hostName = new String(finalHostName[i1+1]);
	            		break;
	            	}
	            	else{
	            		continue;
	            	}
	            }
			}
		}
		hostName = hostName.replaceAll("\\s+","");
		System.out.println("host name is:");
		System.out.println(hostName);
		Socket proxySocket = new Socket(hostName,80);
		OutputStream outFromProxy = proxySocket.getOutputStream();
    InputStream inToProxy = proxySocket.getInputStream();
    BufferedReader inToProxyBuf = new BufferedReader(new InputStreamReader(proxySocket.getInputStream()));
  	DataOutputStream  outFromProxyBuf = new DataOutputStream(proxySocket.getOutputStream());
  	System.out.println("Sending this Data to Server");
  	System.out.println("----------------------------");
  	System.out.println(requestLine);
  	//outFromProxy.writeBytes(requestLine);
  	outFromProxy.write(Byte);
  	System.out.println("----------------------------");
  	System.out.println("Receiving this data from Server");
  	System.out.println("-------------------------------");
		String HeaderLineResponse= null;
		do{
			HeaderLineResponse = HeaderLineResponse + inToProxyBuf.readLine();
		}
		while(!HeaderLineResponse.contains("\r\n\r\n"));
		System.out.println("Header line is:");
		System.out.println(HeaderLineResponse);
  	 int count=0;
     //while((count = inToProxy.read(Byte1)) != -1) {
    //	System.out.println(new String(Byte1));
    //    outToClient.write(Byte1);
   // }
     System.out.println("All data has been successfully received.");
     inToProxy.close();
     outFromProxy.close();
     proxySocket.close();
     count = 0;

 //    outToClient.write(Byte1);
  	//String newString = inToProxy.readLine();
  	//System.out.println(newString);
  	//outToClient.writeBytes(newString);
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
