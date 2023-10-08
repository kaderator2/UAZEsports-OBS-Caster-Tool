package com.uazesports.obscastertool;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.util.Optional;
import java.util.Scanner;

public class EventController {
    private final int MAX_GAMES = 8;
    @FXML
    public TableColumn<Caster, String> discordTableColumn;
    @FXML
    public TableColumn<Caster, String> firstNameTableColumn;
    @FXML
    public TableColumn<Caster, String> lastNameTableColumn;
    @FXML
    protected ChoiceBox<String> totalGamesChoiceBox;
    @FXML
    protected ChoiceBox<String> currentGameChoiceBox;
    @FXML
    protected ChoiceBox<String> homeScoreChoiceBox;
    @FXML
    protected ChoiceBox<String> awayScoreChoiceBox;
    private BufferedWriter writeCasterDB;
    private ObservableList<Caster> casterList = FXCollections.observableArrayList();
    private int totalGames = 6;
    private int currentGame = 1;
    private int homeScore = 0;
    private int awayScore = 0;

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
    private ChoiceBox leftCasterDD;
    @FXML
    private ChoiceBox rightCasterDD;
    @FXML
    private CheckBox autoUpdateScore;

    @FXML
    private TextField homeTeamName;
    @FXML
    private TextField awayTeamName;
    @FXML
    private TextField homeTeamColor;
    @FXML
    private TextField awayTeamColor;

    @FXML
    public void initialize() {
        populateCasterList();
        initScoreSettings();
        handleChoiceBoxSelection(totalGamesChoiceBox, "games.txt");
        handleChoiceBoxSelection(currentGameChoiceBox, "games.txt");
        handleChoiceBoxSelection(homeScoreChoiceBox, "score.txt");
        handleChoiceBoxSelection(awayScoreChoiceBox, "score.txt");
    }

    @FXML
    protected void updateTeamInfo(ActionEvent event) {
        String homeName = homeTeamName.getText();
        String awayName = awayTeamName.getText();
        String homeColor = homeTeamColor.getText();
        String awayColor = awayTeamColor.getText();
        boolean invalidHexColorStart = (homeColor.isEmpty() || awayColor.isEmpty() ||
                (homeColor.charAt(0) != '#' || awayColor.charAt(0) != '#'));
        boolean invalidHexColorLength = (homeColor.length() != 7) || (awayColor.length() != 7);

        if (invalidHexColorStart || invalidHexColorLength) {
            showErrorMessage("Invalid Hex Color Codes! Please use the format #RRGGBB.");
        } else {
            // Valid hex color codes, proceed with updating team information
            String teamInfo = homeName + "\n" + awayName + "\n" + homeColor + "\n" + awayColor;
            try {
                writeDataToFile("TeamInfo.txt", teamInfo);
            }
            catch (Exception e) {
                showErrorMessage("Error updating Team Info!");
            }
        }
    }

    @FXML
    protected void changeActiveCasters(ActionEvent event) {
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
            showErrorMessage("Error swapping casters!");
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
            showErrorMessage("Error Adding Caster!");
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
        homeScoreChoiceBox.getSelectionModel().select(homeScore);
        if (autoUpdateScore.isSelected()) {
            currentGame += 1;
            currentGameChoiceBox.getSelectionModel().select(currentGame - 1);
        }
    }

    @FXML
    protected void increaseAwayScore(ActionEvent event) {
        awayScore += 1;
        awayScoreChoiceBox.getSelectionModel().select(awayScore);
        if (autoUpdateScore.isSelected()) {
            currentGame += 1;
            currentGameChoiceBox.getSelectionModel().select(currentGame - 1);
        }
    }

    @FXML
    protected void quit(ActionEvent event) {
        Alert closeAlert = new Alert(Alert.AlertType.CONFIRMATION);
        closeAlert.setHeaderText("UAZ Caster Tool Dialog");
        closeAlert.setContentText("Are you sure you would like to exit?");
        Optional<ButtonType> closeResult = closeAlert.showAndWait();
        if (closeResult.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    // ~~~~~~~~~~~ END OF FXML FUNCTIONS ~~~~~~~~~~~

    public static void showErrorMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("UAZ Caster Tool Dialog");
        alert.setHeaderText("An error has been encountered");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void handleChoiceBoxSelection(ChoiceBox<String> choiceBox, String fileName) {
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                try {
                    if (number2.intValue() >= 0 && number2.intValue() < choiceBox.getItems().size()) {
                        int value = Integer.parseInt(choiceBox.getItems().get(number2.intValue()));
                        if (fileName.equals("games.txt")) {
                            if (choiceBox == totalGamesChoiceBox) {
                                totalGames = value;
                                currentGameChoiceBox.getItems().clear();
                                for (int i = 0; i < totalGames; i++) {
                                    currentGameChoiceBox.getItems().add(String.valueOf(i + 1));
                                }
                                // Adjust currentGame if it's now out of bounds
                                if (currentGame > totalGames) {
                                    currentGame = totalGames;
                                }
                                // Update currentGameChoiceBox selection
                                currentGameChoiceBox.getSelectionModel().select(currentGame - 1);
                            } else if (choiceBox == currentGameChoiceBox) {
                                currentGame = value;
                            }
                        } else if (fileName.equals("score.txt")) {
                            if (choiceBox == homeScoreChoiceBox) {
                                homeScore = value;
                            } else if (choiceBox == awayScoreChoiceBox) {
                                awayScore = value;
                            }
                        }
                        writeDataToFile(fileName, (fileName.equals("games.txt")) ? getGameStr() : getScoreStr());
                    }
                } catch (Exception e) {
                    showErrorMessage("Error Initializing choice boxes!");
                    e.printStackTrace();
                }
            }
        });
    }


    private void resetCasterFile() {
        File cFile = new File("casterDatabase.csv");
        cFile.delete();
        Main.initializeCasterFile();
    }

    private void initScoreSettings() {
        totalGamesChoiceBox.getItems().clear();
        currentGameChoiceBox.getItems().clear();
        homeScoreChoiceBox.getItems().clear();
        awayScoreChoiceBox.getItems().clear();
        String[] temp = new String[MAX_GAMES];
        for (int i = 0; i < MAX_GAMES; i++) {
            temp[i] = String.valueOf(i + 1);
        }
        totalGamesChoiceBox.getItems().addAll(temp);
        totalGamesChoiceBox.getSelectionModel().select(temp.length - 1);
        if (currentGameChoiceBox.getItems().isEmpty()) {
            //currentGameChoiceBox.getItems().add("0");
            currentGameChoiceBox.getItems().addAll(temp);
            currentGameChoiceBox.getSelectionModel().select(0);
        }
        homeScoreChoiceBox.getItems().add("0");
        awayScoreChoiceBox.getItems().add("0");
        homeScoreChoiceBox.getItems().addAll(temp);
        awayScoreChoiceBox.getItems().addAll(temp);
        homeScoreChoiceBox.getSelectionModel().select(0);
        awayScoreChoiceBox.getSelectionModel().select(0);
    }

    private void populateCasterList() {
        try {
            loadCastersFromFile();
        } catch (Exception e) {
            showErrorMessage("Error Loading Caster Database!");
        }
        discordTableColumn.setCellValueFactory(new PropertyValueFactory<>("discordTB"));
        firstNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstNameTB"));
        lastNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastNameTB"));
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
            boolean commentedLine = line.charAt(0) == '#';
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