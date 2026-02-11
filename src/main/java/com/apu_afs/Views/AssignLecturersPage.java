package com.apu_afs.Views;
import com.apu_afs.Helper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.ImageIcon;



import net.miginfocom.swing.MigLayout;

import com.apu_afs.GlobalState;
import com.apu_afs.Models.AcademicLeader;
import com.apu_afs.Models.AssignLecturers;
import com.apu_afs.Models.Data;
import com.apu_afs.Models.Lecturer;
import com.apu_afs.Models.Module;
import com.apu_afs.Models.User;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.TableModels.AssignedLecturersTableModel;
import com.apu_afs.Views.components.HeaderPanel;
import com.apu_afs.Views.components.NavPanel;

public class AssignLecturersPage extends JPanel {

  private JComboBox<Module> moduleDropdown;
  private JTable table;
  private AssignedLecturersTableModel tableModel;
  private Module selectedModule;

  public AssignLecturersPage(Router router, GlobalState state) {

    super(new MigLayout(
      "fill, insets 0, gap 0",
      "[][grow]",
      "[][grow]"
    ));


    if (state.getCurrUser() == null ||
        !(state.getCurrUser() instanceof AcademicLeader)) {

      SwingUtilities.invokeLater(() ->
        router.showView(Pages.LOGIN, state)
      );
      return;
    }

    AcademicLeader leader = (AcademicLeader) state.getCurrUser();


    HeaderPanel header = new HeaderPanel(router, state);
    NavPanel nav = new NavPanel(router, state);


    JPanel body = new JPanel(
      new MigLayout("insets 20, wrap 1, gapy 15", "[grow]", "[]")
    );
    body.setBackground(App.slate100);


    JLabel title = new JLabel("Assign Lecturers to Module");
    title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));


    List<Module> modules =
      Module.getListOfModuleByMatchingValues(
        "leaderID", leader.getID()
      );

    selectedModule = modules.isEmpty() ? null : modules.get(0);

    moduleDropdown = new JComboBox<>(modules.toArray(new Module[0]));
    moduleDropdown.setPreferredSize(new Dimension(420, 36));

    JPanel moduleRow = new JPanel(
      new MigLayout("insets 0", "[][grow]")
    );
    moduleRow.setBackground(App.slate100);
    moduleRow.add(new JLabel("Module:"));
    moduleRow.add(moduleDropdown, "growx");


    JButton addBtn = new JButton("Add Lecturer");
    JButton removeBtn = new JButton("Remove Lecturer");

    addBtn.setForeground(Color.WHITE);
    addBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    addBtn.setBackground(App.green600);
    addBtn.setFocusable(false);
    addBtn.setBorder(BorderFactory.createCompoundBorder(
      addBtn.getBorder(),
      BorderFactory.createEmptyBorder(5, 6, 5, 6)
    ));

    removeBtn.setForeground(Color.WHITE);
    removeBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    removeBtn.setBackground(App.red600);
    removeBtn.setFocusable(false);
    removeBtn.setBorder(BorderFactory.createCompoundBorder(
      removeBtn.getBorder(),
      BorderFactory.createEmptyBorder(5, 6, 5, 6)
    ));

    JPanel btnRow = new JPanel(
    new MigLayout("insets 0, gapx 5", "[grow][]")
    );
    btnRow.setBackground(App.slate100);

    btnRow.add(new JLabel(), "grow"); 
    btnRow.add(addBtn);
    btnRow.add(removeBtn);

    addBtn.setIcon(
      Helper.iconResizer(
        new ImageIcon("assets/add-icon.png"),
        18,
        18
      )
    );

    removeBtn.setText("Remove Lecturer");
    removeBtn.setIcon(
      Helper.iconResizer(
        new ImageIcon("assets/cancel-icon.png"),
        18,
        18
      )
    );




    tableModel = new AssignedLecturersTableModel(
      selectedModule == null
        ? List.of()
        : AssignLecturers.getLecturersForModule(selectedModule)
    );

    table = new JTable(tableModel);
    table.setFillsViewportHeight(true);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    table.setRowHeight(40);
    table.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
    table.setBackground(Color.WHITE);
    table.setForeground(Color.BLACK);
    table.setGridColor(new Color(226, 232, 240));
    table.setShowGrid(true);
    table.setIntercellSpacing(new Dimension(1, 1));

    JTableHeader tableHeader = table.getTableHeader();
    tableHeader.setBackground(new Color(51, 65, 85));
    tableHeader.setForeground(Color.WHITE);
    tableHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
    tableHeader.setPreferredSize(
      new Dimension(tableHeader.getPreferredSize().width, 45)
    );

    DefaultTableCellRenderer renderer =
      new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(
          JTable table, Object value, boolean isSelected,
          boolean hasFocus, int row, int column
        ) {
          Component c = super.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column
          );

          if (c instanceof JLabel) {
            ((JLabel) c).setBorder(
              BorderFactory.createEmptyBorder(8, 12, 8, 12)
            );
          }

          if (!isSelected) {
            c.setBackground(
              row % 2 == 0
                ? Color.WHITE
                : new Color(248, 250, 252)
            );
          }

          return c;
        }
      };

    for (int i = 0; i < table.getColumnCount(); i++) {
      table.getColumnModel()
        .getColumn(i)
        .setCellRenderer(renderer);
    }

    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBackground(App.slate100);
    scrollPane.setBorder(
      BorderFactory.createLineBorder(new Color(203, 213, 225))
    );


    moduleDropdown.addActionListener(e -> {
      selectedModule = (Module) moduleDropdown.getSelectedItem();
      tableModel.setLecturers(
        selectedModule == null
          ? List.of()
          : AssignLecturers.getLecturersForModule(selectedModule)
      );
    });

    addBtn.addActionListener(e -> {
      if (selectedModule == null) return;

      Lecturer lecturer = chooseLecturerForModule(selectedModule);
      if (lecturer == null) return;

      AssignLecturers.assign(selectedModule, lecturer);

      JOptionPane.showMessageDialog(
        this,
        "Lecturer " + lecturer.getFirstName() + " "
          + lecturer.getLastName()
          + " successfully added.",
        "Success",
        JOptionPane.INFORMATION_MESSAGE
      );

      tableModel.setLecturers(
        AssignLecturers.getLecturersForModule(selectedModule)
      );
    });

    removeBtn.addActionListener(e -> {
      int row = table.getSelectedRow();
      if (row == -1) {
        JOptionPane.showMessageDialog(
          this,
          "Please select a lecturer to remove.",
          "No Selection",
          JOptionPane.WARNING_MESSAGE
        );
        return;
      }

      Lecturer lecturer = tableModel.getLecturerAt(row);

      int confirm = JOptionPane.showConfirmDialog(
        this,
        "Removing lecturer\n\n"
        + lecturer.getFirstName() + " " + lecturer.getLastName()
        + "\n\nModule: " + selectedModule.getCode(),
        "Confirm Removal",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE
      );

      if (confirm != JOptionPane.YES_OPTION) return;

      AssignLecturers.remove(selectedModule, lecturer);

      JOptionPane.showMessageDialog(
        this,
        "Lecturer removed successfully.",
        "Success",
        JOptionPane.INFORMATION_MESSAGE
      );

      tableModel.setLecturers(
        AssignLecturers.getLecturersForModule(selectedModule)
      );
    });


    body.add(title);
    body.add(moduleRow, "growx");
    body.add(btnRow, "growx");
    body.add(scrollPane, "grow, push");

    add(header, "span, growx, wrap");
    add(nav, "growy");
    add(body, "grow");
  }


  private Lecturer chooseLecturerForModule(Module module) {

    List<Lecturer> assigned =
      AssignLecturers.getLecturersForModule(module);

    List<Lecturer> available =
      Data.fetch("data/users.txt").stream()
        .map(row -> row.split(", "))
        .map(p -> User.getUserByMatchingValues("id", p[0]))
        .filter(u -> u instanceof Lecturer)
        .map(u -> (Lecturer) u)
        .filter(l -> !assigned.contains(l))
        .collect(Collectors.toList());

    if (available.isEmpty()) {
      JOptionPane.showMessageDialog(
        this,
        "All lecturers are already assigned to this module."
      );
      return null;
    }

    return (Lecturer) JOptionPane.showInputDialog(
      this,
      "Select Lecturer:",
      "Add Lecturer",
      JOptionPane.PLAIN_MESSAGE,
      null,
      available.toArray(),
      null
    );
  }
}
