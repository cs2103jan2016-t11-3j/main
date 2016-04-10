//@@author A0130622X

package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
 * Controls the HelpPopup to allow access to help manual
 * Help topic displayed sorted by command type: Add, Search, Edit, Delete, Undo, Save, Exit.
 * Display topic changes with left/right arrowkeys pressed.
 * Popup closes with Esc pressed
 * 
 * @author Seow Hwee
 *
 */

public class HelpPopupController implements Initializable {
	
	static Stage helpStage = new Stage();
	static ArrayList<String> displayList = MainController.getHelpList(1);
	static int page = 1;
	
	@FXML 
	private VBox helpPane;
	@FXML
	private TextArea helpText;
	@FXML
	private Label topicLabel;
	@FXML
	private Label pageNumber;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		assert helpText != null : "fx:id=\"helpText\" was not injected: check your FXML file 'HelpPopup.fxml'.";
	    assert helpPane != null : "fx:id=\"helpPane\" was not injected: check your FXML file 'HelpPopup.fxml'.";
	    assert pageNumber != null : "fx:id=\"pageNumber\" was not injected: check your FXML file 'HelpPopup.fxml'.";
	    assert topicLabel != null : "fx:id=\"topicLabel\" was not injected: check your FXML file 'HelpPopup.fxml'.";

		setDisplay();
	}

	private void setDisplay() {
		setHelpContent();
		setPageNumber();
	}
	
	private void setHelpContent() {

		topicLabel.setText(displayList.get(0));
		helpText.clear();
		
		for (int i = 1; i < displayList.size(); i++) {
			helpText.appendText(displayList.get(i) + "\n");
		}
		
		Platform.runLater(() -> helpText.setScrollTop(Double.MIN_VALUE));
	}
	
	private void setPageNumber() {
		pageNumber.setText(page + "/7");
	}
	
	/**
	 * Closes the help pop-up window
	 * @param event - Enter key pressed
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
	 * @param event - left or right arrow key pressed
	 */
	@FXML
	public void handleArrowPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.RIGHT && page < 7) {
			page++;
		}
		if (event.getCode() == KeyCode.LEFT && page > 1) {
			page--;		
		}
		displayList = MainController.getHelpList(page);	
		setDisplay();
	}

	/**
	 * Called by MainController to activate help pop-up.
	 * Function starts and shows the help pop-up window.
	 * @throws IOException unable to load fxml file
	 */
	public void startHelp() throws IOException {		
		Parent help = FXMLLoader.load(getClass().getResource("HelpPopup.fxml"));
		Scene helpScene = new Scene(help);
		helpStage.setScene(helpScene);
		
		setStyle(helpScene);
		helpStage.show();
	}
	
	private void setStyle(Scene scene) {
		URL url = this.getClass().getResource("HelpStyle.css");
		if (url == null) {	
			System.out.println("Error: HelpStyle.css stylesheet not found.");       
		}
		String css = url.toExternalForm(); 
		scene.getStylesheets().add(css);
	}

}
