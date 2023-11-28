package application;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Defines an object of Project that will be used to track bugs for different
 * projects.
 */
public class Project {

	/** The name. */
	private String name;

	/** The description. */
	private String description;

	/** The date. */
	private LocalDate date;

	/**
	 * Creates a new Project object that represents a real life project.
	 *
	 * @param name        the name of the project.
	 * @param date        the date the project was created.
	 * @param description optionally, a description of the project
	 */
	public Project(String name, LocalDate date, String description) {
		this.name = name;
		this.description = description;
		this.date = date;
	}

	/**
	 * Returns the name of the project.
	 *
	 * @return the name of the project.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets a name for the project.
	 *
	 * @param name the name to be set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the description of the project.
	 *
	 * @return the description of the project.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets a description for the project.
	 *
	 * @param description the description to be set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the date of creation of the project.
	 *
	 * @return the date of creation of the project.
	 */
	public LocalDate getDate() {
		return this.date;
	}

	/**
	 * Sets a date for the project.
	 *
	 * @param date the date to be set.
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return this.getName();
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
	        return true;
	    }
	    if (obj == null || getClass() != obj.getClass()) {
	        return false;
	    }
	    Project other = (Project) obj;
	    return Objects.equals(name, other.name);
	}
}
