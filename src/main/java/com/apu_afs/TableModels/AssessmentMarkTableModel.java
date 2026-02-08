package com.apu_afs.TableModels;

import javax.swing.table.AbstractTableModel;

import com.apu_afs.Models.AssessmentMark;
import com.apu_afs.Models.GradeRange;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssessmentMarkTableModel extends AbstractTableModel {
    private List<AssessmentMark> marks;
    private final String[] columnNames = {
      "ID", "Assessment", "Student", "Marks Obtained", "Percentage (%)", "Grade", "Recorded At"
    };

    public AssessmentMarkTableModel(List<AssessmentMark> marks) {
      this.marks = marks != null ?
      marks.stream()
        .sorted((currMark, nextMark) -> Integer.compare(
            Integer.parseInt(currMark.getID()), Integer.parseInt(nextMark.getID())
        ))
        .collect(Collectors.toList()) 
      : new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return marks.size();
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
      AssessmentMark mark = marks.get(rowIndex);
      switch (columnIndex) {
        case 0: return mark.getID();
        case 1: return mark.getAssessment() != null ? mark.getAssessment().getName() : "Not Available (N/A)";
        case 2: return mark.getStudent() != null ? (mark.getStudent().getFirstName() + " " + mark.getStudent().getLastName()) : "Not Available (N/A)";
        case 3: return String.valueOf(mark.getMarksObtained());
        case 4: return String.format("%.2f", mark.getPercentage());
        case 5: return GradeRange.getGradeForPercentage(mark.getPercentage());
        case 6: return mark.getRecordedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        default: return null;
      }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public AssessmentMark getMarkAt(int rowIndex) {
        return marks.get(rowIndex);
    }

    public void setMarks(List<AssessmentMark> marks) {
      this.marks = marks != null ?
      marks.stream()
        .sorted((currMark, nextMark) -> Integer.compare(
            Integer.parseInt(currMark.getID()), Integer.parseInt(nextMark.getID())
        ))
        .collect(Collectors.toList()) 
      : new ArrayList<>();
      fireTableDataChanged();
    }

    public void clear() {
      marks.clear();
      fireTableDataChanged();
    }
}
