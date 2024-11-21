/**
 * This class is responsible for handeling the connection with the server
 */
package client.controller;

import client.model.Message;
import client.model.User;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static client.controller.ClientController.ip;
import static client.controller.ClientController.port;

public class ClientConnection {
    private Socket socket;
    private Connection connection;
    private java.lang.String userName;
    private User clientUser;
    private ClientController clientController;
    private InetAddress inetAddress;

    /**
     * Constructor that instantiates the private variables
     * @param clientUser
     * @param clientController
     */
    public ClientConnection(User clientUser, ClientController clientController) {
        this.clientController = clientController;
        this.userName = clientUser.getName();
        this.clientUser = clientUser;
        try {
            socket = new Socket(ip, port);
            inetAddress = socket.getLocalAddress();
        } catch (IOException e) {
            System.out.println("Can not connect");
        }
    }

    /**
     * This method returns the InetAddress
     * @return
     */
    public InetAddress getInetAddress() {
        return inetAddress;
    }

    /**
     * This method returns the username
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method tries to make a connection to the server
     * If not possible the method disconnects
     */
    public void connect() {
        if (connection == null) {
            try {
                connection = new Connection(socket);

            } catch (IOException e) {
                disconnect();
            }
        }
    }

    /**
     * This method connects to the server and sends over a new user
     * @param user
     */
    public void connectAndRegister(User user) {
        try {
            Socket socket = new Socket(ip, port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            oos.writeObject(user);
            disconnect();

        } catch (IOException e) {
            System.err.println("Error in ClientConnection - connectAndRegister() : " + e.getMessage());
        }
    }

    /**
     * This method disconnects the client from the server
     */
    public void disconnect() {
        try {
            connection = null;
            socket.close();
            System.out.println("disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This inner class handles the connection with the server
     * @return
     */
    public Connection getConnection() {
        return connection;
    }

    public class Connection {
        private Socket clientSocket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        /**
         * Contructor that initialises the socket and streams
         * @param socket
         * @throws IOException
         */
        public Connection(Socket socket) throws IOException {
            clientSocket = socket;
            if(clientSocket != null){
                ois = new ObjectInputStream(clientSocket.getInputStream());
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                logInClient();
            }
        }

        /**
         * This method sends the username to the server and then reads the user that is sent back by the server
         * to save as the current user
         */
        public void logInClient() {
            try {
                oos.writeObject(userName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                Object input = ois.readObject();
                if (input != null) {
                    clientUser = (User) input;
                    clientController.setUser(clientUser);
                    clientUser.readSavedContacts();
                    new Listener().start();
                    Runtime r = Runtime.getRuntime();
                    r.addShutdownHook(new ClientExitProtection());
                    clientController.startChatFrame(clientUser);
                    clientController.setupChat(clientUser);
                    clientController.updateContactsList(clientUser.getContacts());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }

        /**
         * This method sends a message to the server
         * @param msg
         */
        public void sendMessage(Message msg) {
            try {
                oos.writeObject(msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * This method sends the array of users to the server
         * @param users
         */
        public void sendUsersForChatHistory(User[] users){
            try{
                oos.writeObject(users);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * This inner class is responsible for listening for incomming objects from the server
         */
        private class Listener extends Thread {
            /**
             * This method contains a loop that runs and listens for an object from the server,
             * based on the object type it handles what to do
             */
            public void run() {
                try {
                    while (true) {
                        Object o = ois.readObject();

                        if (o instanceof ArrayList<?> onlineUsers) {
                            Thread.sleep(200);
                            clientController.updateOnlinelist(onlineUsers);
                        }

                        if(o instanceof LinkedList<?> messageHistory){
                            LinkedList<Message> temp = (LinkedList<Message>) messageHistory;
                            for (int i = 0; i<temp.size(); i++){
                                if (temp.get(i).isImageHasBeenShown()){
                                    temp.get(i).setImage(null);
                                }
                            }
                            if(clientController.getSelectedChat() != null){
                                if(temp.get(0).getSender().getName().equals(clientController.getSelectedChat().getName()) || temp.get(0).getReceiver().getName().equals(clientController.getSelectedChat().getName())) {
                                    clientController.updateChatMessages((LinkedList<Message>) messageHistory);
                                }
                            }
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        /**
         * This inner class makes sure that the client always disconnects
         */
        class ClientExitProtection extends Thread {
            /**
             * This method writes the user to the server
             */
            @Override
            public void run() {
                try {
                    oos.writeObject(clientUser);
                    oos.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
