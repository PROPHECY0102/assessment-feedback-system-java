package com.apu_afs.Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.apu_afs.Helper;
import com.apu_afs.Models.Enums.Mode;
import com.apu_afs.Models.Enums.Pages;

public class Student extends User {
  private String program;
  private Mode mode;
  private double cgpa;
  private double creditHours;
  private LocalDate enrolledAt;

  public static final Map<String, Integer> columnLookup = Map.ofEntries(
    Map.entry("userID", 0),
    Map.entry("program", 1),
    Map.entry("mode", 2),
    Map.entry("cgpa", 3),
    Map.entry("creditHours", 4),
    Map.entry("enrolledAt", 5)
  );

  public static final String filePath = "data/students.txt";

  private static final List<NavOption> additionalNavOptions = List.of();
  
  public Student(List<String> props) {
    super(props);
    List<String> studentProps = this.fetchProps();
    this.program = studentProps.get(columnLookup.get("program")).trim();
    this.mode = Mode.fromValue(studentProps.get(columnLookup.get("mode")).trim());
    this.cgpa = Double.parseDouble(studentProps.get(columnLookup.get("cgpa")).trim());
    this.creditHours = Double.parseDouble(studentProps.get(columnLookup.get("creditHours")).trim());
    this.enrolledAt = LocalDate.parse(studentProps.get(columnLookup.get("enrolledAt")).trim(), Helper.dateTimeFormatter);
    this.navOptions.addAll(additionalNavOptions);
  }

  public Student(HashMap<String, String> inputValues) {
    super(inputValues);
    this.program = inputValues.get("program");
    this.mode = Mode.fromValue(inputValues.get("mode"));
    this.cgpa = Double.parseDouble(inputValues.get("cgpa"));
    this.creditHours = Double.parseDouble(inputValues.get("creditHours"));
    this.enrolledAt = LocalDate.parse(inputValues.get("enrolledAt"), Helper.dateTimeFormatter);
    this.navOptions.addAll(additionalNavOptions);
  }

  public static Validation validate(HashMap<String, String> inputValues) {
    Validation cannotBeEmptyCheck = Validation.isEmptyCheck(new String[] {"program", "cgpa", "creditHours"}, inputValues);
    if (!cannotBeEmptyCheck.getSuccess()) {
      return cannotBeEmptyCheck;
    }

    Validation validDoubleCheck = Validation.validDoubleCheck(new String[] {"cgpa", "creditHours"}, inputValues);
    if (!validDoubleCheck.getSuccess()) {
      return validDoubleCheck;
    }

    Validation positiveDoubleCheck = Validation.validPositiveDoubleCheck(new String[] {"cgpa", "creditHours"}, inputValues);
    if (!positiveDoubleCheck.getSuccess()) {
      return positiveDoubleCheck;
    }

    Validation cgpaRangeCheck = Validation.validRangeCheck(new String[] {"cgpa"}, inputValues, new double[] {1, 4});
    if (!cgpaRangeCheck.getSuccess()) {
      return cgpaRangeCheck;
    }

    Validation validDateCheck = Validation.validDateCheck(new String[] {"enrolledAt"}, inputValues);
    if (!validDateCheck.getSuccess()) {
      return validDateCheck;
    }

    Validation notAfterToday = Validation.maxDateCheck(new String[] {"enrolledAt"}, inputValues, LocalDate.now());
    if (!notAfterToday.getSuccess()) {
      return notAfterToday;
    }

    return new Validation("Success! No invalid input", true);
  }

  public String getProgram() {
    return this.program;
  }

  public Mode getMode() {
    return this.mode;
  }

  public double getCgpa() {
    return this.cgpa;
  }

  public double getCreditHours() {
    return this.creditHours;
  }

  public LocalDate getEnrolledAt() {
    return this.enrolledAt;
  }

  public void setProgram(String program) {
    this.program = program;
    updateUser();
  }

  public void setMode(Mode mode) {
    this.mode = mode;
    updateUser();
  }

  public void setCgpa(double cgpa) {
    this.cgpa = cgpa;
    updateUser();
  }

  public void setCreditHours(double creditHours) {
    this.creditHours = creditHours;
    updateUser();
  }
  
  public void setEnrolledAt(LocalDate enrolledAt) {
    this.enrolledAt = enrolledAt;
    updateUser();
  }

  @Override
  public void updateUser() {
    super.updateUser();
    List<String> data = Data.fetch(filePath);
    
    // To the outdated role specific data and replace it with the updated information
    List<String> updatedData = data.stream().filter((dataRow) -> {
      List<String> props = List.of(dataRow.split(", "));
      return !props.get(columnLookup.get("userID")).trim().equals(this.ID);
    }).collect(Collectors.toCollection(ArrayList::new));

    List<String> updatedProps = new ArrayList<>();
    updatedProps.add(this.ID);
    updatedProps.add(this.program);
    updatedProps.add(this.mode.getValue());
    updatedProps.add(String.valueOf(this.cgpa));
    updatedProps.add(String.valueOf(this.creditHours));
    updatedProps.add(this.enrolledAt.format(Helper.dateTimeFormatter));

    updatedData.add(String.join(", ", updatedProps));
    Data.save(filePath, String.join("\n", updatedData));
  }

  @Override
  public void deleteUser() {
    super.deleteUser();
    List<String> data = Data.fetch(filePath);
    
    // To the outdated role specific data and replace it with the updated information
    List<String> updatedData = data.stream().filter((dataRow) -> {
      List<String> props = List.of(dataRow.split(", "));
      return !props.get(columnLookup.get("userID")).trim().equals(this.ID);
    }).collect(Collectors.toCollection(ArrayList::new));

    Data.save(filePath, String.join("\n", updatedData));
  }
}
