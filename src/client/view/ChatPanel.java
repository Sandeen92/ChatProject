package client.view;

import client.controller.ClientController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;

public class ChatPanel {
    private JPanel chatPanel;
    private JPanel topPanel;
    private JPanel pnlMultiMessage;
    private JPanel centerPanel;
    private JPanel pnlChat;
    private JPanel pnlUsers;
    private JList listOnlineUsers;
    private JList listContacts;
    private JButton btnAddToContacts;
    private JButton btnRemoveFromContacts;
    private JTextField txfChatMessageText;
    private JButton btnChatSend;
    private JButton btnChatSelectImage;
    private JLabel lblChatProfilePicture;
    private JPanel pnlChatInfo;
    private JComboBox cmbMultiMessageReceiverList;
    private JButton btnMultiMessageAddToReceiverList;
    private JTextField txfMultiMessageText;
    private JButton btnMultiMessageRemoveFromReceiverList;
    private JButton btnMultiMessageSelectImage;
    private JButton btnMultiMessageSend;
    private JPanel pnlProfile;
    private JLabel lblMyProfilePicture;
    private JButton button1;
    private JButton button3;
    private JButton btnDisconnect;
    private JScrollPane jspChat;
    private JPanel pnlOnlineUsers;
    private JScrollPane jspOnlineUsers;
    private JPanel pnlContacts;
    private JScrollPane jspContacts;
    private JPanel pnlProfileInfo;
    private JLabel lblMyName;
    private JLabel lblMyIP;
    private JPanel pnlProfileButtons;
    private JLabel lblChatUsername;
    private JList listChat;
    private JButton btnChat;
    private ChatFrame frame;
    private ClientController clientController;
    private DefaultListModel onlineUsersListModel = new DefaultListModel();
    private DefaultListModel contactsListModel = new DefaultListModel();
    private ImageIcon imgToSend;
    private DefaultListModel chatModel = new DefaultListModel();


    public ChatPanel(ChatFrame frame, ClientController clientController){
        this.frame = frame;
        this.clientController = clientController;

        buttons();


    }

    public void setupChat(String username){
        lblMyName.setText("NAME : " + username);
        lblMyIP.setText("IP : " + clientController.getClientConnection().getInetAddress().getHostAddress());
    }


    public JPanel getChatPanel() {
        return chatPanel;
    }

    public void updateChatInfo(ImageIcon img, String username){
        lblChatProfilePicture.setIcon(img);
        lblChatUsername.setText(username);
    }

    public void getSelectedImage(){

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("jpg", "jpg"));
        fileChooser.setAcceptAllFileFilterUsed(true);
        int dlg = fileChooser.showOpenDialog(null);

        if (dlg == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            imgToSend = new ImageIcon(file.getAbsolutePath());
        }
    }

    private void buttons() {
        btnChatSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    clientController.newMessage(txfChatMessageText.getText(), imgToSend);
                    imgToSend = null;
            }
        });


        btnChatSelectImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSelectedImage();
            }
        });



        btnDisconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientController.getClientConnection().disconnect();
                frame.dispose();

            }
        });


        listOnlineUsers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                listContacts.clearSelection();
            }


        });

        listContacts.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                listOnlineUsers.clearSelection();
            }


        });


        btnMultiMessageAddToReceiverList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientController.addToMulitMessageReceiverList();

            }
        });

        btnMultiMessageRemoveFromReceiverList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cmbMultiMessageReceiverList.getSelectedIndex() != -1){
                    cmbMultiMessageReceiverList.removeItemAt(cmbMultiMessageReceiverList.getSelectedIndex());
                }
            }
        });

        btnMultiMessageSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(cmbMultiMessageReceiverList.getItemCount() > 0) {
                    clientController.sendMultiMessage(txfMultiMessageText.getText(), imgToSend);
                    imgToSend = null;
                }
            }
        });

        btnMultiMessageSelectImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSelectedImage();
            }
        });

        btnAddToContacts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listOnlineUsers.getSelectedIndex() != -1){
                    clientController.addToContacts();
                }
            }
        });

        btnRemoveFromContacts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listContacts.getSelectedIndex() != -1) {
                    clientController.removeFromContacts();
                }
            }
        });

        btnChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listOnlineUsers.getSelectedIndex() != -1) {
                    clientController.updateChatInfo(1);
                }
                else if(listContacts.getSelectedIndex() != -1) {
                    clientController.updateChatInfo(2);
                }
            }
        });
    }

    public void clearContactsList() {
        contactsListModel.removeAllElements();
        listContacts.removeAll();
    }

    public void updateChatPosition() {
        int size = listChat.getModel().getSize();
        listChat.ensureIndexIsVisible(size-1);
    }




    public void clearOnlineList() {
        onlineUsersListModel.removeAllElements();
        listOnlineUsers.removeAll();
    }

    public void setTxfChatText(String text) {
        txfChatMessageText.setText(text);
    }
    public void setProfilePicture(ImageIcon profilePicture) {
        lblMyProfilePicture.setIcon(profilePicture);
    }
    public JList getListOnlineUsers() {
        return listOnlineUsers;
    }
    public JComboBox getCmbMultiMessageReceiverList() {
        return cmbMultiMessageReceiverList;
    }

    public JList getListContacts() {
        return listContacts;
    }

    public DefaultListModel getOnlineUsersListModel() {
        return onlineUsersListModel;
    }

    public void setOnlineUsersListModel(DefaultListModel onlineUsersListModel) {
        this.onlineUsersListModel = onlineUsersListModel;
    }

    public DefaultListModel getContactsListModel() {
        return contactsListModel;
    }

    public void setContactsListModel(DefaultListModel contactsListModel) {
        this.contactsListModel = contactsListModel;
    }

    public JTextField getTxfMultiMessageText() {
        return txfMultiMessageText;
    }

    public void setTxfMultiMessageText(JTextField txfMultiMessageText) {
        this.txfMultiMessageText = txfMultiMessageText;
    }

    public JList getListChat() {
        return listChat;
    }

    public void setListChat(JList listChat) {
        this.listChat = listChat;
    }

    public DefaultListModel getChatModel() {
        return chatModel;
    }

    public void setChatModel(DefaultListModel chatModel) {
        this.chatModel = chatModel;
    }
}



