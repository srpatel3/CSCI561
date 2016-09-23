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
			//System.out.println("Request from a client accepted now creating thread for it.");
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
		//System.out.println("Hii I am inside thread");
		Socket ServerSocket = null;
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
		InputStream fromClient = ClientSocket.getInputStream();
		OutputStream outToClient = ClientSocket.getOutputStream();
		byte[] Byte = new byte[1024];
		int length = fromClient.read(Byte);
		if(length>0){
			System.out.println("REQUEST"
                    + System.getProperty("line.separator") + "-------");
            System.out.println(new String(Byte, 0, length));
            String requestLine =new String(Byte);
            //StringTokenizer tokens = new StringTokenizer(requestLine);
            //tokens.nextToken();
            String hostName=null;
            String[] finalHostName = requestLine.split(":");
            for(int i=0;i<finalHostName.length;i++){
            	//System.out.println("Value of Split "+i +" is "+finalHostName[i]);
            	if(finalHostName[i].contains("Host")){
            		hostName = new String(finalHostName[i+1]);
            		break;
            	}
            	else{
            		continue;
            	}
            }
            if(!hostName.contains("www.")){
            	hostName = "www."+hostName;
            }
            String hostName1 = hostName.replaceAll("\\s+","");
            //Open and read file
            //System.out.println("host Name is:" +hostName1);
            ServerSocket = new Socket(hostName1,80);
            System.out.println("Connection with server established successfully.");
            OutputStream outFromServer = ServerSocket.getOutputStream();
            InputStream inFromServer = ServerSocket.getInputStream();
        	BufferedReader inFrom = new BufferedReader(new InputStreamReader(ServerSocket.getInputStream()));

            outFromServer.write(Byte, 0, length);
            System.out.println("Sending this data back to client:");
            int count=0;
             while((count = inFromServer.read(Byte)) != -1) {
            	System.out.println(new String(Byte, 0, length));
                outToClient.write(Byte, 0, length);
            }
            System.out.println(inFrom.readLine());
            outFromServer.flush();
            outFromServer.close();
            inFromServer.close();
		}
		fromClient.close();
		outToClient.flush();
		outToClient.close();
		ServerSocket.close();
		ClientSocket.close();
	}
}
