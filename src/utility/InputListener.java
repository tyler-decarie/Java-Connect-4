package utility;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import client.ClientGUI;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class InputListener implements Runnable{

	//attributes
	private Socket socket;
	private ObjectInputStream ois;
	private int number = 0;
	private List<PropertyChangeListener> observers = new ArrayList<PropertyChangeListener>();
	
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
		// runs infinitely, needs to accept every input
		while (true) {
		
		try {
			ois = new ObjectInputStream(socket.getInputStream());
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
	
	//
	public void notify(Object object) {
		
		for( PropertyChangeListener observer : observers  )
		{
			// even tho theres only one observer, the example works like this
			// this will need to work with both messages and game events
			observer.propertyChange(new PropertyChangeEvent(this, null, object, object));
		}
		
	}


	public int getNumber() {
		return number;
	}


	public void setNumber(int number) {
		this.number = number;
	}

}
