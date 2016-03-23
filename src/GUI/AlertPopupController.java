package GUI;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class AlertPopupController implements Initializable {
    
	@FXML
    private ListView<String> alertTasks;
    @FXML
    private VBox alertPane;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		populateList();
		alertPane.requestFocus();
	}

	private void populateList() {
		ArrayList<String> output = MainController.getAlertOutput();
		ObservableList<String> items =FXCollections.observableArrayList(output);
		
		alertTasks.setItems(items);
	}
	@FXML
	public void handleEnterPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			alertPane.setVisible(false);
			System.out.println("hidden");
		}
	}

}
