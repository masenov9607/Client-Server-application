package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;


public class Server {

	ServerSocket sock;
	ArrayList<ClientConnection> connectionList;
	static Semaphore accessClientConnections = new Semaphore(1);
	TaskManager manager = new TaskManager();

	Server(int port){
		connectionList = new ArrayList<ClientConnection>();
		try {
			sock = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void waitForNewClient(){
		System.out.println("Waiting for new connection");
		try {
			Socket	clientSocket = sock.accept();
			registerNewClient(clientSocket);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void processQueries(){
		long waitMiliseconds;
		try {

			accessClientConnections.acquire();
			ArrayList<ClientConnection> temp = new ArrayList<ClientConnection>(connectionList);
			accessClientConnections.release();

			for(ClientConnection cc: temp){
				if(cc.isReadyToReceive()){
					System.out.println("Submit new task");			
					manager.submitTask(()->{executeQuery(cc);});
				}
			}

			waitMiliseconds = temp.isEmpty() ? 500 : 1000;
			manager.waitAllPendingTasksToBeCompleted(waitMiliseconds);
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void executeQuery(ClientConnection cc){
		String q = cc.readQuery();

		//Check if client exiting
		if(q.equals("END"))
		{

			unregisterClient(cc);
		}
		else
		{
			cc.sendMSGToClient(q);
		}

	}

	void registerNewClient(Socket clientSocket){
		try {
			accessClientConnections.acquire();
			connectionList.add(new ClientConnection(clientSocket));
			accessClientConnections.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void unregisterClient(ClientConnection cc){
		//System.out.println("Unregister client");
		try{
			accessClientConnections.acquire();
			connectionList.remove(cc);
			accessClientConnections.release();
			cc.disconnect();
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
