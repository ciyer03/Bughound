package application.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import application.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Handles the events that occur on the Project Creation page.
 */
public class ProjectController implements Initializable {

	/** The stage. */
	@FXML
	private Stage stage;

	/** The scene. */
	private Scene scene;

	/** The root. */
	private Parent root;

	/** The project table. */
	@FXML
	private TableView<Project> projectTable;

	/** The project date. */
	@FXML
	private TableColumn<Project, Date> projectDate;

	/** The project description. */
	@FXML
	private TableColumn<Project, String> projectDescription;

	/** The project name. */
	@FXML
	private TableColumn<Project, String> projectName;

	/** The project name field. */
	@FXML
	private TextField projectNameField;

	/** The project date field. */
	@FXML
	private DatePicker projectDateField;

	/** The project description field. */
	@FXML
	private TextArea projectDescriptionField;

	/** The project search field. */
	@FXML
	private TextField searchProjects;

	/** The projects. */
	private ObservableList<Project> projects = FXCollections.observableArrayList();

	/** The searched projects. */
	private FilteredList<Project> filteredProjects;

	/**
	 * Initialize the "Create Project" page.
	 *
	 * @param url      the url of the page
	 * @param resource the resource bundles required.
	 */
	@Override
	public void initialize(URL url, ResourceBundle resource) {
		this.updateTable();
		this.searchProject();
	}

	/**
	 * Creates the project with the user supplied information on the textboxes, to
	 * the database.
	 *
	 * @param event the event that should occur.
	 */
	@FXML
	private void handleSubmitProject(ActionEvent event) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Missing Input");
		alert.setHeaderText(null);
		alert.setContentText("Please fill out the required fields.");

		if (this.projectNameField.getText().isEmpty() || this.projectDateField.getValue() == null) {
			alert.showAndWait();
			return;
		}

		Project project = new Project(this.projectNameField.getText(), this.projectDateField.getValue(),
				this.projectDescriptionField.getText());
		DataModel.getInstance().getProjects().add(project);
		this.projects.add(project);
		this.projectNameField.clear();
		this.projectDateField.setValue(LocalDate.now());
		this.projectDescriptionField.clear();

		if (DataModel.getInstance().addProjectToDB(project) < 0) {
			Alert failureAlert = new Alert(AlertType.ERROR);
			failureAlert.setTitle("Failure");
			failureAlert.setHeaderText(null);
			failureAlert.setContentText("Failed to add " + project.getName());
			failureAlert.showAndWait();
		}
		this.updateTable();
		this.updateFilteredProjects();
	}

	/**
	 * Removes the selected project.
	 *
	 * @param event the event that should occur.
	 */
	@FXML
	private void handleRemoveProject(ActionEvent event) {
		int selectedID = this.projectTable.getSelectionModel().getSelectedIndex();
		if (selectedID == -1) {
			return;
		}

		Project removedProject = DataModel.getInstance().getProjects().remove(selectedID);
		this.projects.remove(removedProject);
		if (DataModel.getInstance().removeProjectFromDB(removedProject) < 0) {
			Alert failureAlert = new Alert(AlertType.ERROR);
			failureAlert.setTitle("Failure");
			failureAlert.setHeaderText(null);
			failureAlert.setContentText("Failed to remove " + removedProject.getName());
			failureAlert.showAndWait();
		}
		this.updateTable();
		this.updateFilteredProjects();
	}

	/**
	 * Update filtered projects.
	 */
	private void updateFilteredProjects() {
		this.filteredProjects = new FilteredList<>(this.projects, b -> true);
		this.searchProject();
	}

	/**
	 * Searches projects table.
	 */
	@FXML
	private void searchProject() {
		if (!(this.projects.isEmpty())) {
			this.filteredProjects = new FilteredList<>(this.projects, b -> true);
			this.searchProjects.textProperty().addListener((observable, oldValue, newValue) -> {
				this.filteredProjects.setPredicate(project -> {
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}
					String lowerCaseFilter = newValue.toLowerCase();
					if (project.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
						return true;
					}
					return false;
				});
			});
			SortedList<Project> sorted = new SortedList<>(this.filteredProjects);
			sorted.comparatorProperty().bind(this.projectTable.comparatorProperty());
			this.projectTable.setItems(sorted);
		}
	}

	/**
	 * Updates projects table.
	 */
	@FXML
	private void updateTable() {
		this.projectName.setCellValueFactory(new PropertyValueFactory<Project, String>("name"));
		this.projectDate.setCellValueFactory(new PropertyValueFactory<Project, Date>("date"));
		this.projectDescription.setCellValueFactory(new PropertyValueFactory<Project, String>("description"));
		this.projectDateField.setValue(LocalDate.now());
		VBox customPlaceholder = new VBox(new Label("No existing projects"));
		customPlaceholder.setAlignment(Pos.CENTER);
		this.projectTable.setPlaceholder(customPlaceholder);
		this.projects.setAll(DataModel.getInstance().getProjects());
		this.projectTable.setItems(this.projects);
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
