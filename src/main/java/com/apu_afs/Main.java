package com.apu_afs;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.UIManager;

import com.apu_afs.Views.App;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {

    public static void main(String[] args) {
        try {
            // Flatlaf setup
            UIManager.setLookAndFeel( new FlatLightLaf() );

            // Scroll bar look and feel
            UIManager.put( "ScrollBar.trackArc", 999 );
            UIManager.put( "ScrollBar.thumbArc", 999 );
            UIManager.put( "ScrollBar.trackInsets", new Insets( 2, 4, 2, 4 ) );
            UIManager.put( "ScrollBar.thumbInsets", new Insets( 2, 2, 2, 2 ) );
            UIManager.put( "ScrollBar.track", new Color( 0xe0e0e0 ) );

            // Text Field look and feel
            UIManager.put( "Component.focusWidth", 2 );

            // Rounded Corner for all Component
            UIManager.put( "Button.arc", 12 );
            UIManager.put( "Component.arc", 12 );
            UIManager.put( "ProgressBar.arc", 12 );
            UIManager.put( "TextComponent.arc", 12 );
            UIManager.put( "TextArea.arc", 12 );

            // ComboBox button background
            UIManager.put("ComboBox.buttonBackground", App.slate200);

            new App(new GlobalState());
        } catch( Exception e ) {
            System.err.println( e.getMessage() );
        }
    }
}