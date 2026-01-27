package com.apu_afs.Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.apu_afs.Helper;

public class Module {
  String ID;
  String code;
  String title;
  String description;
  double creditHours;
  LocalDate createdAt;
  AcademicLeader leader;
  Lecturer instructor;

  public static Map<String, Integer> columnLookup = Map.ofEntries(
    Map.entry("id", 0),
    Map.entry("code", 1),
    Map.entry("title", 2),
    Map.entry("description", 3),
    Map.entry("creditHours", 4),
    Map.entry("createdAt", 5),
    Map.entry("leaderID", 6),
    Map.entry("instructorID", 7)
  );

  private static String filePath = "data/modules.txt";

  public Module(List<String> props) {
    this.ID = props.get(columnLookup.get("id")).trim();
    this.code = props.get(columnLookup.get("code")).trim();
    this.title = props.get(columnLookup.get("title")).trim();
    this.description = props.get(columnLookup.get("description")).trim();
    this.creditHours = Double.parseDouble(props.get(columnLookup.get("creditHours")).trim());
    this.createdAt = LocalDate.parse(props.get(columnLookup.get("createdAt")).trim(), Helper.dateTimeFormatter);
    User potentialLeader = User.getUserByMatchingValues("id", props.get(columnLookup.get("leaderID")).trim());
    if (potentialLeader instanceof AcademicLeader leader) {
      this.leader = leader;
    }
    User potentialInstructor = User.getUserByMatchingValues("id", props.get(columnLookup.get("instructorID")).trim());
    if (potentialInstructor instanceof Lecturer instructor) {
      this.instructor = instructor;
    }
  }

  public Module(HashMap<String, String> inputValues) {
    String moduleID;
    if (inputValues.get("id") == null) {
      IDIncrement idIncrement = new IDIncrement();
      moduleID = String.valueOf(idIncrement.getModuleID());
    } else {
      moduleID = inputValues.get("id");
    }

    this.ID = moduleID;
    this.code = inputValues.get("code");
    this.title = inputValues.get("title");
    this.description = inputValues.get("description");
    this.creditHours = Double.parseDouble(inputValues.get("creditHours"));
    this.createdAt = LocalDate.parse(inputValues.get("createdAt"), Helper.dateTimeFormatter);
    User potentialLeader = User.getUserByMatchingValues("id", inputValues.get("leaderID"));
    if (potentialLeader instanceof AcademicLeader leader) {
      this.leader = leader;
    }
    User potentialInstructor = User.getUserByMatchingValues("id", inputValues.get("instructorID"));
    if (potentialInstructor instanceof Lecturer instructor) {
      this.instructor = instructor;
    }
  }

  public static Module getModuleByMatchingValues(String column, String value) {
    List<String> modulesData = Data.fetch(Module.filePath);
    
    for (String modulesRow : modulesData) {
      List<String> props = List.of(modulesRow.split(", "));
      if (props.get(columnLookup.get(column)).equals(value)) {
        return new Module(props);
      }
    }

    return null;
  }

  public static List<Module> getListOfModuleByMatchingValues(String column, String value) {
    List<String> modulesData = Data.fetch(Module.filePath);
    List<Module> modules = new ArrayList<>();
    
    for (String modulesRow : modulesData) {
      List<String> props = List.of(modulesRow.split(", "));
      if (props.get(columnLookup.get(column)).equals(value)) {
        modules.add(new Module(props));
      }
    }

    return modules;
  }

  public static List<Module> fetchModules(String search) {
    List<String> modulesData = Data.fetch(Module.filePath);
    List<Module> modules = new ArrayList<>();
    
    for (String modulesRow : modulesData) {
      List<String> props = List.of(modulesRow.split(", "));
      modules.add(new Module(props)); 
    }

    // List<Module> searchResult = modules.stream().filter(module -> {
      
    // });

    return modules;
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
