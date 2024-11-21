/**
 * This class represents a user and contains all the needed data
 */
package client.model;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class User implements Serializable {

    private static final long serialVersionUID = 7070632216337793430L;
    private String name;
    private ImageIcon profilePicture;
    private ArrayList<User> contacts = new ArrayList<>();
    private String userPath;
    private String absolutePath;
    private File directory;
    private File savedContacts;

    /**
     * Constructor for user
     * @param name
     */
    public User(String name){
        this.name = name;
        this.profilePicture = new ImageIcon("images/default.jpg");
    }

    /**
     * Second Constructor for user
     * @param name
     * @param profilePicture
     */
    public User(String name, ImageIcon profilePicture){
        this.name = name;
        this.profilePicture = profilePicture;
    }

    /**
     * This method adds a user to the users stored contacts
     * @param user
     */
    public void addContact(User user){
        boolean exists = false;
        for (User u : contacts){
            if (u.getName().equals(user.getName())){
                exists = true;
            }
        }
        if (!exists){
            contacts.add(user);
            writeSavedContacts();
        }
    }

    /**
     * This method removes a user from the stored contacts
     * @param user
     */
    public void removeContact(User user){
        for (int i=0; i<contacts.size(); i++){
            if (contacts.get(i) == user){
                contacts.remove(i);
            }
        }
      writeSavedContacts();
    }


    public void writeSavedContacts() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(savedContacts, false)));
            oos.writeObject(contacts);
            oos.flush();
            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in User - writeSavedContacts()");
        }
    }


    public void readSavedContacts(){
        try {
            userPath = System.getProperty("user.dir") + "\\files\\" + name;
            absolutePath = System.getProperty("user.dir") + "\\files\\" + name + "\\savedContacts.txt";
            Path p = Paths.get(userPath);
            if (!Files.exists(p)){
                Files.createDirectory(p);
                directory = new File(userPath);
            }
            savedContacts = new File(absolutePath);

            if (!savedContacts.exists()){
                savedContacts.createNewFile();
            }

            if(savedContacts.length()!=0) {
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(savedContacts)));
                try{
                    contacts = (ArrayList<User>) ois.readObject();
                } catch (EOFException e){
                    ois.close();
                }
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error in readSavedContacts()");
        }
    }

    /**
     * This method returns the name as a string
     * @return
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Getters and setters
     * @return
     */
    public java.lang.String getName() {return name;}

    public ImageIcon getProfilePicture() {return profilePicture;}

    public ArrayList<User> getContacts() {return contacts;}
}
