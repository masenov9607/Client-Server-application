package test_proh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

public class Client {
	int serverPort;
	Socket sock;
	static Semaphore mutex = new Semaphore(1);
	static int receivedPacketsCount = 0;
	Client(int port){
		try {
			sock = new Socket("localhost",port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendToServer(String msg){
		try {
			PrintWriter	outToServer = 
					new PrintWriter(sock.getOutputStream(),true);
			outToServer.println(msg);
			//outToServer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String receiveFromServer(){
		
		String modifiedSentence = "";
		try {
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));

			modifiedSentence = inFromServer.readLine();
			incrementReceivedPacketsCount();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return modifiedSentence;
	}
	public void stop(){
		try {
			sendToServer("END");
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int getReceivedPacketsCount(){
		int res = 0;
		try {
			mutex.acquire();
			res = receivedPacketsCount;
			mutex.release();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	private void incrementReceivedPacketsCount(){
		try {
			mutex.acquire();
			receivedPacketsCount++;
			mutex.release();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
