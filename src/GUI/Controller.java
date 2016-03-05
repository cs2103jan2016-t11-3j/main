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
import logic.TaskObject;

public class Controller implements Initializable {
	static String _input;
	static UIMain _UI = new UIMain();
	@FXML
	private TextField userInput;
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
    	if (event.getCode() == KeyCode.ENTER) {
    	System.out.println(userInput.getText());
    	_input = userInput.getText();
    	UIMain.passInput(_input);
    	userInput.clear();
 
    	}
	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/**taskColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
		deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
		indexColumn.setCellValueFactory(new PropertyValueFactory<>("taskId"));
		
		taskTable.getItems().setAll(getTaskList());
		*/
	}

	private ArrayList<TaskObject> getTaskList() {
		return UIMain.getTaskList();
	}


	public static String getInput() {
		return _input;
	}
	
} 
