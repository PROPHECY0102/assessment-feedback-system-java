package com.apu_afs.Models.Enums;

public enum EmploymentType {
  FULL_TIME("full", "Full Time"),
  PART_TIME("part", "Part Time"),
  CONTRACT("contract", "Contract");

  private final String value;
  private final String display;

  EmploymentType(String value, String display) {
    this.value = value;
    this.display = display;
  }

  public String getValue() {
    return this.value;
  }

  public String getDisplay() {
    return this.display;
  }

  public static EmploymentType fromValue(String value) {
    for (EmploymentType employmentType : EmploymentType.values()) {
      if (employmentType.value.equals(value)) {
        return employmentType;
      }
    }
    throw new IllegalArgumentException("Unknown Employment Type value: " + value);
  }
}
