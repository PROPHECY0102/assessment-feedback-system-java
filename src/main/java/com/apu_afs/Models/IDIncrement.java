package com.apu_afs.Models;

import java.util.ArrayList;
import java.util.List;

public class IDIncrement {
  private Integer userID;
  private Integer gradeRangeID;

  private static final String filePath = "data/idIncrements.txt";

  IDIncrement() {
    List<String> incrementIds = Data.fetch(filePath);

    this.userID = Integer.parseInt(incrementIds.get(0).trim());
    this.gradeRangeID = Integer.parseInt(incrementIds.get(1).trim());
  }

  public Integer getUserID() {
    this.userID++;
    saveIDIncrement();
    return this.userID;
  }

  public Integer getGradeRangeID() {
    this.gradeRangeID++;
    saveIDIncrement();
    return gradeRangeID;
  }

  private void saveIDIncrement() {
    List<String> rows = new ArrayList<>();
    rows.add(String.valueOf(this.userID));
    rows.add(String.valueOf(this.gradeRangeID));

    Data.save(filePath, String.join("\n", rows));
  }
}
