package com.example.javf;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * The server computes BMI and sends back to the client a string that reports the BMI.
 *
 * Formula:
 * BMI = weight (kg) / height (meters)
 */
public class BMIServer extends Application {
    private int clientNo = 0;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        // Set pane
        TextArea ta = new TextArea();
        ta.setWrapText(true);
        ta.setEditable(false);
        ScrollPane pane = new ScrollPane(ta);

        // Set scene
        Scene scene = new Scene(pane);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Create new thread to run server
        new Thread(() -> {
            try {
                // Create server
                ServerSocket sc = new ServerSocket(8000);
                ta.appendText("MultiThreadServer started at " + new Date() + "\n");

                while (true) {
                    // Listen for socket connection
                    Socket socket = sc.accept();

                    // Get input stream data
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    double height = input.readDouble();
                    double weight = input.readDouble();

                    clientNo++;

                    // Run application thread to do stuff just cause
                    Platform.runLater(() -> {
                        ta.appendText("Starting thread for client " + clientNo + " at" + new Date() + "\n");

                        // Find the client's host name, and IP address
                        InetAddress inetAddress = socket.getInetAddress();
                        ta.appendText("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + "\n");
                        ta.appendText("Client " + clientNo + "'s IP address is " + inetAddress.getHostAddress() + "\n");

                        // Display input entered
                        ta.appendText("client " + clientNo + " is " + height + "metres " + weight + "kg" + "\n"
                                    + "BMI " + height / weight + "\n");
                    });
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}