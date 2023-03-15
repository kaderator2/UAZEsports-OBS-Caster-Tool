package application;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.text.Text;
 
public class EventController {
    @FXML private Text actiontarget;
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        actiontarget.setText("Sign in button pressed");
    }
    
    @FXML protected void changeActiveCasters(ActionEvent event) {
    	//TODO Fix me
        actiontarget.setText("Sign in button pressed");
    }
    
    @FXML protected void addNewCaster(ActionEvent event) {
    	//TODO Fix me
        actiontarget.setText("Sign in button pressed");
    }
    

}