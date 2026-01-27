package com.apu_afs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.apu_afs.Models.Data;
import com.apu_afs.Models.User;

public class GlobalState {
  // Persistent State
  private User currUser; // nullable must check before using
  private boolean staySignedIn;
  
  // Volatile State 
  // use for user form Pages.USER when editing selected users form Pages.MANAGEUSERS
  // no need to save this variable for persistent use
  private String selectedUserID;

  // use for searching and filtering
  private String userSearch;
  private Set<String> userRoleConditions;

  private static final String filepath = "data/state.txt";

  public GlobalState() {
    List<String> stateData = Data.fetch(GlobalState.filepath);

    List<String> stateDataProps = List.of(stateData.get(0).split(", "));
    if (stateDataProps.get(0).equals("guest")) {
      this.currUser = null;
      this.staySignedIn = false;
    }

    this.currUser = User.getUserByMatchingValues("id", stateDataProps.get(0).trim());
    this.staySignedIn = Boolean.parseBoolean(stateDataProps.get(1).trim());
  }

  public User getCurrUser() {
    return this.currUser;
  }
  
  public boolean getStaySignedIn() {
    return this.staySignedIn;
  }

  public String getSelectedUserID() {
    return this.selectedUserID;
  }

  public String getUserSearch() {
    return this.userSearch;
  }

  public Set<String> getUserRoleConditions() {
    return this.userRoleConditions;
  }

  public void setCurrUser(User currUser) {
    this.currUser = currUser;
    saveState();
  }

  public void setStaySignedIn(boolean staySignedIn) {
    this.staySignedIn = staySignedIn;
    saveState();
  }

  public void setSelectedUserID(String selectedUserID) {
    this.selectedUserID = selectedUserID;
  }

  public void setUserSearch(String userSearch) {
    this.userSearch = userSearch;
  }

  public void setUserRoleConditions(Set<String> userRoleConditions) {
    this.userRoleConditions = userRoleConditions;
  }

  public void saveState() {
    List<String> updatedState = new ArrayList<>();
    updatedState.add(this.currUser == null ? "guest" : this.currUser.getID());
    updatedState.add(String.valueOf(this.staySignedIn));

    Data.save(filepath, String.join(", ", updatedState));
  }

  // When reaching new page after using state value remember to clear except login sessions
  public void clearState() {
    this.setSelectedUserID(null);
    this.setUserSearch(null);
    this.setUserRoleConditions(null);
  }

  // When user logout reset all state including login sessions
  public void hardResetState() {
    this.setCurrUser(null);
    this.setStaySignedIn(false);
    this.setSelectedUserID(null);
    this.setUserSearch(null);
    this.setUserRoleConditions(null);
  }
}
