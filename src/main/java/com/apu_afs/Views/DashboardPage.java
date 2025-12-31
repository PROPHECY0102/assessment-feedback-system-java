package com.apu_afs.Views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.apu_afs.GlobalState;
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

    header = new HeaderPanel(router, state);
    nav = new NavPanel(router, state);

    contentBody = new JPanel();
    contentBody.setBackground(App.slate100);
    
    this.add(header, "span, growx, wrap");
    this.add(nav, "growy");
    this.add(contentBody, "grow");

    // super(new MigLayout(
    //   "fill, insets 0, gap 0",  // Layout constraints
    //   "[][][grow]",              // Column constraints: auto, auto, grow
    //   "[][grow]"                 // Row constraints: auto, grow
    // ));
    
    // // Header - spans all columns, wraps to next row
    // JPanel header = new JPanel();
    // header.setBackground(new Color(60, 63, 65));
    // header.add(new JLabel("Header"));
    // add(header, "span, growx, wrap, h 60!");
    
    // // Nav - takes only content width, full height of remaining space
    // JPanel nav = new JPanel();
    // nav.setBackground(new Color(78, 80, 82));
    // nav.setLayout(new MigLayout("fillx, insets 10"));
    // nav.add(new JLabel("Nav Item 1"), "wrap");
    // nav.add(new JLabel("Nav Item 2"), "wrap");
    // nav.add(new JLabel("Nav Item 3"), "wrap");
    // add(nav, "growy, w 200!");
    
    // // Content - takes remaining width and height
    // JPanel content = new JPanel();
    // content.setBackground(new Color(43, 43, 43));
    // content.add(new JLabel("Main Content Area"));
    // add(content, "grow");
  }
}
