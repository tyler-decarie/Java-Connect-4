/**
 * Created on Jul 4, 2006
 *
 * Project: demo03_BasicEchoClientandServerExercises
 */
package server;

import java.io.*;
import java.net.*;
import java.util.*;


/**
 * @author dwatson
 * @version 1.0
 * 
 */
public class Server
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		ServerSocket server = null;
		Socket socket = null;
		
		ArrayList<Socket> sockets = new ArrayList<>(2);
		
		try
		{
			server = new ServerSocket(3333);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		
		System.out.println("Server gup and running!!!!");
		
		while(true)
		{
			try
			{
				socket = server.accept();
				System.out.println("Accepted a client connection.");
				sockets.add(socket);
				
				if(sockets.size() == 2) {
					new Thread(new ClientHandler(sockets.get(0), sockets.get(1))).start();
					sockets.clear();
				}
				
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
