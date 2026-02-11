package com.apu_afs.Views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.apu_afs.GlobalState;
import com.apu_afs.Helper;
import com.apu_afs.Models.Assessment;
import com.apu_afs.Models.AssessmentMark;
import com.apu_afs.Models.Student;
import com.apu_afs.Models.StudentModule;
import com.apu_afs.Models.Validation;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Models.Enums.Role;
import com.apu_afs.TableModels.AssessmentMarkTableModel;
import com.apu_afs.Views.components.HeaderPanel;
import com.apu_afs.Views.components.NavPanel;
import com.apu_afs.Views.components.TextField;

import net.miginfocom.swing.MigLayout;

public class EnterMarksPage extends JPanel {
  HeaderPanel header;
  NavPanel nav;

  JPanel contentBody;

  JPanel searchSection;
  TextField searchField;
  JButton searchClearBtn;
  JButton searchBtn;

  JPanel actionBtnsContainer;
  JButton addBtn;
  JButton editBtn;
  JButton deleteBtn;

  JPanel tableSection;
  JLabel rowCountLabel;
  JTable table;

  List<AssessmentMark> marks;
  AssessmentMarkTableModel markTableModel;

  String searchInput;
  int selectedRow = -1;
 
  private static final String dataContext = "Assessment Marks";

