package com.apu_afs.Models.reports;

import com.apu_afs.GlobalState;
import com.apu_afs.Models.Data;
import com.apu_afs.Models.Feedback;
import com.apu_afs.Models.StudentComment;

import java.util.ArrayList;
import java.util.List;

public class FeedbackSummaryReport implements Report {

  private static final String STUDENT_COMMENT_FILE =
    System.getProperty("user.dir") + "/data/studentComments.txt";

  private static final String FEEDBACK_FILE =
    System.getProperty("user.dir") + "/data/feedbacks.txt";

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
    return "Feedback Summary";
  }

  @Override
  public String[] getColumns() {
    return new String[] { "ID", "Module", "Content", "Date" };
  }

  @Override
  public Object[][] generate(GlobalState state, String filterId) {

    setState(state);
    setFilterId(filterId);


    // student
    if ("student".equalsIgnoreCase(getFilterId())) {

      List<String> raw = Data.fetch(STUDENT_COMMENT_FILE);
      if (raw == null || raw.size() <= 1) {
        return new Object[0][0];
      }

      List<Object[]> rows = new ArrayList<>();

      for (int i = 1; i < raw.size(); i++) {
        List<String> props = List.of(raw.get(i).split(", ", 5));
        StudentComment sc = new StudentComment(props);

        String moduleTitle =
          sc.getModule() != null
            ? sc.getModule().getTitle()
            : "-";

        rows.add(new Object[] {
          sc.getID(),           // ID
          moduleTitle,          // Module
          sc.getComment(),      // Content
          sc.getCommentedAt()   // Date
        });
      }

      return rows.toArray(new Object[0][0]);
    }

    //lecture
    List<String> raw = Data.fetch(FEEDBACK_FILE);
    if (raw == null || raw.size() <= 1) {
      return new Object[0][0];
    }

    List<Object[]> rows = new ArrayList<>();

    for (int i = 1; i < raw.size(); i++) {
      List<String> props = List.of(raw.get(i).split(", ", 5));
      Feedback fb = new Feedback(props);

      String moduleTitle =
        fb.getAssessmentMark() != null &&
        fb.getAssessmentMark().getAssessment() != null &&
        fb.getAssessmentMark().getAssessment().getModule() != null
          ? fb.getAssessmentMark().getAssessment().getModule().getTitle()
          : "-";

      rows.add(new Object[] {
        fb.getID(),            // ID
        moduleTitle,           // Module
        fb.getContent(),       // Content
        fb.getUpdatedAt()      // Date
      });
    }

    return rows.toArray(new Object[0][0]);
  }
}
