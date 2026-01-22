package com.apu_afs.Views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.apu_afs.GlobalState;
import com.apu_afs.Helper;
import com.apu_afs.Models.User;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Models.Enums.Role;
import com.apu_afs.TableModels.UserTableModel;
import com.apu_afs.Views.components.HeaderPanel;
import com.apu_afs.Views.components.NavPanel;
import com.apu_afs.Views.components.TextField;

import net.miginfocom.swing.MigLayout;

public class ManageUsersPage extends JPanel {

  HeaderPanel header;
  NavPanel nav;

  JPanel contentBody;

  JPanel searchSection;
  TextField searchField;
  JButton searchClearBtn;
  JButton searchBtn;

  JPanel filterOptionsContainer;
  JCheckBox filterAdminCheckButton;
  JCheckBox filterAcademicCheckButton;
  JCheckBox filterLecturerCheckButton;
  JCheckBox filterStudentCheckButton;

  JPanel searchFilterGroup;

  JPanel actionBtnsContainer;
  JButton addBtn;
  JButton editBtn;

  JPanel searchFilterActionRow;

  JPanel tableSection;
  JLabel rowCountLabel;
  JTable table;

  List<User> users;
  UserTableModel userTableModel;

  String searchInput;
  Set<String> roleConditions;
 
  private static final String[] allowedRoles = {"admin"};
  private static final String dataContext = "Users";
  
  public ManageUsersPage(Router router, GlobalState state) {
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

    contentBody = new JPanel(new MigLayout("insets 20 20, wrap 1, gapy 10"));
    contentBody.setBackground(App.slate100);

    searchInput = state.getUserSearch() != null ? state.getUserSearch() : "";
    roleConditions = state.getUserRoleConditions() != null ? 
        state.getUserRoleConditions() : 
        Arrays.stream(Role.values()).map(Role::getValue).collect(Collectors.toSet());

    searchField = new TextField("Search " + dataContext + "...");
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
        router.showView(Pages.MANAGEUSERS, state);
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

    filterAdminCheckButton = new JCheckBox();
    filterAdminCheckButton.setText("Admin");
    filterAdminCheckButton.setSelected(roleConditions.contains("admin"));

    filterAcademicCheckButton = new JCheckBox();
    filterAcademicCheckButton.setText("Academic Leader");
    filterAcademicCheckButton.setSelected(roleConditions.contains("academic"));

    filterLecturerCheckButton = new JCheckBox();
    filterLecturerCheckButton.setText("Lecturer");
    filterLecturerCheckButton.setSelected(roleConditions.contains("lecturer"));

    filterStudentCheckButton = new JCheckBox();
    filterStudentCheckButton.setText("Student");
    filterStudentCheckButton.setSelected(roleConditions.contains("student"));

    Map<String, JCheckBox> filterCheckBoxes = Map.ofEntries(
        Map.entry("admin", filterAdminCheckButton),
        Map.entry("academic", filterAcademicCheckButton),
        Map.entry("lecturer", filterLecturerCheckButton),
        Map.entry("student", filterStudentCheckButton)
    );

    for (Map.Entry<String, JCheckBox> entry : filterCheckBoxes.entrySet()) {
        String roleKey = entry.getKey();
        JCheckBox checkBox = entry.getValue();
        
        checkBox.addActionListener(e -> {
            if (checkBox.isSelected()) {
                roleConditions.add(roleKey);
            } else {
                roleConditions.remove(roleKey);
            }

            state.setUserSearch(searchField.getText());
            state.setUserRoleConditions(roleConditions);
            router.showView(Pages.MANAGEUSERS, state);
        });
    }

    searchBtn.addActionListener(e -> {
        state.setUserSearch(searchField.getText());

        roleConditions.clear();
        for (String key : filterCheckBoxes.keySet()) {
            if (filterCheckBoxes.get(key).isSelected()) {
                roleConditions.add(key);
            }
        }

        state.setUserRoleConditions(roleConditions);
        router.showView(Pages.MANAGEUSERS, state);
    });

    filterOptionsContainer = new JPanel(new MigLayout("insets 0, gapx 5"));
    filterOptionsContainer.setBackground(App.slate100);
    filterOptionsContainer.add(filterAdminCheckButton);
    filterOptionsContainer.add(filterAcademicCheckButton);
    filterOptionsContainer.add(filterLecturerCheckButton);
    filterOptionsContainer.add(filterStudentCheckButton);

    searchFilterGroup = new JPanel(new MigLayout("insets 0, wrap 1"));
    searchFilterGroup.setBackground(App.slate100);
    searchFilterGroup.add(searchSection);
    searchFilterGroup.add(filterOptionsContainer);

    addBtn = new JButton();
    addBtn.setText("Add " + dataContext);
    addBtn.setIcon(Helper.iconResizer(new ImageIcon("assets/add-icon.png"), 18, 18));
    addBtn.setForeground(Color.WHITE);
    addBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    addBtn.setBackground(App.green600);
    addBtn.setFocusable(false);
    addBtn.setBorder(BorderFactory.createCompoundBorder(addBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    addBtn.addActionListener(e -> {
        router.showView(Pages.USER, state);
    });

    editBtn = new JButton();
    editBtn.setText("Edit Selected " + dataContext.substring(0, dataContext.length() - 1));
    editBtn.setIcon(Helper.iconResizer(new ImageIcon("assets/edit-icon.png"), 18, 18));
    editBtn.setForeground(Color.WHITE);
    editBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    editBtn.setBackground(App.orange600);
    editBtn.setFocusable(false);
    editBtn.setBorder(BorderFactory.createCompoundBorder(editBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    editBtn.addActionListener(e -> {
        if (table.getSelectedRow() != -1) {
            state.setSelectedUserID(String.valueOf(table.getValueAt(table.getSelectedRow(), 0)));
            router.showView(Pages.USER, state);
        } else {
            JOptionPane.showMessageDialog(router, "No user has been selected, cannot edit user!", "Error: Edit Selected User", JOptionPane.ERROR_MESSAGE);
        }
    });

    actionBtnsContainer = new JPanel(new MigLayout("insets 0, gapx 5"));
    actionBtnsContainer.setBackground(App.slate100);
    actionBtnsContainer.add(addBtn);
    actionBtnsContainer.add(editBtn);

    searchFilterActionRow = new JPanel(new MigLayout("insets 5 10"));
    searchFilterActionRow.setBackground(App.slate100);
    searchFilterActionRow.add(searchFilterGroup);
    searchFilterActionRow.add(actionBtnsContainer, "push, align right");
    
    users = User.fetchUsers(searchInput, roleConditions);
    userTableModel = new UserTableModel(users);

    rowCountLabel = new JLabel();
    rowCountLabel.setText("Total " + dataContext + " queried: " + String.valueOf(userTableModel.getRowCount()));
    rowCountLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

    table = new JTable(userTableModel);
    table.setPreferredScrollableViewportSize(new Dimension(table.getWidth(), 800));
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

    tableSection = new JPanel(new MigLayout("insets 5 10, wrap 1, gapy 5"));
    tableSection.setBackground(App.slate100);
    tableSection.add(rowCountLabel);
    tableSection.add(scrollPane, "grow, width 100%");
    contentBody.add(searchFilterActionRow, "growx, width 100%");
    contentBody.add(tableSection, "grow, width 100%");
    
    this.add(header, "span, growx, wrap");
    this.add(nav, "growy");
    this.add(contentBody, "span, grow");

    state.clearState();
  }
}
