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
    this.description = props.get(columnLookup.get("description")).trim().replace("|", "\n");
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

  public static List<Module> fetchModules(String search, User currUser) {
    List<String> modulesData = Data.fetch(Module.filePath);
    List<Module> modules = new ArrayList<>();

    boolean filterOnlyCurrLecturer = currUser.getRole() == Role.LECTURER;
    
    for (String modulesRow : modulesData) {
      List<String> props = List.of(modulesRow.split(", "));
      if (filterOnlyCurrLecturer) {
        if (props.get(columnLookup.get("instructorID")).equals(currUser.getID())) {
          modules.add(new Module(props));
        }
      } else {
        modules.add(new Module(props));
      }
    }

    List<Module> searchResult = modules.stream()
    .filter(module -> {
      return module.getCode().toLowerCase().contains(search.toLowerCase()) ||
      module.getTitle().toLowerCase().contains(search.toLowerCase()) ||
      String.valueOf(module.getCreditHours()).contains(search.toLowerCase()) ||
      (module.getLeader() != null && module.getLeader().getFaculty().contains(search.toLowerCase())) ||
      (module.getLeader() != null && (module.getLeader().getFirstName() + module.getLeader().getLastName()).contains(search.toLowerCase())) ||
      (module.getInstructor() != null && (module.getInstructor().getFirstName() + module.getInstructor().getLastName()).contains(search.toLowerCase()));
    }).collect(Collectors.toList());

    return searchResult;
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

  public Lecturer getInstructor() {
    return instructor;
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

  public void setInstructor(Lecturer instructor) {
    this.instructor = instructor;
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
    updatedProps.add(this.description.replace("\n", "|"));
    updatedProps.add(String.valueOf(this.creditHours));
    updatedProps.add(this.createdAt.format(Helper.dateTimeFormatter));
    updatedProps.add(this.leader != null ? this.leader.getID() : "0");
    updatedProps.add(this.instructor != null ? this.instructor.getID() : "0");

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
