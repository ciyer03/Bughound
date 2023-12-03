package application.controller;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class EditCommentController {

    @FXML
    private TextArea editCommentDescriptionField;
	/** The stage. */
    
    @FXML
    private ChoiceBox<Ticket> editParentTicket;
    
	@FXML
	private Stage stage;

	/** The scene. */
	private Scene scene;

	/** The root. */
	private Parent root;
	
	private Comment selectedComment;
    
    /**
	 * Takes user back to comment page
	 * 
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
	 * Sets Comment
	 */
	public void setCommentInfo() {
		this.selectedComment = DataModel.getInstance().getSelectedComment();
		DataModel dataModel = DataModel.getInstance();
		ObservableList<Project> projects = dataModel.getProjects();
		ObservableList<Ticket> existingTickets = editParentTicket.getItems();
		for (Project proj: projects) {
			ObservableList<Ticket> tickets = dataModel.getTickets(proj);
			existingTickets.addAll(tickets);
		}
		this.editParentTicket.setValue(this.selectedComment.getParentTicket());
		this.editCommentDescriptionField.setText(this.selectedComment.getDescription());
	}
}

