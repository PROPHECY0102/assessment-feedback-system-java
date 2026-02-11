package com.apu_afs.Models.reports;

import com.apu_afs.Models.Enums.ReportType;

public class AnalyseReport {

  public static Report getReport(ReportType type) {
    return switch (type) {
      case GRADE_DISTRIBUTION -> new GradeDistributionReport();
      case MODULE_PERFORMANCE -> new ModulePerformanceReport();
      case LECTURER_WORKLOAD -> new LecturerWorkloadReport();
      case CLASS_ENROLLMENT -> new ClassEnrollmentReport();
      case FEEDBACK_SUMMARY -> new FeedbackSummaryReport();
    };
  }

  
}


