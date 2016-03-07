package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
	private TextArea helpMessageBox;
	
	@FXML
	public void handleEscPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE) {
			helpStage.close();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
	}
	
	public void setHelpStage(ArrayList<String> helpManualList) throws IOException {		
		Parent help = FXMLLoader.load(UIMain.class.getResource("HelpPopup.fxml"));
		helpStage.setScene(new Scene(help));
		
		displayList = helpManualList;
		helpStage.show();
		
	}

	

}
