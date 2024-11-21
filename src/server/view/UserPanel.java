package server.view;

import server.controller.ServerController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserPanel {
    private JPanel pnlUsers;
    private JScrollPane jspUsers;
    private JTextArea txaUsers;
    private JButton btnAllUsers;
    private JButton connectedUsersButton;
    private JLabel lblTitle;
    private ServerController controller;

    public UserPanel(ServerController controller){
        this.controller = controller;

        btnAllUsers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               populateRegisteredUsers();
            }
        });
        connectedUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               populateConnectedUsers();
            }
        });
    }

    public void populateConnectedUsers(){
        txaUsers.setText("");
        lblTitle.setText("Connected Users");
        controller.populateConnectedUsers();
    }

    public void populateRegisteredUsers(){
        txaUsers.setText("");
        lblTitle.setText("All Users");
        controller.populateRegisteredUsers();
    }

    public JTextArea getTxaUsers() {
        return txaUsers;
    }

    public JPanel getPnlUsers() {
        return pnlUsers;
    }
}
