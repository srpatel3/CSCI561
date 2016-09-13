import java.io.*;
import java.util.*;
import java.net.*;
class Client {

    public static void main(String argv[]) throws Exception
    {
    	System.out.println("Client Program Started: Trying to Connect to Server on Port: 6789");
    	Socket clientSocket = new Socket("localhost", 6789);
    	System.out.println("Connection with Server established...");
    	String fileName;
    	byte[] bytes = new byte[4];
    	BufferedReader inFromUser = 
             new BufferedReader(new InputStreamReader(System.in));
    	DataOutputStream outToServer = 
             new DataOutputStream(clientSocket.getOutputStream());
    	OutputStream outbyByte = clientSocket.getOutputStream();
    	System.out.println("Enter File Name:");
    	fileName = inFromUser.readLine();
    	File file = new File(fileName);
    	InputStream in = new FileInputStream(file);
    	int count;
    	outToServer.writeBytes(fileName + "\n");
    	System.out.println("Sending file name to server: "+fileName);
    	outToServer.flush();
    	//outToServer.close();
    	System.out.println("Sending data to server...");
    	while ((count = in.read(bytes)) > 0) {
    		//System.out.println("Sending " + count);
    		//String toServer = new String(bytes);
    		//System.out.println("Sending "+toServer);
    		//outToServer.writeBytes(toServer);
    		outbyByte.write(bytes, 0, count);
    	}
    	System.out.println("File Transfer Completed...");
    	//outToServer.writeBytes("exit");
    	outToServer.close();
    	outbyByte.close();
    	in.close();
    	clientSocket.close();
    }
}
