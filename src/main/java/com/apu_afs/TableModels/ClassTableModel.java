package com.apu_afs.TableModels;

import javax.swing.table.AbstractTableModel;
import java.util.List;

import com.apu_afs.Models.ModuleClass;
import com.apu_afs.Models.Module;


public class ClassTableModel extends AbstractTableModel {

  private List<ModuleClass> classes;

  private final String[] columnNames = {
    "Class Code", "Day", "Start Time", "End Time", "Classroom", "Module"
  };

  public ClassTableModel(List<ModuleClass> classes) {
    this.classes = classes;
  }

  @Override public int getRowCount() { return classes.size(); }
  @Override public int getColumnCount() { return columnNames.length; }
  @Override public String getColumnName(int c) { return columnNames[c]; }

  @Override
  public Object getValueAt(int r, int c) {
      ModuleClass mc = classes.get(r);

      return switch (c) {
          case 0 -> mc.getClassCode();
          case 1 -> mc.getDay();
          case 2 -> mc.getStartTime();
          case 3 -> mc.getEndTime();
          case 4 -> mc.getClassroom();
          case 5 -> {
              Module m = Module.getModuleByMatchingValues("id", mc.getModule().getID());
              yield m != null ? m.getTitle() : "-";
          }
          default -> null;
      };
  }



  public ModuleClass getClassAt(int row) {
    return classes.get(row);
  }

  public void removeRow(int row) {
  classes.remove(row);
  fireTableRowsDeleted(row, row);
  }

  public void setClasses(List<ModuleClass> newClasses) {
    this.classes = newClasses;
    fireTableDataChanged();
  }

  


}
