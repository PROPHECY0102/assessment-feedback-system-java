package com.apu_afs.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.apu_afs.Models.Enums.Role;

public class AssessmentMark {
  private String ID;
  private Assessment assessment;
  private Student student;
  private double marksObtained;
  private LocalDateTime recordedAt;

  public static Map<String, Integer> columnLookup = Map.ofEntries(
    Map.entry("id", 0),
    Map.entry("assessment", 1),
    Map.entry("student", 2),
    Map.entry("marksObtained", 3),
    Map.entry("recordedAt", 4)
  );

  private static String filePath = "data/assessmentMarks.txt";

  public AssessmentMark(List<String> props) {
    this.ID = props.get(columnLookup.get("id")).trim();
    this.assessment = Assessment.getAssessmentByMatchingValues("id", props.get(columnLookup.get("assessment")).trim());
    User potentialStudent = User.getUserByMatchingValues("id", props.get(columnLookup.get("student")).trim());
    this.student = potentialStudent instanceof Student studentUser ? studentUser : null;
    this.marksObtained = Double.parseDouble(props.get(columnLookup.get("marksObtained")).trim());
    String dateTimeStr = props.get(columnLookup.get("recordedAt")).trim();
    this.recordedAt = LocalDateTime.parse(dateTimeStr, java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
  }

  public AssessmentMark(HashMap<String, String> inputValues) {
    String assessmentMarkID;
    if (inputValues.get("id") == null) {
      IDIncrement idIncrement = new IDIncrement();
      assessmentMarkID = String.valueOf(idIncrement.getAssessmentMarkID());
    } else {
      assessmentMarkID = inputValues.get("id");
    }

    this.ID = assessmentMarkID;
    this.assessment = Assessment.getAssessmentByMatchingValues("id", inputValues.get("assessment"));
    User potentialStudent = User.getUserByMatchingValues("id", inputValues.get("student"));
    this.student = potentialStudent instanceof Student studentUser ? studentUser : null;
    this.marksObtained = Double.parseDouble(inputValues.get("marksObtained"));
    this.recordedAt = LocalDateTime.parse(inputValues.get("recordedAt"), java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
  }

  public static AssessmentMark getAssessmentMarkByMatchingValues(String column, String value) {
    List<String> marksData = Data.fetch(AssessmentMark.filePath);
    
    for (String markRow : marksData) {
      List<String> props = List.of(markRow.split(", "));
      if (props.get(columnLookup.get(column)).trim().equals(value)) {
        return new AssessmentMark(props);
      }
    }

    return null;
  }

  public static List<AssessmentMark> getListOfAssessmentMarksByMatchingValues(String column, String value) {
    List<String> marksData = Data.fetch(AssessmentMark.filePath);
    List<AssessmentMark> marks = new ArrayList<>();
    
    for (String markRow : marksData) {
      List<String> props = List.of(markRow.split(", "));
      if (props.get(columnLookup.get(column)).trim().equals(value)) {
        marks.add(new AssessmentMark(props));
      }
    }

    return marks;
  }

  public static List<AssessmentMark> fetchAssessmentMarks(String search, User currUser) {
    List<String> marksData = Data.fetch(AssessmentMark.filePath);
    List<AssessmentMark> marks = new ArrayList<>();

    boolean filterOnlyCurrLecturer = currUser.getRole() == Role.LECTURER;
    
    for (String markRow : marksData) {
      List<String> props = List.of(markRow.split(", "));
      AssessmentMark mark = new AssessmentMark(props);
      if (filterOnlyCurrLecturer) {
        if (mark.getAssessment() != null && mark.getAssessment().getLecturer() != null && 
            mark.getAssessment().getLecturer().getID().equals(currUser.getID())) {
          marks.add(mark);
        }
      } else {
        marks.add(mark);
      }
    }

    List<AssessmentMark> searchResult = marks.stream()
    .filter(mark -> {
      return (mark.getAssessment() != null && mark.getAssessment().getName().toLowerCase().contains(search.toLowerCase())) ||
      (mark.getStudent() != null && (mark.getStudent().getFirstName() + " " + mark.getStudent().getLastName()).toLowerCase().contains(search.toLowerCase())) ||
      String.valueOf(mark.getMarksObtained()).contains(search.toLowerCase());
    }).collect(Collectors.toList());

    return searchResult;
  }

  public static Validation validate(HashMap<String, String> inputValues) {
    Validation cannotBeEmptyCheck = Validation.isEmptyCheck(new String[] {"assessment", "student", "marksObtained"}, inputValues);
    if (!cannotBeEmptyCheck.getSuccess()) {
      return cannotBeEmptyCheck;
    }

    Validation validDoubleCheck = Validation.validDoubleCheck(new String[] {"marksObtained"}, inputValues);
    if (!validDoubleCheck.getSuccess()) {
      return validDoubleCheck;
    }

    Validation positiveDoubleCheck = Validation.validPositiveDoubleCheck(new String[] {"marksObtained"}, inputValues);
    if (!positiveDoubleCheck.getSuccess()) {
      return positiveDoubleCheck;
    }

    Assessment assessment = Assessment.getAssessmentByMatchingValues("id", inputValues.get("assessment"));
    if (assessment == null) {
      return new Validation("Assessment not found", false);
    }

    Validation marksRangeCheck = Validation.validRangeCheck(new String[] {"marksObtained"}, inputValues, new double[] {0, assessment.getTotalMarks()});
    if (!marksRangeCheck.getSuccess()) {
      return marksRangeCheck;
    }

    return new Validation("Success! No invalid input", true);
  }

  public static void saveAssessmentMark(AssessmentMark mark) {
    List<String> marksData = Data.fetch(AssessmentMark.filePath);
    List<String> updatedData = new ArrayList<>();
    
    boolean found = false;
    for (String markRow : marksData) {
      List<String> props = List.of(markRow.split(", "));
      if (props.get(columnLookup.get("id")).trim().equals(mark.getID())) {
        updatedData.add(mark.toSaveFormat());
        found = true;
      } else {
        updatedData.add(markRow);
      }
    }

    if (!found) {
      updatedData.add(mark.toSaveFormat());
    }

    String content = String.join("\n", updatedData);
    Data.save(AssessmentMark.filePath, content);
  }

  public static void deleteAssessmentMark(String markID) {
    List<String> marksData = Data.fetch(AssessmentMark.filePath);
    List<String> updatedData = marksData.stream()
      .filter(markRow -> {
        List<String> props = List.of(markRow.split(", "));
        return !props.get(columnLookup.get("id")).trim().equals(markID);
      })
      .collect(Collectors.toList());

    String content = String.join("\n", updatedData);
    Data.save(AssessmentMark.filePath, content);
  }

  public String toSaveFormat() {
    return String.format("%s, %s, %s, %s, %s",
      this.ID,
      this.assessment != null ? this.assessment.getID() : "",
      this.student != null ? this.student.getID() : "",
      this.marksObtained,
      this.recordedAt.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
    );
  }

  // Getters and Setters
  public String getID() {
    return ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }

  public Assessment getAssessment() {
    return assessment;
  }

  public void setAssessment(Assessment assessment) {
    this.assessment = assessment;
  }

  public Student getStudent() {
    return student;
  }

  public void setStudent(Student student) {
    this.student = student;
  }

  public double getMarksObtained() {
    return marksObtained;
  }

  public void setMarksObtained(double marksObtained) {
    this.marksObtained = marksObtained;
  }

  public LocalDateTime getRecordedAt() {
    return recordedAt;
  }

  public void setRecordedAt(LocalDateTime recordedAt) {
    this.recordedAt = recordedAt;
  }

  public double getPercentage() {
    if (assessment != null && assessment.getTotalMarks() > 0) {
      return (marksObtained / assessment.getTotalMarks()) * 100;
    }
    return 0;
  }
}
