package controllers;

import java.io.File;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.User;
import model.UserData;


/**
 * FXML Controller for Admin's page
 * @author Veton Abazovic
 *
 */
public class AdminController {
	/**
	 * Delegated instance of ConMethods
	 */
	static ConMethods controls = new ConMethods();
	
	/**
	 * Delegated instance of UserData
	 */
	static UserData uData = new UserData();
	
	@FXML
	ListView<String> userList;
	
	
	@FXML
	Button button_logOut;
	
	@FXML
	Button button_addUser;
	@FXML
	Button button_deleteUser;
	
	@FXML
	TextField text_username;
	
	/**
	 * List of users of the application
	 */
	ObservableList<String> user = FXCollections.observableArrayList();
	
	@FXML
	void initialize(){
		LoadUserNames();
	}
	
	 /**
	  * Button event handling
	  * @param event Event calling ButtonAction
	  */ 
	 public void ButtonAction(ActionEvent event) {
		 Button b = (Button) event.getSource();
		 controls.setStage(b);
		 //button_deleteUser.setDisable(false);
		    
		 if(b == button_logOut) {
			 controls.LogOut();
		 }else if(b == button_addUser) {
			 String username = text_username.getText().toLowerCase();
			 if(username.equals("admin") || username.equals("stock") || username.equals("")) {
				 controls.showError("Invalid user name!");
				 return;
			 }else {
				 if(!user.contains(username)) {
					 user.add(username);
					 userList.setItems(user);
					 User u = new User(username);
					 uData.saveUserData(u);
					 text_username.clear(); 
				 }else {
					 controls.showError("This user is already in the system!");
				 }
			 }
		 }else if(b == button_deleteUser) {
			 String selected = userList.getSelectionModel().getSelectedItem();
			 if(selected != null) {
				 DeleteUser(selected);
				 user.remove(selected);
			 }
			 text_username.clear();
		 }
	 }
	 
	 /**
	  * Loads usernames from local save files
	  */
	 public void LoadUserNames() {
		 File file = null;
	     File[] paths;
	      
	     try {  
	         file = new File(".\\saves\\");
	         paths = file.listFiles();
	         for(File path:paths) {	 
		         String temp = path.toString();
		         if(temp.substring(8, temp.length()-5).equals("stock")) {
		        	 continue;
		         }
		         user.add(temp.substring(8,temp.length()-5));
		         userList.setItems(user);
	         }
	         
	      } catch(Exception e) {
	         controls.showError("Could not load users.");
	         return;
	      }
	 }
	 
	 /**
	  * Removes a user and their save file from the application
	  * @param username username of User to delete from application
	  */
	 public void DeleteUser(String username) {
		 File file = new File(".\\saves\\" + username + ".save");
		 if(!file.exists()) {
			 controls.showError("User does not exist.");
			 return;
		 }
		 if(!file.delete()){ 
			 controls.showError("Unable to delete user.");
			 return;
	     }
	 }
}
