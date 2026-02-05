package com.apu_afs.Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.apu_afs.Helper;
import com.apu_afs.Models.Enums.ModuleStatus;

// StudentModule simulates a pivot table to represent a many to many relationship such as student can enrolled into many modules but a module can be enrolled into by many students
public class StudentModule {
  private String ID;
  private Student student; // composite key
  private Module module; // composite key
  private ModuleStatus status;
  private LocalDate enrolledAt;
  private double points;

  public static Map<String, Integer> columnLookup = Map.ofEntries(
    Map.entry("id", 0),
    Map.entry("student", 1),
    Map.entry("module", 2),
    Map.entry("status", 3),
    Map.entry("enrolledAt", 4),
    Map.entry("points", 5)
  );

  private static String filePath = "data/studentModules.txt";

  public StudentModule(List<String> props) {
    this.ID = props.get(columnLookup.get("id")).trim();
    User potentialUser = User.getUserByMatchingValues("id", props.get(columnLookup.get("student")).trim());
    this.student = potentialUser instanceof Student studentUser ? studentUser : null;
    this.module = Module.getModuleByMatchingValues("id", props.get(columnLookup.get("module")).trim());
    this.status = ModuleStatus.fromValue(props.get(columnLookup.get("status")).trim());
    this.enrolledAt = LocalDate.parse(props.get(columnLookup.get("enrolledAt")).trim(), Helper.dateTimeFormatter);
    this.points = Double.parseDouble(props.get(columnLookup.get("points")).trim());
  }

  public StudentModule(HashMap<String, String> inputValues) {
    String studentModuleID;
    if (inputValues.get("id") == null) {
      IDIncrement idIncrement = new IDIncrement();
      studentModuleID = String.valueOf(idIncrement.getStudentModuleID());
    } else {
      studentModuleID = inputValues.get("id");
    }

    this.ID = studentModuleID;
    User potentialUser = User.getUserByMatchingValues("id", inputValues.get("student"));
    this.student = potentialUser instanceof Student studentUser ? studentUser : null;
    this.module = Module.getModuleByMatchingValues("id", inputValues.get("module"));
    this.status = ModuleStatus.fromValue(inputValues.get("status"));
    this.enrolledAt = LocalDate.parse(inputValues.get("enrolledAt"), Helper.dateTimeFormatter);
    this.points = Double.parseDouble(inputValues.get("points"));
  }

  public static StudentModule getStudentModuleByMatchingValues(String column, String value) {
    List<String> studentModulesData = Data.fetch(StudentModule.filePath);
    
    for (String modulesRow : studentModulesData) {
      List<String> props = List.of(modulesRow.split(", "));
      if (props.get(columnLookup.get(column)).trim().equals(value)) {
        return new StudentModule(props);
      }
    }

    return null;
  }

  public static List<StudentModule> getListOfStudentModulesByMatchingValues(String column, String value) {
    List<String> studentModulesData = Data.fetch(StudentModule.filePath);
    List<StudentModule> studentModules = new ArrayList<>();
    
    for (String modulesRow : studentModulesData) {
      List<String> props = List.of(modulesRow.split(", "));
      if (props.get(columnLookup.get(column)).trim().equals(value)) {
        studentModules.add(new StudentModule(props));
      }
    }

    return studentModules;
  }

  public static StudentModule getStudentModuleByCompositeKey(String studentID, String moduleID) {
    List<String> studentModulesData = Data.fetch(StudentModule.filePath);
    
    for (String modulesRow : studentModulesData) {
      List<String> props = List.of(modulesRow.split(", "));
      String currSMStudentID = props.get(columnLookup.get("student")).trim();
      String currSMModuleID = props.get(columnLookup.get("module")).trim();
      if (currSMStudentID.equals(studentID) && currSMModuleID.equals(moduleID)) {
        return new StudentModule(props);
      }
    }

    return null;
  }

  public static List<StudentModule> fetchStudentModules(String search, String studentID, String moduleID, Set<String> statuses) {
    List<String> studentModulesData = Data.fetch(StudentModule.filePath);
    List<StudentModule> studentModules = new ArrayList<>();
    
    for (String modulesRow : studentModulesData) {
      List<String> props = List.of(modulesRow.split(", "));

      if (!studentID.isEmpty() && !props.get(columnLookup.get("student")).trim().equals(studentID)) {
        continue;
      }
      
      if (!moduleID.isEmpty() && !props.get(columnLookup.get("module")).trim().equals(moduleID)) {
        continue;
      }
      
      if (statuses.isEmpty() || !statuses.contains(props.get(columnLookup.get("status")).trim())) {
        continue;
      }

      studentModules.add(new StudentModule(props));
    }

    List<StudentModule> searchResult = studentModules.stream()
    .filter(studentModule -> {
      return (studentModule.getStudent().getFirstName() + studentModule.getStudent().getLastName()).toLowerCase().contains(search.toLowerCase()) ||
      studentModule.getModule().getCode().toLowerCase().contains(search.toLowerCase()) ||
      studentModule.getModule().getTitle().toLowerCase().contains(search.toLowerCase());
    }).collect(Collectors.toList());

    return searchResult;
  }

  public String getID() {
    return this.ID;
  }

  public Student getStudent() {
    return this.student;
  }

  public Module getModule() {
    return this.module;
  }

  public ModuleStatus getStatus() {
    return this.status;
  }

  public LocalDate getEnrolledAt() {
    return this.enrolledAt;
  }

  public double getPoints() {
    return this.points;
  }

  public void setID(String ID) {
    this.ID = ID;
    update();
  }

  public void setStudent(Student student) {
    this.student = student;
    update();
  }

  public void setModule(Module module) {
    this.module = module;
    update();
  }

  public void setStatus(ModuleStatus status) {
    this.status = status;
    update();
  }

  public void setEnrolledAt(LocalDate enrolledAt) {
    this.enrolledAt = enrolledAt;
    update();
  }

  public void setPoints(double points) {
    this.points = points;
    update();
  }

  public void update() {
    List<String> studentModulesData = Data.fetch(StudentModule.filePath);

    List<String> updatedStudentModulesData = studentModulesData.stream().filter(moduleRow -> {
      List<String> props = List.of(moduleRow.split(", "));
      return !props.get(columnLookup.get("id")).equals(this.ID);
    }).collect(Collectors.toList());

    List<String> updatedProps = new ArrayList<>();
    updatedProps.add(this.ID);
    updatedProps.add(this.student != null ? this.student.getID() : "0");
    updatedProps.add(this.module != null ? this.module.getID() : "0");
    updatedProps.add(this.status.getValue());
    updatedProps.add(this.enrolledAt.format(Helper.dateTimeFormatter));
    updatedProps.add(String.valueOf(this.points));

    updatedStudentModulesData.add(String.join(", ", updatedProps));
    Data.save(StudentModule.filePath, String.join("\n", updatedStudentModulesData));
  }

  public void delete() {
    List<String> studentModulesData = Data.fetch(StudentModule.filePath);

    List<String> updatedStudentModulesData = studentModulesData.stream().filter(moduleRow -> {
      List<String> props = List.of(moduleRow.split(", "));
      return !props.get(columnLookup.get("id")).equals(this.ID);
    }).collect(Collectors.toList());

    Data.save(StudentModule.filePath, String.join("\n", updatedStudentModulesData));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (object == null || getClass() != object.getClass()) return false;
    User that = (User) object;
    return Objects.equals(this.ID, that.ID);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(this.ID);
  }
}
