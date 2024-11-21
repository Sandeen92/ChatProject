/**
 * This class saves and manages all messages sent to the server
 */
package server.model;

import client.model.Message;
import client.model.User;

import java.io.*;
import java.util.*;

public class MessageManager {
    private ArrayList <Message> allMessages;
    private LinkedList<Message> returnMessageList;
    private File mmStoredMessages = new File("files/mmStoresMessages.txt");

    /**
     * Constructor for MessageManager
     */
    public MessageManager(){
        allMessages = new ArrayList<>();
        returnMessageList = new LinkedList<>();
        readFromFile();
    }

    /**
     * This method saves the arraylist to the file
     */
    public synchronized void saveToFile(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(mmStoredMessages,false)));
            oos.writeObject(allMessages);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in MessageManager - saveToFile()");
        }
    }

    /**
     * This method reads the ArrayList from a file
     */
    public synchronized void readFromFile(){
        try {
            if (mmStoredMessages.length()!=0) {
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(mmStoredMessages)));
                allMessages = (ArrayList<Message>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error in MessageManager - readFromFile()");
        }
    }

    /**
     * This method saves all messages between two users in a new list and returns the list
     * @param user1
     * @param user2
     * @return
     */
    public LinkedList<Message> getAllMessagesFromTwoUsers(User user1, User user2){
       returnMessageList.clear();
       Message m = new Message("You are now chatting", null, user1, user2, null);
       returnMessageList.add(m);
        for(Message message : allMessages){
            if((message.getSender().getName().equals(user1.getName()) && message.getReceiver().getName().equals(user2.getName())) || (message.getSender().getName().equals(user2.getName()) && message.getReceiver().getName().equals(user1.getName())) ){
                returnMessageList.add(message);
            }
        }
        return returnMessageList;
    }

    /**
     * This method saves a message in the ArrayList
     * @param message
     */
    public void addMessageToStore(Message message){
        allMessages.add(message);
    }

}
