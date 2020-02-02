package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ServerGUI extends Application {

    Stage window;
    Scene serverScene;
    static TextArea serverOutputTA;

    @Override
    public void start(Stage primaryStage) throws Exception {

    	ServerSocket server = null;
        Socket socket = null;
        
        ArrayList<Socket> sockets = new ArrayList<>(2);

        try {
            server = new ServerSocket(3333);
            System.out.println("Server up and running!!!!");
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        
        while(true) {
            try {
                socket = server.accept();
                System.out.println("Accepted a client connection.");
                sockets.add(socket);

                if(sockets.size() == 2) {
                    new Thread(new ClientHandler(sockets.get(0), sockets.get(1))).start();
                    sockets.clear();
                }

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        

    }
}
//}