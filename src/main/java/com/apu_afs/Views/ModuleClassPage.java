package com.apu_afs.Views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.apu_afs.GlobalState;
import com.apu_afs.Helper;
import com.apu_afs.Models.ComboBoxItem;
import com.apu_afs.Models.Lecturer;
import com.apu_afs.Models.ModuleClass;
import com.apu_afs.Models.ModuleLecturer;
import com.apu_afs.Models.User;
import com.apu_afs.Models.Validation;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Models.Module;
import com.apu_afs.Views.components.HeaderPanel;
import com.apu_afs.Views.components.NavPanel;
import com.apu_afs.Views.components.TextField;

import net.miginfocom.swing.MigLayout;

public class ModuleClassPage extends JPanel {
  
  HeaderPanel header;
  NavPanel nav;
  
  JPanel contentBody;

  JLabel formTitle;

  JTabbedPane formTabbedPane;
  JPanel mainform;

  ArrayList<ComboBoxItem> dayOptions;

  JPanel classCodeDayRow;
  JPanel classCodeFieldGroup;
  JLabel classCodeLabel;
  TextField classCodeField;
  JLabel classCodeErrorLabel;
  JPanel dayFieldGroup;
  JLabel dayLabel;
  JComboBox<ComboBoxItem> dayComboBox;
  JLabel dayErrorLabel;

  JPanel startEndTimeRow;
  JPanel startTimeFieldGroup;
  JLabel startTimeLabel;
  TextField startTimeField;
  JLabel startTimeErrorLabel;
  JPanel endTimeFieldGroup;
  JLabel endTimeLabel;
  TextField endTimeField;
  JLabel endTimeErrorLabel;

  JPanel classroomRow;
  JPanel classroomFieldGroup;
  JLabel classroomLabel;
  TextField classroomField;
  JLabel classroomErrorLabel;

  ArrayList<ComboBoxItem> moduleOptions;

  JPanel moduleLecturerRow;
  JPanel moduleFieldGroup;
  JLabel moduleLabel;
  JComboBox<ComboBoxItem> moduleComboBox;
  JLabel moduleErrorLabel;
  JPanel lecturerFieldGroup;
  JLabel lecturerLabel;
  JComboBox<ComboBoxItem> lecturerComboBox;
  JLabel lecturerErrorLabel;

  JPanel actionButtonGroup;
  JButton submitBtn;
  JButton deleteBtn;

  String actionContext;
  ModuleClass editingModuleClass;

  Map<String, TextField> textFields;
  Map<String, JComboBox<ComboBoxItem>> comboBoxes;
  Map<String, JSpinner> timeSpinners;
  Map<String, JLabel> errorLabels;

  private static final String[] allowedRoles = {"admin"};

  public ModuleClassPage(Router router, GlobalState state) {
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
    } else if (!List.of(allowedRoles).contains(state.getCurrUser().getRole().getValue())) {
        SwingUtilities.invokeLater(() -> {
            router.showView(Pages.DASHBOARD, state);
        });
    }

    if (state.getSelectedModuleClassID() == null) {
      actionContext = "add";
      editingModuleClass = null;
    } else {
      actionContext = "edit";
      editingModuleClass = ModuleClass.getModuleClassbyMatchingValues("id", state.getSelectedModuleClassID());
    }

    header = new HeaderPanel(router, state);
    nav = new NavPanel(router, state);

    contentBody = new JPanel(new MigLayout("insets 20 20, wrap 1, gapy 10"));
    contentBody.setBackground(App.slate100);

    formTitle = new JLabel();
    if (actionContext.equals("edit")) {
      formTitle.setText("Editing Class ID: " + editingModuleClass.getId());
    } else {
      formTitle.setText("Create New Class Form");
    }
    formTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

