package com.apu_afs.Views;

public enum Pages {
  LOGIN("Login"), 
  DASHBOARD("Dashboard");

  private final String display;

  Pages(String display) {
    this.display = display;
  }

  public String getDisplay() {
    return display;
  }
}
