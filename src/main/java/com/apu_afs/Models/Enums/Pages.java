package com.apu_afs.Models.Enums;

public enum Pages {
  LOGIN("Login"), 
  DASHBOARD("Dashboard"),
  MANAGEUSERS("Manage Users"),
  USER("User Form");

  private final String display;

  Pages(String display) {
    this.display = display;
  }

  public String getDisplay() {
    return display;
  }
}
