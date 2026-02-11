package com.apu_afs.Models;

import java.util.ArrayList;
import java.util.List;

public class ModuleLecturer {

  private String moduleID;
  private String lecturerID;

  private static final String filePath =
      System.getProperty("user.dir") + "/data/moduleLecturers.txt";

  
  public ModuleLecturer() {
  }


  public ModuleLecturer(String moduleID, String lecturerID) {
    this.moduleID = moduleID;
    this.lecturerID = lecturerID;
  }

  //GETTER
  public String getModuleID() {
    return moduleID;
  }

  public String getLecturerID() {
    return lecturerID;
  }

  //SETTERS
  public void setModuleID(String moduleID) {
    this.moduleID = moduleID;
  }

  public void setLecturerID(String lecturerID) {
    this.lecturerID = lecturerID;
  }


  public static List<ModuleLecturer> fetchAll() {

    List<String> raw = Data.fetch(filePath);
    List<ModuleLecturer> list = new ArrayList<>();

    if (raw == null || raw.size() <= 1) {
      return list;
    }

    for (int i = 1; i < raw.size(); i++) {
      String row = raw.get(i).trim();
      if (row.isEmpty()) continue;

      String[] p = row.split(", ");
      if (p.length < 2) continue;

      ModuleLecturer ml = new ModuleLecturer();
      ml.setModuleID(p[0]);       
      ml.setLecturerID(p[1]);     
      list.add(ml);
    }

    return list;
  }

  public static List<ModuleLecturer> fetchLecturersByModule(String moduleID) {
    List<String> moduleLecturerData = Data.fetch(ModuleLecturer.filePath);
    List<ModuleLecturer> moduleLecturers = new ArrayList<>();

    for (String moduleLecturerRow : moduleLecturerData) {
      List<String> props = List.of(moduleLecturerRow.split(", "));
      if (props.get(0).trim().equals(moduleID)) {
        moduleLecturers.add(new ModuleLecturer(props.get(0).trim(), props.get(1).trim()));
      }
    }

    return moduleLecturers;
  }


  public void save() {

    List<String> fetched = Data.fetch(filePath);
    List<String> data = new ArrayList<>(fetched == null ? List.of() : fetched);

    if (data.isEmpty()) {
      data.add("moduleID, lecturerID");
    }

    String record = getModuleID() + ", " + getLecturerID(); 

    for (int i = 1; i < data.size(); i++) {
      if (data.get(i).trim().equals(record)) {
        return;
      }
    }

    data.add(record);
    Data.save(filePath, String.join("\n", data));
  }


  public void delete() {

    List<String> fetched = Data.fetch(filePath);
    List<String> raw = new ArrayList<>(fetched == null ? List.of() : fetched);

    if (raw.isEmpty()) return;

    List<String> updated = new ArrayList<>();
    updated.add(raw.get(0)); 

    String target = getModuleID() + ", " + getLecturerID(); 

    for (int i = 1; i < raw.size(); i++) {
      if (!raw.get(i).trim().equals(target)) {
        updated.add(raw.get(i));
      }
    }

    Data.save(filePath, String.join("\n", updated));
  }
}
