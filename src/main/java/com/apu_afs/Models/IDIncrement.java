package com.apu_afs.Models;

import java.util.ArrayList;
import java.util.List;

public class IDIncrement {
  private Integer userID;

  private static final String filePath = "data/idIncrements.txt";

  IDIncrement() {
    List<String> incrementIds = Data.fetch(filePath);

    this.userID = Integer.parseInt(incrementIds.get(0).trim());
  }

  public Integer getUserID() {
    this.userID++;
    saveIDIncrement();
    return this.userID;
  }

  private void saveIDIncrement() {
    List<String> rows = new ArrayList<>();
    rows.add(String.valueOf(this.userID));

    Data.save(filePath, String.join("\n", rows));
  }
}
