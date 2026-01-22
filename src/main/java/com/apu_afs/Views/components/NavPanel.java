package com.apu_afs.Views.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.apu_afs.GlobalState;
import com.apu_afs.Helper;
import com.apu_afs.Models.NavOption;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Views.App;
import com.apu_afs.Views.Router;

import net.miginfocom.swing.MigLayout;

public class NavPanel extends JPanel {

  JPanel navList;
  
  public NavPanel(Router router, GlobalState state) {
    super(new MigLayout("insets 0"));
    this.setBackground(App.blue900);

    navList = new JPanel(new MigLayout("insets 0, wrap 1, fillx"));
    navList.setBackground(App.blue900);
    for (NavOption navOption : state.getCurrUser().getNavOptions()) {
      JButton navBtn = new JButton();
      navBtn.setLayout(new MigLayout("fill"));
      navBtn.setBackground(App.blue900);
      navBtn.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
      navBtn.setFocusable(false);

      JPanel navDisplayGroup = new JPanel(new MigLayout("insets 0, gapx 10", "[][]"));
      navDisplayGroup.setBackground(App.blue900);

      JLabel navIcon = new JLabel();
      navIcon.setIcon(Helper.iconResizer(new ImageIcon(navOption.getIconSrc()), 24, 24));

      JLabel navDisplayName = new JLabel(navOption.getDisplayName());
      navDisplayName.setForeground(Color.WHITE);
      navDisplayName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 24));

      navBtn.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
          navBtn.setBackground(App.blue600);
          navDisplayGroup.setBackground(App.blue600);
        }

        @Override
        public void mouseExited(MouseEvent e) {
          navBtn.setBackground(App.blue900);
          navDisplayGroup.setBackground(App.blue900);

          // selected page/current page will have a different background color
          if (router.getCurrPage() == navOption.getPage()) {
            navBtn.setBackground(App.blue600);
            navDisplayGroup.setBackground(App.blue600);
          }
        }
      });

      navBtn.addActionListener(e -> {
        router.showView(navOption.getPage(), state);
      });

      // selected page/current page will have a different background color
      if (router.getCurrPage() == navOption.getPage()) {
        navBtn.setBackground(App.blue600);
        navDisplayGroup.setBackground(App.blue600);
      }

      navDisplayGroup.add(navIcon);
      navDisplayGroup.add(navDisplayName);
      navBtn.add(navDisplayGroup);
      navList.add(navBtn, "growx");
    }
   
    

    this.add(navList);
  }
}
