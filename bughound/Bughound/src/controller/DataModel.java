package controller;
import application.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataModel {
	private static final DataModel instance = new DataModel();
    private final ObservableList<Project> projects;

    private DataModel() {
        projects = FXCollections.observableArrayList();
    }

    public static DataModel getInstance() {
        return instance;
    }

    public ObservableList<Project> getProjects() {
        return projects;
    }
}
