package application;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.text.Text;
 
public class EventController {
    @FXML private Text actiontarget;
    
    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        actiontarget.setText("Sign in button pressed");
    }

}