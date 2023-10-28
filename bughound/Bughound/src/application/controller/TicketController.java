package application.controller;

import java.net.URL;
import java.util.ResourceBundle;
import application.Project;
import application.Ticket;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;

public class TicketController implements Initializable {
	
	@FXML
	private Stage stage;
	private Scene scene;
	private Parent root;
	
    @FXML
    private ChoiceBox<String> bugChoiceBox;

    @FXML
    private TextArea bugDescriptionField;

    @FXML
    private TextField bugNameField;
    
    @FXML
	private TableView<Ticket> ticketTable;
	@FXML
	private TableColumn<Ticket, String> bugProjectName;
	@FXML
	private TableColumn<Ticket, String> bugName;
	@FXML
	private TableColumn<Ticket, String> bugDescription;

	
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	this.bugProjectName.setCellValueFactory(new PropertyValueFactory<Ticket, String>("parentProject"));
		this.bugName.setCellValueFactory(new PropertyValueFactory<Ticket, String>("issueName"));
		this.bugDescription.setCellValueFactory(new PropertyValueFactory<Ticket, String>("description"));
    	DataModel dataModel = DataModel.getInstance();
    	ObservableList<Project> projects = dataModel.getProjects();
		for (Project project: projects) {
			bugChoiceBox.getItems().add(project.getName());
		}
		VBox customPlaceholder = new VBox(new Label("No existing tickets"));
		customPlaceholder.setAlignment(Pos.CENTER);
		this.ticketTable.setPlaceholder(customPlaceholder);
	}
    
    
    @FXML
    void handleSubmitTicket(ActionEvent event) {
    	Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Missing Input");
		alert.setHeaderText(null);
		alert.setContentText("Please fill out the required fields.");

		if (this.bugNameField.getText().isEmpty() || this.bugChoiceBox.getValue() == null) {
			alert.showAndWait();
			return;
		}
		Project selectedProject = null;
		DataModel dataModel = DataModel.getInstance();
    	ObservableList<Project> projects = dataModel.getProjects();
    	for (Project project: projects) {
			if (bugChoiceBox.getValue() == project.getName()) {
				selectedProject = project;
			}
		}
		Ticket ticket = new Ticket(selectedProject, this.bugNameField.getText(), this.bugDescriptionField.getText(), LocalDate.now());
		
    }
    
    @FXML
    void handleRemoveProject(ActionEvent event) {

    }
    
    /**
	 * Takes the user back to the homepage.
	 *
	 * @param event the event that should occur.
	 */
	@FXML
	private void handleBack(ActionEvent event) {
		try {
			this.root = FXMLLoader.load(this.getClass().getClassLoader().getResource("view/Home.fxml"));
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

