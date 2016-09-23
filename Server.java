import java.io.*;
import java.net.*;
import java.util.*;

public class Server{

	@SuppressWarnings("deprecation")
	public static void main(String args[]) throws Exception{
		//set the port number
		int port = 6789;
		//Establish the listen socket
		ServerSocket welcomeSocket = new ServerSocket(port);
		//Process Http request in an infinite loop
		while(true){
			//Listen for a TCP connection request
			Socket ClientSocket = welcomeSocket.accept();
			DataOutputStream ds = new DataOutputStream(ClientSocket.getOutputStream());
			ds.writeBytes("Something if you finally like to receive you stupid client");
			//Construct an Object to Process an http request
			HttpRequest request = new HttpRequest(ClientSocket);
			//Create new thread to process the request
			Thread thread = new Thread(request);
			//start the thread
			thread.start();
			thread.join();
			//thread.stop();

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
		os.writeBytes("Hii something frrom proxy server you may like");
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
	            	//	System.out.println("Again host vali string foind");
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
        BufferedReader inToProxyBf = new BufferedReader(new InputStreamReader(proxySocket.getInputStream()));
  	DataOutputStream  outFromProxyBf = new DataOutputStream(proxySocket.getOutputStream());
  	os.writeBytes("Hii this is proxy sending you some data");
  	System.out.println("Sending this Data to Server");
  	System.out.println("----------------------------");
  	System.out.println(new String(Byte.toString()));
  	//outFromProxy.writeBytes(requestLine);
  	outFromProxy.write(Byte);
  	outFromProxyBf.writeBytes("\r\n");
  	outToClient.writeBytes("Hii I am proxy sending you some bytes");
  	System.out.println("----------------------------");
  	System.out.println("Receiving this data from Server");
  	System.out.println("-------------------------------");
  	 int count=0;
 /* 	 String responseToClient = inToProxyBf.readLine();
  	String responseHeader = null;
	while((responseHeader = inFromClient.readLine()).length()!=0){
		responseToClient = responseToClient.concat(responseHeader)+"\r\n";
		//System.out.print(HeaderLien);
	}
	responseToClient = responseToClient.concat("\r\n");
	outToClient.writeBytes(responseToClient);
	outToClient.flush();
	outFromProxy.flush();
	outFromProxyBf.flush();*/
     while((count = inToProxy.read(Byte1)) != -1) {
    	System.out.println(new String(Byte1));
    	//inToProxy.wait();
        outToClient.write(Byte1,0,count);
        outToClient.flush();
    }
     System.out.println("All data has been successfully received.");
     inToProxy.close();
     outFromProxy.close();
     proxySocket.close();
     ClientSocket.close();
     count = 0;
 //    outToClient.write(Byte1);
  	//String newString = inToProxy.readLine();
  	//System.out.println(newString);
  	//outToClient.writeBytes(newString);
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

