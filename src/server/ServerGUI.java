package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;

public class ServerGUI extends Application {

    Stage window;

    @Override
    public void start(Stage arg0) throws Exception {

        ServerSocket server = null;
        Socket socket = null;

        ArrayList<Socket> sockets = new ArrayList<>(2);

        try {
            server = new ServerSocket(3333);
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("Server up and running!!!!");

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