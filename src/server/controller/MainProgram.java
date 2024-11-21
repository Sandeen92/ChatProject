/**
 * This is the main class for the server
 */
package server.controller;

import client.controller.ClientController;
import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;

import javax.swing.*;


public class MainProgram {
    /**
     * This method starts the server
     * @param args
     */
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel( new FlatCarbonIJTheme() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        try {
            new ServerController();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
