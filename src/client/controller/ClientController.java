/**
 * This class is responsible for handeling the logic for the client
 */
package client.controller;

import client.model.Message;
import client.view.ChatFrame;
import client.view.LoginFrame;
import client.model.User;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;


public class ClientController {

    public static final java.lang.String ip = "127.0.0.1";
    public static final int port = 3465;
    public static final java.lang.String defaultPicPath = "images/default.jpg";
    private LoginFrame view;
    private ClientConnection clientConnection;
    private ChatFrame chatFrame;
    private ArrayList<User> onlineUsers;
    private String fileName;
    private User user;

    /**
     * Constructor that creates the loginframe and initialises the onlineuser array
     */
    public ClientController(){
        view = new LoginFrame(this);
        onlineUsers = new ArrayList<>();
    }

    /**
     * This method takes in the user that is logging in and connects it to the server
     * @param user The user that logs in
     * @throws IOException
     */
    public void logIn(User user) throws IOException {
        clientConnection = new ClientConnection(user, this);
        clientConnection.connect();
    }

    /**
     * This method connects a user that is newly registered
     * @param user
     */
    public void connectAndRegister(User user){
        clientConnection = new ClientConnection(user, this);
        clientConnection.connectAndRegister(user);
    }

    /**
     * This method returns the client connection
     * @return ClientConnection
     */
    public ClientConnection getClientConnection() {
        return this.clientConnection;
    }

    /**
     * This method sets the online user list
     * @param onlineUsers
     */
    public void setOnlineUsers(ArrayList<User> onlineUsers){
        this.onlineUsers = onlineUsers;
    }

    /**
     * This method starts the chat frame
     * @param user
     */
    public void startChatFrame(User user) {
        chatFrame = new ChatFrame(this);
        view.dispose();
        user.readSavedContacts();
    }

    /**
     * This method takes in a message and calls the method to send it
     * @param message
     */
    public void sendMessage(Message message){
        clientConnection.getConnection().sendMessage(message);
    }

    /**
     * This method sends an array of users to the server
     * @param users
     */
    public void sendUsersForChatHistory(User[] users){
        clientConnection.getConnection().sendUsersForChatHistory(users);
    }

    /**
     * This method sets up the chat
     * @param user
     */
    public void setupChat(User user){
        this.user = user;
        chatFrame.getChatPanel().setupChat(user.getName());
        chatFrame.getChatPanel().setProfilePicture(generateProfilePicture(user));
    }

    private ImageIcon generateProfilePicture(User user){
        ImageIcon img = new ImageIcon(user.getProfilePicture().getImage());
        Image scaleImage = img.getImage().getScaledInstance(80, 80,Image.SCALE_DEFAULT);
        ImageIcon newImg = new ImageIcon(scaleImage);
        return newImg;
    }

    /**
     * This method calls the method to update the online users list
     * @param onlineUsers
     */
    public void updateOnlinelist(ArrayList<?> onlineUsers) {
        updateOnlineUsers((ArrayList<User>) onlineUsers);
    }

    /**
     * This method updates the messages in the current chat
     * @param messageList
     */
    public void updateChatMessages(LinkedList<Message> messageList) {
        showChatMessages(messageList);
    }

    /**
     * This method returns the LoginFrame
     * @return
     */
    public LoginFrame getView() {
        return view;
    }

    /**
     * This method returns the ChatFrame
     * @return
     */

    public ChatFrame getChatFrame() {
        return chatFrame;
    }

    /**
     * This method returns the currently stored online users on the client side
     * @return
     */
    public ArrayList<User> getOnlineUsers() {
        return onlineUsers;
    }

    public void createUser(String username) {
        User user = new User(username, new ImageIcon("images/" + username + ".jpg"));
        connectAndRegister(user);
    }

