package utility;

import java.io.ObjectInputStream;
import java.net.Socket;

public class InputListener implements Runnable{

	//attributes
	private Socket socket;
	private ObjectInputStream ois;
	private int number;
	
	
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
