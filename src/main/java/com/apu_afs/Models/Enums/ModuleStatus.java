package com.apu_afs.Models.Enums;

public enum ModuleStatus {
  ACTIVE("active", "Active"),
  COMPLETED("completed", "Completed"),
  SUSPENDED("suspended", "Suspended"),
  DROPPED("dropped", "Dropped");

  private final String value;
  private final String display;

  ModuleStatus(String value, String display) {
    this.value = value;
    this.display = display;
  }

  public String getValue() {
    return this.value;
  }

  public String getDisplay() {
    return this.display;
  }

  public static ModuleStatus fromValue(String value) {
    for (ModuleStatus moduleStatus : ModuleStatus.values()) {
      if (moduleStatus.value.equals(value)) {
        return moduleStatus;
      }
    }
    throw new IllegalArgumentException("Unknown Module Status value: " + value);
  }
}
