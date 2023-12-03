package application.controller;

import java.time.LocalDate;

import application.Project;
import application.Ticket;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class EditTicketController.
 */
public class EditTicketController {

	/** The edit bug description field. */
	@FXML
	private TextArea editBugDescriptionField;

	/** The edit bug choice box. */
	@FXML
	private ChoiceBox<Project> editBugChoiceBox;

	/** The edit bug name field. */
	@FXML
	private TextField editBugNameField;

	/** The stage. */
	@FXML
	private Stage stage;

	/** The scene. */
	private Scene scene;

	/** The root. */
	private Parent root;

	/** The selected ticket. */
	private Ticket selectedTicket;

	/**
	 * Handle back.
	 *
	 * @param event the event
	 */
	@FXML
	private void handleBack(ActionEvent event) {
		try {
			this.root = FXMLLoader.load(this.getClass().getClassLoader().getResource("view/Ticket.fxml"));
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
	 * Saves edited ticket information to the database.
	 *
	 * @param event the event
	 */
	@FXML
	void saveEditTicket(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Missing Input");
		alert.setHeaderText(null);
		alert.setContentText("Please fill out the required fields.");

		if (this.editBugNameField.getText().isEmpty() || this.editBugChoiceBox.getValue() == null) {
			alert.showAndWait();
			return;
		}

		this.selectedTicket = DataModel.getInstance().getSelectedTicket();
		Ticket editedTicket = new Ticket(this.editBugChoiceBox.getValue(), this.editBugNameField.getText(),
				this.editBugDescriptionField.getText(), LocalDate.now());

		if (DataModel.getInstance().updateTicketInDB(this.selectedTicket, editedTicket) <= 0) {
			Alert failureAlert = new Alert(AlertType.ERROR);
			failureAlert.setTitle("Failure");
			failureAlert.setHeaderText(null);
			failureAlert.setContentText("Failed to update ticket " + this.selectedTicket.getIssueName());
			failureAlert.showAndWait();
		}

		try {
			this.root = FXMLLoader.load(this.getClass().getClassLoader().getResource("view/Ticket.fxml"));
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
	 * Sets ticket info.
	 */
	public void setTicketInfo() {
		this.selectedTicket = DataModel.getInstance().getSelectedTicket();
		DataModel dataModel = DataModel.getInstance();
		ObservableList<Project> projects = dataModel.getProjects();
		this.editBugChoiceBox.setItems(projects);
		this.editBugChoiceBox.setValue(this.selectedTicket.getParentProject());
		this.editBugNameField.setText(this.selectedTicket.getIssueName());
		this.editBugDescriptionField.setText(this.selectedTicket.getDescription());
	}

}
