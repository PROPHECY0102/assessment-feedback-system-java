package com.apu_afs.Views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.apu_afs.GlobalState;
import com.apu_afs.Models.GradeRange;
import com.apu_afs.Models.Validation;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.TableModels.GradeRangeTableModel;
import com.apu_afs.Views.components.HeaderPanel;
import com.apu_afs.Views.components.NavPanel;
import com.apu_afs.Views.components.TextField;

import net.miginfocom.swing.MigLayout;

public class ManageGradesPage extends JPanel {
  HeaderPanel header;
  NavPanel nav;

  JPanel contentBody;

  JPanel titleContainer;
  JLabel title;
  JLabel subtitle;

  JPanel gradeFormTableContainer;
  
  JPanel gradeForm;

  JLabel formContext;

  JPanel gradePointsRow;
  JPanel gradeFieldGroup;
  JLabel gradeLabel;
  TextField gradeField;
  JLabel gradeErrorLabel;
  JPanel pointsFieldGroup;
  JLabel pointsLabel;
  TextField pointsField;
  JLabel pointsErrorLabel;

  JPanel descriptionRow;
  JPanel descriptionFieldGroup;
  JLabel descriptionLabel;
  TextField descriptionField;
  JLabel descriptionErrorLabel;

  JPanel minMaxRow;
  JPanel minFieldGroup;
  JLabel minLabel;
  TextField minField;
  JLabel minErrorLabel;
  JPanel maxFieldGroup;
  JLabel maxLabel;
  TextField maxField;
  JLabel maxErrorLabel;

  JPanel actionButtonGroup;
  JButton submitBtn;
  JButton deleteBtn;

  JPanel gradeTableContainer;
  JButton clearSelectionBtn;
  GradeRangeTableModel gradeRangeTableModel;
  List<GradeRange> gradeRanges;
  JTable table;

  Map<String, TextField> textFields;
  Map<String, JLabel> errorLabels;

  String editingGradeID;

  private static final String[] allowedRoles = {"admin"};

