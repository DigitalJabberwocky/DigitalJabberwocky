import java.net.ServerSocket;
import java.io.IOException;

public class Server{

  private ServerSocket serv;

	public Server(int port){
		try {
		    serv = new ServerSocket(port);
		    System.out.println("Server Created!");
		} 
		catch (IOException e) {
		    System.out.println("Could not listen on port: " + port);
		    System.exit(-1);
		}
	}

	

	
	public void destroy(){
		try{
		   serv.close();
		   System.out.println("Server Closed!");
		}catch (IOException e){
		    System.out.println("Could not close server");
		    System.exit(-1);
		}
	}
	
}