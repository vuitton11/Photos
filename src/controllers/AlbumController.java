package controllers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.Photo;
import model.Tag;


/**
 * FXML Controller for the album view
 * @author Ryan Sadofsky
 *
 */
public class AlbumController {
	/**
	 * Delegated instance of ConMethods
	 */
	private static ConMethods controls = new ConMethods();
	
	@FXML
	Button button_logOut;
	
	@FXML
	Button button_back;
	
	@FXML
	Button button_addPhoto;
	
	@FXML
	Button button_removePhoto;
	
	@FXML
	Button button_movePhoto;
	
	@FXML
	Button button_editCaption;
	
	@FXML
	Button button_manageTags;
	
	@FXML
	Button button_viewSlideshow;
	
	@FXML
	ListView<Photo> thumbnails;
	
	@FXML
	ImageView photoDisplay;
	
	@FXML
	Text album_header;
	
	@FXML
	Text text_caption;
	
	@FXML
	Text text_date;
	
	@FXML
	ListView<Tag> list_tags;
	
	
	/**
	 * Width of thumbnail images
	 */
	private static final int thumbnail_sizeX = 60;
	
	/**
	 * Height of thumbnail images
	 */
	private static final int thumbnail_sizeY = 50;
	
	
	/**
	 * Observable list of photos in this album
	 */
	ObservableList<Photo> photos = FXCollections.observableArrayList();
	
	/**
	 * Observable list of tags for selected photo
	 */
	ObservableList<Tag> tags = FXCollections.observableArrayList();
	
	
	@FXML
	void initialize() {
		photos.clear();
		
		ArrayList<Photo> phs = controls.getActiveAlbum().getPhotos();
		for(int i = 0; i < phs.size(); i++) {
			Photo ph = phs.get(i);
			File pFile = new File(ph.getPath().substring(6, ph.getPath().length()));
			if(pFile.exists()) {
				photos.add(ph);
			}else {
				controls.getActiveAlbum().getPhotos().remove(ph);
			}
		}
		
		album_header.setText("Photos in: " + controls.getActiveAlbum().getName());
		
		thumbnails.getSelectionModel().selectedItemProperty().addListener( (photo, oldPhoto, newPhoto) ->
    	changeAction(newPhoto)
    	);
		
		 RefreshAlbum();
		 if(photos.contains(controls.getActivePhoto())){
			 thumbnails.getSelectionModel().select(controls.getActivePhoto());
		 }
		 else if(photos.size() > 0) {
			thumbnails.getSelectionModel().select(0);
		 }
		
	}
	 
	 /**
	  * Method called by listener upon photo selection change
	  * @param photo new photo selected
	  */
	 private void changeAction(Photo photo) {
		 if(photos.size() == 0) {
			 photoDisplay.setVisible(false);
			 text_caption.setText("");
			 text_date.setText("");
			 list_tags.setItems(null);
		 }
		 
		     if(photo != null) {
				 photoDisplay.setVisible(true);
				 photoDisplay.setImage(new Image(photo.getPath()));
				 text_caption.setText(photo.getCaption());
				 text_date.setText("Date taken: " + photo.getDate());
				 tags.setAll(photo.getTags());
				 list_tags.setItems(tags);
		     }
		 
	 }
	  
	 /**
	  * Refreshes the album to ensure new data is displayed
	  */
	 private void RefreshAlbum() {
		 thumbnails.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>(){
			 
			@Override
			public ListCell<Photo> call(ListView<Photo> param) {
				ListCell<Photo> thumbs = new ListCell<Photo>() {
					
					@Override
					protected void updateItem(Photo photo, boolean b) {
						super.updateItem(photo, b);
						if(photo != null) {
							Image thumb = new Image(photo.getPath());
							ImageView imgView = new ImageView(thumb);
							
							imgView.setFitWidth(thumbnail_sizeX);
							imgView.setFitHeight(thumbnail_sizeY);
							setGraphic(imgView);
							setText(photo.getCaption());
						}
					}
				};
				
				return thumbs;
			}
		 });
		 
		 ObservableList<Photo> temp = FXCollections.observableArrayList();
		 temp.addAll(controls.getActiveAlbum().getPhotos());
		 
