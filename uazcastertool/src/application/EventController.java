package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class EventController {
	private BufferedWriter writeCasterDB;
	private ObservableList<Caster> casterList = FXCollections.observableArrayList();
	@FXML
	private Label errorText;
	@FXML
	private Label swappingErrorText;
	@FXML
	private Label swappingSuccessText;
	@FXML
	private Label successText;
	@FXML
	private TextField firstNameField;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField discordField;
	@FXML
	private TableView<Caster> tbData;
	@FXML
	public TableColumn<Caster, String> discordTB;
	@FXML
	public TableColumn<Caster, String> firstNameTB;
	@FXML
	public TableColumn<Caster, String> lastNameTB;
	@FXML
	private ChoiceBox leftCasterDD;
	@FXML
	private ChoiceBox rightCasterDD;

	@FXML
	public void initialize() {
		populateCasterList();
	}

	@FXML
	protected void quit(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	protected void changeActiveCasters(ActionEvent event) {
		swappingErrorText.setVisible(false);
		swappingSuccessText.setVisible(false);
		try {
			Caster leftCaster = (Caster) leftCasterDD.getSelectionModel().getSelectedItem();
			Caster rightCaster = (Caster) rightCasterDD.getSelectionModel().getSelectedItem();

			writeDataToFile("LCFN.txt", leftCaster.getFirstNameTB());
			writeDataToFile("LCLN.txt", leftCaster.getLastNameTB());
			writeDataToFile("RCFN.txt", rightCaster.getFirstNameTB());
			writeDataToFile("RCLN.txt", rightCaster.getLastNameTB());
			writeDataToFile("RCD.txt", rightCaster.getDiscordTB());
			writeDataToFile("LCD.txt", leftCaster.getDiscordTB());
			swappingSuccessText.setVisible(true);
		} catch (Exception e) {
			System.out.println("Error swapping caster!");
			swappingErrorText.setVisible(true);
		}
	}

	@FXML
	protected void clearCasterDB(ActionEvent event) {
		Alert deleteConfirm = new Alert(AlertType.CONFIRMATION, "Clear Caster Database?", ButtonType.YES,
				ButtonType.NO);
		deleteConfirm.showAndWait();
		if (deleteConfirm.getResult() == ButtonType.YES) {
			resetCasterFile();
		}
		casterList = FXCollections.observableArrayList();
		populateCasterList();
	}

	private void resetCasterFile() {
		File cFile = new File("casterDatabase.csv");
		cFile.delete();
		Main.initCasterFile();
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
			boolean hasInvalid = firstName.contains("'") || lastName.contains("'") || discordUser.contains("'");
			boolean fullyEmpty = firstName.equals("") && lastName.equals("") && discordUser.equals("");
			if (hasCommas || fullyEmpty || hasInvalid) {
				throw new java.io.IOException();
			}
			Caster newCaster = new Caster(firstName, lastName, discordUser);
			writeCaster(newCaster);
			// System.out.println(firstName + lastName + discordUser);
			populateCasterList();
			successText.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			errorText.setVisible(true);
		}

	}

	@FXML
	protected void deleteSelectedCaster(ActionEvent event) {
		Caster deletedCaster = tbData.getSelectionModel().getSelectedItem();
		casterList.remove(deletedCaster);
		resetCasterFile();
		for (Caster caster : casterList) {
			try {
				writeCaster(caster);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		populateCasterList();
	}

	private void populateCasterList() {
		try {
			loadCastersFromFile();
		} catch (Exception e) {
			System.out.print(e);
		}
		discordTB.setCellValueFactory(new PropertyValueFactory<>("discordTB"));
		firstNameTB.setCellValueFactory(new PropertyValueFactory<>("firstNameTB"));
		lastNameTB.setCellValueFactory(new PropertyValueFactory<>("lastNameTB"));
		tbData.setItems(casterList);
		leftCasterDD.setItems(casterList);
		rightCasterDD.setItems(casterList);
	}

	private void loadCastersFromFile() throws java.io.IOException {
		Scanner casterFile = new Scanner(new File("casterDatabase.csv"));
		casterList = FXCollections.observableArrayList();
		while (casterFile.hasNextLine()) {
			String line = casterFile.nextLine();
			String[] splitLine = line.split(",");
			boolean commentedLine = line.substring(0, 1).equals("#");
			if (!commentedLine) {
				Caster newCaster = new Caster(splitLine[0].replace("'", ""), splitLine[1].replace("'", ""),
						splitLine[2].replace("'", ""));
				casterList.add(newCaster);
			}
		}
		casterFile.close();
	}

	private void writeCaster(Caster newCaster) throws java.io.IOException {
		writeCasterDB = new BufferedWriter(new FileWriter("casterDatabase.csv", true));
		writeCasterDB.newLine();
		writeCasterDB.write(newCaster.toDataString());
		writeCasterDB.close();
	}

	private void writeDataToFile(String fname, String data) throws FileNotFoundException {
		// System.out.println("Cant find caster db file... Creating a new one!");
		PrintWriter newFilePW = new PrintWriter(fname);
		newFilePW.print(data);
		newFilePW.close();
		// TODO Auto-generated catch block
		// System.out.println("Cant make new file, quitting...");

	}
}