package application;

import java.time.LocalDateTime;

/**
 * The Class Comment.
 */
public class Comment {
	private Ticket parentTicket;
	private String description;
	private LocalDateTime timeOfCreation;

	/**
	 * Instantiates a new comment.
	 *
	 * @param parentTicket   the parent ticket
	 * @param description    the description
	 * @param timeOfCreation the time of creation
	 */
	public Comment(Ticket parentTicket, String description, LocalDateTime timeOfCreation) {
		this.parentTicket = parentTicket;
		this.description = description;
		this.timeOfCreation = timeOfCreation;
	}

	/**
	 * Gets the parent ticket.
	 *
	 * @return the parent ticket
	 */
	public Ticket getParentTicket() {
		return this.parentTicket;
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
	 * Gets the time of creation.
	 *
	 * @return the time of creation
	 */
	public LocalDateTime getTimeOfCreation() {
		return this.timeOfCreation;
	}

	/**
	 * Sets the time of creation.
	 *
	 * @param timeOfCreation the new time of creation
	 */
	public void setTimeOfCreation(LocalDateTime timeOfCreation) {
		this.timeOfCreation = timeOfCreation;
	}
}
