/**
 * This class is responsible for the LoginFrame
 */
package client.view;

import client.controller.ClientController;

import javax.swing.*;

public class LoginFrame extends JFrame {
    private LoginPanel loginPanel;
    private ClientController clientController;

    /**
     * Constructor for the LoginFrame
     * @param clientController
     */
    public LoginFrame(ClientController clientController){
        this.clientController = clientController;
        loginPanel = new LoginPanel(clientController);
        setUpFrame();
    }

    /**
     * This method sets up the LoginFrame
     */
    public void setUpFrame(){
        setTitle("Chat Client Login");
        setSize(300, 400);
        setLocation(900, 100);
        setVisible(true);
        setContentPane(loginPanel.getMainPanel());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        this.revalidate();
    }

    /**
     * Getter for LoginPanel
     * @return
     */
    public LoginPanel getLoginPanel() {
        return loginPanel;
    }
}
