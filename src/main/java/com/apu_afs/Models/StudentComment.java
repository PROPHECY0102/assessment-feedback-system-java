package com.apu_afs.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.apu_afs.Helper;
import com.apu_afs.Models.Enums.Role;

public class StudentComment {
  private String ID;
  private Student student;
  private Module module;
  private String comment; // comment text
  private LocalDateTime commentedAt;

  public static Map<String, Integer> columnLookup = Map.ofEntries(
    Map.entry("id", 0),
    Map.entry("student", 1),
    Map.entry("module", 2),
    Map.entry("comment", 3),
    Map.entry("commentedAt", 4)
  );

  private static String filePath = "data/studentComments.txt";

  public StudentComment(List<String> props) {
    this.ID = props.get(columnLookup.get("id")).trim();
    User potentialStudent = User.getUserByMatchingValues("id", props.get(columnLookup.get("student")).trim());
    this.student = potentialStudent instanceof Student studentUser ? studentUser : null;
    this.module = Module.getModuleByMatchingValues("id", props.get(columnLookup.get("module")).trim());
    this.comment = Helper.saveDecode(props.get(columnLookup.get("comment")).trim());
    String commentedAtStr = props.get(columnLookup.get("commentedAt")).trim();
    this.commentedAt = LocalDateTime.parse(commentedAtStr, java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
  }

  public StudentComment(HashMap<String, String> inputValues) {
    String studentCommentID;
    if (inputValues.get("id") == null) {
      IDIncrement idIncrement = new IDIncrement();
      studentCommentID = String.valueOf(idIncrement.getStudentCommentID());
    } else {
      studentCommentID = inputValues.get("id");
    }

    this.ID = studentCommentID;
    User potentialStudent = User.getUserByMatchingValues("id", inputValues.get("student"));
    this.student = potentialStudent instanceof Student studentUser ? studentUser : null;
    this.module = Module.getModuleByMatchingValues("id", inputValues.get("module"));
    this.comment = inputValues.get("comment") != null ? inputValues.get("comment") : "";
    this.commentedAt = LocalDateTime.parse(inputValues.get("commentedAt"), java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
  }

  public static StudentComment getStudentCommentByMatchingValues(String column, String value) {
    List<String> commentsData = Data.fetch(StudentComment.filePath);
    
    for (String commentRow : commentsData) {
      List<String> props = List.of(commentRow.split(", ", 5)); // Limit split to 5 parts due to comment may have commas
      if (props.get(columnLookup.get(column)).trim().equals(value)) {
        return new StudentComment(props);
      }
    }

    return null;
  }

  public static List<StudentComment> getListOfStudentCommentsByMatchingValues(String column, String value) {
    List<String> commentsData = Data.fetch(StudentComment.filePath);
    List<StudentComment> comments = new ArrayList<>();
    
    for (String commentRow : commentsData) {
      List<String> props = List.of(commentRow.split(", ", 5));
      if (props.get(columnLookup.get(column)).trim().equals(value)) {
        comments.add(new StudentComment(props));
      }
    }

    return comments;
  }

  public static List<StudentComment> fetchStudentComments(String search, User currUser) {
    List<String> commentsData = Data.fetch(StudentComment.filePath);
    List<StudentComment> comments = new ArrayList<>();

    boolean filterOnlyCurrLecturer = currUser.getRole() == Role.LECTURER;
    
    for (String commentRow : commentsData) {
      List<String> props = List.of(commentRow.split(", ", 5));
      StudentComment comment = new StudentComment(props);
      if (filterOnlyCurrLecturer) {
        // Only show comments from students on lecturer's modules
        if (comment.getModule() != null && comment.getModule().getInstructor() != null && 
            comment.getModule().getInstructor().getID().equals(currUser.getID())) {
          comments.add(comment);
        }
      } else {
        comments.add(comment);
      }
    }

    List<StudentComment> searchResult = comments.stream()
    .filter(comment -> {
      return comment.getComment().toLowerCase().contains(search.toLowerCase()) ||
      (comment.getStudent() != null && (comment.getStudent().getFirstName() + " " + comment.getStudent().getLastName()).toLowerCase().contains(search.toLowerCase())) ||
      (comment.getModule() != null && comment.getModule().getTitle().toLowerCase().contains(search.toLowerCase()));
    }).collect(Collectors.toList());

    return searchResult;
  }

  public static void saveStudentComment(StudentComment comment) {
    List<String> commentsData = Data.fetch(StudentComment.filePath);
    List<String> updatedData = new ArrayList<>();
    
    boolean found = false;
    for (String commentRow : commentsData) {
      List<String> props = List.of(commentRow.split(", ", 5));
      if (props.get(columnLookup.get("id")).trim().equals(comment.getID())) {
        updatedData.add(comment.toSaveFormat());
        found = true;
      } else {
        updatedData.add(commentRow);
      }
    }

    if (!found) {
      updatedData.add(comment.toSaveFormat());
    }

    String content = String.join("\n", updatedData);
    Data.save(StudentComment.filePath, content);
  }

  public static void deleteStudentComment(String commentID) {
    List<String> commentsData = Data.fetch(StudentComment.filePath);
    List<String> updatedData = commentsData.stream()
      .filter(commentRow -> {
        List<String> props = List.of(commentRow.split(", ", 5));
        return !props.get(columnLookup.get("id")).trim().equals(commentID);
      })
      .collect(Collectors.toList());

    String content = String.join("\n", updatedData);
    Data.save(StudentComment.filePath, content);
  }

  public String toSaveFormat() {
    return String.format("%s, %s, %s, %s, %s",
      this.ID,
      this.student != null ? this.student.getID() : "",
      this.module != null ? this.module.getID() : "",
      Helper.saveEncode(this.comment),
      this.commentedAt.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
    );
  }

  // Getters and Setters
  public String getID() {
    return ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public Module getModule() {
    return module;
  }

  public void setModule(Module module) {
    this.module = module;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public LocalDateTime getCommentedAt() {
    return commentedAt;
  }

  public void setCommentedAt(LocalDateTime commentedAt) {
    this.commentedAt = commentedAt;
  }
}
