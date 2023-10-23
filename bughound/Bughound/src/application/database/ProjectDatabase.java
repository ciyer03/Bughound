package application.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import application.Project;

/**
 * The Data Access Layer (aptly named DAL) which handles all communication to and from the SQLite database.
 */
public class ProjectDatabase {
	private Connection connection;
	private static final String URL = "jdbc:sqlite:data/projects.sqlite";

	/**
	 * Creates a DAL object to interact with the database.
	 */
	public ProjectDatabase() {
		try {
			this.connection = DriverManager.getConnection(URL); // Connects to the local database.
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.createDB();
	}
	
	/**
	 * Creates a valid SQLite Database.
	 */
	private void createDB() {
		Statement sm = null;
		try {
			sm = this.connection.createStatement();
			// The database will have a name, a date, and a description field, corresponding to each project.
			String projectTable = "CREATE TABLE IF NOT EXISTS projects (" + "name TEXT NOT NULL PRIMARY KEY, "
					+ "date  TEXT NOT NULL," + "description TEXT" + ")";
			sm.execute(projectTable); // Create the database with the given information.
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (sm != null) {
					sm.close(); // Close the connection after done using.
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
	}
	
	/**
	 * Adds the given project to the database.
	 *
	 * @param project the project to add to the database.
	 */
	public void addProject(Project project) {
		Statement sm = null;
		try {
			sm = this.connection.createStatement(); // Open connection
			String insertProject = "INSERT INTO projects (name, date, description) VALUES ('" + project.getName()
					+ "', '" + project.getDate().toString() + "', '" + project.getDescription() + "')";
			sm.executeUpdate(insertProject); // Insert the project with the given details into the database.
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (sm != null) {
					sm.close(); // Close connection after insertion is done.
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * Makes a List of all the projects that are currently in the database and returns them.
	 *
	 * @return the projects that are currently in the database.
	 */
	public List<Project> getProjects() {
		ArrayList<Project> projects = new ArrayList<>();
		Statement sm = null;
		try {
			sm = this.connection.createStatement(); // Open connection.
			String storedProject = "SELECT * FROM projects"; // Which database to select from.
			ResultSet projectSet = sm.executeQuery(storedProject); // Request the said database.
			
			// Loop until we have gathered all the projects.
			while (projectSet.next()) {
				// Store the project information
				String projectName = projectSet.getString("name");
				String stringDate = projectSet.getString("date");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate projectDate = LocalDate.parse(stringDate, formatter);
				String projectDescription = projectSet.getString("description");

				// Add the returned project to the to be returned list.
				projects.add(new Project(projectName, projectDate, projectDescription));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (sm != null) {
					sm.close(); // Close the connection to the database after finishing.
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return projects; // Pass the projects to the caller.
	}

	/**
	 * Returns the connection to the database.
	 *
	 * @return the connection to the database, in case caller wants to modify directly.
	 */
	public Connection connectToDB() {
		return this.connection;
	}
}