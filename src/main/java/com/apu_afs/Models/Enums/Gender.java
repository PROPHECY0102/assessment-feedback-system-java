package com.apu_afs.Models.Enums;

public enum Gender {
  MALE("male", "Male"),
  FEMALE("female", "Female");

  private final String value;
  private final String display;

  Gender(String value, String display) {
    this.value = value;
    this.display = display;
  }

  public String getValue() {
    return this.value;
  }

  public String getDisplay() {
    return this.display;
  }

  public static Gender fromValue(String value) {
    for (Gender gender : Gender.values()) {
      if (gender.value.equals(value)) {
        return gender;
      }
    }
    throw new IllegalArgumentException("Unknown gender: " + value);
  }
}
