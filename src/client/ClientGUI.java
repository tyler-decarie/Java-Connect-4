package client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utility.InputListener;
import utility.Message;
import utility.Piece;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

/**
 * This class handles creation of the login page, and play area of the game client.
 * The login page is used to connect to the server and inputs from the play area or chat are sent to the server to be sent to another player.
 * @author Tyler Decarie, Travis Brady
 * 
 */
public class ClientGUI extends Application implements PropertyChangeListener {

	Stage window; // stage is the whole window
	Scene loginScene, gameScene; // scenes are the content within the window
	String ip;
	String username;
	Socket socket;
	ObjectOutputStream oos;
	InputListener lis;
	TextArea chatBoxTa;
	GridPane pieces;
	BorderPane playArea;
	Label failLbl;
	VBox reinstateBox;
	Label winnerLbl;
	boolean opponentConnected;
	boolean loggedIn;

	private static final int ROWS = 6;
	private static final int COLUMNS = 7;

	Rectangle[] rectArray = new Rectangle[COLUMNS];
	Piece[][] pieceArray = new Piece[COLUMNS][ROWS]; // [rows][columns]

	boolean redsTurn = true;

	
	/**Starts the ClientGUI Application and loads the screens and game 
	 * @param primaryStage 
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		window = primaryStage;

		createLoginWindow();
		createGameWindow();

		window.setScene(loginScene); // sets default scene to scene1 on launch
		window.show(); // shows everything on the window
	}

	/**
	 *  This method handles the creation of the login window.
	 *  When login credentials are entered and the button is pressed
	 *  the connect() method is called
	 */
	public void createLoginWindow() {

		Label ipLbl = new Label("IP Address"); // makes new lbl for ip
		TextField ipTf = new TextField(); // makes new tf for ip
		Label nameLbl = new Label("Username"); // makes new lbl for name
		TextField nameTf = new TextField(); // makes new tf for name
		Button button1 = new Button("Connect"); // makes new button

		button1.setPrefSize(85, 35); // sets button size
		Font scene1Font = new Font("Veranda", 18);
		ipLbl.setFont(scene1Font);
		nameLbl.setFont(scene1Font);

		// layout of tf's, lbl's and button
		GridPane grid = new GridPane();
		grid.setVgap(8); // sets vertical gap of items in grid
		grid.setHgap(10); // sets horizontal gap of items in grid
		grid.addRow(0, ipLbl, ipTf);
		grid.addRow(1, nameLbl, nameTf);
		grid.addRow(2, button1);
		grid.setAlignment(Pos.CENTER);

		failLbl = new Label("Failed to connect");
		failLbl.setVisible(false);
		grid.addRow(3, failLbl);

		// event handler for connect button click
		button1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				// gets text input from textfields
				ip = ipTf.getText();
				username = nameTf.getText();

				if (ip.length() != 0 && username.length() != 0) {

					connect();

				}
			}
		});
		window.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				window.close();
				System.exit(0);
			}
		});
		window.setTitle("Connect 4 - Login"); // sets title of the window
		loginScene = new Scene(grid, 400, 400); // adds everything to the scene

	}

	
	/**
	 * This method creates the game window with the chat area and the play area
	 */
	public void createGameWindow() {

		GridPane screen = new GridPane(); // creates new gridpane of the whole scene
		screen.setStyle("-fx-border-color: black"); // styles border black
		screen.setPadding(new Insets(20)); // gives padding of 20 on each side
		screen.setVgap(20); // sets vertical gap of 20 between items in the grid
		screen.setHgap(90); // sets horizontal gap of 24 between items in grid

		playArea = new BorderPane(); // creates new borderpane for the playArea(top left)
		playArea.setPadding(new Insets(12)); // gives padding of 12 on each side
		playArea.setStyle("-fx-background-color: #00B2EE; -fx-border-color: #000000");
		playArea.setPrefSize(740, 480); // sets the size of the pane (width, height)
		playArea.getChildren().addAll(createPlayArea()); // creates grid for game pieces
		playArea.getChildren().addAll(createColumnOverlay());

		screen.add(playArea, 0, 0); // adds the pane to the grid in column 0, row 0

		VBox chatArea = new VBox(20); // creates vertical box layout with a gap of 20 between items

		chatBoxTa = new TextArea(); // creates text area for chat to display in
		chatBoxTa.setMinSize(310, 330); // sets the size of the text area
		chatBoxTa.setEditable(false); // sets textarea so you cant type in it
		chatBoxTa.setWrapText(true);

		HBox msgArea = new HBox(12); // creates horizontal box layout with gap of 12 between items
		TextField msgTf = new TextField(); // creates textfield to type message to be sent
		msgTf.setMinWidth(255); // sets width of textfield
		Button msgButton = new Button("Send"); // creates button to send message
		// adds the textfield and button to the horizontal box layout
		msgArea.getChildren().addAll(msgTf, msgButton);

		// adds the textarea and horizontal box layout into the vertical box layout
		chatArea.getChildren().addAll(chatBoxTa, msgArea);
		
		reinstateBox = new VBox(10);
		HBox buttonBox = new HBox(10);
		winnerLbl = new Label("");
		Label reconnectLbl = new Label("Would you like to play again?");
		Button yesBtn = new Button("Yes");
		Button noBtn = new Button("No");
		
		winnerLbl.setFont(new Font("Arial", 16));
		reconnectLbl.setFont(new Font("Arial", 16));
		
		buttonBox.getChildren().addAll(yesBtn, noBtn);
		reinstateBox.getChildren().addAll(winnerLbl, reconnectLbl, buttonBox);
		
		reinstateBox.setVisible(false);
		
		chatArea.getChildren().add(reinstateBox);

		screen.add(chatArea, 1, 0); // adds the parents vertical box layout to the gridpane in column 1, row 0

		// event handler to display messages in textarea when the send button is clicked
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
		
		yesBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) { 
            	// resets game if button is clicked, if opponent has quit add this player to matchmaking
                resetGameArea(pieceArray);
                reinstateBox.setVisible(false);
                if (!opponentConnected) {
    				try {
    					socket.close();
    					oos.close();
    					
        				connect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        });
        noBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) { // closes game if button is clicked
            	try {
					oos.writeObject("OPPONENT-HAS-DROPPED");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                window.close();
                System.exit(0);
            }
        });

		window.setTitle("Connect 4"); // sets title of the window
		gameScene = new Scene(screen, 1000, 540);
		window.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				if (loggedIn) {
					try {
					oos.writeObject("OPPONENT-HAS-DROPPED");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				window.close();
				System.exit(0);
			}
		});
	}

	/**
	 * @return the GridPane returned contains the grid for the play area. Every slot is a Piece object
	 * a grid containing Piece objects for the slots in the play area
	 */
	public GridPane createPlayArea() {

		// creates grid for pieces
		pieces = new GridPane();
		pieces.setPadding(new Insets(10));
		pieces.setHgap(10);
		pieces.setVgap(10);

		for (int y = 0; y < ROWS; y++) {

			for (int x = 0; x < COLUMNS; x++) {
				Piece gamePiece = new Piece(x, y);
				gamePiece.setFill(Color.LIGHTGRAY);
				gamePiece.setRadius(34);
				pieceArray[x][y] = gamePiece;
				pieces.add(gamePiece, x, y);

			}
		}

		return pieces;

	}

	/**
	 * Creates a overlay of rectangles so the user knows where theyre going to place
	 * a game piece
	 * 
	 * @return GridPane layout with seven rectangles to the playArea
	 */
	public GridPane createColumnOverlay() {

		GridPane columns = new GridPane();
		columns.setPadding(new Insets(0, 10, 0, 10));
		columns.setHgap(10);

		// creates 7 rectangles for the 7 columns
		for (int i = 0; i < COLUMNS; i++) {

			Rectangle rect = new Rectangle(68, 480);
			rect.setFill(Color.TRANSPARENT);

			// when hovering mouse over a rectangle it will change its color
			rect.setOnMouseEntered(e -> rect.setFill(Color.rgb(200, 200, 50, 0.3)));
			// when mouse isnt hovering it will make it transparent again
			rect.setOnMouseExited(e -> rect.setFill(Color.TRANSPARENT));

			rectArray[i] = rect;
			columns.add(rect, i, 0);
			int column = i;
			rect.setOnMouseClicked(e -> placeGamePiece(column, pieceArray));
		}

		return columns;

	}

	
	/**
	 * when a column is clicked this method is called.
	 * this method checks the column that was clicked and places the Piece in the lowest unfilled position
	 * @param column the column that was clicked for placing a Piece
	 * @param pieceArray the array of pieces in the game grid
	 */
	public void placeGamePiece(int column, Piece[][] pieceArray) {

		Piece checkPiece = pieceArray[column][0];

		if (!(checkPiece.getFill() == Color.LIGHTGRAY)) {

			// Column filled if I did this right

		} else {

			for (int y = 5; y >= 0; y--) {

				checkPiece = pieceArray[column][y];

				if (!(checkPiece.getFill() == Color.LIGHTGRAY)) {
					// theres a piece in this spot
				} else {

					checkPiece = pieceArray[column][y];
					break;
				}

			}

			checkPiece.setFill(Color.RED);
			playArea.setDisable(true);
			try {
				oos.writeObject(checkPiece);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			checkWin(checkPiece);

		}

	}

	
	/**
	 * this method calls a series of check methods for win conditions.
	 * if any of the methods returns true the player was won, the play area will be disabled and prompt to play again or exit the game
	 * 
	 * @param checkPiece the piece that was just placed in the play area
	 */
	private void checkWin(Piece checkPiece) {
		System.out.println(checkPiece.getColumn() + "," + checkPiece.getRow());

		boolean win = false;

		if (checkForwardDiagonal(checkPiece)) {
			System.out.println("you win");
			win = true;
		} else if (checkHorizontal(checkPiece)) {
			System.out.println("you win");
			win = true;
		} else if (checkDown(checkPiece)) {
			System.out.println("you win");
			win = true;
		} else if (checkBackDiagonal(checkPiece)) {
			System.out.println("you win");
			win = true;
		}

		if (win) {
			try {
				winnerLbl.setText("You win!");
				oos.writeObject("CONDITION-YOU-LOSE");
				playArea.setDisable(true);
				reinstateBox.setVisible(true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	
	/**
	 * checks the game board for a win condition in a backward diagonal direction like a "\"
	 * @param currentPiece the Piece that was placed by the player
	 * @return returns true if the win condition has been met, otherwise false
	 * @throws ArrayIndexOutOfBoundsException 
	 */
	private boolean checkBackDiagonal(Piece currentPiece) throws ArrayIndexOutOfBoundsException {

		int counter = 0;

		int x = currentPiece.getColumn();
		int y = currentPiece.getRow();

		for (int i = -3; i < 4; i++) {

			if (((x + i) < 0) || ((y + i) < 0)) {
				i++;
				continue;
			} else if (((x + i) > (COLUMNS - 1)) || ((y + i) > (ROWS - 1))) {
				break;
			}

			Piece nextPiece = pieceArray[x + i][y + i];

			if (!(nextPiece.getFill() == Color.RED)) {

				counter = 0;

			} else {

				counter++;

			}

			if (counter == 4) {
				return true;
			}

		}

		return false;

	}

	/**
	 * checks the game board for a win condition in a forward diagonal direction like a "/"
	 * @param currentPiece Piece placed
	 * @return true if condition met, false if not
	 * @throws ArrayIndexOutOfBoundsException
	 */
	private boolean checkForwardDiagonal(Piece currentPiece) throws ArrayIndexOutOfBoundsException {

		int counter = 0;
		ArrayList<Piece> placedPiece = new ArrayList<>();
		int x = currentPiece.getColumn();
		int y = currentPiece.getRow();
		// placedPiece.add(currentPiece);
		for (int i = -3; i < 4; i++) {

			if (((x + i) > (COLUMNS - 1)) || ((y - i) < 0)) {

				return false;

			}
			if (!(((x + i) < 0) || ((y - i) > ROWS - 1))) {

				Piece nextPiece = pieceArray[x + i][y - i];

				if (!(nextPiece.getFill() == Color.RED)) {

					counter = 0;

				} else {

					counter++;
					placedPiece.add(nextPiece);

				}
				System.out.println("counter is at: " + counter);
				if (counter == 4) {

					return true;

				}

			}

		}
		for (Piece p : placedPiece) {

			System.out.println("placed pieces: " + p.toString());
		}
		return false;

	}

	/**
	 * checks the 3 Pieces below the placed Piece for 4 in a row
	 * @param currentPiece the Piece that was placed
	 * @return true if condition is met, false otherwise
	 * @throws ArrayIndexOutOfBoundsException
	 */
	private boolean checkDown(Piece currentPiece) throws ArrayIndexOutOfBoundsException {

		int counter = 0;

		int x = currentPiece.getColumn();
		int y = currentPiece.getRow();

		for (int i = 0; i < 4; i++) {

			if (y > ROWS - 1) {
				return false;
			}

			Piece nextPiece = pieceArray[x][y];

			if (!(nextPiece.getFill() == Color.RED)) {

				return false;
			} else {

				counter++;
				y++;

			}

			System.out.println("Counter" + counter);
			if (counter == 4) {

				return true;
			}

		}

		return false;
	}

	/**
	 * checks the game board for a win in the horizontal axis 
	 * @param currentPiece the Piece placed
	 * @return true if condition is met, false otherwise
	 * @throws ArrayIndexOutOfBoundsException
	 */
	private boolean checkHorizontal(Piece currentPiece) throws ArrayIndexOutOfBoundsException {

		int counter = 0;

		int x = currentPiece.getColumn();
		int y = currentPiece.getRow();

		for (int i = -3; i < 4; i++) {

			if ((x + i) < 0) {
				
				continue;
			} else if ((x + i) > (COLUMNS - 1)) {
				break;
			}

			Piece nextPiece = pieceArray[x + i][y];

			if (!(nextPiece.getFill() == Color.RED)) {

				counter = 0;

			} else {

				counter++;

			}

			if (counter == 4) {
				return true;
			}

		}

		return false;

	}

	/**
	 * once the IP and username have been entered on the login page, this method attemps 
	 */
	public void connect() {
		try {

			socket = new Socket(ip, 3333);
			OutputStream os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);

			lis = new InputListener(socket, this);

			new Thread(lis).start();

			window.setScene(gameScene);
			playArea.setDisable(true);
			loggedIn = true;
		} catch (UnknownHostException e) {

			failLbl.setVisible(true);
			System.out.println("Failed to Connect");

		} catch (IOException e) {

			failLbl.setVisible(true);
			System.out.println("Failed to Connect");

		}

	}

	/**
	 * reads objects from the server and opponent 
	 * Messages are appended to the chat box, Piece objects are placed on the game board,
	 * YOU-GO-FIRST Strings make the play area active, OPPONENT-HAS-DROPPED will auto win the game for the player and prompt for reconnection
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {

		String toString = event.getOldValue().toString();
		System.out.println(toString);
		if (toString.contains("Message")) {
			Message message = (Message) event.getOldValue();
			chatBoxTa.appendText(message.toString());
		} else if (toString.contains("Piece")) {
			Piece piece = (Piece) event.getOldValue();
			piece = pieceArray[piece.getColumn()][piece.getRow()];
			piece.setFill(Color.YELLOW);
			playArea.setDisable(false);
		} else if (toString.contains("YOU-GO-FIRST")) {

			playArea.setDisable(false);
		} else if (toString.contains("CONDITION-YOU-LOSE")) {
			setWinLblText();
			playArea.setDisable(true);
			reinstateBox.setVisible(true);
			// Add message and buttons for staying or leaving
		} else if (toString.contains("OPPONENT-HAS-DROPPED")) {
			winnerLbl.setText("Oppoenent disconnected");
			reinstateBox.setVisible(true);
			opponentConnected = false;
		}

	}
	
	/**
	 * Generally at the end of a game this method is called, resets all Pieces in the grid to its default state
	 * @param pieceArray the entire array of game Pieces
	 */
	private void resetGameArea(Piece[][] pieceArray) {

        for (int y = 0; y < ROWS; y++) {

            for (int x = 0; x < COLUMNS; x++) {
                Piece gamePiece = pieceArray[x][y];
                gamePiece.setFill(Color.LIGHTGRAY);
                pieceArray[x][y] = gamePiece;

            }
        }
    }
	
	/**
	 * Sets the win label on the GUI to "You lose!"
	 */
	private void setWinLblText() {
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				winnerLbl.setText("You lose!");
				
			}
		}); 
	}

}
