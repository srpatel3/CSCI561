import java.io.*;
import java.net.*;

class Server {

  public static void main(String argv[]) throws Exception
    {
	  
      ServerSocket welcomeSocket = new ServerSocket(6789);
      System.out.println("Server Started: Now Waiting for Clients...");
      while(true){
      Socket connectionSocket = welcomeSocket.accept();
      System.out.println("Connection With Client established...");
      InputStream in = connectionSocket.getInputStream();
      BufferedReader inFromClient = 
              new BufferedReader(new
              InputStreamReader(connectionSocket.getInputStream()));
      String fileName = inFromClient.readLine();
      System.out.println("received from client: "+ fileName);
      File fout = new File("new_"+fileName);
      //FileOutputStream fos = new FileOutputStream(fout);
      OutputStream out = new FileOutputStream(fout);
      System.out.println("file with name: new_"+fileName +" created.");
      //out.close();
      byte[] bytes = new byte[4];
      int count;
      System.out.println("Now Writing data to: new_"+fileName);
      while ((count = in.read(bytes)) > 0) {
        out.write(bytes, 0, count);
      }
      connectionSocket.close();
      out.close();
      in.close();
      System.out.println("File transfer Complete now Waiting for other Clients...");
      }
//      welcomeSocket.close();
    }
}
