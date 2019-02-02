package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Tag;

/**
 * FXML Controller for tag management
 * @author Ryan Sadofsky
 *
 */
public class TagController{
	/**
	 * Delegated instance of ConMethods
	 */
	private static ConMethods controls = new ConMethods();
	
	@FXML
	ListView<Tag> list_tags;
	
	@FXML
	Button button_back;
	
	@FXML
	Button button_addTag;
	
	@FXML
	Button button_editTag;
	
	@FXML
	Button button_removeTag;
	
	@FXML
	Button button_confirm;
	
	@FXML
	Button button_cancel;
	
	@FXML
	ComboBox<String> combo_tagType;
	
	@FXML
	TextField text_tagValue;
	
	
	/**
	 * Observable list of tags for the current active photo
	 */
	ObservableList<Tag> tags = FXCollections.observableArrayList();
	
	/**
	 * Observable list of tag types, including default types and any added by the user
	 */
	ObservableList<String> tagTypes = FXCollections.observableArrayList();
	
	/**
	 * True if the user is adding a new tag, otherwise false
	 */
	private boolean isAdding = false;
	
	/**
	 * True if the user is editing an existing tag, otherwise false
	 */
	private boolean isEditing = false;

	
	/**
	 * Initializes the controller by populating the tags and tagTypes lists, and disables buttons into a 'read only' mode
	 */
	@FXML
	void initialize() {
		tags.addAll(controls.getActivePhoto().getTags());
		tagTypes.addAll(controls.getUser().getTagTypes());
		list_tags.setItems(tags);
		combo_tagType.setItems(tagTypes);
		
		setModeReadOnly();
		combo_tagType.setEditable(true);
		
		list_tags.getSelectionModel().selectedItemProperty().addListener( (tag, oldTag, newTag) ->
    	changeAction(newTag)
    	);
	}
	
	
   /**
    * Button event handling
	* @param event Event which called ButtonAction
	*/
	public void ButtonAction(ActionEvent event) {
		 Button b = (Button) event.getSource();
		 controls.setStage(b);
		 
		 Tag selected_tag = list_tags.getSelectionModel().getSelectedItem();
		 
		 if(b == button_back) {
			 controls.gotoAlbumView();
		 }
		 else if(b == button_addTag) {
			 isAdding = true;
			 setModeEditing();
			 combo_tagType.setValue("");
			 text_tagValue.clear();
			 return;
		 }
		 else if(b == button_editTag){
			 if(selected_tag == null) {
				 controls.showError("No tag selected.");
				 return;
			 }
			 isEditing = true;
			 setModeEditing();
			 return;
		 }
		 else if(b == button_removeTag) {
			 if(selected_tag == null) {
				 controls.showError("No tag selected.");
				 return;
			 }
			 int index = tags.indexOf(selected_tag);
			 controls.getActivePhoto().getTags().remove(selected_tag);
			 if(index != tags.size()-1) {
				 list_tags.getSelectionModel().select(index + 1);
			 }else if(index != 0) {
				 list_tags.getSelectionModel().select(index - 1);
			 }else {
				 combo_tagType.setValue("");
				 text_tagValue.clear();
			 }
		 }
		 else if(b == button_cancel) {
			 setModeReadOnly();
		 }
		 else if(b == button_confirm) {
			 if(!entryIsValid()) {
				return;
			 }
			 
			 String key = combo_tagType.getValue();
			 String value = text_tagValue.getText();
			 if(isAdding) {
				 controls.getActivePhoto().getTags().add(new Tag(key,value));
			 }
			 else if(isEditing) {
				selected_tag.setKey(key);
				selected_tag.setValue(value);
			 }
			 
			 if(!tagTypes.contains(key)) {
				 controls.getUser().getTagTypes().add(key);
			 }
			 setModeReadOnly();
		 }
		 refresh();
	}
	
	 /**
	  * Determines whether or not the entered tag is valid
	  * @return True if the entry is valid, otherwise false
	  */
	 private boolean entryIsValid() {
		 String key = combo_tagType.getValue();
		 String value = text_tagValue.getText();
		 String[] values = value.split(",");
		 for(int i = 0; i < values.length; i ++) {
			 if(key == null || values[i] == null || key.equals("") || values[i].equals("")) {
				 controls.showError("Cannot leave values blank.");
				 return false;
			 }else if(TagExists(key,values[i])) {
				 controls.showError("Tag already exists.");
				 return false;
			 }
		 }
		 return true;
	 }
	 
	 /**
	  * Called on by the change listener to display the currently selected tag's details
	  * @param tag New tag selected
	  */
	 private void changeAction(Tag tag) {
		 if(tag == null) {
			 return;
		 }
		 combo_tagType.setValue(tag.getKey());
		 text_tagValue.setText(tag.getValue());
		 setModeReadOnly();
	 }
	
	/**
	 * Refreshes to ensure new data is displayed
	 */
	 private void refresh() {
		 tags.clear();
		 tags.addAll(controls.getActivePhoto().getTags());
		 
		 tagTypes.clear();
		 tagTypes.addAll(controls.getUser().getTagTypes());
		 
		 combo_tagType.setItems(tagTypes);
		 FXCollections.sort(tags);
		 list_tags.setItems(tags);
	 }
	 
	 /**
	  * Enables/Disables buttons to enter an 'editing' mode
	  */
	 private void setModeEditing() {
		 	button_confirm.setDisable(false);
			button_cancel.setDisable(false);
			combo_tagType.setDisable(false);
			text_tagValue.setDisable(false);
			
			button_addTag.setDisable(true);
			button_editTag.setDisable(true);
			button_removeTag.setDisable(true);
	 }
	 
	 /**
	  * Enables/Disables buttons to enter a 'read only' mode
	  */
	 private void setModeReadOnly() {
		 	button_confirm.setDisable(true);
			button_cancel.setDisable(true);
			combo_tagType.setDisable(true);
			text_tagValue.setDisable(true);
			
			button_addTag.setDisable(false);
			button_editTag.setDisable(false);
			button_removeTag.setDisable(false);
			
			isAdding = false;
			isEditing = false;
	 }
	 
	 /**
	  * Checks if a key value pair already exists
	  * @param key tag key
	  * @param value tag value
	  * @return True if the key:value pair already exists, otherwise false
	  */
	 private boolean TagExists(String key, String value) {
		 for(int i = 0; i < tags.size(); i++) {
			 Tag t = tags.get(i);
			 if(t.getKey().equals(key)) {
				 String[] values = t.getValue().split(",");
				 for(int j = 0; j < values.length; j++) {
					 if(values[j].equals(value)) {
						 return true;
					 }
				 }
			 }
		 }
		 return false;
	 }
}
