package GUI;
	
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import logic.Logic;
import logic.TaskObject;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class UIMain extends Application {
	
	static Logic logic = new Logic();
	Stage window;
	static String input;
	static ArrayList<TaskObject> taskList;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		Parent root = FXMLLoader.load(getClass().getResource("UIScene.fxml"));
		Scene scene = new Scene(root, 650, 500);
		
		window.setTitle("Adult TaskFinder");
		window.setScene(scene);
		window.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
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

	public String getOutput() {
		
		ArrayList<String> output = logic.getOutput();
		if(output.size() == 1) {
			return output.get(0);
		}
		return "";
	}
}
