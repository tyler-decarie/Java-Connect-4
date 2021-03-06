package server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

import utility.InputListener;

/**
 * 
 * ClientHandler pairs two players and sets up InputListeners to recieve and send objects over the server.
 * ClientHandler is observing the two InputListeners for property changes.
 * 
 * @author Tyler Decarie, Travis Brady
 *
 */
public class ClientHandler implements Runnable, PropertyChangeListener {

	private Socket socket1;
	private Socket socket2;
	private ObjectOutputStream oos1;
	private ObjectOutputStream oos2;
	private InputListener lis1;
	private InputListener lis2;
	
	
	/**
	 * Constructor decides which player will go first in the game and creates two inputlisteners 
	 * @param socket1  socket for the first player
	 * @param socket2 socket for the second player
	 * 
	 */
	public ClientHandler(Socket socket1, Socket socket2) {
		Random rand = new Random();
		int chooseFirst = rand.nextInt(1);
		System.out.println(chooseFirst);
		this.socket1 = socket1;
		this.socket2 = socket2;
		
		try {
			
			oos1 = new ObjectOutputStream(this.socket1.getOutputStream());
			oos2 = new ObjectOutputStream(this.socket2.getOutputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			ServerGUI.writeToTextArea("\nClient Disconnected");
		}
		
		lis1 = new InputListener(1, socket1, this);
		lis2 = new InputListener(2, socket2, this);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (chooseFirst == 0) {
			try {
				oos2.writeObject("YOU-GO-FIRST");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (chooseFirst == 1) {
			try {
				oos1.writeObject("YOU-GO-FIRST");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	/**
	 *	Instantiates InputListeners to recieve objects from the players
	 */
	@Override
	public void run() {
		
		new Thread(lis1).start();
		new Thread(lis2).start();
		
	}
	
	
	/**
	 * Sends objects from one player to another
	 */
	public synchronized void propertyChange(PropertyChangeEvent event) {
		
		InputListener il = (InputListener)event.getSource();
//		System.out.println(il.getNumber());
		System.out.println(event.getOldValue());
		if(il.getNumber() == 1)
		{			
			// im thinking we check the source of the input will make it easy to decide where to send the object to
			// (1 to 2, 2 to 1)
			try {
				oos2.writeObject(event.getOldValue());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
			}
		}
		else if (il.getNumber() == 2)
		{
			// send input to other player
			try {
				oos1.writeObject(event.getOldValue());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
			}
		}
		
	}

}
