package application.controller;

import java.net.URL;
import java.util.ResourceBundle;
import application.Ticket;
import application.Comment;
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
import javafx.scene.control.Label;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.*; 

public class CommentController implements Initializable {
	
	@FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    private TextArea commentDescriptionField;
    
    @FXML
    private DatePicker commentTimestamp;
    
    @FXML
    private TableView<Comment> commentTable;
    @FXML
    private TableColumn<Comment, LocalDateTime> timeStamp;
    @FXML
    private TableColumn<Comment, String> commentDescription;
    private ObservableList<Comment> comments = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timeStamp.setCellValueFactory(new PropertyValueFactory<Comment, LocalDateTime>("timeOfCreation"));
        commentDescription.setCellValueFactory(new PropertyValueFactory<Comment, String>("description"));
        VBox customPlaceholder = new VBox(new Label("No existing comments for this ticket"));
        customPlaceholder.setAlignment(Pos.CENTER);
        commentTable.setPlaceholder(customPlaceholder);
    }
    
    @FXML
    void handleSubmitComment(ActionEvent event) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Missing Input");
        alert.setHeaderText(null);
        alert.setContentText("Please fill out the required fields.");

        if (commentDescriptionField.getText().isEmpty()) {
            alert.showAndWait();
            return;
        }
		
        Ticket ticket = null;
        Comment comment = new Comment(ticket, commentDescriptionField.getText(), LocalDateTime.now());
        comments.add(comment);
        commentTable.setItems(comments);
        commentDescriptionField.clear();
        
    }

    @FXML
    void handleRemoveComment(ActionEvent event) {
        int selectedID = commentTable.getSelectionModel().getSelectedIndex();
        if (selectedID == -1) {
            return;
        }
        commentTable.getItems().remove(selectedID);
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