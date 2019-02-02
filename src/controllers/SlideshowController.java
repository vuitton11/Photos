package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Photo;

/**
 * FXML Controller for album slideshow
 * @author Ryan Sadofsky
 *
 */
public class SlideshowController {
	
	/**
	 * Delegated instance of ConMethods
	 */
	private static ConMethods controls = new ConMethods();
	
	@FXML
	ImageView DisplayedImage;
	
	@FXML
	Button button_back;
	
	@FXML
	Button button_prevPhoto;
	
	@FXML
	Button button_nextPhoto;
	
	
	/**
	 * Observable list of photos which the user can view in the slideshow
	 */
	ObservableList<Photo> photosToDisplay = FXCollections.observableArrayList();
	
	
	/**
	 * Index of image in list currently being displayed
	 */
	int image_index = 0;
	
	
	/**
	 * Initializes controller by populating photosToDisplay with photos from the current active album
	 */
	@FXML
	void initialize() {
		photosToDisplay.setAll(controls.getActiveAlbum().getPhotos());
		button_prevPhoto.setDisable(true);
		if(photosToDisplay.size() < 2) {
			button_nextPhoto.setDisable(true);
		}
		
		DisplayedImage.setImage(new Image(photosToDisplay.get(image_index).getPath()));
		
	}
		
	/**
	 * Button event handling
	 * @param event Event which called ButtonAction
	 */
	 public void ButtonAction(ActionEvent event) {
		 Button b = (Button) event.getSource();
		 controls.setStage(b);
		 
		 if(b == button_back) {
			 controls.gotoAlbumView();
		 }
		 else if(b == button_prevPhoto) {
			 image_index --;
			 DisplayedImage.setImage(new Image(photosToDisplay.get(image_index).getPath()));
		 }
		 else if(b == button_nextPhoto) {
			 image_index ++;
			 DisplayedImage.setImage(new Image(photosToDisplay.get(image_index).getPath()));
		 }
		checkIndex();
	 }
	 
	 /**
	  * Disables next photo/previous photo buttons based on the current index of the image being displayed
	  */
	 private void checkIndex() {
		 if(image_index == 0) { button_prevPhoto.setDisable(true); }
		 else { button_prevPhoto.setDisable(false); }
		 
		 if(image_index == photosToDisplay.size()-1) { button_nextPhoto.setDisable(true); }
		 else { button_nextPhoto.setDisable(false); }
	 }
}
