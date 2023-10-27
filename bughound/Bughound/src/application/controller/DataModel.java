package application.controller;

import application.Project;
import application.database.ProjectDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Tracks and keeps a record of created projects.
 */
public class DataModel {
	private static final DataModel instance = new DataModel();
	private ObservableList<Project> projects;
	private ProjectDatabase db;

	/**
	 * Instantiates a new data model.
	 */
	private DataModel() {
		this.projects = FXCollections.observableArrayList();
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
	public int addToDB(Project project) {
		return this.db.addProject(project);
	}

	/**
	 * Removes the given Project from the database.
	 *
	 * @param project the project
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	public int removeFromDB(Project project) {
		return this.db.removeProject(project);
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
}