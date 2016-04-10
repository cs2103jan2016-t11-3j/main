//@@author A0130622X

package GUI;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * Controls the TaskWindow to allow interaction with the program Inputs keyed in
 * textfield read with enter key pressed Tasks displayed in tableview updates
 * after every command Feedback message displayed above the textfield HelpPopup
 * initiated with with F1 hotkey pressed Program closes with Esc pressed
 * 
 * @author Seow Hwee
 *
 */
public class MainController implements Initializable {

	private static final String MESSAGE_NODE_NOT_INJECTED = 
			"fx:id=\"%1$s\" was not injected: check your FXML file 'TaskWindow.fxml'.";
	
	private static String _input;
	private static UIMain _UI = new UIMain();

	@FXML
	private static BorderPane layout;
	@FXML
	private VBox sidePanel;
	@FXML
	private ListView<String> taskDateList;
	@FXML
	private TableView<TaskObject> taskTable;
	@FXML
	private TableColumn<TaskObject, String> indexColumn;
	@FXML
	private TableColumn<TaskObject, String> taskColumn;
	@FXML
	private TableColumn<TaskObject, String> statusColumn;
	@FXML
	private TableColumn<TaskObject, String> timeColumn;
	@FXML
	private TextFlow feedbackBox;
	@FXML
	private Text feedbackMessage;
	@FXML
	private TextField userInput;
	@FXML
	private Label recurTitle;
	@FXML
	private Label programName;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		assert layout != null : String.format(MESSAGE_NODE_NOT_INJECTED, "layout");
		assert programName != null : String.format(MESSAGE_NODE_NOT_INJECTED, "programName");
		
		assert sidePanel != null : String.format(MESSAGE_NODE_NOT_INJECTED, "sidePanel");
		assert taskDateList != null : String.format(MESSAGE_NODE_NOT_INJECTED, "taskDateList");
		assert recurTitle != null : String.format(MESSAGE_NODE_NOT_INJECTED, "recurTitle");

		assert taskTable != null : String.format(MESSAGE_NODE_NOT_INJECTED, "taskTable");
		assert indexColumn != null : String.format(MESSAGE_NODE_NOT_INJECTED, "indexColumn");
		assert taskColumn != null : String.format(MESSAGE_NODE_NOT_INJECTED, "taskColumn");
		assert statusColumn != null : String.format(MESSAGE_NODE_NOT_INJECTED, "statusColumn");
		assert timeColumn != null : String.format(MESSAGE_NODE_NOT_INJECTED, "timeColumn");

		assert feedbackBox != null : String.format(MESSAGE_NODE_NOT_INJECTED, "feedbackBox");
		assert feedbackMessage != null : String.format(MESSAGE_NODE_NOT_INJECTED, "feedbackMessage");
		assert userInput != null : String.format(MESSAGE_NODE_NOT_INJECTED, "userInput");

