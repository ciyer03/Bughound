package application.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import application.Project;
import application.Ticket;

/**
 * The Data Access Layer (aptly named DAL) which handles all communication to
 * and from the SQLite database.
 */
public class ProjectDatabase {

	/** The connection. */
	private Connection connection;

	/** The Constant URL. */
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
			// The database will have a name, a date, and a description field, corresponding
			// to each project.
			String projectTable = "CREATE TABLE IF NOT EXISTS projects (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "name TEXT NOT NULL PRIMARY KEY, " + "date  TEXT NOT NULL," + "description TEXT" + ")";
			
			String ticketTable = "CREATE TABLE IF NOT EXISTS tickets (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " 
					+ "parent_project_id INTEGER NOT NULL, " + "issue_name TEXT NOT NULL, " 
							+ "date TEXT NOT NULL, " + "description TEXT, " + "FOREIGN KEY (parent_project_id) REFERENCES projects(id)" + ")";
			
			String commentTable = "CREATE TABLE IF NOT EXISTS comments (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, " 
					+ "parent_ticket_id INTEGER NOT NULL, " + "description TEXT NOT NULL, " 
							+ "date TEXT NOT NULL, " + "FOREIGN KEY (parent_ticket_id) REFERENCES tickets(id)" + ")";
			
			sm.execute(projectTable); // Create the database with the given information.
			sm.execute(ticketTable);
			sm.execute(commentTable);
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
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	public int addProject(Project project) {
		PreparedStatement sm = null;
		int status = 0;
		try {
			String insertProject = "INSERT INTO projects (name, date, description) VALUES (?, ?, ?)";
			
			sm = this.connection.prepareStatement(insertProject);
			sm.setString(1, project.getName());
			sm.setString(2, project.getDate().toString());
			sm.setString(3, project.getDescription());
			
			status = sm.executeUpdate(); // Insert the project with the given details into the database.
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
		return status;
	}
	
	public int addTicket(Ticket ticket) {
		PreparedStatement sm = null;
		int status = 0;
		try {
			String insertTicket = "INSERT INTO tickets (parent_project_id, issue_name, date, description VALUES (?, ?, ? ,?)";

			sm = this.connection.prepareStatement(insertTicket);
			sm.setString(1, this.getProjectID(ticket.getParentProject()));
			sm.setString(2, ticket.getIssueName());
			sm.setString(3, ticket.getDateOfCreation().toString());
			sm.setString(4, ticket.getDescription());
			
			status = sm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (sm != null) {
					sm.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return status;
	}

	/**
	 * Removes the given project from the database.
	 *
	 * @param project the project to remove from the database.
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	public int removeProject(Project project) {
		PreparedStatement sm = null;
		int status = 0;
		try {
			String removeProject = "DELETE FROM projects where name = ? AND date = ? AND description = ?";
			
			sm = this.connection.prepareStatement(removeProject);
			sm.setString(1, project.getName());
			sm.setString(2, project.getDate().toString());
			sm.setString(3, project.getDescription());

			status = sm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (sm != null) {
					sm.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return status;
	}
	
	public int removeTicket(Ticket ticket) {
		PreparedStatement sm = null;
		int status = 0;
		try {
			String removeTicket = "DELETE FROM tickets WHERE id = ?";
			
			sm = this.connection.prepareStatement(removeTicket);
			sm.setString(1, this.getTicketID(ticket));
			
			status = sm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (sm != null) {
					sm.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return status;
	}

	/**
	 * Makes a List of all the projects that are currently in the database and
	 * returns them.
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
	
	private String getProjectID(Project project) {
		PreparedStatement sm = null;
		long projectID = 0;
		try {
			String fetchProject = "SELECT id FROM projects WHERE name = ? AND date = ? AND description = ?";
			sm = this.connection.prepareStatement(fetchProject);
			sm.setString(1, project.getName());
			sm.setString(2, project.getDate().toString());
			sm.setString(3, project.getDescription());
			ResultSet resultSet = sm.executeQuery();
			
			if (resultSet.next()) {
				projectID = resultSet.getLong("id");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (sm != null) {
				try {
					sm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return Long.toString(projectID);
	}
	
	private String getTicketID(Ticket ticket) {
		long ticketID = 0;
		PreparedStatement sm = null;
		try {
			String fetchTicket = "SELECT id FROM tickets WHERE issue_name = ? AND parent_project_id = ?";
			
			sm = this.connection.prepareStatement(fetchTicket);
			sm.setString(1, ticket.getIssueName());
			sm.setString(2, this.getProjectID(ticket.getParentProject()));
			ResultSet resultSet = sm.executeQuery();
			
			if (resultSet.next()) {
				ticketID = resultSet.getLong("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (sm != null) {
					sm.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		
		return Long.toString(ticketID);
	}

	/**
	 * Gets the tickets associated with the given project.
	 *
	 * @param project the project whose tickets are to be retrieved
	 * @return the tickets of the supplied project
	 */
	public List<Ticket> getTickets(Project project) {
		return new ArrayList<>();
	}

	/**
	 * Returns the connection to the database.
	 *
	 * @return the connection to the database, in case caller wants to modify
	 *         directly.
	 */
	public Connection connectToDB() {
		return this.connection;
	}
}
