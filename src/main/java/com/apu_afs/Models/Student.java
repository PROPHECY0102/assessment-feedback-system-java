package com.apu_afs.Models;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.apu_afs.Models.Enums.Mode;

public class Student extends User {
  private String program;
  private Mode mode;
  private double cgpa;
  private double creditHours;
  private LocalDate enrolledAt;

  public static final String filePath = "data/students.txt";
  
  public Student(List<String> props) {
    super(props);
  }

  public Student(HashMap<String, String> inputValues) {
    super(inputValues);
  }

  @Override
  public void updateUser() {
    super.updateUser();
  }  
}
