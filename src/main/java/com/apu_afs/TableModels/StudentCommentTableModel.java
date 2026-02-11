package com.apu_afs.TableModels;

import javax.swing.table.AbstractTableModel;

import com.apu_afs.Models.StudentComment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentCommentTableModel extends AbstractTableModel {
    private List<StudentComment> comments;
    private final String[] columnNames = {
        "ID", "Student", "Module", "Comment", "Date"
    };

    public StudentCommentTableModel(List<StudentComment> comments) {
      this.comments = comments != null ?
      comments.stream()
        .sorted((currComment, nextComment) -> Integer.compare(
            Integer.parseInt(currComment.getID()), Integer.parseInt(nextComment.getID())
        ))
        .collect(Collectors.toList()) 
      : new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return comments.size();
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
      StudentComment comment = comments.get(rowIndex);
      switch (columnIndex) {
        case 0: return comment.getID();
        case 1: return comment.getStudent() != null ? 
                        (comment.getStudent().getFirstName() + " " + comment.getStudent().getLastName()) : "Not Available (N/A)";
        case 2: return comment.getModule() != null ? comment.getModule().getTitle() : "Not Available (N/A)";
        case 3: return comment.getComment() != null && comment.getComment().length() > 50 ? 
                        comment.getComment().substring(0, 50) + "..." : comment.getComment();
        case 4: return comment.getCommentedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        default: return null;
      }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public StudentComment getCommentAt(int rowIndex) {
        return comments.get(rowIndex);
    }

    public void setComments(List<StudentComment> comments) {
      this.comments = comments != null ?
      comments.stream()
        .sorted((currComment, nextComment) -> Integer.compare(
            Integer.parseInt(currComment.getID()), Integer.parseInt(nextComment.getID())
        ))
        .collect(Collectors.toList()) 
      : new ArrayList<>();
      fireTableDataChanged();
    }

    public void clear() {
      comments.clear();
      fireTableDataChanged();
    }
}