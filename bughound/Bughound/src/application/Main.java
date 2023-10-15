package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Sets up the stage for the application.
 */
public class Main extends Application {

	/**
	 * Start.
	 *
	 * @param primaryStage the primary stage
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(this.getClass().getClassLoader().getResource("view/Home.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(this.getClass().getResource("/css/application.css").toExternalForm());
			primaryStage.setTitle("Bughound");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments to be passed, if any.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
