package com.apu_afs.Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.apu_afs.Helper;
import com.apu_afs.Models.Enums.Role;

public class Assessment {
  private String ID;
  private String name;
  private Module module;
  private String type; // assignment, quiz, final exam, etc.
  private double totalMarks;
  private Lecturer lecturer;
  private LocalDate createdAt;

  public static Map<String, Integer> columnLookup = Map.ofEntries(
    Map.entry("id", 0),
    Map.entry("name", 1),
    Map.entry("module", 2),
    Map.entry("type", 3),
    Map.entry("totalMarks", 4),
    Map.entry("lecturer", 5),
    Map.entry("createdAt", 6)
  );

  private static String filePath = "data/assessments.txt";

  public Assessment(List<String> props) {
    this.ID = props.get(columnLookup.get("id")).trim();
    this.name = props.get(columnLookup.get("name")).trim();
    this.module = Module.getModuleByMatchingValues("id", props.get(columnLookup.get("module")).trim());
    this.type = props.get(columnLookup.get("type")).trim();
    this.totalMarks = Double.parseDouble(props.get(columnLookup.get("totalMarks")).trim());
    User potentialLecturer = User.getUserByMatchingValues("id", props.get(columnLookup.get("lecturer")).trim());
    if (potentialLecturer instanceof Lecturer lecturer) {
      this.lecturer = lecturer;
    }
    this.createdAt = LocalDate.parse(props.get(columnLookup.get("createdAt")).trim(), Helper.dateTimeFormatter);
  }

  public Assessment(HashMap<String, String> inputValues) {
    String assessmentID;
    if (inputValues.get("id") == null) {
      IDIncrement idIncrement = new IDIncrement();
      assessmentID = String.valueOf(idIncrement.getAssessmentID());
    } else {
      assessmentID = inputValues.get("id");
    }

    this.ID = assessmentID;
    this.name = inputValues.get("name");
    this.module = Module.getModuleByMatchingValues("id", inputValues.get("module"));
    this.type = inputValues.get("type");
    this.totalMarks = Double.parseDouble(inputValues.get("totalMarks"));
    User potentialLecturer = User.getUserByMatchingValues("id", inputValues.get("lecturer"));
    if (potentialLecturer instanceof Lecturer lecturer) {
      this.lecturer = lecturer;
    }
    this.createdAt = LocalDate.parse(inputValues.get("createdAt"), Helper.dateTimeFormatter);
  }

  public static Assessment getAssessmentByMatchingValues(String column, String value) {
    List<String> assessmentsData = Data.fetch(Assessment.filePath);
    
    for (String assessmentRow : assessmentsData) {
      List<String> props = List.of(assessmentRow.split(", "));
      if (props.get(columnLookup.get(column)).trim().equals(value)) {
        return new Assessment(props);
      }
    }

    return null;
  }

  public static List<Assessment> getListOfAssessmentsByMatchingValues(String column, String value) {
    List<String> assessmentsData = Data.fetch(Assessment.filePath);
    List<Assessment> assessments = new ArrayList<>();
    
    for (String assessmentRow : assessmentsData) {
      List<String> props = List.of(assessmentRow.split(", "));
      if (props.get(columnLookup.get(column)).trim().equals(value)) {
        assessments.add(new Assessment(props));
      }
    }

    return assessments;
  }

  public static List<Assessment> fetchAssessments(String search, User currUser) {
    List<String> assessmentsData = Data.fetch(Assessment.filePath);
    List<Assessment> assessments = new ArrayList<>();

    boolean filterOnlyCurrLecturer = currUser.getRole() == Role.LECTURER;
    
    for (String assessmentRow : assessmentsData) {
      List<String> props = List.of(assessmentRow.split(", "));
      if (filterOnlyCurrLecturer) {
        if (props.get(columnLookup.get("lecturer")).trim().equals(currUser.getID())) {
          assessments.add(new Assessment(props));
        }
      } else {
        assessments.add(new Assessment(props));
      }
    }

    List<Assessment> searchResult = assessments.stream()
    .filter(assessment -> {
      return assessment.getName().toLowerCase().contains(search.toLowerCase()) ||
      assessment.getType().toLowerCase().contains(search.toLowerCase()) ||
      (assessment.getModule() != null && assessment.getModule().getTitle().toLowerCase().contains(search.toLowerCase())) ||
      (assessment.getModule() != null && assessment.getModule().getCode().toLowerCase().contains(search.toLowerCase())) ||
      String.valueOf(assessment.getTotalMarks()).contains(search.toLowerCase());
    }).collect(Collectors.toList());

    return searchResult;
  }

