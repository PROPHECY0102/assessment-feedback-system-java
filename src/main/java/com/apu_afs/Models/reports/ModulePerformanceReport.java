package com.apu_afs.Models.reports;

import com.apu_afs.GlobalState;
import com.apu_afs.Models.AcademicLeader;
import com.apu_afs.Models.AssessmentMark;
import com.apu_afs.Models.Data;
import com.apu_afs.Models.GradeRange;
import com.apu_afs.Models.Module;

import java.util.*;
import java.util.stream.Collectors;

public class ModulePerformanceReport implements Report {

  private GlobalState state;
  private AcademicLeader leader;
  private Map<String, List<AssessmentMark>> marksByModule;

  public GlobalState getState() {
    return state;
  }

  public AcademicLeader getLeader() {
    return leader;
  }

  public Map<String, List<AssessmentMark>> getMarksByModule() {
    return marksByModule;
  }

  public void setState(GlobalState state) {
    this.state = state;
  }

  public void setLeader(AcademicLeader leader) {
    this.leader = leader;
  }

  public void setMarksByModule(Map<String, List<AssessmentMark>> marksByModule) {
    this.marksByModule = marksByModule;
  }

  @Override
  public String getTitle() {
    return "Module Performance Report";
  }

  @Override
  public String[] getColumns() {
    return new String[] {
      "Module Code",
      "Module Name",
      "CGPA",
      "Highest GPA",
      "Lowest GPA",
      "Total Students",
      "Pass",
      "Fail"
    };
  }

  @Override
  public Object[][] generate(GlobalState state, String filterId) {


    setState(state);

    if (!(getState().getCurrUser() instanceof AcademicLeader)) {
      return new Object[0][0];
    }

    setLeader((AcademicLeader) getState().getCurrUser());

    List<String> rawMarks = Data.fetch("data/assessmentMarks.txt");
    if (rawMarks == null || rawMarks.isEmpty()) {
      return new Object[0][0];
    }

    List<AssessmentMark> marks = rawMarks.stream()
      .map(row -> row.split(", "))
      .map(p -> new AssessmentMark(List.of(p)))
      .collect(Collectors.toList());

    setMarksByModule(
      marks.stream()
        .filter(m -> m.getAssessment() != null)
        .filter(m -> m.getAssessment().getModule() != null)
        .collect(Collectors.groupingBy(
          m -> m.getAssessment().getModule().getID()
        ))
    );

    List<Object[]> rows = new ArrayList<>();

    for (String moduleId : getMarksByModule().keySet()) {

      Module module =
        Module.getModuleByMatchingValues("id", moduleId);
      if (module == null) continue;


      if (module.getLeader() == null ||
          !module.getLeader().getID().equals(getLeader().getID())) {
        continue;
      }

      List<AssessmentMark> moduleMarks =
        getMarksByModule().get(moduleId);


      List<Double> gpas = moduleMarks.stream()
        .map(m -> {
          GradeRange gr =
            GradeRange.getListOfGradeRanges().stream()
              .filter(r ->
                m.getPercentage() >= r.getMin() &&
                m.getPercentage() <= r.getMax()
              )
              .findFirst()
              .orElse(null);
          return gr != null ? gr.getPoints() : null;
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

      if (gpas.isEmpty()) continue;

      double avgGpa =
        gpas.stream().mapToDouble(Double::doubleValue).average().orElse(0);
      double maxGpa =
        gpas.stream().mapToDouble(Double::doubleValue).max().orElse(0);
      double minGpa =
        gpas.stream().mapToDouble(Double::doubleValue).min().orElse(0);


      int pass = 0;
      int fail = 0;

      for (AssessmentMark m : moduleMarks) {
        GradeRange gr =
          GradeRange.getListOfGradeRanges().stream()
            .filter(r ->
              m.getPercentage() >= r.getMin() &&
              m.getPercentage() <= r.getMax()
            )
            .findFirst()
            .orElse(null);

        if (gr == null) continue;

        if (gr.getDescription().toLowerCase().contains("fail")) {
          fail++;
        } else {
          pass++;
        }
      }

      rows.add(new Object[] {
        module.getCode(),                       
        module.getTitle(),                      
        String.format("%.2f", avgGpa),
        String.format("%.2f", maxGpa),
        String.format("%.2f", minGpa),
        moduleMarks.size(),
        pass,
        fail
      });
    }

    return rows.toArray(new Object[0][]);
  }
}
