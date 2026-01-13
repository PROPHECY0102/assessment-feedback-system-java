package com.apu_afs.Models;

import java.util.List;

import com.apu_afs.Views.Pages;

public class Admin extends User {
  
  public Admin(List<String> props) {
    super(props);
    this.navOptions.add(new NavOption(Pages.MANAGEUSERS));
  }

  @Override
  public void updateUser() {
    super.updateUser();
  }  
}
