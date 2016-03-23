package GUI;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class AlertPopupController implements Initializable {
    
	@FXML
    private ListView<String> alertTasks;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		populateList();
	}

	private void populateList() {
		ArrayList<String> output = MainController.getAlertOutput();
		ObservableList<String> items =FXCollections.observableArrayList(output);
		
		alertTasks.setItems(items);
	}

}
