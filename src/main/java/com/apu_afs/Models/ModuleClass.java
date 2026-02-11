package com.apu_afs.Models;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.apu_afs.Helper;

public class ModuleClass {
  private String id;
  private String classCode;
  private String day;
  private LocalTime startTime;
  private LocalTime endTime;
  private String classroom;   
  private Module module;    
  private Lecturer lecturer;  

  private static final String filePath = "data/classes.txt";
  public static final Map<String, Integer> columnLookup = Map.ofEntries(
    Map.entry("id", 0),
    Map.entry("classCode", 1),
    Map.entry("day", 2),
    Map.entry("startTime", 3),
    Map.entry("endTime", 4),
    Map.entry("classroom", 5),
    Map.entry("moduleID", 6),
    Map.entry("lecturerID", 7)
  );

  public ModuleClass(List<String> props) {
    this.id = props.get(columnLookup.get("id")).trim();
    this.classCode = props.get(columnLookup.get("classCode")).trim();
    this.day = props.get(columnLookup.get("day")).trim();
    this.startTime = LocalTime.parse(props.get(columnLookup.get("startTime")).trim());
    this.endTime = LocalTime.parse(props.get(columnLookup.get("endTime")).trim());
    this.classroom = props.get(columnLookup.get("classroom")).trim();
    this.module = Module.getModuleByMatchingValues("id", props.get(columnLookup.get("moduleID")).trim());
    User potentialUser = User.getUserByMatchingValues("id", props.get(columnLookup.get("lecturerID")).trim());
    if (potentialUser instanceof Lecturer lecturer) {
      this.lecturer = lecturer;
    }
  }

  public ModuleClass(HashMap<String, String> inputValues) {
    String moduleClassID;
    if (inputValues.get("id") == null) {
      IDIncrement idIncrement = new IDIncrement();
      moduleClassID = String.valueOf(idIncrement.getModuleClassID());
    } else {
      moduleClassID = inputValues.get("id");
    }

    this.id = moduleClassID;
    this.classCode = inputValues.get("classCode");
    this.day = inputValues.get("day");
    this.startTime = LocalTime.parse(inputValues.get("startTime"));
    this.endTime = LocalTime.parse(inputValues.get("endTime"));
    this.classroom = inputValues.get("classroom");
    this.module = Module.getModuleByMatchingValues("id", inputValues.get("moduleID"));
    User potentialUser = User.getUserByMatchingValues("id", inputValues.get("lecturerID"));
    if (potentialUser instanceof Lecturer lecturer) {
      this.lecturer = lecturer;
    }
  }

  public static List<ModuleClass> fetchAll() {

    List<String> data = Data.fetch(filePath);
    List<ModuleClass> classes = new ArrayList<>();

    if (data == null || data.isEmpty()) return classes;

    for (String row : data) {
      if (row.isBlank()) continue;
      classes.add(new ModuleClass(List.of(row.split(", "))));
    }

    return classes;
  }

  public static List<ModuleClass> fetch(String search) {
    List<String> moduleClassData = Data.fetch(ModuleClass.filePath);
    List<ModuleClass> classes = new ArrayList<>();

    for (String moduleClassRow : moduleClassData) {
      List<String> props = List.of(moduleClassRow.split(", "));
      classes.add(new ModuleClass(props));
    }

    List<ModuleClass> searchResult = classes.stream().filter(moduleClass -> {
      return (moduleClass.getModule().getCode().toLowerCase().contains(search.toLowerCase()) ||
        moduleClass.getModule().getTitle().toLowerCase().contains(search.toLowerCase()) ||
        (moduleClass.getLecturer().getFirstName() + moduleClass.getLecturer().getLastName()).toLowerCase().contains(search.toLowerCase()) ||
        (moduleClass.getLecturer().getAcademicLeader() != null && moduleClass.getLecturer().getAcademicLeader().getFaculty().toLowerCase().contains(search.toLowerCase())));
      }).collect(Collectors.toList());

    return searchResult;
  }

