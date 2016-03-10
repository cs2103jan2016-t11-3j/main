package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class HelpPopupController implements Initializable {
	
	static Stage helpStage = new Stage();
	static ArrayList<String> displayList;
	  
	@FXML
	private ListView<String> helpBox;
	
	@FXML
	public void handleEscPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE) {
			helpStage.close();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setHelpContent();
	}
	
	private void setHelpContent() {
		displayList = Controller.getHelpList();
		ObservableList<String> items = FXCollections.observableArrayList(displayList);
		helpBox.setItems(items);
	}

	public void startHelp() throws IOException {		
		Parent help = FXMLLoader.load(getClass().getResource("HelpPopup.fxml"));
		
		helpStage.setScene(new Scene(help));
		helpStage.show();
	}

	

}
