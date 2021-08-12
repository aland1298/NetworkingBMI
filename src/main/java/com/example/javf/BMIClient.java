package com.example.javf;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * Client sends the weight and height for a person to the server.
 */
public class BMIClient extends Application {
    /**
     * Main Method for Client
     * @param args - rawer
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Nodes
        Button bt = new Button("Submit");
        bt.setDefaultButton(true);

        // Fields
        TextField tf = new TextField();
        tf.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
        tf.setPromptText("Enter height");
        TextField tf1 = new TextField();
        tf1.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
        tf1.setPromptText("Enter weight");
        TextArea ta = new TextArea();
        ta.setEditable(false);
        ta.setWrapText(true);
        ta.setFocusTraversable(false);
        ta.setStyle("-fx-focus-color: transparent");

        // Panes
        BorderPane pane = new BorderPane();
        pane.setBottom(ta);
        pane.setLeft(tf);
        pane.setCenter(tf1);
        pane.setRight(bt);

        // Scene
        Scene scene = new Scene(pane);

        // Stage
        primaryStage.setResizable(false);
        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        primaryStage.show();

        bt.setOnAction((e) -> {
            try (Socket socket = new Socket("localhost", 8000)) {
                Platform.runLater(() -> {
                    // Display the connection to the server
                    ta.appendText("Successfully connected to " + socket.getInetAddress() + " at " + new Date() + "\n");
                });
                // Send data to the server
                DataOutputStream height = new DataOutputStream(socket.getOutputStream());
                height.writeDouble(Double.parseDouble(tf.getText()));
                DataOutputStream weight = new DataOutputStream(socket.getOutputStream());
                weight.writeDouble(Double.parseDouble(tf1.getText()));
            }
            catch (IOException ex) {
                ta.appendText("Connection failed \n");
            }
        });
    }
}
