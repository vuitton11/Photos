package controllers;


import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.User;
import model.UserData;


/**
 * FXML Controller for the application log in page
 * @author Ryan Sadofsky
 *
 */
public class LoginController{

	/**
	 * Delegated instance of ConMethods
	 */
	static ConMethods controls = new ConMethods();
	
	/**
	 * Delegated instance of UserData
	 */
	static UserData uData = new UserData();
		
	@FXML
	Button button_login;
	
	@FXML
	TextField text_username;
	
	/**
	 * Initializes controller
	 */
	@FXML
	void initialize() {
		
	}
   
	/**
	 * Button event handling
	 * @param event Event which called ButtonAction
	 */
    public void ButtonAction(ActionEvent event) {
    	Button b = (Button) event.getSource();
    	controls.setStage(b);
    	
    	if(b == button_login) {
    		String username = text_username.getText().toLowerCase();
    		controls.setUser(new User(username));
    		switch(username) {
    		case "":
    			return;
    		case "admin":
    			controls.gotoAdminPage();
    			return;
    		default:
    			/*	Load User's photos	*/	
    			if(!findUsername(username) && !username.equals("stock")) {
    				controls.showError("User not found.");
    				return;
    			}
       			uData.loadUserData(username);
       			controls.gotoHomePage();
    			return;
    		}	
    	}
    	
    }
    
    public boolean findUsername(String username) {
		 File file = null;
	     File[] paths;
	      
	     try {  
	         file = new File(".\\saves\\");
	         paths = file.listFiles();
	         for(File path:paths) {	 
		         String temp = path.toString();
		         if(temp.substring(8, temp.length()-5).equals(username)) {
		        	 return true;
		         }
	         }
	         
	      } catch(Exception e) {
	         controls.showError("Error in finding user.");
	         System.exit(1);
	      }
	     
	     return false;
	 }
    
    
}
