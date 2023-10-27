package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class Ticket.
 */
public class Ticket {
	private Project parentProject;
	private String issueName;
	private String description;
	private LocalDate dateOfCreation;
	private ArrayList<Comment> comments;

	/**
	 * Instantiates a new ticket.
	 *
	 * @param parentProject  the parent project
	 * @param issueName      the issue name
	 * @param description    the description
	 * @param dateOfCreation the date of creation
	 */
	public Ticket(Project parentProject, String issueName, String description, LocalDate dateOfCreation) {
		this.parentProject = parentProject;
		this.issueName = issueName;
		this.description = description;
		this.dateOfCreation = dateOfCreation;
		this.comments = new ArrayList<>();
	}

	/**
	 * Gets the parent project.
	 *
	 * @return the parent project
	 */
	public Project getParentProject() {
		return this.parentProject;
	}

	/**
	 * Gets the issue name.
	 *
	 * @return the issue name
	 */
	public String getIssueName() {
		return this.issueName;
	}

	/**
	 * Sets the issue name.
	 *
	 * @param issueName the new issue name
	 */
	public void setIssueName(String issueName) {
		this.issueName = issueName;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the date of creation.
	 *
	 * @return the date of creation
	 */
	public LocalDate getDateOfCreation() {
		return this.dateOfCreation;
	}
	
	/**
	 * Returns all comments added for this ticket.
	 * 
	 * @return all comments added for this ticket.
	 */
	public List<Comment> getComments() {
		return this.comments;
	}
}
