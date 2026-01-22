package com.apu_afs.Views.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.apu_afs.GlobalState;
import com.apu_afs.Helper;
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

  JPanel headerProfileSection;
  JPanel profileUsernameContainer;
  JLabel profileImageLabel;
  JLabel profileUsernameLabel;
  JLabel profileRoleLabel;
  JPanel editProfileContainer;
  JButton editProfileBtn;
  
  
  public HeaderPanel(Router router, GlobalState state) {
    super(new MigLayout("insets 5 10"));
    this.setBackground(App.slate900);
    this.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

    headerImageLabel = new JLabel();
    headerImageLabel.setBackground(App.slate100);
    headerImageLabel.setIcon(Helper.iconResizer(new ImageIcon("assets/apu-icon.png"), 64, 64));
    
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
    currPageLabel.setText(" | " + router.getCurrPage().getDisplay());
    currPageLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 42));
    currPageLabel.setForeground(App.slate100);

    headerTitleSection = new JPanel();
    headerTitleSection.setBackground(App.slate900);
    headerTitleSection.add(headerImageLabel);
    headerTitleSection.add(titleContainer);
    headerTitleSection.add(currPageLabel);

    profileImageLabel = new JLabel();
    profileImageLabel.setIcon(Helper.iconResizer(new ImageIcon("assets/header-profile-icon.png"), 24, 24));
    
    profileUsernameLabel = new JLabel();
    profileUsernameLabel.setText(state.getCurrUser().getUsername());
    profileUsernameLabel.setForeground(App.slate100);
    profileUsernameLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

    profileUsernameContainer = new JPanel(new MigLayout("insets 0"));
    profileUsernameContainer.setBackground(App.slate900);
    profileUsernameContainer.add(profileImageLabel);
    profileUsernameContainer.add(profileUsernameLabel);

    profileRoleLabel = new JLabel();
    profileRoleLabel.setLayout(new MigLayout("insets 0"));
    profileRoleLabel.setText(
      state.getCurrUser().getFirstName() + " " + state.getCurrUser().getLastName() + ", " + state.getCurrUser().getRole().getDisplay()
    );
    profileRoleLabel.setForeground(App.slate100);
    profileRoleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    
    editProfileBtn = new JButton();
    editProfileBtn.setText("Edit Profile");
    editProfileBtn.setIcon(Helper.iconResizer(new ImageIcon("assets/header-edit-profile.png"), 18, 18));
    editProfileBtn.setBackground(App.slate100);
    editProfileBtn.setForeground(App.slate900);
    editProfileBtn.setBorder(BorderFactory.createCompoundBorder(editProfileBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    editProfileBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    editProfileBtn.setFocusable(false);
    
    editProfileContainer = new JPanel(new MigLayout("insets 0"));
    editProfileContainer.setBackground(App.slate900);
    editProfileContainer.add(editProfileBtn, "align right");

    headerProfileSection = new JPanel(new MigLayout("insets 0, wrap 1, fillx"));
    headerProfileSection.setBackground(App.slate900);
    headerProfileSection.add(profileUsernameContainer, "growx");
    headerProfileSection.add(profileRoleLabel, "growx");

    this.add(headerTitleSection);
    this.add(headerProfileSection, "push, align right, gapright 10");
    this.add(editProfileContainer, "align right");
  }
}
