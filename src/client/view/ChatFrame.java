/**
 * This class is responsible for the frame of the client
 */
package client.view;

import client.controller.ClientController;

import javax.swing.*;

public class ChatFrame extends JFrame {

    private ChatPanel chatPanel;
    private ClientController clientController;

    /**
     * Constructor for the ChatFrame
     * @param clientController
     */
    public ChatFrame(ClientController clientController){
        this.clientController = clientController;
        chatPanel = new ChatPanel(this, clientController);
        setUpFrame();
    }

    /**
     * This method configures the frame and sets it up
     */
    public void setUpFrame(){
        setSize(910, 610);
        setLocation(400, 200);
        setVisible(true);
        setContentPane(chatPanel.getChatPanel());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        this.revalidate();
    }

    /**
     * This method returns the chatpanel
     * @return
     */
    public ChatPanel getChatPanel() {return chatPanel;}
}
