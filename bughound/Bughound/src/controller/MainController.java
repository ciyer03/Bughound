package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class MainController {
	
	@FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    void handleBack(ActionEvent event) {
    	try {
    		  root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Home.fxml"));
    		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		  scene = new Scene(root);
    		  scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
    		  stage.setScene(scene);
    		  stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void handleCreateProject(ActionEvent event) {
    	try {
    		  root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Project.fxml"));
    		  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		  scene = new Scene(root);
    		  scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
    		  stage.setScene(scene);
    		  stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
}
