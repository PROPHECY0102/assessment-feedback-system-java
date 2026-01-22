package com.apu_afs.TableModels;

import javax.swing.table.AbstractTableModel;

import com.apu_afs.Helper;
import com.apu_afs.Models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserTableModel extends AbstractTableModel {
    private List<User> users;
    private final String[] columnNames = {
        "ID", "Username", "Password", "First Name", "Last Name", 
        "Gender", "D.O.B", "Email", "Phone Number", "Role"
    };

    public UserTableModel(List<User> users) {
        this.users = users != null ?
        users.stream()
            .sorted((currUser, nextUser) -> Integer.compare(
                Integer.parseInt(currUser.getID()), Integer.parseInt(nextUser.getID())
            ))
            .collect(Collectors.toList()) 
        : new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return users.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User user = users.get(rowIndex);
        switch (columnIndex) {
            case 0: return user.getID();
            case 1: return user.getUsername();
            case 2: return user.getPassword();
            case 3: return user.getFirstName();
            case 4: return user.getLastName();
            case 5: return user.getGender().getDisplay();
            case 6: return user.getDob().format(Helper.dateTimeFormatter);
            case 7: return user.getEmail();
            case 8: return user.getPhoneNumber();
            case 9: return user.getRole().getDisplay();
            default: return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 3) {
            return Character.class;
        }
        return String.class;
    }

    public User getUserAt(int rowIndex) {
        return users.get(rowIndex);
    }

    public void setUsers(List<User> users) {
       this.users = users != null ?
        users.stream()
            .sorted((currUser, nextUser) -> Integer.compare(
                Integer.parseInt(currUser.getID()), Integer.parseInt(nextUser.getID())
            ))
            .collect(Collectors.toList()) 
        : new ArrayList<>();
        fireTableDataChanged();
    }

    public void clear() {
        users.clear();
        fireTableDataChanged();
    }
}