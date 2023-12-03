package application;

import java.time.LocalDate;

/**
 * The Class Ticket.
 */
public class Ticket {

	/** The parent project. */
	private Project parentProject;

	/** The issue name. */
	private String issueName;

	/** The description. */
	private String description;

	/** The date of creation. */
	private LocalDate dateOfCreation;

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
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return this.getIssueName();
	}
}
