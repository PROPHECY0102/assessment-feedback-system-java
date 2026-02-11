package com.apu_afs.TableModels;

import javax.swing.table.AbstractTableModel;

import com.apu_afs.Helper;
import com.apu_afs.Models.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleTableModel extends AbstractTableModel {
    private List<Module> modules;
    private final String[] columnNames = {
        "ID", "Code", "Title", "Credits", "Created At",
    };

    public ModuleTableModel(List<Module> modules) {
      this.modules = modules != null ?
      modules.stream()
        .sorted((currModule, nextModule) -> Integer.compare(
            Integer.parseInt(currModule.getID()), Integer.parseInt(nextModule.getID())
        ))
        .collect(Collectors.toList()) 
      : new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return modules.size();
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
      Module module = modules.get(rowIndex);

      switch (columnIndex) {
        case 0: return module.getID();
        case 1: return module.getCode();
        case 2: return module.getTitle();
        case 3: return module.getCreditHours(); 
        case 4: return module.getCreatedAt()
                            .format(Helper.dateTimeFormatter); 
        default: return null;
      }
    }


    @Override
    public Class<?> getColumnClass(int columnIndex) {
      return switch (columnIndex) {
        case 3 -> Double.class;   
        default -> String.class;
      };
    }


    public Module getModuleAt(int rowIndex) {
        return modules.get(rowIndex);
    }

    public void setModules(List<Module> modules) {
      this.modules = modules != null ?
      modules.stream()
        .sorted((currModule, nextModule) -> Integer.compare(
            Integer.parseInt(currModule.getID()), Integer.parseInt(nextModule.getID())
        ))
        .collect(Collectors.toList()) 
      : new ArrayList<>();
      fireTableDataChanged();
    }

    public void clear() {
      modules.clear();
      fireTableDataChanged();
    }
}
