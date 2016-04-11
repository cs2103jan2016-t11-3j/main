# A0130622Xunused
###### \bin\GUI\AlertPopup.fxml
``` fxml
  
  <?import javafx.scene.paint.*?>
  <?import javafx.scene.effect.*?>
  <?import javafx.scene.text.*?>
  <?import java.lang.*?>
  <?import javafx.geometry.*?>
  <?import javafx.scene.control.*?>
  <?import javafx.scene.layout.*?>
  <?import javafx.scene.layout.BorderPane?>
  
  <VBox fx:id="alertPane" maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" onKeyPressed="#handleEnterPressed" prefHeight="300.0" prefWidth="300.0" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1" fx:controller="GUI.AlertPopupController">
     <children>
        <ListView fx:id="alertTasks" onKeyReleased="#handleEnterPressed" prefHeight="300.0" prefWidth="300.0">
           <VBox.margin>
              <Insets bottom="7.0" left="7.0" right="7.0" top="7.0" />
           </VBox.margin>
           <padding>
              <Insets bottom="10.0" left="10.0" right="10.0" top="10.0" />
           </padding>
        </ListView>
        <Label alignment="CENTER_RIGHT" prefHeight="22.0" prefWidth="291.0" text="Press &lt;Enter&gt; to close alert" textFill="WHITE">
           <font>
              <Font name="Agency FB" size="18.0" />
           </font>
        </Label>
     </children>
  </VBox>
```
###### \bin\GUI\AlertStyle.css
``` css

#alertPane {
	-fx-background-color: brown;
	-fx-background-radius: 0.5em;
}

#alertTasks {
	-fx-font-size: 17;
	-fx-font-family: 'Agency FB';
}
```
###### \src\GUI\AlertPopup.fxml
``` fxml
  
  <?import javafx.scene.paint.*?>
  <?import javafx.scene.effect.*?>
  <?import javafx.scene.text.*?>
  <?import java.lang.*?>
  <?import javafx.geometry.*?>
  <?import javafx.scene.control.*?>
  <?import javafx.scene.layout.*?>
  <?import javafx.scene.layout.BorderPane?>
  
  <VBox fx:id="alertPane" maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" onKeyPressed="#handleEnterPressed" prefHeight="300.0" prefWidth="300.0" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1" fx:controller="GUI.AlertPopupController">
     <children>
        <ListView fx:id="alertTasks" onKeyReleased="#handleEnterPressed" prefHeight="300.0" prefWidth="300.0">
           <VBox.margin>
              <Insets bottom="7.0" left="7.0" right="7.0" top="7.0" />
           </VBox.margin>
           <padding>
              <Insets bottom="10.0" left="10.0" right="10.0" top="10.0" />
           </padding>
        </ListView>
        <Label alignment="CENTER_RIGHT" prefHeight="22.0" prefWidth="291.0" text="Press &lt;Enter&gt; to close alert" textFill="WHITE">
           <font>
              <Font name="Agency FB" size="18.0" />
           </font>
        </Label>
     </children>
  </VBox>
```
###### \src\GUI\AlertPopupController.java
``` java

package GUI;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.TaskObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class AlertPopupController implements Initializable {

	@FXML
	private ListView<String> alertTasks;
	@FXML
	private VBox alertPane;
	ArrayList<String> output = MainController.getAlertOutput();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setStyle();
		setWrapText();
		setAlertOutput();
		if (output.isEmpty()) {
			alertPane.setVisible(false);
		} else {
			populateAlertList();
			setFocusToAlert();
		}
	}

	private void setWrapText() {
		alertTasks.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			private Text text;

			@Override
			public ListCell<String> call(ListView<String> stringListView) {
				ListCell<String> cell = new ListCell<String>() {

					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (!isEmpty()) {
							text = new Text(item.toString());
							text.wrappingWidthProperty().bind(alertTasks.widthProperty());
							setGraphic(text);
						}
					}
				};
				return cell;
			}
		});
	}

	private void setStyle() {
		URL url = this.getClass().getResource("AlertStyle.css");
		if (url == null) {
			System.out.println("Error: AlertStyle.css stylesheet not found.");
		}
		String css = url.toExternalForm();
		alertPane.getStylesheets().add(css);
	}

	private void setAlertOutput() {
		output = MainController.getAlertOutput();
	}

	private void populateAlertList() {
		ObservableList<String> items = FXCollections.observableArrayList(output);
		alertTasks.setItems(items);
	}

	private void setFocusToAlert() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				alertPane.requestFocus();
			}
		});
	}

	@FXML
	public void handleEnterPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			alertPane.setVisible(false);
		}
	}

}
```
###### \src\GUI\AlertStyle.css
``` css

#alertPane {
	-fx-background-color: brown;
	-fx-background-radius: 0.5em;
}

#alertTasks {
	-fx-font-size: 17;
	-fx-font-family: 'Agency FB';
}
```
