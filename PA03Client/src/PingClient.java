import java.awt.Toolkit;
import java.io.*;
import java.net.*;
import java.util.*;


public class PingClient extends TimerTask{
	//Gonna have to  make exact as asked: Dofa bhulto nai
	//public static Toolkit toolkit;
	//public static Timer timer;
	public static int counter=1;
	public static int MaxDelay = 1000;
	int portno;
	InetAddress Address1;
	public static Timer timer = new Timer(true);
	public static boolean flag=false;
	public PingClient(int port,InetAddress add){
		this.portno = port;
		this.Address1 = add;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		DatagramSocket clientSocket=null;
		if(counter<=10){
			try {
				clientSocket = new DatagramSocket();
			} catch (SocketException e2) {
				// TODO Auto-generated catch block
				System.out.println("Error while creating socket");
				e2.printStackTrace();
			}
			long sTime = new Date().getTime();
			String request = "PING " + counter + " " + sTime + " \n";
			System.out.println("Sending: "+request);
			byte[] byteArray = new byte[1024];
			byteArray = request.getBytes();
			DatagramPacket ping = new DatagramPacket(byteArray, byteArray.length, Address1, portno);
				try {
					clientSocket.send(ping);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					System.out.println("Error while sending ping\n");
					e1.printStackTrace();
				}
				try {
					//Client will wait for exact one second for packet to arrive or else it will 
					//raise and Exception
					clientSocket.setSoTimeout(MaxDelay);
					DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
					clientSocket.receive(response);
					long rTime = new Date().getTime();
					//printData(response, rTime - sTime);
				} catch (IOException e) {
					//System.out.println("Packet " + counter+" timed out");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("Something Jhand jevu");
					e.printStackTrace();
				}
				System.out.println("Counter Value is: "+counter);
				counter++;
		} else{
			//System.out.println("Inside else statement");
			//clientSocket.close();
			flag = true;
			
		}
		
		
	}	
	public static void main(String[] args) throws Exception{
		if (args.length != 2) {
			System.out.println("Required arguments: Server port");
			return;
		}
		int port = Integer.parseInt(args[1]);
		InetAddress Address;
		Address = InetAddress.getByName(args[0]);
		TimerTask myTimerTask = new PingClient(port,Address);
	    timer.scheduleAtFixedRate(myTimerTask, 0, 1*1000);
	    //while(flag!=true){
	//    System.out.println("1");	
	    //}
	    
	    Thread.sleep(12000);
	    System.out.println("Timer canceled");
	    timer.cancel();
	    timer.purge();
		
		//System.out.println("Host Name  is: "+Address +"And Client Details is: "+port);
	}
   private static void printData(DatagramPacket request, long RTT) throws Exception
   {
      byte[] buf = request.getData();
      ByteArrayInputStream bais = new ByteArrayInputStream(buf);
      InputStreamReader isr = new InputStreamReader(bais);
      BufferedReader br = new BufferedReader(isr);
      String line = br.readLine();
      System.out.println("Received from: " + request.getAddress().getHostAddress() + ": " + new String(line) + " Delay: " + RTT );
   }

}
