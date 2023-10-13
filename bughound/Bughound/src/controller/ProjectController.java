package controller;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import application.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;

public class ProjectController implements Initializable {
	
	@FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TableView<Project> projectTable;
    @FXML
    private TableColumn<Project, Date> projectDate;
    @FXML
    private TableColumn<Project, String> projectDescription;
    @FXML
    private TableColumn<Project, String> projectName;
    @FXML
    private TextField projectNameField;
    @FXML
    private DatePicker projectDateField;
    @FXML
    private TextArea projectDescriptionField;
    
    private ObservableList<Project> projects = FXCollections.observableArrayList(); 
    
    @Override
    public void initialize(URL url, ResourceBundle resource) {
    	projectName.setCellValueFactory(new PropertyValueFactory<Project, String>("name"));
    	projectDate.setCellValueFactory(new PropertyValueFactory<Project, Date>("date"));
    	projectDescription.setCellValueFactory(new PropertyValueFactory<Project, String>("description"));
    	projectDateField.setValue(LocalDate.now());
    	VBox customPlaceholder = new VBox(new Label("No existing projects"));
    	customPlaceholder.setAlignment(Pos.CENTER);
    	projectTable.setPlaceholder(customPlaceholder);
    	projects.setAll(DataModel.getInstance().getProjects());
    	projectTable.setItems(projects);
    }
    
    @FXML
    void handleSubmitProject(ActionEvent event) {
    	Alert alert = new Alert(AlertType.WARNING);
    	alert.setTitle("Missing Input");
    	alert.setHeaderText(null);
    	alert.setContentText("Please fill out the required fields.");
    	if (projectNameField.getText().isEmpty() || projectDateField.getValue() == null) {
    		alert.showAndWait();
    		return;
    	}
    	Project project = new Project(projectNameField.getText(), projectDateField.getValue(), projectDescriptionField.getText());
    	projects = projectTable.getItems();
    	projects.add(project);
    	projectTable.setItems(projects);
    	projectNameField.clear();
    	projectDateField.setValue(LocalDate.now());
    	projectDescriptionField.clear();
        DataModel.getInstance().getProjects().add(project);

    }
    
    @FXML
    void handleBack(ActionEvent event) {
    	try {
    		  root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Home.fxml"));
    		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		  scene = new Scene(root);
    		  scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
    		  stage.setScene(scene);
    		  stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void handleRemoveProject(ActionEvent event) {
    	int selectedID = projectTable.getSelectionModel().getSelectedIndex();
    	if (selectedID == -1 ) {
    		return;
    	}
        projectTable.getItems().remove(selectedID);
        DataModel.getInstance().getProjects().remove(selectedID);
    }
    
}
