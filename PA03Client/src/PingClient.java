import java.io.*;
import java.net.*;
import java.util.*;

/*
 * Client to generate a ping requests over UDP.
 * Code has started in the PingServer.java
 */

public class PingClient{
	//Gonna have to  make exact as asked: Dofa bhulto nai
	private static final int MAX_TIMEOUT = 1000;
	public static void main(String[] args) throws Exception{
		if (args.length != 2) {
			System.out.println("Required arguments: Server port");
			return;
		}
		int port = Integer.parseInt(args[1]);
		InetAddress Address;
		Address = InetAddress.getByName(args[0]);
		System.out.println("Host Name  is: "+Address +"And Client Details is: "+port);
		DatagramSocket Clientsocket = new DatagramSocket();
		int counter = 0;
		// Processing loop.
		while (counter < 10) {
			Thread.sleep(MAX_TIMEOUT);
			long msSend = new Date().getTime();
			String request = "PING " + counter + " " + msSend + " \n";
			System.out.println("Sending: "+request);
			byte[] byteArray = new byte[1024];
			byteArray = request.getBytes();
			DatagramPacket ping = new DatagramPacket(byteArray, byteArray.length, Address, port);
			Clientsocket.send(ping);
			try {
				Clientsocket.setSoTimeout(MAX_TIMEOUT);
				DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
				Clientsocket.receive(response);
				long msReceived = new Date().getTime();
				//printData(response, msReceived - msSend);
			} catch (IOException e) {
				//System.out.println("Packet " + counter+" timed out");
			}
			counter++;
		}
	}
   private static void printData(DatagramPacket request, long delayTime) throws Exception
   {
      byte[] buf = request.getData();
      ByteArrayInputStream bais = new ByteArrayInputStream(buf);
      InputStreamReader isr = new InputStreamReader(bais);
      BufferedReader br = new BufferedReader(isr);
      String line = br.readLine();
      System.out.println("Received from " + request.getAddress().getHostAddress() + ": " + new String(line) + " Delay: " + delayTime );
   }
}
