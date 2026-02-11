package com.apu_afs.Models;

import java.time.LocalDateTime;
import java.util.HashMap;

public class StudentFeedback {

  private Student student;
  private Module module;
  private String comment;
  private LocalDateTime commentedAt;


  public StudentFeedback() {
    this.commentedAt = LocalDateTime.now();
  }


  public StudentFeedback(Student student, Module module, String comment) {
    this.student = student;
    this.module = module;
    this.comment = comment;
    this.commentedAt = LocalDateTime.now();
  }


  public Student getStudent() {
    return student;
  }

  public Module getModule() {
    return module;
  }

  public String getComment() {
    return comment;
  }

  public LocalDateTime getCommentedAt() {
    return commentedAt;
  }


  public void setStudent(Student student) {
    this.student = student;
  }

  public void setModule(Module module) {
    this.module = module;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public void setCommentedAt(LocalDateTime commentedAt) {
    this.commentedAt = commentedAt;
  }


  public void submit() {

    HashMap<String, String> data = new HashMap<>();

    data.put("student", getStudent().getID());   
    data.put("module", getModule().getID());     
    data.put("comment", getComment());           
    data.put(
      "commentedAt",
      getCommentedAt().format(
        java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
      )
    );

    StudentComment.saveStudentComment(new StudentComment(data));
  }
}
