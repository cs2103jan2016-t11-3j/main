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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class HelpPopupController implements Initializable {
	
	static Stage helpStage = new Stage();
	static ArrayList<String> displayList;
	  
	@FXML
	private TextFlow helpBox;
	
	@FXML
	public void handleEscPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE) {
			helpStage.close();
		}
	}
	
	@FXML 
	public void handleArrowPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.RIGHT) {
			
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setHelpContent();
	}
	
	private void setHelpContent() {
		displayList = Controller.getHelpList(1);
		for (int i = 0; i < displayList.size(); i++) {
			Text line = new Text(displayList.get(i) + "\n");
			helpBox.getChildren().add(line);
		}
	}

	public void startHelp() throws IOException {		
		Parent help = FXMLLoader.load(getClass().getResource("HelpPopup.fxml"));
		
		helpStage.setScene(new Scene(help));
		helpStage.show();
	}

	

}
