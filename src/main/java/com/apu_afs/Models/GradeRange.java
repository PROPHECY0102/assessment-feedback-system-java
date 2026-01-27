package com.apu_afs.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GradeRange {
  String ID;
  String grade; // A, B+, C- (need to be unique and max two characters)
  String description; // Distinction, Credit
  double points; // 4.0, 3.7, 3.3
  double min; // 50 (50-79)
  double max; // 79 (50-79)

  public static Map<String, Integer> columnLookup = Map.ofEntries(
    Map.entry("id", 0),
    Map.entry("grade", 1),
    Map.entry("description", 2),
    Map.entry("points", 3),
    Map.entry("min", 4),
    Map.entry("max", 5)
  );

  private static final String filePath = "data/gradeRanges.txt";

  public GradeRange(List<String> props) {
    this.ID = props.get(columnLookup.get("id")).trim();
    this.grade = props.get(columnLookup.get("grade")).trim();
    this.description = props.get(columnLookup.get("description")).trim();
    this.points = Double.parseDouble(props.get(columnLookup.get("points")).trim());
    this.min = Double.parseDouble(props.get(columnLookup.get("min")).trim());
    this.max = Double.parseDouble(props.get(columnLookup.get("max")).trim());
  }

  public GradeRange(HashMap<String, String> inputValues) {
    String gradeRangeID;
    if (inputValues.get("id") == null) {
      IDIncrement idIncrement = new IDIncrement();
      gradeRangeID = String.valueOf(idIncrement.getGradeRangeID());
    } else {
      gradeRangeID = inputValues.get("id");
    }

    this.ID = gradeRangeID;
    this.grade = inputValues.get("grade");
    this.description = inputValues.get("description");
    this.points = Double.parseDouble(inputValues.get("points"));
    this.min = Double.parseDouble(inputValues.get("min"));
    this.max = Double.parseDouble(inputValues.get("max"));
  }

  public static GradeRange getGradeRangeByMatchingValue(String column, String value) {
    List<String> gradesData = Data.fetch(GradeRange.filePath);

    for (String gradeRangesRow : gradesData) {
      List<String> props = List.of(gradeRangesRow.split(", "));
      if (props.get(columnLookup.get(column)).trim().equals(value)) {
        return new GradeRange(props);
      }
    }

    return null;
  }

  public static List<GradeRange> getListOfGradeRanges() {
    List<String> gradesData = Data.fetch(GradeRange.filePath);
    List<GradeRange> gradeRanges = new ArrayList<>();

    for (String gradeRangesRow : gradesData) {
      gradeRanges.add(new GradeRange(List.of(gradeRangesRow.split(", "))));
    }

    return gradeRanges;
  }

  public static Validation validate(HashMap<String, String> inputValues) {
    Validation cannotBeEmptyCheck = Validation.isEmptyCheck(new String[] {"grade", "description", "points", "min", "max"}, inputValues);
    if (!cannotBeEmptyCheck.getSuccess()) {
      return cannotBeEmptyCheck;
    }

    Validation maxLengthCheck = Validation.maxLengthCheck(new String[] {"grade"}, inputValues, 2);
    if (!maxLengthCheck.getSuccess()) {
      return maxLengthCheck;
    }

    GradeRange existingGrade = GradeRange.getGradeRangeByMatchingValue("grade", inputValues.get("grade"));
    if (existingGrade != null && (inputValues.get("id") == null || !existingGrade.getID().equals(inputValues.get("id")))) {
      return new Validation("Grade must be unique. '" + inputValues.get("grade") + "' already exist", false, "grade");
    }

    Validation validDoubleCheck = Validation.validDoubleCheck(new String[] {"points", "min", "max"}, inputValues);
    if (!validDoubleCheck.getSuccess()) {
      return validDoubleCheck;
    }

    Validation validGPACheck = Validation.validRangeCheck(new String[] {"points"}, inputValues, new double[] {0, 4});
    if (!validGPACheck.getSuccess()) {
      return validGPACheck;
    }

    Validation validMinMaxScores = Validation.validRangeCheck(new String[] {"min", "max"}, inputValues, new double[] {0, 100});
    if (!validMinMaxScores.getSuccess()) {
      return validMinMaxScores;
    }

    return new Validation("No Invalid inputs", true);
  }

  public String getID() {
    return ID;
  }

  public String getGrade() {
    return grade;
  }

  public String getDescription() {
    return description;
  }

  public double getPoints() {
    return points;
  }

  public double getMin() {
    return min;
  }

  public double getMax() {
    return max;
  }

  public void setID(String ID) {
    this.ID = ID;
    update();
  }

  public void setGrade(String grade) {
    this.grade = grade;
    update();
  }

  public void setDescription(String description) {
    this.description = description;
    update();
  }

  public void setPoints(double points) {
    this.points = points;
    update();
  }

  public void setMin(double min) {
    this.min = min;
    update();
  }
  
  public void setMax(double max) {
    this.max = max;
    update();
  }

  public void update() {
    List<String> gradeRangesData = Data.fetch(filePath);

    List<String> updatedGradeRangesData = gradeRangesData.stream().filter((gradeRangeRow) -> {
      List<String> props = List.of(gradeRangeRow.split(", "));
      return !props.get(columnLookup.get("id")).trim().equals(this.ID);
    }).collect(Collectors.toList());

    List<String> updatedGradeRangeProps = new ArrayList<>();
    updatedGradeRangeProps.add(this.ID);
    updatedGradeRangeProps.add(this.grade);
    updatedGradeRangeProps.add(this.description);
    updatedGradeRangeProps.add(String.valueOf(this.points));
    updatedGradeRangeProps.add(String.valueOf(this.min));
    updatedGradeRangeProps.add(String.valueOf(this.max));

    updatedGradeRangesData.add(String.join(", ", updatedGradeRangeProps));
    Data.save(filePath, String.join("\n", updatedGradeRangesData));
  }

  public void delete() {
    List<String> gradeRangesData = Data.fetch(filePath);

    List<String> updatedGradeRangesData = gradeRangesData.stream().filter((gradeRangeRow) -> {
      List<String> props = List.of(gradeRangeRow.split(", "));
      return !props.get(columnLookup.get("id")).trim().equals(this.ID);
    }).collect(Collectors.toList());

    Data.save(filePath, String.join("\n", updatedGradeRangesData));
  }
}
