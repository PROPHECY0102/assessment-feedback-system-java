package com.apu_afs.Views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;


import com.apu_afs.GlobalState;
import com.apu_afs.Models.AcademicLeader;
import com.apu_afs.Models.Assessment;
import com.apu_afs.Models.AssessmentMark;
import com.apu_afs.Models.Feedback;
import com.apu_afs.Models.ModuleClass;
import com.apu_afs.Models.Student;
import com.apu_afs.Models.StudentModule;
import com.apu_afs.Models.User;
import com.apu_afs.Models.Data;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Models.Enums.Role;
import com.apu_afs.Views.components.*;

import net.miginfocom.swing.MigLayout;

public class DashboardPage extends JPanel {

  HeaderPanel header;
  NavPanel nav;
  JPanel contentBody;

  JPanel firstRow;
  JPanel usersPanel;
  JLabel usersLabel;
  JLabel usersValue;
  JButton manageUsersBtn;
  JPanel moduleClassesPanel;
  JLabel moduleClassesLabel;
  JLabel moduleClassesValue;
  JButton moduleClassesBtn;

  JPanel secondRow;
  JPanel adminPanel;
  JLabel adminLabel;
  JLabel adminValue;
  JButton manageAdminBtn;
  JPanel academicPanel;
  JLabel academicLabel;
  JLabel academicValue;
  JButton manageAcademicBtn;
  JPanel lecturerPanel;
  JLabel lecturerLabel;
  JLabel lecturerValue;
  JButton manageLecturerBtn;
  JPanel studentPanel;
  JLabel studentLabel;
  JLabel studentValue;
  JButton manageStudentBtn;

  public DashboardPage(Router router, GlobalState state) {
    super(new MigLayout(
      "fill, insets 0, gap 0",
      "[][][grow]",
      "[][grow]"
    ));

    if (state.getCurrUser() == null) {
      SwingUtilities.invokeLater(() ->
        router.showView(Pages.LOGIN, state)
      );
      return;
    }

    header = new HeaderPanel(router, state);
    nav = new NavPanel(router, state);

    contentBody = new JPanel(new MigLayout("wrap 3, insets 20, gap 20"));
    contentBody.setBackground(App.slate100);


    if (state.getCurrUser().getRole() == Role.ADMIN) {
      renderAdminDashboard(router, state);
    } else if (state.getCurrUser().getRole() == Role.LECTURER) {
      renderLecturerDashboard(router, state);
    } else if (state.getCurrUser().getRole() == Role.ACADEMIC_LEADER) {
      renderAcademicLeaderDashboard(router, state);
    } else if (state.getCurrUser().getRole() == Role.STUDENT) {
      renderStudentDashboard(router, state);
    } else {
      contentBody.add(new JLabel("Dashboard"));
    }

    this.add(header, "span, growx, wrap");
    this.add(nav, "growy");
    this.add(contentBody, "span, grow");
  }

