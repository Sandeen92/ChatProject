package server.view;

import server.controller.ChatServer;
import server.controller.ServerController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDateTime;

public class ControlPanel {
    private JPanel pnlControl;
    private JLabel lblStatus;
    private JPanel pnlAddress;
    private JTextField txfIP;
    private JTextField txfPort;
    private JButton btnStartServer;
    private JButton btnShutDown;
    private JLabel lblUptime;
    private JPanel pnlUptime;
    private JPanel pnlStatus;
    private JPanel pnlStatusAndUptime;
    private ServerController controller;


    public ControlPanel(ServerController controller){
        this.controller = controller;
        buttons();
    }

    public void startServer(){
        btnStartServer.setEnabled(false);
        txfIP.setEnabled(false);
        txfPort.setEnabled(false);
        btnShutDown.setEnabled(true);
        lblStatus.setText("ONLINE");
        lblStatus.setForeground(new java.awt.Color(5, 138, 0));
        UptimeCounter uptimeCounter = new UptimeCounter();
        uptimeCounter.start();
    }

    public void buttons(){
        btnStartServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ChatServer server = new ChatServer(Integer.parseInt(txfPort.getText()),controller);
                    server.start();
                    startServer();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }


    public JPanel getPnlControl() {
        return pnlControl;
    }




        private class UptimeCounter extends Thread{
            private String seconds = "00";
            private String minutes = "00";
            private String hours = "00";
            private int minCounter = 0;
            private int hourCounter = 0;
            private long createdMillis = System.currentTimeMillis();
            public int getSeconds() {
                long nowMillis = System.currentTimeMillis();
                return (int)((nowMillis - this.createdMillis) / 1000);
            }


            @Override
            public void run() {
                while(true){
                    // SECONDS
                    if (getSeconds() < 10){
                        seconds = "0" + (getSeconds());
                    }
                    else if (getSeconds() >= 10) {
                        seconds = String.valueOf(getSeconds());
                    }

                    // MINUTES
                    if (getSeconds() >= 60){
                        seconds = "00";
                        createdMillis = System.currentTimeMillis();
                        minCounter++;

                        if(minCounter > 9 && minCounter < 60){
                            minutes = String.valueOf(minCounter);
                        }
                        else if(minCounter == 60){
                            minCounter = 0;
                            minutes = "00";
                            hourCounter++;
                        }
                        else {
                            minutes = "0" + minCounter;
                        }
                    }

                    // HOURS
                    if(hourCounter < 10){
                        hours = "0" + hourCounter;
                    }
                    else {
                        hours = String.valueOf(hourCounter);
                    }

                    lblUptime.setText(String.format("Uptime: %s:%s:%s", hours, minutes, seconds));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
}

