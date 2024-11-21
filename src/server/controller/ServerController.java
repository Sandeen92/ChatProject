/**
 * This class is responsible for handeling communication between server and trafficlog and view
 */
package server.controller;

import client.model.User;
import server.model.*;
import server.view.MainFrame;
import server.view.TrafficLogPanel;

import java.time.LocalDateTime;

public class ServerController {

    private MainFrame view;
    private TrafficLogController tlc;
    private TrafficLogPanel tlp;
    private UserStorage userStorage = new UserStorage();
    private ConnectedUsers connectedUsers = new ConnectedUsers();
    private MessageManager mm;

    /**
     * Constructor for servercontroller
     */
    public ServerController(){
        tlp = new TrafficLogPanel(this);
        view = new MainFrame(this, tlp);
        tlc = new TrafficLogController(this, tlp);
        mm = new MessageManager();
        tlp.setTrafficLogController(tlc);
    }

    /**
     * This method adds a String to the Traffic Log. It then refreshes the GUI with the new String.
     * @param s Chosen string to display in the Traffic Log.
     */
    public void addLogMessage(String s){
        tlc.addToTrafficLog(s);
        view.getMainPanel().getTrafficLogPanel().populate(tlc.getTrafficLog().toString());
    }


    /**
     * This method searches the trafficlog between two different dates
     * @param searchFrom
     * @param searchTo
     */

    public void refreshTrafficLog(){
        view.getMainPanel().getTrafficLogPanel().populate(tlc.getTrafficLog().toString());
    }


    public void searchTrafficLog(LocalDateTime searchFrom, LocalDateTime searchTo){
        String s = tlc.searchTrafficLog(searchFrom, searchTo);
        view.populateWithSearch(s);
    }

    public void populateConnectedUsers() {
        for(User u : getConnectedUsers().getConnectedUsers()){
            view.getMainPanel().getUserPanel().getTxaUsers().append(u.getName()+"\n");
        }
    }

    public void populateRegisteredUsers() {
        for(User u : getUserStorage().getRegisteredUsers()){
            view.getMainPanel().getUserPanel().getTxaUsers().append(u.getName()+"\n");
        }
    }

    /**
     * Returns connected users
     * @return
     */
    public ConnectedUsers getConnectedUsers() {
        return connectedUsers;
    }

    /**
     * Updates connected users
     */
    public void updateConnectedUsers(){
        view.getMainPanel().getUserPanel().populateConnectedUsers();
    }

    /**
     * returns the UserStorage object
     * @return
     */
    public UserStorage getUserStorage() {
        return userStorage;
    }

    /**
     * Returns the MessageManager object
     * @return
     */
    public MessageManager getMm() {
        return mm;
    }
}
