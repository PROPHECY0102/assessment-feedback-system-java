package com.apu_afs.Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.apu_afs.Helper;
import com.apu_afs.Models.Enums.Role;

public class Module {
  private String ID;
  private String code;
  private String title;
  private String description;
  private double creditHours;
  private LocalDate createdAt;
  private AcademicLeader leader;


  public static Map<String, Integer> columnLookup = Map.ofEntries(
    Map.entry("id", 0),
    Map.entry("code", 1),
    Map.entry("title", 2),
    Map.entry("description", 3),
    Map.entry("creditHours", 4),
    Map.entry("createdAt", 5),
    Map.entry("leaderID", 6)
  );

  private static String filePath = "data/modules.txt";

  public Module(List<String> props) {
    this.ID = props.get(columnLookup.get("id")).trim();
    this.code = props.get(columnLookup.get("code")).trim();
    this.title = props.get(columnLookup.get("title")).trim();
    this.description = Helper.saveDecode(props.get(columnLookup.get("description")).trim());
    this.creditHours = Double.parseDouble(props.get(columnLookup.get("creditHours")).trim());
    this.createdAt = LocalDate.parse(props.get(columnLookup.get("createdAt")).trim(), Helper.dateTimeFormatter);
    User potentialLeader = User.getUserByMatchingValues("id", props.get(columnLookup.get("leaderID")).trim());
    if (potentialLeader instanceof AcademicLeader leader) {
      this.leader = leader;
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

  }

  public static Module getModuleByMatchingValues(String column, String value) {
    List<String> modulesData = Data.fetch(Module.filePath);
    
    for (String modulesRow : modulesData) {
      List<String> props = List.of(modulesRow.split(", "));
      if (props.get(columnLookup.get(column)).trim().equals(value)) {
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
      if (props.get(columnLookup.get(column)).trim().equals(value)) {
        modules.add(new Module(props));
      }
    }

    return modules;
  }

  public static List<Module> fetchAllModules() {
    List<String> modulesData = Data.fetch(Module.filePath);
    List<Module> modules = new ArrayList<>();

    for (String moduleRow : modulesData) {
      List<String> props = List.of(moduleRow.split(", "));
      modules.add(new Module(props));
    }

    return modules;
  }

public static List<Module> fetchModules(String search, User currUser) {
  List<String> modulesData = Data.fetch(Module.filePath);
  List<Module> modules = new ArrayList<>();

  for (String modulesRow : modulesData) {
    List<String> props = List.of(modulesRow.split(", "));

      // Lecturer → module 
      if (currUser.getRole() == Role.LECTURER) {

      boolean teachesModule = ModuleLecturer.fetchAll().stream()
          .anyMatch(ml ->
              ml.getModuleID().equals(props.get(columnLookup.get("id"))) &&
              ml.getLecturerID().equals(currUser.getID())
          );


    if (teachesModule) {
        modules.add(new Module(props));
    }
}
      // Academic Leader → module lead
      else if (currUser.getRole() == Role.ACADEMIC_LEADER) {
        if (props.get(columnLookup.get("leaderID")).trim().equals(currUser.getID().trim())){
          modules.add(new Module(props));
        }
      }
      else {
        modules.add(new Module(props));
      }
    }

    return modules.stream()
      .filter(module ->
        module.getCode().toLowerCase().contains(search.toLowerCase()) ||
        module.getTitle().toLowerCase().contains(search.toLowerCase())
      )
      .collect(Collectors.toList());
  }
    public List<Lecturer> getLecturers() {
        return ModuleLecturer.fetchAll().stream()
            .filter(ml -> ml.getModuleID().equals(this.ID))
            .map(ml -> User.getUserByMatchingValues("id", ml.getLecturerID()))
            .filter(u -> u instanceof Lecturer)
            .map(u -> (Lecturer) u)
            .distinct()
            .collect(Collectors.toList());
    }






  public static Validation validate(HashMap<String, String> inputValues) {
    Validation cannotBeEmptyCheck = Validation.isEmptyCheck(new String[] {"code", "title", "description", "creditHours"}, inputValues);
    if (!cannotBeEmptyCheck.getSuccess()) {
      return cannotBeEmptyCheck;
    }

    Validation minLengthCheck = Validation.minLengthCheck(new String[] {"code", "title"}, inputValues, 3);
    if (!minLengthCheck.getSuccess()) {
      return minLengthCheck;
    }

    Validation maxLengthCheck = Validation.maxLengthCheck(new String[] {"code", "title"}, inputValues, 50);
    if (!maxLengthCheck.getSuccess()) {
      return maxLengthCheck;
    }

    Validation validDoubleCheck = Validation.validDoubleCheck(new String[] {"creditHours"}, inputValues);
    if (!validDoubleCheck.getSuccess()) {
      return validDoubleCheck;
    }

    Validation validDateCheck = Validation.validDateCheck(new String[] {"createdAt"}, inputValues);
    if (!validDateCheck.getSuccess()) {
      return validDateCheck;
    }

    return new Validation("Success! No Invalid Input", true);
  }

  public String getID() {
    return ID;
  }

  public String getCode() {
    return code;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public double getCreditHours() {
    return creditHours;
  }

  public LocalDate getCreatedAt() {
    return createdAt;
  }

  public AcademicLeader getLeader() {
    return leader;
  }



  public void setID(String ID) {
    this.ID = ID;
    update();
  }

  public void setCode(String code) {
    this.code = code;
    update();
  }

  public void setTitle(String title) {
    this.title = title;
    update();
  }

  public void setDescription(String description) {
    this.description = description;
    update();
  }

  public void setCreditHours(double creditHours) {
    this.creditHours = creditHours;
    update();
  }

  public void setCreatedAt(LocalDate createdAt) {
    this.createdAt = createdAt;
    update();
  }

  public void setLeader(AcademicLeader leader) {
    this.leader = leader;
    update();
  }

  public void update() {
    List<String> moduleData = Data.fetch(Module.filePath);

    List<String> updatedModuleData = moduleData.stream().filter(moduleRow -> {
      List<String> props = List.of(moduleRow.split(", "));
      return !props.get(columnLookup.get("id")).equals(this.ID);
    }).collect(Collectors.toList());

    List<String> updatedProps = new ArrayList<>();
    updatedProps.add(this.ID);
    updatedProps.add(this.code);
    updatedProps.add(this.title);
    updatedProps.add(Helper.saveEncode(this.description));
    updatedProps.add(String.valueOf(this.creditHours));
    updatedProps.add(this.createdAt.format(Helper.dateTimeFormatter));
    updatedProps.add(this.leader != null ? this.leader.getID() : "0");
 
    updatedModuleData.add(String.join(", ", updatedProps));
    Data.save(Module.filePath, String.join("\n", updatedModuleData));
  }

  public void delete() {
    List<String> moduleData = Data.fetch(Module.filePath);

    List<String> updatedModuleData = moduleData.stream().filter(moduleRow -> {
      List<String> props = List.of(moduleRow.split(", "));
      return !props.get(columnLookup.get("id")).equals(this.ID);
    }).collect(Collectors.toList());

    Data.save(Module.filePath, String.join("\n", updatedModuleData));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Module)) return false;
    Module module = (Module) o;
    return Objects.equals(this.ID, module.ID);
  }

  
  @Override
  public int hashCode() {
    return Objects.hash(this.ID);
  }

  @Override
  public String toString() {
    return this.code + " - " + this.title;
  }

  

}
