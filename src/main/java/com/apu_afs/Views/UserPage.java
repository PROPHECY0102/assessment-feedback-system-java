package com.apu_afs.Views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.apu_afs.GlobalState;
import com.apu_afs.Helper;
import com.apu_afs.Models.AcademicLeader;
import com.apu_afs.Models.Admin;
import com.apu_afs.Models.ComboBoxItem;
import com.apu_afs.Models.Lecturer;
import com.apu_afs.Models.Student;
import com.apu_afs.Models.User;
import com.apu_afs.Models.Validation;
import com.apu_afs.Models.Enums.EmploymentType;
import com.apu_afs.Models.Enums.Gender;
import com.apu_afs.Models.Enums.Mode;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Models.Enums.Role;
import com.apu_afs.Views.components.HeaderPanel;
import com.apu_afs.Views.components.NavPanel;
import com.apu_afs.Views.components.TextField;
import com.toedter.calendar.JDateChooser;

import net.miginfocom.swing.MigLayout;

public class UserPage extends JPanel {

  HeaderPanel header;
  NavPanel nav;

  JPanel contentBody;

  JLabel formTitle;

  JTabbedPane formTabbedPane;
  JPanel mainform;
  JPanel subform;

  JPanel usernamePasswordRow;
  JPanel usernameFieldGroup;
  JLabel usernameLabel;
  TextField usernameField;
  JLabel usernameErrorLabel;
  JPanel passwordFieldGroup;
  JLabel passwordLabel;
  TextField passwordField;
  JLabel passwordErrorLabel;

  JPanel firstLastNameRow;
  JPanel firstNameFieldGroup;
  JLabel firstNameLabel;
  TextField firstNameField;
  JLabel firstNameErrorLabel;
  JPanel lastNameFieldGroup;
  JLabel lastNameLabel;
  TextField lastNameField;
  JLabel lastNameErrorLabel;

  ArrayList<ComboBoxItem> genderOptions;

  JPanel genderDobRow;
  JPanel genderFieldGroup;
  JLabel genderLabel;
  JComboBox<ComboBoxItem> genderComboBox;
  JLabel genderErrorLabel;
  JPanel dobFieldGroup;
  JLabel dobLabel;
  JDateChooser dobDateChooser;
  JLabel dobErrorLabel;
  
  JPanel emailPhoneRow;
  JPanel emailFieldGroup;
  JLabel emailLabel;
  TextField emailField;
  JLabel emailErrorLabel;
  JPanel phoneNumberFieldGroup;
  JLabel phoneNumberLabel;
  TextField phoneNumberField;
  JLabel phoneNumberErrorLabel;

  ArrayList<ComboBoxItem> roleOptions;

  JPanel roleRow;
  JPanel roleFieldGroup;
  JLabel roleLabel;
  JComboBox<ComboBoxItem> roleComboBox;
  JLabel roleErrorLabel;

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
  JButton submitBtn;
  JButton deleteBtn;

  String actionContext;
  User editingUser;

  Map<String, TextField> textFields;

  Map<String, JComboBox<ComboBoxItem>> comboBoxes;

  Map<String, JDateChooser> dateChoosers;

  Map<String, JLabel> errorLabels;
 
  private static final String[] allowedRoles = {"admin"};
  
  public UserPage(Router router, GlobalState state) {
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
    } else if (!Arrays.asList(allowedRoles).contains(state.getCurrUser().getRole().getValue())) {
        SwingUtilities.invokeLater(() -> {
            router.showView(Pages.DASHBOARD, state);
        });
    }

    if (state.getSelectedUserID() == null) {
      actionContext = "add";
      editingUser = null;
    } else {
      actionContext = "edit";
      editingUser = User.getUserByMatchingValues("id", state.getSelectedUserID());
    }

    header = new HeaderPanel(router, state);
    nav = new NavPanel(router, state);

    contentBody = new JPanel(new MigLayout("insets 20 20, wrap 1, gapy 10"));
    contentBody.setBackground(App.slate100);

    formTitle = new JLabel();
    if (actionContext.equals("edit")) {
      formTitle.setText("Editing User ID: " + editingUser.getID());
    } else {
      formTitle.setText("Create New User Form");
    }
    formTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

