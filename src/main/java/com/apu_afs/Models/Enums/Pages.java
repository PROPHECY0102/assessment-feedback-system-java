package com.apu_afs.Models.Enums;

public enum Pages {
  LOGIN("Login"), 
  DASHBOARD("Dashboard"),
  MANAGEUSERS("Manage Users"),
  USER("User Form"),
  MANAGEGRADES("Manage Grades"),
  MANAGECLASSES("Manage Classes"),
  MODULECLASS("Class Form"),
  PROFILE("Your Profile"),
  MANAGEMODULES("Manage Modules"),
  MODULE("Module Form"),
  ASSESSMENTS("Assessments"),
  ENTERMARKS("Enter Marks"),
  PROVIDEFEEDBACK("Provide Feedback"),
  STUDENTCOMMENTS("Students Comments"),
  CHECKRESULT("Result"),
  REGISTERCLASS("Register Classes"),
  COMMENTLECTURE("Student Comments"),
  FEEDBACKLECTURE("Student Feedback"),
  ASSIGNLECTURE("Assign Lecturers"),
  ANALYSEREPORT("Analyse Report");
      
  private final String display;

  Pages(String display) {
    this.display = display;
  }

  public String getDisplay() {
    return display;
  }
}
