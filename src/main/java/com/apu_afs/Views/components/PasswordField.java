package com.apu_afs.Views.components;

import javax.swing.JPasswordField;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class PasswordField extends JPasswordField {

  public PasswordField(String placeholder) {
    super();
    this.setText(placeholder);
    this.setEchoChar((char) 0);
    this.addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(FocusEvent e) {
        String passwordInput = new String(PasswordField.this.getPassword());
        if (passwordInput.equals(placeholder)) {
          PasswordField.this.setText("");
          PasswordField.this.setForeground(Color.BLACK);
          PasswordField.this.setEchoChar('*');
        }
      }
      
      @Override
      public void focusLost(FocusEvent e) {
        String passwordInput = new String(PasswordField.this.getPassword());
        if (passwordInput.isEmpty()) {
          PasswordField.this.setText(placeholder);
          PasswordField.this.setForeground(Color.GRAY);
          PasswordField.this.setEchoChar((char) 0);
        }
      }
    });
  }
}
