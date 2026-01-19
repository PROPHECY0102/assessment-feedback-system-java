package com.apu_afs.Models;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class Student extends User {
  private String program;
  private String mode;
  private double cgpa;
  private LocalDate enrolledAt;
  
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
