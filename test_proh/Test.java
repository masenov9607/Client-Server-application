package test_proh;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test {
	public static void sendWord(String msg){
		
		Client cl = new Client(5555);
		
		cl.sendToServer(msg);
		String response = cl.receiveFromServer();
		System.out.println("Response: " + response + " id: " + Client.getReceivedPacketsCount());

		cl.stop();
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		String input = "An int, a char and a string walk into a bar and order some drinks." 
				+"A short while later, the int and char start hitting on the waitress who gets very uncomfortable and walks away."
				+ "The string walks up to the waitress and says "
				+ "You’ll have to forgive them, they’re primitive types.";
		
		ThreadPoolExecutor executor = 
				  (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

		String WhiteSpaceCommaDotMatcher = "\\s*(\\.|,|\\s)\\s*";
		String[] words = input.split(WhiteSpaceCommaDotMatcher);
		
		for(String w:words){
			
			executor.submit(() -> sendWord(w));
			
		}
			executor.shutdown();
			
			try {
				executor.awaitTermination(60, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("Expected received packets: " + words.length);
			System.out.println("Actual received packets: " + Client.getReceivedPacketsCount());
	}

}
