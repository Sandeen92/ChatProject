/**
 * Class responsible for the server and manages the connections with the clients
 */

package server.controller;

import client.model.*;
import client.model.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class ChatServer extends Thread{

    private ServerSocket serverSocket;
    private ServerController serverController;
    private ArrayList<ClientHandler> clientHandlerPool;
    private int i = 0;

    /**
     * Constructor for the ChatServer
     * @param port
     * @param serverController
     * @throws IOException
     */
    public ChatServer(int port, ServerController serverController) throws IOException {
        this.serverController = serverController;
        clientHandlerPool = new ArrayList<ClientHandler>();
        serverSocket = new ServerSocket(port);
        serverController.addLogMessage("Server started! Port: " + port);
    }


    /**
     * This method starts a thread and calls the method for connecting
     */
    public void run(){
        connection();
    }

    /**
     * This method is responsible for accepting connections from clients and
     * putting them in the clienthandlerpool
     */
    public void connection() {
            try {
                while (true) {
                    serverController.addLogMessage("Servern väntar på nästa client");
                    Socket socket = serverSocket.accept();
                    serverController.addLogMessage("Servern tog emot en client");

                    if (socket!=null) {
                        serverController.addLogMessage("Client nr " + i + " accepted");
                        ClientHandler ch = new ClientHandler(socket, i);
                        clientHandlerPool.add(ch);
                        ch.start();
                        i++;
                    }
                }
            } catch (IOException e) {
                serverController.addLogMessage("Error in server connection: " + e.getMessage());
            }
    }

    /**
     * This method sends all clients the current onlineUsers list
     * @param onlineUsers
     */
    public void sendOnlineListToAllClients(ArrayList<User> onlineUsers){
        for(ClientHandler ch: clientHandlerPool){
            ch.sendArrayListToClient(onlineUsers);
        }
    }

    /**
     * This method sends out the Messages between 2 users
     * @param users
     */
    public void sendMessageHistoryToTwoClients(User[] users){
        for(ClientHandler ch: clientHandlerPool){
            if(ch.getUser().getName().equals(users[0].getName()) || ch.getUser().getName().equals(users[1].getName())){
                ch.sendMessageHistoryToClient(serverController.getMm().getAllMessagesFromTwoUsers(users[0],users[1]));
            }
        }
    }

    /**
     * This inner class is responsible for handeling communication with one client
     */
    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        private Object input;
        private User user;
        private int clientNumber;

        /**
         * Constructor for ClientHandler
         * @param socket
         * @param nr
         * @throws IOException
         */
        public ClientHandler(Socket socket, int nr) throws IOException {
            this.clientSocket = socket;
            this.clientNumber = nr;
        }

        /**
         * This method returns the user in the clienthandler
         * @return
         */
        public User getUser() {return user;}

        /**
         * This method sends a message to a specific stream
         * @param msg
         */
        public void sendMessageToSpecificOOS(Message msg){
            try {
                oos.writeObject(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * This method sends the message history to the client
         * @param messageHistory
         */
        public void sendMessageHistoryToClient(LinkedList<Message> messageHistory){
            try{
                oos.writeObject(messageHistory);
                oos.reset();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        /**
         * This method sends the online users list to the client
         * @param onlineUsers
         */
        public void sendArrayListToClient(ArrayList<User> onlineUsers){
            try{
                oos.writeObject(onlineUsers);
                oos.reset();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        /**
         * This method disconnects the clienthandler from the client
         */
        public void disconnect(){
            try {
                ois.close();
                oos.close();
                clientSocket.close();
                clientSocket = null;
                clientHandlerPool.remove(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

        /**
         * This method listens for incomming objects from the client and makes decisions based on
         * the object type
         */
        public void run() {
            try {

                this.oos = new ObjectOutputStream(clientSocket.getOutputStream());
                this.ois = new ObjectInputStream(clientSocket.getInputStream());

                while (true) {
                        input = ois.readObject();

                    if(input instanceof String userName){
                        if(serverController.getUserStorage().checkForUserByName(userName) != null){
                            this.user = serverController.getUserStorage().checkForUserByName(userName);
                            oos.writeObject(user);
                            serverController.getConnectedUsers().updateList(user);
                            serverController.updateConnectedUsers();
                            Thread.sleep(100);
                            sendOnlineListToAllClients(serverController.getConnectedUsers().getConnectedUsers());
                        } else {
                            oos.writeObject(user);
                            disconnect();
                        }
                    }

                    if (input instanceof User user){

                        boolean alreadyRegistered = serverController.getUserStorage().checkIfUserIsStored(user);
                        boolean alreadyOnline = serverController.getConnectedUsers().checkIfUserIsOnline(user);


                        if (!alreadyRegistered && !alreadyOnline){
                            serverController.addLogMessage("Servern har lagt till " + user.getName() + " i storage");
                            serverController.getUserStorage().addUser(user);
                            disconnect();
                            break;
                        }
                        else if (alreadyRegistered && !alreadyOnline) {
                            user = serverController.getUserStorage().getUserFromStorage(user.getName());
                            serverController.addLogMessage(user.getName() + " har nu igång sin lyssnare i servern");
                            serverController.getConnectedUsers().updateList(user);
                            serverController.updateConnectedUsers();
                        }
                        else if (alreadyRegistered && alreadyOnline){
                            if (user == this.user){
                                serverController.updateConnectedUsers();
                            }
                            else {
                                serverController.addLogMessage("Användaren är redan registrerad och online");
                            }
                        }
                    }

                    if (input instanceof Message msg){
                        msg.setSender(user);
                        serverController.addLogMessage("Från: " + msg.getSender() + "\nTill: "
                                + msg.getReceiver() + "\nMeddelande: " +msg.getText());
                        serverController.getMm().addMessageToStore(msg);
                        serverController.getMm().saveToFile();

                        for (int i = 0; i < clientHandlerPool.size(); i++) {
                            if (clientHandlerPool.get(i).getUser().getName().equals(msg.getReceiver().getName())) {
                                clientHandlerPool.get(i).sendMessageToSpecificOOS(msg);
                            }
                        }
                    }

                    if(input instanceof User[] users){
                        sendMessageHistoryToTwoClients(users);
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                clientHandlerPool.remove(this);
                serverController.addLogMessage("ClientHandler " + clientNumber + " dog");
                if (user !=null) {
                    serverController.addLogMessage(user.getName() + " tappade sin connection med servern");
                    serverController.getConnectedUsers().updateList(user);
                    serverController.updateConnectedUsers();
                    sendOnlineListToAllClients(serverController.getConnectedUsers().getConnectedUsers());
                }
                try {
                    if(ois != null){
                        ois.close();
                        clientSocket = null;
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }



}

