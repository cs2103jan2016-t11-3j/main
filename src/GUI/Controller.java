package GUI;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Controller implements Initializable {
	static String input;
	
	@FXML
	private TextField userInput;
	
	@FXML
	public void handleEnterPressed(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER) {
    	System.out.println(userInput.getText());
    	input = userInput.getText();
    	userInput.clear();
    	}
	}
	
	@FXML


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

	public static String getInput() {
		return input;
	}
	
	
	


} 
