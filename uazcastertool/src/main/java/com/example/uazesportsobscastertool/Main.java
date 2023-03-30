package com.example.uazesportsobscastertool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application {
    private static Scanner casterDB;

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("mainLayout.fxml"));
            Scene scene = new Scene(root, 640, 400);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("UAZ Caster Tool");
            Image uaIcon = new Image(getClass().getResourceAsStream("ualogo.png"));
            primaryStage.getIcons().add(uaIcon);
            primaryStage.setResizable(false);
            initCasterFile();
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void initCasterFile() {
        // attempts to open caster db file. Creates new one if it does not exist
        try {
            casterDB = new Scanner(new File("casterDatabase.csv"));
            casterDB.close();
        } catch (Exception e) {
            // System.out.println("Cant find caster db file... Creating a new one!");
            try {
                PrintWriter initCasterDB = new PrintWriter("casterDatabase.csv");
                initCasterDB.print("#Caster First Name, Caster Last Name, Caster Discord");
                initCasterDB.close();
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                // System.out.println("Cant make new file, quitting...");
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
