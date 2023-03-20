package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EventController {
	private BufferedWriter writeCasterDB;
	private ArrayList<Caster> casterList = new ArrayList<>();
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
	protected void changeActiveCasters(ActionEvent event) {
		// TODO Fix me
		System.out.println("Sign in button pressed");
	}
	
	@FXML
	protected void clearCasterDB(ActionEvent event) {
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
			boolean hasCommas = firstName.contains(",") || lastName.contains(",") || discordUser.contains(",");
			boolean fullyEmpty = firstName.equals("") && lastName.equals("") && discordUser.equals("");
			if (hasCommas || fullyEmpty) {
				throw new java.io.IOException();
			}
			Caster newCaster = new Caster(firstName, lastName, discordUser);
			writeCaster(newCaster);
			System.out.println(firstName + lastName + discordUser);
			successText.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			errorText.setVisible(true);
		}
	}

	private void loadCasters() throws java.io.IOException {
		Scanner casterFile = new Scanner(new File("casterDatabase.csv"));
		while (casterFile.hasNextLine()) {
			String line = casterFile.nextLine();
			String[] splitLine = line.split(",");
			boolean commentedLine = line.substring(0, 1).equals("#");
			if (!commentedLine) {
				Caster newCaster = new Caster(splitLine[0], splitLine[1], splitLine[2]);
				if (!casterList.contains(newCaster)) {
					casterList.add(newCaster);
				}
			}
		}
		casterFile.close();
	}

	private void writeCaster(Caster newCaster) throws java.io.IOException {
		writeCasterDB = new BufferedWriter(new FileWriter("casterDatabase.csv", true));
		writeCasterDB.newLine();
		writeCasterDB.write(newCaster.toString());
		writeCasterDB.close();
	}
}