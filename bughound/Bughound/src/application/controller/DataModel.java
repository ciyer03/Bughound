package application.controller;

import application.Project;
import application.Ticket;
import application.database.ProjectDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Tracks and keeps a record of created projects.
 */
public class DataModel {

	/** The Constant instance. */
	private static final DataModel instance = new DataModel();

	/** The projects. */
	private ObservableList<Project> projects;

	/** The tickets. */
	private ObservableList<Ticket> tickets;

	/** The db. */
	private ProjectDatabase db;

	/**
	 * Instantiates a new data model.
	 */
	private DataModel() {
		this.projects = FXCollections.observableArrayList();
		this.tickets = FXCollections.observableArrayList();
		this.db = new ProjectDatabase();
	}

	/**
	 * Gets the single instance of DataModel.
	 *
	 * @return single instance of DataModel
	 */
	public static DataModel getInstance() {
		return instance;
	}

	/**
	 * Stores the given Project to the database.
	 *
	 * @param project the project to be stored.
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	public int addProjectToDB(Project project) {
		return this.db.addProject(project);
	}

	/**
	 * Stores the given Ticket to the database.
	 *
	 * @param ticket the ticket to be stored.
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	public int addTicketToDB(Ticket ticket) {
		return this.db.addTicket(ticket);
	}

	/**
	 * Removes the given Project from the database.
	 *
	 * @param project the project to remove
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	public int removeProjectFromDB(Project project) {
		return this.db.removeProject(project);
	}

	/**
	 * Removes the given Ticket from database.
	 *
	 * @param ticket the ticket to remove from the database
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	public int removeTicketFromDB(Ticket ticket) {
		return this.db.removeTicket(ticket);
	}

	/**
	 * Returns the projects currently stored in the databases.
	 *
	 * @return the projects currently stored in the databases.
	 */
	public ObservableList<Project> getProjects() {
		this.projects = FXCollections.observableList(this.db.getProjects());
		return this.projects;
	}

	/**
	 * Returns the tickets currently stored for the given Project.
	 *
	 * @param parentProject the project whose tickets are to be fetched.
	 * @return the tickets currently stored for the given Project.
	 */
	public ObservableList<Ticket> getTickets(Project parentProject) {
		this.tickets = FXCollections.observableList(this.db.getTickets(parentProject));
		return this.tickets;
	}
}