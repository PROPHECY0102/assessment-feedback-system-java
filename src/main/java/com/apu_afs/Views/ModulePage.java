package com.apu_afs.Views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.apu_afs.GlobalState;
import com.apu_afs.Helper;
import com.apu_afs.Models.AcademicLeader;
import com.apu_afs.Models.Admin;
import com.apu_afs.Models.ComboBoxItem;
import com.apu_afs.Models.Lecturer;
import com.apu_afs.Models.Module;
import com.apu_afs.Models.Student;
import com.apu_afs.Models.StudentModule;
import com.apu_afs.Models.User;
import com.apu_afs.Models.Validation;
import com.apu_afs.Models.Enums.ModuleStatus;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Models.Enums.Role;
import com.apu_afs.TableModels.StudentModuleTableModel;
import com.apu_afs.Views.components.FixedTextArea;
import com.apu_afs.Views.components.HeaderPanel;
import com.apu_afs.Views.components.NavPanel;
import com.apu_afs.Views.components.TextField;
import com.toedter.calendar.JDateChooser;

import net.miginfocom.swing.MigLayout;

public class ModulePage extends JPanel {

  HeaderPanel header;
  NavPanel nav;

  JPanel contentBody;

  JLabel formTitle;

  JTabbedPane formTabbedPane;
  JPanel mainform;
  JPanel studentTab;

  JPanel codeTitleRow;
  JPanel codeFieldGroup;
  JLabel codeLabel;
  TextField codeField;
  JLabel codeErrorLabel;
  JPanel titleFieldGroup;
  JLabel titleLabel;
  TextField titleField;
  JLabel titleErrorLabel;

  JPanel descriptionRow;
  JPanel descriptionFieldGroup;
  JLabel descriptionLabel;
  JTextArea descriptionTextArea;
  JLabel descriptionErrorLabel;

  JPanel creditHoursCreatedAtRow;
  JPanel creditHoursFieldGroup;
  JLabel creditHoursLabel;
  TextField creditHoursField;
  JLabel creditHoursErrorLabel;
  JPanel createdAtFieldGroup;
  JLabel createdAtLabel;
  JDateChooser createdAtDateChooser;
  JLabel createdAtErrorLabel;

  ArrayList<ComboBoxItem> academicLeaderList;
  ArrayList<ComboBoxItem> lecturerList;

  JPanel academicLeaderLecturerRow;
  JPanel academicLeaderFieldGroup;
  JLabel academicLeaderLabel;
  JComboBox<ComboBoxItem> academicLeaderComboBox;
  JLabel academicLeaderErrorLabel;
  JPanel lecturerFieldGroup;
  JLabel lecturerLabel;
  JComboBox<ComboBoxItem> lecturerComboBox;
  JLabel lecturerErrorLabel;

  JPanel actionButtonGroup;
  JButton submitBtn;
  JButton deleteBtn;

  String searchInput;
  Set<String> studentModuleStatusConditions;

  JPanel searchSection;
  TextField searchField;
  JButton searchClearBtn;
  JButton searchBtn;

  JPanel filterOptionsContainer;
  JCheckBox filterUnregisteredCheckButton;
  JCheckBox filterActiveCheckButton;
  JCheckBox filterCompletedCheckButton;
  JCheckBox filterSuspendedCheckButton;
  JCheckBox filterDroppedCheckButton;

  JPanel searchFilterGroup;

  List<ComboBoxItem> moduleStatusOptions;

  JPanel studentTabActionGroup;
  JLabel selectedStudentDisplay;
  JPanel moduleStatusRow;
  JComboBox<ComboBoxItem> moduleStatusComboBox;
  JButton updateBtn;

  JPanel searchFilterActionRow;

  JLabel studentCount;
  JTable studentModuleTable;

  String actionContext;
  Module editingModule;

  Map<String, TextField> textFields;
  Map<String, JTextArea> textAreas;
  Map<String, JDateChooser> dateChoosers;
  Map<String, JComboBox<ComboBoxItem>> comboBoxes;
  Map<String, JLabel> errorLabels;

  List<Student> students;
  List<StudentModule> studentModules;
  StudentModuleTableModel smTableModel;
  
