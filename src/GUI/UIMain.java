package GUI;
	
import java.io.IOException;
import java.util.ArrayList;

import common.TaskObject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import logic.Logic;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Launches the program, initiating the TaskWindow users interact with
 * Creates Logic object within class to interact with backend component
 * Passes input from user to logic
 * Gets output from logic after input command run in logic
 * 
 * @author Seow Hwee
 *
 */

public class UIMain extends Application {
	
	static Logic logic = new Logic();
	Stage mainWindow;
	static String input;
	static ArrayList<TaskObject> taskList;
	AlertPopupController alertPopup;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		startMain(primaryStage);
		startAlertPopup();
	}

	private void startMain(Stage primaryStage) throws IOException {
		mainWindow = primaryStage;
		Parent root = FXMLLoader.load(getClass().getResource("TaskWindow.fxml"));
		Scene scene = new Scene(root, 650, 500);
		
		mainWindow.setTitle("Adult TaskFinder");
		mainWindow.setScene(scene);
		mainWindow.show();
	}	
	
	
	private void startAlertPopup() throws IOException {
		Parent alert = FXMLLoader.load(getClass().getResource("AlertPopup.fxml"));

		Stage alertStage = new Stage();
		alertStage.setScene(new Scene(alert, 300, 200));
		alertStage.showAndWait();
	}

	
	public static void main(String[] args) {
		launch(args);
	}
	
	//not used
	public ArrayList<TaskObject> getTaskList() {
		taskList = logic.getTaskList();
		return taskList;
	}

	public void passInput(String input) {
		logic.run(input);
		printOutput();
	}
	
	private static void printOutput() {
		ArrayList<String> output = logic.getOutput();
		for (int i = 0; i < output.size(); i++) {
			System.out.println(output.get(i));		
		}
	}

	public String getMessage() {	
		ArrayList<String> output = logic.getOutput();
		assert output != null: "Output = null, check logic output message";
		
		if(output.size() == 1) {
			return output.get(0);
		}
		return "";
	}

	public ArrayList<TaskObject> getLastOutputTaskList() {
		taskList = logic.getLastOutputTaskList();
		
		assert taskList != null: "Output = null, check logic lastOutputTaskList";
		
		return taskList;
	}

	public ArrayList<String> getHelpOutput() {
		return logic.getOutput();
	}
	
	public ArrayList<String> getAlertOutput() {
		return logic.getAlertOutput();
	}
	

}
