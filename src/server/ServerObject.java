package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 *  
 * ServerObject class is responsible for making connections between two players
 * @author Tyler Decarie, Travis Brady
 *
 */
public class ServerObject implements Runnable{
	
	/** Runs the server as a thread so it can run concurrently with the ServerGUI
	 *
	 */
	@Override
	public void run() {
		int counter = 1;
		ServerSocket server = null;
	    Socket socket = null;
	        
	    ArrayList<Socket> sockets = new ArrayList<>(2);

	    try {
	        server = new ServerSocket(3333);
	        System.out.println("Server up and running!!!!");
	        ServerGUI.writeToTextArea("Server is up and running!");
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }
   
	    while(true) {
	        try {
	            socket = server.accept();
	            ServerGUI.writeToTextArea("\nAccepted a client connection.");
	            sockets.add(socket);

	            if(sockets.size() == 2) {
	                new Thread(new ClientHandler(sockets.get(0), sockets.get(1))).start();
	                ServerGUI.writeToTextArea("\nClients Paired. Game " + counter++ + " started.");
	                sockets.clear();
	            }

	        } catch (IOException e) {
	               
	        }
	   }
	}
}	
	

