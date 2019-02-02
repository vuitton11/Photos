package app;
	
import java.io.File;
import java.util.Optional;

import controllers.ConMethods;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

/**
 * Main application class
 * @author Ryan Sadofsky
 *
 */
public class Photos extends Application {
	private static ConMethods controls = new ConMethods();
	@Override
	public void start(Stage primaryStage) {
		File saveFolder = new File(".\\saves\\");

		if (!saveFolder.exists()) {
		    try{
		    	saveFolder.mkdir();
		    } 
		    catch(SecurityException se){
		        controls.showError("Unable to create directory for save files");
		    }        
		}
		
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/LogIn.fxml"));
			Scene scene = new Scene(root,265,350);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Log in");
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			controls.showError("Unable to start application");
			System.exit(1);
		}
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent winEvent){
		    	Stage exitStage = (Stage) winEvent.getSource();
		    	if(exitStage.getTitle() == null || exitStage.getTitle() != "Log in") {
		    		if(!controls.getUser().getUsername().equals("admin")) {
			    	Alert alert = new Alert(AlertType.CONFIRMATION,"Do you want to save your changes?", ButtonType.YES, ButtonType.NO);
				     Optional<ButtonType> response = alert.showAndWait();
					 if (response.get() == ButtonType.YES) {
							 controls.LogOut();
							 return;
					 }
		    		}
		    	}
		        Platform.exit();
		        System.exit(0);
		    }
		});
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}