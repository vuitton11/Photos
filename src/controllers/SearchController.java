package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import model.Album;
import model.Photo;
import model.Tag;

/**
 * FXML Controller for image searching
 * @author Veton Abazovic
 *
 */
public class SearchController {
	
	/**
	 * Delegated instance of ConMethods
	 */
	static ConMethods controls = new ConMethods();
	
	
	@FXML
	Button button_back;
	@FXML
	Button button_search;

	@FXML
	DatePicker button_sDate;
	@FXML
	DatePicker button_eDate;
	
	@FXML
	MenuButton button_tag2;
	@FXML
	MenuButton button_tag1;
	
	@FXML
	ToggleButton andOr;
	
	@FXML
	TextField button_t1;
	@FXML
	TextField button_t2;

	@FXML
	ComboBox<String> combo_tag1;
	@FXML
	ComboBox<String> combo_tag2;
	
	@FXML
	TextField tag_value1;
	
	@FXML
	TextField tag_value2;
	
	@FXML
	TextField search_albumText;
	
	@FXML
	ToggleGroup search_by;
	
	@FXML
	RadioButton search_dateRange;
	
	@FXML
	RadioButton search_tags;
	
	@FXML
	Text text_date1;
	
	@FXML
	Text text_date2;
	
	@FXML
	Text text_tag1;
	@FXML
	Text text_tag2;
	@FXML
	Text text_tag3;
	@FXML
	Text text_tag4;
	
	/**
	 * List of tag types for the user
	 */
	ObservableList<String> tagTypes = FXCollections.observableArrayList();
	
