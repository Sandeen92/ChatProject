/**
 * This class is responsible for controlling the TrafficLog
 */
package server.controller;

import server.model.*;
import server.view.TrafficLogPanel;

import java.time.LocalDateTime;

public class TrafficLogController {

    private ServerController controller;
    private TrafficLogPanel trafficLogPanel;
    private TrafficLog trafficLog;

    /**
     * Constructor for this class. It asigns a ServerController and creates the TrafficLogPanel for the GUI.
     * @param controller ServerController for communicating with the GUI.
     * @param trafficLogPanel The TrafficLogPanel contains the GUI for the TrafficLog.
     */
    public TrafficLogController(ServerController controller, TrafficLogPanel trafficLogPanel){
        this.controller = controller;
        this.trafficLogPanel = trafficLogPanel;
        trafficLog = new TrafficLog(this);
    }

    /**
     * Adds a String to the Traffic log.
     * @param s A string to be added to the Traffic log.
     */
    public void addToTrafficLog(String s){
        trafficLog.add(s);
    }

    /**
     * Searches the trafficlog between two dates
     * @param searchFrom
     * @param searchTo
     * @return
     */
    public String searchTrafficLog(LocalDateTime searchFrom, LocalDateTime searchTo){
        return trafficLog.search(searchFrom, searchTo);
    }

    /**
     * Returns a TrafficLog object.
     * @return The object to be returned.
     */
    public TrafficLog getTrafficLog() {return trafficLog;}
}
