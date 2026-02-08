package com.apu_afs.TableModels;

import javax.swing.table.AbstractTableModel;

import com.apu_afs.Models.Feedback;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackTableModel extends AbstractTableModel {
    private List<Feedback> feedbacks;
    private final String[] columnNames = {
      "ID", "Student", "Assessment", "Feedback", "Grade", "Last Updated"
    };

    public FeedbackTableModel(List<Feedback> feedbacks) {
      this.feedbacks = feedbacks != null ?
      feedbacks.stream()
        .sorted((currFeedback, nextFeedback) -> Integer.compare(
            Integer.parseInt(currFeedback.getID()), Integer.parseInt(nextFeedback.getID())
        ))
        .collect(Collectors.toList()) 
      : new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return feedbacks.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      Feedback feedback = feedbacks.get(rowIndex);
      switch (columnIndex) {
        case 0: return feedback.getID();
        case 1: return feedback.getAssessmentMark() != null && feedback.getAssessmentMark().getStudent() != null ? 
                        (feedback.getAssessmentMark().getStudent().getFirstName() + " " + feedback.getAssessmentMark().getStudent().getLastName()) : "Not Available (N/A)";
        case 2: return feedback.getAssessmentMark() != null && feedback.getAssessmentMark().getAssessment() != null ? 
                        feedback.getAssessmentMark().getAssessment().getName() : "Not Available (N/A)";
        case 3: return feedback.getContent() != null && feedback.getContent().length() > 50 ? 
                        feedback.getContent().substring(0, 50) + "..." : feedback.getContent();
        case 4: return feedback.getAssessmentMark() != null ? com.apu_afs.Models.GradeRange.getGradeForPercentage(feedback.getAssessmentMark().getPercentage()) : "-";
        case 5: return feedback.getUpdatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        default: return null;
      }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public Feedback getFeedbackAt(int rowIndex) {
        return feedbacks.get(rowIndex);
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
      this.feedbacks = feedbacks != null ?
      feedbacks.stream()
        .sorted((currFeedback, nextFeedback) -> Integer.compare(
            Integer.parseInt(currFeedback.getID()), Integer.parseInt(nextFeedback.getID())
        ))
        .collect(Collectors.toList()) 
      : new ArrayList<>();
      fireTableDataChanged();
    }

    public void clear() {
      feedbacks.clear();
      fireTableDataChanged();
    }
}