		 photos = temp;
		 thumbnails.setItems(photos);
		 //thumbnails.getSelectionModel().select(photos.size() - 1);
	 }
	 
	 /**
	  * Button event handling
	  * @param event Event which called ButtonAction
	  */
	 public void ButtonAction(ActionEvent event) {
		 Button b = (Button) event.getSource();
		 controls.setStage(b);
		 Photo selected_photo = thumbnails.getSelectionModel().getSelectedItem();
		    
		 if(b == button_logOut) {
			 controls.LogOut();
			 return;
		 }
		 else if(b == button_back) {
			 controls.gotoHomePage();
			 return;
		 }
		 else if(b == button_addPhoto) {
			 File selected_file = controls.fileSelect("Select a file to add to your album");
			 if(selected_file == null) {
				 return;
			 }
			 
			 Photo ph = setDateValues(selected_file);
			 
			 controls.getActiveAlbum().getPhotos().add(ph);
			 photos.clear();
			 photos.addAll(controls.getActiveAlbum().getPhotos());
			 thumbnails.setItems(photos);
			 thumbnails.getSelectionModel().select(photos.size()-1);
		 }
		 else if(b == button_removePhoto) {
			 if(selected_photo == null) {
				 controls.showError("No photo selected.");
				 return;
			 }
			 
			 Alert alert = new Alert(AlertType.CONFIRMATION,"Are you sure you want to delete?", ButtonType.YES, ButtonType.CANCEL);
		     Optional<ButtonType> response = alert.showAndWait();
			 if (response.get() == ButtonType.CANCEL) {
				 return;
			 }
			
			int index = photos.indexOf(selected_photo);
			controls.getActiveAlbum().removePhoto(selected_photo);
			photos.setAll(controls.getActiveAlbum().getPhotos());
			thumbnails.setItems(photos);
			if(index < photos.size()) {
				thumbnails.getSelectionModel().select(index);
			}else if(index == photos.size()) {
				thumbnails.getSelectionModel().select(index - 1);
			}
		 }
		 else if(b == button_movePhoto) {
			 if(selected_photo == null) {
				 controls.showError("No photo selected.");
				 return;
			 }else if(controls.getUser().getAlbums().size() < 2) {
				 controls.showError("No album to move/copy to.");
				 return;
			 }
			 controls.setActivePhoto(selected_photo);
			 controls.changeScene("FileChangeLocation.fxml", 300, 400, false);
		 }
		 else if(b == button_editCaption) {
			 if(selected_photo == null) {
				 controls.showError("No photo selected.");
				 return;
			 }
			 String newCaption = controls.inputBox("Caption Edit", "Edit the caption", selected_photo.getCaption());
			 if(newCaption != null) {
				 selected_photo.setCaption(newCaption);
				 text_caption.setText(selected_photo.getCaption());
			 }
		 }
		 else if(b == button_manageTags) {
			 if(selected_photo == null) {
				 controls.showError("No photo selected.");
				 return;
			 }
			 controls.setActivePhoto(selected_photo);
			 controls.changeScene("TagManagement.fxml", 318, 460, false);
		 }
		 else if(b == button_viewSlideshow) {
			 if(photos.size() == 0) {
				 controls.showError("No photos to view.");
				 return;
			 }
			 controls.changeScene("Slideshow.fxml", 655, 450, false);
		 }
		 
		 RefreshAlbum(); 
	 }
	 
	 
	 /**
	  * Sets the photo's date to the file's "last modified" date
	  * @param f File of photo
	  * @return Photo with modified date
	  */
	 private Photo setDateValues(File f) {
		 int m,d,y;
		 long dateLong = f.lastModified();
		 SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		 
		 sdf.applyPattern("MM");
		 m = Integer.parseInt(sdf.format(dateLong));
		 
		 sdf.applyPattern("dd");
		 d = Integer.parseInt(sdf.format(dateLong));
		 
		 sdf.applyPattern("yyyy");
		 y = Integer.parseInt(sdf.format(dateLong));
		 
		 Photo ph = new Photo(f.toURI().toString());
		 ph.setDate(m, d, y);
		 
		 return ph;
	 }
}
