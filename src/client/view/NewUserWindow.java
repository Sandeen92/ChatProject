package client.view;

import client.controller.ClientController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class NewUserWindow extends JFrame {

    private LoginPanel loginPanel;
    private ClientController clientController;
    private JPanel newUserPanel;
    private JTextField txfUsername;
    private JButton btnRegister;
    private JTextField txfProfilePicture;
    private JButton btnChooseImage;
    private String fileName;
    private InputStream fis = null;
    private OutputStream fos = null;

    public NewUserWindow(ClientController clientController) {
        this.clientController = clientController;
        setUpFrame();
    }

    public void setUpFrame() {
        setTitle("Chat Client New User");
        setSize(400, 300);
        setLocation(1000, 100);
        setVisible(true);
        setContentPane(newUserPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        buttons();
        this.revalidate();
    }

    private void buttons() {
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!txfUsername.getText().equals("")){
                    createUser();
                    dispose();
                }
            }
        });

        btnChooseImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txfProfilePicture.setText(clientController.getProfilePicture());
            }
        });
    }

    private void createUser() {
        clientController.saveNewProfilePicture(txfUsername.getText());
        clientController.createUser(txfUsername.getText());

    }
}