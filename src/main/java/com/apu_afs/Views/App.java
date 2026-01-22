package com.apu_afs.Views;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.apu_afs.GlobalState;

public class App extends JFrame {
  // Application Config
  public static final String title = "Assessment Feedback System - APU";
  public static final int frameWidth = 1440;
  public static final int frameHeight = 900;
  public static final String icoPath = "assets/apu-icon.png";

  // Color Theme
  public static final Color slate900 = new Color(0x0f172b);
  public static final Color slate800 = new Color(0x1d293d);
  public static final Color slate700 = new Color(0x314158);
  public static final Color slate600 = new Color(0x45556c);
  public static final Color slate500 = new Color(0x62748e);
  public static final Color slate400 = new Color(0x90a1b9);
  public static final Color slate300 = new Color(0xcad5e2);
  public static final Color slate200 = new Color(0xe2e8f0);
  public static final Color slate100 = new Color(0xf1f5f9);

  public static final Color blue900 = new Color(0x1c398e);
  public static final Color blue800 = new Color(0x193cb8);
  public static final Color blue600 = new Color(0x155dfc);

  public static final Color green600 = new Color(0x00a63e);

  public static final Color red600 = new Color(0xe7000b);
  public static final Color red200 = new Color(0xffc9c9);
  public static final Color red100 = new Color(0xffe2e2);


  public static final Color orange600 = new Color(0xe17100);
  
  public App(GlobalState state) {
    this.setTitle(App.title);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(true);
    this.setSize(App.frameWidth, App.frameHeight);
    this.setExtendedState(JFrame.MAXIMIZED_VERT);
    this.setLocationRelativeTo(null);
    this.add(new Router(state));
    
    ImageIcon ico = new ImageIcon(App.icoPath);
    this.setIconImage(ico.getImage());
    this.getContentPane().setBackground(new Color(0xcad5e2));
    this.setVisible(true);
  }
}
