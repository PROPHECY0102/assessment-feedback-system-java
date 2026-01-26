package com.apu_afs.Views;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.apu_afs.GlobalState;
import com.apu_afs.Helper;
import com.apu_afs.Models.ComboBoxItem;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Views.components.HeaderPanel;
import com.apu_afs.Views.components.NavPanel;
import com.apu_afs.Views.components.TextField;
import com.toedter.calendar.JDateChooser;

import net.miginfocom.swing.MigLayout;

public class ProfilePage extends JPanel {
  HeaderPanel header;
  NavPanel nav;

  JPanel contentBody;

  JPanel profileFormContainer;

  JPanel profileDetailsContainer;
  JLabel profileImage;
  JLabel profileUsername;
  JLabel profileFullName;
  JLabel profileGenderAge;
  JLabel profileRole;
  
  JTabbedPane formTabbedPane;
  JPanel mainform;
  JPanel subform;

  JPanel usernameRow;
  JPanel usernameFieldGroup;
  JLabel usernameLabel;
  TextField usernameField;
  JLabel usernameErrorLabel;

  JPanel currentPasswordRow;
  JPanel currentPasswordFieldGroup;
  JLabel currentPasswordLabel;
  TextField currentPasswordField;
  JLabel currentPasswordErrorLabel;

  JPanel newPasswordRow;
  JPanel newPasswordFieldGroup;
  JLabel newPasswordLabel;
  TextField newPasswordField;
  JLabel newPasswordErrorLabel;

  JPanel emailRow;
  JPanel emailFieldGroup;
  JLabel emailLabel;
  TextField emailField;
  JLabel emailErrorLabel;

  JPanel phoneNumberRow;
  JPanel phoneNumberFieldGroup;
  JLabel phoneNumberLabel;
  TextField phoneNumberField;
  JLabel phoneNumberErrorLabel;

  // admin detail fields
  JPanel departmentRow;
  JPanel departmentFieldGroup;
  JLabel departmentLabel;
  TextField departmentField;
  JLabel departmentErrorLabel;

  // employment type and employed at can be reused for admin, academic leader and lecturer
  ArrayList<ComboBoxItem> employmentTypeOptions;

  JPanel employmentInfoRow;
  JPanel employmentTypeFieldGroup;
  JLabel employmentTypeLabel;
  JComboBox<ComboBoxItem> employmentTypeComboBox;
  JLabel employmentTypeErrorLabel;
  JPanel employedAtFieldGroup;
  JLabel employedAtLabel;
  JDateChooser employedAtDateChooser;
  JLabel employedAtErrorLabel;

  // academic leader detail fields
  JPanel facultyRow;
  JPanel facultyFieldGroup;
  JLabel facultyLabel;
  TextField facultyField;
  JLabel facultyErrorLabel;

  // lecturer detail fields
  ArrayList<ComboBoxItem> academicLeaderList;

  JPanel academicLeaderRow;
  JPanel academicLeaderFieldGroup;
  JLabel academicLeaderLabel;
  JComboBox<ComboBoxItem> academicLeaderComboBox;
  JLabel academicLeaderErrorLabel;

  // student detail fields
  ArrayList<ComboBoxItem> modeOptions;

  JPanel programModeRow;
  JPanel programFieldGroup;
  JLabel programLabel;
  TextField programField;
  JLabel programErrorLabel;
  JPanel modeFieldGroup;
  JLabel modeLabel;
  JComboBox<ComboBoxItem> modeComboBox;
  JLabel modeErrorLabel;

  JPanel cgpaCreditHoursRow;
  JPanel cgpaFieldGroup;
  JLabel cgpaLabel;
  TextField cgpaField;
  JLabel cgpaErrorLabel;
  JPanel creditHoursFieldGroup;
  JLabel creditHoursLabel;
  TextField creditHoursField;
  JLabel creditHoursErrorLabel;

  JPanel enrolledAtRow;
  JPanel enrolledAtFieldGroup;
  JLabel enrolledAtLabel;
  JDateChooser enrolledAtDateChooser;
  JLabel enrolledAtErrorLabel;

  JPanel actionButtonGroup;
  JButton editBtn;
  JButton logoutBtn;

  Map<String, TextField> textFields;
  Map<String, JComboBox<ComboBoxItem>> comboBoxes;
  Map<String, JDateChooser> dateChoosers;
  Map<String, JLabel> errorLabels;

  public ProfilePage(Router router, GlobalState state) {
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

    contentBody = new JPanel(new MigLayout("insets 20 20, wrap 1, gapy 10"));
    contentBody.setBackground(App.slate100);

    profileImage = new JLabel();
    profileImage.setIcon(Helper.iconResizer(new ImageIcon("assets/header-profile-icon.png"), 120, 120));

    usernameLabel = new JLabel();
    usernameLabel.setText("Username: ");
    usernameField = new TextField("Enter Username Here...");
    usernameField.setBackground(App.slate200);
    usernameField.setBorder(BorderFactory.createCompoundBorder(usernameField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    usernameField.setPreferredSize(new Dimension(600, 35));
    usernameField.setText(state.getCurrUser().getUsername());
    usernameErrorLabel = new JLabel("\s");
    usernameErrorLabel.setForeground(App.red600);
    usernameFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    usernameFieldGroup.setBackground(App.slate100);
    usernameFieldGroup.add(usernameLabel);
    usernameFieldGroup.add(usernameField);
    usernameFieldGroup.add(usernameErrorLabel);

    usernameRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    usernameRow.setBackground(App.slate100);
    usernameRow.add(usernameFieldGroup);

    this.add(header, "span, growx, wrap");
    this.add(nav, "growy");
    this.add(contentBody, "span, grow");
    state.clearState();
  }
  
}
