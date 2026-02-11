package com.apu_afs.Models.reports;

import com.apu_afs.GlobalState;
import com.apu_afs.Models.Data;
import com.apu_afs.Models.Module;

import java.util.*;

public class ClassEnrollmentReport implements Report {

  private GlobalState state;
  private String filterId;

  public GlobalState getState() {
    return state;
  }

  public String getFilterId() {
    return filterId;
  }

  public void setState(GlobalState state) {
    this.state = state;
  }

  public void setFilterId(String filterId) {
    this.filterId = filterId;
  }

  @Override
  public String getTitle() {
    return "Class Enrollment Report";
  }

  @Override
  public String[] getColumns() {
    return new String[] {
      "Module Code",
      "Module Name",
      "Class Code",
      "Total Students"
    };
  }

  @Override
  public Object[][] generate(GlobalState state, String filterId) {

    // guna setter
    setState(state);
    setFilterId(filterId);

    List<String> rows = Data.fetch("data/studentModules.txt");

    if (rows == null || rows.isEmpty()) {
      return new Object[0][0];
    }

    Map<String, Set<String>> grouped = new HashMap<>();

    for (String row : rows) {

      String[] p = row.split(", ");
      if (p.length < 5) continue;

      String studentId = p[1];
      String moduleId = p[2];
      String classCode = p[3];
      String status = p[4];

      if (!"active".equalsIgnoreCase(status)) continue;

      String key = moduleId + "|" + classCode;

      grouped
        .computeIfAbsent(key, k -> new HashSet<>())
        .add(studentId);
    }

    Object[][] data = new Object[grouped.size()][4];
    int i = 0;

    for (Map.Entry<String, Set<String>> entry : grouped.entrySet()) {

      String[] keyParts = entry.getKey().split("\\|");
      String moduleId = keyParts[0];
      String classCode = keyParts[1];

      Module module =
        Module.getModuleByMatchingValues("id", moduleId);

      data[i][0] = module != null ? module.getCode() : moduleId;   
      data[i][1] = module != null ? module.getTitle() : "Unknown Module"; 
      data[i][2] = classCode;
      data[i][3] = entry.getValue().size();

      i++;
    }

    return data;
  }
}
