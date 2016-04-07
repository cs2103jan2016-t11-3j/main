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

	static String _input;
	static UIMain _UI = new UIMain();
	ArrayList<TaskObject> taskList = _UI.getLastOutputTaskList();

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

	/**
	 * Handles enter pressed by reading input in textfield and process input.
	 * @param event - Enter pressed
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

	private void hideSidePanel() {
		if ((!_input.startsWith("edit")) && !_input.startsWith("delete")) {

			sidePanel.setVisible(false);

		} else {
			fillSidebar();
		}
	}

	/**
	 * Handles when F1 pressed by activating help. 
	 * Handles when F3 pressed by sorting displayed list.
	 * Handles then Esc pressed by closing program.
	 * @param event - F1 or F3 or Esc Pressed
	 * @throws IOException
	 */
	@FXML
	public void handleKeyPressed(KeyEvent event) throws IOException {
		if (event.getCode() == KeyCode.F1) {
			HelpPopupController popupController = new HelpPopupController();
			popupController.startHelp();
		}
		if (event.getCode() == KeyCode.F3) {
			_UI.setSortByDate();
			display();
		}
		if (event.getCode() == KeyCode.ESCAPE) {
			System.exit(0);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		assert taskColumn != null : "fx:id=\"taskColumn\" was not injected: check your FXML file 'TaskWindow.fxml'.";
		assert taskDateList != null : "fx:id=\"taskDateList\" was not injected: check your FXML file 'TaskWindow.fxml'.";
		assert taskTable != null : "fx:id=\"taskTable\" was not injected: check your FXML file 'TaskWindow.fxml'.";
		assert layout != null : "fx:id=\"layout\" was not injected: check your FXML file 'TaskWindow.fxml'.";
		assert recurTitle != null : "fx:id=\"recurTitle\" was not injected: check your FXML file 'TaskWindow.fxml'.";
		assert timeColumn != null : "fx:id=\"timeColumn\" was not injected: check your FXML file 'TaskWindow.fxml'.";
		assert indexColumn != null : "fx:id=\"indexColumn\" was not injected: check your FXML file 'TaskWindow.fxml'.";
		assert statusColumn != null : "fx:id=\"statusColumn\" was not injected: check your FXML file 'TaskWindow.fxml'.";
		assert programName != null : "fx:id=\"programName\" was not injected: check your FXML file 'TaskWindow.fxml'.";
		assert feedbackMessage != null : "fx:id=\"feedbackMessage\" was not injected: check your FXML file 'TaskWindow.fxml'.";
		assert userInput != null : "fx:id=\"userInput\" was not injected: check your FXML file 'TaskWindow.fxml'.";
		assert sidePanel != null : "fx:id=\"sidePanel\" was not injected: check your FXML file 'TaskWindow.fxml'.";
		assert feedbackBox != null : "fx:id=\"feedbackBox\" was not injected: check your FXML file 'TaskWindow.fxml'.";

		_UI.setSortByDate();
		manageSidePanel();
		setWrapText(); // for sideBarList
		displayMessage();
		display(); // start program with all tasks in table

	}

	private void manageSidePanel() {
		sidePanel.managedProperty().bind(sidePanel.visibleProperty());
	}

	private void sidePanelAnimation() {
		TranslateTransition openNav = new TranslateTransition(new Duration(300), sidePanel);
		openNav.setToX(0);

		if (sidePanel.getTranslateX() != 0) {
			openNav.play();
		}

	}

	private void readInput() {
		_input = userInput.getText();
	}

	private void passInput() throws IOException {
		if (_input.startsWith("help")) {
			HelpPopupController popupController = new HelpPopupController();
			popupController.startHelp();
		} else if (_input.startsWith("sort")) {
			_UI.setSortByDate();
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
			sidePanel.setVisible(true);
			fillSidebar();
			sidePanelAnimation();
		}

		displayMessage(); // print feedback message
		display(); // refreshes table after every command
	}

	private void setSelectionFocus() {
		if (_input.startsWith("add")) {
			taskTable.scrollTo(taskTable.getItems().size() - 1);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					taskTable.getSelectionModel().select(taskTable.getItems().size() - 1);
				}
			});
		} else if ((_input.startsWith("edit") || _input.startsWith("view") || _input.startsWith("find")
				|| !_input.startsWith("filter") || !_input.startsWith("display") || _input.startsWith("search")
				|| !_input.startsWith("list")) && sidePanel.isVisible() == false) {

			String[] input = _input.split(" ");
			if (input.length > 1) {
				try {
					int index = Integer.parseInt(input[1]);
					taskTable.scrollTo(index - 1);
					taskTable.getSelectionModel().select(index - 1);
				} catch (NumberFormatException e) {

				}
			}
		} else if (sidePanel.isVisible() == false) {
			taskTable.getSelectionModel().clearSelection();
		}
	}

	private boolean isRecurringDateRequest() {

		if (_UI.getOutput().size() > 0) {
			if (_UI.getOutput().get(0).startsWith("Displaying recurrence")) {
				return true;
			}
		}
		return false;
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

	private void displayMessage() {
		feedbackMessage.setText(_UI.getMessage());
		feedbackBox.getChildren().clear();
		feedbackBox.getChildren().add(feedbackMessage);
	}

	private void display() {
		ObservableList<TaskObject> taskData = FXCollections.observableArrayList(getOutputTaskList());
		fillTable(taskData);
	}
	
	/**
	 * Called by HelpPopupController to retrieve content in help manual for display.
	 * 
	 * @param i - index to indicate which section of help manual to retrieve
	 * @return _UI.getOutput: returns ArrayList<String> from different sections of help manual
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

	private ArrayList<TaskObject> getOutputTaskList() {
		return _UI.getLastOutputTaskList();
	}

	private void fillTable(ObservableList<TaskObject> taskData) {
		populateIndex();
		populateColumns();
		setCellProperty();
		taskTable.setItems(taskData);
	}

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

	private void populateColumns() {
		taskColumn.setCellValueFactory(new PropertyValueFactory<TaskObject, String>("Title"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<TaskObject, String>("status"));
		timeColumn.setCellValueFactory(new PropertyValueFactory<TaskObject, String>("timeOutputString"));
	}

	private void setCellProperty() {
		wrapText();
		colourCode();
	}

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

	private void setWrapText() {
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

}
