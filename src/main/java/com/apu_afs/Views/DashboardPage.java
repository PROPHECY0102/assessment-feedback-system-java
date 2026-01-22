package com.apu_afs.Views;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.apu_afs.GlobalState;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Views.components.*;

import net.miginfocom.swing.MigLayout;

public class DashboardPage extends JPanel {

  HeaderPanel header;
  NavPanel nav;

  JPanel contentBody;
  
  public DashboardPage(Router router, GlobalState state) {
    super(new MigLayout(
      "fill, insets 0, gap 0",  
        "[][][grow]",              
        "[][grow]"   
    ));

    if (state.getCurrUser() == null) {
      SwingUtilities.invokeLater(() -> {
        router.showView(Pages.LOGIN, state);
      });
      return;
    }

    header = new HeaderPanel(router, state);
    nav = new NavPanel(router, state);

    contentBody = new JPanel(new MigLayout("align center center"));
    contentBody.setBackground(App.slate100);

    JLabel temp = new JLabel();
    temp.setText("this is dashboard page" + router.getCurrPage().getDisplay());
    contentBody.add(temp);
    
    this.add(header, "span, growx, wrap");
    this.add(nav, "growy");
    this.add(contentBody, "span, grow");
  }
}
