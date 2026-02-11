package com.apu_afs.Views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
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
import com.apu_afs.Models.Module;
import com.apu_afs.Models.Validation;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Models.Enums.Role;
import com.apu_afs.TableModels.AssessmentTableModel;
import com.apu_afs.Views.components.HeaderPanel;
import com.apu_afs.Views.components.NavPanel;
import com.apu_afs.Views.components.TextField;

import net.miginfocom.swing.MigLayout;

public class AssessmentsPage extends JPanel {
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

  List<Assessment> assessments;
  AssessmentTableModel assessmentTableModel;

  String searchInput;
  int selectedRow = -1;
 
  private static final String dataContext = "Assessments";

  public AssessmentsPage(Router router, GlobalState state) {
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
    addBtn.setText("Add Assessment");
    addBtn.setIcon(Helper.iconResizer(new ImageIcon("assets/add-icon.png"), 18, 18));
    addBtn.setForeground(Color.WHITE);
    addBtn.setBackground(App.green600);
    addBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    addBtn.setFocusable(false);
    addBtn.setBorder(BorderFactory.createCompoundBorder(addBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    addBtn.addActionListener(e -> showAddAssessmentDialog(state));

    editBtn = new JButton();
    editBtn.setText("Edit");
    editBtn.setIcon(Helper.iconResizer(new ImageIcon("assets/edit-icon.png"), 18, 18));
    editBtn.setForeground(Color.WHITE);
    editBtn.setBackground(App.orange600);
    editBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    editBtn.setFocusable(false);
    editBtn.setBorder(BorderFactory.createCompoundBorder(editBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    editBtn.addActionListener(e -> {
      int viewRow = table.getSelectedRow();

      if (viewRow != -1) {
        int modelRow = table.convertRowIndexToModel(viewRow);
        showEditAssessmentDialog(state, modelRow);
      } else {
        JOptionPane.showMessageDialog(this, 
          "Please select an assessment first", 
          "Warning", 
          JOptionPane.WARNING_MESSAGE);
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
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this assessment?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
          Assessment selected = assessmentTableModel.getAssessmentAt(selectedRow);
          Assessment.deleteAssessment(selected.getID());
          refetchData(state);
          selectedRow = -1;
        }
      } else {
        JOptionPane.showMessageDialog(this, "Please select an assessment first", "Warning", JOptionPane.WARNING_MESSAGE);
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
    assessments = Assessment.fetchAssessments(searchInput, state.getCurrUser());
    assessmentTableModel = new AssessmentTableModel(assessments);

    table = new JTable(assessmentTableModel);
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

    rowCountLabel = new JLabel("Total " + dataContext + ": " + assessments.size());
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
    assessments = Assessment.fetchAssessments(searchInput, state.getCurrUser());
    assessmentTableModel.setAssessments(assessments);
    rowCountLabel.setText("Total " + dataContext + ": " + assessments.size());
    selectedRow = -1;
  }

  private void showAddAssessmentDialog(GlobalState state) {
    JPanel panel = new JPanel(new MigLayout("insets 10, wrap 1, gapy 10"));
    panel.setBackground(App.slate100);

    // Get lecturer's modules only
    List<Module> modules = Module.fetchModules("", state.getCurrUser());

    
    if (modules.isEmpty()) {
      JOptionPane.showMessageDialog(this, "You don't have any modules assigned. Please contact your academic leader.", "No Modules", JOptionPane.WARNING_MESSAGE);
      return;
    }

    JLabel nameLabel = new JLabel("Assessment Name:");
    JTextField nameField = new JTextField(20);
    
    JLabel moduleLabel = new JLabel("Module:");
    JComboBox<Module> moduleCombo = new JComboBox<>(modules.toArray(new Module[0]));
    moduleCombo.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
        JLabel label = new JLabel(value != null ? value.getTitle() : "");
        return label;
    });
    
    JLabel typeLabel = new JLabel("Assessment Type:");
    String[] types = {"Assignment", "Quiz", "Final Exam", "Presentation"};
    JComboBox<String> typeCombo = new JComboBox<>(types);
    
    JLabel marksLabel = new JLabel("Total Marks:");
    JTextField marksField = new JTextField("100", 10);

    panel.add(nameLabel);
    panel.add(nameField, "growx");
    panel.add(moduleLabel);
    panel.add(moduleCombo, "growx");
    panel.add(typeLabel);
    panel.add(typeCombo, "growx");
    panel.add(marksLabel);
    panel.add(marksField, "growx");

    int result = JOptionPane.showConfirmDialog(this, panel, "Add New Assessment", JOptionPane.OK_CANCEL_OPTION);
    
    if (result == JOptionPane.OK_OPTION) {
      HashMap<String, String> inputValues = new HashMap<>();
      inputValues.put("name", nameField.getText().trim());
      inputValues.put("module", ((Module) moduleCombo.getSelectedItem()).getID());
      inputValues.put("type", (String) typeCombo.getSelectedItem());
      inputValues.put("totalMarks", marksField.getText().trim());
      inputValues.put("lecturer", state.getCurrUser().getID());
      inputValues.put("createdAt", LocalDate.now().format(Helper.dateTimeFormatter));

      Validation validation = Assessment.validate(inputValues);
      if (validation.getSuccess()) {
        Assessment assessment = new Assessment(inputValues);
        Assessment.saveAssessment(assessment);
        JOptionPane.showMessageDialog(this, "Assessment created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        refetchData(state);
      } else {
        JOptionPane.showMessageDialog(this, validation.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void showEditAssessmentDialog(GlobalState state, int rowIndex) {
    Assessment assessment = assessmentTableModel.getAssessmentAt(rowIndex);
    
    JPanel panel = new JPanel(new MigLayout("insets 10, wrap 1, gapy 10"));
    panel.setBackground(App.slate100);

   // List<Module> modules = Module.getListOfModuleByMatchingValues("instructorID", state.getCurrUser().getID());
    List<Module> modules = Module.fetchModules("", state.getCurrUser());


    JLabel nameLabel = new JLabel("Assessment Name:");
    JTextField nameField = new JTextField(assessment.getName(), 20);
    
    JLabel moduleLabel = new JLabel("Module:");
    JComboBox<Module> moduleCombo = new JComboBox<>(modules.toArray(new Module[0]));
    moduleCombo.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
        JLabel label = new JLabel(value != null ? value.getTitle() : "");
        return label;
    });
    // Select module by ID match
    if (assessment.getModule() != null) {
      for (int i = 0; i < modules.size(); i++) {
        if (modules.get(i).getID().equals(assessment.getModule().getID())) {
          moduleCombo.setSelectedIndex(i);
          break;
        }
      }
    }
    
    JLabel typeLabel = new JLabel("Assessment Type:");
    String[] types = {"Assignment", "Quiz", "Final Exam", "Presentation"};
    JComboBox<String> typeCombo = new JComboBox<>(types);
    typeCombo.setSelectedItem(assessment.getType());
    
    JLabel marksLabel = new JLabel("Total Marks:");
    JTextField marksField = new JTextField(String.valueOf(assessment.getTotalMarks()), 10);

    panel.add(nameLabel);
    panel.add(nameField, "growx");
    panel.add(moduleLabel);
    panel.add(moduleCombo, "growx");
    panel.add(typeLabel);
    panel.add(typeCombo, "growx");
    panel.add(marksLabel);
    panel.add(marksField, "growx");

    int result = JOptionPane.showConfirmDialog(this, panel, "Edit Assessment", JOptionPane.OK_CANCEL_OPTION);
    
    if (result == JOptionPane.OK_OPTION) {
      HashMap<String, String> inputValues = new HashMap<>();
      inputValues.put("id", assessment.getID());
      inputValues.put("name", nameField.getText().trim());
      inputValues.put("module", ((Module) moduleCombo.getSelectedItem()).getID());
      inputValues.put("type", (String) typeCombo.getSelectedItem());
      inputValues.put("totalMarks", marksField.getText().trim());
      inputValues.put("lecturer", state.getCurrUser().getID());
      inputValues.put("createdAt", assessment.getCreatedAt().format(Helper.dateTimeFormatter));

      Validation validation = Assessment.validate(inputValues);
      if (validation.getSuccess()) {
        Assessment updatedAssessment = new Assessment(inputValues);
        Assessment.saveAssessment(updatedAssessment);
        JOptionPane.showMessageDialog(this, "Assessment updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        refetchData(state);
      } else {
        JOptionPane.showMessageDialog(this, validation.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
}