    public void saveNewProfilePicture(String username) {
        File inputImage = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;

        if (!(fileName == null)) {
            inputImage = new File(fileName);
        }
        else {
            inputImage = new File("images/default.jpg");
        }

        File outputImage = new File("images/" + username + ".jpg");

        try {
            fis = new FileInputStream(inputImage);
            fos = new FileOutputStream(outputImage);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            fos.flush();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }

                if (fos != null) {
                    fos.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getProfilePicture() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("jpg", "jpg"));
        fileChooser.setAcceptAllFileFilterUsed(true);
        int dlg = fileChooser.showOpenDialog(null);

        if (dlg == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            fileName = file.getPath();
            return fileName;
        }
        return null;
    }

    public void connectClient(String username){
        try {
            User user = new User(username);
            logIn(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateChatInfo(int choice){

        if(choice == 1) {
            User userToGet = (User) chatFrame.getChatPanel().getListOnlineUsers().getSelectedValue();
            ImageIcon img = new ImageIcon(userToGet.getProfilePicture().getImage());
            Image scaleImage = img.getImage().getScaledInstance(80, 80,Image.SCALE_DEFAULT);
            ImageIcon newImg = new ImageIcon(scaleImage);
            chatFrame.getChatPanel().updateChatInfo(newImg, userToGet.getName());
            getChatHistory();
        }
        else {
            User userToGet = (User) chatFrame.getChatPanel().getListContacts().getSelectedValue();
            ImageIcon img = new ImageIcon(userToGet.getProfilePicture().getImage());
            Image scaleImage = img.getImage().getScaledInstance(80, 80,Image.SCALE_DEFAULT);
            ImageIcon newImg = new ImageIcon(scaleImage);
            chatFrame.getChatPanel().updateChatInfo(newImg, userToGet.getName());
            getChatHistory();
        }


    }

    public void getChatHistory(){
        User[] usersToCompare = new User[2];
        usersToCompare[0] = user;
        if(chatFrame.getChatPanel().getListOnlineUsers().getSelectedValue() != null){
            usersToCompare[1] = (User) chatFrame.getChatPanel().getListOnlineUsers().getSelectedValue();
        } else {
            usersToCompare[1] = (User) chatFrame.getChatPanel().getListContacts().getSelectedValue();
        }
        sendUsersForChatHistory(usersToCompare);
    }

    public void newMessage(String text, ImageIcon image){

        User sender = user;
        User receiver = null;
        if(chatFrame.getChatPanel().getListOnlineUsers().getSelectedValue() != null){
            receiver = (User) chatFrame.getChatPanel().getListOnlineUsers().getSelectedValue();
        } else {
            receiver = (User) chatFrame.getChatPanel().getListContacts().getSelectedValue();
        }


        Message message = new Message(text, image, sender, receiver);
        sendMessage(message);
        chatFrame.getChatPanel().setTxfChatText("");
        getChatHistory();

    }

    public String dateFormat(LocalDateTime dateAndTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm");
        return dateAndTime.format(formatter);
    }

    public String imageDateFormatter(LocalDateTime dateAndTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssnn");
        return dateAndTime.format(formatter);
    }

    public void sendMultiMessage(String text, ImageIcon image) {

        User sender = user;

        for (int i = 0; i < chatFrame.getChatPanel().getCmbMultiMessageReceiverList().getItemCount(); i++) {
            User receiver = (User) chatFrame.getChatPanel().getCmbMultiMessageReceiverList().getItemAt(i);
            System.out.println("SEND MULTI: " + receiver.getName());
            Message message = new Message(text, image, sender, receiver);
            sendMessage(message);
            chatFrame.getChatPanel().getTxfMultiMessageText().setText("");

            getChatHistory();
        }
    }

    public void showChatMessages(LinkedList<Message> messageList) {

        DefaultListModel dlm = new DefaultListModel<>();
        chatFrame.getChatPanel().getChatModel().removeAllElements();

        for (Message m : messageList) {
            if (m.getImage() != null) {
                if(m.getText().equals("")){
                    dlm.addElement(String.format("[%s] %s:\n", dateFormat(m.getDate()), m.getSender()));
                }
                else{
                    dlm.addElement(String.format("[%s] %s: %s\n", dateFormat(m.getDate()), m.getSender(), m.getText()));
                    dlm.addElement(String.format("[%s] %s:\n", dateFormat(m.getDate()), m.getSender()));
                }
                dlm.addElement(formatImage(m.getImage()));
            }
            else{
                dlm.addElement(String.format("[%s] %s: %s\n", dateFormat(m.getDate()), m.getSender(), m.getText()));
            }
        }
        chatFrame.getChatPanel().getListChat().setModel(dlm);
        chatFrame.getChatPanel().updateChatPosition();
    }

    public ImageIcon formatImage(ImageIcon img) {
        Image scaleImage = img.getImage().getScaledInstance(150, 80,Image.SCALE_DEFAULT);
        ImageIcon newImg = new ImageIcon(scaleImage);
        return newImg;
    }

    public void addToContacts() {
        User selectedUser = (User) chatFrame.getChatPanel().getListOnlineUsers().getSelectedValue();
        user.addContact(selectedUser);
        System.out.println("ADD: " + selectedUser + " selected.");
        updateContactsList(user.getContacts());
    }

    public void removeFromContacts() {
        User selectedUser = (User) chatFrame.getChatPanel().getListContacts().getSelectedValue();
        user.removeContact(selectedUser);
        System.out.println("REMOVE: " + selectedUser + " selected.");
        updateContactsList(user.getContacts());
    }

    public void updateContactsList(ArrayList<User> contactsList){
        chatFrame.getChatPanel().clearContactsList();

        for (int i = 0; i < contactsList.size(); i++) {
            if(!contactsList.get(i).getName().equals(user.getName())){
                chatFrame.getChatPanel().getContactsListModel().addElement(contactsList.get(i));
                System.out.println(contactsList.get(i).getName());
            }
        }
        chatFrame.getChatPanel().getListContacts().setModel(chatFrame.getChatPanel().getContactsListModel());
    }

    public void updateOnlineUsers(ArrayList<User> onlineUsers){
        chatFrame.getChatPanel().clearOnlineList();

        for (int i = 0; i < onlineUsers.size(); i++) {
            if(!onlineUsers.get(i).getName().equals(user.getName())){
                chatFrame.getChatPanel().getOnlineUsersListModel().addElement(onlineUsers.get(i));
            }
        }
        chatFrame.getChatPanel().getListOnlineUsers().setModel(chatFrame.getChatPanel().getOnlineUsersListModel());
    }

    public User getSelectedChat() {
        User temp = null;
        if(chatFrame.getChatPanel().getListOnlineUsers().getSelectedValue() != null){
            return (User) chatFrame.getChatPanel().getListOnlineUsers().getSelectedValue();
        } else {
            return (User) chatFrame.getChatPanel().getListContacts().getSelectedValue();
        }

    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public void addToMulitMessageReceiverList() {
        User sendToUser = null;
        if(chatFrame.getChatPanel().getListOnlineUsers().getSelectedValue() !=null){
            sendToUser = (User) chatFrame.getChatPanel().getListOnlineUsers().getSelectedValue();
        } else {
            sendToUser = (User) chatFrame.getChatPanel().getListContacts().getSelectedValue();
        }


        JComboBox<User> receiverList = chatFrame.getChatPanel().getCmbMultiMessageReceiverList();
        boolean userAlreadyAdded = false;

        if(receiverList.getItemCount() == 0){
            chatFrame.getChatPanel().getCmbMultiMessageReceiverList().addItem((User) chatFrame.getChatPanel().getListOnlineUsers().getSelectedValue());
        }
        else {
            for (int i = 0; i < receiverList.getItemCount(); i++) {
                User existingUser = receiverList.getItemAt(i);

                if (existingUser.equals(sendToUser)) {
                    userAlreadyAdded = true;
                    break;
                }
            }

            if (!userAlreadyAdded) {
                receiverList.addItem(sendToUser);
            } else {
                System.out.println("User already added to multimessage");
            }
        }

    }
}
