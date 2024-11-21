package client.view;

import client.controller.ClientController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class LoginPanel {
    private JPanel mainPanel;
    private JTextField txfIP;
    private JTextField txfPort;
    private JButton btnConnect;
    private JTextField txfUsername;
    private JButton newUserButton;
    private ClientController clientController;


    public LoginPanel(ClientController clientController){
        this.clientController = clientController;
        buttons();
    }

    private void buttons() {
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientController.connectClient(txfUsername.getText());
            }
        });


        newUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewUserWindow nuw = new NewUserWindow(clientController);
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JTextField getTxfUsername() {
        return txfUsername;
    }
}
