package com.apu_afs.Views;

import javax.swing.JPanel;
import com.apu_afs.GlobalState;

import net.miginfocom.swing.MigLayout;

public class Router extends JPanel {
  private Pages currPage;

  public Router(GlobalState state) {
    super(new MigLayout("fill", "[]", "[]"));
    this.showView(Pages.LOGIN, state);
  }
  
  public void showView(Pages page, GlobalState state) {
    this.removeAll();
    
    this.currPage = page;
    this.add(this.createPanel(page, state), "grow");

    revalidate();
    repaint();
  }

  private JPanel createPanel(Pages page, GlobalState state) {
    switch (page) {
      case Pages.LOGIN: return new LoginPage(this, state);
      case Pages.DASHBOARD: return new DashboardPage(this, state);
      default: return new DashboardPage(this, state);
    }
  }

  public Pages getCurrPage() {
    return currPage;
  }

  public void setCurrPage(Pages currPage) {
    this.currPage = currPage;
  }
}
