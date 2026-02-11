package com.apu_afs.Models.reports;

import com.apu_afs.GlobalState;
import com.apu_afs.Models.AcademicLeader;
import com.apu_afs.Models.Assessment;
import com.apu_afs.Models.AssessmentMark;
import com.apu_afs.Models.GradeRange;
import com.apu_afs.Models.Module;
import com.apu_afs.Models.Data;

import java.util.*;

public class GradeDistributionReport implements Report {

  private GlobalState state;
  private String moduleFilter;
  private String lecturerFilter;

  private static final List<String> GRADE_ORDER = List.of(
  "A+", "A", "A-",
  "B+", "B", "B-",
  "C+", "C", "C-",
  "D+", "D",
  "F"
);


  public GlobalState getState() {
    return state;
  }

  public String getModuleFilter() {
    return moduleFilter;
  }

  public String getLecturerFilter() {
    return lecturerFilter;
  }


  public void setState(GlobalState state) {
    this.state = state;
  }

  public void setModuleFilter(String moduleFilter) {
    this.moduleFilter = moduleFilter;
  }

  public void setLecturerFilter(String lecturerFilter) {
    this.lecturerFilter = lecturerFilter;
  }

  @Override
  public String getTitle() {
    return "Grade Distribution Report";
  }

  @Override
  public String[] getColumns() {
    return new String[] {
      "Grade",
      "Total Students"
    };
  }

  @Override
  public Object[][] generate(GlobalState state, String filterId) {


    setState(state);

    if (!(getState().getCurrUser() instanceof AcademicLeader)) {
      return new Object[0][0];
    }

    AcademicLeader leader =
        (AcademicLeader) getState().getCurrUser();

    if (filterId != null) {
      String[] parts = filterId.split("\\|");
      setModuleFilter(parts[0]);
      setLecturerFilter(parts[1]);
    }

    List<String> raw =
        Data.fetch("data/assessmentMarks.txt");

    Map<String, Integer> gradeCount = new HashMap<>();

    for (String row : raw) {

        List<String> p = List.of(row.split(", "));
        AssessmentMark mark = new AssessmentMark(p);

        if (mark.getAssessment() == null ||
            mark.getAssessment().getModule() == null ||
            mark.getStudent() == null)
            continue;

        Assessment assessment = mark.getAssessment();
        Module module = assessment.getModule();

        // leader check
        if (module.getLeader() == null ||
            !module.getLeader().getID().equals(leader.getID()))
            continue;

        // module filter
        if (!module.getID().equals(getModuleFilter()))
            continue;

        // lecturer filter
        if (!getLecturerFilter().equals("all") &&
            (assessment.getLecturer() == null ||
            !assessment.getLecturer().getID().equals(getLecturerFilter())))
            continue;

        String grade =
            GradeRange.getGradeForPercentage(mark.getPercentage());

        gradeCount.put(
            grade,
            gradeCount.getOrDefault(grade, 0) + 1
        );
    }


    List<Object[]> rows = new ArrayList<>();

    for (String grade : GRADE_ORDER) {
      if (gradeCount.containsKey(grade)) {
        rows.add(new Object[] {
          grade,
          gradeCount.get(grade)
        });
      }
    }

    Object[][] data = new Object[rows.size()][2];
    for (int i = 0; i < rows.size(); i++) {
      data[i] = rows.get(i);
    }

    return data;

    }

}
