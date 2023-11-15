package application.controller;
import application.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class EditProjectController {

    @FXML
    private DatePicker editProjectDateField;

    @FXML
    private TextArea editProjectDescriptionField;

    @FXML
    private TextField editProjectNameField;
    
    private Project selectedProject;
    
    private ObservableList<Project> projects = FXCollections.observableArrayList();

    /** The stage. */
	@FXML
	private Stage stage;

	/** The scene. */
	private Scene scene;

	/** The root. */
	private Parent root;
	
	
    @FXML
    void handleBack(ActionEvent event) {
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

    @FXML
    void saveEditProject(ActionEvent event) {
    	Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Missing Input");
		alert.setHeaderText(null);
		alert.setContentText("Please fill out the required fields.");

		if (this.editProjectNameField.getText().isEmpty() || this.editProjectDateField.getValue() == null) {
			alert.showAndWait();
			return;
		}
		this.projects.setAll(DataModel.getInstance().getProjects());
		
		
		/**
		 * Code here to update project. The selectedProject variable is already set to the selected project
		 */
		
		
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
    
    public void setInfo(Project proj) {
    	this.selectedProject = proj;
    	this.editProjectNameField.setText(proj.getName());
    	this.editProjectDateField.setValue(proj.getDate());
    	this.editProjectDescriptionField.setText(proj.getDescription());
    }

}