    classCodeLabel = new JLabel();
    classCodeLabel.setText("Class Code: ");
    classCodeField = new TextField("Enter Class Code Here...");
    classCodeField.setBackground(App.slate200);
    classCodeField.setBorder(BorderFactory.createCompoundBorder(classCodeField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    classCodeField.setPreferredSize(new Dimension(600, 35));
    if (actionContext.equals("edit")) {
      classCodeField.setText(editingModuleClass.getClassCode());
    }
    classCodeErrorLabel = new JLabel("\s");
    classCodeErrorLabel.setForeground(App.red600);
    classCodeFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    classCodeFieldGroup.setBackground(App.slate100);
    classCodeFieldGroup.add(classCodeLabel);
    classCodeFieldGroup.add(classCodeField);
    classCodeFieldGroup.add(classCodeErrorLabel);

    dayOptions = new ArrayList<>();
    for (String day : getDayOptions()) {
      dayOptions.add(new ComboBoxItem(day, Helper.firstLetterUpperCase(day)));
    }

    dayLabel = new JLabel();
    dayLabel.setText("Day of the Week: ");
    dayComboBox = new JComboBox<>(dayOptions.stream().toArray(ComboBoxItem[]::new));
    dayComboBox.setBackground(App.slate200);
    dayComboBox.setBorder(BorderFactory.createCompoundBorder(dayComboBox.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    dayComboBox.setPreferredSize(new Dimension(200, 35));
    dayComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
      JLabel label = new JLabel();
      if (value != null) {
        label.setText(value.getLabelText());
      }
      return label;
    });
    if (actionContext.equals("edit")) {
      for (int i = 0; i < dayComboBox.getItemCount(); i++) {
        ComboBoxItem item = dayComboBox.getItemAt(i);
        if (item.getValue().equals(editingModuleClass.getDay())) {
          dayComboBox.setSelectedIndex(i);
          break;
        }
      }
    }  
    dayErrorLabel = new JLabel("\s");
    dayErrorLabel.setForeground(App.red600);
    dayFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    dayFieldGroup.setBackground(App.slate100);
    dayFieldGroup.add(dayLabel);
    dayFieldGroup.add(dayComboBox);
    dayFieldGroup.add(dayErrorLabel);

    startTimeLabel = new JLabel();
    startTimeLabel.setText("Class Starts At use (HH:mm) in 24 hours example '13:30'");
    startTimeField = new TextField("Enter Start Time Here...");
    startTimeField.setBackground(App.slate200);
    startTimeField.setBorder(BorderFactory.createCompoundBorder(startTimeField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    startTimeField.setPreferredSize(new Dimension(600, 35));
    if (actionContext.equals("edit")) {
      startTimeField.setText(editingModuleClass.getStartTime().format(Helper.timeFormatter));
    }
    startTimeErrorLabel = new JLabel("\s");
    startTimeErrorLabel.setForeground(App.red600);
    startTimeFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    startTimeFieldGroup.setBackground(App.slate100);
    startTimeFieldGroup.add(startTimeLabel);
    startTimeFieldGroup.add(startTimeField);
    startTimeFieldGroup.add(startTimeErrorLabel);

    endTimeLabel = new JLabel();
    endTimeLabel.setText("Class Ends At use (HH:mm) in 24 hours example '18:15'");
    endTimeField = new TextField("Enter Start Time Here...");
    endTimeField.setBackground(App.slate200);
    endTimeField.setBorder(BorderFactory.createCompoundBorder(endTimeField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    endTimeField.setPreferredSize(new Dimension(600, 35));
    if (actionContext.equals("edit")) {
      endTimeField.setText(editingModuleClass.getEndTime().format(Helper.timeFormatter));
    }
    endTimeErrorLabel = new JLabel("\s");
    endTimeErrorLabel.setForeground(App.red600);
    endTimeFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    endTimeFieldGroup.setBackground(App.slate100);
    endTimeFieldGroup.add(endTimeLabel);
    endTimeFieldGroup.add(endTimeField);
    endTimeFieldGroup.add(endTimeErrorLabel);

    classroomLabel = new JLabel();
    classroomLabel.setText("Classroom: ");
    classroomField = new TextField("Enter Classroom Here...");
    classroomField.setBackground(App.slate200);
    classroomField.setBorder(BorderFactory.createCompoundBorder(classroomField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    classroomField.setPreferredSize(new Dimension(600, 35));
    if (actionContext.equals("edit")) {
      classroomField.setText(editingModuleClass.getClassCode());
    }
    classroomErrorLabel = new JLabel("\s");
    classroomErrorLabel.setForeground(App.red600);
    classroomFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    classroomFieldGroup.setBackground(App.slate100);
    classroomFieldGroup.add(classroomLabel);
    classroomFieldGroup.add(classroomField);
    classroomFieldGroup.add(classroomErrorLabel);

    moduleOptions = new ArrayList<>();
    for (Module module : Module.fetchAllModules()) {
      moduleOptions.add(new ComboBoxItem(module.getID(), module.getCode() + " - " + module.getTitle()));
    }

    moduleLabel = new JLabel();
    moduleLabel.setText("Module: ");
    moduleComboBox = new JComboBox<>(moduleOptions.stream().toArray(ComboBoxItem[]::new));
    moduleComboBox.setBackground(App.slate200);
    moduleComboBox.setBorder(BorderFactory.createCompoundBorder(moduleComboBox.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    moduleComboBox.setPreferredSize(new Dimension(200, 35));
    moduleComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
      JLabel label = new JLabel();
      if (value != null) {
        label.setText(value.getLabelText());
      }
      return label;
    });
    if (actionContext.equals("edit")) {
      for (int i = 0; i < moduleComboBox.getItemCount(); i++) {
        ComboBoxItem item = moduleComboBox.getItemAt(i);
        if (item.getValue().equals(editingModuleClass.getDay())) {
          moduleComboBox.setSelectedIndex(i);
          break;
        }
      }
    }
    moduleErrorLabel = new JLabel("\s");
    moduleErrorLabel.setForeground(App.red600);
    moduleFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    moduleFieldGroup.setBackground(App.slate100);
    moduleFieldGroup.add(moduleLabel);
    moduleFieldGroup.add(moduleComboBox);
    moduleFieldGroup.add(moduleErrorLabel);

    lecturerLabel = new JLabel();
    lecturerLabel.setText("Lecturer: ");
    lecturerComboBox = new JComboBox<>();
    ComboBoxItem currSelectedModule = (ComboBoxItem) moduleComboBox.getSelectedItem();
    this.setLecturerOptions(currSelectedModule.getValue());
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
    if (actionContext.equals("edit")) {
      for (int i = 0; i < lecturerComboBox.getItemCount(); i++) {
        ComboBoxItem item = lecturerComboBox.getItemAt(i);
        if (item.getValue().equals(editingModuleClass.getDay())) {
          lecturerComboBox.setSelectedIndex(i);
          break;
        }
      }
    }
    lecturerErrorLabel = new JLabel("\s");
    lecturerErrorLabel.setForeground(App.red600);
    lecturerFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    lecturerFieldGroup.setBackground(App.slate100);
    lecturerFieldGroup.add(lecturerLabel);
    lecturerFieldGroup.add(lecturerComboBox);
    lecturerFieldGroup.add(lecturerErrorLabel);

    classCodeDayRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    classCodeDayRow.setBackground(App.slate100);
    classCodeDayRow.add(classCodeFieldGroup, "width 50%");
    classCodeDayRow.add(dayFieldGroup, "width 50%");

    startEndTimeRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    startEndTimeRow.setBackground(App.slate100);
    startEndTimeRow.add(startTimeFieldGroup);
    startEndTimeRow.add(endTimeFieldGroup);

    classroomRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    classroomRow.setBackground(App.slate100);
    classroomRow.add(classroomFieldGroup);

    moduleLecturerRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 100"));
    moduleLecturerRow.setBackground(App.slate100);
    moduleLecturerRow.add(moduleFieldGroup, "width 50%");
    moduleLecturerRow.add(lecturerFieldGroup, "width 50%");

    mainform = new JPanel(new MigLayout("insets 30 0, wrap 1, gapy 10"));
    mainform.setBackground(App.slate100);
    mainform.add(classCodeDayRow, "width 100%");
    mainform.add(startEndTimeRow, "width 100%");
    mainform.add(classroomRow, "width 100%");
    mainform.add(moduleLecturerRow, "width 100%");

    formTabbedPane = new JTabbedPane();
    formTabbedPane.addTab("Class Information", mainform);

    textFields = Map.ofEntries(
      Map.entry("classCode", classCodeField),
      Map.entry("classroom", classroomField),
      Map.entry("startTime", startTimeField),
      Map.entry("endTime", endTimeField)
    );

    comboBoxes = Map.ofEntries(
      Map.entry("day", dayComboBox),
      Map.entry("moduleID", moduleComboBox),
      Map.entry("lecturerID", lecturerComboBox)
    );

    errorLabels = Map.ofEntries(
      Map.entry("classCode", classCodeErrorLabel),
      Map.entry("classroom", classCodeErrorLabel),
      Map.entry("startTime", startTimeErrorLabel),
      Map.entry("endTime", endTimeErrorLabel),
      Map.entry("day", dayErrorLabel),
      Map.entry("moduleID", moduleErrorLabel),
      Map.entry("lecturerID", lecturerErrorLabel)
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
      inputValues.put("id", actionContext.equals("edit") ? editingModuleClass.getId() : null);
      for (String fieldKey : textFields.keySet()) {
        inputValues.put(fieldKey, textFields.get(fieldKey).getText().trim());
      }

      for (String comboBoxKey : comboBoxes.keySet()) {
        ComboBoxItem selectedComboBoxItem = (ComboBoxItem) comboBoxes.get(comboBoxKey).getSelectedItem();
        inputValues.put(comboBoxKey, selectedComboBoxItem.getValue());
      }

      Validation inputValidation = ModuleClass.validateModuleClass(inputValues);
      if (inputValidation.getSuccess()) {
        ModuleClass moduleClass = new ModuleClass(inputValues);
        moduleClass.update();
        state.setSelectedModuleClassID(moduleClass.getId());

        String moduleClassInfoDisplay = "\nClass ID:" + moduleClass.getId() + "\nClass Code: " + moduleClass.getClassCode() + " Day: " + moduleClass.getDay() +  
          "\nStart Time: " + moduleClass.getStartTime().format(Helper.timeFormatter) + " End Time: " + moduleClass.getEndTime().format(Helper.timeFormatter) + "\nClassroom: " + moduleClass.getClassroom();
        String messageDialogContent = actionContext.equals("edit") ? "Current Class has been updated!" + moduleClassInfoDisplay : "New Class has been created!" + moduleClassInfoDisplay;
        String messageDialogTitle = actionContext.equals("edit") ? "Success: Updated Selected Class" : "Success: Created New Class";
        JOptionPane.showMessageDialog(router, messageDialogContent, messageDialogTitle, JOptionPane.INFORMATION_MESSAGE);
        router.showView(Pages.MODULECLASS, state);
      } else {
        this.displayError(router, inputValidation);
      }
    });

    deleteBtn = new JButton();
    deleteBtn.setText("Delete Class Entry");
    deleteBtn.setForeground(Color.WHITE);
    deleteBtn.setBackground(App.red600);
    deleteBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    deleteBtn.setBorder(BorderFactory.createCompoundBorder(deleteBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    deleteBtn.setFocusable(false);
    deleteBtn.addActionListener(e -> {
      if (actionContext.equals("edit")) {
         String moduleClassInfoDisplay = "\nClass ID:" + editingModuleClass.getId() + "\nClass Code: " + editingModuleClass.getClassCode() + " Day: " + editingModuleClass.getDay() +  
          "\nStart Time: " + editingModuleClass.getStartTime().format(Helper.timeFormatter) + " End Time: " + editingModuleClass.getEndTime().format(Helper.timeFormatter) + "\nClassroom: " + editingModuleClass.getClassroom();
        int choice = JOptionPane.showConfirmDialog(router, "Are you sure you want to delete this class?" + moduleClassInfoDisplay, "Delete This Class Confirmation", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
          editingModuleClass.delete();
          router.showView(Pages.MANAGECLASSES, state);
          JOptionPane.showMessageDialog(router, "This Class has been deleted successfully" + moduleClassInfoDisplay, "Class has been Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
      }
    });

    moduleComboBox.addActionListener(e -> {
      ComboBoxItem selectedModule = (ComboBoxItem) moduleComboBox.getSelectedItem();
      this.setLecturerOptions(selectedModule.getValue());
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

  private String[] getDayOptions() {
    return new String[] {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
  }

  private void setLecturerOptions(String moduleID) {
    this.lecturerComboBox.removeAllItems();
    List<ModuleLecturer> moduleLecturers = ModuleLecturer.fetchLecturersByModule(moduleID);

    for (ModuleLecturer moduleLecturer : moduleLecturers) {
      User potentialLecturer = User.getUserByMatchingValues("id", moduleLecturer.getLecturerID());
      if (potentialLecturer instanceof Lecturer lecturer) {
        this.lecturerComboBox.addItem(new ComboBoxItem(lecturer.getID(), lecturer.getFirstName() + " " + lecturer.getLastName()));
      }
    }
  }

  private void displayError(Router router, Validation validation) {
    if (textFields.get(validation.getField()) != null) {
      textFields.get(validation.getField()).setBackground(App.red100);
    } else if (comboBoxes.get(validation.getField()) != null) {
      comboBoxes.get(validation.getField()).setBackground(App.red100);
    }

    if (errorLabels.get(validation.getField()) != null) {
      errorLabels.get(validation.getField()).setText(validation.getMessage());
    }

    String messageDialogTitle = actionContext.equals("edit") ? "Cannot edit Class: " + editingModuleClass.getId() : "Cannot create new Class"; 
    JOptionPane.showMessageDialog(router, validation.getMessage(), "Error: Invalid Form input! " + messageDialogTitle, JOptionPane.ERROR_MESSAGE);
  }
}
