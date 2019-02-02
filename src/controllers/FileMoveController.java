package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import model.Album;
import model.Photo;


/**
 * FXML Controller for file moving interface
 * @author Ryan Sadofsky
 *
 */
public class FileMoveController {
	/**
	 * Delegated instance of ConMethods
	 */
	private static ConMethods controls = new ConMethods();
	
	@FXML
	ListView<Album> list_targetAlbums;
	
	@FXML
	Button button_back;
	
	@FXML
	Button button_copy;
	
	@FXML
	CheckBox check_delete;
	
	/**
	 * List of albums which a photo can be moved/copied to
	 */
	ObservableList<Album> targetAlbums = FXCollections.observableArrayList();
	
	
	/**
	 * Initializes controller with a mouse click listener, and populates targetAlbums
	 */
	@FXML
	void initialize() {
		targetAlbums.addAll(controls.getUser().getAlbums());
		targetAlbums.remove(controls.getActiveAlbum());
		
		list_targetAlbums.setItems(targetAlbums);
		
		check_delete.setOnMouseClicked(new EventHandler<MouseEvent>() {

		    @Override
		    public void handle(MouseEvent click) {
		       if(check_delete.isSelected()) {
		    	   button_copy.setText("Move");
		       }else {
		    	   button_copy.setText("Copy");
		       }
		    }
		});
		
		list_targetAlbums.setCellFactory(new Callback<ListView<Album>, ListCell<Album>>(){
			 
			@Override
			public ListCell<Album> call(ListView<Album> param) {
				ListCell<Album> albums = new ListCell<Album>() {
					
					@Override
					protected void updateItem(Album album, boolean b) {
						super.updateItem(album, b);
						if(album != null) {
							setText(album.getName());
						}
					}
				};
				
				return albums;
			}
		 });
	}
	
	/**
	 * Button event handling
	 * @param event Event which called ButtonAction
	 */
	 public void ButtonAction(ActionEvent event) {
		 Album selected_album = list_targetAlbums.getSelectionModel().getSelectedItem();
		 Button b = (Button) event.getSource();
		 controls.setStage(b);
		 
		 if(b == button_back) {
			 controls.gotoAlbumView();
		 }
		 else if(b == button_copy) {
			 if(selected_album == null) {
				 controls.showError("No target album selected.");
				 return;
			 }
			 
			 Photo toMove = controls.getActivePhoto();
	 
			 selected_album.getPhotos().add(controls.makeCopyOfPhoto(toMove));
			 
			 if(check_delete.isSelected()) {
				 controls.getActiveAlbum().getPhotos().remove(toMove);
			 }
			 controls.gotoAlbumView();
		 }
	 }

}