  public EnterMarksPage(Router router, GlobalState state) {
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

    if (state.getCurrUser().getRole() != Role.LECTURER) {
      SwingUtilities.invokeLater(() -> {
        router.showView(Pages.DASHBOARD, state);
      });
      return;
    }

    header = new HeaderPanel(router, state);
    nav = new NavPanel(router, state);

    contentBody = new JPanel(new MigLayout("insets 20 20, wrap 1, gapy 10"));
    contentBody.setBackground(App.slate100);

    searchInput = "";

    searchField = new TextField("Search " + dataContext + "...");
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
      searchField.setText("");
      searchInput = "";
      refetchData(state);
    });

    searchBtn = new JButton();
    searchBtn.setText("Search");
    searchBtn.setForeground(Color.WHITE);
    searchBtn.setBackground(App.blue600);
    searchBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    searchBtn.setFocusable(false);
    searchBtn.setBorder(BorderFactory.createCompoundBorder(searchBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    searchBtn.addActionListener(e -> {
      searchInput = searchField.getText().trim();
      refetchData(state);
    });

    JPanel searchGroup = new JPanel(new MigLayout("insets 0, gapx 10"));
    searchGroup.setBackground(App.slate100);
    searchGroup.add(searchField);
    searchGroup.add(searchBtn);
    searchGroup.add(searchClearBtn);

    addBtn = new JButton();
    addBtn.setText("Add Mark");
    addBtn.setIcon(Helper.iconResizer(new ImageIcon("assets/add-icon.png"), 18, 18));
    addBtn.setForeground(Color.WHITE);
    addBtn.setBackground(App.green600);
    addBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    addBtn.setFocusable(false);
    addBtn.setBorder(BorderFactory.createCompoundBorder(addBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    addBtn.addActionListener(e -> showAddMarkDialog(state));

    editBtn = new JButton();
    editBtn.setText("Edit");
    editBtn.setIcon(Helper.iconResizer(new ImageIcon("assets/edit-icon.png"), 18, 18));
    editBtn.setForeground(Color.WHITE);
    editBtn.setBackground(App.orange600);
    editBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    editBtn.setFocusable(false);
    editBtn.setBorder(BorderFactory.createCompoundBorder(editBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    editBtn.addActionListener(e -> {
      if (selectedRow != -1) {
        showEditMarkDialog(state, selectedRow);
      } else {
        JOptionPane.showMessageDialog(this, "Please select a mark record first", "Warning", JOptionPane.WARNING_MESSAGE);
      }
    });

    deleteBtn = new JButton();
    deleteBtn.setText("Delete");
    deleteBtn.setIcon(Helper.iconResizer(new ImageIcon("assets/delete-icon.png"), 18, 18));
    deleteBtn.setForeground(Color.WHITE);
    deleteBtn.setBackground(App.red600);
    deleteBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    deleteBtn.setFocusable(false);
    deleteBtn.setBorder(BorderFactory.createCompoundBorder(deleteBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    deleteBtn.addActionListener(e -> {
      if (selectedRow != -1) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this mark record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
          AssessmentMark selected = markTableModel.getMarkAt(selectedRow);
          AssessmentMark.deleteAssessmentMark(selected.getID());
          refetchData(state);
          selectedRow = -1;
        }
      } else {
        JOptionPane.showMessageDialog(this, "Please select a mark record first", "Warning", JOptionPane.WARNING_MESSAGE);
      }
    });

    actionBtnsContainer = new JPanel(new MigLayout("insets 0, gapx 10"));
    actionBtnsContainer.setBackground(App.slate100);
    actionBtnsContainer.add(addBtn);
    actionBtnsContainer.add(editBtn);
    actionBtnsContainer.add(deleteBtn);

    JPanel searchFilterActionRow = new JPanel(new MigLayout("insets 0, gapx 20"));
    searchFilterActionRow.setBackground(App.slate100);
    searchFilterActionRow.add(searchGroup);
    searchFilterActionRow.add(actionBtnsContainer, "push, align right");

    // Table
    marks = AssessmentMark.fetchAssessmentMarks(searchInput, state.getCurrUser());
    markTableModel = new AssessmentMarkTableModel(marks);

    table = new JTable(markTableModel);
    table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    table.getSelectionModel().addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) {
        selectedRow = table.getSelectedRow();
      }
    });

    table.setPreferredScrollableViewportSize(new Dimension(table.getWidth(), 800));
    table.setFillsViewportHeight(true);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    table.setRowHeight(40);

    JTableHeader header_table = table.getTableHeader();
    header_table.setBackground(new Color(51, 65, 85));
    header_table.setForeground(Color.WHITE);
    header_table.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
    header_table.setPreferredSize(new Dimension(header_table.getPreferredSize().width, 45));

    table.setBackground(Color.WHITE);
    table.setForeground(Color.BLACK);
    table.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
    table.setGridColor(new Color(226, 232, 240));
    table.setShowGrid(true);
    table.setIntercellSpacing(new Dimension(1, 1));

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

    for (int i = 0; i < table.getColumnCount(); i++) {
        table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
    }

    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBackground(App.slate100);
    scrollPane.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

    rowCountLabel = new JLabel("Total " + dataContext + ": " + marks.size());
    rowCountLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

    tableSection = new JPanel(new MigLayout("insets 5 10, wrap 1, gapy 5"));
    tableSection.setBackground(App.slate100);
    tableSection.add(rowCountLabel);
    tableSection.add(scrollPane, "grow, width 100%");
    contentBody.add(searchFilterActionRow, "growx, width 100%");
    contentBody.add(tableSection, "grow, width 100%");

    this.add(header, "span, growx, wrap");
    this.add(nav, "growy");
    this.add(contentBody, "span, grow");
  }

  private void refetchData(GlobalState state) {
    marks = AssessmentMark.fetchAssessmentMarks(searchInput, state.getCurrUser());
    markTableModel.setMarks(marks);
    rowCountLabel.setText("Total " + dataContext + ": " + marks.size());
    selectedRow = -1;
  }

  private void showAddMarkDialog(GlobalState state) {
    // Get lecturer's assessments
    List<Assessment> assessments = Assessment.getListOfAssessmentsByMatchingValues("lecturer", state.getCurrUser().getID());
    
    if (assessments.isEmpty()) {
      JOptionPane.showMessageDialog(this, "You don't have any assessments created. Please create assessments first.", "No Assessments", JOptionPane.WARNING_MESSAGE);
      return;
    }

    JPanel panel = new JPanel(new MigLayout("insets 10, wrap 1, gapy 10"));
    panel.setBackground(App.slate100);

    JLabel assessmentLabel = new JLabel("Assessment:");
    JComboBox<Assessment> assessmentCombo = new JComboBox<>(assessments.toArray(new Assessment[0]));
    assessmentCombo.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
        JLabel label = new JLabel(value != null ? value.getName() : "");
        return label;
    });
    
    JLabel studentLabel = new JLabel("Student:");
    JComboBox<Student> studentCombo = new JComboBox<>();
    studentCombo.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
        JLabel label = new JLabel(value != null ? (value.getFirstName() + " " + value.getLastName()) : "");
        return label;
    });

    // Update student list when assessment changes
    assessmentCombo.addActionListener(e -> {
      Assessment selected = (Assessment) assessmentCombo.getSelectedItem();
      if (selected != null && selected.getModule() != null) {
        List<StudentModule> studentModules = StudentModule.getListOfStudentModulesByMatchingValues("module", selected.getModule().getID());
        studentCombo.removeAllItems();
        for (StudentModule sm : studentModules) {
          if (sm.getStudent() != null) {
            studentCombo.addItem(sm.getStudent());
          }
        }
      }
    });
    
    // Initialize student list
    Assessment initialAssessment = (Assessment) assessmentCombo.getSelectedItem();
    if (initialAssessment != null && initialAssessment.getModule() != null) {
      List<StudentModule> studentModules = StudentModule.getListOfStudentModulesByMatchingValues("module", initialAssessment.getModule().getID());
      for (StudentModule sm : studentModules) {
        if (sm.getStudent() != null) {
          studentCombo.addItem(sm.getStudent());
        }
      }
    }
    
    JLabel marksLabel = new JLabel("Marks Obtained:");
    JTextField marksField = new JTextField("0", 10);

    panel.add(assessmentLabel);
    panel.add(assessmentCombo, "growx");
    panel.add(studentLabel);
    panel.add(studentCombo, "growx");
    panel.add(marksLabel);
    panel.add(marksField, "growx");

    int result = JOptionPane.showConfirmDialog(this, panel, "Add Assessment Mark", JOptionPane.OK_CANCEL_OPTION);
    
    if (result == JOptionPane.OK_OPTION) {
      if (studentCombo.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(this, "Please select a student", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
      }

      Assessment selectedAssessment = (Assessment) assessmentCombo.getSelectedItem();
      Student selectedStudent = (Student) studentCombo.getSelectedItem();

      // Check for duplicate assessment mark
      if (AssessmentMark.existsAssessmentMarkByAssessmentAndStudent(selectedAssessment.getID(), selectedStudent.getID())) {
        JOptionPane.showMessageDialog(this, "A mark for this student and assessment already exists. Please edit the existing mark instead.", "Duplicate Mark", JOptionPane.WARNING_MESSAGE);
        return;
      }

      HashMap<String, String> inputValues = new HashMap<>();
      inputValues.put("assessment", selectedAssessment.getID());
      inputValues.put("student", selectedStudent.getID());
      inputValues.put("marksObtained", marksField.getText().trim());
      inputValues.put("recordedAt", LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

      Validation validation = AssessmentMark.validate(inputValues);
      if (validation.getSuccess()) {
        AssessmentMark mark = new AssessmentMark(inputValues);
        AssessmentMark.saveAssessmentMark(mark);
        JOptionPane.showMessageDialog(this, "Mark added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        refetchData(state);
      } else {
        JOptionPane.showMessageDialog(this, validation.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void showEditMarkDialog(GlobalState state, int rowIndex) {
    AssessmentMark mark = markTableModel.getMarkAt(rowIndex);
    
    // Get lecturer's assessments
    List<Assessment> assessments = Assessment.getListOfAssessmentsByMatchingValues("lecturer", state.getCurrUser().getID());

    JPanel panel = new JPanel(new MigLayout("insets 10, wrap 1, gapy 10"));
    panel.setBackground(App.slate100);

    JLabel assessmentLabel = new JLabel("Assessment:");
    JComboBox<Assessment> assessmentCombo = new JComboBox<>(assessments.toArray(new Assessment[0]));
    assessmentCombo.setSelectedItem(mark.getAssessment());
    assessmentCombo.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
        JLabel label = new JLabel(value != null ? value.getName() : "");
        return label;
    });
    
    JLabel studentLabel = new JLabel("Student:");
    JComboBox<Student> studentCombo = new JComboBox<>();
    studentCombo.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
        JLabel label = new JLabel(value != null ? (value.getFirstName() + " " + value.getLastName()) : "");
        return label;
    });

    // Update student list when assessment changes
    assessmentCombo.addActionListener(e -> {
      Assessment selected = (Assessment) assessmentCombo.getSelectedItem();
      if (selected != null && selected.getModule() != null) {
        List<StudentModule> studentModules = StudentModule.getListOfStudentModulesByMatchingValues("module", selected.getModule().getID());
        studentCombo.removeAllItems();
        for (StudentModule sm : studentModules) {
          if (sm.getStudent() != null) {
            studentCombo.addItem(sm.getStudent());
          }
        }
      }
    });
    
    // Initialize student list
    Assessment initialAssessment = (Assessment) assessmentCombo.getSelectedItem();
    if (initialAssessment != null && initialAssessment.getModule() != null) {
      List<StudentModule> studentModules = StudentModule.getListOfStudentModulesByMatchingValues("module", initialAssessment.getModule().getID());
      for (StudentModule sm : studentModules) {
        if (sm.getStudent() != null) {
          studentCombo.addItem(sm.getStudent());
        }
      }
      if (mark.getStudent() != null) {
        for (int i = 0; i < studentCombo.getItemCount(); i++) {
          if (studentCombo.getItemAt(i).getID().equals(mark.getStudent().getID())) {
            studentCombo.setSelectedIndex(i);
            break;
          }
        }
      }
    }
    
    JLabel marksLabel = new JLabel("Marks Obtained:");
    JTextField marksField = new JTextField(String.valueOf(mark.getMarksObtained()), 10);

    panel.add(assessmentLabel);
    panel.add(assessmentCombo, "growx");
    panel.add(studentLabel);
    panel.add(studentCombo, "growx");
    panel.add(marksLabel);
    panel.add(marksField, "growx");

    int result = JOptionPane.showConfirmDialog(this, panel, "Edit Assessment Mark", JOptionPane.OK_CANCEL_OPTION);
    
    if (result == JOptionPane.OK_OPTION) {
      HashMap<String, String> inputValues = new HashMap<>();
      inputValues.put("id", mark.getID());
      inputValues.put("assessment", ((Assessment) assessmentCombo.getSelectedItem()).getID());
      inputValues.put("student", ((Student) studentCombo.getSelectedItem()).getID());
      inputValues.put("marksObtained", marksField.getText().trim());
      inputValues.put("recordedAt", mark.getRecordedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

      Validation validation = AssessmentMark.validate(inputValues);
      if (validation.getSuccess()) {
        AssessmentMark updatedMark = new AssessmentMark(inputValues);
        AssessmentMark.saveAssessmentMark(updatedMark);
        JOptionPane.showMessageDialog(this, "Mark updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        refetchData(state);
      } else {
        JOptionPane.showMessageDialog(this, validation.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
}