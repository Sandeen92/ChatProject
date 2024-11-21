/**
 * This class represents a Message in the TrafficLog
 */
package server.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TrafficLogMessage implements Serializable {

    private static final long serialVersionUID = 9152493698288657911L;
    private LocalDateTime dateAndTime;
    private String logMsg;

    /**
     * Constructor for TrafficLogMessage
     * @param logMsg
     */
    public TrafficLogMessage(String logMsg){
        dateAndTime = LocalDateTime.now();
        this.logMsg = logMsg;
    }

    /**
     * Getters and setters
     * @return
     */
    public LocalDateTime getDateAndTime() {return dateAndTime;}

    public String dateFormat(LocalDateTime dateAndTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm");
        return dateAndTime.format(formatter);
    }

    public String toString(){
        return String.format("[%s] %s", dateFormat(dateAndTime), logMsg);
    }
}
