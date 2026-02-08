package com.apu_afs.Views;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.apu_afs.GlobalState;
import com.apu_afs.Models.Assessment;
import com.apu_afs.Models.AssessmentMark;
import com.apu_afs.Models.Feedback;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Models.Enums.Role;
import com.apu_afs.Views.components.*;

import net.miginfocom.swing.MigLayout;

public class DashboardPage extends JPanel {

  HeaderPanel header;
  NavPanel nav;

  JPanel contentBody;
  
  public DashboardPage(Router router, GlobalState state) {
    super(new MigLayout(
      "fill, insets 0, gap 0",  
        "[][][grow]",              
        "[][grow]"   
    ));

    if (state.getCurrUser() == null) {
      SwingUtilities.invokeLater(() -> {
        router.showView(Pages.LOGIN, state);
      });
      return;
    }

    header = new HeaderPanel(router, state);
    nav = new NavPanel(router, state);

    contentBody = new JPanel(new MigLayout("wrap 3, insets 20, gap 20"));
    contentBody.setBackground(App.slate100);

    // Role-specific dashboard (lecturer)
    if (state.getCurrUser().getRole() == Role.LECTURER) {
      renderLecturerDashboard(router, state);
    } else {
      JLabel temp = new JLabel();
      temp.setText("this is dashboard page" + router.getCurrPage().getDisplay());
      contentBody.add(temp);
    }
    
    this.add(header, "span, growx, wrap");
    this.add(nav, "growy");
    this.add(contentBody, "span, grow");
  }

  private void renderLecturerDashboard(Router router, GlobalState state) {
    // Totals
    List<Assessment> myAssessments = Assessment.getListOfAssessmentsByMatchingValues("lecturer", state.getCurrUser().getID());
    List<AssessmentMark> myMarks = AssessmentMark.fetchAssessmentMarks("", state.getCurrUser());
    List<Feedback> myFeedbacks = Feedback.fetchFeedbacks("", state.getCurrUser());

    int totalAssessments = myAssessments.size();
    int totalMarks = myMarks.size();
    int totalFeedback = myFeedbacks.size();

    // pending feedback = marks without feedback or empty content
    int pendingFeedback = 0;
    for (AssessmentMark mark : myMarks) {
      Feedback f = Feedback.getFeedbackByMatchingValues("assessmentMark", mark.getID());
      if (f == null || f.getContent() == null || f.getContent().trim().isEmpty()) {
        pendingFeedback++;
      }
    }

    // Card design
    java.util.function.BiFunction<String, String, JPanel> makeCard = (title, value) -> {
      JPanel card = new JPanel(new MigLayout("insets 10, align center center"));
      card.setBackground(App.slate200);
      card.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
      JLabel titleLabel = new JLabel(title);
      titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
      JLabel valueLabel = new JLabel(value);
      valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
      card.add(titleLabel, "wrap");
      card.add(valueLabel, "wrap");
      return card;
    };

    JPanel assessmentsCard = makeCard.apply("My Assessments", String.valueOf(totalAssessments));
    JButton gotoAssessments = new JButton("View");
    gotoAssessments.addActionListener(e -> router.showView(Pages.ASSESSMENTS, state));
    assessmentsCard.add(gotoAssessments);

    JPanel marksCard = makeCard.apply("Marks Records", String.valueOf(totalMarks));
    JButton gotoMarks = new JButton("Enter Marks");
    gotoMarks.addActionListener(e -> router.showView(Pages.ENTERMARKS, state));
    marksCard.add(gotoMarks);

    JPanel feedbackCard = makeCard.apply("Feedback Records", String.valueOf(totalFeedback));
    JButton gotoFeedback = new JButton("Provide Feedback");
    gotoFeedback.addActionListener(e -> router.showView(Pages.PROVIDEFEEDBACK, state));
    feedbackCard.add(gotoFeedback);

    JPanel pendingCard = makeCard.apply("Pending Feedback", String.valueOf(pendingFeedback));
    pendingCard.add(new JLabel());

    // Add cards to content
    contentBody.add(assessmentsCard, "grow, pushx");
    contentBody.add(marksCard, "grow, pushx");
    contentBody.add(feedbackCard, "grow, pushx");
    contentBody.add(pendingCard, "grow, pushx");
  }
}
