package com.apu_afs.Views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import net.miginfocom.swing.MigLayout;

import com.apu_afs.GlobalState;
import com.apu_afs.Helper;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Models.Enums.ReportType;
import com.apu_afs.Models.Module;
import com.apu_afs.Models.reports.AnalyseReport;
import com.apu_afs.Models.reports.Report;
import com.apu_afs.Views.components.HeaderPanel;
import com.apu_afs.Views.components.NavPanel;

public class AnalysedReportPage extends JPanel {

  private JTable table;
  private JLabel titleLabel;
  private String currentFilterId;
  private ReportType currentReportType;

  public AnalysedReportPage(Router router, GlobalState state) {

    super(new MigLayout(
      "fill, insets 0, gap 0",
      "[][grow]",
      "[][grow]"
    ));

    if (state.getCurrUser() == null) {
      SwingUtilities.invokeLater(() ->
        router.showView(Pages.LOGIN, state)
      );
      return;
    }

    HeaderPanel header = new HeaderPanel(router, state);
    NavPanel nav = new NavPanel(router, state);

    JPanel body = new JPanel(
      new MigLayout("insets 20, wrap 1, gapy 15", "[grow]", "[]")
    );
    body.setBackground(App.slate100);

    titleLabel = new JLabel("Choose Report Type");
    titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
    titleLabel.setHorizontalAlignment(JLabel.CENTER);

    JPanel btnRow = new JPanel(
      new MigLayout("insets 25 20, gapx 10, align center")
    );
    btnRow.setBackground(App.slate100);

    JSeparator divider = new JSeparator();
    divider.setForeground(new Color(148, 163, 184));
    divider.setBackground(new Color(148, 163, 184));
    divider.setOpaque(true);

    addReportButton(btnRow, state, ReportType.GRADE_DISTRIBUTION);
    addReportButton(btnRow, state, ReportType.MODULE_PERFORMANCE);
    addReportButton(btnRow, state, ReportType.LECTURER_WORKLOAD);
    addReportButton(btnRow, state, ReportType.CLASS_ENROLLMENT);
    addReportButton(btnRow, state, ReportType.FEEDBACK_SUMMARY);

    table = new JTable();
    table.setRowHeight(40);
    table.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
    table.setFillsViewportHeight(true);

    JTableHeader th = table.getTableHeader();
    th.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
    th.setBackground(new Color(51, 65, 85));
    th.setForeground(Color.WHITE);
    th.setPreferredSize(new Dimension(0, 45));

    table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int col
      ) {
        Component c = super.getTableCellRendererComponent(
          table, value, isSelected, hasFocus, row, col
        );
        if (!isSelected) {
          c.setBackground(row % 2 == 0
            ? Color.WHITE
            : new Color(248, 250, 252));
        }
        return c;
      }
    });

    JScrollPane scroll = new JScrollPane(table);

    body.add(btnRow, "growx, align center");
    body.add(divider, "growx, h 1!, gapy 10");
    body.add(titleLabel, "growx, align center, gapy 10");
    body.add(scroll, "grow");

    JButton printBtn = new JButton("Print");
    printBtn.setBackground(new Color(34, 197, 94));
    printBtn.setForeground(Color.WHITE);
    printBtn.setFocusable(false);
    printBtn.setPreferredSize(new Dimension(200, 45));
    printBtn.setIcon(
      Helper.iconResizer(
        new ImageIcon("assets/download-icon.png"),
        18,
        18
      )
    );

    printBtn.addActionListener(e -> {

      int selectedRow = table.getSelectedRow();

      if (selectedRow == -1) {
        JOptionPane.showMessageDialog(
          this,
          "Please select a row first!",
          "Information",
          JOptionPane.INFORMATION_MESSAGE
        );
        return;
      }

      StringBuilder msg = new StringBuilder();

      if (currentReportType == ReportType.GRADE_DISTRIBUTION
        && currentFilterId != null) {

        String[] parts = currentFilterId.split("\\|");
        String moduleId = parts[0];
        String lecturerId = parts[1];

        Module module =
          Module.getModuleByMatchingValues("id", moduleId);

        String moduleName =
          module != null ? module.getTitle() : moduleId;

        String lecturerName = "All Lecturers";

        if (!lecturerId.equals("all")) {
          var user = com.apu_afs.Models.User
            .getUserByMatchingValues("id", lecturerId);

          if (user != null) {
            lecturerName =
              user.getFirstName() + " " + user.getLastName();
          }
        }

        msg.append("Module: ").append(moduleName).append("\n");
        msg.append("Lecturer: ").append(lecturerName).append("\n\n");
      }

      // Row details
      for (int col = 0; col < table.getColumnCount(); col++) {
        msg.append(table.getColumnName(col))
          .append(": ")
          .append(table.getValueAt(selectedRow, col))
          .append("\n");
      }

      JOptionPane.showMessageDialog(
        this,
        msg.toString(),
        "Information",
        JOptionPane.INFORMATION_MESSAGE
      );
    });

    body.add(printBtn, "align right, gapy 10");

    add(header, "span, growx, wrap");
    add(nav, "growy");
    add(body, "grow");
  }

  private void addReportButton(
    JPanel panel, GlobalState state, ReportType type
  ) {
    JButton btn = new JButton(type.name().replace("_", " "));
    btn.setBackground(App.blue600);
    btn.setForeground(Color.WHITE);
    btn.setFocusable(false);
    btn.setPreferredSize(new Dimension(200, 45));
    btn.addActionListener(e -> loadReport(state, type));
    panel.add(btn);
  }

  private void loadReport(GlobalState state, ReportType type) {

    Report report = AnalyseReport.getReport(type);
    titleLabel.setText(report.getTitle());

    String filterId = null;

    if (type == ReportType.GRADE_DISTRIBUTION) {

      List<String> raw =
        com.apu_afs.Models.Data.fetch("data/modules.txt");

      List<Module> modules = new ArrayList<>();

      for (String r : raw) {
        Module m = new Module(List.of(r.split(", ")));
        if (m.getLeader() != null &&
          m.getLeader().getID()
            .equals(state.getCurrUser().getID())) {
          modules.add(m);
        }
      }

      JComboBox<Module> moduleBox =
        new JComboBox<>(modules.toArray(new Module[0]));

      JComboBox<Object> lecturerBox =
        new JComboBox<>();

      moduleBox.addActionListener(e -> {

        lecturerBox.removeAllItems();
        lecturerBox.addItem("All Lecturers");

        Module m = (Module) moduleBox.getSelectedItem();

        if (m != null) {

          List<com.apu_afs.Models.ModuleLecturer> assignments =
            com.apu_afs.Models.ModuleLecturer.fetchAll();

          assignments.stream()
            .filter(ml -> ml.getModuleID().equals(m.getID()))
            .forEach(ml -> {
              com.apu_afs.Models.User u =
                com.apu_afs.Models.User.getUserByMatchingValues(
                  "id",
                  ml.getLecturerID()
                );

              if (u instanceof com.apu_afs.Models.Lecturer) {
                lecturerBox.addItem(u);
              }
            });
        }
      });

      moduleBox.setSelectedIndex(0);

      JPanel panel = new JPanel(
        new MigLayout("wrap 2", "[][grow]")
      );
      panel.add(new JLabel("Module:"));
      panel.add(moduleBox);
      panel.add(new JLabel("Lecturer:"));
      panel.add(lecturerBox);

      int result = JOptionPane.showConfirmDialog(
        this,
        panel,
        "Grade Distribution Filter",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE
      );

      if (result != JOptionPane.OK_OPTION) return;

      Module m = (Module) moduleBox.getSelectedItem();
      Object l = lecturerBox.getSelectedItem();

      String lecturerId =
        (l instanceof String) ? "all"
          : ((com.apu_afs.Models.Lecturer) l).getID();

      filterId = m.getID() + "|" + lecturerId;
    }

    if (type == ReportType.FEEDBACK_SUMMARY) {
      String[] options = { "Student", "Lecturer" };
      int c = JOptionPane.showOptionDialog(
        this,
        "Which feedback would you like to see?",
        "Feedback Type",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[0]
      );
      if (c == -1) return;
      filterId = c == 0 ? "student" : "lecturer";
    }

    this.currentFilterId = filterId;
    this.currentReportType = type;

    table.setModel(new DefaultTableModel(
      report.generate(state, filterId),
      report.getColumns()
    ));
  }
}
