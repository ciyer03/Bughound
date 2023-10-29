package application.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import application.Project;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The Class TicketController.
 */
public class TicketController implements Initializable {

	/** The stage. */
	@FXML
	private Stage stage;

	/** The scene. */
	private Scene scene;

	/** The root. */
	private Parent root;

	/** The bug choice box. */
	@FXML
	private ChoiceBox<Project> bugChoiceBox;

	/** The bug description field. */
	@FXML
	private TextArea bugDescriptionField;

	/** The bug name field. */
	@FXML
	private TextField bugNameField;

	/** The ticket table. */
	@FXML
	private TableView<Ticket> ticketTable;

	/** The bug project name. */
	@FXML
	private TableColumn<Ticket, Project> bugProjectName;

	/** The bug name. */
	@FXML
	private TableColumn<Ticket, String> bugName;

	/** The bug description. */
	@FXML
	private TableColumn<Ticket, String> bugDescription;

	/** The tickets. */
	private ObservableList<Ticket> tickets = FXCollections.observableArrayList();

	/**
	 * Initialize.
	 *
	 * @param location  the location
	 * @param resources the resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		DataModel dataModel = DataModel.getInstance();
		ObservableList<Project> projects = dataModel.getProjects();
		this.bugProjectName.setCellValueFactory(new PropertyValueFactory<Ticket, Project>("parentProject"));
		this.bugName.setCellValueFactory(new PropertyValueFactory<Ticket, String>("issueName"));
		this.bugDescription.setCellValueFactory(new PropertyValueFactory<Ticket, String>("description"));
		this.bugChoiceBox.setItems(projects);
		VBox customPlaceholder = new VBox(new Label("No existing tickets"));
		customPlaceholder.setAlignment(Pos.CENTER);
		this.ticketTable.setPlaceholder(customPlaceholder);

		this.bugChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				ObservableList<Ticket> projectTickets = dataModel.getTickets(newValue);
				this.tickets.setAll(projectTickets);
				this.ticketTable.setItems(this.tickets);
			}
		});
	}

	/**
	 * Handle submit ticket.
	 *
	 * @param event the event
	 */
	@FXML
	void handleSubmitTicket(ActionEvent event) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Missing Input");
		alert.setHeaderText(null);
		alert.setContentText("Please fill out the required fields.");

		if (this.bugNameField.getText().isEmpty() || this.bugChoiceBox.getValue() == null) {
			alert.showAndWait();
			return;
		}

		Ticket ticket = new Ticket(this.bugChoiceBox.getValue(), this.bugNameField.getText(),
				this.bugDescriptionField.getText(), LocalDate.now());
		this.tickets.add(ticket);

		if (DataModel.getInstance().addTicketToDB(ticket) < 0) {
			Alert failureAlert = new Alert(AlertType.ERROR);
			failureAlert.setTitle("Failure");
			failureAlert.setHeaderText(null);
			failureAlert.setContentText("Failed to add " + ticket.getIssueName());
			failureAlert.showAndWait();
			return;
		}
		DataModel.getInstance().getTickets(this.bugChoiceBox.getValue()).add(ticket);
		this.ticketTable.setItems(this.tickets);
		this.bugNameField.clear();
		this.bugDescriptionField.clear();
	}

	/**
	 * Handle add comment.
	 *
	 * @param event the event
	 */
	@FXML
	void handleAddComment(ActionEvent event) {
		try {
			Ticket selectedTicket = this.ticketTable.getSelectionModel().getSelectedItem();
			if (!this.tickets.isEmpty()) {
				if (selectedTicket == null) {
					Alert failureAlert = new Alert(AlertType.ERROR);
					failureAlert.setTitle("Failure");
					failureAlert.setHeaderText(null);
					failureAlert.setContentText("Please select a Ticket before attempting to add comments.");
					failureAlert.showAndWait();
					return;
				}

				DataModel.getInstance().setTicket(selectedTicket);
				this.root = FXMLLoader.load(this.getClass().getClassLoader().getResource("view/Comment.fxml"));

				this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				this.scene = new Scene(this.root);
				this.scene.getStylesheets().add(this.getClass().getResource("/css/application.css").toExternalForm());
				this.stage.setScene(this.scene);
				this.stage.show();
				return;
			}

			Alert failureAlert = new Alert(AlertType.ERROR);
			failureAlert.setTitle("Failure");
			failureAlert.setHeaderText(null);
			failureAlert.setContentText("Please add at least one Ticket before attempting to add comments.");
			failureAlert.showAndWait();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handle remove project.
	 *
	 * @param event the event
	 */
	@FXML
	void handleRemoveTicket(ActionEvent event) {
		int selectedID = this.ticketTable.getSelectionModel().getSelectedIndex();
		if (selectedID == -1) {
			return;
		}

		Ticket removedTicket = this.ticketTable.getItems().remove(selectedID);
		DataModel.getInstance().getTickets(removedTicket.getParentProject()).remove(removedTicket);
		this.tickets.remove(removedTicket);
		if (DataModel.getInstance().removeTicketFromDB(removedTicket) < 0) {
			Alert failureAlert = new Alert(AlertType.ERROR);
			failureAlert.setTitle("Failure");
			failureAlert.setHeaderText(null);
			failureAlert.setContentText("Failed to remove " + removedTicket.getIssueName());
			failureAlert.showAndWait();
		}
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

	/**
	 * Pass ticket.
	 *
	 * @return the ticket
	 */
	public Ticket passTicket() {
		return this.ticketTable.getSelectionModel().getSelectedItem();
	}
}
