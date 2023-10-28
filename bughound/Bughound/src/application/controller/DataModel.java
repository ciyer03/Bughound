package application.controller;

import application.Comment;
import application.Project;
import application.Ticket;
import application.database.ProjectDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Tracks and keeps a record of created projects.
 */
public class DataModel {
	private static final DataModel instance = new DataModel();
	private ObservableList<Project> projects;
	private ObservableList<Ticket> tickets;
	private ObservableList<Comment> comments;
	private ProjectDatabase db;

	/**
	 * Instantiates a new data model.
	 */
	private DataModel() {
		this.projects = FXCollections.observableArrayList();
		this.tickets = FXCollections.observableArrayList();
		this.comments = FXCollections.observableArrayList();
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
	 * Stores the given Comment to the database.
	 *
	 * @param comment the comment to be stored.
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	public int addCommentToDB(Comment comment) {
		return this.db.addComment(comment);
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
	 * Removes the given Ticket from the database.
	 *
	 * @param ticket the ticket to remove from the database
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	public int removeTicketFromDB(Ticket ticket) {
		return this.db.removeTicket(ticket);
	}

	/**
	 * Removes the given Comment from the database.
	 *
	 * @param comment the comment to remove from the database.
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	public int removeCommentFromDB(Comment comment) {
		return this.db.removeComment(comment);
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

	/**
	 * Returns the comments currently stored for the given Ticket.
	 *
	 * @param parentTicket the ticket whose comments are to be fetched.
	 * @return the comments currently stored for the given Ticket.
	 */
	public ObservableList<Comment> getComments(Ticket parentTicket) {
		this.comments = FXCollections.observableArrayList(this.db.getComments(parentTicket));
		return this.comments;
	}
}