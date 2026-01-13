package com.apu_afs.Views;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.apu_afs.GlobalState;
import com.apu_afs.Models.User;
import com.apu_afs.Views.components.PasswordField;
import com.apu_afs.Views.components.TextField;

public class LoginPage extends JPanel {
  JPanel imageContainer;
  JLabel imageLabel;

  JPanel formContainer;
  JPanel form;
  JPanel formHeader;
  JLabel formHeaderImageLabel;
  JPanel titleContainer;
  JLabel titleAssessmentLabel;
  JLabel titleFeedbackLabel;
  JLabel titleSystemLabel;
  JPanel loginLabelContainer;
  JLabel loginLabel;
  JLabel usernameLabel;
  TextField usernameTextfield;
  JLabel usernameErrorLabel;
  JLabel passwordLabel;
  PasswordField passwordTextField;
  JLabel passwordErrorLabel;
  JCheckBox staySignedInCheckBox;
  JPanel loginBtnContainer;
  JButton loginBtn;

  static String usernamePlaceholder = "Enter your Username...";
  static String passwordPlaceholder = "Enter your Password...";
  
  public LoginPage(Router router, GlobalState state) {
    super(new GridLayout(1, 2));
    
    if (state.getCurrUser() != null && state.getStaySignedIn()) {
      SwingUtilities.invokeLater(() -> {
        router.showView(Pages.DASHBOARD, state);
      });
      return;
    } else {
      state.setCurrUser(null);
    }

    imageLabel = new JLabel();
    imageLabel.setIcon(new ImageIcon("assets/apu-background-dimmed.png"));

    imageContainer = new JPanel();
    imageContainer.add(imageLabel);

    formHeaderImageLabel = new JLabel();
    formHeaderImageLabel.setIcon(App.iconResizer(new ImageIcon(App.icoPath), 64, 64));

    titleAssessmentLabel = new JLabel();
    titleAssessmentLabel.setText("Assessment");
    titleFeedbackLabel = new JLabel();
    titleFeedbackLabel.setText("Feedback");
    titleSystemLabel = new JLabel();
    titleSystemLabel.setText("System");

    titleContainer = new JPanel(new GridLayout(3, 1));
    titleContainer.setBackground(App.slate100);
    titleContainer.add(titleAssessmentLabel);
    titleContainer.add(titleFeedbackLabel);
    titleContainer.add(titleSystemLabel);

    formHeader = new JPanel();
    formHeader.setBackground(App.slate100);
    formHeader.setAlignmentX(JPanel.LEFT_ALIGNMENT);
    formHeader.add(formHeaderImageLabel);
    formHeader.add(titleContainer);

    loginLabel = new JLabel();
    loginLabel.setText(router.getCurrPage().getDisplay());
    loginLabelContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
    loginLabelContainer.setBackground(App.slate100);
    loginLabelContainer.setAlignmentX(JPanel.LEFT_ALIGNMENT);
    loginLabelContainer.add(loginLabel);
    usernameLabel = new JLabel();
    usernameLabel.setText("Username: ");
    usernameLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
    usernameTextfield = new TextField(LoginPage.usernamePlaceholder);
    usernameTextfield.setBackground(App.slate200);
    usernameTextfield.setBorder(BorderFactory.createCompoundBorder(usernameTextfield.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    usernameTextfield.setPreferredSize(new Dimension(250, 50));
    usernameTextfield.setAlignmentX(JTextField.LEFT_ALIGNMENT);
    usernameErrorLabel = new JLabel();
    usernameErrorLabel.setForeground(App.red600);
    usernameErrorLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
    passwordLabel = new JLabel();
    passwordLabel.setText("Password: ");
    passwordLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
    passwordTextField = new PasswordField(LoginPage.passwordPlaceholder);
    passwordTextField.setBackground(App.slate200);
    passwordTextField.setBorder(BorderFactory.createCompoundBorder(passwordTextField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    passwordTextField.setPreferredSize(new Dimension(250, 50));
    passwordTextField.setAlignmentX(JTextField.LEFT_ALIGNMENT);
    passwordErrorLabel = new JLabel();
    passwordErrorLabel.setForeground(App.red600);
    passwordErrorLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
    staySignedInCheckBox = new JCheckBox();
    staySignedInCheckBox.setFocusable(false);
    staySignedInCheckBox.setText("Stay Signed In?");
    staySignedInCheckBox.setBackground(App.slate100);
    staySignedInCheckBox.setAlignmentX(JCheckBox.LEFT_ALIGNMENT);

    loginBtn = new JButton();
    loginBtn.setText("Login");
    loginBtn.setBackground(App.blue600);
    loginBtn.setForeground(Color.WHITE);
    loginBtn.setBorder(BorderFactory.createCompoundBorder(loginBtn.getBorder(), BorderFactory.createEmptyBorder(6, 12, 6, 12)));
    loginBtn.addActionListener(e -> {
      String usernameInput = usernameTextfield.getText().trim();
      String passwordInput = new String(passwordTextField.getPassword()).trim();

      User possibleUser = User.userAuth(usernameInput, passwordInput);
      if (possibleUser == null) {
        usernameErrorLabel.setText("Incorrect Username or Password");
        passwordErrorLabel.setText("Incorrect Username or Password");
        JOptionPane.showMessageDialog(router, "Unable to Login! Incorrect Username or Password was Entered", "AFS | Login Error", JOptionPane.ERROR_MESSAGE);
        return;
      }

      state.setCurrUser(possibleUser);
      state.setStaySignedIn(staySignedInCheckBox.isSelected());
      JOptionPane.showMessageDialog(router, "Welcome Back " + possibleUser.getUsername() + "!", "AFS | Login Successful", JOptionPane.INFORMATION_MESSAGE);
      router.showView(Pages.DASHBOARD, state);
    });
    loginBtnContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    loginBtnContainer.setBackground(App.slate100);
    loginBtnContainer.setAlignmentX(JPanel.LEFT_ALIGNMENT);
    loginBtnContainer.add(loginBtn);

    form = new JPanel();
    form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
    form.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
    form.setBackground(App.slate100);
    form.add(formHeader);
    form.add(Box.createVerticalStrut(10));
    form.add(loginLabelContainer);
    form.add(Box.createVerticalStrut(10));
    form.add(usernameLabel);
    form.add(Box.createVerticalStrut(10));
    form.add(usernameTextfield);
    form.add(Box.createVerticalStrut(10));
    form.add(usernameErrorLabel);
    form.add(Box.createVerticalStrut(10));
    form.add(passwordLabel);
    form.add(Box.createVerticalStrut(10));
    form.add(passwordTextField);
    form.add(Box.createVerticalStrut(10));
    form.add(passwordErrorLabel);
    form.add(Box.createVerticalStrut(10));
    form.add(staySignedInCheckBox);
    form.add(Box.createVerticalStrut(10));
    form.add(loginBtnContainer);
    
    formContainer = new JPanel(new GridBagLayout());
    formContainer.setBackground(App.slate900);
    formContainer.add(form);
    
    this.add(imageContainer);
    this.add(formContainer);
  }
}