  public static Validation validate(HashMap<String, String> inputValues) {
    Validation cannotBeEmptyCheck = Validation.isEmptyCheck(new String[] {"name", "module", "type", "totalMarks"}, inputValues);
    if (!cannotBeEmptyCheck.getSuccess()) {
      return cannotBeEmptyCheck;
    }

    Validation minLengthCheck = Validation.minLengthCheck(new String[] {"name", "type"}, inputValues, 2);
    if (!minLengthCheck.getSuccess()) {
      return minLengthCheck;
    }

    Validation maxLengthCheck = Validation.maxLengthCheck(new String[] {"name", "type"}, inputValues, 50);
    if (!maxLengthCheck.getSuccess()) {
      return maxLengthCheck;
    }

    Validation validDoubleCheck = Validation.validDoubleCheck(new String[] {"totalMarks"}, inputValues);
    if (!validDoubleCheck.getSuccess()) {
      return validDoubleCheck;
    }

    Validation positiveDoubleCheck = Validation.validPositiveDoubleCheck(new String[] {"totalMarks"}, inputValues);
    if (!positiveDoubleCheck.getSuccess()) {
      return positiveDoubleCheck;
    }

    return new Validation("Success! No invalid input", true);
  }

  public static void saveAssessment(Assessment assessment) {
    List<String> assessmentsData = Data.fetch(Assessment.filePath);
    List<String> updatedData = new ArrayList<>();
    
    boolean found = false;
    for (String assessmentRow : assessmentsData) {
      List<String> props = List.of(assessmentRow.split(", "));
      if (props.get(columnLookup.get("id")).trim().equals(assessment.getID())) {
        updatedData.add(assessment.toSaveFormat());
        found = true;
      } else {
        updatedData.add(assessmentRow);
      }
    }

    if (!found) {
      updatedData.add(assessment.toSaveFormat());
    }

    String content = String.join("\n", updatedData);
    Data.save(Assessment.filePath, content);
  }

  public static void deleteAssessment(String assessmentID) {
    List<String> assessmentsData = Data.fetch(Assessment.filePath);
    List<String> updatedData = assessmentsData.stream()
      .filter(assessmentRow -> {
        List<String> props = List.of(assessmentRow.split(", "));
        return !props.get(columnLookup.get("id")).trim().equals(assessmentID);
      })
      .collect(Collectors.toList());

    String content = String.join("\n", updatedData);
    Data.save(Assessment.filePath, content);
  }

  public String toSaveFormat() {
    return String.format("%s, %s, %s, %s, %s, %s, %s",
      this.ID,
      this.name,
      this.module != null ? this.module.getID() : "",
      this.type,
      this.totalMarks,
      this.lecturer != null ? this.lecturer.getID() : "",
      this.createdAt.format(Helper.dateTimeFormatter)
    );
  }

  // Getters and Setters
  public String getID() {
    return ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Module getModule() {
    return module;
  }

  public void setModule(Module module) {
    this.module = module;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public double getTotalMarks() {
    return totalMarks;
  }

  public void setTotalMarks(double totalMarks) {
    this.totalMarks = totalMarks;
  }

  public Lecturer getLecturer() {
    return lecturer;
  }

  public void setLecturer(Lecturer lecturer) {
    this.lecturer = lecturer;
  }

  public LocalDate getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDate createdAt) {
    this.createdAt = createdAt;
  }
}
