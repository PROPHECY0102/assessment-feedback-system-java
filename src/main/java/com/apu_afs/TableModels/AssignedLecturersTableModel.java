package com.apu_afs.TableModels;

import javax.swing.table.AbstractTableModel;
import java.util.List;

import com.apu_afs.Models.Lecturer;

public class AssignedLecturersTableModel extends AbstractTableModel {

  private List<Lecturer> lecturers;

  private final String[] columns = {
    "First Name", "Last Name", "Gender", "Email", "Phone"
  };

  public AssignedLecturersTableModel(List<Lecturer> lecturers) {
    this.lecturers = lecturers;
  }

  @Override
  public int getRowCount() {
    return lecturers.size();
  }

  @Override
  public int getColumnCount() {
    return columns.length;
  }

  @Override
  public String getColumnName(int col) {
    return columns[col];
  }

  @Override
  public Object getValueAt(int row, int col) {
    Lecturer l = lecturers.get(row);
    return switch (col) {
      case 0 -> l.getFirstName();
      case 1 -> l.getLastName();
      case 2 -> l.getGender();
      case 3 -> l.getEmail();
      case 4 -> l.getPhoneNumber();
      default -> null;
    };
  }

  public Lecturer getLecturerAt(int row) {
    return lecturers.get(row);
  }

  public void setLecturers(List<Lecturer> lecturers) {
    this.lecturers = lecturers;
    fireTableDataChanged();
  }
}
