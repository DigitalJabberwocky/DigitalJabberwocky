import java.net.*;
import java.io.*;

public class Server extends Thread{

	private ServerSocket serv = null;
	private String PutInfo = null;
	private int myPort;
	private boolean isConnected = false;
	
	
	public Server(int port){
		myPort = port;
	}
	
	public void run(){
		try {
			serv = new ServerSocket(myPort);
			System.out.println("Server Created!");			
			Socket accept = serv.accept();
			isConnected = true;
		   	BufferedReader br = new BufferedReader(new InputStreamReader(accept.getInputStream()));
			String temp;
			while((temp = br.readLine()) != null){
				System.out.println(temp);
				if(PutInfo != null) {
					PrintStream ps = new PrintStream(accept.getOutputStream());
					ps.println(PutInfo);
					ps.flush();
					PutInfo = null;
				}
				/* DEBUG */
				if(PutInfo == null)
					System.out.println("HERE");
			}
		} 
		catch (IOException e) {
		    System.out.println("Could not listen on port: " + myPort);
		    System.exit(-1);
		}
			

	}

	public void PutString(String s){
		//while(PutInfo != null){ }		
		PutInfo = s;
	}
	
	public boolean connected(){
		return isConnected;
	}
	

	
/*	public void destroy(){
		try{
		   serv.close();
		   System.out.println("Server Closed!");
		   Thread.yield();
		}catch (IOException e){
		    System.out.println("Could not close server");
		    System.exit(-1);
		}
	}
*/	
}
