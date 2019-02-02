package controllers;

import java.io.File;
import java.util.Calendar;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;
import model.UserData;



/**
 * Class holding static runtime values, and universal controller methods such as input prompts, error messaging, and Stage changing
 * @author Ryan Sadofsky
 *
 */
public class ConMethods {
	/**
	 * Delegated instance of UserData class
	 */
	private static UserData uData = new UserData();
	
	/**
	 * User currently logged into the application
	 */
	private static User user;
	/**
	 * @return Current active user
	 */
	public User getUser() { return user; }
	/**
	 * 
	 * @param u User to set as the active User of the application
	 */
	public void setUser(User u) { user = u; }
	
		
	/**
	 * Album actively being viewed/manipulated
	 */
	private static Album activeAlbum;
	/**
	 * @return Current active album in application
	 */
	public Album getActiveAlbum() { return activeAlbum; }
	/**
	 * 
	 * @param album Album to set as the current active Album
	 */
	public void setActiveAlbum(Album album) { activeAlbum = album; }
	
	
	/**
	 * Photo actively being moved/manipulated
	 */
	private static Photo activePhoto;
	/**
	 * @return Current active photo in application
	 */
	public Photo getActivePhoto() { return activePhoto; }
	/**
	 * 
	 * @param photo Photo to set as current active Photo
	 */
	public void setActivePhoto(Photo photo) { activePhoto = photo; }
	
	
	/**
	 * Active JavaFx Stage in the application
	 */
	private static Stage activeStage;
	/**
	 * @return Current active Stage in application
	 */
	public Stage getStage() { return activeStage; }
	/**
	 * The node argument must be node activated in the stage it is a part of, such as a button being clicked.
	 * @param node JavaFx Node from stage to set as activeStage
	 */
	public void setStage(Node node) { activeStage = (Stage) node.getScene().getWindow(); }
	
	
	/**
	 * Method to change scenes by changing the scene of the active stage
	 * 
	 * @param fxml String of fxml file to load in new stage
	 * @param winWidth width of stage window
	 * @param winHeight height of stage window
	 * @param resizable boolean to make new stage resizable or not
	 */
	public void changeScene(String fxml, double winWidth, double winHeight, boolean resizable) {
		try {
			Pane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/" + fxml));
			Scene scene = new Scene(root, winWidth, winHeight);
			activeStage.setTitle("");
			activeStage.setScene(scene);
			activeStage.setResizable(resizable);
			activeStage.show();
			if(fxml.equals("LogIn.fxml")) {
				activeStage.setTitle("Log in");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return;
	}
	
	/**
	 * Opens a file selector window, limiting acceptable files to images
	 * @param title Title of the file selector window
	 * @return File selected by user
	 */
	public File fileSelect(String title) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		String imageExtensions[]= {"*.png","*.jpg","*.gif"};
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", imageExtensions));
		
		File f = fileChooser.showOpenDialog(activeStage);
    	return f;
	}
	
	
	/**
	 * Opens a window that prompts input from the user
	 * @param title Title of window
	 * @param prompt Text of prompt in window
	 * @return The string the user has input
	 */
	public String inputBox(String title, String prompt) {
		 TextInputDialog inputBox = new TextInputDialog();
		 inputBox.setTitle(title);
		 inputBox.setHeaderText(prompt);
		 inputBox.setWidth(150);
		 inputBox.showAndWait();
		 return inputBox.getResult();
	 }
	
	
	/**
	 * Opens a window that prompts input from the user, with the edit box containing a default text
	 * @param title Title of window
	 * @param prompt Text of prompt in window
	 * @param defaultText Text to initialize the prompt's text edit box with
	 * @return The string the user has input
	 */
	public String inputBox(String title, String prompt, String defaultText) {
		 TextInputDialog inputBox = new TextInputDialog(defaultText);
		 inputBox.setTitle(title);
		 inputBox.setHeaderText(prompt);
		 inputBox.setWidth(150);
		 inputBox.showAndWait();
		 return inputBox.getResult();
	 }
	
	 /**
	  * Displays an error message to the user
	  * @param errorMessage Message to display to the user about the error they recieved
	  */
	 public void showError(String errorMessage) {
	    Alert alert = new Alert(AlertType.ERROR,errorMessage);
		alert.showAndWait();
	 }
	
	
	 /**
	  * Changes scene to the Home Page
	  */
	public void gotoHomePage() {
		changeScene("MainUserPage.fxml", 444, 500, false);
	}
	
	/**
	 *  Changes scene to Album View
	 */
	public void gotoAlbumView() {
		changeScene("AlbumView.fxml", 580, 530, false);
	}
	
	/**
	 *  Changes scene to Image Search page
	 */
	public void gotoSearchPage() {
		changeScene("SearchImagePage.fxml", 580, 530, false);
	}
	
	/**
	 * Changes scene to Admin page
	 */
	public void gotoAdminPage() {
		changeScene("AdminUserPage.fxml", 575, 448, false);
	}
	
	/**
	 * Logs out of the application and saves user data if the user is a non-admin user
	 */
	public void LogOut() {
		changeScene("LogIn.fxml", 265, 350, false);
		
		/*	Save User data if user isnt stock user or admin	*/
		if(!getUser().getUsername().equals("admin")) {
			User u = getUser();
			uData.saveUserData(u);
		}
	}
	
	/**
	 * Makes an exact copy of a photo object
	 * @param ph Photo to copy
	 * @return New object copy of photo
	 */
	public Photo makeCopyOfPhoto(Photo ph) {
		Photo photo = new Photo(ph.getPath());
		photo.setCaption(ph.getCaption());
		photo.getCalendar().clear();
		photo.getCalendar().set(Calendar.MILLISECOND, 0);
		photo.getCalendar().setTime(ph.getCalendar().getTime());
		photo.getTags().addAll(ph.getTags());
		
		return photo;
	}
}
