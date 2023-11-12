package application.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Controls the interactions that happen on the homepage of the application.
 */
public class MainController {

	/** The stage. */
	@FXML
	private Stage stage;

	/** The scene. */
	private Scene scene;

	/** The root. */
	private Parent root;

	/**
	 * Takes the user to the New Project Creation page.
	 *
	 * @param event the event that should occur.
	 */
	@FXML
	private void handleCreateProject(ActionEvent event) {
		try {
			this.root = FXMLLoader.load(this.getClass().getClassLoader().getResource("view/Project.fxml"));
			this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			this.scene = new Scene(this.root);
			this.scene.getStylesheets().add(this.getClass().getResource("/css/application.css").toExternalForm());
			this.stage.setScene(this.scene);
			this.stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handle create ticket.
	 *
	 * @param event the event
	 */
	@FXML
	private void handleCreateTicket(ActionEvent event) {
		try {
			if (!(DataModel.getInstance().getProjects().isEmpty())) {
				this.root = FXMLLoader.load(this.getClass().getClassLoader().getResource("view/Ticket.fxml"));
				this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				this.scene = new Scene(this.root);
				this.scene.getStylesheets().add(this.getClass().getResource("/css/application.css").toExternalForm());
				this.stage.setScene(this.scene);
				this.stage.show();
				return;
			}
			Alert failureAlert = new Alert(AlertType.ERROR);
			failureAlert.setTitle("Failure");
			failureAlert.setHeaderText(null);
			failureAlert.setContentText("Please add at least one Project before attempting to add tickets.");
			failureAlert.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
