/**
 * This class is responsible for saving and managing the information for Traffic on the server
 */
package server.model;

import server.controller.TrafficLogController;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class TrafficLog {

    //private static final long serialVersionUID = 3484417692910346763L;
    private ArrayList<TrafficLogMessage> log;
    private File savedLog = new File("files/savedLog.txt");
    private TrafficLogController tlController;

    /**
     * Constructor for TrafficLog
     * @param tlController
     */
    public TrafficLog(TrafficLogController tlController){
        this.tlController = tlController;
        log = new ArrayList<>();
        try {
            if (savedLog.length()!=0) {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(savedLog)));
            log = (ArrayList<TrafficLogMessage>) ois.readObject();
            ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error in TrafficLog constructor");
        }
    }

    /**
     * This method adds a log message to the arrayList
     * @param logMsg
     */
    public synchronized void add(String logMsg){
        TrafficLogMessage tlMsg = new TrafficLogMessage(logMsg);
        log.add(tlMsg);
        write(log);
    }

    /**
     * This method writes the arrayList to a file
     * @param log
     */
    public synchronized void write(ArrayList<TrafficLogMessage> log){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(savedLog,false)));
            oos.writeObject(log);
            oos.flush();
            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error in Traffic Log - write()");
        }
    }

    /**
     * This method searches in the TrafficLog between 2 dates and returns as a string
     * @param searchFrom
     * @param searchTo
     * @return
     */
    public String search(LocalDateTime searchFrom, LocalDateTime searchTo){
        String temp = "";
        ArrayList<TrafficLogMessage> tempArray = new ArrayList<>();

        for (int i = 0; i < log.size(); i++){
            if (log.get(i).getDateAndTime().isAfter(searchFrom)
            && log.get(i).getDateAndTime().isBefore(searchTo)){
                tempArray.add(log.get(i));
            }
        }

        for (TrafficLogMessage t : tempArray){
            temp += t.toString() + " \n";
        }

        return temp;
    }

    /**
     * To String method, returns string
     * @return
     */
    public String toString(){
        String temp = "";

        for (int i=0; i<log.size(); i++){
            temp += log.get(i).toString() + " \n";
        }
        return temp;
    }
}
