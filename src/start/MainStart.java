package start;

import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;

import javax.swing.*;

public class MainStart {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( new FlatCarbonIJTheme() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
      new StartFrame();
    }
}