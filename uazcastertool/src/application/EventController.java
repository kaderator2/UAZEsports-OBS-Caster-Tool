package application;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EventController {
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
			System.out.println(firstName + lastName + discordUser);
			successText.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			errorText.setVisible(true);
		}
	}

}