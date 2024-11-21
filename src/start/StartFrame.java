package start;

import client.controller.ClientController;
import server.controller.ServerController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartFrame extends JFrame{
    private JPanel pnlStart;
    private JButton serverButton;
    private JButton clientButton;

    public StartFrame(){
        setUpFrame();

    }


    public void setUpFrame(){
        //setTitle("Chatzz :>");
        setSize(100, 200);
        setLocation(400, 200);
        setVisible(true);
        setContentPane(pnlStart);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        buttons();
        this.revalidate();
    }

    public void buttons(){
        serverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ServerController();
                serverButton.setEnabled(false);
            }
        });
        clientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ClientController();
            }
        });
    }
}
