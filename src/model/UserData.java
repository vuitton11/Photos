package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;

import controllers.ConMethods;

/**
 * Class for generating, saving, and retrieving user data
 * @author Ryan Sadofsky
 *
 */
public class UserData {

/**
 * Delegated instance of ConMethods to reference application information
 */
private static ConMethods controls = new ConMethods();
    
	/**
	 * Sets user as a stock user if no stock save was found and goes to the application home page.
	 * Stock photos are loaded from within the application workspace.
	 */
	public void GenerateStockUser() {    	
    	String basePath = "file:/" + System.getProperty("user.dir") + "\\stock_photos\\";
    	
    	User u = new User("stock");
    	controls.setUser(u);
    	
    	Album a = new Album("Stock");
    	u.getAlbums().add(a);
    	
    	/*
    	a.getPhotos().add(setDateValues(new File(basePath + "Stock1.jpg")));
    	a.getPhotos().add(new Photo(basePath + "Stock2.jpg"));
    	a.getPhotos().add(new Photo(basePath + "Stock3.jpg"));
    	a.getPhotos().add(new Photo(basePath + "Stock4.jpg"));
    	a.getPhotos().add(new Photo(basePath + "Stock5.jpg"));
    	a.getPhotos().add(new Photo(basePath + "Stock6.gif"));
    	*/
    	
    	File testFile = new File(".\\stock_photos\\Stock1.jpg");
    	
    	if(!testFile.exists()) {
    		controls.showError("File DNE");
    	}
    	
    	a.getPhotos().add(setDateValues(new File(".\\stock_photos\\Stock1.jpg")));
    	a.getPhotos().add(setDateValues(new File(".\\stock_photos\\Stock2.jpg")));
    	a.getPhotos().add(setDateValues(new File(".\\stock_photos\\Stock3.jpg")));
    	a.getPhotos().add(setDateValues(new File(".\\stock_photos\\Stock4.jpg")));
    	a.getPhotos().add(setDateValues(new File(".\\stock_photos\\Stock5.jpg")));
    	a.getPhotos().add(setDateValues(new File(".\\stock_photos\\Stock6.gif")));
    	controls.gotoHomePage();
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
	
	
	/**
	 * Serializes the user data and outputs to a file named, [username].save
	 * @param u User object to serialize
	 */
	public void saveUserData(User u) {
		File file = new File(".\\saves\\" + u.getUsername() + ".save");
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(u);
			oos.close();
		}catch (Exception e){
			controls.showError("Unable to export user data.");
			System.exit(1);
		}
	}
    
	/**
	 * Attemps to load locally saved user data from within the application workspace
	 * If no save file is found, a new User is created.
	 * @param username Username to attempt to load user data to
	 */
    public void loadUserData(String username) {
		File file = new File(".\\saves\\" + username + ".save");
		if(!file.exists()) {
			if(username.equals("stock")) {
				GenerateStockUser();
				return;
			}
			controls.setUser(new User(username));
			return;
		}
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			User u = (User) ois.readObject();
			controls.setUser(u);
			ois.close();
		}catch (Exception e){
			controls.showError("Unable to import user data.");
			System.exit(1);
		}
	}
}
