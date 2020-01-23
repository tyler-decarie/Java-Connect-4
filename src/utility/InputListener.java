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

public class InputListener implements Runnable, PropertyChangeListener{

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
		this.number = number;
		observers.add(observer);
		
	}
	


	@Override
	public void run() {
		
		
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			ois.readObject();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}

	
	
}
