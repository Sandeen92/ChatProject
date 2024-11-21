package server.view;

import server.controller.ServerController;

import javax.swing.*;

public class MainFrame extends JFrame {

    MainPanel mainPanel;
    ServerController controller;

    public MainFrame(ServerController controller, TrafficLogPanel tlp){
        this.controller = controller;
        mainPanel = new MainPanel(controller, tlp);
        setUpFrame();
    }


    public void setUpFrame(){
        setTitle("Chat Server");
        setSize(900, 600);
        setVisible(true);
        setContentPane(mainPanel.getMainPanel());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        this.revalidate();
    }

    public MainPanel getMainPanel() {return mainPanel;}

    public void populateWithSearch(String s){
        mainPanel.getTrafficLogPanel().populate(s);
    }


}


