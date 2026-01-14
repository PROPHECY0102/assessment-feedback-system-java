package com.apu_afs.Models;

import java.util.ArrayList;
import java.util.List;

public class IDIncrement {
  private Integer userID;
  private Integer adminID;
  private Integer academicID;
  private Integer lecturerID;
  private Integer studentID;

  private static final String filePath = "data/idIncrement.txt";

  IDIncrement() {
    List<String> incrementIds = Data.fetch(filePath);

    this.userID = Integer.parseInt(incrementIds.get(0).trim());
    this.adminID = Integer.parseInt(incrementIds.get(1).trim());
    this.academicID = Integer.parseInt(incrementIds.get(2).trim());
    this.lecturerID = Integer.parseInt(incrementIds.get(3).trim());
    this.studentID = Integer.parseInt(incrementIds.get(4).trim());
  }

  public Integer getUserID() {
    return userID;
  }

  public Integer getAdminID() {
    return adminID;
  }

  public Integer getAcademicID() {
    return academicID;
  }

  public Integer getLecturerID() {
    return lecturerID;
  }

  public Integer getStudentID() {
    return studentID;
  }

  public void incrementUserID() {
    this.userID = userID++;
    saveIDIncrement();
  }

  public void incrementAdminID() {
    this.adminID = adminID++;
    saveIDIncrement();
  }

  public void incrementAcademicID() {
    this.academicID = academicID++;
    saveIDIncrement();
  }

  public void incrementLecturerID() {
    this.lecturerID = lecturerID++;
    saveIDIncrement();
  }

  public void incrementStudentID() {
    this.studentID = studentID++;
    saveIDIncrement();
  }

  private void saveIDIncrement() {
    List<String> rows = new ArrayList<>();
    rows.add(String.valueOf(this.userID));
    rows.add(String.valueOf(this.adminID));
    rows.add(String.valueOf(this.academicID));
    rows.add(String.valueOf(this.lecturerID));
    rows.add(String.valueOf(this.studentID));

    Data.save(filePath, String.join("\n", rows));
  }
}
