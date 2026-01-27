package com.apu_afs.Views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
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
import com.apu_afs.Models.Enums.Mode;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Models.Enums.Role;
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

    contentBody = new JPanel(new MigLayout("insets 20 20, wrap 1, gapy 10, align center center"));
    contentBody.setBackground(App.slate100);

    profileImage = new JLabel();
    profileImage.setIcon(Helper.iconResizer(new ImageIcon("assets/profile-user-icon-black.png"), 256, 256));
    profileUsername = new JLabel();
    profileUsername.setText("@" + state.getCurrUser().getUsername());
    profileUsername.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
    profileFullName = new JLabel();
    profileFullName.setText(state.getCurrUser().getFirstName() + " " + state.getCurrUser().getLastName());
    profileFullName.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
    LocalDate todayDate = LocalDate.now();
    profileGenderAge = new JLabel();
    profileGenderAge.setText(state.getCurrUser().getGender().getDisplay() + ", " + String.valueOf(Period.between(state.getCurrUser().getDob(), todayDate).getYears()));
    profileGenderAge.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
    profileRole = new JLabel();
    profileRole.setText("Role: " + state.getCurrUser().getRole().getDisplay());
    profileRole.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

    profileDetailsContainer = new JPanel(new MigLayout("insets 0, wrap 1, gapy 20"));
    profileDetailsContainer.setBackground(App.slate100);
    profileDetailsContainer.add(profileImage);
    profileDetailsContainer.add(profileUsername);
    profileDetailsContainer.add(profileFullName);
    profileDetailsContainer.add(profileGenderAge);
    profileDetailsContainer.add(profileRole);

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

    currentPasswordLabel = new JLabel();
    currentPasswordLabel.setText("Current Password (Leave blank if you do not intend to change password)");
    currentPasswordField = new TextField("Enter Current Password Here...");
    currentPasswordField.setBackground(App.slate200);
    currentPasswordField.setBorder(BorderFactory.createCompoundBorder(currentPasswordField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    currentPasswordField.setPreferredSize(new Dimension(600, 35));
    currentPasswordErrorLabel = new JLabel("\s");
    currentPasswordErrorLabel.setForeground(App.red600);
    currentPasswordFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    currentPasswordFieldGroup.setBackground(App.slate100);
    currentPasswordFieldGroup.add(currentPasswordLabel);
    currentPasswordFieldGroup.add(currentPasswordField);
    currentPasswordFieldGroup.add(currentPasswordErrorLabel);

    newPasswordLabel = new JLabel();
    newPasswordLabel.setText("New Password (Must fill current password first in order to change to a new password)");
    newPasswordField = new TextField("Enter New Password Here...");
    newPasswordField.setBackground(App.slate200);
    newPasswordField.setBorder(BorderFactory.createCompoundBorder(newPasswordField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    newPasswordField.setPreferredSize(new Dimension(600, 35));
    newPasswordErrorLabel = new JLabel("\s");
    newPasswordErrorLabel.setForeground(App.red600);
    newPasswordFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    newPasswordFieldGroup.setBackground(App.slate100);
    newPasswordFieldGroup.add(newPasswordLabel);
    newPasswordFieldGroup.add(newPasswordField);
    newPasswordFieldGroup.add(newPasswordErrorLabel);

    emailLabel = new JLabel();
    emailLabel.setText("Email: ");
    emailField = new TextField("Enter Email Here...");
    emailField.setBackground(App.slate200);
    emailField.setBorder(BorderFactory.createCompoundBorder(emailField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    emailField.setPreferredSize(new Dimension(600, 35));
    emailField.setText(state.getCurrUser().getEmail());
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
    phoneNumberField.setText(state.getCurrUser().getPhoneNumber());
    phoneNumberErrorLabel = new JLabel("\s");
    phoneNumberErrorLabel.setForeground(App.red600);
    phoneNumberFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    phoneNumberFieldGroup.setBackground(App.slate100);
    phoneNumberFieldGroup.add(phoneNumberLabel);
    phoneNumberFieldGroup.add(phoneNumberField);
    phoneNumberFieldGroup.add(phoneNumberErrorLabel);

    usernameRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    usernameRow.setBackground(App.slate100);
    usernameRow.add(usernameFieldGroup);

    currentPasswordRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    currentPasswordRow.setBackground(App.slate100);
    currentPasswordRow.add(currentPasswordFieldGroup);

    newPasswordRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    newPasswordRow.setBackground(App.slate100);
    newPasswordRow.add(newPasswordFieldGroup);

    emailRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    emailRow.setBackground(App.slate100);
    emailRow.add(emailFieldGroup);

    phoneNumberRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    phoneNumberRow.setBackground(App.slate100);
    phoneNumberRow.add(phoneNumberFieldGroup);

    departmentLabel = new JLabel();
    departmentLabel.setText("Department: ");
    departmentField = new TextField("Enter Department Here...");
    departmentField.setBackground(App.slate200);
    departmentField.setBorder(BorderFactory.createCompoundBorder(departmentField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    departmentField.setPreferredSize(new Dimension(600, 35));
    departmentField.setEditable(false);
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
    employmentTypeComboBox.setEnabled(false);
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
    employedAtDateChooser.setEnabled(false);
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
    facultyField.setEditable(false);
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
    academicLeaderComboBox.setEnabled(false);
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
    programField.setEditable(false);
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
    modeComboBox.setEnabled(false);
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
    cgpaField.setEditable(false);
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
    creditHoursField.setEditable(false);
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
    enrolledAtDateChooser.setEnabled(false);
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
    employmentInfoRow.add(employmentTypeFieldGroup);
    employmentInfoRow.add(employedAtFieldGroup);

    facultyRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    facultyRow.setBackground(App.slate100);
    facultyRow.add(facultyFieldGroup);

    academicLeaderRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    academicLeaderRow.setBackground(App.slate100);
    academicLeaderRow.add(academicLeaderFieldGroup);

    programModeRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    programModeRow.setBackground(App.slate100);
    programModeRow.add(programFieldGroup);
    programModeRow.add(modeFieldGroup);

    cgpaCreditHoursRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    cgpaCreditHoursRow.setBackground(App.slate100);
    cgpaCreditHoursRow.add(cgpaFieldGroup);
    cgpaCreditHoursRow.add(creditHoursFieldGroup);

    enrolledAtRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    enrolledAtRow.setBackground(App.slate100);
    enrolledAtRow.add(enrolledAtFieldGroup);

    mainform = new JPanel(new MigLayout("insets 30 0, wrap 1, gapy 10"));
    mainform.setBackground(App.slate100);
    mainform.add(usernameRow);
    mainform.add(currentPasswordRow);
    mainform.add(newPasswordRow);
    mainform.add(emailRow);
    mainform.add(phoneNumberRow);

    subform = new JPanel(new MigLayout("insets 30 0, wrap 1, gapy 10"));
    subform.setBackground(App.slate100);

    formTabbedPane = new JTabbedPane();
    formTabbedPane.addTab("User Information", mainform);
    formTabbedPane.addTab(state.getCurrUser().getRole().getDisplay() + " Details (View Only)", subform);

    User userRef = state.getCurrUser();
    if (userRef instanceof Admin editingAdmin) {
      departmentField.setText(editingAdmin.getDepartment());
      employedAtDateChooser.setDate(Helper.convertLocalDateToDate(editingAdmin.getEmployedAt()));

      for (int i = 0; i < employmentTypeComboBox.getItemCount(); i++) {
        ComboBoxItem item = employmentTypeComboBox.getItemAt(i);
        if (item.getValue().equals(editingAdmin.getEmploymentType().getValue())) {
          employmentTypeComboBox.setSelectedIndex(i);
          break;
        }
      }

      subform.add(departmentRow);
      subform.add(employmentInfoRow);
    } else if (userRef instanceof AcademicLeader editingAcademicLeader) {
      facultyField.setText(editingAcademicLeader.getFaculty());
      employedAtDateChooser.setDate(Helper.convertLocalDateToDate(editingAcademicLeader.getEmployedAt()));

      for (int i = 0; i < employmentTypeComboBox.getItemCount(); i++) {
        ComboBoxItem item = employmentTypeComboBox.getItemAt(i);
        if (item.getValue().equals(editingAcademicLeader.getEmploymentType().getValue())) {
          employmentTypeComboBox.setSelectedIndex(i);
          break;
        }
      }

      subform.add(facultyRow);
      subform.add(employmentInfoRow);
    } else if (userRef instanceof Lecturer editingLecturer) {
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

      subform.add(academicLeaderRow);
      subform.add(employmentInfoRow);
    } else if (userRef instanceof Student editingStudent) {
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

      subform.add(programModeRow);
      subform.add(cgpaCreditHoursRow);
      subform.add(enrolledAtRow);
    }

    textFields = Map.ofEntries(
      Map.entry("username", usernameField),
      Map.entry("currentPassword", currentPasswordField),
      Map.entry("newPassword", newPasswordField),
      Map.entry("email", emailField),
      Map.entry("phoneNumber", phoneNumberField)
    );

    errorLabels = Map.ofEntries(
      Map.entry("username", usernameErrorLabel),
      Map.entry("currentPassword", currentPasswordErrorLabel),
      Map.entry("newPassword", newPasswordErrorLabel),
      Map.entry("email", emailErrorLabel),
      Map.entry("phoneNumber", phoneNumberErrorLabel)
    );

    editBtn = new JButton();
    editBtn.setText("Edit");
    editBtn.setIcon(Helper.iconResizer(new ImageIcon("assets/header-edit-profile.png"), 18, 18));
    editBtn.setForeground(Color.WHITE);
    editBtn.setBackground(App.green600);
    editBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    editBtn.setBorder(BorderFactory.createCompoundBorder(editBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    editBtn.setFocusable(false);
    editBtn.addActionListener(e -> {
      HashMap<String, String> inputValues = new HashMap<>();
      for (String key : textFields.keySet()) {
        inputValues.put(key, textFields.get(key).getText().trim());
      }

      Validation profileValidation = User.validateEditingProfile(inputValues, state.getCurrUser().getUsername());
      if (profileValidation.getSuccess()) {
        state.getCurrUser().setUsername(inputValues.get("username"));
        state.getCurrUser().setEmail(inputValues.get("email"));
        state.getCurrUser().setPhoneNumber(inputValues.get("phoneNumber"));

        if (!inputValues.get("currentPassword").isEmpty()) {
          state.getCurrUser().setPassword(inputValues.get("newPassword"));
        }

        router.showView(Pages.PROFILE, state);
        JOptionPane.showMessageDialog(router, "Your Profile information has been successfully edited!", "Success: Edited Profile", JOptionPane.INFORMATION_MESSAGE);
      } else {
        this.displayError(router, profileValidation);
      }
    });

    logoutBtn = new JButton();
    logoutBtn.setText("Logout");
    logoutBtn.setIcon(Helper.iconResizer(new ImageIcon("assets/icon-logout.png"), 18, 18));
    logoutBtn.setForeground(Color.WHITE);
    logoutBtn.setBackground(App.red600);
    logoutBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    logoutBtn.setBorder(BorderFactory.createCompoundBorder(logoutBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    logoutBtn.setFocusable(false);
    logoutBtn.addActionListener(e -> {
      state.hardResetState();
      router.showView(Pages.LOGIN, state);
      JOptionPane.showMessageDialog(router, "You have successfully logout from AFS!", "AFS | Logout", JOptionPane.INFORMATION_MESSAGE);
    });

    actionButtonGroup = new JPanel(new MigLayout("insets 50 0, aligny center"));
    actionButtonGroup.setBackground(App.slate100);
    actionButtonGroup.add(logoutBtn);
    actionButtonGroup.add(editBtn, "push, alignx right");

    profileFormContainer = new JPanel(new MigLayout("insets 0, gapx 60"));
    profileFormContainer.setBackground(App.slate100);
    profileFormContainer.add(profileDetailsContainer);
    profileFormContainer.add(formTabbedPane, "aligny top, wrap");
    profileFormContainer.add(actionButtonGroup, "span, growx");

    contentBody.add(profileFormContainer);

    this.add(header, "span, growx, wrap");
    this.add(nav, "growy");
    this.add(contentBody, "span, grow");
    state.clearState();
  }
  
  private void displayError(Router router, Validation validation) {
    if (textFields.get(validation.getField()) != null) {
      textFields.get(validation.getField()).setBackground(App.red100);
    }

    if (errorLabels.get(validation.getField()) != null) {
      errorLabels.get(validation.getField()).setText(validation.getMessage());
    }

    String messageDialogTitle = "Cannot edit profile"; 
    JOptionPane.showMessageDialog(router, validation.getMessage(), "Error: Invalid Form input! " + messageDialogTitle, JOptionPane.ERROR_MESSAGE);
  }
}
