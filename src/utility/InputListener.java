package utility;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Tyler Decarie, Travis Brady
 * 
 * Class observed by the ClientGUI to respond to changes made over the network
 *
 */
public class InputListener implements Runnable{

	//Attributes
	private Socket socket;
	private ObjectInputStream ois;
	private int number;
	private List<PropertyChangeListener> observers = new ArrayList<PropertyChangeListener>();
	
	//Constructors
	//number 0 if this one
	public InputListener(Socket socket, PropertyChangeListener observer) {
		this.socket = socket;
		observers.add(observer);
		
	}
	
	//number not 0 if this one
	public InputListener(int number, Socket socket, PropertyChangeListener observer) {
		
		this.socket = socket;
		this.setNumber(number);
		observers.add(observer);
		
	}
	
	

	@Override
	public void run() {
		try {
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// runs infinitely, needs to accept every input
		while (true) {
		
		try {
			
			// waits here to read an object, then sends the object to the observer
			
			notify(ois.readObject());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Client Disconnected");
			break;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			break;
		}
		}
	}
	
	/**
	 * 
	 * @param object notifies the ClientGUI of what the change is
	 */
	public void notify(Object object) {
		
		for( PropertyChangeListener observer : observers  )
		{
			// even tho theres only one observer, the example works like this
			// this will need to work with both messages and game events
			observer.propertyChange(new PropertyChangeEvent(this, null, object, null));
		}
		
	}


	public int getNumber() {
		return number;
	}


	public void setNumber(int number) {
		this.number = number;
	}

}
