package com.apu_afs.Views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoginPage extends JPanel {

  JLabel header;
  JButton dashboardBtn;
  
  public LoginPage(Router router) {
    header = new JLabel();
    header.setText("This is the Login Page");

    dashboardBtn = new JButton();
    dashboardBtn.setText("Go to Dashboard");
    dashboardBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        router.showView("dashboard");
      }
    });
    
    this.add(header);
    this.add(dashboardBtn);
  }
}
