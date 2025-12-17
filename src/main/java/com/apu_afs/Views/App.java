package com.apu_afs.Views;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class App extends JFrame {
  private static String title = "Assessment Feedback System - APU";
  private static int frameWidth = 1280;
  private static int frameHeight = 720;
  private static String icoPath = "assets/apu-icon.png"; 
  
  public App() {
    this.setTitle(App.title);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(true);
    this.setSize(App.frameWidth, App.frameHeight);
    this.setLocationRelativeTo(null);
    this.add(new Router());
    this.setVisible(true);

    ImageIcon ico = new ImageIcon(App.icoPath);
    this.setIconImage(ico.getImage());
    this.getContentPane().setBackground(new Color(0xcad5e2));
  }
}
