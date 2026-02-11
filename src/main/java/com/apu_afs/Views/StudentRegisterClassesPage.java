package com.apu_afs.Views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
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
import javax.swing.ImageIcon;

import net.miginfocom.swing.MigLayout;

import com.apu_afs.GlobalState;
import com.apu_afs.Helper;
import com.apu_afs.Models.Module;
import com.apu_afs.Models.ModuleClass;
import com.apu_afs.Models.Student;
import com.apu_afs.Models.StudentModule;
import com.apu_afs.Models.Enums.ModuleStatus;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.TableModels.ClassTableModel;
import com.apu_afs.Views.components.HeaderPanel;
import com.apu_afs.Views.components.NavPanel;

public class StudentRegisterClassesPage extends JPanel {

  HeaderPanel header;
  NavPanel nav;

  JPanel contentBody;
  JTable table;
  ClassTableModel tableModel;

  public StudentRegisterClassesPage(Router router, GlobalState state) {
    super(new MigLayout(
      "fill, insets 0, gap 0",
      "[][][grow]",
      "[][grow]"
    ));

    if (state.getCurrUser() == null || !(state.getCurrUser() instanceof Student)) {
      SwingUtilities.invokeLater(() -> {
        router.showView(Pages.LOGIN, state);
      });
      return;
    }

    Student currStudent = (Student) state.getCurrUser();

    header = new HeaderPanel(router, state);
    nav = new NavPanel(router, state);

    contentBody = new JPanel(new MigLayout("insets 20 20, wrap 1, gapy 15"));
    contentBody.setBackground(App.slate100);

    JPanel titleRow = new JPanel(new MigLayout("insets 0", "[grow][]"));
    titleRow.setBackground(App.slate100);

    JButton timetableBtn = new JButton("TimeTable");
    timetableBtn.setBackground(App.blue600);
    timetableBtn.setForeground(Color.WHITE);
    timetableBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
    timetableBtn.setFocusPainted(false);
    timetableBtn.setBorder(
      BorderFactory.createCompoundBorder(
        timetableBtn.getBorder(),
        BorderFactory.createEmptyBorder(5, 6, 5, 6)
      )
    );

    JButton enrollBtn = new JButton("Enroll");
    enrollBtn.setIcon(
      Helper.iconResizer(
        new ImageIcon("assets/add-icon.png"),
        18,
        18
      )
    );
    enrollBtn.setBackground(App.green600);
    enrollBtn.setForeground(Color.WHITE);
    enrollBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
    enrollBtn.setFocusPainted(false);
    enrollBtn.setBorder(
      BorderFactory.createCompoundBorder(
        enrollBtn.getBorder(),
        BorderFactory.createEmptyBorder(5, 6, 5, 6)
      )
    );

    JButton dropBtn = new JButton("Drop Out");
    dropBtn.setBackground(App.red600);
    dropBtn.setForeground(Color.WHITE);
    dropBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
    dropBtn.setFocusPainted(false);
    dropBtn.setBorder(
      BorderFactory.createCompoundBorder(
        dropBtn.getBorder(),
        BorderFactory.createEmptyBorder(5, 6, 5, 6)
      )
    );
    dropBtn.setIcon(
      Helper.iconResizer(
        new ImageIcon("assets/cancel-icon.png"),
        18,
        18
      )
    );

    JPanel btnGroup = new JPanel(new MigLayout("insets 0, gapx 6"));
    btnGroup.setBackground(App.slate100);
    btnGroup.add(timetableBtn);
    btnGroup.add(enrollBtn);
    btnGroup.add(dropBtn);

    titleRow.add(btnGroup, "align right");

    tableModel = new ClassTableModel(ModuleClass.fetchAll());
    table = new JTable(tableModel);

    table.setRowHeight(40);
    table.setFillsViewportHeight(true);
    table.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
    table.setGridColor(new Color(226, 232, 240));
    table.setShowGrid(true);

    JTableHeader headerTable = table.getTableHeader();
    headerTable.setBackground(new Color(51, 65, 85));
    headerTable.setForeground(Color.WHITE);
    headerTable.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
    headerTable.setPreferredSize(
      new Dimension(headerTable.getPreferredSize().width, 45)
    );

    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column
      ) {
        Component c = super.getTableCellRendererComponent(
          table, value, isSelected, hasFocus, row, column
        );

        if (!isSelected) {
          c.setBackground(
            row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252)
          );
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
      table.getColumnModel().getColumn(i).setCellRenderer(renderer);
    }

