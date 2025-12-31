package com.apu_afs;

import java.util.ArrayList;
import java.util.Arrays;

import com.apu_afs.Models.Data;
import com.apu_afs.Models.User;

public class GlobalState {
  private User currUser; // nullable must check before using
  private boolean staySignedIn;

  private static final String filepath = "data/state.txt";

  public GlobalState() {
    ArrayList<String> stateData = Data.fetch(GlobalState.filepath);
    ArrayList<String> stateDataProps = new ArrayList<String>(Arrays.asList(stateData.get(0).split(", ")));
    if (stateDataProps.get(0).equals("guest")) {
      this.currUser = null;
      this.staySignedIn = false;
    }

    this.currUser = User.getUserByUsername(stateDataProps.get(0));
    this.staySignedIn = Boolean.parseBoolean(stateDataProps.get(1));
  }

  public User getCurrUser() {
    return this.currUser;
  }
  
  public boolean getStaySignedIn() {
    return this.staySignedIn;
  }

  public void setCurrUser(User currUser) {
    this.currUser = currUser;
    saveState();
  }

  public void setStaySignedIn(boolean staySignedIn) {
    this.staySignedIn = staySignedIn;
    saveState();
  }

  public void saveState() {
    ArrayList<String> updatedState = new ArrayList<>();
    updatedState.add(this.currUser == null ? "guest" : this.currUser.getUsername());
    updatedState.add(String.valueOf(this.staySignedIn));

    Data.save(filepath, String.join(", ", updatedState));
  }
}
