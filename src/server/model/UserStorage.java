/**
 * This class is responsible for storing users that are registered both.
 * Use this when comparing new registrations so duplicate accounts cant be created.
 * Or when online users want to send messages to offline users.
 */
package server.model;

import client.model.User;

import java.io.*;
import java.util.ArrayList;

public class UserStorage {

    private ArrayList<User> registeredUsers = new ArrayList<>();
    private File storedUsers = new File ("files/storedUsers.txt");

    /**
     * Constructor that reads saved users from textfile at serverboot
     */
    public UserStorage(){
            try {
                if(storedUsers.length()!=0) {
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(storedUsers)));
                try{
                    registeredUsers = (ArrayList<User>) ois.readObject();
                } catch (EOFException e){
                    ois.close();
                }
                ois.close();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("Error in UserStorage contructor");
            }
        }

    /**
     * Adds a newly created User object to the registeredUsers array
     * @param user a User object with name and profile picture
     * @author Casper Johannesson
     */
    public void addUser(User user){
        registeredUsers.add(user);
        saveUsers();

        for (int i = 0; i< registeredUsers.size(); i++) {
            System.out.println(registeredUsers.get(i).getName() + " finns i UserStorage");
        }
    }

    /**
     * Use this method before server shutdown to save all the users that server collected during run-time.
     * Writes to textfile </storedUsers.txt>
     */

    public void saveUsers(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(storedUsers, false)));
            oos.writeObject(registeredUsers);
            oos.flush();
            oos.close();

        } catch (IOException e) {
           e.printStackTrace();
           System.out.println("Error in UserStorage - saveUsers()");
        }
    }

    /**
     * This method checks if the user is already stored in the UserStorage
     * @param user
     * @return
     */
    public boolean checkIfUserIsStored(User user){

        for (int i=0; i<registeredUsers.size(); i++){
            if (registeredUsers.get(i).getName().equals(user.getName())){
                return true;
            }
        }
        return false;
    }

    /**
     * This method searches for the user by name
     * @param userName
     * @return
     */
    public User checkForUserByName(String userName){
        for(User user : registeredUsers){
            if(userName.equals(user.getName())){
                return user;
            }
        }
        return null;
    }

    /**
     * This method gets a user from the userStorage
     * @param stringName
     * @return
     */
    public User getUserFromStorage(String stringName){
        User user = null;
        for (int i = 0; i< registeredUsers.size(); i++){
            if (registeredUsers.get(i).getName().equals(stringName)){
                user = registeredUsers.get(i);
            }
        }
        return user;
    }


    public int size(){return registeredUsers.size();}

    public ArrayList<User> getRegisteredUsers() {return registeredUsers;}
}
