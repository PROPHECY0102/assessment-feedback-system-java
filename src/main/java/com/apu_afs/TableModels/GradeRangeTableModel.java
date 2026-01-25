package com.apu_afs.TableModels;

import javax.swing.table.AbstractTableModel;

import com.apu_afs.Models.GradeRange;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GradeRangeTableModel extends AbstractTableModel {
    private List<GradeRange> gradeRanges;
    private final String[] columnNames = {
      "Grade", "Points", "Range", "Description"
    };

    public GradeRangeTableModel(List<GradeRange> gradeRanges) {
        this.gradeRanges = gradeRanges != null ?
        gradeRanges.stream()
            .sorted((currGradeRange, nextGradeRange) -> Double.compare(
              nextGradeRange.getPoints(), currGradeRange.getPoints()
            ))
            .collect(Collectors.toList()) 
        : new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return gradeRanges.size();
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
        GradeRange gradeRange = gradeRanges.get(rowIndex);
        switch (columnIndex) {
            case 0: return gradeRange.getGrade();
            case 1: return gradeRange.getPoints();
            case 2: return String.valueOf(gradeRange.getMin()) + "-" + String.valueOf(gradeRange.getMax());
            case 3: return gradeRange.getDescription();
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

    public GradeRange getGradeRangeAt(int rowIndex) {
        return gradeRanges.get(rowIndex);
    }

    public void setGradeRanges(List<GradeRange> gradeRanges) {
       this.gradeRanges = gradeRanges != null ?
        gradeRanges.stream()
            .sorted((currGradeRange, nextGradeRange) -> Double.compare(
              nextGradeRange.getPoints(), currGradeRange.getPoints()
            ))
            .collect(Collectors.toList()) 
        : new ArrayList<>();
        fireTableDataChanged();
    }

    public void clear() {
        gradeRanges.clear();
        fireTableDataChanged();
    }
}