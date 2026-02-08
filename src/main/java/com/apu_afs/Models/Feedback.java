package com.apu_afs.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.apu_afs.Helper;
import com.apu_afs.Models.Enums.Role;

public class Feedback {
  private String ID;
  private AssessmentMark assessmentMark;
  private String content; // feedback text
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static Map<String, Integer> columnLookup = Map.ofEntries(
    Map.entry("id", 0),
    Map.entry("assessmentMark", 1),
    Map.entry("content", 2),
    Map.entry("createdAt", 3),
    Map.entry("updatedAt", 4)
  );

  private static String filePath = "data/feedbacks.txt";

  public Feedback(List<String> props) {
    this.ID = props.get(columnLookup.get("id")).trim();
    this.assessmentMark = AssessmentMark.getAssessmentMarkByMatchingValues("id", props.get(columnLookup.get("assessmentMark")).trim());
    this.content = Helper.saveDecode(props.get(columnLookup.get("content")).trim());
    String createdAtStr = props.get(columnLookup.get("createdAt")).trim();
    this.createdAt = LocalDateTime.parse(createdAtStr, java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    String updatedAtStr = props.get(columnLookup.get("updatedAt")).trim();
    this.updatedAt = LocalDateTime.parse(updatedAtStr, java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
  }

  public Feedback(HashMap<String, String> inputValues) {
    String feedbackID;
    if (inputValues.get("id") == null) {
      IDIncrement idIncrement = new IDIncrement();
      feedbackID = String.valueOf(idIncrement.getFeedbackID());
    } else {
      feedbackID = inputValues.get("id");
    }

    this.ID = feedbackID;
    this.assessmentMark = AssessmentMark.getAssessmentMarkByMatchingValues("id", inputValues.get("assessmentMark"));
    this.content = inputValues.get("content") != null ? inputValues.get("content") : "";
    this.createdAt = LocalDateTime.parse(inputValues.get("createdAt"), java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    this.updatedAt = LocalDateTime.parse(inputValues.get("updatedAt"), java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
  }

  public static Feedback getFeedbackByMatchingValues(String column, String value) {
    List<String> feedbacksData = Data.fetch(Feedback.filePath);
    
    for (String feedbackRow : feedbacksData) {
      List<String> props = List.of(feedbackRow.split(", ", 5)); // Limit split to 5 parts due to content may have commas
      if (props.get(columnLookup.get(column)).trim().equals(value)) {
        return new Feedback(props);
      }
    }

    return null;
  }

  public static List<Feedback> getListOfFeedbacksByMatchingValues(String column, String value) {
    List<String> feedbacksData = Data.fetch(Feedback.filePath);
    List<Feedback> feedbacks = new ArrayList<>();
    
    for (String feedbackRow : feedbacksData) {
      List<String> props = List.of(feedbackRow.split(", ", 5));
      if (props.get(columnLookup.get(column)).trim().equals(value)) {
        feedbacks.add(new Feedback(props));
      }
    }

    return feedbacks;
  }

  public static List<Feedback> fetchFeedbacks(String search, User currUser) {
    List<String> feedbacksData = Data.fetch(Feedback.filePath);
    List<Feedback> feedbacks = new ArrayList<>();

    boolean filterOnlyCurrLecturer = currUser.getRole() == Role.LECTURER;
    
    for (String feedbackRow : feedbacksData) {
      List<String> props = List.of(feedbackRow.split(", ", 5));
      Feedback feedback = new Feedback(props);
      if (filterOnlyCurrLecturer) {
        if (feedback.getAssessmentMark() != null && feedback.getAssessmentMark().getAssessment() != null && 
            feedback.getAssessmentMark().getAssessment().getLecturer() != null && 
            feedback.getAssessmentMark().getAssessment().getLecturer().getID().equals(currUser.getID())) {
          feedbacks.add(feedback);
        }
      } else {
        feedbacks.add(feedback);
      }
    }

    List<Feedback> searchResult = feedbacks.stream()
    .filter(feedback -> {
      return feedback.getContent().toLowerCase().contains(search.toLowerCase()) ||
      (feedback.getAssessmentMark() != null && feedback.getAssessmentMark().getAssessment() != null && 
       feedback.getAssessmentMark().getAssessment().getName().toLowerCase().contains(search.toLowerCase())) ||
      (feedback.getAssessmentMark() != null && feedback.getAssessmentMark().getStudent() != null && 
       (feedback.getAssessmentMark().getStudent().getFirstName() + " " + feedback.getAssessmentMark().getStudent().getLastName()).toLowerCase().contains(search.toLowerCase()));
    }).collect(Collectors.toList());

    return searchResult;
  }

  public static void saveFeedback(Feedback feedback) {
    List<String> feedbacksData = Data.fetch(Feedback.filePath);
    List<String> updatedData = new ArrayList<>();
    
    boolean found = false;
    for (String feedbackRow : feedbacksData) {
      List<String> props = List.of(feedbackRow.split(", ", 5));
      if (props.get(columnLookup.get("id")).trim().equals(feedback.getID())) {
        updatedData.add(feedback.toSaveFormat());
        found = true;
      } else {
        updatedData.add(feedbackRow);
      }
    }

    if (!found) {
      updatedData.add(feedback.toSaveFormat());
    }

    String content = String.join("\n", updatedData);
    Data.save(Feedback.filePath, content);
  }

  public String toSaveFormat() {
    return String.format("%s, %s, %s, %s, %s",
      this.ID,
      this.assessmentMark != null ? this.assessmentMark.getID() : "",
      Helper.saveEncode(this.content),
      this.createdAt.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
      this.updatedAt.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
    );
  }

  // Getters and Setters
  public String getID() {
    return ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }

  public AssessmentMark getAssessmentMark() {
    return assessmentMark;
  }

  public void setAssessmentMark(AssessmentMark assessmentMark) {
    this.assessmentMark = assessmentMark;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
