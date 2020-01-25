package server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import utility.InputListener;
import utility.Message;

public class ClientHandler implements Runnable, PropertyChangeListener {

	private Socket socket1;
	private Socket socket2;
	private ObjectOutputStream oos1;
	private ObjectOutputStream oos2;
	private InputListener lis1;
	private InputListener lis2;
	
	
	public ClientHandler(Socket socket1, Socket socket2) {
		
		this.socket1 = socket1;
		this.socket2 = socket2;
		
		try {
			
			oos1 = new ObjectOutputStream(this.socket1.getOutputStream());
			oos2 = new ObjectOutputStream(this.socket2.getOutputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Client Disconnected");
		}
		
		lis1 = new InputListener(1, socket1, this);
		lis2 = new InputListener(2, socket2, this);
		
	}

	@Override
	public void run() {
		
		new Thread(lis1).start();
		new Thread(lis2).start();
		
	}
	
	
	public void propertyChange(PropertyChangeEvent event) {
		
		InputListener il = (InputListener)event.getSource();
		System.out.println(il.getNumber());
		System.out.println(event.getOldValue());
		if(il.getNumber() == 1)
		{			
			// im thinking we check the source of the input will make it easy to decide where to send the object to
			// (1 to 2, 2 to 1)
			try {
				oos2.writeObject(event.getOldValue());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (il.getNumber() == 2)
		{
			// send input to other player
			try {
				oos1.writeObject(event.getOldValue());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
