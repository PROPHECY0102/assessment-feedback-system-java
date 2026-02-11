package com.apu_afs.Models;

import java.util.ArrayList;
import java.util.List;

public class IDIncrement {
  private Integer userID;
  private Integer gradeRangeID;
  private Integer moduleID;
  private Integer studentModuleID;
  private Integer assessmentID;
  private Integer assessmentMarkID;
  private Integer feedbackID;
  private Integer studentCommentID;
  private Integer studentClassEnrollmentID;
  private Integer moduleClassID;


  private static final String filePath = "data/idIncrements.txt";

  IDIncrement() {
    List<String> incrementIds = Data.fetch(filePath);

    this.userID = Integer.parseInt(incrementIds.get(0).trim());
    this.gradeRangeID = Integer.parseInt(incrementIds.get(1).trim());
    this.moduleID = Integer.parseInt(incrementIds.get(2).trim());
    this.studentModuleID = Integer.parseInt(incrementIds.get(3).trim());
    this.assessmentID = incrementIds.size() > 4 ? Integer.parseInt(incrementIds.get(4).trim()) : 0;
    this.assessmentMarkID = incrementIds.size() > 5 ? Integer.parseInt(incrementIds.get(5).trim()) : 0;
    this.feedbackID = incrementIds.size() > 6 ? Integer.parseInt(incrementIds.get(6).trim()) : 0;
    this.studentCommentID = incrementIds.size() > 7 ? Integer.parseInt(incrementIds.get(7).trim()) : 0;
    this.studentClassEnrollmentID = incrementIds.size() > 8 ? Integer.parseInt(incrementIds.get(8).trim()) : 0;
    this.moduleClassID = Integer.parseInt(incrementIds.get(9).trim());
 }

  public Integer getUserID() {
    this.userID++;
    saveIDIncrement();
    return this.userID;
  }

  public Integer getGradeRangeID() {
    this.gradeRangeID++;
    saveIDIncrement();
    return this.gradeRangeID;
  }

  public Integer getModuleID() {
    this.moduleID++;
    saveIDIncrement();
    return this.moduleID;
  }

  public Integer getStudentModuleID() {
    this.studentModuleID++;
    saveIDIncrement();
    return this.studentModuleID;
  }

  public Integer getAssessmentID() {
    this.assessmentID++;
    saveIDIncrement();
    return this.assessmentID;
  }

  public Integer getAssessmentMarkID() {
    this.assessmentMarkID++;
    saveIDIncrement();
    return this.assessmentMarkID;
  }

  public Integer getFeedbackID() {
    this.feedbackID++;
    saveIDIncrement();
    return this.feedbackID;
  }

  public Integer getStudentCommentID() {
    this.studentCommentID++;
    saveIDIncrement();
    return this.studentCommentID;
  }

  public Integer getStudentClassEnrollmentID() {
    this.studentClassEnrollmentID++;
    saveIDIncrement();
    return this.studentClassEnrollmentID;
  }

  public Integer getModuleClassID() {
    this.moduleClassID++;
    saveIDIncrement();
    return this.moduleClassID;
  }


  private void saveIDIncrement() {
    List<String> rows = new ArrayList<>();
    rows.add(String.valueOf(this.userID));
    rows.add(String.valueOf(this.gradeRangeID));
    rows.add(String.valueOf(this.moduleID));
    rows.add(String.valueOf(this.studentModuleID));
    rows.add(String.valueOf(this.assessmentID));
    rows.add(String.valueOf(this.assessmentMarkID));
    rows.add(String.valueOf(this.feedbackID));
    rows.add(String.valueOf(this.studentCommentID));
    rows.add(String.valueOf(this.studentClassEnrollmentID));
    rows.add(String.valueOf(this.moduleClassID));

    Data.save(filePath, String.join("\n", rows));
  }
}
