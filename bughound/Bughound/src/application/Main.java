package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			// set up the HBox as stated in view/Main.fxml, which is the homepage.
			HBox root = (HBox) FXMLLoader.load(getClass().getClassLoader().getResource("view/Main.fxml"));

			// set up the scene from the HBox root
			Scene scene = new Scene(root);

			// Set the scene and present the scene on the screen
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args); // launch the application
	}
}