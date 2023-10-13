package application;

import java.time.LocalDate;

public class Project {
	String name, description;
	LocalDate date;
	
	public Project(String name, LocalDate date, String description) {
		this.name = name;
		this.description = description;
		this.date = date;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
}
