package GUI;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import logic.TaskObject;

public class Controller implements Initializable {
	static String _input;
	static UIMain _UI = new UIMain();
	
	@FXML
	private TextField userInput;
	@FXML 
	private static BorderPane layout;
	@FXML
	private TextFlow feedbackBox;
	@FXML
	private static TableView<ArrayList<TaskObject>> taskTable;
	@FXML
	private static TableColumn<ArrayList<TaskObject>, String> indexColumn;
	@FXML
	private static TableColumn<ArrayList<TaskObject>, String> taskColumn;
	@FXML
	private static TableColumn<ArrayList<TaskObject>, Integer> deadlineColumn;
	
	
	@FXML
	public void handleEnterPressed(KeyEvent event) {
    	feedbackBox.getChildren().clear();
		if (event.getCode() == KeyCode.ENTER) {
    	System.out.println(userInput.getText());
    	_input = userInput.getText();
    	_UI.passInput(_input);
    	userInput.clear();
    	displayMessage();
 
    	}
	}
	
	private void displayMessage() {
		Text feedbackMessage = new Text(_UI.getOutput());
		feedbackBox.getChildren().add(feedbackMessage);
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/**taskColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
		deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
		indexColumn.setCellValueFactory(new PropertyValueFactory<>("taskId"));
		
		taskTable.getItems().setAll(_UI.getTaskList());
		*/
		
	}

	
} 
