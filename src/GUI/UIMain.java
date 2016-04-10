//@@author A0130622X

package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

import common.AtfLogger;
import common.TaskObject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import logic.Logic;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Launches the program, initiating the TaskWindow users interact with Creates
 * Logic object within class to interact with backend component Passes input
 * from user to logic Gets output from logic after input command run in logic
 * 
 * @author Seow Hwee
 *
 */

public class UIMain extends Application {

	public static final String MESSAGE_INVALID_STYLESHEET = "Error: MainStyle.css stylesheet not found.";

	private static Logic logic = new Logic();
	private static Stage window;
	private static ArrayList<TaskObject> taskList;
	
	private static Logger logger = AtfLogger.getLogger();
	
	/**
	 * Starts and loads the program, shows the window. 
	 * Main window title set as AdultTaskFinder.
	 * 
	 */
	
	@Override
	public void start(Stage primaryStage) {
		window = primaryStage;
		Parent root;
		
		try {
			root = FXMLLoader.load(getClass().getResource("TaskWindow.fxml"));
			Scene scene = new Scene(root, 720, 500);
			setStyle(scene);
			window.setTitle("AdultTaskFinder");
			window.setScene(scene);
			window.show();
		} catch (IOException e) {
			logger.warning("unable to load TaskWindow.fxml file");
			e.printStackTrace();
		}
	}
	
	//load style from stylesheet
	private void setStyle(Scene scene) {
		URL url = this.getClass().getResource("MainStyle.css");
		if (url == null) {
			System.out.println(MESSAGE_INVALID_STYLESHEET);
		}
		String css = url.toExternalForm();
		scene.getStylesheets().add(css);
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Called by MainController to pass input to logic for processing
	 * 
	 * @param input
	 *            - user input from textfield
	 */
	public void passInput(String input) {
		logic.run(input);
	}	
	
	//---------------------------------- GETTERS ----------------------------------------
	/**
	 * Called by MainController to get feedback message to display in feedback
	 * box. Output size = 0 indicates feedback message stored.
	 * 
	 * @return output.get(0)
	 */
	public String getMessage() {
		
		ArrayList<String> output = logic.getOutput();
		assert output != null : "Output = null, check logic output message";

		if (output.size() == 1) {
			return output.get(0);
		} else {
			return "";
		}
	}

	/**
	 * Called by MainController to get last output task list to display in table
	 * view.
	 * 
	 * @return logic.getLastOutputTaskList();
	 */
	public ArrayList<TaskObject> getLastOutputTaskList() {
		
		taskList = logic.getLastOutputTaskList();
		assert taskList != null : "Output = null, check logic lastOutputTaskList";

		return taskList;
	}

	/**
	 * Called by MainController to get output generated after processing user
	 * input. Used mainly to obtain help manual.
	 * 
	 * @return output - output from logic containing help manual
	 */
	public ArrayList<String> getOutput() {
		
		ArrayList<String> output = logic.getOutput();
		assert output != null : "Output = null, check logic output message";

		return output;
	}

	/**
	 * Called by MainController to get index of added task for focus.
	 * 
	 * @return logic.getSortedIndex()
	 */
	public int getAddSortedIndex() {
		return logic.getSortedIndex();
	}

	/**
	 * Called by MainController to obtain array list of task timings.
	 * 
	 * @return logic.getTaskDateTimeOutput()
	 */
	public ArrayList<String> getTaskDateOutput() {
		return logic.getTaskDateTimeOutput();
	}

}
