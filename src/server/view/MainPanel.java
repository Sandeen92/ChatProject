package server.view;

import server.controller.ServerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel{
    private JPanel mainPanel;
    private JPanel centerPanel;
    private JPanel pnlTraffic;
    private JPanel pnlUsers;
    private JPanel pnlControl;
    private ServerController controller;
    private TrafficLogPanel trafficLogPanel;
    private UserPanel userPanel;
    private ControlPanel controlPanel;


    public MainPanel(ServerController controller, TrafficLogPanel tlp){
        this.controller = controller;
        trafficLogPanel = tlp;
        userPanel = new UserPanel(controller);
        controlPanel = new ControlPanel(controller);
        setUpPanel();
    }

    public void setUpPanel(){

        pnlTraffic.setVisible(true);
        pnlTraffic.setLayout(new BorderLayout());
        pnlTraffic.add(trafficLogPanel.getPnlTraffic(), BorderLayout.CENTER);

        pnlUsers.setVisible(true);
        pnlUsers.setLayout(new BorderLayout());
        pnlUsers.add(userPanel.getPnlUsers(), BorderLayout.CENTER);

        pnlControl.setVisible(true);
        pnlControl.setLayout(new BorderLayout());
        pnlControl.add(controlPanel.getPnlControl(), BorderLayout.CENTER);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
    public TrafficLogPanel getTrafficLogPanel() {return trafficLogPanel;}

    public UserPanel getUserPanel() {
        return userPanel;
    }
}