  public static ModuleClass getModuleClassbyMatchingValues(String column, String value) {
    List<String> moduleClassData = Data.fetch(ModuleClass.filePath);

    for (String moduleClassRow : moduleClassData) {
      List<String> props = List.of(moduleClassRow.split(", "));
      if (props.get(columnLookup.get(column)).trim().equals(value)) {
        return new ModuleClass(props);
      }
    }

    return null;
  }

  public static Validation validateModuleClass(HashMap<String, String> inputValues) {
    Validation cannotBeEmptyCheck = Validation.isEmptyCheck(new String[] {"classCode", "day", "startTime", "endTime", "classroom"}, inputValues);
    if (!cannotBeEmptyCheck.getSuccess()) {
      return cannotBeEmptyCheck;
    }

    Validation validTimeCheck = Validation.validTimeCheck(new String[] {"startTime", "endTime"}, inputValues);
    if (!validTimeCheck.getSuccess()) {
      return validTimeCheck;
    }

    return new Validation("Success", true);
  }


  public String getId() { return id; }
  public String getClassCode() { return classCode; }
  public String getDay() { return day; }
  public LocalTime getStartTime() { return startTime; }
  public LocalTime getEndTime() { return endTime; }
  public String getClassroom() { return classroom; }
  public Module getModule() { return module; }
  public Lecturer getLecturer() { return lecturer; }

  public void setId(String id) {
    this.id = id;
    this.update();
  }

  public void setClassCode(String classCode) {
    this.classCode = classCode;
    this.update();
  }

  public void setDay(String day) {
    this.day = day;
    this.update();
  }

  public void setStartTime(LocalTime startTime) {
    this.startTime = startTime;
    this.update();
  }

  public void setEndTime(LocalTime endTime) {
    this.endTime = endTime;
    this.update();
  }

  public void setClassroom(String classroom) {
    this.classroom = classroom;
    this.update();
  }

  public void setModule(Module module) {
    this.module = module;
    this.update();
  }

  public void setLecturer(Lecturer lecturer) {
    this.lecturer = lecturer;
    this.update();
  }

  public void update() {
    List<String> moduleClassesData = Data.fetch(ModuleClass.filePath);
    
    // To remove the existing ModuleClass old records out of the ModuleClasses list
    List<String> updatedmoduleClassesData = moduleClassesData.stream().filter((moduleRow) -> {
      List<String> props = List.of(moduleRow.split(", "));
      return !props.get(columnLookup.get("id")).trim().equals(this.id);
    }).collect(Collectors.toCollection(ArrayList::new));

    List<String> moduleClassProps = new ArrayList<>();
    moduleClassProps.add(this.id);
    moduleClassProps.add(this.classCode);
    moduleClassProps.add(this.day);
    moduleClassProps.add(this.startTime.format(Helper.timeFormatter));
    moduleClassProps.add(this.endTime.format(Helper.timeFormatter));
    moduleClassProps.add(this.classroom);
    moduleClassProps.add(this.module.getID());
    moduleClassProps.add(this.lecturer.getID());

    updatedmoduleClassesData.add(String.join(", ", moduleClassProps));
    Data.save(ModuleClass.filePath, String.join("\n", updatedmoduleClassesData));
  }

  public void delete() {
    List<String> moduleClassesData = Data.fetch(ModuleClass.filePath);
    
    // To remove the existing ModuleClass old records out of the ModuleClasses list
    List<String> updatedmoduleClassesData = moduleClassesData.stream().filter((moduleRow) -> {
      List<String> props = List.of(moduleRow.split(", "));
      return !props.get(columnLookup.get("id")).trim().equals(this.id);
    }).collect(Collectors.toCollection(ArrayList::new));

    Data.save(ModuleClass.filePath, String.join("\n", updatedmoduleClassesData));
  }

}
