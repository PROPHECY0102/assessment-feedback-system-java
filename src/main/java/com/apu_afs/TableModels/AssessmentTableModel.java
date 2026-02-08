package com.apu_afs.TableModels;

import javax.swing.table.AbstractTableModel;

import com.apu_afs.Helper;
import com.apu_afs.Models.Assessment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssessmentTableModel extends AbstractTableModel {
    private List<Assessment> assessments;
    private final String[] columnNames = {
        "ID", "Name", "Module", "Type", "Total Marks", "Created At"
    };

    public AssessmentTableModel(List<Assessment> assessments) {
      this.assessments = assessments != null ?
      assessments.stream()
        .sorted((currAssessment, nextAssessment) -> Integer.compare(
            Integer.parseInt(currAssessment.getID()), Integer.parseInt(nextAssessment.getID())
        ))
        .collect(Collectors.toList()) 
      : new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return assessments.size();
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
      Assessment assessment = assessments.get(rowIndex);
      switch (columnIndex) {
        case 0: return assessment.getID();
        case 1: return assessment.getName();
        case 2: return assessment.getModule() != null ? assessment.getModule().getTitle() : "Not Available (N/A)";
        case 3: return assessment.getType();
        case 4: return String.valueOf(assessment.getTotalMarks());
        case 5: return assessment.getCreatedAt().format(Helper.dateTimeFormatter);
        default: return null;
      }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public Assessment getAssessmentAt(int rowIndex) {
        return assessments.get(rowIndex);
    }

    public void setAssessments(List<Assessment> assessments) {
      this.assessments = assessments != null ?
      assessments.stream()
        .sorted((currAssessment, nextAssessment) -> Integer.compare(
            Integer.parseInt(currAssessment.getID()), Integer.parseInt(nextAssessment.getID())
        ))
        .collect(Collectors.toList()) 
      : new ArrayList<>();
      fireTableDataChanged();
    }

    public void clear() {
      assessments.clear();
      fireTableDataChanged();
    }
}
