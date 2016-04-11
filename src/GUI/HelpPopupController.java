//@@author A0130622X

package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import common.AtfLogger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controls the HelpPopup to allow access to help manual Help topic displayed
 * sorted by command type: Add, Search, Edit, Delete, Undo, Save, Exit. Display
 * topic changes with left/right arrowkeys pressed. Popup closes with Esc
 * pressed
 * 
 * @author Seow Hwee
 *
 */

public class HelpPopupController implements Initializable {

	public static final String MESSAGE_INVALID_STYLESHEET = "Error: HelpStyle.css stylesheet not found.";
	private static final String MESSAGE_NODE_NOT_INJECTED = 
			"fx:id=\"%1$s\" was not injected: check your FXML file 'HelpPopup.fxml'.";

	private static Stage helpStage = new Stage();
	private static ArrayList<String> displayList = MainController.getHelpList(1);
	private static int page = 1;

	private static Logger logger = AtfLogger.getLogger();
	
	@FXML
	private VBox helpPane;
	@FXML
	private TextArea helpText;
	@FXML
	private Label topicLabel;
	@FXML
	private Label pageNumber;

	
	// ----------------------------- START HELP WINDOW ----------------------------
	/**
	 * Called by MainController to activate help pop-up. Function starts and
	 * shows the help pop-up window.
	 * 
	 * @throws IOException
	 *             unable to load fxml file
	 */
	public void startHelp() {
		Parent help;
		try {
			help = FXMLLoader.load(getClass().getResource("HelpPopup.fxml"));
			Scene helpScene = new Scene(help);
			helpStage.setScene(helpScene);
			setStyle(helpScene);
			helpStage.show();
		} catch (IOException e) {
			logger.warning("unable to load HelpPopup.fxml file");
			e.printStackTrace();
		}
	}

	private void setStyle(Scene scene) {
		URL url = this.getClass().getResource("HelpStyle.css");
		if (url == null) {
			System.out.println(MESSAGE_INVALID_STYLESHEET);
		}
		String css = url.toExternalForm();
		scene.getStylesheets().add(css);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		assert helpText != null : String.format(MESSAGE_NODE_NOT_INJECTED, "helpText");
		assert helpPane != null : String.format(MESSAGE_NODE_NOT_INJECTED, "helpPane");
		assert pageNumber != null : String.format(MESSAGE_NODE_NOT_INJECTED, "pageNumber");
		assert topicLabel != null : String.format(MESSAGE_NODE_NOT_INJECTED, "topicLabel");

		setDisplay();
	}

	// -------------------------- SET HELP CONTENT METHODS ----------------------------

	private void setDisplay() {
		setHelpContent();
		setPageNumber();
	}

	private void setHelpContent() {
		setTopicName();
		clearPrevText();
		setCurrText();
		setHelpScrollTop();
	}

	private void setPageNumber() {
		pageNumber.setText(page + "/28");
	}

	private void setTopicName() {
		topicLabel.setText(displayList.get(0));
	}

	private void clearPrevText() {
		helpText.clear();
	}

	private void setCurrText() {
		for (int i = 1; i < displayList.size(); i++) {
			helpText.appendText(displayList.get(i) + "\n");
		}
	}

	private void setHelpScrollTop() {
		Platform.runLater(() -> helpText.setScrollTop(Double.MIN_VALUE));
	}

	// ------------------------------ HANDLE KEYS PRESSED METHODS
	// ----------------------------

	/**
	 * Closes the help pop-up window
	 * 
	 * @param event
	 *            - Enter key pressed
	 */
	@FXML
	public void handleEscPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE) {
			helpStage.close();
		}
	}

	/**
	 * Allows Help Pop-up Content to change with press of left/right arrow key.
	 * Help displayed switches between topics with each press.
	 * 
	 * @param event
	 *            - left or right arrow key pressed
	 */
	@FXML
	public void handleArrowPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.RIGHT && page < 28) {
			page++;
		}
		if (event.getCode() == KeyCode.LEFT && page > 1) {
			page--;
		}
		displayList = MainController.getHelpList(page);
		setDisplay();
	}

}
