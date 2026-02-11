package com.apu_afs.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentCheckResult {

    private Student student;

    public StudentCheckResult() {
    }

    public StudentCheckResult(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public List<ModuleResult> getResults() {

        Student currentStudent = getStudent();

        List<AssessmentMark> allMarks =
                AssessmentMark.fetchAssessmentMarks("", currentStudent);

        List<AssessmentMark> studentMarks = allMarks.stream()
                .filter(mark ->
                        mark.getStudent() != null &&
                        mark.getStudent().getID()
                                .equals(currentStudent.getID()))
                .collect(Collectors.toList());

        Map<String, List<AssessmentMark>> byModule = studentMarks.stream()
                .filter(mark ->
                        mark.getAssessment() != null &&
                        mark.getAssessment().getModule() != null)
                .collect(Collectors.groupingBy(
                        mark -> mark.getAssessment()
                                .getModule()
                                .getTitle()
                ));

        List<ModuleResult> results = new ArrayList<>();

        for (String moduleName : byModule.keySet()) {

            List<AssessmentMark> moduleMarks = byModule.get(moduleName);

            double avgPercentage = moduleMarks.stream()
                    .mapToDouble(AssessmentMark::getPercentage)
                    .average()
                    .orElse(0.0);

            GradeRange matched = GradeRange.getListOfGradeRanges()
                    .stream()
                    .filter(range ->
                            avgPercentage >= range.getMin() &&
                            avgPercentage <= range.getMax())
                    .findFirst()
                    .orElse(null);

            String grade = matched != null ? matched.getGrade() : "-";
            double gpa = matched != null ? matched.getPoints() : 0.0;

            results.add(new ModuleResult(moduleName, grade, gpa));
        }

        results.sort(
                (a, b) -> a.getGrade()
                        .compareToIgnoreCase(b.getGrade())
        );

        return results;
    }

    public double getCGPA() {

        List<ModuleResult> results = getResults();

        if (results.isEmpty()) {
            return 0.0;
        }

        return results.stream()
                .mapToDouble(ModuleResult::getGpa)
                .average()
                .orElse(0.0);
    }
}