	/**
	 * Initializes controller
	 */
	@FXML
	void initialize(){
		andOr.setSelected(false);
		setStateDateRange(true);
		
		andOr.setOnMouseClicked(new EventHandler<MouseEvent>() {

		    @Override
		    public void handle(MouseEvent click) {
		       if(andOr.isSelected()) {
		    	   andOr.setText("And");
		       }else {
		    	   andOr.setText("Or");
		       }
		    }
		});
		
		search_by.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

		         if (search_by.getSelectedToggle().equals(search_dateRange)) {
		        	 setStateDateRange(true);
		         }else if(search_by.getSelectedToggle().equals(search_tags)) {
		        	 setStateDateRange(false);
		         }

		     } 
		});
		
		tagTypes.addAll(controls.getUser().getTagTypes());
		
		combo_tag1.setItems(tagTypes);
		combo_tag2.setItems(tagTypes);
		
		combo_tag1.setEditable(false);
		combo_tag2.setEditable(false);
	}
	
	/**
	 * Sets field visibilities based on search method
	 * @param b True if searching by date range, false if searching by tags
	 */
	private void setStateDateRange(boolean b) {
		text_date1.setVisible(b);
		text_date2.setVisible(b);
		button_sDate.setVisible(b);
		button_eDate.setVisible(b);
		
		andOr.setVisible(!b);
		text_tag1.setVisible(!b);
		text_tag2.setVisible(!b);
		text_tag3.setVisible(!b);
		text_tag4.setVisible(!b);
		combo_tag1.setVisible(!b);
		combo_tag2.setVisible(!b);
		tag_value1.setVisible(!b);
		tag_value2.setVisible(!b);
		
		if(b) {
			combo_tag1.setValue("");
			combo_tag2.setValue("");
			tag_value1.setText("");
			tag_value2.setText("");
		}else {
			button_sDate.setValue(null);
			button_eDate.setValue(null);
		}

		
	}
    
	/**
	 * Button event handling
	 * @param event Event which called ButtonAction
	 */
    public void ButtonAction(ActionEvent event) {
    	Button b = (Button) event.getSource();
    	controls.setStage(b);
		 if(b == button_back) {
			 controls.gotoHomePage();;
		 }else if(b == button_search) {
			 
			 
			 if(search_by.getSelectedToggle().equals(search_dateRange)) {
				 if(button_sDate.getValue() == null || button_eDate.getValue() == null) {
					 controls.showError("Please add a start and end date.");
					 return;
				 }
				 
				 if(button_sDate.getValue().isAfter(button_eDate.getValue())) {
					 controls.showError("Please ensure start date is before end date.");
					 return;
				}
				 
			 }else {
				 if((combo_tag1.getValue().equals("") && combo_tag2.getValue().equals(""))) {
					 controls.showError("Please enter valid search fields.");
					 return;
				 }
				 
				 if(!combo_tag1.getValue().equals("") && tag_value1.getText().equals("")) {
					 controls.showError("Please enter valid tag value");
					 return;
				 }
				 
				 if(!combo_tag2.getValue().equals("") && tag_value2.getText().equals("")) {
					 controls.showError("Please enter valid tag value");
					 return;
				 }
			 }
			 
			 
			 if(search_albumText.getText() == null || search_albumText.getText().equals("")) {
				 controls.showError("Please enter a name for the album.");
				 return;
			 }
			 
			 if(AlbumExists(search_albumText.getText())) {
				 controls.showError("Album with this name already exists.");
				 return;
			 }
			 
			 Album newAlbum = new Album(search_albumText.getText());
			 
			 if(button_sDate.getValue() != null && button_eDate.getValue() != null) {
				
			
				String string_sDate = button_sDate.getValue().toString();
				String string_eDate = button_eDate.getValue().toString();
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date sDate = new Date();
				Date eDate = new Date();
				try {
					sDate = sdf.parse(string_sDate);
					eDate = sdf.parse(string_eDate);
				} catch (ParseException e) {
					controls.showError("Unable to parse dates.");
					return;
				}
				
				Calendar sCal = Calendar.getInstance();
					sCal.clear();
				Calendar eCal = Calendar.getInstance();
					eCal.clear();
				
				sCal.setTime(sDate);
				sCal.set(Calendar.MILLISECOND, 0);
				eCal.setTime(eDate);
				eCal.set(Calendar.MILLISECOND, 0);
				
				
				ArrayList<Photo> matches = matchByDate(sCal, eCal);
				
				if(matches.size() == 0) {
					controls.showError("No matches found.");
					return;
				}
				
				for(int i = 0; i < matches.size(); i++) {
					newAlbum.getPhotos().add(controls.makeCopyOfPhoto(matches.get(i)));
				}
				
				
			 }else {
				 
				 ArrayList<Tag> searchForTags = new ArrayList<Tag>();
				 
				 ArrayList<Photo> matches_tag1 = new ArrayList<Photo>();
				 ArrayList<Photo> matches_tag2 = new ArrayList<Photo>();
				 
				 if(!combo_tag1.getValue().equals("") && !tag_value1.getText().equals("")) {
					 Tag t1 = new Tag(combo_tag1.getValue(), tag_value1.getText());
					 searchForTags.add(t1);
					 matches_tag1 = matchByTag(t1);
				 }
				 
				 if(!combo_tag2.getValue().equals("") && !tag_value2.getText().equals("")) {
					 Tag t2 = new Tag(combo_tag2.getValue(), tag_value2.getText());
					 searchForTags.add(t2);
					 matches_tag2 = matchByTag(t2);
				 }
				 
				 
				
				 
				if(searchForTags.size() == 1) {
					if(matches_tag1.size() > 0) {
						for(int i = 0; i < matches_tag1.size(); i++) {
							newAlbum.getPhotos().add(controls.makeCopyOfPhoto(matches_tag1.get(i)));
						}
					}else if(matches_tag2.size() > 0) {
						for(int i = 0; i < matches_tag2.size(); i++) {
							newAlbum.getPhotos().add(controls.makeCopyOfPhoto(matches_tag2.get(i)));
						}
					}
					
					
				}else {
				
				if(andOr.getText().equals("And")) {
					for(int i = 0; i < matches_tag1.size(); i++) {
						Photo p = matches_tag1.get(i);
						
						if(matches_tag2.contains(p)) {
							
							newAlbum.getPhotos().add(controls.makeCopyOfPhoto(p));
						}
					}
					
				}else {
					
					for(int i = 0; i < matches_tag2.size(); i++) {
						Photo p = matches_tag2.get(i);
						
						if(!matches_tag1.contains(p)) {
							matches_tag1.add(p);
						}
					}
					for(int i = 0; i < matches_tag1.size(); i++) {
						newAlbum.getPhotos().add(controls.makeCopyOfPhoto(matches_tag1.get(i)));
					}
				}
				}
				
			 }
			 
			 
			 if(newAlbum.getPhotos().size() == 0) {
					controls.showError("No Matches Found.");
					return;
				}
			 

				controls.getUser().getAlbums().add(newAlbum);
				controls.setActiveAlbum(newAlbum);
				controls.gotoAlbumView();
				return;
			 
			 
		 }
    }
    
   
    /**
     * Returns a list of photos which fall within the specified date range (inclusive)
     * @param sCal Start date
     * @param eCal End date
     * @return ArrayList of photos that fall within the date range inclusively
     */
    private ArrayList<Photo> matchByDate(Calendar sCal, Calendar eCal){
    	ArrayList<Photo> found = new ArrayList<Photo>();
    	
    	ArrayList<Album> albums = controls.getUser().getAlbums();
    	
    	for(int i = 0; i < albums.size(); i++) {
    		ArrayList<Photo> photos = albums.get(i).getPhotos();
    		
    		for(int j = 0; j < photos.size(); j++) {
    			Calendar c = photos.get(j).getCalendar();
    			if(c.equals(sCal) || c.equals(eCal) || (c.before(eCal) && c.after(sCal))) {
    				found.add(photos.get(j));
    			}
    		}
    	}
    	return found;
    }
    
    
    /**
     * Searches for photos with the specified tag
     * @param searchTag Tag to search for
     * @return List of photos that contain the specified tag
     */
    private ArrayList<Photo> matchByTag(Tag searchTag){
    	ArrayList<Photo> found = new ArrayList<Photo>();
    	
    	ArrayList<Album> albums = controls.getUser().getAlbums();
    	
    	for(int i = 0; i < albums.size(); i++) {
    		ArrayList<Photo> photos = albums.get(i).getPhotos();
    		
    		for(int j = 0; j < photos.size(); j++) {
    			ArrayList<Tag> tags = photos.get(j).getTags();
    			
    			for(int k = 0; k < tags.size(); k++) {
    				Tag tag = tags.get(k);
    				
    				if(tag.getKey().equals(searchTag.getKey())) {
    					String[] split_tag = tag.getValue().split(",");
    					
    					for(int l = 0; l < split_tag.length; l++) {
    						if(split_tag[l].equals(searchTag.getValue())) {
    							found.add(photos.get(j));
    						}
    					}
    				}
    			}
    		}
    	}
    	return found;
    }
    
    /**
     * Determines whether or not an album with a specified name already exists
     * @param name Name to search for in existing albums
     * @return True if album with specified names already exists, otherwise false
     */
    private boolean AlbumExists(String name) {
		 for(int i = 0; i < controls.getUser().getAlbums().size(); i++) {
			 if(controls.getUser().getAlbums().get(i).getName().equals(name)) {
				 return true;
			 }
		 }
		 return false;
	 }
}
