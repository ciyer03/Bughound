package application.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import application.Comment;
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
		this.openConnection();
		this.createDB();
	}

	/**
	 * Opens the connection to the database.
	 */
	private void openConnection() {
		if (this.connection == null) {
			try {
				this.connection = DriverManager.getConnection(URL); // Connects to the local database.
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates a valid SQLite Database.
	 */
	private void createDB() {
		this.openConnection(); // Open the connection to the database.
		Statement sm = null;
		try {
			sm = this.connection.createStatement();
			// The Project database will have an id, name, a date, and a description field,
			// corresponding to each project.
			String projectTable = "CREATE TABLE IF NOT EXISTS projects (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "name TEXT NOT NULL, " + "date TEXT NOT NULL," + "description TEXT" + ")";

			// The Ticket database will have an id, parent_project_id, issue_name, date,
			// description, and an association key.
			String ticketTable = "CREATE TABLE IF NOT EXISTS tickets (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "parent_project_id INTEGER NOT NULL, " + "issue_name TEXT NOT NULL, " + "date TEXT NOT NULL, "
					+ "description TEXT, " + "FOREIGN KEY (parent_project_id) REFERENCES projects(id)" + ")";

			// The Comment database will have an id, parent_ticket_id, description, date,
			// and an association key.
			String commentTable = "CREATE TABLE IF NOT EXISTS comments (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "parent_ticket_id INTEGER NOT NULL, " + "description TEXT NOT NULL, " + "date TEXT NOT NULL, "
					+ "FOREIGN KEY (parent_ticket_id) REFERENCES tickets(id)" + ")";

			// Create the database with the given information.
			sm.execute(projectTable);
			sm.execute(ticketTable);
			sm.execute(commentTable);
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection(); // Close the connection to the database if there's an error.
		} finally {
			try {
				if (sm != null) {
					sm.close();
					this.closeConnection(); // Close the connection after done using.
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				this.closeConnection(); // Close the connection to the database if there's an error.
			}
		}
	}

	/**
	 * Adds the given Project to the database.
	 *
	 * @param project the project to add to the database.
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	public int addProject(Project project) {
		this.openConnection(); // Open the connection to the database.
		PreparedStatement sm = null;
		int status = 0;

		try {
			// Criterion for the data to be inserted into the database.
			String insertProject = "INSERT INTO projects (name, date, description) VALUES (?, ?, ?)";

			// Set values for insertion into database.
			sm = this.connection.prepareStatement(insertProject); // Prepare to insert.
			sm.setString(1, project.getName());
			sm.setString(2, project.getDate().toString());
			sm.setString(3, project.getDescription());

			status = sm.executeUpdate(); // Insert the project with the given details into the database.
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection(); // Close the connection to the database if there's an error.
		} finally {
			try {
				if (sm != null) {
					sm.close();
					this.closeConnection(); // Close connection after insertion is done.
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				this.closeConnection(); // Close the connection to the database if there's an error.
			}
		}
		return status;
	}

	/**
	 * Adds the given Ticket to the database. Associates it with its respective
	 * Project.
	 *
	 * @param ticket the ticket to add to the database.
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	public int addTicket(Ticket ticket) {
		this.openConnection(); // Open the connection to the database.
		PreparedStatement sm = null;
		int status = 0;

		try {
			// Criterion for the data to be inserted into the database.
			String insertTicket = "INSERT INTO tickets (parent_project_id, issue_name, date, description) VALUES (?, ?, ? ,?)";

			// Set values for insertion into database.
			sm = this.connection.prepareStatement(insertTicket); // Prepare to insert.
			sm.setInt(1, this.getProjectID(ticket.getParentProject()));
			sm.setString(2, ticket.getIssueName());
			sm.setString(3, ticket.getDateOfCreation().toString());
			sm.setString(4, ticket.getDescription());

			status = sm.executeUpdate(); // Insert the Ticket with the given details into the database.
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection(); // Close the connection to the database if there's an error.
		} finally {
			try {
				if (sm != null) {
					sm.close();
					this.closeConnection(); // Close the connection to the database after done using.
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				this.closeConnection(); // Close the connection to the database if there's an error.
			}
		}
		return status;
	}

	/**
	 * Adds the given Comment to the database. Associates it with its respective
	 * Ticket.
	 *
	 * @param comment the comment
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	public int addComment(Comment comment) {
		this.openConnection(); // Open the connection to the database.
		PreparedStatement sm = null;
		int status = 0;

		try {
			// Criterion for the data to be inserted into the database.
			String insertComment = "INSERT INTO comments (parent_ticket_id, description, date) VALUES (?, ?, ?)";

			// Set values for insertion into database.
			sm = this.connection.prepareStatement(insertComment); // Prepare to insert.
			sm.setInt(1, this.getTicketID(comment.getParentTicket()));
			sm.setString(2, comment.getDescription());
			sm.setString(3, comment.getTimestamp().toString());

			status = sm.executeUpdate(); // Insert the Comment with the given details into the database.
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection(); // Close the connection to the database if there's an error.
		} finally {
			try {
				if (sm != null) {
					sm.close();
					this.closeConnection(); // Close the connection to the database after done using.
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				this.closeConnection(); // Close the connection to the database if there's an error.
			}
		}
		return status;
	}

	/**
	 * Removes the given Project from the database.
	 *
	 * @param project the project to remove from the database.
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	public int removeProject(Project project) {
		this.openConnection(); // Open the connection to the database.
		PreparedStatement sm = null;
		int status = 0;

		try {
			if (this.removeAllTickets(project) < 0) {
				System.out.println("Failed to remove Tickets for the Project " + project.getName());
			}
			// Criterion for the data to be deleted from the database.
			String removeProject = "DELETE FROM projects WHERE id = ?";

			this.openConnection();
			// Set values for the database.
			sm = this.connection.prepareStatement(removeProject); // Prepare to insert.
			sm.setInt(1, this.getProjectID(project));

			status = sm.executeUpdate(); // Remove the Project with the given details from the database.
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection(); // Close the connection to the database if there's an error.
		} finally {
			try {
				if (sm != null) {
					sm.close();
					this.closeConnection(); // Close the connection to the database after done using.
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				this.closeConnection(); // Close the connection to the database if there's an error.
			}
		}
		return status;
	}

	/**
	 * Removes the given Ticket from the database.
	 *
	 * @param ticket the ticket to remove from the database.
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	public int removeTicket(Ticket ticket) {
		this.openConnection(); // Open the connection to the database.
		PreparedStatement sm = null;
		int status = 0;

		try {
			if (this.removeAllComments(ticket) < 0) {
				System.out.println("Failed to remove comments for Ticket " + ticket.getIssueName());
			}
			// Criterion for the data to be deleted from the database.
			String removeTicket = "DELETE FROM tickets WHERE id = ? AND parent_project_id = ?";

			this.openConnection();
			// Set values for the database.
			sm = this.connection.prepareStatement(removeTicket); // Prepare to insert.
			sm.setInt(1, this.getTicketID(ticket));
			sm.setInt(2, this.getProjectID(ticket.getParentProject()));

			status = sm.executeUpdate(); // Remove the Ticket with the given details from the database.
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection(); // Close the connection to the database if there's an error.
		} finally {
			try {
				if (sm != null) {
					sm.close();
					this.closeConnection(); // Close the connection to the database after done using.
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				this.closeConnection(); // Close the connection to the database if there's an error.
			}
		}
		return status;
	}

	/**
	 * Removes the given Comment from the database.
	 *
	 * @param comment the comment to remove from the database.
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	public int removeComment(Comment comment) {
		this.openConnection(); // Open the connection to the database.
		PreparedStatement sm = null;
		int status = 0;

		try {
			// Criterion for the data to be deleted from the database.
			String removeComment = "DELETE FROM comments WHERE id = ? AND parent_ticket_id = ?";

			// Set values for the database.
			sm = this.connection.prepareStatement(removeComment); // Prepare to insert.
			sm.setInt(1, this.getCommentID(comment));
			sm.setInt(2, this.getTicketID(comment.getParentTicket()));

			status = sm.executeUpdate(); // Remove the Comment with the given details from the database.
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection(); // Close the connection to the database if there's an error.
		} finally {
			try {
				if (sm != null) {
					sm.close();
					this.closeConnection(); // Close the connection to the database after done using.
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				this.closeConnection(); // Close the connection to the database if there's an error.
			}
		}
		return status;
	}

	/**
	 * Removes all the Tickets lodged for the given Project.
	 *
	 * @param parentProject the parent project whose Tickets to remove
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	private int removeAllTickets(Project parentProject) {
		PreparedStatement sm = null;
		int status = 0;
		ArrayList<Ticket> allTickets = new ArrayList<>(this.getTickets(parentProject));

		try {
			if (allTickets.isEmpty()) {
				return 1;
			}

			for (Ticket ticket : allTickets) {
				if (this.removeAllComments(ticket) < 0) {
					System.out.println("Failed to remove comments for Ticket " + ticket.getIssueName());
				}
			}
			// Criterion for the data to be deleted from the database.
			String removeAllTicketsForProject = "DELETE FROM tickets WHERE parent_project_id = ?";

			this.openConnection();
			// Set values for the database.
			sm = this.connection.prepareStatement(removeAllTicketsForProject); // Prepare to insert.
			sm.setInt(1, this.getProjectID(parentProject));

			status = sm.executeUpdate(); // Remove all the Tickets with the given details from the database.
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection(); // Close the connection to the database if there's an error.
		} finally {
			try {
				if (sm != null) {
					sm.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				this.closeConnection(); // Close the connection to the database if there's an error.
			}
		}
		return status;
	}

	/**
	 * Removes all the Comments lodged for the given Ticket.
	 *
	 * @param parentTicket the parent Ticket whose Comments to remove
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	private int removeAllComments(Ticket parentTicket) {
		PreparedStatement sm = null;
		int status = 0;
		ArrayList<Comment> allComments = new ArrayList<>(this.getComments(parentTicket));

		try {
			if (allComments.isEmpty()) {
				return 1;
			}

			// Criterion for the data to be deleted from the database.
			String removeAllCommentsForTicket = "DELETE FROM comments WHERE parent_ticket_id = ?";

			this.openConnection();
			// Set values for the database.
			sm = this.connection.prepareStatement(removeAllCommentsForTicket); // Prepare to insert.
			sm.setInt(1, this.getTicketID(parentTicket));

			status = sm.executeUpdate(); // Remove all the Comments with the given details from the database.
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection(); // Close the connection to the database if there's an error.
		} finally {
			try {
				if (sm != null) {
					sm.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				this.closeConnection(); // Close the connection to the database if there's an error.
			}
		}
		return status;
	}

	/**
	 * Edits the selectedProject with details from editedProject.
	 *
	 * @param selectedProject the selected project to be edited
	 * @param editedProject   the edited project whose values are to be set to
	 *                        selectedProject
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	public int editProject(Project selectedProject, Project editedProject) {
		this.openConnection(); // Open the connection to the database.
		PreparedStatement sm = null;
		int status = 0;

		try {
			// Criterion for the data to be updated from the database.
			String updateProject = "UPDATE projects SET name = ?, date = ?, description = ? WHERE id = ?";

			// Set values for the database.
			sm = this.connection.prepareStatement(updateProject); // Prepare to insert.
			sm.setString(1, editedProject.getName());
			sm.setString(2, editedProject.getDate().toString());
			sm.setString(3, editedProject.getDescription());
			sm.setInt(4, this.getProjectID(selectedProject));

			status = sm.executeUpdate(); // Update the selectedProject with details from editedProject
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection();
		} finally {
			try {
				if (sm != null) {
					sm.close();
					this.closeConnection(); // Close the connection to the database if there's an error.
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				this.closeConnection(); // Close the connection to the database if there's an error.
			}
		}
		return status;
	}

	/**
	 * Edits the selectedTicket with details from editedTicket.
	 *
	 * @param selectedTicket the selected ticket to be edited
	 * @param editedTicket   the edited ticket whose values are to be set to
	 *                       selectedTicket
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 **/
	public int editTicket(Ticket selectedTicket, Ticket editedTicket) {
		this.openConnection();
		PreparedStatement sm = null;
		int status = 0;

		try {
			String updateTicket = "UPDATE tickets SET parent_project_id = ?, issue_name = ?, date = ?, description = ? WHERE id = ?";

			sm = this.connection.prepareStatement(updateTicket);
			sm.setInt(1, this.getProjectID(editedTicket.getParentProject()));
			sm.setString(2, editedTicket.getIssueName());
			sm.setString(3, editedTicket.getDateOfCreation().toString());
			sm.setString(4, editedTicket.getDescription());
			sm.setInt(5, this.getTicketID(selectedTicket));

			status = sm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection();
		} finally {
			try {
				if (sm != null) {
					sm.close();
					this.openConnection();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				this.closeConnection();
			}
		}

		return status;
	}

	/**
	 * Edits the selectedComment with details from editedComment.
	 *
	 * @param selectedComment the selected comment to be edited
	 * @param editedComment   the edited comment whose values are to be set to
	 *                        selectedComment
	 * @return the row count for SQL Data Manipulation Language (DML) statements or
	 *         0 if failed.
	 */
	public int editComment(Comment selectedComment, Comment editedComment) {
		this.openConnection();
		PreparedStatement sm = null;
		int status = 0;

		try {
			String updateComment = "UPDATE comments SET parent_ticket_id = ?, description = ?, date = ? WHERE id = ?";

			sm = this.connection.prepareStatement(updateComment);
			sm.setInt(1, this.getTicketID(editedComment.getParentTicket()));
			sm.setString(2, editedComment.getDescription());
			sm.setString(3, editedComment.getTimestamp().toString());
			sm.setInt(4, this.getCommentID(selectedComment));

			status = sm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection();
		} finally {
			try {
				if (sm != null) {
					sm.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				this.closeConnection();
			}
		}

		return status;
	}

	/**
	 * Gets the project ID.
	 *
	 * @param project the Project whose ID to fetch
	 * @return the project ID
	 */
	private int getProjectID(Project project) {
		PreparedStatement sm = null;
		int projectID = 0;

		try {
			// Criterion for the data to be fetched from the database.
			String fetchProject = "SELECT id FROM projects WHERE name = ? AND date = ? AND description = ?";

			// Set values for the database.
			sm = this.connection.prepareStatement(fetchProject); // Prepare to insert.
			sm.setString(1, project.getName());
			sm.setString(2, project.getDate().toString());
			sm.setString(3, project.getDescription());
			ResultSet resultSet = sm.executeQuery();

			if (resultSet.next()) {
				projectID = resultSet.getInt("id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection(); // Close the connection to the database if there's an error.
		} finally {
			if (sm != null) {
				try {
					sm.close();
				} catch (SQLException e) {
					e.printStackTrace();
					this.closeConnection(); // Close the connection to the database if there's an error.
				}
			}
		}
		return projectID;
	}

	/**
	 * Gets the ticket ID.
	 *
	 * @param ticket the Ticket whose ID to fetch
	 * @return the ticket ID
	 */
	private int getTicketID(Ticket ticket) {
		int ticketID = 0;
		PreparedStatement sm = null;

		try {
			// Criterion for the data to be fetched from the database.
			String fetchTicket = "SELECT id FROM tickets WHERE parent_project_id = ? AND issue_name = ? AND date = ? AND description = ?";

			// Set values for the database.
			sm = this.connection.prepareStatement(fetchTicket); // Prepare to insert.
			sm.setInt(1, this.getProjectID(ticket.getParentProject()));
			sm.setString(2, ticket.getIssueName());
			sm.setString(3, ticket.getDateOfCreation().toString());
			sm.setString(4, ticket.getDescription());
			ResultSet resultSet = sm.executeQuery();

			if (resultSet.next()) {
				ticketID = resultSet.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection(); // Close the connection to the database if there's an error.
		} finally {
			try {
				if (sm != null) {
					sm.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				this.closeConnection(); // Close the connection to the database if there's an error.
			}
		}
		return ticketID;
	}

	/**
	 * Gets the comment ID.
	 *
	 * @param comment the Comment whose ID to fetch.
	 * @return the comment ID
	 */
	private int getCommentID(Comment comment) {
		PreparedStatement sm = null;
		int commentID = 0;

		try {
			// Criterion for the data to be fetched from the database.
			String fetchComment = "SELECT id FROM comments WHERE parent_ticket_id = ? AND description = ? AND date = ?";

			// Set values for the database.
			sm = this.connection.prepareStatement(fetchComment); // Prepare to insert.
			sm.setInt(1, this.getTicketID(comment.getParentTicket()));
			sm.setString(2, comment.getDescription());
			sm.setString(3, comment.getTimestamp().toString());

			ResultSet resultSet = sm.executeQuery();
			if (resultSet.next()) {
				commentID = resultSet.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection(); // Close the connection to the database if there's an error.
		} finally {
			try {
				if (sm != null) {
					sm.close();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				this.closeConnection(); // Close the connection to the database if there's an error.
			}
		}
		return commentID;
	}

	/**
	 * Makes a List of all the projects that are currently in the database.
	 *
	 * @return the projects that are currently in the database.
	 */
	public List<Project> getProjects() {
		this.openConnection(); // Open the connection to the database.
		ArrayList<Project> projects = new ArrayList<>();
		Statement sm = null;

		try {
			sm = this.connection.createStatement(); // Prepare to insert.
			String storedProject = "SELECT * FROM projects"; // Criterion for the data to be fetched from the database.
			ResultSet projectSet = sm.executeQuery(storedProject); // Request the said database.

			// Loop until we have gathered all the projects.
			while (projectSet.next()) {
				// Retrieve and store the project information
				String projectName = projectSet.getString("name");
				String date = projectSet.getString("date");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate projectDate = LocalDate.parse(date, formatter);
				String projectDescription = projectSet.getString("description");

				// Add the returned Project to the to be returned list.
				projects.add(new Project(projectName, projectDate, projectDescription));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection(); // Close the connection to the database if there's an error.
		} finally {
			try {
				if (sm != null) {
					sm.close(); // Close the connection to the database after finishing.
					this.closeConnection();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				this.closeConnection(); // Close the connection to the database if there's an error.
			}
		}
		return projects;
	}

	/**
	 * Makes a List of all the tickets associated with the given project.
	 *
	 * @param project the Project whose tickets are to be retrieved.
	 * @return the tickets of the supplied Project.
	 */
	public List<Ticket> getTickets(Project project) {
		this.openConnection(); // Open the connection to the database.
		ArrayList<Ticket> tickets = new ArrayList<>();
		PreparedStatement sm = null;

		try {
			// Criterion for the data to be fetched from the database.
			String storedComments = "SELECT issue_name, date, description FROM tickets WHERE parent_project_id = ?";

			// Set values for the database.
			sm = this.connection.prepareStatement(storedComments); // Prepare to insert.
			sm.setInt(1, this.getProjectID(project));
			ResultSet resultSet = sm.executeQuery(); // Request the specified information.

			// Loop until we have gathered all the projects.
			while (resultSet.next()) {
				// Retrieve and store the project information
				String issueName = resultSet.getString("issue_name");
				String date = resultSet.getString("date");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate ticketDate = LocalDate.parse(date, formatter);
				String description = resultSet.getString("description");

				// Add the returned Ticket to the to be returned list.
				Ticket ticket = new Ticket(project, issueName, description, ticketDate);
				tickets.add(ticket);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection(); // Close the connection to the database if there's an error.
		} finally {
			try {
				if (sm != null) {
					sm.close();
					this.closeConnection(); // Close the connection to the database after done using.
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				this.closeConnection(); // Close the connection to the database if there's an error.
			}
		}
		return tickets;
	}

	/**
	 * Makes a List of all the comments associated with the given Ticket.
	 *
	 * @param ticket the Ticket whose comments are to be retrieved.
	 * @return the comments associated with the given Ticket.
	 */
	public List<Comment> getComments(Ticket ticket) {
		this.openConnection(); // Open the connection to the database.
		ArrayList<Comment> comments = new ArrayList<>();
		PreparedStatement sm = null;

		try {
			// Criterion for the data to be fetched from the database.
			String storedComments = "SELECT description, date FROM comments WHERE parent_ticket_id = ?";

			// Set values for the database.
			sm = this.connection.prepareStatement(storedComments); // Prepare to insert.
			sm.setInt(1, this.getTicketID(ticket));
			ResultSet resultSet = sm.executeQuery(); // Request the specified information.

			// Loop until we have gathered all the projects.
			while (resultSet.next()) {
				// Retrieve and store the project information
				String description = resultSet.getString("description");
				String timestamp = resultSet.getString("date");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
				LocalDateTime timeDate = LocalDateTime.parse(timestamp, formatter);

				// Add the returned Comment to the to be returned list.
				Comment comment = new Comment(ticket, description, timeDate);
				comments.add(comment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			this.closeConnection(); // Close the connection to the database if there's an error.
		} finally {
			try {
				if (sm != null) {
					sm.close();
					this.closeConnection(); // Close the connection to the database after done using.
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
				this.closeConnection(); // Close the connection to the database if there's an error.
			}
		}
		return comments;
	}

	/**
	 * Close connection to the database to prevent data/resource leaks.
	 */
	private void closeConnection() {
		try {
			if (this.connection != null) {
				this.connection.close();
				this.connection = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
