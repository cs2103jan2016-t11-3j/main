package GUI;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import logic.Logic;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class UIMain extends Application {
	
	static Logic logic = new Logic();
	Stage window;
	static String input;
	
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
		input = Controller.getInput();
		logic.setUserInput(input);
	}
}