  private void renderAdminDashboard(Router router, GlobalState state) {
    contentBody.setLayout(new MigLayout("insets 20 20, wrap 1, gapy 25"));
    
    usersLabel = new JLabel();
    usersLabel.setText("Total Users in AFS: ");
    usersLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
    usersLabel.setForeground(Color.WHITE);
    usersValue = new JLabel();
    String totalUsers = String.valueOf(User.fetchUsers("", List.of(Role.values()).stream().map(Role::getValue).collect(Collectors.toSet())).size());
    usersValue.setText(totalUsers);
    usersValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));
    usersValue.setForeground(Color.WHITE);
    manageUsersBtn = new JButton();
    manageUsersBtn.setText("View Users");
    manageUsersBtn.setForeground(Color.WHITE);
    manageUsersBtn.setBackground(App.blue600);
    manageUsersBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    manageUsersBtn.setBorder(BorderFactory.createCompoundBorder(manageUsersBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    manageUsersBtn.setFocusable(false);
    manageUsersBtn.addActionListener(e -> {
      router.showView(Pages.MANAGEUSERS, state);
    });
    usersPanel = new JPanel(new MigLayout("insets 0, wrap 1, gap 5, fill"));
    usersPanel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
    usersPanel.setBackground(App.slate800);
    usersPanel.add(usersLabel);
    usersPanel.add(usersValue, "alignx right");
    usersPanel.add(manageUsersBtn, "alignx right");

    moduleClassesLabel = new JLabel();
    moduleClassesLabel.setText("Total Classes in AFS: ");
    moduleClassesLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
    moduleClassesLabel.setForeground(Color.WHITE);
    moduleClassesValue = new JLabel();
    String totalModuleClasses = String.valueOf(ModuleClass.fetch("").size());
    moduleClassesValue.setText(totalModuleClasses);
    moduleClassesValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));
    moduleClassesValue.setForeground(Color.WHITE);
    moduleClassesBtn = new JButton();
    moduleClassesBtn.setText("View Classes");
    moduleClassesBtn.setForeground(Color.WHITE);
    moduleClassesBtn.setBackground(App.blue600);
    moduleClassesBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    moduleClassesBtn.setBorder(BorderFactory.createCompoundBorder(moduleClassesBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    moduleClassesBtn.setFocusable(false);
    moduleClassesBtn.addActionListener(e -> {
      router.showView(Pages.MANAGECLASSES, state);
    });
    moduleClassesPanel = new JPanel(new MigLayout("insets 0, wrap 1, gap 5, fill"));
    moduleClassesPanel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
    moduleClassesPanel.setBackground(App.gray800);
    moduleClassesPanel.add(moduleClassesLabel);
    moduleClassesPanel.add(moduleClassesValue, "alignx right");
    moduleClassesPanel.add(moduleClassesBtn, "alignx right");

    adminLabel = new JLabel();
    adminLabel.setText("Total Admins: ");
    adminLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
    adminLabel.setForeground(Color.WHITE);
    adminValue = new JLabel();
    String totalAdmins = String.valueOf(User.getListOfUsersByMatchingValues("role", Role.ADMIN.getValue()).size());
    adminValue.setText(totalAdmins);
    adminValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36));
    adminValue.setForeground(Color.WHITE);
    manageAdminBtn = new JButton();
    manageAdminBtn.setText("View Admins");
    manageAdminBtn.setForeground(Color.WHITE);
    manageAdminBtn.setBackground(App.purple600);
    manageAdminBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    manageAdminBtn.setBorder(BorderFactory.createCompoundBorder(manageAdminBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    manageAdminBtn.setFocusable(false);
    manageAdminBtn.addActionListener(e -> {
      state.setUserRoleConditions(Set.copyOf(List.of(Role.ADMIN.getValue())));
      router.showView(Pages.MANAGEUSERS, state);
    });
    adminPanel = new JPanel(new MigLayout("insets 0, wrap 1, gap 5, fill"));
    adminPanel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
    adminPanel.setBackground(App.orange600);
    adminPanel.add(adminLabel);
    adminPanel.add(adminValue, "alignx right");
    adminPanel.add(manageAdminBtn, "alignx right");

    academicLabel = new JLabel();
    academicLabel.setText("Total Academic Leaders: ");
    academicLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
    academicLabel.setForeground(Color.WHITE);
    academicValue = new JLabel();
    String totalAcademic = String.valueOf(User.getListOfUsersByMatchingValues("role", Role.ACADEMIC_LEADER.getValue()).size());
    academicValue.setText(totalAcademic);
    academicValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36));
    academicValue.setForeground(Color.WHITE);
    manageAcademicBtn = new JButton();
    manageAcademicBtn.setText("View Academic Leaders");
    manageAcademicBtn.setForeground(Color.WHITE);
    manageAcademicBtn.setBackground(App.blue600);
    manageAcademicBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    manageAcademicBtn.setBorder(BorderFactory.createCompoundBorder(manageAcademicBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    manageAcademicBtn.setFocusable(false);
    manageAcademicBtn.addActionListener(e -> {
      state.setUserRoleConditions(Set.copyOf(List.of(Role.ACADEMIC_LEADER.getValue())));
      router.showView(Pages.MANAGEUSERS, state);
    });
    academicPanel = new JPanel(new MigLayout("insets 0, wrap 1, gap 5, fill"));
    academicPanel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
    academicPanel.setBackground(App.green600);
    academicPanel.add(academicLabel);
    academicPanel.add(academicValue, "alignx right");
    academicPanel.add(manageAcademicBtn, "alignx right");

    lecturerLabel = new JLabel();
    lecturerLabel.setText("Total Lecturers: ");
    lecturerLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
    lecturerLabel.setForeground(Color.WHITE);
    lecturerValue = new JLabel();
    String totalLecturers = String.valueOf(User.getListOfUsersByMatchingValues("role", Role.LECTURER.getValue()).size());
    lecturerValue.setText(totalLecturers);
    lecturerValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36));
    lecturerValue.setForeground(Color.WHITE);
    manageLecturerBtn = new JButton();
    manageLecturerBtn.setText("View Lecturers");
    manageLecturerBtn.setForeground(Color.WHITE);
    manageLecturerBtn.setBackground(App.green600);
    manageLecturerBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    manageLecturerBtn.setBorder(BorderFactory.createCompoundBorder(manageLecturerBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    manageLecturerBtn.setFocusable(false);
    manageLecturerBtn.addActionListener(e -> {
      state.setUserRoleConditions(Set.copyOf(List.of(Role.LECTURER.getValue())));
      router.showView(Pages.MANAGEUSERS, state);
    });
    lecturerPanel = new JPanel(new MigLayout("insets 0, wrap 1, gap 5, fill"));
    lecturerPanel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
    lecturerPanel.setBackground(App.blue600);
    lecturerPanel.add(lecturerLabel);
    lecturerPanel.add(lecturerValue, "alignx right");
    lecturerPanel.add(manageLecturerBtn, "alignx right");

    studentLabel = new JLabel();
    studentLabel.setText("Total Students: ");
    studentLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
    studentLabel.setForeground(Color.WHITE);
    studentValue = new JLabel();
    String totalStudents = String.valueOf(User.getListOfUsersByMatchingValues("role", Role.STUDENT.getValue()).size());
    studentValue.setText(totalStudents);
    studentValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36));
    studentValue.setForeground(Color.WHITE);
    manageStudentBtn = new JButton();
    manageStudentBtn.setText("View Students");
    manageStudentBtn.setForeground(Color.WHITE);
    manageStudentBtn.setBackground(App.orange600);
    manageStudentBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
    manageStudentBtn.setBorder(BorderFactory.createCompoundBorder(manageStudentBtn.getBorder(), BorderFactory.createEmptyBorder(5, 6, 5, 6)));
    manageStudentBtn.setFocusable(false);
    manageStudentBtn.addActionListener(e -> {
      state.setUserRoleConditions(Set.copyOf(List.of(Role.STUDENT.getValue())));
      router.showView(Pages.MANAGEUSERS, state);
    });
    studentPanel = new JPanel(new MigLayout("insets 0, wrap 1, gap 5, fill"));
    studentPanel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
    studentPanel.setBackground(App.purple600);
    studentPanel.add(studentLabel);
    studentPanel.add(studentValue, "alignx right");
    studentPanel.add(manageStudentBtn, "alignx right");

    // Make first row span 2 columns each
    firstRow = new JPanel(new MigLayout("fill, insets 0, aligny center, gapx 50", 
        "[grow, fill][grow, fill][grow, fill][grow, fill]"));
    firstRow.setBackground(App.slate100);
    firstRow.add(usersPanel, "grow, span 2");
    firstRow.add(moduleClassesPanel, "grow, span 2");

    // Second row stays the same
    secondRow = new JPanel(new MigLayout("fill, insets 0, aligny center, gapx 50", 
        "[grow, fill][grow, fill][grow, fill][grow, fill]"));
    secondRow.setBackground(App.slate100);
    secondRow.add(adminPanel, "grow");
    secondRow.add(academicPanel, "grow");
    secondRow.add(lecturerPanel, "grow");
    secondRow.add(studentPanel, "grow");

    contentBody.add(firstRow, "growx, wrap");
    contentBody.add(secondRow, "growx");
  }

  private void renderLecturerDashboard(Router router, GlobalState state) {

    List<Assessment> myAssessments =
      Assessment.getListOfAssessmentsByMatchingValues(
        "lecturer", state.getCurrUser().getID()
      );

    List<AssessmentMark> myMarks =
      AssessmentMark.fetchAssessmentMarks("", state.getCurrUser());

    List<Feedback> myFeedbacks =
      Feedback.fetchFeedbacks("", state.getCurrUser());

    int totalAssessments = myAssessments.size();
    int totalMarks = myMarks.size();
    int totalFeedback = myFeedbacks.size();

    int pendingFeedback = 0;
    for (AssessmentMark mark : myMarks) {
      Feedback f =
        Feedback.getFeedbackByMatchingValues(
          "assessmentMark", mark.getID()
        );
      if (f == null || f.getContent() == null || f.getContent().trim().isEmpty()) {
        pendingFeedback++;
      }
    }

    java.util.function.BiFunction<String, String, JPanel> makeCard =
      (title, value) -> {
        JPanel card = new JPanel(new MigLayout("insets 10, align center center"));
        card.setBackground(App.slate200);
        card.setBorder(
          BorderFactory.createLineBorder(new Color(226, 232, 240))
        );

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));

        card.add(titleLabel, "wrap");
        card.add(valueLabel, "wrap");
        return card;
      };

    JPanel assessmentsCard =
      makeCard.apply("My Assessments", String.valueOf(totalAssessments));
    JButton gotoAssessments = new JButton("View");
    gotoAssessments.addActionListener(
      e -> router.showView(Pages.ASSESSMENTS, state)
    );
    assessmentsCard.add(gotoAssessments);

    JPanel marksCard =
      makeCard.apply("Marks Records", String.valueOf(totalMarks));
    JButton gotoMarks = new JButton("Enter Marks");
    gotoMarks.addActionListener(
      e -> router.showView(Pages.ENTERMARKS, state)
    );
    marksCard.add(gotoMarks);

    JPanel feedbackCard =
      makeCard.apply("Feedback Records", String.valueOf(totalFeedback));
    JButton gotoFeedback = new JButton("Provide Feedback");
    gotoFeedback.addActionListener(
      e -> router.showView(Pages.PROVIDEFEEDBACK, state)
    );
    feedbackCard.add(gotoFeedback);

    JPanel pendingCard =
      makeCard.apply("Pending Feedback", String.valueOf(pendingFeedback));

    contentBody.add(assessmentsCard, "grow, pushx");
    contentBody.add(marksCard, "grow, pushx");
    contentBody.add(feedbackCard, "grow, pushx");
    contentBody.add(pendingCard, "grow, pushx");
  }

  private void renderAcademicLeaderDashboard(Router router, GlobalState state) {

    AcademicLeader leader = (AcademicLeader) state.getCurrUser();

    JLabel welcomeLabel = new JLabel(
  "Welcome Back, " + leader.getFirstName() + "!"
);
welcomeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
welcomeLabel.setForeground(new Color(30, 64, 175));
welcomeLabel.setHorizontalAlignment(JLabel.CENTER);

contentBody.add(welcomeLabel, "span 3, align center, wrap");


    long numberOfModules =
      Data.fetch("data/modules.txt").stream()
        .map(row -> row.split(", "))
        .filter(p -> p.length > 6)
        .filter(p -> p[6].equals(leader.getID())) // leaderID
        .count();

    long numberOfLecturers =
      Data.fetch("data/users.txt").stream()
        .skip(1) // skip header
        .map(row -> row.split(", "))
        .filter(p -> p.length > 9)
        .filter(p -> p[9].equalsIgnoreCase("lecturer"))
        .count();

    java.util.function.BiFunction<String, String, JPanel> makeCard =
      (title, value) -> {
        JPanel card = new JPanel(new MigLayout("insets 10, align center center"));
        card.setBackground(App.slate200);
        card.setBorder(
          BorderFactory.createLineBorder(new Color(226, 232, 240))
        );

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));

        card.add(titleLabel, "wrap");
        card.add(valueLabel, "wrap");
        return card;
      };

    JPanel moduleCard =
      makeCard.apply("Modules", String.valueOf(numberOfModules));

    JPanel lecturerCard =
      makeCard.apply("Total Lecturers", String.valueOf(numberOfLecturers));

    JPanel reportCard =
      makeCard.apply("Analyse Reports", "");
    JButton analyseBtn = new JButton("Open");
    analyseBtn.addActionListener(
      e -> router.showView(Pages.ANALYSEREPORT, state)
    );

    ImageIcon icon = new ImageIcon("assets/al-wishes.png");
    Image img = icon.getImage().getScaledInstance(500, -1, Image.SCALE_SMOOTH);
    JLabel wishesImage = new JLabel(new ImageIcon(img));

    JPanel imageWrapper = new JPanel(new MigLayout("align center"));
    imageWrapper.setBackground(App.slate100);
    imageWrapper.add(wishesImage);
    
    reportCard.add(analyseBtn);

    contentBody.add(moduleCard, "grow, pushx");
    contentBody.add(lecturerCard, "grow, pushx");
    contentBody.add(reportCard, "grow, pushx");
    contentBody.add(imageWrapper, "span 3, growx, pushy");


  }

  private void renderStudentDashboard(Router router, GlobalState state) {

  Student student = (Student) state.getCurrUser();

  List<StudentModule> enrolled =
      StudentModule.getListOfStudentModulesByMatchingValues(
          "student",
          student.getID()
      );

  int totalClasses = enrolled.size();

  JLabel niceDay = new JLabel("Have a Nice Day!");
  niceDay.setFont(new Font("Segoe Script", Font.BOLD, 36));
  niceDay.setForeground(new Color(30, 64, 175));
  niceDay.setHorizontalAlignment(JLabel.CENTER);

  contentBody.add(niceDay, "span, align center, wrap");

  JPanel cardRow = new JPanel(new MigLayout("insets 20, gap 30", "[grow][grow]"));
  cardRow.setBackground(App.slate100);

  JPanel classCard = new JPanel(new MigLayout("wrap 1, insets 30"));
  classCard.setBackground(App.slate200);
  classCard.setBorder(
    BorderFactory.createLineBorder(new Color(226, 232, 240))
  );

  JLabel classLabel = new JLabel("Class Enroll");
  classLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
  classLabel.setHorizontalAlignment(JLabel.CENTER);

  JLabel classValue = new JLabel(String.valueOf(totalClasses));
  classValue.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
  classValue.setHorizontalAlignment(JLabel.CENTER);

  classCard.add(classLabel, "align center");
  classCard.add(classValue, "align center");

  JPanel resultCard = new JPanel(new MigLayout("wrap 1, insets 30, gapy 15"));
  resultCard.setBackground(App.slate200);
  resultCard.setBorder(
    BorderFactory.createLineBorder(new Color(226, 232, 240))
  );

  JLabel resultLabel = new JLabel("Result is Out!");
  resultLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
  resultLabel.setHorizontalAlignment(JLabel.CENTER);

  JButton resultBtn = new JButton("View Result");
  resultBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
  resultBtn.setBackground(new Color(37, 99, 235));
  resultBtn.setForeground(Color.WHITE);
  resultBtn.setFocusPainted(false);
  resultBtn.setBorder(
    BorderFactory.createCompoundBorder(
      resultBtn.getBorder(),
      BorderFactory.createEmptyBorder(10, 30, 10, 30)
    )
  );

  resultBtn.addActionListener(e ->
      router.showView(Pages.CHECKRESULT, state)
  );

  resultCard.add(resultLabel, "align center");
  resultCard.add(resultBtn, "align center");

  cardRow.add(classCard, "grow");
  cardRow.add(resultCard, "grow");

  contentBody.add(cardRow, "span, growx");
}

  
}
