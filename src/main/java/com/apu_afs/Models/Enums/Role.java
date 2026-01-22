package com.apu_afs.Models.Enums;

public enum Role {
  ADMIN("admin", "Admin"),
  ACADEMIC_LEADER("academic", "Academic Leader"),
  LECTURER("lecturer", "Lecturer"),
  STUDENT("student", "Student");

  private final String value;
  private final String display;

  Role(String value, String display) {
    this.value = value;
    this.display = display;
  }

  public String getValue() {
    return this.value;
  }

  public String getDisplay() {
    return this.display;
  }

  public static Role fromValue(String value) {
    for (Role role : Role.values()) {
      if (role.value.equals(value)) {
        return role;
      }
    }
    throw new IllegalArgumentException("Unknown role value: " + value);
  }
}
