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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.apu_afs.GlobalState;
import com.apu_afs.Helper;
import com.apu_afs.Models.AssessmentMark;
import com.apu_afs.Models.Feedback;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Models.Enums.Role;
import com.apu_afs.TableModels.FeedbackTableModel;
import com.apu_afs.Views.components.HeaderPanel;
import com.apu_afs.Views.components.NavPanel;
import com.apu_afs.Views.components.TextField;

import net.miginfocom.swing.MigLayout;

public class ProvideFeedbackPage extends JPanel {
  HeaderPanel header;
  NavPanel nav;

  JPanel contentBody;

  JPanel searchSection;
  TextField searchField;
  JButton searchClearBtn;
  JButton searchBtn;

  JPanel actionBtnsContainer;
  JButton editBtn;

  JPanel tableSection;
  JLabel rowCountLabel;
  JTable table;

  List<Feedback> feedbacks;
  FeedbackTableModel feedbackTableModel;

  String searchInput;
  int selectedRow = -1;
 
  private static final String dataContext = "Feedbacks";

  public ProvideFeedbackPage(Router router, GlobalState state) {
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

    editBtn = new JButton();
    editBtn.setText("Edit Feedback");
    editBtn.setIcon(Helper.iconResizer(new ImageIcon("assets/edit-icon.png"), 18, 18));
    editBtn.setForeground(Color.WHITE);
    editBtn.setBackground(App.orange600);
    editBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    editBtn.setFocusable(false);
    editBtn.setBorder(BorderFactory.createCompoundBorder(editBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    editBtn.addActionListener(e -> {
      if (selectedRow != -1) {
        showEditFeedbackDialog(state, selectedRow);
      } else {
        JOptionPane.showMessageDialog(this, "Please select a feedback record first", "Warning", JOptionPane.WARNING_MESSAGE);
      }
    });

    actionBtnsContainer = new JPanel(new MigLayout("insets 0, gapx 10"));
    actionBtnsContainer.setBackground(App.slate100);
    actionBtnsContainer.add(editBtn);

    JPanel searchFilterActionRow = new JPanel(new MigLayout("insets 0, gapx 20"));
    searchFilterActionRow.setBackground(App.slate100);
    searchFilterActionRow.add(searchGroup);
    searchFilterActionRow.add(actionBtnsContainer, "push, align right");

    
    initializeFeedbacksForMarkedAssessments(state);

    
    feedbacks = Feedback.fetchFeedbacks(searchInput, state.getCurrUser());
    feedbackTableModel = new FeedbackTableModel(feedbacks);

    table = new JTable(feedbackTableModel);
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

    rowCountLabel = new JLabel("Total " + dataContext + ": " + feedbacks.size());
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

  private void initializeFeedbacksForMarkedAssessments(GlobalState state) {
    // Get all marked assessments for this lecturer
    List<AssessmentMark> marks = AssessmentMark.fetchAssessmentMarks("", state.getCurrUser());
    
    for (AssessmentMark mark : marks) {
      
      List<Feedback> existingFeedbacks = Feedback.getListOfFeedbacksByMatchingValues("assessmentMark", mark.getID());
      
      if (existingFeedbacks.isEmpty()) {
        
        HashMap<String, String> feedbackData = new HashMap<>();
        feedbackData.put("assessmentMark", mark.getID());
        feedbackData.put("content", "");
        feedbackData.put("createdAt", LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        feedbackData.put("updatedAt", LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        
        Feedback feedback = new Feedback(feedbackData);
        Feedback.saveFeedback(feedback);
      }
    }
  }

  private void refetchData(GlobalState state) {
    feedbacks = Feedback.fetchFeedbacks(searchInput, state.getCurrUser());
    feedbackTableModel.setFeedbacks(feedbacks);
    rowCountLabel.setText("Total " + dataContext + ": " + feedbacks.size());
    selectedRow = -1;
  }

  private void showEditFeedbackDialog(GlobalState state, int rowIndex) {
    Feedback feedback = feedbackTableModel.getFeedbackAt(rowIndex);
    AssessmentMark mark = feedback.getAssessmentMark();

    if (mark == null || mark.getAssessment() == null || mark.getStudent() == null) {
      JOptionPane.showMessageDialog(this, "Invalid feedback record", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    JPanel panel = new JPanel(new MigLayout("insets 10, wrap 1, gapy 10"));
    panel.setBackground(App.slate100);

    JLabel studentLabel = new JLabel("Student: " + mark.getStudent().getFirstName() + " " + mark.getStudent().getLastName());
    studentLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
    
    JLabel assessmentLabel = new JLabel("Assessment: " + mark.getAssessment().getType());
    assessmentLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
    
    JLabel marksLabel = new JLabel(String.format("Marks: %.2f / %.2f (%.2f%%)", 
        mark.getMarksObtained(), mark.getAssessment().getTotalMarks(), mark.getPercentage()));
    marksLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

    panel.add(studentLabel);
    panel.add(assessmentLabel);
    panel.add(marksLabel);

    JLabel feedbackLabel = new JLabel("Feedback:");
    JTextArea feedbackArea = new JTextArea(feedback.getContent(), 8, 40);
    feedbackArea.setLineWrap(true);
    feedbackArea.setWrapStyleWord(true);
    feedbackArea.setBackground(App.slate200);
    feedbackArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
    feedbackArea.setBorder(BorderFactory.createCompoundBorder(
        feedbackArea.getBorder(), 
        BorderFactory.createEmptyBorder(10, 15, 10, 15)));

    JScrollPane scrollPane = new JScrollPane(feedbackArea);

    panel.add(feedbackLabel);
    panel.add(scrollPane);

    int result = JOptionPane.showConfirmDialog(this, panel, "Edit Feedback", JOptionPane.OK_CANCEL_OPTION);
    
    if (result == JOptionPane.OK_OPTION) {
      feedback.setContent(feedbackArea.getText().trim());
      feedback.setUpdatedAt(LocalDateTime.now());
      Feedback.saveFeedback(feedback);
      JOptionPane.showMessageDialog(this, "Feedback updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
      refetchData(state);
    }
  }
}
