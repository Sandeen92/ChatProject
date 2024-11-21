package server.view;

import server.controller.ServerController;
import server.controller.TrafficLogController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TrafficLogPanel {
    private JPanel pnlTraffic;
    private JPanel pnlLogControl;
    private JButton btnApply;
    private JButton btnReset;
    private JScrollPane jspLog;
    private JTextArea txaLog;
    private JTextField txfSearchFrom;
    private JTextField txfSearchTo;
    private JPanel pnlSearch;
    private JPanel pnlTextFields;
    private ServerController controller;
    private TrafficLogController trafficLogController;


    public TrafficLogPanel(ServerController controller){
        this.controller = controller;
        buttons();

    }

    public void populate(String str){
        txaLog.setText("");
        txaLog.append(str);
        txaLog.setCaretPosition(txaLog.getDocument().getLength());
    }

    private void searchTrafficLog(String searchFrom, String searchTo){
        if (searchFrom.length() == 12 && searchTo.length() == 12) {
            if(checkIfNumeric(searchFrom) && checkIfNumeric(searchTo)) {
                LocalDateTime dateFrom = splitStringToDate(searchFrom);
                LocalDateTime dateTo = splitStringToDate(searchTo);
                System.out.println(dateFrom);
                System.out.println(dateTo);
                if(dateFrom != null && dateTo != null) {
                    controller.searchTrafficLog(dateFrom, dateTo);
                }
            }
        }
    }

    private LocalDateTime splitStringToDate(String s) {
        String[] numbers = new String[12];
        for(int i = 0; i < 12; i++){
            numbers[i] = s.substring(i, i+1);
        }

        String year = numbers[0] + numbers[1] + numbers[2] + numbers[3];
        String month = numbers[4] + numbers[5];
        String day = numbers[6] + numbers [7];
        String hour = numbers[8] + numbers[9];
        String minute = numbers[10] + numbers[11];

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm");
        s = String.format("%s/%s/%s-%s:%s", year, month, day, hour, minute);
        try{
            LocalDateTime date = LocalDateTime.parse(s, formatter);
            return date;
        } catch (DateTimeException e) {
            return null;
        }
    }

    private boolean checkIfNumeric(String s) {
        try {
            double d = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private void resetSearch() {
        txfSearchFrom.setText("");
        txfSearchTo.setText("");
        controller.refreshTrafficLog();
    }

    public void setTrafficLogController(TrafficLogController tlc){
        trafficLogController = tlc;
    }

    public JPanel getPnlTraffic() {
        return pnlTraffic;
    }

    private void buttons() {
        btnApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchTrafficLog(txfSearchFrom.getText(), txfSearchTo.getText());
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetSearch();
            }
        });
    }


}
