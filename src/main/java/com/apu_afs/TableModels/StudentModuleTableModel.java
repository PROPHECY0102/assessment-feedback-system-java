package com.apu_afs.TableModels;

import javax.swing.table.AbstractTableModel;
import com.apu_afs.Helper;
import com.apu_afs.Models.Student;
import com.apu_afs.Models.StudentModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentModuleTableModel extends AbstractTableModel {
  private List<Student> students;
  private String moduleID;
  
  private final String[] columnNames = {
    "ID", "Name", "Program", "Email", 
    "Enrollment Status", "Enrolled Date", "Points"
  };

  public StudentModuleTableModel(List<Student> students, String moduleID) {
    this.students = students != null ? 
      students.stream()
        .sorted((s1, s2) -> Integer.compare(
            Integer.parseInt(s1.getID()), Integer.parseInt(s2.getID())
        ))
        .collect(Collectors.toList()) 
      : new ArrayList<>();
      
      this.moduleID = moduleID;
  }

  @Override
  public int getRowCount() {
    return students.size();
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
    Student student = students.get(rowIndex);
    
    StudentModule enrollment = StudentModule.getStudentModuleByCompositeKey(student.getID(), moduleID);
    
    switch (columnIndex) {
      case 0: return student.getID();
      case 1: return student.getFirstName() + " " + student.getLastName();
      case 2: return student.getProgram();
      case 3: return student.getEmail();
      case 4: return enrollment != null ? enrollment.getStatus().getDisplay() : "Unregistered";
      case 5: return enrollment != null ? 
          enrollment.getEnrolledAt().format(Helper.dateTimeFormatter) : "-";
      case 6: return enrollment != null ? 
          String.format("%.2f", enrollment.getPoints()) : "-";
      default: return null;
    }
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  public Student getStudentAt(int rowIndex) {
    return students.get(rowIndex);
  }

  public void setData(List<Student> students, String moduleID, Set<String> statuses) {
    this.students = students != null ? 
      students.stream()
      .sorted((s1, s2) -> Integer.compare(
          Integer.parseInt(s1.getID()), Integer.parseInt(s2.getID())
      ))
      .collect(Collectors.toList()) 
      : new ArrayList<>();
      
    this.moduleID = moduleID;
    fireTableDataChanged();
  }

  public void clear() {
    students.clear();
    fireTableDataChanged();
  }
}