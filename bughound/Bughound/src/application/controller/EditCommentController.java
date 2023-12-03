package application.controller;

import java.time.LocalDateTime;

import application.Comment;
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
import javafx.stage.Stage;

/**
 * The Class EditCommentController.
 */
public class EditCommentController {

	/** The edit comment description field. */
	@FXML
	private TextArea editCommentDescriptionField;
	/** The stage. */

	@FXML
	private ChoiceBox<Ticket> editParentTicket;

	/** The stage. */
	@FXML
	private Stage stage;

	/** The scene. */
	private Scene scene;

	/** The root. */
	private Parent root;

	/** The selected comment. */
	private Comment selectedComment;

	/**
	 * Takes user back to comment page.
	 *
	 * @param event the event that should occur.
	 */
	@FXML
	void handleBack(ActionEvent event) {
		try {
			this.root = FXMLLoader.load(this.getClass().getClassLoader().getResource("view/Comment.fxml"));
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
	 * Handle save comment.
	 *
	 * @param event the event
	 */
	@FXML
	void handleSaveComment(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Missing Input");
		alert.setHeaderText(null);
		alert.setContentText("Please fill out the required fields.");

		if (this.editCommentDescriptionField.getText().isEmpty() || this.editParentTicket.getValue() == null) {
			alert.showAndWait();
			return;
		}
		this.selectedComment = DataModel.getInstance().getSelectedComment();
		Comment editedComment = new Comment(this.editParentTicket.getValue(),
				this.editCommentDescriptionField.getText(), LocalDateTime.now());
		if (DataModel.getInstance().updateCommentInDB(this.selectedComment, editedComment) <= 0) {
			Alert failureAlert = new Alert(AlertType.ERROR);
			failureAlert.setTitle("Failure");
			failureAlert.setHeaderText(null);
			failureAlert.setContentText("Failed to update comment.");
			failureAlert.showAndWait();
		}

		try {
			this.root = FXMLLoader.load(this.getClass().getClassLoader().getResource("view/Comment.fxml"));
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
	 * Sets Comment.
	 */
	public void setCommentInfo() {
		this.selectedComment = DataModel.getInstance().getSelectedComment();

		// Code unlocking ability to move comments to any ticket in any project:
		/*
		 * DataModel dataModel = DataModel.getInstance(); ObservableList<Project>
		 * projects = dataModel.getProjects(); ObservableList<Ticket> existingTickets =
		 * this.editParentTicket.getItems(); for (Project proj : projects) {
		 * ObservableList<Ticket> tickets = dataModel.getTickets(proj);
		 * existingTickets.addAll(tickets); }
		 */

		// Code that only allows moving of comments to tickets in the current project.
		Project parentProject = this.selectedComment.getParentTicket().getParentProject();
		ObservableList<Ticket> projectTickets = this.editParentTicket.getItems();
		projectTickets.addAll(DataModel.getInstance().getTickets(parentProject));
		this.editParentTicket.setValue(this.selectedComment.getParentTicket());
		this.editCommentDescriptionField.setText(this.selectedComment.getDescription());
	}
}
