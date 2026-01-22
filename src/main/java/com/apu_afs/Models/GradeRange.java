package com.apu_afs.Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

  public static List<GradeRange> getListOfGradeRanges() {
    List<String> gradesData = Data.fetch(GradeRange.filePath);
    List<GradeRange> gradeRanges = new ArrayList<>();

    for (String props : gradesData) {
      gradeRanges.add(new GradeRange(Arrays.asList(props.split(", "))));
    }

    return gradeRanges;
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

  }

  public void delete() {

  }
}
