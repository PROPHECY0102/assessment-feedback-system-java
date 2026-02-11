package com.apu_afs.Views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import java.util.Map;
import java.util.LinkedHashMap;


import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextArea;




import com.apu_afs.GlobalState;
import com.apu_afs.Models.ModuleResult;
import com.apu_afs.Models.Student;
import com.apu_afs.Models.StudentCheckResult;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Models.Enums.Role;
import com.apu_afs.TableModels.StudentResultTableModel;
import com.apu_afs.Views.components.HeaderPanel;
import com.apu_afs.Views.components.NavPanel;

import net.miginfocom.swing.MigLayout;

public class CheckResultPage extends JPanel {

  HeaderPanel header;
  NavPanel nav;

  JPanel contentBody;
  JPanel tableSection;
  JLabel rowCountLabel;
  JTable table;

  StudentResultTableModel tableModel;

  public CheckResultPage(Router router, GlobalState state) {
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

    if (state.getCurrUser().getRole() != Role.STUDENT) {
      SwingUtilities.invokeLater(() -> {
        router.showView(Pages.DASHBOARD, state);
      });
      return;
    }


    header = new HeaderPanel(router, state);
    nav = new NavPanel(router, state);


    contentBody = new JPanel(new MigLayout("insets 20 20, wrap 1, gapy 15"));
    contentBody.setBackground(App.slate100);



    Student student = (Student) state.getCurrUser();
    StudentCheckResult resultService = new StudentCheckResult(student);

    tableModel = new StudentResultTableModel(resultService.getResults());
    table = new JTable(tableModel);
    table.setEnabled(false);

    table.setRowHeight(40);
    table.setFillsViewportHeight(true);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

    JTableHeader tableHeader = table.getTableHeader();
    tableHeader.setBackground(new Color(51, 65, 85));
    tableHeader.setForeground(Color.WHITE);
    tableHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
    tableHeader.setPreferredSize(new Dimension(
      tableHeader.getPreferredSize().width, 45
    ));

    table.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
    table.setGridColor(new Color(226, 232, 240));
    table.setShowGrid(true);

    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(
        JTable table, Object value,
        boolean isSelected, boolean hasFocus,
        int row, int column
      ) {
        Component c = super.getTableCellRendererComponent(
          table, value, isSelected, hasFocus, row, column
        );

        if (!isSelected) {
          c.setBackground(row % 2 == 0
            ? Color.WHITE
            : new Color(248, 250, 252));
        }

        if (c instanceof JLabel) {
          ((JLabel) c).setBorder(
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
          );
        }

        return c;
      }
    };

    for (int i = 0; i < table.getColumnCount(); i++) {
      table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
    }

    JScrollPane scrollPane = new JScrollPane(table);

    rowCountLabel = new JLabel(
    "Total Modules: " + tableModel.getRowCount()
    );
    rowCountLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

    double cgpa = resultService.getCGPA();

    JLabel cgpaLabel = new JLabel(
    "CGPA: " + String.format("%.2f", cgpa)
    );

    cgpaLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));

    tableSection = new JPanel(new MigLayout("insets 5 10, wrap 1, gapy 5"));
    tableSection.setBackground(App.slate100);
    tableSection.add(rowCountLabel);
    tableSection.add(cgpaLabel);
    tableSection.add(scrollPane, "grow, width 100%");


    contentBody.add(tableSection, "grow, width 100%");

    JButton transcriptBtn = new JButton("TRANSCRIPT");
    transcriptBtn.setBackground(new Color(37, 99, 235)); 
    transcriptBtn.setForeground(Color.WHITE);
    transcriptBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
    transcriptBtn.setFocusPainted(false);
    transcriptBtn.setPreferredSize(new Dimension(160, 70));

    transcriptBtn.addActionListener(e -> {
    showTranscriptDialog(resultService);
    });


    JPanel buttonPanel = new JPanel(new MigLayout("insets 10 0 0 0", "[grow][]"));
    buttonPanel.setBackground(App.slate100);
    buttonPanel.add(transcriptBtn, "align right");
    contentBody.add(buttonPanel, "growx");



    this.add(header, "span, growx, wrap");
    this.add(nav, "growy");
    this.add(contentBody, "span, grow");
  }

  private void showTranscriptDialog(StudentCheckResult resultService) {

  JDialog dialog = new JDialog();
  dialog.setTitle("Academic Transcript");
  dialog.setSize(500, 400);
  dialog.setLocationRelativeTo(this);
  dialog.setModal(true);


  JTextArea textArea = new JTextArea();
  textArea.setEditable(false);
  textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
  textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

  StringBuilder sb = new StringBuilder();

  sb.append("ACADEMIC TRANSCRIPT\n");
  sb.append("====================\n\n");

  sb.append(String.format("%-25s %-10s %-5s\n",
      "Module", "Grade", "GPA"));
  sb.append("------------------------------------------------------------\n");


  for (ModuleResult r : resultService.getResults()) {
    sb.append(String.format("%-25s %-10s %.2f\n",
        r.getModuleName(),
        r.getGrade(),
        r.getGpa()
    ));
  }



  sb.append("--------------------------------------------------------------\n");


  Map<String, Integer> gradeCount = new LinkedHashMap<>();

  for (ModuleResult r : resultService.getResults()) {
    String grade = r.getGrade();
    gradeCount.put(grade, gradeCount.getOrDefault(grade, 0) + 1);
  }

  sb.append("\nGrade Summary:\n");

  for (Map.Entry<String, Integer> entry : gradeCount.entrySet()) {
    sb.append(String.format(
        "%-3s = %d%n",
        entry.getKey(),
        entry.getValue()
    ));
  }

  sb.append(String.format("CGPA: %.2f\n", resultService.getCGPA()));

  textArea.setText(sb.toString());

  dialog.add(new JScrollPane(textArea));
  dialog.setVisible(true);
}

}
