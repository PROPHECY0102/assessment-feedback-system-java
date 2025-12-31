package com.apu_afs.Views.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.apu_afs.GlobalState;
import com.apu_afs.Views.App;
import com.apu_afs.Views.Router;

import net.miginfocom.swing.MigLayout;

public class HeaderPanel extends JPanel {
  JPanel headerTitleSection;
  JLabel headerImageLabel;
  JPanel titleContainer;
  JLabel titleAssessmentLabel;
  JLabel titleFeedbackLabel;
  JLabel titleSystemLabel;
  JLabel currPageLabel;
  
  public HeaderPanel(Router router, GlobalState state) {
    super(new MigLayout());
    this.setBackground(App.slate900);
    this.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

    headerImageLabel = new JLabel();
    headerImageLabel.setBackground(App.slate100);
    headerImageLabel.setIcon(App.iconResizer(new ImageIcon("assets/apu-icon.png"), 64, 64));
    
    titleAssessmentLabel = new JLabel();
    titleAssessmentLabel.setText("Assessment");
    titleAssessmentLabel.setForeground(App.slate100);
    titleFeedbackLabel = new JLabel();
    titleFeedbackLabel.setText("Feedback");
    titleFeedbackLabel.setForeground(App.slate100);
    titleSystemLabel = new JLabel();
    titleSystemLabel.setText("System");
    titleSystemLabel.setForeground(App.slate100);

    titleContainer = new JPanel(new GridLayout(3, 1));
    titleContainer.setBackground(App.slate900);
    titleContainer.add(titleAssessmentLabel);
    titleContainer.add(titleFeedbackLabel);
    titleContainer.add(titleSystemLabel);

    currPageLabel = new JLabel();
    currPageLabel.setText(" | " + "Dashboard");
    currPageLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 42));
    currPageLabel.setForeground(App.slate100);

    headerTitleSection = new JPanel();
    headerTitleSection.setBackground(App.slate900);
    headerTitleSection.add(headerImageLabel);
    headerTitleSection.add(titleContainer);
    headerTitleSection.add(currPageLabel);

    this.add(headerTitleSection, BorderLayout.WEST);
  }
}
