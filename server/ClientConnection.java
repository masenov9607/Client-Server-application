package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection {
	 Socket sock;
	 BufferedReader reader;
	 PrintWriter writer;
	 
	 ClientConnection(Socket s){
		 sock = s; 
		 try {
			reader = new BufferedReader(
					 new InputStreamReader(sock.getInputStream()));
			 writer =  new PrintWriter(sock.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	 }
	public boolean isReadyToReceive(){
		 boolean isReady = false;
		 try {
			 isReady = reader.ready();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return isReady;
	 }
	void sendMSGToClient(String msg){
		if(!sock.isClosed()){
			writer.println(msg);
		}
	}
	 
	 public String readQuery(){
		 String q = "Query is not read";
		 try {
			q = reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return q;
	 }
	 
	 public void disconnect(){
		 try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }

}
