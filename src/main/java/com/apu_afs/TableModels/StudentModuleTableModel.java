package com.apu_afs.TableModels;

import javax.swing.table.AbstractTableModel;
import com.apu_afs.Helper;
import com.apu_afs.Models.Student;
import com.apu_afs.Models.StudentModule;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentModuleTableModel extends AbstractTableModel {
  private List<Student> students;
  private List<StudentModule> studentModules;
  
  private final String[] columnNames = {
    "Student ID", "Name", "Program", "CGPA", 
    "Enrollment Status", "Enrolled Date", "Points"
  };

  public StudentModuleTableModel(List<Student> students, List<StudentModule> studentModules) {
    this.students = students != null ? 
      students.stream()
        .sorted((s1, s2) -> Integer.compare(
            Integer.parseInt(s1.getID()), Integer.parseInt(s2.getID())
        ))
        .collect(Collectors.toList()) 
      : new ArrayList<>();
      
      this.studentModules = studentModules != null ? studentModules : new ArrayList<>();
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
    
    // Find if this student is enrolled in the module (LEFT JOIN simulation)
    StudentModule enrollment = findStudentModuleEnrollment(student.getID());
    
    switch (columnIndex) {
      case 0: return student.getID();
      case 1: return student.getFirstName() + " " + student.getLastName();
      case 2: return student.getProgram();
      case 3: return String.format("%.2f", student.getCgpa());
      case 4: return enrollment != null ? enrollment.getStatus().getDisplay() : "Not Enrolled";
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

  private StudentModule findStudentModuleEnrollment(String studentId) {
    return studentModules.stream()
      .filter(sm -> sm.getStudent().getID().equals(studentId))
      .findFirst()
      .orElse(null);
  }

  public Student getStudentAt(int rowIndex) {
    return students.get(rowIndex);
  }

  public StudentModule getEnrollmentAt(int rowIndex) {
    Student student = students.get(rowIndex);
    return findStudentModuleEnrollment(student.getID());
  }
  
  public boolean isStudentEnrolled(int rowIndex) {
    return getEnrollmentAt(rowIndex) != null;
  }

  public void setData(List<Student> students, List<StudentModule> studentModules) {
    this.students = students != null ? 
      students.stream()
      .sorted((s1, s2) -> Integer.compare(
          Integer.parseInt(s1.getID()), Integer.parseInt(s2.getID())
      ))
      .collect(Collectors.toList()) 
      : new ArrayList<>();
      
    this.studentModules = studentModules != null ? studentModules : new ArrayList<>();
    fireTableDataChanged();
  }

  public void clear() {
    students.clear();
    studentModules.clear();
    fireTableDataChanged();
  }
}