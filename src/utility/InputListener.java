package utility;

import java.beans.PropertyChangeListener;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

public class InputListener implements Runnable{

	//attributes
	private Socket socket;
	private ObjectInputStream ois;
	private int number = 0;;
	private List<PropertyChangeListener> listeners;
	
	//number 0 if this one
	public InputListener(Socket socket) {
		
		this.socket = socket;
		
	}
	
	
	//number not 0 if this one
	public InputListener(int number, Socket socket) {
		
		this.socket = socket;
		this.number = number;
		
	}
	
	
	@Override
	public void run() {
		
		
		
	}

	
	
}
