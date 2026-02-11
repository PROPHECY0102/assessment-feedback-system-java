package com.apu_afs.TableModels;

import javax.swing.table.AbstractTableModel;

import com.apu_afs.Helper;
import com.apu_afs.Models.ModuleClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleClassTableModel extends AbstractTableModel {
    private List<ModuleClass> moduleClasses;
    private final String[] columnNames = {
        "ID", "Class Code", "Day", "Start Time", "End Time", 
        "Classroom", "Module", "Lecturer"
    };

    public ModuleClassTableModel(List<ModuleClass> moduleClasses) {
        this.moduleClasses = moduleClasses != null ?
        moduleClasses.stream()
            .sorted((currModuleClass, nextModuleClass) -> Integer.compare(
                Integer.parseInt(currModuleClass.getId()), Integer.parseInt(nextModuleClass.getId())
            ))
            .collect(Collectors.toList()) 
        : new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return moduleClasses.size();
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
        ModuleClass moduleClass = moduleClasses.get(rowIndex);
        switch (columnIndex) {
            case 0: return moduleClass.getId();
            case 1: return moduleClass.getClassCode();
            case 2: return moduleClass.getDay();
            case 3: return moduleClass.getStartTime().format(Helper.timeFormatter);
            case 4: return moduleClass.getEndTime().format(Helper.timeFormatter);
            case 5: return moduleClass.getClassroom();
            case 6: return moduleClass.getModule().getCode();
            case 7: return moduleClass.getLecturer().getFirstName() + " " + moduleClass.getLecturer().getLastName();
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

    public ModuleClass getModuleAt(int rowIndex) {
        return moduleClasses.get(rowIndex);
    }

    public void setModuleClasses(List<ModuleClass> moduleClasses) {
       this.moduleClasses = moduleClasses != null ?
         moduleClasses.stream()
            .sorted((currModuleClass, nextModuleClass) -> Integer.compare(
                Integer.parseInt(currModuleClass.getId()), Integer.parseInt(nextModuleClass.getId())
            ))
            .collect(Collectors.toList()) 
        : new ArrayList<>();
        fireTableDataChanged();
    }

    public void clear() {
        moduleClasses.clear();
        fireTableDataChanged();
    }
}