package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.TaskObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Controller implements Initializable {
	
	static String _input;
	static UIMain _UI = new UIMain();
	ArrayList<TaskObject> taskList = _UI.getLastOutputTaskList();
	HelpPopupController popupController = new HelpPopupController();
	
	@FXML
	private TextField userInput;
	@FXML 
	private static BorderPane layout;
	@FXML
	private TextFlow feedbackBox;
	@FXML
	private Text feedbackMessage;
	@FXML
	private TableView<TaskObject> taskTable;
	@FXML
	private TableColumn<TaskObject, String> indexColumn;
	@FXML
	private TableColumn<TaskObject, String> taskColumn;
	@FXML
	private TableColumn<TaskObject, String> statusColumn;
	@FXML
	private TableColumn<TaskObject, Integer> startDateColumn;
	@FXML
	private TableColumn<TaskObject, Integer> endDateColumn;
	@FXML
	private TableColumn<TaskObject, String> timeColumn;
	
	@FXML
	public void handleEnterPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
    	System.out.println(userInput.getText());
    	_input = userInput.getText();
    	_UI.passInput(_input);
    	userInput.clear(); //clears textfield after each input
    	displayMessage(); //print feedback message
    	display(); //refreshes table after every command
    	}
	}
	
	@FXML
	public void handleHelpPressed(KeyEvent event) throws IOException {
		if (event.getCode() == KeyCode.F1) {
			popupController.startHelp();
			System.out.println("help activated"); //to be removed aft everything is stable
		}
	}
	
	@FXML
	public void handleEscReleased(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE) {
			System.exit(0);
		}
	}
	
	public static ArrayList<String> getHelpList(int i) {
		switch(i) {
		case 1:
			_UI.passInput("help Add");
			break;
		case 2:
			_UI.passInput("help Search");
			break;
		case 3:
			_UI.passInput("help Edit");
			break;
		case 4:
			_UI.passInput("help Delete");
			break;
		case 5:
			_UI.passInput("help Undo");
			break;
		case 6:
			_UI.passInput("help save");
			break;
		case 7:
			_UI.passInput("help Exit");
			break;
		}
		return _UI.getHelpOutput();
	}
	
	private void displayMessage() {
		feedbackMessage.setText(_UI.getMessage());
		feedbackBox.getChildren().clear();
		feedbackBox.getChildren().add(feedbackMessage);
	}

	private void display() {
		ObservableList<TaskObject> groupData = FXCollections.observableArrayList(taskList);		
		fillTable(groupData);
	}

	public void fillTable(ObservableList<TaskObject> groupData) {
		populateIndex();
		populateColumns();		
		taskTable.setItems(groupData);
	}

	public void populateColumns() {
		taskColumn.setCellValueFactory(new PropertyValueFactory<TaskObject, String>("Title"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<TaskObject, String>("status"));
		endDateColumn.setCellValueFactory(new PropertyValueFactory<TaskObject, Integer>("endDate"));
		startDateColumn.setCellValueFactory(new PropertyValueFactory<TaskObject, Integer>("startDate"));
		timeColumn.setCellValueFactory(new PropertyValueFactory<TaskObject, String>("timeOutputString"));
	}

	public void populateIndex() {
		indexColumn.setCellFactory(col -> new TableCell<TaskObject, String>() {
		    @Override
			public void updateIndex(int index) {
		        super.updateIndex(index);
		        if (isEmpty() || index < 0) {
		            setText(null);
		        } else {
		            setText(Integer.toString(index+1));
		        }
		    }
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		display(); //start program with all tasks in table
	}	
} 
