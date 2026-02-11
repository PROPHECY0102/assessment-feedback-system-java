package com.apu_afs.Models;

public class ModuleResult {
  private final String moduleName;
  private final String grade;
  private final double gpa;

  public ModuleResult(String moduleName, String grade, double gpa) {
    this.moduleName = moduleName;
    this.grade = grade;
    this.gpa = gpa;
  }

  public String getModuleName() { return moduleName; }
  public String getGrade() { return grade; }
  public double getGpa() { return gpa; }
}
