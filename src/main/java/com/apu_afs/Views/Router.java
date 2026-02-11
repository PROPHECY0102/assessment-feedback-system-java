package com.apu_afs.Views;

import javax.swing.JPanel;
import com.apu_afs.GlobalState;
import com.apu_afs.Models.Enums.Pages;

import net.miginfocom.swing.MigLayout;

public class Router extends JPanel {
  private Pages currPage;

  public Router(GlobalState state) {
    super(new MigLayout("fill", "[]", "[]"));
    this.showView(Pages.LOGIN, state);
  }
  
  public void showView(Pages page, GlobalState state) {
    this.removeAll();
    
    this.currPage = page;
    this.add(this.createPanel(page, state), "grow");

    revalidate();
    repaint();
  }

  private JPanel createPanel(Pages page, GlobalState state) {
    switch (page) {
      case Pages.LOGIN: return new LoginPage(this, state);
      case Pages.DASHBOARD: return new DashboardPage(this, state);
      case Pages.MANAGEUSERS: return new ManageUsersPage(this, state);
      case Pages.USER: return new UserPage(this, state);
      case Pages.MANAGEGRADES: return new ManageGradesPage(this, state);
      case Pages.MANAGECLASSES: return new ManageClassesPage(this, state);
      case Pages.MODULECLASS: return new ModuleClassPage(this, state);
      case Pages.PROFILE: return new ProfilePage(this, state);
      case Pages.MANAGEMODULES: return new ManageModulesPage(this, state);
      case Pages.MODULE: return new ModulePage(this, state);
      case Pages.ASSESSMENTS: return new AssessmentsPage(this, state);
      case Pages.ENTERMARKS: return new EnterMarksPage(this, state);
      case Pages.PROVIDEFEEDBACK: return new ProvideFeedbackPage(this, state);
      case Pages.CHECKRESULT: return new CheckResultPage(this, state);
      case Pages.REGISTERCLASS: return new StudentRegisterClassesPage(this, state);
      case Pages.FEEDBACKLECTURE: return new StudentFeedbackPage(this, state);
      case Pages.ASSIGNLECTURE: return new AssignLecturersPage(this, state);
      case Pages.ANALYSEREPORT: return new AnalysedReportPage(this, state);
      default: return new DashboardPage(this, state);
    }
  }

  public Pages getCurrPage() {
    return currPage;
  }

  public void setCurrPage(Pages currPage) {
    this.currPage = currPage;
  }
}
