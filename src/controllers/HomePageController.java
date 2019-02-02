package controllers;

import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import model.Album;
import model.User;


/**
 * FXML Controller for the user home page when the user logs in
 * @author Ryan Sadofsky
 *
 */
public class HomePageController{
	/**
	 * Delegated instance of ConMethods
	 */
	static ConMethods controls = new ConMethods();
	
	@FXML
	ListView<Album> list_albumList;
	
	@FXML
	Button button_viewAlbum;
	
	@FXML
	Button button_logOut;
	
	@FXML
	Button button_addAlbum;
	@FXML
	Button button_deleteAlbum;
	@FXML
	Button button_renameAlbum;
	
	@FXML
	Button button_imageSearch;
	
	/**
	 * Current user of application
	 */
	static User USER;
	
	
	/**
	 * Observable list of the user's albums
	 */
	ObservableList<Album> albums = FXCollections.observableArrayList();
	
	
	/**
	 * Initializes controller by setting current user and adding the user's albums to the albums list
	 */
	@FXML
	void initialize() {
		USER = controls.getUser();
		albums.addAll(USER.getAlbums());
		list_albumList.setItems(albums);
		
		
		list_albumList.setOnMouseClicked(new EventHandler<MouseEvent>() {

		    @Override
		    public void handle(MouseEvent click) {
		        if (click.getClickCount() == 2) {
		        	Album selectedAlbum = list_albumList.getSelectionModel().getSelectedItem();
		        	if(selectedAlbum != null) {
			    		controls.setActiveAlbum(selectedAlbum);
			    		controls.gotoAlbumView();
			    	}
		        }
		    }
		});
		
	}
	
	 /**
	  * Button event handling
	  * @param event Event which called ButtonAction
	  */
	 public void ButtonAction(ActionEvent event) {
	    Button b = (Button) event.getSource();
	    controls.setStage(b);
	    
	    Album selectedAlbum = list_albumList.getSelectionModel().getSelectedItem();
	    
	    if(b == button_viewAlbum) {
	    	if(selectedAlbum != null) {
	    		controls.setActiveAlbum(selectedAlbum);
	    		controls.gotoAlbumView();
	    	}
	    }
	    else if(b == button_logOut) {
	    	controls.LogOut();
	    }
	    else if(b == button_addAlbum) {
	    	String album_name = controls.inputBox("Add Album", "Enter name of new album");
	    	if(album_name != null) {
	    		if(AlbumExists(album_name)) {
	    			controls.showError("Album with name '" + album_name + "' already exists.");
	    			return;
	    		}
	    		if(album_name.equals("")) {
		    		controls.showError("Album name cannot be blank.");
		    		return;
		    	}
	    		USER.albums.add(new Album(album_name));
	    	}
	    }
	    else if(b == button_renameAlbum) {
	    	if(selectedAlbum == null) {
	    		controls.showError("No album selected.");
	    		return;
	    	}
	    		String newName = controls.inputBox("Rename Album","Enter new name for album: " + selectedAlbum.getName(), selectedAlbum.getName());
	    		if(newName != null) {
	    			if(AlbumExists(newName)) {
		    			controls.showError("Album with name '" + newName + "' already exists.");
		    			return;
		    		}
	    			selectedAlbum.setName(newName);
	    		}
	    }
	    else if(b == button_deleteAlbum) {
	    	if(selectedAlbum == null) {
	    		controls.showError("No album selected.");
	    		return;
	    	}
	    	Alert alert = new Alert(AlertType.CONFIRMATION,"Are you sure you want to delete: "
	    	+ selectedAlbum.getName() + "?", ButtonType.YES, ButtonType.CANCEL);
	        			Optional<ButtonType> response = alert.showAndWait();
	        if (response.get() == ButtonType.CANCEL) {
	        	 return;
	        }
	        	
	        USER.albums.remove(selectedAlbum);
	       // albums.remove(selectedAlbum);
	    }
	    else if(b == button_imageSearch) {
	    	controls.gotoSearchPage();
	    	return;
	    }
	    refresh();
	    return;
	 }
	 
	 
	 
	 /**
	  * Refreshes album list to ensure new data is displayed
	  */
	 private void refresh() {
		 albums.clear();
		 albums.addAll(USER.getAlbums());
		 FXCollections.sort(albums);
		 list_albumList.setItems(albums);
	 }
	 
	 /**
	  * Determines whether or not al album with the same name already exists
	  * @param name Name of new album the user is attempting to create
	  * @return true if an album with that name already exists, otherwise false
	  */
	 private boolean AlbumExists(String name) {
		 for(int i = 0; i < albums.size(); i++) {
			 if(albums.get(i).getName().equals(name)) {
				 return true;
			 }
		 }
		 return false;
	 }
}
