package com.apu_afs.Models.Enums;

public enum Mode {
  FULL_TIME("full", "Full Time"),
  PART_TIME("part", "Part Time");

  private final String value;
  private final String display;

  Mode(String value, String display) {
    this.value = value;
    this.display = display;
  }

  public String getValue() {
    return this.value;
  }

  public String getDisplay() {
    return this.display;
  }

  public static Mode fromValue(String value) {
    for (Mode mode : Mode.values()) {
      if (mode.value.equals(value)) {
        return mode;
      }
    }
    throw new IllegalArgumentException("Unknown mode value: " + value);
  }
}