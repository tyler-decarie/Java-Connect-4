package client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import utility.InputListener;
import utility.Message;
import javafx.scene.Scene;
import javafx.scene.control.TextField;


public class ClientGUI extends Application implements PropertyChangeListener{

	Stage window; //stage is the whole window
	Scene scene1, scene2; //scenes are the content within the window
	String ip;
	String username;
	Socket socket;
	ObjectOutputStream oos;
	InputListener lis;
	TextArea chatBoxTa;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		window = primaryStage;
		
		//scene1 stuff
		
		Label ipLbl = new Label("IP Address"); //makes new lbl for ip
		TextField ipTf = new TextField(); //makes new tf for ip
		Label nameLbl = new Label("Username"); //makes new lbl for name
		TextField nameTf = new TextField(); //makes new tf for name
		Button button1 = new Button("Connect"); //makes new button
		
		button1.setPrefSize(85, 35); //sets button size
		Font scene1Font = new Font("Veranda", 18);
		ipLbl.setFont(scene1Font); 
		nameLbl.setFont(scene1Font);
		
		//layout of tf's, lbl's and button
		GridPane grid = new GridPane();
		grid.setVgap(8); //sets vertical gap of items in grid
		grid.setHgap(10);  //sets horizontal gap of items in grid
		grid.addRow(0, ipLbl, ipTf);
		grid.addRow(1, nameLbl, nameTf);
		grid.addRow(2, button1);
		grid.setAlignment(Pos.CENTER);
		
		Label failLbl = new Label("");
		grid.addRow(3, failLbl);
		
		//event handler for connect button click
		button1.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				
				//gets text input from textfields
				ip = ipTf.getText();
				username = nameTf.getText();
				
				if(ip.length() != 0 && username.length() != 0) {
					
					connect();
					
					
					
				} 
			}
		}); 	
		
		

		scene1 = new Scene(grid, 400, 400); //adds everything to the scene
		
		//end of scene1 stuff
		
		//start of scene 2 shit
		
		GridPane screen = new GridPane(); //creates new gridpane of the whole scene
		screen.setStyle("-fx-border-color: black"); //styles border black
		screen.setPadding(new Insets(20)); //gives padding of 20 on each side
		screen.setVgap(20); //sets vertical gap of 20 between items in the grid
		screen.setHgap(24); //sets horizontal gap of 24 between items in grid
		
		BorderPane playArea = new BorderPane();  //creates new borderpane for the playArea(top left)
		playArea.setPadding(new Insets(12)); //gives padding of 12 on each side
		playArea.setStyle("-fx-background-color: #ddd; -fx-border-color: #000000");
		playArea.setPrefSize(700, 400); //sets the size of the pane
		screen.add(playArea, 0, 0); //adds the pane to the grid in column 0, row 0
		
		VBox chatArea = new VBox(20); //creates vertical box layout with a gap of 20 between items
		chatBoxTa = new TextArea(); //creates text area for chat to display in
		chatBoxTa.setMinSize(252, 360); //sets the size of the text area
		chatBoxTa.setEditable(false); //sets textarea so you cant type in it
		TextField msgTf = new TextField(); //creates textfield to type message to be sent
		msgTf.setMinWidth(300);  //sets width of textfield
		Button msgButton = new Button("Send"); //creates button to send message
		HBox msgArea = new HBox(12); //creates horizontal box layout with gap of 12 between items
		
		//adds the textfield and button to the horizontal box layout
		msgArea.getChildren().addAll(msgTf, msgButton);
		
		//adds the textarea and horizontal box layout into the vertical box layout
		chatArea.getChildren().addAll(chatBoxTa, msgArea);
		screen.add(chatArea, 1, 0); //adds the parents vertical box layout to the gridpane in column 1, row 0
		
		//event handler to display messages in textarea when the send button is clicked
		//msgButton.setOnAction(e -> chatBoxTa.appendText(msgTf.getText() + "\n"));
		msgButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				
				Message message = new Message(username, msgTf.getText(), new Date());
				try {
					oos.writeObject(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				chatBoxTa.appendText(message.toString());
				msgTf.clear();
				
			}
			
		});
		
		
		scene2 = new Scene(screen, 1000, 600);
		
		window.setScene(scene1); //sets default scene to scene1 on launch
		window.setTitle("Login"); //sets title of the window
		window.show(); //shows everything on the window
	}

	
	public void connect() {
		try {
			
			socket = new Socket(ip, 3333);
			OutputStream os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);
			
			lis = new InputListener(socket, this);
			
			new Thread(lis).start();
			
		} catch (UnknownHostException e) {
			
			//failLbl.setText("Failed to connect");
			System.out.println("Failed to Connect");
			
		} catch (IOException e) {
			
			//failLbl.setText("Failed to connect");
			System.out.println("Failed to Connect");
			
		}
		
		
		window.setScene(scene2);
	}
	
	@Override
	// inputListener will send the object to the gui through here,
	// needs to work with messages and game events 
	public void propertyChange(PropertyChangeEvent event) {
				
		Message message = (Message) event.getOldValue();
		chatBoxTa.appendText(message.toString());
		
	}
	
}
