package com.apu_afs.Models.reports;

import com.apu_afs.GlobalState;
import com.apu_afs.Models.Lecturer;
import com.apu_afs.Models.ModuleClass;
import com.apu_afs.Models.User;

import java.time.Duration;
import java.util.*;

public class LecturerWorkloadReport implements Report {

  private GlobalState state;
  private Map<String, Integer> totalClasses;
  private Map<String, Long> totalMinutes;
  private Map<String, Set<String>> moduleMap;

  public GlobalState getState() {
    return state;
  }

  public Map<String, Integer> getTotalClasses() {
    return totalClasses;
  }

  public Map<String, Long> getTotalMinutes() {
    return totalMinutes;
  }

  public Map<String, Set<String>> getModuleMap() {
    return moduleMap;
  }

  public void setState(GlobalState state) {
    this.state = state;
  }

  public void setTotalClasses(Map<String, Integer> totalClasses) {
    this.totalClasses = totalClasses;
  }

  public void setTotalMinutes(Map<String, Long> totalMinutes) {
    this.totalMinutes = totalMinutes;
  }

  public void setModuleMap(Map<String, Set<String>> moduleMap) {
    this.moduleMap = moduleMap;
  }

  @Override
  public String getTitle() {
    return "Lecturer Workload Report";
  }

  @Override
  public String[] getColumns() {
    return new String[] {
      "Lecturer",
      "Total Classes",
      "Total Modules",
      "Total Hours"
    };
  }

  @Override
  public Object[][] generate(GlobalState state, String filterId) {


    setState(state);
    setTotalClasses(new HashMap<>());
    setTotalMinutes(new HashMap<>());
    setModuleMap(new HashMap<>());

    List<ModuleClass> classes = ModuleClass.fetchAll();

    for (ModuleClass mc : classes) {

      String lecturerId = mc.getLecturer().getID();
      if (lecturerId == null || lecturerId.isBlank() || lecturerId.equals("0"))
        continue;

      // total classes
      getTotalClasses().put(
        lecturerId,
        getTotalClasses().getOrDefault(lecturerId, 0) + 1
      );

      // total minutes
      long minutes = Duration
        .between(mc.getStartTime(), mc.getEndTime())
        .toMinutes();

      getTotalMinutes().put(
        lecturerId,
        getTotalMinutes().getOrDefault(lecturerId, 0L) + minutes
      );

      getModuleMap().putIfAbsent(lecturerId, new HashSet<>());
      getModuleMap().get(lecturerId).add(mc.getModule().getID());
    }

    Object[][] data = new Object[getTotalClasses().size()][4];
    int row = 0;

    for (String lecturerId : getTotalClasses().keySet()) {

      User u = User.getUserByMatchingValues("id", lecturerId);
      if (!(u instanceof Lecturer)) continue;

      Lecturer l = (Lecturer) u;

      long mins = getTotalMinutes().getOrDefault(lecturerId, 0L);

      data[row][0] = l.getFirstName() + " " + l.getLastName(); 
      data[row][1] = getTotalClasses().get(lecturerId);
      data[row][2] = getModuleMap().get(lecturerId).size();
      data[row][3] = formatDuration(mins);

      row++;
    }

    return data;
  }

  private String formatDuration(long totalMinutes) {

    long hours = totalMinutes / 60;
    long minutes = totalMinutes % 60;

    if (hours > 0 && minutes > 0) {
      return hours + " Hours " + minutes + " Minutes";
    }

    if (hours > 0) {
      return hours + (hours == 1 ? " Hour" : " Hours");
    }

    return minutes + " Minutes";
  }
}
