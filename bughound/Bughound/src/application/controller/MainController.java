package application.controller;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class MainController {
	@FXML
	HBox mainBox;

	@FXML
	public void createProjectOp() {
		// Set the resource to load to view/Project_page.fxml, the page for creating a
		// new project.
		URL url = getClass().getClassLoader().getResource("view/Project_Page.fxml");

		try {
			// Create an AnchorPane from the aforementioned resource 'url'.
			AnchorPane pane1 = (AnchorPane) FXMLLoader.load(url);

			// Add the newly created anchor pane to the main HBox.
			mainBox.getChildren().add(pane1);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}