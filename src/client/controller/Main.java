/**
 * This class is the main class for the client
 */

package client.controller;

import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;

import javax.swing.*;

public class Main {
    /**
     * Not used anymore
     * @param args
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new FlatCarbonIJTheme() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        new ClientController();
        new ClientController();
        new ClientController();
    }
}
