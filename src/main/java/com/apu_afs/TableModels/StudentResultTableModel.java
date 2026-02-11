package com.apu_afs.TableModels;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

import com.apu_afs.Models.ModuleResult;

public class StudentResultTableModel extends AbstractTableModel {

  private List<ModuleResult> results;

  private final String[] columnNames = {
    "Module",
    "Grade",
    "GPA"
  };

  public StudentResultTableModel(List<ModuleResult> results) {
    this.results = results != null ? results : new ArrayList<>();
  }

  @Override
  public int getRowCount() {
    return results.size();
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
    ModuleResult r = results.get(rowIndex);

    switch (columnIndex) {
      case 0:
        return r.getModuleName();
      case 1:
        return r.getGrade();
      case 2:
        return String.format("%.2f", r.getGpa());
      default:
        return "";
    }
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  public void setResults(List<ModuleResult> results) {
    this.results = results != null ? results : new ArrayList<>();
    fireTableDataChanged();
  }

  public List<ModuleResult> getResults() {
    return results;
  }
}
