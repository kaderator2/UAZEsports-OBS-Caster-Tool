package com.uazesports.obscastertool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.Scanner;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

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
            initializeCasterFile();
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        primaryStage.setOnCloseRequest(event -> {
            Alert closeAlert = new Alert(Alert.AlertType.CONFIRMATION);
            closeAlert.setHeaderText("UAZ Caster Tool Dialog");
            closeAlert.setContentText("Are you sure you would like to exit?");
            Optional<ButtonType> closeResult = closeAlert.showAndWait();
            if (closeResult.get() == ButtonType.OK) {
                System.exit(0);
            }
            else{
                event.consume();
            }
        });
    }

    public static void initializeCasterFile() {
        // Attempts to open the caster database file. Creates a new one if it does not exist.
        try (Scanner casterDatabaseScanner = new Scanner(new File("casterDatabase.csv"))) {
            // No need to do anything if the file exists.
        } catch (FileNotFoundException e) {
            try (PrintWriter initCasterDatabaseWriter = new PrintWriter("casterDatabase.csv")) {
                initCasterDatabaseWriter.print("#Caster First Name, Caster Last Name, Caster Discord");
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }
}