		manageSidePanel();
		sidebarWrapText(); // for sideBarList
		displayMessage();
		display(); // start program with all tasks in table

	}
	
	//--------------------------- HANDLE KEYS PRESSED METHODS -----------------------------
	/**
	 * Handles enter pressed by reading input in textfield and process input.
	 * 
	 * @param event
	 *            - Enter pressed
	 * @throws IOException
	 */
	@FXML
	// reads input on enter
	public void handleEnterPressed(KeyEvent event) throws IOException {
		if (event.getCode() == KeyCode.ENTER) {
			readInput();
			passInput();
			clearTextField();
			hideSidePanel();
			feedbackUser();
		}
	}

	/**
	 * Handles when F1 pressed by activating help. Handles when Esc pressed by
	 * closing program.
	 * 
	 * @param event
	 *            - F1 or Esc Pressed
	 * @throws IOException
	 */
	@FXML
	public void handleKeyPressed(KeyEvent event) throws IOException {
		if (event.getCode() == KeyCode.F1) {
			HelpPopupController popupController = new HelpPopupController();
			popupController.startHelp();
		}

		if (event.getCode() == KeyCode.ESCAPE) {
			System.exit(0);
		}
	}

	// ---------------------- SIDE PANEL METHODS ------------------------------

	private void manageSidePanel() {
		sidePanel.managedProperty().bind(sidePanel.visibleProperty());
	}

	private void sidebarWrapText() {
		taskDateList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(final ListView<String> list) {
				return new ListCell<String>() {
					{
						Text text = new Text();
						text.wrappingWidthProperty().bind(taskDateList.widthProperty());
						text.textProperty().bind(itemProperty());

						setPrefWidth(0);
						setGraphic(text);
					}
				};
			}
		});
	}

	private void hideSidePanel() {
		if ((!_input.startsWith("edit")) && !_input.startsWith("delete")) {
			sidePanel.setVisible(false);
		} else {
			fillSidebar();
		}
	}
	
	private void sidePanelAnimation() {
		TranslateTransition openNav = new TranslateTransition(new Duration(300), sidePanel);
		openNav.setToX(0);

		if (sidePanel.getTranslateX() != 0) {
			openNav.play();
		}
	}
	
	private void fillSidebar() {
		taskDateList.getItems().clear();
		try {
			ArrayList<String> recurringTimes = _UI.getTaskDateOutput();
			
			recurTitle.setText(recurringTimes.get(0));
			recurringTimes.remove(0);
			ObservableList<String> items = FXCollections.observableArrayList(recurringTimes);
			
			taskDateList.getItems().clear();
			taskDateList.setItems(items);
		} catch (NullPointerException e) {
			
		}		
	}

	//----------------------- USER INPUT AND FEEDBACK METHODS ---------------------------------
	private void readInput() {
		_input = userInput.getText();
	}

	private void passInput() throws IOException {
		if (_input.startsWith("help")) {
			HelpPopupController popupController = new HelpPopupController();
			popupController.startHelp();
		} else {
			_UI.passInput(_input);
		}
	}
	
	// clears textfield after each input
	private void clearTextField() {
		userInput.clear(); 
	}

	private void feedbackUser() {
		
		if (isRecurringDateRequest()) {
			sidePanel.setVisible(true);
			fillSidebar();
			sidePanelAnimation();
		}
		displayMessage(); 
		display();
		setSelectionFocus();
	}

	private boolean isRecurringDateRequest() {
		if (_UI.getOutput().size() > 0) {
			if (_UI.getOutput().get(0).startsWith("Displaying recurrence")) {
				return true;
			}
		}
		return false;
	}
	
	//shows feedback message in feedback box
	private void displayMessage() {
		feedbackMessage.setText(_UI.getMessage());
		feedbackBox.getChildren().clear();
		feedbackBox.getChildren().add(feedbackMessage);
	}

	//----------------------------- TASK TABLEVIEW METHODS -------------------------------
	
	//fills taskTable with outputTaskList
	private void display() {
		ObservableList<TaskObject> taskData = FXCollections.observableArrayList(getOutputTaskList());
		fillTable(taskData);
	}

	private void fillTable(ObservableList<TaskObject> taskData) {
		populateIndex();
		populateColumns();
		setCellProperty();
		taskTable.setItems(taskData);
	}

	//fill index column
	private void populateIndex() {
		indexColumn.setCellFactory(col -> new TableCell<TaskObject, String>() {
			@Override
			public void updateIndex(int index) {
				super.updateIndex(index);
				if (isEmpty() || index < 0) {
					setText(null);
				} else {
					setText(Integer.toString(index + 1));
				}
			}
		});
	}

	//fill all table columns except index Column
	private void populateColumns() {
		taskColumn.setCellValueFactory(new PropertyValueFactory<TaskObject, String>("Title"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<TaskObject, String>("status"));
		timeColumn.setCellValueFactory(new PropertyValueFactory<TaskObject, String>("timeOutputString"));
	}

	private void setCellProperty() {
		tableWrapText();
		colourCode();
	}

	//highlight rows of tasks according to their status
	private void colourCode() {
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
							} else {
								this.getTableRow().getStyleClass().add("overdueTasks");
							}
							setGraphic(text);
						}
					}
				};
				return cell;
			}
		});

	}

	private void tableWrapText() {
		taskNameWrapText();
		timeWrapText();
	}

	private void taskNameWrapText() {
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
	}

	private void timeWrapText() {
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

	private void setSelectionFocus() {
		if (_input.startsWith("add")) {
			
			focusToAddedTask();
			
		} else if ((_input.startsWith("edit") || _input.startsWith("view") || _input.startsWith("find")
				|| !_input.startsWith("filter") || !_input.startsWith("display") 
				|| _input.startsWith("search") || !_input.startsWith("list")) 
				&& sidePanel.isVisible() == false) {
			
			focusToEditedOrViewedTask();
			
		} else if (sidePanel.isVisible() == false) {
			
			clearFocus();
			
		}
	}

	private void focusToAddedTask() {
		int sortIndex = _UI.getAddSortedIndex();
		taskTable.scrollTo(sortIndex - 1);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				taskTable.getSelectionModel().select(sortIndex - 1);
			}
		});
	}

	private void focusToEditedOrViewedTask() {
		String[] input = _input.split(" ");
		if (input.length > 1) {
			try {
				int index = Integer.parseInt(input[1]);
				taskTable.scrollTo(index - 1);
				taskTable.getSelectionModel().select(index - 1);
			} catch (NumberFormatException e) {

			}
		}
	}

	private void clearFocus() {
		taskTable.getSelectionModel().clearSelection();
	}

	//-------------------------- GETTERS -----------------------------
	/**
	 * Called by HelpPopupController to retrieve content in help manual for
	 * display.
	 * 
	 * @param i
	 *            - index to indicate which section of help manual to retrieve
	 * @return _UI.getOutput: returns ArrayList<String> from different sections
	 *         of help manual
	 */
	public static ArrayList<String> getHelpList(int i) {
		switch (i) {
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
	
	//gets tasklist to be displayed in table
	private ArrayList<TaskObject> getOutputTaskList() {
		return _UI.getLastOutputTaskList();
	}

}
