public class Program{
	public static void main(String [] args){
		Server serv;
		serv = new Server(4444);
		serv.start();
		
		while(serv.connected() == false){
		}
		serv.PutString("Sup");
		System.out.println("Sup");
		serv.PutString("Hello");
		System.out.println("Hello");
		//serv.destroy();
	}
}
