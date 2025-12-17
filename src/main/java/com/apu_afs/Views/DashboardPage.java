package com.apu_afs.Views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DashboardPage extends JPanel {

  JLabel header;
  JButton loginbtn;
  
  public DashboardPage(Router router) {
    header = new JLabel();
    header.setText("This is the Dashboard Page");

    loginbtn = new JButton();
    loginbtn.setText("Go to login");
    loginbtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        router.showView("login");
      }
    });
    
    this.add(header);
    this.add(loginbtn);
  }
}
