import java.net.*;
import java.io.*;

public class Client{
	
	Socket clientSocket = null;

	public Client(String host, int port){
	
		try {
	    		clientSocket = new Socket(host, port);
			PrintStream ps = new PrintStream(clientSocket.getOutputStream());			
			ps.println("Hello server");
			BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String temp;
			while((temp = br.readLine()) != null){
				System.out.println(temp);
			}
		} 
		catch (IOException e) {
	    		System.out.println("Accept failed: " +port);
	    		System.exit(-1);
		}

	}


	public void destroy(){
		try{
		   clientSocket.close();
		   System.out.println("Client Closed!");
		}
		catch (IOException e){
		    System.out.println("Could not close Client");
		    System.exit(-1);
		}
	}
}
