package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class EventController {
	private BufferedWriter writeCasterDB;
	private ObservableList<Caster> casterList = FXCollections.observableArrayList();
	private final int MAX_GAMES = 8;
	private int totalGames = 6;
	private int currentGame = 1;
	private int homeScore = 0;
	private int awayScore = 0;
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
	protected ChoiceBox<String> totalGamesBox;
	@FXML
	protected ChoiceBox<String> currentGameBox;
	@FXML
	protected ChoiceBox<String> homeScoreBox;
	@FXML
	protected ChoiceBox<String> awayScoreBox;
	@FXML
	private CheckBox autoUpdateScore;

	@FXML
	public void initialize() {
		populateCasterList();
		initScoreSettings();
		totalGamesBox.getSelectionModel().selectedIndexProperty()
				.addListener((ChangeListener<? super Number>) new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> observableValue, Number number,
							Number number2) {
						try {
							totalGames = Integer.parseInt(totalGamesBox.getItems().get((Integer) number2));
							currentGameBox.getSelectionModel().clearSelection();
							currentGameBox.getItems().clear();
							for (int i = 0; i < totalGames; i++) {
								currentGameBox.getItems().add(String.valueOf(i + 1));
							}
							currentGameBox.getSelectionModel().select(0);
							writeDataToFile("games.txt", getGameStr());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
		currentGameBox.getSelectionModel().selectedIndexProperty()
				.addListener((ChangeListener<? super Number>) new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> observableValue, Number number,
							Number number2) {
						try {
							currentGame = Integer.parseInt(currentGameBox.getItems().get((Integer) number2));
							writeDataToFile("games.txt", getGameStr());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
		homeScoreBox.getSelectionModel().selectedIndexProperty()
				.addListener((ChangeListener<? super Number>) new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> observableValue, Number number,
							Number number2) {
						try {
							homeScore = Integer.parseInt(homeScoreBox.getItems().get((Integer) number2));
							writeDataToFile("score.txt", getScoreStr());
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				});
		awayScoreBox.getSelectionModel().selectedIndexProperty()
				.addListener((ChangeListener<? super Number>) new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> observableValue, Number number,
							Number number2) {
						try {
							awayScore = Integer.parseInt(awayScoreBox.getItems().get((Integer) number2));
							writeDataToFile("score.txt", getScoreStr());
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}

					}
				});
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

	@FXML
	protected void addNewCaster(ActionEvent event) {
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
				e.printStackTrace();
			}
		}
		populateCasterList();
	}

	@FXML
	protected void resetScoreSettings(ActionEvent event) {
		initScoreSettings();
	}

	@FXML
	protected void increaseHomeScore(ActionEvent event) {
		homeScore += 1;
		homeScoreBox.getSelectionModel().select(homeScore);
		if (autoUpdateScore.isSelected()) {
			currentGame += 1;
			currentGameBox.getSelectionModel().select(currentGame - 1);
		}
	}

	@FXML
	protected void increaseAwayScore(ActionEvent event) {
		awayScore += 1;
		awayScoreBox.getSelectionModel().select(awayScore);
		if (autoUpdateScore.isSelected()) {
			currentGame += 1;
			currentGameBox.getSelectionModel().select(currentGame - 1);
		}
	}

	@FXML
	protected void quit(ActionEvent event) {
		System.exit(0);
	}

	// ~~~~~~~~~~~ END OF FXML FUNCTIONS ~~~~~~~~~~~

	private void resetCasterFile() {
		File cFile = new File("casterDatabase.csv");
		cFile.delete();
		Main.initCasterFile();
	}

	private void initScoreSettings() {
		totalGamesBox.getItems().clear();
		currentGameBox.getItems().clear();
		homeScoreBox.getItems().clear();
		awayScoreBox.getItems().clear();
		String[] temp = new String[MAX_GAMES];
		for (int i = 0; i < MAX_GAMES; i++) {
			temp[i] = String.valueOf(i + 1);
		}
		totalGamesBox.getItems().addAll(temp);
		totalGamesBox.getSelectionModel().select(temp.length - 1);
		if (currentGameBox.getItems().isEmpty()) {
			currentGameBox.getItems().addAll(temp);
			currentGameBox.getSelectionModel().select(0);
		}
		homeScoreBox.getItems().add("0");
		awayScoreBox.getItems().add("0");
		homeScoreBox.getItems().addAll(temp);
		awayScoreBox.getItems().addAll(temp);
		homeScoreBox.getSelectionModel().select(0);
		awayScoreBox.getSelectionModel().select(0);
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
		PrintWriter newFilePW = new PrintWriter(fname);
		newFilePW.print(data);
		newFilePW.close();
	}

	private String getGameStr() {
		return "GAME " + currentGame + "/" + totalGames;
	}

	private String getScoreStr() {
		return homeScore + " - " + awayScore;
	}
}