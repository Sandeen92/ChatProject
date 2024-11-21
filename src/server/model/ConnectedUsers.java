/**
 * This class is responsible for storing users that are connected to the server
 */
package server.model;

import client.model.User;

import java.util.ArrayList;

public class ConnectedUsers {

    private ArrayList<User> connectedUsers = new ArrayList<>();


    public ConnectedUsers(){
    }
    /**
     * This method receives a user to set as offline if already online, and vice versa
     * @param user User-object to logout or login
     */
    public synchronized void updateList(User user){
        if (connectedUsers.isEmpty()){
            connectedUsers.add(user);
            System.out.println(user.getName() + " is the first one online!");
        }
        else {
            if (connectedUsers.contains(user)){
                connectedUsers.remove(user);
                System.out.println(user.getName() + " is now offline");
            }
            else {
                connectedUsers.add(user);
                System.out.println(user.getName() + " is now online");
            }
        }
    }

    /**
     * This method recieves a user and checks if the user is online
     * @param user
     * @return
     */
    public boolean checkIfUserIsOnline(User user) {

        for (int i=0; i<connectedUsers.size(); i++){
            if (connectedUsers.get(i).getName().equals(user.getName())){
                return true;
            }
        }
        return false;
    }

    /**
     * Getters and setters
     * @param index
     * @return
     */
    public User getUserAtIndex(int index){
        return connectedUsers.get(index);
    }

    public boolean isEmpty(){
        return connectedUsers.isEmpty();
    }

    public int size(){
        return connectedUsers.size();
    }

    public ArrayList<User> getConnectedUsers() {return connectedUsers;}
    public void setConnectedUsers(ArrayList<User> connectedUsers) {this.connectedUsers = connectedUsers;}

}