    usernameLabel = new JLabel();
    usernameLabel.setText("Username: ");
    usernameField = new TextField("Enter Username Here...");
    usernameField.setBackground(App.slate200);
    usernameField.setBorder(BorderFactory.createCompoundBorder(usernameField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    usernameField.setPreferredSize(new Dimension(600, 35));
    if (actionContext.equals("edit")) {
      usernameField.setText(editingUser.getUsername());
    }
    usernameErrorLabel = new JLabel("\s");
    usernameErrorLabel.setForeground(App.red600);
    usernameFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    usernameFieldGroup.setBackground(App.slate100);
    usernameFieldGroup.add(usernameLabel);
    usernameFieldGroup.add(usernameField);
    usernameFieldGroup.add(usernameErrorLabel);

    passwordLabel = new JLabel();
    passwordLabel.setText("Password: ");
    passwordField = new TextField("Enter Password Here...");
    passwordField.setBackground(App.slate200);
    passwordField.setBorder(BorderFactory.createCompoundBorder(passwordField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    passwordField.setPreferredSize(new Dimension(600, 35));
    if (actionContext.equals("edit")) {
      passwordField.setText(editingUser.getPassword());
    }
    passwordErrorLabel = new JLabel("\s");
    passwordErrorLabel.setForeground(App.red600);
    passwordFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    passwordFieldGroup.setBackground(App.slate100);
    passwordFieldGroup.add(passwordLabel);
    passwordFieldGroup.add(passwordField);
    passwordFieldGroup.add(passwordErrorLabel);

    firstNameLabel = new JLabel();
    firstNameLabel.setText("First Name: ");
    firstNameField = new TextField("Enter First Name Here...");
    firstNameField.setBackground(App.slate200);
    firstNameField.setBorder(BorderFactory.createCompoundBorder(firstNameField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    firstNameField.setPreferredSize(new Dimension(600, 35));
    if (actionContext.equals("edit")) {
      firstNameField.setText(editingUser.getFirstName());
    }
    firstNameErrorLabel = new JLabel("\s");
    firstNameErrorLabel.setForeground(App.red600);
    firstNameFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    firstNameFieldGroup.setBackground(App.slate100);
    firstNameFieldGroup.add(firstNameLabel);
    firstNameFieldGroup.add(firstNameField);
    firstNameFieldGroup.add(firstNameErrorLabel);

    lastNameLabel = new JLabel();
    lastNameLabel.setText("Last Name: ");
    lastNameField = new TextField("Enter Last Name Here...");
    lastNameField.setBackground(App.slate200);
    lastNameField.setBorder(BorderFactory.createCompoundBorder(lastNameField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    lastNameField.setPreferredSize(new Dimension(600, 35));
    if (actionContext.equals("edit")) {
      lastNameField.setText(editingUser.getLastName());
    }
    lastNameErrorLabel = new JLabel("\s");
    lastNameErrorLabel.setForeground(App.red600);
    lastNameFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    lastNameFieldGroup.setBackground(App.slate100);
    lastNameFieldGroup.add(lastNameLabel);
    lastNameFieldGroup.add(lastNameField);
    lastNameFieldGroup.add(lastNameErrorLabel);

    genderOptions = new ArrayList<>();
    for (Gender gender : Gender.values()) {
      genderOptions.add(new ComboBoxItem(gender.getValue(), gender.getDisplay()));
    }

    genderLabel = new JLabel();
    genderLabel.setText("Gender: ");
    genderComboBox = new JComboBox<>(genderOptions.stream().toArray(ComboBoxItem[]::new));
    genderComboBox.setBackground(App.slate200);
    genderComboBox.setBorder(BorderFactory.createCompoundBorder(genderComboBox.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    genderComboBox.setPreferredSize(new Dimension(200, 35));
    genderComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
      JLabel label = new JLabel();
      if (value != null) {
        label.setText(value.getLabelText());
      }
      return label;
    });
    if (actionContext.equals("edit")) {
      for (int i = 0; i < genderComboBox.getItemCount(); i++) {
        ComboBoxItem item = genderComboBox.getItemAt(i);
        if (item.getValue().equals(editingUser.getGender().getValue())) {
          genderComboBox.setSelectedIndex(i);
          break;
        }
      }
    }  
    genderErrorLabel = new JLabel("\s");
    genderErrorLabel.setForeground(App.red600);
    genderFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    genderFieldGroup.setBackground(App.slate100);
    genderFieldGroup.add(genderLabel);
    genderFieldGroup.add(genderComboBox);
    genderFieldGroup.add(genderErrorLabel);

    dobLabel = new JLabel();
    dobLabel.setText("Date of Birth: ");
    dobDateChooser = new JDateChooser();
    dobDateChooser.setDateFormatString(Helper.dateFormat);
    dobDateChooser.setBackground(App.slate200);
    dobDateChooser.getDateEditor().getUiComponent().setBackground(App.slate200);
    dobDateChooser.getDateEditor().getUiComponent().setBorder(BorderFactory.createCompoundBorder(dobDateChooser.getDateEditor().getUiComponent().getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    dobDateChooser.getCalendarButton().setBorder(BorderFactory.createCompoundBorder(dobDateChooser.getCalendarButton().getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    dobDateChooser.setPreferredSize(new Dimension(200, 35));
    if (actionContext.equals("edit")) {
      dobDateChooser.setDate(Helper.convertLocalDateToDate(editingUser.getDob()));
    }
    dobErrorLabel = new JLabel("\s");
    dobErrorLabel.setForeground(App.red600);
    dobFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    dobFieldGroup.setBackground(App.slate100);
    dobFieldGroup.add(dobLabel);
    dobFieldGroup.add(dobDateChooser);
    dobFieldGroup.add(dobErrorLabel);

    emailLabel = new JLabel();
    emailLabel.setText("Email: ");
    emailField = new TextField("Enter Email Here...");
    emailField.setBackground(App.slate200);
    emailField.setBorder(BorderFactory.createCompoundBorder(emailField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    emailField.setPreferredSize(new Dimension(600, 35));
    if (actionContext.equals("edit")) {
      emailField.setText(editingUser.getEmail());
    }
    emailErrorLabel = new JLabel("\s");
    emailErrorLabel.setForeground(App.red600);
    emailFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    emailFieldGroup.setBackground(App.slate100);
    emailFieldGroup.add(emailLabel);
    emailFieldGroup.add(emailField);
    emailFieldGroup.add(emailErrorLabel);

    phoneNumberLabel = new JLabel();
    phoneNumberLabel.setText("Phone Number: ");
    phoneNumberField = new TextField("Enter Phone Number Here...");
    phoneNumberField.setBackground(App.slate200);
    phoneNumberField.setBorder(BorderFactory.createCompoundBorder(phoneNumberField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    phoneNumberField.setPreferredSize(new Dimension(600, 35));
    if (actionContext.equals("edit")) {
      phoneNumberField.setText(editingUser.getPhoneNumber());
    }
    phoneNumberErrorLabel = new JLabel("\s");
    phoneNumberErrorLabel.setForeground(App.red600);
    phoneNumberFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    phoneNumberFieldGroup.setBackground(App.slate100);
    phoneNumberFieldGroup.add(phoneNumberLabel);
    phoneNumberFieldGroup.add(phoneNumberField);
    phoneNumberFieldGroup.add(phoneNumberErrorLabel);

    roleOptions = new ArrayList<>();
    for (Role role : Role.values()) {
      roleOptions.add(new ComboBoxItem(role.getValue(), role.getDisplay()));
    }

    roleLabel = new JLabel();
    roleLabel.setText("Role: ");
    roleComboBox = new JComboBox<>(roleOptions.stream().toArray(ComboBoxItem[]::new));
    roleComboBox.setBackground(App.slate200);
    roleComboBox.setBorder(BorderFactory.createCompoundBorder(roleComboBox.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    roleComboBox.setPreferredSize(new Dimension(200, 35));
    roleComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
      JLabel label = new JLabel();
      if (value != null) {
        label.setText(value.getLabelText());
      }
      return label;
    });
    if (actionContext.equals("edit")) {
      for (int i = 0; i < roleComboBox.getItemCount(); i++) {
        ComboBoxItem item = roleComboBox.getItemAt(i);
        if (item.getValue().equals(editingUser.getRole().getValue())) {
          roleComboBox.setSelectedIndex(i); // will this trigger the action listener or does it have to be user select in the gui
          break;
        }
      }
    }
    roleErrorLabel = new JLabel("\s");
    roleErrorLabel.setForeground(App.red600);
    roleFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    roleFieldGroup.setBackground(App.slate100);
    roleFieldGroup.add(roleLabel);
    roleFieldGroup.add(roleComboBox);
    roleFieldGroup.add(roleErrorLabel); 

    usernamePasswordRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    usernamePasswordRow.setBackground(App.slate100);
    usernamePasswordRow.add(usernameFieldGroup);
    usernamePasswordRow.add(passwordFieldGroup);

    firstLastNameRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    firstLastNameRow.setBackground(App.slate100);
    firstLastNameRow.add(firstNameFieldGroup);
    firstLastNameRow.add(lastNameFieldGroup);

    genderDobRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    genderDobRow.setBackground(App.slate100);
    genderDobRow.add(genderFieldGroup, "width 50%");
    genderDobRow.add(dobFieldGroup, "width 50%");

    emailPhoneRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    emailPhoneRow.setBackground(App.slate100);
    emailPhoneRow.add(emailFieldGroup);
    emailPhoneRow.add(phoneNumberFieldGroup);

    roleRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    roleRow.setBackground(App.slate100);
    roleRow.add(roleFieldGroup);

    mainform = new JPanel(new MigLayout("insets 30 0, wrap 1, gapy 10"));
    mainform.setBackground(App.slate100);
    mainform.add(usernamePasswordRow, "width 100%");
    mainform.add(firstLastNameRow, "width 100%");
    mainform.add(genderDobRow, "width 100%");
    mainform.add(emailPhoneRow, "width 100%");
    mainform.add(roleRow, "width 100%");

    departmentLabel = new JLabel();
    departmentLabel.setText("Department: ");
    departmentField = new TextField("Enter Department Here...");
    departmentField.setBackground(App.slate200);
    departmentField.setBorder(BorderFactory.createCompoundBorder(departmentField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    departmentField.setPreferredSize(new Dimension(600, 35));
    departmentErrorLabel = new JLabel("\s");
    departmentErrorLabel.setForeground(App.red600);
    departmentFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    departmentFieldGroup.setBackground(App.slate100);
    departmentFieldGroup.add(departmentLabel);
    departmentFieldGroup.add(departmentField);
    departmentFieldGroup.add(departmentErrorLabel);

    employmentTypeOptions = new ArrayList<>();
    for (EmploymentType employmentType : EmploymentType.values()) {
      employmentTypeOptions.add(new ComboBoxItem(employmentType.getValue(), employmentType.getDisplay()));
    }

    employmentTypeLabel = new JLabel();
    employmentTypeLabel.setText("Employment Type: ");
    employmentTypeComboBox = new JComboBox<>(employmentTypeOptions.stream().toArray(ComboBoxItem[]::new));
    employmentTypeComboBox.setBackground(App.slate200);
    employmentTypeComboBox.setBorder(BorderFactory.createCompoundBorder(employmentTypeComboBox.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    employmentTypeComboBox.setPreferredSize(new Dimension(200, 35));
    employmentTypeComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
      JLabel label = new JLabel();
      if (value != null) {
        label.setText(value.getLabelText());
      }
      return label;
    });
    employmentTypeErrorLabel = new JLabel("\s");
    employmentTypeErrorLabel.setForeground(App.red600);
    employmentTypeFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    employmentTypeFieldGroup.setBackground(App.slate100);
    employmentTypeFieldGroup.add(employmentTypeLabel);
    employmentTypeFieldGroup.add(employmentTypeComboBox);
    employmentTypeFieldGroup.add(employmentTypeErrorLabel);

    employedAtLabel = new JLabel();
    employedAtLabel.setText("Employed At: ");
    employedAtDateChooser = new JDateChooser();
    employedAtDateChooser.setDateFormatString(Helper.dateFormat);
    employedAtDateChooser.setBackground(App.slate200);
    employedAtDateChooser.getDateEditor().getUiComponent().setBackground(App.slate200);
    employedAtDateChooser.getDateEditor().getUiComponent().setBorder(BorderFactory.createCompoundBorder(employedAtDateChooser.getDateEditor().getUiComponent().getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    employedAtDateChooser.getCalendarButton().setBorder(BorderFactory.createCompoundBorder(employedAtDateChooser.getCalendarButton().getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    employedAtDateChooser.setPreferredSize(new Dimension(200, 35));
    employedAtErrorLabel = new JLabel("\s");
    employedAtErrorLabel.setForeground(App.red600);
    employedAtFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    employedAtFieldGroup.setBackground(App.slate100);
    employedAtFieldGroup.add(employedAtLabel);
    employedAtFieldGroup.add(employedAtDateChooser);
    employedAtFieldGroup.add(employedAtErrorLabel);

    facultyLabel = new JLabel();
    facultyLabel.setText("Faculty: ");
    facultyField = new TextField("Enter Faculty Here...");
    facultyField.setBackground(App.slate200);
    facultyField.setBorder(BorderFactory.createCompoundBorder(facultyField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    facultyField.setPreferredSize(new Dimension(600, 35));
    facultyErrorLabel = new JLabel("\s");
    facultyErrorLabel.setForeground(App.red600);
    facultyFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    facultyFieldGroup.setBackground(App.slate100);
    facultyFieldGroup.add(facultyLabel);
    facultyFieldGroup.add(facultyField);
    facultyFieldGroup.add(facultyErrorLabel);

    academicLeaderList = new ArrayList<>();
    academicLeaderList.add(new ComboBoxItem("0", "Not Available (N/A)"));
    for (User user : User.getListOfUsersByMatchingValues("role", Role.ACADEMIC_LEADER.getValue())) {
      if (user instanceof AcademicLeader academicLeaderUser) {
        academicLeaderList.add(new ComboBoxItem(academicLeaderUser.getID(), academicLeaderUser.getFirstName() + " " + academicLeaderUser.getLastName() + " - " + academicLeaderUser.getFaculty()));
      }
    }

    academicLeaderLabel = new JLabel();
    academicLeaderLabel.setText("Supervised by Academic Leader: ");
    academicLeaderComboBox = new JComboBox<>(academicLeaderList.stream().toArray(ComboBoxItem[]::new));
    academicLeaderComboBox.setBackground(App.slate200);
    academicLeaderComboBox.setBorder(BorderFactory.createCompoundBorder(academicLeaderComboBox.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    academicLeaderComboBox.setPreferredSize(new Dimension(200, 35));
    academicLeaderComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
      JLabel label = new JLabel();
      if (value != null) {
        label.setText(value.getLabelText());
      }
      return label;
    });
    academicLeaderErrorLabel = new JLabel("\s");
    academicLeaderErrorLabel.setForeground(App.red600);
    academicLeaderFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    academicLeaderFieldGroup.setBackground(App.slate100);
    academicLeaderFieldGroup.add(academicLeaderLabel);
    academicLeaderFieldGroup.add(academicLeaderComboBox);
    academicLeaderFieldGroup.add(academicLeaderErrorLabel);

    programLabel = new JLabel();
    programLabel.setText("Program: ");
    programField = new TextField("Enter Program Here...");
    programField.setBackground(App.slate200);
    programField.setBorder(BorderFactory.createCompoundBorder(programField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    programField.setPreferredSize(new Dimension(600, 35));
    programErrorLabel = new JLabel("\s");
    programErrorLabel.setForeground(App.red600);
    programFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    programFieldGroup.setBackground(App.slate100);
    programFieldGroup.add(programLabel);
    programFieldGroup.add(programField);
    programFieldGroup.add(programErrorLabel);

    modeOptions = new ArrayList<>();
    for (Mode mode : Mode.values()) {
      modeOptions.add(new ComboBoxItem(mode.getValue(), mode.getDisplay()));
    }

    modeLabel = new JLabel();
    modeLabel.setText("Mode of Study: ");
    modeComboBox = new JComboBox<>(modeOptions.stream().toArray(ComboBoxItem[]::new));
    modeComboBox.setBackground(App.slate200);
    modeComboBox.setBorder(BorderFactory.createCompoundBorder(modeComboBox.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    modeComboBox.setPreferredSize(new Dimension(200, 35));
    modeComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
      JLabel label = new JLabel();
      if (value != null) {
        label.setText(value.getLabelText());
      }
      return label;
    });
    modeErrorLabel = new JLabel("\s");
    modeErrorLabel.setForeground(App.red600);
    modeFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    modeFieldGroup.setBackground(App.slate100);
    modeFieldGroup.add(modeLabel);
    modeFieldGroup.add(modeComboBox);
    modeFieldGroup.add(modeErrorLabel);

    cgpaLabel = new JLabel();
    cgpaLabel.setText("CGPA: ");
    cgpaField = new TextField("Enter CGPA Here...");
    cgpaField.setBackground(App.slate200);
    cgpaField.setBorder(BorderFactory.createCompoundBorder(cgpaField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    cgpaField.setPreferredSize(new Dimension(600, 35));
    cgpaErrorLabel = new JLabel("\s");
    cgpaErrorLabel.setForeground(App.red600);
    cgpaFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    cgpaFieldGroup.setBackground(App.slate100);
    cgpaFieldGroup.add(cgpaLabel);
    cgpaFieldGroup.add(cgpaField);
    cgpaFieldGroup.add(cgpaErrorLabel);

    creditHoursLabel = new JLabel();
    creditHoursLabel.setText("Total Credit Hours: ");
    creditHoursField = new TextField("Enter Credit Hours Here...");
    creditHoursField.setBackground(App.slate200);
    creditHoursField.setBorder(BorderFactory.createCompoundBorder(creditHoursField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    creditHoursField.setPreferredSize(new Dimension(600, 35));
    creditHoursErrorLabel = new JLabel("\s");
    creditHoursErrorLabel.setForeground(App.red600);
    creditHoursFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    creditHoursFieldGroup.setBackground(App.slate100);
    creditHoursFieldGroup.add(creditHoursLabel);
    creditHoursFieldGroup.add(creditHoursField);
    creditHoursFieldGroup.add(creditHoursErrorLabel);

    enrolledAtLabel = new JLabel();
    enrolledAtLabel.setText("Enrolled At: ");
    enrolledAtDateChooser = new JDateChooser();
    enrolledAtDateChooser.setDateFormatString(Helper.dateFormat);
    enrolledAtDateChooser.setBackground(App.slate200);
    enrolledAtDateChooser.getDateEditor().getUiComponent().setBackground(App.slate200);
    enrolledAtDateChooser.getDateEditor().getUiComponent().setBorder(BorderFactory.createCompoundBorder(enrolledAtDateChooser.getDateEditor().getUiComponent().getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    enrolledAtDateChooser.getCalendarButton().setBorder(BorderFactory.createCompoundBorder(enrolledAtDateChooser.getCalendarButton().getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    enrolledAtDateChooser.setPreferredSize(new Dimension(200, 35));
    enrolledAtErrorLabel = new JLabel("\s");
    enrolledAtErrorLabel.setForeground(App.red600);
    enrolledAtFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    enrolledAtFieldGroup.setBackground(App.slate100);
    enrolledAtFieldGroup.add(enrolledAtLabel);
    enrolledAtFieldGroup.add(enrolledAtDateChooser);
    enrolledAtFieldGroup.add(enrolledAtErrorLabel);

    departmentRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    departmentRow.setBackground(App.slate100);
    departmentRow.add(departmentFieldGroup);

    employmentInfoRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    employmentInfoRow.setBackground(App.slate100);
    employmentInfoRow.add(employmentTypeFieldGroup, "width 50%");
    employmentInfoRow.add(employedAtFieldGroup, "width 50%");

    facultyRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    facultyRow.setBackground(App.slate100);
    facultyRow.add(facultyFieldGroup);

    academicLeaderRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    academicLeaderRow.setBackground(App.slate100);
    academicLeaderRow.add(academicLeaderFieldGroup);

    programModeRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    programModeRow.setBackground(App.slate100);
    programModeRow.add(programFieldGroup, "width 50%");
    programModeRow.add(modeFieldGroup, "width 50%");

    cgpaCreditHoursRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    cgpaCreditHoursRow.setBackground(App.slate100);
    cgpaCreditHoursRow.add(cgpaFieldGroup);
    cgpaCreditHoursRow.add(creditHoursFieldGroup);

    enrolledAtRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    enrolledAtRow.setBackground(App.slate100);
    enrolledAtRow.add(enrolledAtFieldGroup);

    subform = new JPanel(new MigLayout("insets 30 0, wrap 1, gapy 10"));
    subform.setBackground(App.slate100);

    formTabbedPane = new JTabbedPane();
    formTabbedPane.addTab("User Information", mainform);
    Role currRole = actionContext.equals("edit") ? editingUser.getRole() : Role.ADMIN;
    formTabbedPane.addTab(currRole.getDisplay() + " Details", subform);

    roleComboBox.addActionListener(e -> {
      ComboBoxItem selectedRoleBoxItem = (ComboBoxItem) roleComboBox.getSelectedItem();
      changeSubForm(Role.fromValue(selectedRoleBoxItem.getValue()));
    });

    changeSubForm(currRole);

    textFields = Map.ofEntries(
      Map.entry("username", usernameField),
      Map.entry("password", passwordField),
      Map.entry("firstName", firstNameField),
      Map.entry("lastName", lastNameField),
      Map.entry("email", emailField),
      Map.entry("phoneNumber", phoneNumberField),
      Map.entry("department", departmentField),
      Map.entry("faculty", facultyField),
      Map.entry("program", programField),
      Map.entry("cgpa", cgpaField),
      Map.entry("creditHours", creditHoursField)
    );

    comboBoxes = Map.ofEntries(
      Map.entry("gender", genderComboBox),
      Map.entry("role", roleComboBox),
      Map.entry("employmentType", employmentTypeComboBox),
      Map.entry("academicLeader", academicLeaderComboBox),
      Map.entry("mode", modeComboBox)
    );

    dateChoosers = Map.ofEntries(
      Map.entry("dob", dobDateChooser),
      Map.entry("employedAt", employedAtDateChooser),
      Map.entry("enrolledAt", enrolledAtDateChooser)
    );

    errorLabels = Map.ofEntries(
      Map.entry("username", usernameErrorLabel),
      Map.entry("password", passwordErrorLabel),
      Map.entry("firstName", firstNameErrorLabel),
      Map.entry("lastName", lastNameErrorLabel),
      Map.entry("gender", genderErrorLabel),
      Map.entry("dob", dobErrorLabel),
      Map.entry("email", emailErrorLabel),
      Map.entry("phoneNumber", phoneNumberErrorLabel),
      Map.entry("role", roleErrorLabel),
      Map.entry("department", departmentErrorLabel),
      Map.entry("faculty", facultyErrorLabel),
      Map.entry("program", programErrorLabel),
      Map.entry("cgpa", cgpaErrorLabel),
      Map.entry("creditHours", creditHoursErrorLabel),
      Map.entry("employmentType", employmentTypeErrorLabel),
      Map.entry("academicLeader", academicLeaderErrorLabel),
      Map.entry("mode", modeErrorLabel),
      Map.entry("employedAt", employedAtErrorLabel),
      Map.entry("enrolledAt", enrolledAtErrorLabel)
    );

    submitBtn = new JButton();
    submitBtn.setText("Submit");
    submitBtn.setForeground(Color.WHITE);
    submitBtn.setBackground(App.green600);
    submitBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    submitBtn.setBorder(BorderFactory.createCompoundBorder(submitBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    submitBtn.setFocusable(false);
    submitBtn.addActionListener(e -> {
      HashMap<String, String> inputValues = new HashMap<>();
      inputValues.put("id", actionContext.equals("edit") ? editingUser.getID() : null);
      for (String fieldKey : textFields.keySet()) {
        inputValues.put(fieldKey, textFields.get(fieldKey).getText().trim());
      }

      for (String comboBoxKey : comboBoxes.keySet()) {
        ComboBoxItem selectedComboBoxItem = (ComboBoxItem) comboBoxes.get(comboBoxKey).getSelectedItem();
        inputValues.put(comboBoxKey, selectedComboBoxItem.getValue());
      }

      for (String dateChooserKey : dateChoosers.keySet()) {
        if (dateChoosers.get(dateChooserKey).getDate() == null) {
          inputValues.put(dateChooserKey, "");
        } else {
          inputValues.put(dateChooserKey, Helper.simpleFormatter.format(dateChoosers.get(dateChooserKey).getDate()));
        }
      }

      String userRole = inputValues.get("role");
      Validation inputValidation = User.validateUser(inputValues);
      Validation detailsValidation;
      if (userRole.equals("admin")) {
          detailsValidation = Admin.validate(inputValues);
        } else if (userRole.equals("academic")) {
          detailsValidation = AcademicLeader.validate(inputValues);
        } else if (userRole.equals("lecturer")) {
          detailsValidation = Lecturer.validate(inputValues);
        } else {
          detailsValidation = Student.validate(inputValues);
        }
      if (inputValidation.getSuccess() && detailsValidation.getSuccess()) {
        User user;
        if (userRole.equals("admin")) {
          user = new Admin(inputValues);
        } else if (userRole.equals("academic")) {
          user = new AcademicLeader(inputValues);
        } else if (userRole.equals("lecturer")) {
          user = new Lecturer(inputValues);
        } else {
          user = new Student(inputValues);
        }

        user.updateUser();
        state.setSelectedUserID(user.getID());

        String userInfoDisplay = "\nUser ID:" + user.getID() + "\nUsername: " + user.getUsername() + "\nRole: " + user.getRole().getDisplay();
        String messageDialogContent = actionContext.equals("edit") ? "Current User has been updated!" + userInfoDisplay : "New User has been created!" + userInfoDisplay;
        String messageDialogTitle = actionContext.equals("edit") ? "Success: Updated Selected User" : "Success: Created New User";
        JOptionPane.showMessageDialog(router, messageDialogContent, messageDialogTitle, JOptionPane.INFORMATION_MESSAGE);
        router.showView(Pages.USER, state);
      } else {
        if (!inputValidation.getSuccess()) {
          this.displayError(inputValidation);
          String messageDialogTitle = actionContext.equals("edit") ? "Cannot edit User: " + editingUser.getID() : "Cannot create new User"; 
          JOptionPane.showMessageDialog(router, inputValidation.getMessage(), "Error: Invalid Form input! " + messageDialogTitle, JOptionPane.ERROR_MESSAGE);
        }

        if (!detailsValidation.getSuccess()) {
          this.displayError(detailsValidation);
          String messageDialogTitle = actionContext.equals("edit") ? "Cannot edit User: " + editingUser.getID() : "Cannot create new User"; 
          JOptionPane.showMessageDialog(router, detailsValidation.getMessage(), "Error: Invalid Form input! " + messageDialogTitle, JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    deleteBtn = new JButton();
    deleteBtn.setText("Delete User");
    deleteBtn.setForeground(Color.WHITE);
    deleteBtn.setBackground(App.red600);
    deleteBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    deleteBtn.setBorder(BorderFactory.createCompoundBorder(deleteBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    deleteBtn.setFocusable(false);
    deleteBtn.addActionListener(e -> {
      if (actionContext.equals("edit") && editingUser.getID().equals(state.getCurrUser().getID())) {
        JOptionPane.showMessageDialog(router, "This User cannot be deleted as it is used in the current session", "Error: Unable to delete current user", JOptionPane.ERROR_MESSAGE);
      } else if (actionContext.equals("edit")) {
        String userInfoDisplay = "\nUser ID: " + editingUser.getID() + "\nUsername: " + editingUser.getUsername() + "\nRole: " + editingUser.getRole().getDisplay();
        int choice = JOptionPane.showConfirmDialog(router, "Are you sure you want to delete this user?" + userInfoDisplay, "Delete This User Confirmation", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
          editingUser.deleteUser();
          router.showView(Pages.MANAGEUSERS, state);
          JOptionPane.showMessageDialog(router, "This User has been deleted successfully" + userInfoDisplay, "User has been Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
      }
    });

    actionButtonGroup = new JPanel(new MigLayout("insets 50 0, aligny center"));
    actionButtonGroup.setBackground(App.slate100);
    if (actionContext.equals("edit")) {
      actionButtonGroup.add(deleteBtn);
    }
    actionButtonGroup.add(submitBtn, "push, alignx right");

    contentBody.add(formTitle);
    contentBody.add(formTabbedPane, "width 100%");
    contentBody.add(actionButtonGroup, "width 100%");
    
    this.add(header, "span, growx, wrap");
    this.add(nav, "growy");
    this.add(contentBody, "span, grow");

    state.clearState();
  }

  private void resetSubFormFields() {
    departmentField.setBackground(App.slate200);
    departmentField.setText("");
    departmentErrorLabel.setText("\s");

    employmentTypeComboBox.setBackground(App.slate200);
    employmentTypeComboBox.setSelectedIndex(0);
    employmentTypeErrorLabel.setText("\s");

    employedAtDateChooser.setBackground(App.slate200);
    employedAtDateChooser.setDate(null);
    employedAtErrorLabel.setText("\s");

    facultyField.setBackground(App.slate200);
    facultyField.setText("");
    facultyErrorLabel.setText("\s");

    academicLeaderComboBox.setBackground(App.slate200);
    academicLeaderComboBox.setSelectedIndex(0);
    academicLeaderErrorLabel.setText("\s");

    programField.setBackground(App.slate200);
    programField.setText("");
    programErrorLabel.setText("\s");

    modeComboBox.setBackground(App.slate200);
    modeComboBox.setSelectedIndex(0);
    modeErrorLabel.setText("\s");

    cgpaField.setBackground(App.slate200);
    cgpaField.setText("");
    cgpaErrorLabel.setText("\s");
    
    creditHoursField.setBackground(App.slate200);
    creditHoursField.setText("");
    creditHoursErrorLabel.setText("\s");

    enrolledAtDateChooser.setBackground(App.slate200);
    enrolledAtDateChooser.setDate(null);
    enrolledAtErrorLabel.setText("\s");

    subform.removeAll();
    formTabbedPane.remove(subform);
    formTabbedPane.revalidate();
    formTabbedPane.repaint();
  }

  private void changeSubForm(Role role) {
    resetSubFormFields();
    
    if (role.getValue().equals(Role.ADMIN.getValue())) {
      if (actionContext.equals("edit") && editingUser instanceof Admin editingAdmin) {
        departmentField.setText(editingAdmin.getDepartment());
        employedAtDateChooser.setDate(Helper.convertLocalDateToDate(editingAdmin.getEmployedAt()));

        for (int i = 0; i < employmentTypeComboBox.getItemCount(); i++) {
          ComboBoxItem item = employmentTypeComboBox.getItemAt(i);
          if (item.getValue().equals(editingAdmin.getEmploymentType().getValue())) {
            employmentTypeComboBox.setSelectedIndex(i);
            break;
          }
        }
      }

      subform.add(departmentFieldGroup, "width 100%");
      subform.add(employmentInfoRow, "width 100%");
    } else if (role.getValue().equals(Role.ACADEMIC_LEADER.getValue())) {
      if (actionContext.equals("edit") && editingUser instanceof AcademicLeader editingAcademicLeader) {
        facultyField.setText(editingAcademicLeader.getFaculty());
        employedAtDateChooser.setDate(Helper.convertLocalDateToDate(editingAcademicLeader.getEmployedAt()));

        for (int i = 0; i < employmentTypeComboBox.getItemCount(); i++) {
          ComboBoxItem item = employmentTypeComboBox.getItemAt(i);
          if (item.getValue().equals(editingAcademicLeader.getEmploymentType().getValue())) {
            employmentTypeComboBox.setSelectedIndex(i);
            break;
          }
        }
      }

      subform.add(facultyFieldGroup, "width 100%");
      subform.add(employmentInfoRow, "width 100%");
    } else if (role.getValue().equals(Role.LECTURER.getValue())) {
      if (actionContext.equals("edit") && editingUser instanceof Lecturer editingLecturer) {
        employedAtDateChooser.setDate(Helper.convertLocalDateToDate(editingLecturer.getEmployedAt()));

        if (editingLecturer.getAcademicLeader() == null) {
          academicLeaderComboBox.setSelectedIndex(0);
        } else {
          for (int i = 0; i < academicLeaderComboBox.getItemCount(); i++) {
            ComboBoxItem item = academicLeaderComboBox.getItemAt(i);
            if (item.getValue().equals(editingLecturer.getAcademicLeader().getID())) {
              academicLeaderComboBox.setSelectedIndex(i);
              break;
            }
          }
        }

        for (int i = 0; i < employmentTypeComboBox.getItemCount(); i++) {
          ComboBoxItem item = employmentTypeComboBox.getItemAt(i);
          if (item.getValue().equals(editingLecturer.getEmploymentType().getValue())) {
            employmentTypeComboBox.setSelectedIndex(i);
            break;
          }
        }
      }

      subform.add(academicLeaderRow, "width 100%");
      subform.add(employmentInfoRow, "width 100%");
    } else if (role.getValue().equals(Role.STUDENT.getValue())) {
      if (actionContext.equals("edit") && editingUser instanceof Student editingStudent) {
        programField.setText(editingStudent.getProgram());
        cgpaField.setText(String.valueOf(editingStudent.getCgpa()));
        creditHoursField.setText(String.valueOf(editingStudent.getCreditHours()));

        enrolledAtDateChooser.setDate(Helper.convertLocalDateToDate(editingStudent.getEnrolledAt()));

        for (int i = 0; i < modeComboBox.getItemCount(); i++) {
          ComboBoxItem item = modeComboBox.getItemAt(i);
          if (item.getValue().equals(editingStudent.getMode().getValue())) {
            modeComboBox.setSelectedIndex(i);
            break;
          }
        }
      }

      subform.add(programModeRow, "width 100%");
      subform.add(cgpaCreditHoursRow, "width 100%");
      subform.add(enrolledAtRow, "width 100%");
    }

    formTabbedPane.add(role.getDisplay() + " Details", subform);
    formTabbedPane.revalidate();
    formTabbedPane.repaint();
  }

  private void displayError(Validation validation) {
    if (textFields.get(validation.getField()) != null) {
      textFields.get(validation.getField()).setBackground(App.red100);
    } else if (comboBoxes.get(validation.getField()) != null) {
      comboBoxes.get(validation.getField()).setBackground(App.red100);
    } else if (dateChoosers.get(validation.getField()) != null) {
      dateChoosers.get(validation.getField()).setBackground(App.red100);
    }

    if (errorLabels.get(validation.getField()) != null) {
      errorLabels.get(validation.getField()).setText(validation.getMessage());
    }
  }
}
