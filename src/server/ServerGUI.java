package server;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ServerGUI extends Application {

    Stage window;
    Scene serverScene;
    ServerObject so;

    @Override
    public void start(Stage primaryStage) throws Exception {

    	window = primaryStage;
    	
    	VBox screen = new VBox(10);
    	screen.setPadding(new Insets(10));
    	screen.setAlignment(Pos.CENTER);
    	
    	TextArea serverOutputTA = new TextArea();
    	serverOutputTA.setPrefSize(450, 450);
    	serverOutputTA.setEditable(false); //sets textarea so you cant type in it
		serverOutputTA.setWrapText(true);
			
		HBox buttons = new HBox(15);
		Button startBtn = new Button("Start");
		Button stopBtn = new Button("Stop");
		buttons.getChildren().addAll(startBtn, stopBtn);
		
		screen.getChildren().addAll(serverOutputTA, buttons);
    
		serverScene = new Scene(screen, 500, 500);
        window.setScene(serverScene);
        window.setTitle("Server");
        window.show();
		
		startBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				so = new ServerObject();
				new Thread(so).start();
				
			}
		});
		
    }
}