  public ModulePage(Router router, GlobalState state) {
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

    if (state.getSelectedModuleID() == null) {
      actionContext = "add";
      editingModule = null;
    } else {
      actionContext = "edit";
      editingModule = Module.getModuleByMatchingValues("id", state.getSelectedModuleID());
    }

    header = new HeaderPanel(router, state);
    nav = new NavPanel(router, state);

    contentBody = new JPanel(new MigLayout("insets 20 20, wrap 1, gapy 10"));
    contentBody.setBackground(App.slate100);

    formTitle = new JLabel();
    if (actionContext.equals("edit")) {
      formTitle.setText("Editing Module: " + editingModule.getCode());
    } else {
      formTitle.setText("Create New Module Form");
    }
    formTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

    codeLabel = new JLabel();
    codeLabel.setText("Module Code: ");
    codeField = new TextField("Enter Module Code Here...");
    codeField.setBackground(App.slate200);
    codeField.setBorder(BorderFactory.createCompoundBorder(codeField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    codeField.setPreferredSize(new Dimension(600, 35));
    if (actionContext.equals("edit")) {
      codeField.setText(editingModule.getCode());
    }
    codeErrorLabel = new JLabel("\s");
    codeErrorLabel.setForeground(App.red600);
    codeFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    codeFieldGroup.setBackground(App.slate100);
    codeFieldGroup.add(codeLabel);
    codeFieldGroup.add(codeField);
    codeFieldGroup.add(codeErrorLabel);

    titleLabel = new JLabel();
    titleLabel.setText("Title: ");
    titleField = new TextField("Enter Title Here...");
    titleField.setBackground(App.slate200);
    titleField.setBorder(BorderFactory.createCompoundBorder(titleField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    titleField.setPreferredSize(new Dimension(600, 35));
    if (actionContext.equals("edit")) {
      titleField.setText(editingModule.getTitle());
    }
    titleErrorLabel = new JLabel("\s");
    titleErrorLabel.setForeground(App.red600);
    titleFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    titleFieldGroup.setBackground(App.slate100);
    titleFieldGroup.add(titleLabel);
    titleFieldGroup.add(titleField);
    titleFieldGroup.add(titleErrorLabel);

    descriptionLabel = new JLabel();
    descriptionLabel.setText("Description");
    descriptionTextArea = FixedTextArea.createFixedTextArea(3, 82, 500);
    descriptionTextArea.setBackground(App.slate200);
    descriptionTextArea.setBorder(BorderFactory.createCompoundBorder(descriptionTextArea.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    JScrollPane descriptionScrollPane = new JScrollPane(descriptionTextArea);
    descriptionTextArea.setText(actionContext.equals("edit") ? editingModule.getDescription() : "No description");
    descriptionErrorLabel = new JLabel("\s");
    descriptionErrorLabel.setForeground(App.red600);
    descriptionFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    descriptionFieldGroup.setBackground(App.slate100);
    descriptionFieldGroup.add(descriptionLabel);
    descriptionFieldGroup.add(descriptionScrollPane);
    descriptionFieldGroup.add(descriptionErrorLabel);

    creditHoursLabel = new JLabel();
    creditHoursLabel.setText("Credit Hours: ");
    creditHoursField = new TextField("Enter Credit Hours Here...");
    creditHoursField.setBackground(App.slate200);
    creditHoursField.setBorder(BorderFactory.createCompoundBorder(creditHoursField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    creditHoursField.setPreferredSize(new Dimension(200, 35));
    if (actionContext.equals("edit")) {
      creditHoursField.setText(String.valueOf(editingModule.getCreditHours()));
    }
    creditHoursErrorLabel = new JLabel("\s");
    creditHoursErrorLabel.setForeground(App.red600);
    creditHoursFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    creditHoursFieldGroup.setBackground(App.slate100);
    creditHoursFieldGroup.add(creditHoursLabel);
    creditHoursFieldGroup.add(creditHoursField);
    creditHoursFieldGroup.add(creditHoursErrorLabel);

    createdAtLabel = new JLabel();
    createdAtLabel.setText("Created At: ");
    createdAtDateChooser = new JDateChooser();
    createdAtDateChooser.setDateFormatString(Helper.dateFormat);
    createdAtDateChooser.setBackground(App.slate200);
    createdAtDateChooser.getDateEditor().getUiComponent().setBackground(App.slate200);
    createdAtDateChooser.getDateEditor().getUiComponent().setBorder(BorderFactory.createCompoundBorder(createdAtDateChooser.getDateEditor().getUiComponent().getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    createdAtDateChooser.getCalendarButton().setBorder(BorderFactory.createCompoundBorder(createdAtDateChooser.getCalendarButton().getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    createdAtDateChooser.setPreferredSize(new Dimension(200, 35));
    createdAtDateChooser.setDate(actionContext.equals("edit") ? Helper.convertLocalDateToDate(editingModule.getCreatedAt()) : new Date());
    createdAtDateChooser.setEnabled(false);
    createdAtErrorLabel = new JLabel("\s");
    createdAtErrorLabel.setForeground(App.red600);
    createdAtFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    createdAtFieldGroup.setBackground(App.slate100);
    createdAtFieldGroup.add(createdAtLabel);
    createdAtFieldGroup.add(createdAtDateChooser);
    createdAtFieldGroup.add(createdAtErrorLabel);

    academicLeaderList = new ArrayList<>();
    for (User user : User.getListOfUsersByMatchingValues("role", Role.ACADEMIC_LEADER.getValue())) {
      if (user instanceof AcademicLeader academicLeaderUser) {
        academicLeaderList.add(new ComboBoxItem(academicLeaderUser.getID(), academicLeaderUser.getFirstName() + " " + academicLeaderUser.getLastName() + " - " + academicLeaderUser.getFaculty()));
      }
    }

    academicLeaderLabel = new JLabel();
    academicLeaderLabel.setText("Coordinated By: ");
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
    String selectedAcademicLeaderID = actionContext.equals("edit") ? editingModule.getLeader().getID() : "none";
    if (state.getCurrUser() instanceof AcademicLeader && selectedAcademicLeaderID.equals("none")) {
      selectedAcademicLeaderID = state.getCurrUser().getID();
    }
    for (int index = 0; index < academicLeaderComboBox.getItemCount(); index++) {
      ComboBoxItem item = academicLeaderComboBox.getItemAt(index);
      if (item.getValue().equals(selectedAcademicLeaderID)) {
        academicLeaderComboBox.setSelectedIndex(index);
        break;
      }
    }
    if ((state.getCurrUser() instanceof Admin) == false) {
      academicLeaderComboBox.setEnabled(false);
    }
    academicLeaderErrorLabel = new JLabel("\s");
    academicLeaderErrorLabel.setForeground(App.red600);
    academicLeaderFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    academicLeaderFieldGroup.setBackground(App.slate100);
    academicLeaderFieldGroup.add(academicLeaderLabel);
    academicLeaderFieldGroup.add(academicLeaderComboBox);
    academicLeaderFieldGroup.add(academicLeaderErrorLabel);

    lecturerList = new ArrayList<>();
    for (User user : User.getListOfUsersByMatchingValues("role", Role.LECTURER.getValue())) {
      if (user instanceof Lecturer lecturerUser) {
        lecturerList.add(new ComboBoxItem(lecturerUser.getID(), lecturerUser.getFirstName() + " " + lecturerUser.getLastName() + " - " + (lecturerUser.getAcademicLeader() != null ? lecturerUser.getAcademicLeader().getFaculty() : "None")));
      }
    }

    lecturerLabel = new JLabel();
    lecturerLabel.setText("Instructed By: ");
    lecturerComboBox = new JComboBox<>(lecturerList.stream().toArray(ComboBoxItem[]::new));
    lecturerComboBox.setBackground(App.slate200);
    lecturerComboBox.setBorder(BorderFactory.createCompoundBorder(lecturerComboBox.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    lecturerComboBox.setPreferredSize(new Dimension(200, 35));
    lecturerComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
      JLabel label = new JLabel();
      if (value != null) {
        label.setText(value.getLabelText());
      }
      return label;
    });
    String selectedLecturerID = actionContext.equals("edit") ? editingModule.getInstructor().getID() : "none";
    for (int index = 0; index < lecturerComboBox.getItemCount(); index++) {
      ComboBoxItem item = lecturerComboBox.getItemAt(index);
      if (item.getValue().equals(selectedLecturerID)) {
        lecturerComboBox.setSelectedIndex(index);
        break;
      }
    }
    if (List.of(Role.LECTURER.getValue(), Role.STUDENT.getValue()).contains(state.getCurrUser().getRole().getValue())) {
      lecturerComboBox.setEnabled(false);
    }
    lecturerErrorLabel = new JLabel("\s");
    lecturerErrorLabel.setForeground(App.red600);
    lecturerFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    lecturerFieldGroup.setBackground(App.slate100);
    lecturerFieldGroup.add(lecturerLabel);
    lecturerFieldGroup.add(lecturerComboBox);
    lecturerFieldGroup.add(lecturerErrorLabel);

    codeTitleRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    codeTitleRow.setBackground(App.slate100);
    codeTitleRow.add(codeFieldGroup);
    codeTitleRow.add(titleFieldGroup);

    descriptionRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    descriptionRow.setBackground(App.slate100);
    descriptionRow.add(descriptionFieldGroup);

    creditHoursCreatedAtRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    creditHoursCreatedAtRow.setBackground(App.slate100);
    creditHoursCreatedAtRow.add(creditHoursFieldGroup, "width 50%");
    creditHoursCreatedAtRow.add(createdAtFieldGroup, "width 50%");

    academicLeaderLecturerRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    academicLeaderLecturerRow.setBackground(App.slate100);
    academicLeaderLecturerRow.add(academicLeaderFieldGroup, "width 50%");
    academicLeaderLecturerRow.add(lecturerFieldGroup, "width 50%");

    textFields = Map.ofEntries(
      Map.entry("code", codeField),
      Map.entry("title", titleField),
      Map.entry("creditHours", creditHoursField)
    );

    textAreas = Map.ofEntries(
      Map.entry("description", descriptionTextArea)
    );

    dateChoosers = Map.ofEntries(
      Map.entry("createdAt", createdAtDateChooser)
    );

    comboBoxes = Map.ofEntries(
      Map.entry("leaderID", academicLeaderComboBox),
      Map.entry("instructorID", lecturerComboBox)
    );

    submitBtn = new JButton();
    submitBtn.setText("Submit");
    submitBtn.setForeground(Color.WHITE);
    submitBtn.setBackground(App.green600);
    submitBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    submitBtn.setBorder(BorderFactory.createCompoundBorder(submitBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    submitBtn.setFocusable(false);
    submitBtn.addActionListener(e -> {
      if (actionContext.equals("edit") && state.getCurrUser() instanceof AcademicLeader && !state.getCurrUser().getID().equals(editingModule.getLeader().getID())) {
        String messageDialogTitle = "Cannot edit Module: " + editingModule.getCode(); 
        JOptionPane.showMessageDialog(router, "You are not allowed to modify this module! \nThis module is not under your supervision!", "Error: " + messageDialogTitle + "!", JOptionPane.ERROR_MESSAGE);
        return;
      }

      HashMap<String, String> inputValues = new HashMap<>();
      inputValues.put("id", actionContext.equals("edit") ? editingModule.getID() : null);
      for (String key : textFields.keySet()) {
        inputValues.put(key, textFields.get(key).getText().trim());
      }

      for (String key : textAreas.keySet()) {
        if (key.equals("description")) {
          String desc = textAreas.get(key).getText().trim().isEmpty() ? "No Description" : textAreas.get(key).getText().trim();
          inputValues.put(key, desc);
        } else {
          inputValues.put(key, textAreas.get(key).getText().trim());
        }
      }

      for (String key : dateChoosers.keySet()) {
        if (dateChoosers.get(key).getDate() == null) {
          inputValues.put(key, "");
        } else {
          inputValues.put(key, Helper.simpleFormatter.format(dateChoosers.get(key).getDate()));
        }
      }

      for (String key : comboBoxes.keySet()) {
        ComboBoxItem selectedComboBoxItem = (ComboBoxItem) comboBoxes.get(key).getSelectedItem();
        inputValues.put(key, selectedComboBoxItem.getValue());
      }

      Validation validation = Module.validate(inputValues);
      if (validation.getSuccess()) {
        Module module = new Module(inputValues);

        module.update();
        String moduleInfoDisplay = "\nModule: '" + module.getCode() + "'" + "\nTitle: " + module.getTitle();
        String messageDialogContent = actionContext.equals("edit") ? "Current Module has been updated!" + moduleInfoDisplay : "New Module has been created!" + moduleInfoDisplay;
        String messageDialogTitle = actionContext.equals("edit") ? "Success: Updated Module Module" : "Success: Created New Module";
        JOptionPane.showMessageDialog(router, messageDialogContent, messageDialogTitle, JOptionPane.INFORMATION_MESSAGE);
        router.showView(Pages.MANAGEMODULES, state);
      } else {
        displayError(validation);
        JOptionPane.showMessageDialog(router, "");
        String messageDialogTitle = actionContext.equals("edit") ? "Cannot edit Module: " + editingModule.getCode() : "Cannot create new Module"; 
        JOptionPane.showMessageDialog(router, validation.getMessage(), "Error: Invalid Form input! " + messageDialogTitle, JOptionPane.ERROR_MESSAGE);
      }
    });

    deleteBtn = new JButton();
    deleteBtn.setText("Delete Module");
    deleteBtn.setForeground(Color.WHITE);
    deleteBtn.setBackground(App.red600);
    deleteBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    deleteBtn.setBorder(BorderFactory.createCompoundBorder(deleteBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    deleteBtn.setFocusable(false);
    deleteBtn.addActionListener(e -> {
      if (actionContext.equals("edit")) {
        String moduleInfoDisplay = "\nModule Code: " + editingModule.getCode() + "\nTitle: " + editingModule.getTitle() + "\nSupervised By: " + editingModule.getLeader().getFirstName() + " " + editingModule.getLeader().getLastName();
        int choice = JOptionPane.showConfirmDialog(router, "Are you sure you want to delete this module?" + moduleInfoDisplay, "Delete This Module Confirmation", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
          editingModule.delete();
          router.showView(Pages.MANAGEMODULES, state);
          JOptionPane.showMessageDialog(router, "This Module has been deleted successfully" + moduleInfoDisplay, "Module has been Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
      }
    });

    actionButtonGroup = new JPanel(new MigLayout("insets 50 0, aligny center"));
    actionButtonGroup.setBackground(App.slate100);
    if (actionContext.equals("edit") && List.of(Role.ADMIN.getValue(), Role.ACADEMIC_LEADER.getValue()).contains(state.getCurrUser().getRole().getValue())) {
      actionButtonGroup.add(deleteBtn);
    }
    if (List.of(Role.ADMIN.getValue(), Role.ACADEMIC_LEADER.getValue()).contains(state.getCurrUser().getRole().getValue())) {
      actionButtonGroup.add(submitBtn, "push, alignx right");
    }

    mainform = new JPanel(new MigLayout("insets 30 0, wrap 1, gapy 10"));
    mainform.setBackground(App.slate100);
    mainform.add(codeTitleRow, "width 100%");
    mainform.add(descriptionRow, "width 100%");
    mainform.add(creditHoursCreatedAtRow, "width 100%");
    mainform.add(academicLeaderLecturerRow, "width 100%");
    mainform.add(actionButtonGroup, "width 100%");

    searchInput = state.getStudentSearch() != null ? state.getStudentSearch() : "";
    studentModuleStatusConditions = state.getStudentModuleStatusConditions() != null ?
      state.getStudentModuleStatusConditions() : 
      new HashSet<>(Arrays.asList(ModuleStatus.ACTIVE.getValue()));

    searchField = new TextField("Search Students...");
    if (!searchInput.isEmpty()) {
      searchField.setText(searchInput);
    }
    searchField.setBackground(App.slate200);
    searchField.setBorder(BorderFactory.createCompoundBorder(searchField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    searchField.setPreferredSize(new Dimension(250, 35));

    searchClearBtn = new JButton();
    searchClearBtn.setText("Clear");
    searchClearBtn.setIcon(Helper.iconResizer(new ImageIcon("assets/cancel-icon.png"), 18, 18));
    searchClearBtn.setForeground(Color.WHITE);
    searchClearBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    searchClearBtn.setBackground(App.red600);
    searchClearBtn.setFocusable(false);
    searchClearBtn.setBorder(BorderFactory.createCompoundBorder(searchClearBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    searchClearBtn.addActionListener(e -> {
      state.clearState();
      state.setSelectedModuleID(actionContext.equals("edit") ? editingModule.getID() : null);
      state.setModulePageCurrTab("student");
      router.showView(Pages.MODULE, state);
    });

    searchBtn = new JButton();
    searchBtn.setText("Search");
    searchBtn.setIcon(Helper.iconResizer(new ImageIcon("assets/search-icon.png"), 18, 18));
    searchBtn.setForeground(Color.WHITE);
    searchBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    searchBtn.setBackground(App.blue600);
    searchBtn.setFocusable(false);
    searchBtn.setBorder(BorderFactory.createCompoundBorder(searchBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));

    searchSection = new JPanel(new MigLayout("insets 0, gapx 5, aligny center"));
    searchSection.setBackground(App.slate100);
    searchSection.add(searchField);
    searchSection.add(searchClearBtn);
    searchSection.add(searchBtn);

    filterUnregisteredCheckButton = new JCheckBox();
    filterUnregisteredCheckButton.setText("Unregistered");
    filterUnregisteredCheckButton.setSelected(studentModuleStatusConditions.contains("unregistered"));

    filterActiveCheckButton = new JCheckBox();
    filterActiveCheckButton.setText(ModuleStatus.ACTIVE.getDisplay());
    filterActiveCheckButton.setSelected(studentModuleStatusConditions.contains(ModuleStatus.ACTIVE.getValue()));

    filterCompletedCheckButton = new JCheckBox();
    filterCompletedCheckButton.setText(ModuleStatus.COMPLETED.getDisplay());
    filterCompletedCheckButton.setSelected(studentModuleStatusConditions.contains(ModuleStatus.COMPLETED.getValue()));

    filterSuspendedCheckButton = new JCheckBox();
    filterSuspendedCheckButton.setText(ModuleStatus.SUSPENDED.getDisplay());
    filterSuspendedCheckButton.setSelected(studentModuleStatusConditions.contains(ModuleStatus.SUSPENDED.getValue()));

    filterDroppedCheckButton = new JCheckBox();
    filterDroppedCheckButton.setText(ModuleStatus.DROPPED.getDisplay());
    filterDroppedCheckButton.setSelected(studentModuleStatusConditions.contains(ModuleStatus.DROPPED.getValue()));

    Map<String, JCheckBox> filterCheckBoxes = Map.ofEntries(
      Map.entry("unregistered", filterUnregisteredCheckButton),
      Map.entry(ModuleStatus.ACTIVE.getValue(), filterActiveCheckButton),
      Map.entry(ModuleStatus.COMPLETED.getValue(), filterCompletedCheckButton),
      Map.entry(ModuleStatus.SUSPENDED.getValue(), filterSuspendedCheckButton),
      Map.entry(ModuleStatus.DROPPED.getValue(), filterDroppedCheckButton)
    );

    for (Map.Entry<String, JCheckBox> entry : filterCheckBoxes.entrySet()) {
      String moduleStatus = entry.getKey();
      JCheckBox checkBox = entry.getValue();
      
      checkBox.addActionListener(e -> {
        if (checkBox.isSelected()) {
          studentModuleStatusConditions.add(moduleStatus);
        } else {
          studentModuleStatusConditions.remove(moduleStatus);
        }

        state.setStudentSearch(searchField.getText());
        state.setStudentModuleStatusConditions(studentModuleStatusConditions);
        state.setSelectedModuleID(actionContext.equals("edit") ? editingModule.getID() : null);
        state.setModulePageCurrTab("student");
        router.showView(Pages.MODULE, state);
      });
    }

    searchBtn.addActionListener(e -> {
      state.setStudentSearch(searchField.getText());

      studentModuleStatusConditions.clear();
      for (String key : filterCheckBoxes.keySet()) {
        if (filterCheckBoxes.get(key).isSelected()) {
          studentModuleStatusConditions.add(key);
        }
      }

      state.setStudentModuleStatusConditions(studentModuleStatusConditions);
      state.setSelectedModuleID(actionContext.equals("edit") ? editingModule.getID() : null);
      state.setModulePageCurrTab("student");
      router.showView(Pages.MODULE, state);
    });

    filterOptionsContainer = new JPanel(new MigLayout("insets 0, gapx 5"));
    filterOptionsContainer.setBackground(App.slate100);
    filterOptionsContainer.add(filterUnregisteredCheckButton);
    filterOptionsContainer.add(filterActiveCheckButton);
    filterOptionsContainer.add(filterCompletedCheckButton);
    filterOptionsContainer.add(filterSuspendedCheckButton);
    filterOptionsContainer.add(filterDroppedCheckButton);

    searchFilterGroup = new JPanel(new MigLayout("insets 0, wrap 1"));
    searchFilterGroup.setBackground(App.slate100);
    searchFilterGroup.add(searchSection);
    searchFilterGroup.add(filterOptionsContainer);

    moduleStatusOptions = new ArrayList<>();
    for (ModuleStatus moduleStatus : ModuleStatus.values()) {
      moduleStatusOptions.add(new ComboBoxItem(moduleStatus.getValue(), moduleStatus.getDisplay()));
    }

    selectedStudentDisplay = new JLabel();
    selectedStudentDisplay.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    selectedStudentDisplay.setText("Current Selected Student: None");

    moduleStatusComboBox = new JComboBox<>(moduleStatusOptions.stream().toArray(ComboBoxItem[]::new));
    moduleStatusComboBox.setBackground(App.slate200);
    moduleStatusComboBox.setBorder(BorderFactory.createCompoundBorder(moduleStatusComboBox.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    moduleStatusComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
      JLabel label = new JLabel();
      if (value != null) {
        label.setText(value.getLabelText());
      }
      return label;
    });

    updateBtn = new JButton();
    updateBtn.setText("Update Enrollment Status");
    updateBtn.setForeground(Color.WHITE);
    updateBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    updateBtn.setBackground(App.green600);
    updateBtn.setFocusable(false);
    updateBtn.setBorder(BorderFactory.createCompoundBorder(updateBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));

    moduleStatusRow = new JPanel(new MigLayout("insets 0, gapx 5"));
    moduleStatusRow.setBackground(App.slate100);
    moduleStatusRow.add(moduleStatusComboBox);
    moduleStatusRow.add(updateBtn);

    studentTabActionGroup = new JPanel(new MigLayout("insets 0, wrap 1, gapy 5"));
    studentTabActionGroup.setBackground(App.slate100);
    studentTabActionGroup.add(selectedStudentDisplay);
    studentTabActionGroup.add(moduleStatusRow);

    searchFilterActionRow = new JPanel(new MigLayout("insets 5 10"));
    searchFilterActionRow.setBackground(App.slate100);
    searchFilterActionRow.add(searchFilterGroup);
    searchFilterActionRow.add(studentTabActionGroup, "push, align right");

    String moduleID;
    if (actionContext.equals("edit")) {
      List<User> studentUsers = User.fetchUsers(searchInput, Set.of(Role.STUDENT.getValue()));
      students = studentUsers.stream()
        .map(user -> user instanceof Student student ? student : null)
        .filter(student -> {
          if (student == null) return false;
    
          StudentModule enrollment = StudentModule.getStudentModuleByCompositeKey(student.getID(), editingModule.getID());
          
          if (enrollment == null) {
            return studentModuleStatusConditions.contains("unregistered");
          } else {
            return studentModuleStatusConditions.contains(enrollment.getStatus().getValue());
          }
        })
        .collect(Collectors.toList());
        moduleID = editingModule.getID();
    } else {
      students = new ArrayList<>();
      studentModules = new ArrayList<>();
      moduleID = "0";
    }

    smTableModel = new StudentModuleTableModel(students, moduleID);
    studentCount = new JLabel();
    studentCount.setText("Total Student Queried: " + smTableModel.getRowCount());
    studentCount.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

    studentModuleTable = new JTable(smTableModel);
    studentModuleTable.setPreferredScrollableViewportSize(new Dimension(studentModuleTable.getWidth(), 800));
    JScrollPane scrollPane = new JScrollPane(studentModuleTable);
    scrollPane.setBackground(App.slate100);

    studentModuleTable.setFillsViewportHeight(true);
    studentModuleTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    studentModuleTable.setRowHeight(40);

    JTableHeader tableHeader = studentModuleTable.getTableHeader();
    tableHeader.setBackground(new Color(51, 65, 85));
    tableHeader.setForeground(Color.WHITE);
    tableHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
    tableHeader.setPreferredSize(new Dimension(tableHeader.getPreferredSize().width, 45));

    studentModuleTable.setBackground(Color.WHITE);
    studentModuleTable.setForeground(Color.BLACK);
    studentModuleTable.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
    studentModuleTable.setGridColor(new Color(226, 232, 240));
    studentModuleTable.setShowGrid(true);
    studentModuleTable.setIntercellSpacing(new Dimension(1, 1));

    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value,
              boolean isSelected, boolean hasFocus, int row, int column) {
          Component c = super.getTableCellRendererComponent(table, value, 
              isSelected, hasFocus, row, column);
          
          if (c instanceof JLabel) {
              JLabel label = (JLabel) c;
              label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
          }

          if (!isSelected) {
              if (row % 2 == 0) {
                  c.setBackground(Color.WHITE);
              } else {
                  c.setBackground(new Color(248, 250, 252));
              }
          }
          
          return c;
      }
    };

    for (int i = 0; i < studentModuleTable.getColumnCount(); i++) {
      studentModuleTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
    }

    if (actionContext.equals("edit")) {
      studentModuleTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
          if (e.getValueIsAdjusting()) {
          return;
        }

          if (studentModuleTable.getSelectedRow() > -1) {
            String currStudentID = (String) studentModuleTable.getValueAt(studentModuleTable.getSelectedRow(), 0);

            User selectedUser = User.getUserByMatchingValues("id", currStudentID);
            if (selectedUser instanceof Student selectedStudent) {
              selectedStudentDisplay.setText("Current Selected Student: " + selectedStudent.getFirstName() + " " + selectedStudent.getLastName());
              StudentModule currStudentSM = StudentModule.getStudentModuleByCompositeKey(currStudentID, moduleID);
              if (currStudentSM != null) {
                for (int index = 0; index < moduleStatusComboBox.getItemCount(); index++) {
                  ComboBoxItem item = moduleStatusComboBox.getItemAt(index);
                  if (item.getValue().equals(currStudentSM.getStatus().getValue())) {
                    moduleStatusComboBox.setSelectedIndex(index);
                  }
                }
                moduleStatusComboBox.removeAllItems();
                for (ModuleStatus moduleStatus : ModuleStatus.values()) {
                  moduleStatusComboBox.addItem(new ComboBoxItem(moduleStatus.getValue(), moduleStatus.getDisplay()));
                }
              } else {
                moduleStatusComboBox.removeAllItems();
                List<String> excludeModuleStatus = List.of(ModuleStatus.COMPLETED.getValue(), ModuleStatus.SUSPENDED.getValue(), ModuleStatus.DROPPED.getValue());
                for (ModuleStatus moduleStatus : ModuleStatus.values()) {
                  if (excludeModuleStatus.contains(moduleStatus.getValue())) {
                    continue;
                  }
                  moduleStatusComboBox.addItem(new ComboBoxItem(moduleStatus.getValue(), moduleStatus.getDisplay()));
                }
              }
            }
          }
        }
      });

      updateBtn.addActionListener(e -> {
        if (studentModuleTable.getSelectedRow() > -1) {
          String currStudentID = (String) studentModuleTable.getValueAt(studentModuleTable.getSelectedRow(), 0);
          StudentModule currStudentSM = StudentModule.getStudentModuleByCompositeKey(currStudentID, moduleID);
          ComboBoxItem currSelectedStatus = (ComboBoxItem) moduleStatusComboBox.getSelectedItem();

          if (currStudentSM != null) {
            currStudentSM.setStatus(ModuleStatus.fromValue(currSelectedStatus.getValue()));
          } else {
            HashMap<String, String> inputValues = new HashMap<>();
            inputValues.put("student", currStudentID);
            inputValues.put("module", moduleID);
            inputValues.put("status", currSelectedStatus.getValue());
            inputValues.put("enrolledAt", LocalDate.now().format(Helper.dateTimeFormatter));
            inputValues.put("points", "0");

            currStudentSM = new StudentModule(inputValues);
            currStudentSM.update();
          }

          state.setStudentSearch(searchField.getText());
          state.setStudentModuleStatusConditions(studentModuleStatusConditions);
          state.setSelectedModuleID(actionContext.equals("edit") ? editingModule.getID() : null);
          state.setModulePageCurrTab("student");
          router.showView(Pages.MODULE, state);
        } else {
          JOptionPane.showMessageDialog(router, "No User Selected!", "Error: Unable to update student's module status", JOptionPane.ERROR_MESSAGE);
        }
      });
    }

    studentTab = new JPanel(new MigLayout("insets 30 0, wrap 1, gapy 10"));
    studentTab.setBackground(App.slate100);
    studentTab.add(searchFilterActionRow, "growx, width 100%");
    studentTab.add(studentCount);
    studentTab.add(scrollPane, "growx, width 100%");

    formTabbedPane = new JTabbedPane();
    formTabbedPane.addTab("Module Information", mainform);
    formTabbedPane.addTab("Student Enrollment", studentTab);

    if (state.getModulePageCurrTab() != null && state.getModulePageCurrTab().equals("student")) {
      formTabbedPane.setSelectedIndex(1);
    }

    contentBody.add(formTitle);
    contentBody.add(formTabbedPane, "width 100%");

    this.add(header, "span, growx, wrap");
    this.add(nav, "growy");
    this.add(contentBody, "span, grow");

    state.clearState();
  }

  private void displayError(Validation validation) {
    if (textFields.get(validation.getField()) != null) {
      textFields.get(validation.getField()).setBackground(App.red100);
    } else if (comboBoxes.get(validation.getField()) != null) {
      comboBoxes.get(validation.getField()).setBackground(App.red100);
    } else if (dateChoosers.get(validation.getField()) != null) {
      dateChoosers.get(validation.getField()).setBackground(App.red100);
    } else if (textAreas.get(validation.getField()) != null) {
      textAreas.get(validation.getField()).setBackground(App.red100);
    }

    if (errorLabels.get(validation.getField()) != null) {
      errorLabels.get(validation.getField()).setText(validation.getMessage());
    }
  }
}
