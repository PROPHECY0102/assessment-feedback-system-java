package com.apu_afs.Models.Enums;

public enum ReportType {

  GRADE_DISTRIBUTION("grade_distribution", "Grade Distribution Report"),
  MODULE_PERFORMANCE("module_performance", "Module Performance Report"),
  LECTURER_WORKLOAD("lecturer_workload", "Lecturer Workload Report"),
  CLASS_ENROLLMENT("class_enrollment", "Class Enrollment Report"),
  FEEDBACK_SUMMARY("feedback_summary", "Feedback Summary Report");

  private final String value;
  private final String display;

  ReportType(String value, String display) {
    this.value = value;
    this.display = display;
  }

  public String getValue() {
    return value;
  }

  public String getDisplay() {
    return display;
  }

  @Override
  public String toString() {
    return display;
  }
}
