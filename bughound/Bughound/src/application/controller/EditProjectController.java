package application.controller;

import application.Project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class EditProjectController.
 */
public class EditProjectController {

	/** The edit project date field. */
	@FXML
	private DatePicker editProjectDateField;

	/** The edit project description field. */
	@FXML
	private TextArea editProjectDescriptionField;

	/** The edit project name field. */
	@FXML
	private TextField editProjectNameField;

	/** The selected project. */
	private Project selectedProject;

	/** The stage. */
	@FXML
	private Stage stage;

	/** The scene. */
	private Scene scene;

	/** The root. */
	private Parent root;

	/**
	 * Handle back.
	 *
	 * @param event the event
	 */
	@FXML
	private void handleBack(ActionEvent event) {
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
	 * Save edit project.
	 *
	 * @param event the event
	 */
	@FXML
	private void saveEditProject(ActionEvent event) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Missing Input");
		alert.setHeaderText(null);
		alert.setContentText("Please fill out the required fields.");

		if (this.editProjectNameField.getText().isEmpty() || this.editProjectDateField.getValue() == null) {
			alert.showAndWait();
			return;
		}

		this.selectedProject = DataModel.getInstance().getSelectedProject();
		Project editedProject = new Project(this.editProjectNameField.getText(), this.editProjectDateField.getValue(),
				this.editProjectDescriptionField.getText());
		if (DataModel.getInstance().updateProjectInDB(this.selectedProject, editedProject) <= 0) {
			Alert failureAlert = new Alert(AlertType.ERROR);
			failureAlert.setTitle("Failure");
			failureAlert.setHeaderText(null);
			failureAlert.setContentText("Failed to update project " + this.selectedProject.getName());
			failureAlert.showAndWait();
		}

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
	 * Sets the info.
	 */
	public void setInfo() {
		this.selectedProject = DataModel.getInstance().getSelectedProject();
		this.editProjectNameField.setText(this.selectedProject.getName());
		this.editProjectDateField.setValue(this.selectedProject.getDate());
		this.editProjectDescriptionField.setText(this.selectedProject.getDescription());
	}
}
