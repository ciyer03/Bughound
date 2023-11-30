package application.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import application.Comment;
import application.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The Class CommentController.
 */
public class CommentController implements Initializable {

	/** The selected ticket. */
	private Ticket selectedTicket;
	/** The stage. */
	@FXML
	private Stage stage;

	/** The scene. */
	private Scene scene;

	/** The root. */
	private Parent root;

	/** The comment description field. */
	@FXML
	private TextArea commentDescriptionField;

	/** The comment table. */
	@FXML
	private TableView<Comment> commentTable;

	/** The time stamp. */
	@FXML
	private TableColumn<Comment, LocalDateTime> timeStamp;

	/** The comment description. */
	@FXML
	private TableColumn<Comment, String> commentDescription;

	/** The comments. */
	private ObservableList<Comment> comments = FXCollections.observableArrayList();

	/**
	 * Initialize.
	 *
	 * @param location  the location
	 * @param resources the resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.selectedTicket = DataModel.getInstance().getSelectedTicket();
		this.timeStamp.setCellValueFactory(new PropertyValueFactory<Comment, LocalDateTime>("timestamp"));
		this.commentDescription.setCellValueFactory(new PropertyValueFactory<Comment, String>("description"));
		VBox customPlaceholder = new VBox(new Label("No existing comments for this ticket"));
		customPlaceholder.setAlignment(Pos.CENTER);
		this.commentTable.setPlaceholder(customPlaceholder);

		ObservableList<Comment> ticketComments = DataModel.getInstance().getComments(this.selectedTicket);

		if (!(ticketComments.isEmpty())) {
			this.comments.setAll(ticketComments);
			this.commentTable.setItems(this.comments);
		}
	}

	/**
	 * Handle submit comment.
	 *
	 * @param event the event
	 */
	@FXML
	private void handleSubmitComment(ActionEvent event) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Missing Input");
		alert.setHeaderText(null);
		alert.setContentText("Please fill out the required fields.");

		if (this.commentDescriptionField.getText().isEmpty()) {
			alert.showAndWait();
			return;
		}

		Comment comment = new Comment(this.selectedTicket, this.commentDescriptionField.getText(), LocalDateTime.now());
		this.comments.add(comment);

		if (DataModel.getInstance().addCommentToDB(comment) <= 0) {
			Alert failureAlert = new Alert(AlertType.ERROR);
			failureAlert.setTitle("Failure");
			failureAlert.setHeaderText(null);
			failureAlert.setContentText("Failed to add comment.");
			failureAlert.showAndWait();
			return;
		}
		DataModel.getInstance().getComments(this.selectedTicket).add(comment);

		this.commentTable.setItems(this.comments);
		this.commentDescriptionField.clear();
	}

	/**
	 * Handle remove comment.
	 *
	 * @param event the event
	 */
	@FXML
	private void handleRemoveComment(ActionEvent event) {
		int selectedID = this.commentTable.getSelectionModel().getSelectedIndex();
		if (selectedID == -1) {
			return;
		}
		Comment removedComment = this.commentTable.getItems().remove(selectedID);
		DataModel.getInstance().getComments(removedComment.getParentTicket()).remove(removedComment);
		this.comments.remove(removedComment);
		if (DataModel.getInstance().removeCommentFromDB(removedComment) <= 0) {
			Alert failureAlert = new Alert(AlertType.ERROR);
			failureAlert.setTitle("Failure");
			failureAlert.setHeaderText(null);
			failureAlert.setContentText("Failed to remove comment");
			failureAlert.showAndWait();
		}
	}

	/**
	 * Takes the user back to the ticket page.
	 *
	 * @param event the event that should occur.
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
}