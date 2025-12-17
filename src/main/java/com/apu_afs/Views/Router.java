package com.apu_afs.Views;

import javax.swing.JPanel;
import java.awt.*;

public class Router extends JPanel {
  private CardLayout cardLayout;

  public Router() {
    cardLayout = new CardLayout();
    this.setLayout(cardLayout);

    this.add(new LoginPage(this), "login");
    this.add(new DashboardPage(this), "dashboard");

    this.showView("login");
  }

  public void showView(String page) {
    cardLayout.show(this, page);
  }
}
