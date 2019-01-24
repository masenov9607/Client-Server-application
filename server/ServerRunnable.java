package server;

public class ServerRunnable implements Runnable{
	static Server server =  new Server(5555);

	public static void main(String args[]) {
		
		(new Thread(new ServerRunnable())).start();
		
		while(true){	
			server.waitForNewClient();
		}


	}

	@Override
	public void run() {
		while(true) {
			server.processQueries();
		}

	}
}