  public ManageGradesPage(Router router, GlobalState state) {
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

    header = new HeaderPanel(router, state);
    nav = new NavPanel(router, state);

    contentBody = new JPanel(new MigLayout("insets 20 20, wrap 1, gapy 40, align center center"));
    contentBody.setBackground(App.slate100);

    title = new JLabel("Define APU Grading System");
    title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
    subtitle = new JLabel("These Grades will be used for calculating student's grade point averages (GPA) for their assessments scores.");
    subtitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
    titleContainer = new JPanel(new MigLayout("insets 0, wrap 1, gapy 10"));
    titleContainer.setBackground(App.slate100);
    titleContainer.add(title);
    titleContainer.add(subtitle);
    
    formContext = new JLabel();
    formContext.setText("Creating New Grade");
    formContext.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

    gradeLabel = new JLabel();
    gradeLabel.setText("Grade: ");
    gradeField = new TextField("Enter Grade Here...");
    gradeField.setBackground(App.slate200);
    gradeField.setBorder(BorderFactory.createCompoundBorder(gradeField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    gradeField.setPreferredSize(new Dimension(200, 35));
    gradeErrorLabel = new JLabel("\s");
    gradeErrorLabel.setForeground(App.red600);
    gradeFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    gradeFieldGroup.setBackground(App.slate100);
    gradeFieldGroup.add(gradeLabel);
    gradeFieldGroup.add(gradeField);
    gradeFieldGroup.add(gradeErrorLabel);

    pointsLabel = new JLabel();
    pointsLabel.setText("Points: ");
    pointsField = new TextField("Enter Points Here...");
    pointsField.setBackground(App.slate200);
    pointsField.setBorder(BorderFactory.createCompoundBorder(pointsField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    pointsField.setPreferredSize(new Dimension(200, 35));
    pointsErrorLabel = new JLabel("\s");
    pointsErrorLabel.setForeground(App.red600);
    pointsFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    pointsFieldGroup.setBackground(App.slate100);
    pointsFieldGroup.add(pointsLabel);
    pointsFieldGroup.add(pointsField);
    pointsFieldGroup.add(pointsErrorLabel);

    descriptionLabel = new JLabel();
    descriptionLabel.setText("Description: ");
    descriptionField = new TextField("Enter Description Here...");
    descriptionField.setBackground(App.slate200);
    descriptionField.setBorder(BorderFactory.createCompoundBorder(descriptionField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    descriptionField.setPreferredSize(new Dimension(450, 35));
    descriptionErrorLabel = new JLabel("\s");
    descriptionErrorLabel.setForeground(App.red600);
    descriptionFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    descriptionFieldGroup.setBackground(App.slate100);
    descriptionFieldGroup.add(descriptionLabel);
    descriptionFieldGroup.add(descriptionField);
    descriptionFieldGroup.add(descriptionErrorLabel);

    minLabel = new JLabel();
    minLabel.setText("Minimum Score: ");
    minField = new TextField("Enter Minimum Score Here...");
    minField.setBackground(App.slate200);
    minField.setBorder(BorderFactory.createCompoundBorder(minField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    minField.setPreferredSize(new Dimension(200, 35));
    minErrorLabel = new JLabel("\s");
    minErrorLabel.setForeground(App.red600);
    minFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    minFieldGroup.setBackground(App.slate100);
    minFieldGroup.add(minLabel);
    minFieldGroup.add(minField);
    minFieldGroup.add(minErrorLabel);

    maxLabel = new JLabel();
    maxLabel.setText("Maximum Score: ");
    maxField = new TextField("Enter Maximum Score Here...");
    maxField.setBackground(App.slate200);
    maxField.setBorder(BorderFactory.createCompoundBorder(maxField.getBorder(), BorderFactory.createEmptyBorder(10, 15, 10, 15)));
    maxField.setPreferredSize(new Dimension(200, 35));
    maxErrorLabel = new JLabel("\s");
    maxErrorLabel.setForeground(App.red600);
    maxFieldGroup = new JPanel(new MigLayout("insets 0, wrap 1, gap 5"));
    maxFieldGroup.setBackground(App.slate100);
    maxFieldGroup.add(maxLabel);
    maxFieldGroup.add(maxField);
    maxFieldGroup.add(maxErrorLabel);

    gradePointsRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 50"));
    gradePointsRow.setBackground(App.slate100);
    gradePointsRow.add(gradeFieldGroup);
    gradePointsRow.add(pointsFieldGroup);

    descriptionRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 50"));
    descriptionRow.setBackground(App.slate100);
    descriptionRow.add(descriptionFieldGroup);

    minMaxRow = new JPanel(new MigLayout("insets 0, aligny center, gapx 50"));
    minMaxRow.setBackground(App.slate100);
    minMaxRow.add(minFieldGroup);
    minMaxRow.add(maxFieldGroup); 

    textFields = Map.ofEntries(
      Map.entry("grade", gradeField),
      Map.entry("points", pointsField),
      Map.entry("description", descriptionField),
      Map.entry("min", minField),
      Map.entry("max", maxField)
    );

    errorLabels = Map.ofEntries(
      Map.entry("grade", gradeErrorLabel),
      Map.entry("points", pointsErrorLabel),
      Map.entry("description", descriptionErrorLabel),
      Map.entry("min", minErrorLabel),
      Map.entry("max", maxErrorLabel)
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
      inputValues.put("id", editingGradeID);

      for (String key : textFields.keySet()) {
        inputValues.put(key, textFields.get(key).getText().trim());
        if (key.equals("description")) {
          inputValues.put("description", inputValues.get("description").isEmpty() ? "None" : inputValues.get("description"));
        }
      }

      Validation inputValidation = GradeRange.validate(inputValues);
      if (inputValidation.getSuccess()) {
        GradeRange gr = new GradeRange(inputValues);

        gr.update();
        String gradeInfoDisplay = "\nGrade: '" + gr.getGrade() + "'" + "\nDescription: " + gr.getDescription();
        String messageDialogContent = editingGradeID != null ? "Current Grade has been updated!" + gradeInfoDisplay : "New Grade has been created!" + gradeInfoDisplay;
        String messageDialogTitle = editingGradeID != null ? "Success: Updated Selected Grade" : "Success: Created New Grade";
        JOptionPane.showMessageDialog(router, messageDialogContent, messageDialogTitle, JOptionPane.INFORMATION_MESSAGE);
        router.showView(Pages.MANAGEGRADES, state);
      } else {
        if (!inputValidation.getSuccess()) {
          this.displayError(inputValidation);
          String messageDialogTitle = editingGradeID != null ? "Cannot edit Grade: " + editingGradeID : "Cannot create new Grade"; 
          JOptionPane.showMessageDialog(router, inputValidation.getMessage(), "Error: Invalid Form input! " + messageDialogTitle, JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    deleteBtn = new JButton();
    deleteBtn.setText("Delete");
    deleteBtn.setForeground(Color.WHITE);
    deleteBtn.setBackground(App.red600);
    deleteBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    deleteBtn.setBorder(BorderFactory.createCompoundBorder(deleteBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    deleteBtn.setFocusable(false);
    deleteBtn.addActionListener(e -> {
      GradeRange gr = GradeRange.getGradeRangeByMatchingValue("id", editingGradeID);

      if (gr == null) {
        JOptionPane.showMessageDialog(router, "No Grade is currently being selected", "Error: Unable to delete current grade", JOptionPane.ERROR_MESSAGE);
      } else {
        String gradeInfoDisplay = "\nGrade: '" + gr.getGrade() + "'" + "\nDescription: " + gr.getDescription();
        int choice = JOptionPane.showConfirmDialog(router, "Are you sure you want to delete this Grade?" + gradeInfoDisplay, "Delete This Grade Confirmation", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
          gr.delete();
          router.showView(Pages.MANAGEGRADES, state);
          JOptionPane.showMessageDialog(router, "This Grade has been deleted successfully" + gradeInfoDisplay, "Grade has been Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
      }
    });

    actionButtonGroup = new JPanel(new MigLayout("insets 25 0, aligny center"));
    actionButtonGroup.setBackground(App.slate100);
    actionButtonGroup.add(deleteBtn);
    actionButtonGroup.add(submitBtn, "push, alignx right");

    gradeForm = new JPanel(new MigLayout("insets 0, wrap 1, gapy 10"));
    gradeForm.setBackground(App.slate100);
    gradeForm.add(formContext);
    gradeForm.add(gradePointsRow);
    gradeForm.add(descriptionRow);
    gradeForm.add(minMaxRow);
    gradeForm.add(actionButtonGroup, "growx");

    gradeRanges = GradeRange.getListOfGradeRanges();
    gradeRangeTableModel = new GradeRangeTableModel(gradeRanges);

    table = new JTable(gradeRangeTableModel);
    table.setPreferredScrollableViewportSize(new Dimension(500, 400));
    // Wrap the table in a JScrollPane to show column headers
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBackground(App.slate100);

    // Configure table appearance
    table.setFillsViewportHeight(true);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    table.setRowHeight(40); // Add padding to rows

    // Style the table header
    JTableHeader tableHeader = table.getTableHeader();
    tableHeader.setBackground(new Color(51, 65, 85));
    tableHeader.setForeground(Color.WHITE);
    tableHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
    tableHeader.setPreferredSize(new Dimension(tableHeader.getPreferredSize().width, 45));

    // Style the table cells
    table.setBackground(Color.WHITE);
    table.setForeground(Color.BLACK);
    table.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
    table.setGridColor(new Color(226, 232, 240));
    table.setShowGrid(true);
    table.setIntercellSpacing(new Dimension(1, 1));

    // Add cell padding with a custom renderer
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
        
        // Alternate row colors
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

    // Apply the renderer to all columns
    for (int i = 0; i < table.getColumnCount(); i++) {
      table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
    }

    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
          return;
        }

        if (table.getSelectedRow() > -1) {
          templateGradeFields((String) table.getValueAt(table.getSelectedRow(), 0));
        }
      }
    });

    clearSelectionBtn = new JButton();
    clearSelectionBtn.setText("Clear Grade Selection");
    clearSelectionBtn.setForeground(Color.WHITE);
    clearSelectionBtn.setBackground(App.orange600);
    clearSelectionBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    clearSelectionBtn.setBorder(BorderFactory.createCompoundBorder(clearSelectionBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    clearSelectionBtn.addActionListener(e -> {
      table.clearSelection();
      templateGradeFields("clear");
    });

    gradeTableContainer = new JPanel(new MigLayout("insets 0, wrap 1, gapy 10, align center center"));
    gradeTableContainer.setBackground(App.slate100);
    gradeTableContainer.add(clearSelectionBtn, "alignx right");
    gradeTableContainer.add(scrollPane);

    gradeFormTableContainer = new JPanel(new MigLayout("insets 0, fillx, gapx 50"));
    gradeFormTableContainer.setBackground(App.slate100);
    gradeFormTableContainer.add(gradeForm, "growx");
    gradeFormTableContainer.add(gradeTableContainer, "growx, pushx");

    contentBody.add(titleContainer);
    contentBody.add(gradeFormTableContainer);

    this.add(header, "span, growx, wrap");
    this.add(nav, "growy");
    this.add(contentBody, "span, grow");

    state.clearState();
  }

  private void clearGradeFields() {
    formContext.setText("Creating New Grade");

    for (String key : textFields.keySet()) {
      textFields.get(key).setText("");
    }

    for (String key : errorLabels.keySet()) {
      errorLabels.get(key).setText("\s");
    }
  }

  private void templateGradeFields(String grade) {
    GradeRange currGradeRange = GradeRange.getGradeRangeByMatchingValue("grade", grade);

    if (currGradeRange == null) {
      clearGradeFields();
      return;
    }

    editingGradeID = currGradeRange.getID();

    formContext.setText("Editing Grade '" + currGradeRange.getGrade() + "'");

    gradeField.setText(currGradeRange.getGrade());
    pointsField.setText(String.valueOf(currGradeRange.getPoints()));
    descriptionField.setText(currGradeRange.getDescription());
    minField.setText(String.valueOf(currGradeRange.getMin()));
    maxField.setText(String.valueOf(currGradeRange.getMax()));

    for (String key : errorLabels.keySet()) {
      errorLabels.get(key).setText("\s");
    }
  }

  private void displayError(Validation validation) {
    if (textFields.get(validation.getField()) != null) {
      textFields.get(validation.getField()).setBackground(App.red100);
    }

    if (errorLabels.get(validation.getField()) != null) {
      errorLabels.get(validation.getField()).setText(validation.getMessage());
    }
  }
}