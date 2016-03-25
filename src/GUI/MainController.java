package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.TaskObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Controls the TaskWindow to allow interaction with the program
 * Inputs keyed in textfield read with enter key pressed
 * Tasks displayed in tableview updates after every command
 * Feedback message displayed above the textfield
 * HelpPopup initiated with with F1 hotkey pressed
 * Program closes with Esc pressed
 * 
 * @author Seow Hwee
 *
 */

public class MainController implements Initializable {
	
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

    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		assert layout != null : "fx:id=\"layout\" was not injected: check your FXML file 'UIScene.fxml'.";
		assert taskColumn != null : "fx:id=\"taskColumn\" was not injected: check your FXML file 'UIScene.fxml'.";
		assert endDateColumn != null : "fx:id=\"endDateColumn\" was not injected: check your FXML file 'UIScene.fxml'.";
		assert timeColumn != null : "fx:id=\"timeColumn\" was not injected: check your FXML file 'UIScene.fxml'.";
		assert indexColumn != null : "fx:id=\"indexColumn\" was not injected: check your FXML file 'UIScene.fxml'.";
		assert startDateColumn != null : "fx:id=\"startDateColumn\" was not injected: check your FXML file 'UIScene.fxml'.";
		assert statusColumn != null : "fx:id=\"statusColumn\" was not injected: check your FXML file 'UIScene.fxml'.";
		assert feedbackMessage != null : "fx:id=\"feedbackMessage\" was not injected: check your FXML file 'UIScene.fxml'.";
		assert userInput != null : "fx:id=\"userInput\" was not injected: check your FXML file 'UIScene.fxml'.";
		assert feedbackBox != null : "fx:id=\"feedbackBox\" was not injected: check your FXML file 'UIScene.fxml'.";
		assert taskTable != null : "fx:id=\"taskTable\" was not injected: check your FXML file 'UIScene.fxml'.";
		
		setColumnStyle();
		display(); //start program with all tasks in table
		
	}

	private void setColumnStyle() {
		indexColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 17; -fx-font-family: 'Agency FB'");
		statusColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 17; -fx-font-family: 'Agency FB'");
		taskColumn.setStyle("-fx-font-size: 17; -fx-font-family: 'Agency FB'");
		timeColumn.setStyle("-fx-font-size: 17; -fx-font-family: 'Agency FB'");
	}	
	
	@FXML
	//reads input on enter
	public void handleEnterPressed(KeyEvent event) throws IOException {
		if (event.getCode() == KeyCode.ENTER) {
			System.out.println(userInput.getText()); //to be removed
			readInput();
			passInput();
			clearTextField();
			feedbackUser();
		}
	}
	
	private void readInput() {
		_input = userInput.getText();
	}

	private void passInput() throws IOException {
		if(_input.startsWith("help")) {
			popupController.startHelp();
		} else {
			_UI.passInput(_input);
		}
	}

	private void clearTextField() {
		userInput.clear(); // clears textfield after each input
	}

	private void feedbackUser() {
		displayMessage(); // print feedback message
		display(); // refreshes table after every command
	}
	
	private void displayMessage() {
		feedbackMessage.setText(_UI.getMessage());
		feedbackBox.getChildren().clear();
		feedbackBox.getChildren().add(feedbackMessage);
	}
	
	private void display() {
		ObservableList<TaskObject> taskData = FXCollections.observableArrayList(getOutputTaskList());		
		fillTable(taskData);
	}
	
	@FXML
	public void handleHelpPressed(KeyEvent event) throws IOException {
		if (event.getCode() == KeyCode.F1) {
			popupController.startHelp();
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

	public static ArrayList<String> getAlertOutput() {
		return _UI.getAlertOutput();
	}

	private ArrayList<TaskObject> getOutputTaskList() {
		return _UI.getLastOutputTaskList();
	}

	public void fillTable(ObservableList<TaskObject> taskData) {
		populateIndex();
		populateColumns();
		setCellProperty();
		taskTable.setItems(taskData);
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
	
	public void populateColumns() {
		taskColumn.setCellValueFactory(new PropertyValueFactory<TaskObject, String>("Title"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<TaskObject, String>("status"));
		timeColumn.setCellValueFactory(new PropertyValueFactory<TaskObject, String>("timeOutputString"));
	}

	private void setCellProperty() {
		taskColumn.setCellFactory(new Callback<TableColumn<TaskObject, String>, TableCell<TaskObject, String>>() {
			@Override
			public TableCell<TaskObject, String> call(TableColumn<TaskObject, String> param) {
				final TableCell<TaskObject, String> cell = new TableCell<TaskObject, String>() {
					private Text text;
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (!isEmpty()) {
							text = new Text(item.toString());
							text.setWrappingWidth(taskColumn.getPrefWidth()); // Setting the wrapping width to the Text
							setGraphic(text);
						}
					}
				};
				return cell;
			}
		});

		timeColumn.setCellFactory(new Callback<TableColumn<TaskObject, String>, TableCell<TaskObject, String>>() {
			@Override
			public TableCell<TaskObject, String> call(TableColumn<TaskObject, String> param) {
				final TableCell<TaskObject, String> cell = new TableCell<TaskObject, String>() {
					private Text text;
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (!isEmpty()) {
							text = new Text(item.toString());
							text.setWrappingWidth(timeColumn.getPrefWidth()); // Setting the wrapping width to the Text
							setGraphic(text);
						}
					}
				};
				return cell;
			}
		});
	}
	@FXML
	public void handleTabPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.TAB) {
			taskTable.setFocusTraversable(true);
			userInput.setFocusTraversable(true);
		}
	}
} 
