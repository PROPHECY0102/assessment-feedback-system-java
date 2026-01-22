package com.apu_afs.Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.apu_afs.Helper;
import com.apu_afs.Models.Enums.EmploymentType;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Models.Enums.Role;

public class Lecturer extends User {
  private AcademicLeader academicLeader;
  private EmploymentType employmentType;
  private LocalDate employedAt;

  public static final Map<String, Integer> columnLookup = Map.ofEntries(
    Map.entry("userID", 0),
    Map.entry("academicLeader", 1),
    Map.entry("employmentType", 2),
    Map.entry("employedAt", 3)
  );

  public static final String filePath = "data/lecturers.txt";
  
  public Lecturer(List<String> props) {
    super(props);
    List<String> lecturerProps = this.fetchProps();
    User potentialUser =  User.getUserByMatchingValues("id", lecturerProps.get(columnLookup.get("academicLeader")).trim());
    if (potentialUser != null && potentialUser.getRole().getValue().equals(Role.ACADEMIC_LEADER.getValue())) {
      this.academicLeader = (AcademicLeader) potentialUser;
    }
    this.employmentType = EmploymentType.fromValue(lecturerProps.get(columnLookup.get("employmentType")).trim());
    this.employedAt = LocalDate.parse(lecturerProps.get(columnLookup.get("employedAt")).trim(), Helper.dateTimeFormatter);
  }

  public Lecturer(HashMap<String, String> inputValues) {
    super(inputValues);
    User potentialUser = User.getUserByMatchingValues("id", inputValues.get("academicLeader"));
    if (potentialUser != null && potentialUser.getRole().getValue().equals(Role.ACADEMIC_LEADER.getValue())) {
      this.academicLeader = (AcademicLeader) potentialUser;
    }
    this.employmentType = EmploymentType.fromValue(inputValues.get("employmentType"));
    this.employedAt = LocalDate.parse(inputValues.get("employedAt"), Helper.dateTimeFormatter);
  }

  public AcademicLeader getAcademicLeader() {
    return this.academicLeader;
  }

  public EmploymentType getEmploymentType() {
    return this.employmentType;
  }

  public LocalDate getEmployedAt() {
    return this.employedAt;
  }

  public void setAcademicLeader(AcademicLeader academicLeader) {
    this.academicLeader = academicLeader;
    updateUser();
  }

  public void setEmploymentType(EmploymentType employmentType) {
    this.employmentType = employmentType;
    updateUser();
  }

  public void setEmployedAt(LocalDate employedAt) {
    this.employedAt = employedAt;
    updateUser();
  }

  @Override
  public void updateUser() {
    super.updateUser();
    List<String> data = Data.fetch(filePath);
    
    // To the outdated role specific data and replace it with the updated information
    List<String> updatedData = data.stream().filter((dataRow) -> {
      List<String> props = new ArrayList<String>(Arrays.asList(dataRow.split(", ")));
      return !props.get(columnLookup.get("userID")).trim().equals(this.ID);
    }).collect(Collectors.toCollection(ArrayList::new));

    List<String> updatedProps = new ArrayList<String>();
    updatedProps.add(this.academicLeader == null ? "0" : this.academicLeader.ID);
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
      List<String> props = new ArrayList<String>(Arrays.asList(dataRow.split(", ")));
      return !props.get(columnLookup.get("userID")).trim().equals(this.ID);
    }).collect(Collectors.toCollection(ArrayList::new));

    Data.save(filePath, String.join("\n", updatedData));
  } 
}
