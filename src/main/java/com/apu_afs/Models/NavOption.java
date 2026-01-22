package com.apu_afs.Models;

import com.apu_afs.Models.Enums.Pages;

public class NavOption {
  private Pages page;
  private String iconSrc;
  private String displayName;

  public NavOption(Pages page) {
    this.page = page;
    this.iconSrc = "assets/nav-" + page.getDisplay().replaceAll("\\s+", "").toLowerCase() + ".png";
    this.displayName = page.getDisplay();
  }

  public NavOption(Pages page, String iconSrc, String displayName) {
    this.page = page;
    this.iconSrc = iconSrc;
    this.displayName = displayName;
  }

  public Pages getPage() {
    return this.page;
  }

  public String getIconSrc() {
    return this.iconSrc;
  }

  public String getDisplayName() {
    return this.displayName;
  }

  public void setPage(Pages page) {
    this.page = page;
  }
  public void setIconSrc(String iconSrc) {
    this.iconSrc = iconSrc;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
}
