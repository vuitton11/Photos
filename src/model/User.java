package model;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Class representation of a User of the application
 * @author Ryan Sadofsky
 *
 */
public class User implements Serializable{
	
	/**
	 * Username of this user
	 */
	String username;
	
	
	/**
	 * List of this user's albums
	 */
	public ArrayList<Album> albums = new ArrayList<Album>();
	
	/**
	 * List of this user's tag types (tag keys)
	 */
	private ArrayList<String> tagTypes = new ArrayList<String>();
	
	/**
	 * User constructor
	 * @param username Username to assign to this user
	 */
	public User(String username) {
		this.username = username;
		this.tagTypes.add("Person");
		this.tagTypes.add("Location");
		this.tagTypes.add("Event");
	}
	
	/**
	 * @return username of this user
	 */
	public String toString() {
		return username;
	}
	
	/**
	 * Retrieves username of this user
	 * @return username of this user
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * Retrieves albums of this user
	 * @return ArrayList of this user's albums
	 */
	public ArrayList<Album> getAlbums(){
		return this.albums;
	}
	
	/**
	 * Retrieves tag types of this user
	 * @return ArrayList of this user's tag types
	 */
	public ArrayList<String> getTagTypes(){
		return this.tagTypes;
	}
	
	/**
	 * Adds a new tag type as an option for when the user adds a new tag to a photo
	 * @param type String to add as a new key option for tags
	 */
	public void addTagType(String type) {
		this.tagTypes.add(type);
	}
}