    JScrollPane scrollPane = new JScrollPane(table);

    enrollBtn.addActionListener(e -> {
      int row = table.getSelectedRow();
      if (row == -1) {
        JOptionPane.showMessageDialog(this, "Please select a class first");
        return;
      }

      ModuleClass mc = tableModel.getClassAt(row);

      StudentModule existing =
        StudentModule.getStudentModuleByCompositeKey(
          currStudent.getID(), mc.getModule().getID()
        );

      if (existing != null) {
        JOptionPane.showMessageDialog(this, "Already enrolled in this module");
        return;
      }

      HashMap<String, String> input = new HashMap<>();
      input.put("student", currStudent.getID());
      input.put("module", mc.getModule().getID());
      input.put("classCode", mc.getId());
      input.put("status", ModuleStatus.ACTIVE.getValue());
      input.put("enrolledAt", LocalDate.now().format(Helper.dateTimeFormatter));
      input.put("points", "0");

      new StudentModule(input).update();
      JOptionPane.showMessageDialog(this, "Enrollment successful");
    });

    dropBtn.addActionListener(evt -> {
      int selectedRow = table.getSelectedRow();

      if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a class first");
        return;
      }

      ModuleClass selectedClass = tableModel.getClassAt(selectedRow);

      StudentModule enrolled =
        StudentModule.getByStudentAndClassCode(
          currStudent.getID(),
          selectedClass.getClassCode()
        );

      if (enrolled == null) {
        JOptionPane.showMessageDialog(this, "You are not enrolled in this class");
        return;
      }

      int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to drop this class?",
        "Confirm Drop Out",
        JOptionPane.YES_NO_OPTION
      );

      if (confirm != JOptionPane.YES_OPTION) return;

      enrolled.delete();
      JOptionPane.showMessageDialog(this, "Successfully dropped the class");
    });

    timetableBtn.addActionListener(e -> showTimetable(currStudent));

    contentBody.add(titleRow, "growx");
    contentBody.add(scrollPane, "grow, width 100%");

    this.add(header, "span, growx, wrap");
    this.add(nav, "growy");
    this.add(contentBody, "span, grow");
  }

  private void showTimetable(Student student) {

    List<StudentModule> enrolled =
      StudentModule.getListOfStudentModulesByMatchingValues(
        "student", student.getID()
      );

    List<ModuleClass> classes = ModuleClass.fetchAll();
    Map<String, List<ModuleClass>> byDay = new LinkedHashMap<>();

    for (StudentModule sm : enrolled) {
      for (ModuleClass c : classes) {
        if (c.getId().equals(sm.getClassCode())) {
          byDay.computeIfAbsent(c.getDay(), k -> new ArrayList<>()).add(c);
        }
      }
    }

    StringBuilder sb = new StringBuilder();

    if (byDay.isEmpty()) {
      sb.append("No classes enrolled yet.");
    } else {
      for (Map.Entry<String, List<ModuleClass>> entry : byDay.entrySet()) {
        sb.append(entry.getKey()).append(":\n");

        for (ModuleClass c : entry.getValue()) {
          Module m = Module.getModuleByMatchingValues("id", c.getModule().getID());
          sb.append(" • ")
            .append(m != null ? m.getCode() : "Module")
            .append(" (")
            .append(c.getStartTime())
            .append(" - ")
            .append(c.getEndTime())
            .append(") @ ")
            .append(c.getClassroom())
            .append("\n");
        }
        sb.append("\n");
      }
    }

    JTextArea area = new JTextArea(sb.toString());
    area.setEditable(false);
    area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
    area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JScrollPane sp = new JScrollPane(area);
    sp.setPreferredSize(new Dimension(500, 400));

    JOptionPane.showMessageDialog(
      this,
      sp,
      "TimeTable",
      JOptionPane.INFORMATION_MESSAGE
    );
  }
}
