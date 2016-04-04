package GUI;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.TaskObject;
import javafx.animation.TranslateTransition;
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
import javafx.scene.control.TablePosition;
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
import javafx.util.Duration;

import static logic.constants.Strings.*;

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
	static int sortStatus = 0;
	
	@FXML
	private TextField userInput;
	@FXML
	private TextFlow taskDateList;
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
	//reads input on enter
	public void handleEnterPressed(KeyEvent event) throws IOException {
		if (event.getCode() == KeyCode.ENTER) {
			readInput();
			passInput();
			clearTextField();
			hideSidePanel();
			feedbackUser();
		}
	}

	private void hideSidePanel() {
		if (!_input.startsWith("edit") && !_input.startsWith("delete")) {
			taskDateList.setVisible(false);
		} else {
			fillSidebar();
		}
	}

	@FXML
	public void handleKeyPressed(KeyEvent event) throws IOException {
		if (event.getCode() == KeyCode.F1) {
			popupController.startHelp();
		}
		if (event.getCode() == KeyCode.F3) {
			if (sortStatus == 0) {
				_UI.setSortByType();
				sortStatus = 1;
			} else {
				_UI.setSortByDate();
				sortStatus = 0;
			}
			display();
		}
		if (event.getCode() == KeyCode.ESCAPE) {
			System.exit(0);
		}
	}
    
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
		
		_UI.setSortByDate();
		manageSidePanel();
		display(); //start program with all tasks in table
		
	}

	private void manageSidePanel() {
		taskDateList.managedProperty().bind(taskDateList.visibleProperty());
	}

	private void sidePanelAnimation() {
		TranslateTransition openNav = new TranslateTransition(new Duration(300), taskDateList);
		openNav.setToX(0);
		
		if (taskDateList.getTranslateX() != 0) {
			openNav.play();
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
		setSelectionFocus();			
		if (isRecurringDateRequest()) {
			taskDateList.setVisible(true);
			fillSidebar();
			sidePanelAnimation();
		} else {
			displayMessage(); // print feedback message
			display(); // refreshes table after every command
		}
	}

	private void setSelectionFocus() {
		if (_input.startsWith("add")){
			taskTable.scrollTo(taskTable.getItems().size()-1);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					taskTable.getSelectionModel().select(taskTable.getItems().size()-1); 
				}
			});
		} else if (_input.startsWith("edit") && taskDateList.isVisible() == false) {
			String[] input = _input.split(" ");
			int index = Integer.parseInt(input[1]);
			taskTable.scrollTo(index-1);
			taskTable.getSelectionModel().select(index-1);
		} else {
			taskTable.getSelectionModel().clearSelection();
		}
	}
	
	private boolean isRecurringDateRequest() {

		if (_UI.getOutput().size() > 0) {
			if (_UI.getOutput().get(0).startsWith("Timings for")) {
				return true;
			}		
		}
		return false;
	}

	private void fillSidebar() {
		taskDateList.getChildren().clear();
		ArrayList<String> recurringTimes = _UI.getOutput();
		for (int i = 0; i < recurringTimes.size(); i++) {
			taskDateList.getChildren().add(new Text(recurringTimes.get(i) + "\n"));
		}
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
		return _UI.getOutput();
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
		wrapText();
		colourCode();
	}

	private void colourCode() {
		if (sortStatus == 0) {
			statusColumn.setCellFactory(new Callback<TableColumn<TaskObject, String>, TableCell<TaskObject, String>>() {
				@Override
				public TableCell<TaskObject, String> call(TableColumn<TaskObject, String> param) {
					final TableCell<TaskObject, String> cell = new TableCell<TaskObject, String>() {
						private Text text;

						@Override
						public void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							if (!isEmpty()) {
								text = new Text(item.toString());
								if (item.startsWith("incomplete")) {
									this.getTableRow().getStyleClass().add("undoneTasks");
								} else if (item.startsWith("complete")) {
									this.getTableRow().getStyleClass().add("doneTasks");
								}
								setGraphic(text);
							}
						}
					};
					return cell;
				}
			});
		}
	}

	private void wrapText() {
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
							text.wrappingWidthProperty().bind(taskColumn.widthProperty());
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
							text.wrappingWidthProperty().bind(timeColumn.widthProperty());
							setGraphic(text);
						}
					}
				};
				return cell;
			}
		});
	}
	
} 
