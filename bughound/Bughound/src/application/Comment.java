package application;

import java.time.LocalDateTime;

/**
 * The Class Comment.
 */
public class Comment {

	/** The parent ticket. */
	private Ticket parentTicket;

	/** The description. */
	private String description;

	/** The timestamp. */
	private LocalDateTime timestamp;

	/**
	 * Instantiates a new comment.
	 *
	 * @param parentTicket the parent ticket
	 * @param description  the description
	 * @param timeofCreation the timeof creation
	 */
	public Comment(Ticket parentTicket, String description, LocalDateTime timeofCreation) {
		this.parentTicket = parentTicket;
		this.description = description;
		this.timestamp = timeofCreation;
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
	public LocalDateTime getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Sets the time of creation.
	 *
	 * @param timeOfCreation the new time of creation
	 */
	public void setTimestamp(LocalDateTime timeOfCreation) {
		this.timestamp = timeOfCreation;
	}
}
