package com.apu_afs.Models;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class Lecturer extends User {
  private String faculty;
  private LocalDate employedAt;
  
  public Lecturer(List<String> props) {
    super(props);
  }

  public Lecturer(HashMap<String, String> inputValues) {
    super(inputValues);
  }

  @Override
  public void updateUser() {
    super.updateUser();
  }  
}
