package com.apu_afs.Models;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.apu_afs.Helper;
import com.apu_afs.Models.Enums.EmploymentType;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Models.Enums.Role;

public class AcademicLeader extends User {
  private String faculty;
  private EmploymentType employmentType;
  private LocalDate employedAt;

  public static final Map<String, Integer> columnLookup = Map.ofEntries(
    Map.entry("userID", 0),
    Map.entry("faculty", 1),
    Map.entry("employmentType", 2),
    Map.entry("employedAt", 3)
  );

  public static final String filePath = "data/academicLeaders.txt";

  private static final List<NavOption> additionalNavOptions = List.of(
    new NavOption(Pages.MANAGEMODULES)
  );
  
  public AcademicLeader(List<String> props) {
    super(props);
    List<String> academicProps = this.fetchProps();
    this.faculty = academicProps.get(columnLookup.get("faculty")).trim();
    this.employmentType = EmploymentType.fromValue(academicProps.get(columnLookup.get("employmentType")).trim());
    this.employedAt = LocalDate.parse(academicProps.get(columnLookup.get("employedAt")).trim(), Helper.dateTimeFormatter);
    this.navOptions.addAll(additionalNavOptions);
  }

  public AcademicLeader(HashMap<String, String> inputValues) {
    super(inputValues);
    this.faculty = inputValues.get("faculty");
    this.employmentType = EmploymentType.fromValue(inputValues.get("employmentType"));
    this.employedAt = LocalDate.parse(inputValues.get("employedAt"), Helper.dateTimeFormatter);
    this.navOptions.addAll(additionalNavOptions);
  }

  public static Validation validate(HashMap<String, String> inputValues) {
    Validation cannotBeEmptyCheck = Validation.isEmptyCheck(new String[] {"faculty"}, inputValues);
    if (!cannotBeEmptyCheck.getSuccess()) {
      return cannotBeEmptyCheck;
    }

    Validation validDateCheck = Validation.validDateCheck(new String[] {"employedAt"}, inputValues);
    if (!validDateCheck.getSuccess()) {
      return validDateCheck;
    }

    Validation notAfterToday = Validation.maxDateCheck(new String[] {"employedAt"}, inputValues, LocalDate.now());
    if (!notAfterToday.getSuccess()) {
      return notAfterToday;
    }

    return new Validation("Success! No invalid input", true);
  }

  public String getFaculty() {
    return this.faculty;
  }

  public EmploymentType getEmploymentType() {
    return this.employmentType;
  }

  public LocalDate getEmployedAt() {
    return this.employedAt;
  }

  public void setFaculty(String faculty) {
    this.faculty = faculty;
  }

  public void setEmploymentType(EmploymentType employmentType) {
    this.employmentType = employmentType;
  }

  public void setEmployedAt(LocalDate employedAt) {
    this.employedAt = employedAt;
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
    updatedProps.add(this.faculty);
    updatedProps.add(this.employmentType.getValue());
    updatedProps.add(this.employedAt.format(Helper.dateTimeFormatter));

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

    // Make sure if the academic leader is deleted that it will update all lecturer reference to the academic leader ID is now 0
    List<User> lecturers = User.getListOfUsersByMatchingValues("role", Role.LECTURER.getValue());
    for (User user : lecturers) {
      if (user instanceof Lecturer lecturer) {
        lecturer.setAcademicLeader(null);
      }
    }

    Data.save(filePath, String.join("\n", updatedData));
  }
}
