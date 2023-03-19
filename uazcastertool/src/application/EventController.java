package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EventController {
	private BufferedWriter writeCasterDB;
	@FXML
	private Label errorText;
	@FXML
	private Label successText;
	@FXML
	private TextField firstNameField;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField discordField;

	@FXML
	protected void handleSubmitButtonAction(ActionEvent event) {
		// actiontarget.setText("Sign in button pressed");
	}

	@FXML
	protected void changeActiveCasters(ActionEvent event) {
		// TODO Fix me
		System.out.println("Sign in button pressed");
	}

	@FXML
	protected void addNewCaster(ActionEvent event) {
		// TODO Fix me
		successText.setVisible(false);
		errorText.setVisible(false);
		try {
			
			String firstName = firstNameField.getText();
			String lastName = lastNameField.getText();
			String discordUser = discordField.getText();
			if (firstName.contains(",") || lastName.contains(",") || discordUser.contains(",")) {
				throw new java.io.IOException();
			}
			System.out.println(firstName + lastName + discordUser);
			writeCasterDB = new BufferedWriter(new FileWriter("casterDatabase.csv", true));
			writeCasterDB.newLine();
			writeCasterDB.write(firstName + ", " + lastName + ", " + discordUser);
			writeCasterDB.close();
			successText.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			errorText.setVisible(true);
		}

	}

}