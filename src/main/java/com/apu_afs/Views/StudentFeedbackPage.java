package com.apu_afs.Views;

import java.awt.Font;
import java.awt.Dimension;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.apu_afs.GlobalState;
import com.apu_afs.Helper;
import com.apu_afs.Models.Lecturer;
import com.apu_afs.Models.Module;
import com.apu_afs.Models.ModuleLecturer;
import com.apu_afs.Models.Student;
import com.apu_afs.Models.StudentFeedback;
import com.apu_afs.Models.StudentModule;
import com.apu_afs.Models.User;
import com.apu_afs.Models.Enums.Pages;
import com.apu_afs.Models.Enums.Role;
import com.apu_afs.Views.components.HeaderPanel;
import com.apu_afs.Views.components.NavPanel;

import net.miginfocom.swing.MigLayout;

public class StudentFeedbackPage extends JPanel {

    HeaderPanel header;
    NavPanel nav;
    JPanel contentBody;

    JComboBox<Module> moduleDropdown;
    JLabel lecturerValueLabel;
    JLabel classCodeValueLabel;
    JTextArea commentArea;
    JButton submitBtn;

    public StudentFeedbackPage(Router router, GlobalState state) {

        super(new MigLayout(
            "fill, insets 0, gap 0",
            "[][][grow]",
            "[][grow]"
        ));

        if (state.getCurrUser() == null) {
            SwingUtilities.invokeLater(() -> router.showView(Pages.LOGIN, state));
            return;
        }

        if (state.getCurrUser().getRole() != Role.STUDENT) {
            SwingUtilities.invokeLater(() -> router.showView(Pages.DASHBOARD, state));
            return;
        }

        header = new HeaderPanel(router, state);
        nav = new NavPanel(router, state);

        contentBody = new JPanel(
            new MigLayout("insets 30, fillx, wrap 1", "[grow]", "[]")
        );
        contentBody.setBackground(App.slate100);

        Student student = (Student) state.getCurrUser();

        List<Module> studentModules = StudentModule
            .getListByStudent(student.getID())
            .stream()
            .map(sm -> Module.getModuleByMatchingValues("id", sm.getModuleID()))
            .filter(m -> m != null)
            .collect(Collectors.toList());

        JPanel formPanel = new JPanel(
            new MigLayout("wrap 1, gapy 12", "[grow]", "[]")
        );
        formPanel.setBackground(App.slate100);

        JLabel moduleLabel = new JLabel("Select Module:");
        moduleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

        moduleDropdown = new JComboBox<>(studentModules.toArray(new Module[0]));
        moduleDropdown.setPreferredSize(new Dimension(300, 38));

        JLabel lecturerLabel = new JLabel("Lecturer:");
        lecturerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

        lecturerValueLabel = new JLabel("-");
        lecturerValueLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));

        JPanel lecturerRow = new JPanel(new MigLayout("insets 0, gapx 10"));
        lecturerRow.setBackground(App.slate100);
        lecturerRow.add(lecturerLabel);
        lecturerRow.add(lecturerValueLabel);

        JLabel classCodeLabel = new JLabel("Class Code:");
        classCodeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

        classCodeValueLabel = new JLabel("-");
        classCodeValueLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));

        JPanel classCodeRow = new JPanel(new MigLayout("insets 0, gapx 10"));
        classCodeRow.setBackground(App.slate100);
        classCodeRow.add(classCodeLabel);
        classCodeRow.add(classCodeValueLabel);


        moduleDropdown.addActionListener(e -> {
            Module selected = (Module) moduleDropdown.getSelectedItem();

            if (selected == null) {
                lecturerValueLabel.setText("-");
                classCodeValueLabel.setText("-");
                return;
            }

            try {


                List<Lecturer> lecturers = ModuleLecturer.fetchAll().stream()
                    .filter(ml -> ml.getModuleID().equals(selected.getID()))
                    .map(ml -> User.getUserByMatchingValues("id", ml.getLecturerID()))
                    .filter(u -> u instanceof Lecturer)
                    .map(u -> (Lecturer) u)
                    .collect(Collectors.toList());

                if (!lecturers.isEmpty()) {
                    Lecturer lecturer = lecturers.get(0);
                    lecturerValueLabel.setText(
                        lecturer.getFirstName() + " " + lecturer.getLastName()
                    );
                } else {
                    lecturerValueLabel.setText("-");
                }


                StudentModule sm =
                    StudentModule.getStudentModuleByCompositeKey(
                        student.getID(),
                        selected.getID()
                    );

                if (sm != null) {
                    classCodeValueLabel.setText(sm.getClassCode());
                } else {
                    classCodeValueLabel.setText("-");
                }

            } catch (Exception ex) {
                lecturerValueLabel.setText("-");
                classCodeValueLabel.setText("-");
            }
        });

        if (!studentModules.isEmpty()) {
            moduleDropdown.setSelectedIndex(0);
        }


        JLabel commentLabel = new JLabel("Your Feedback:");
        commentLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

        commentArea = new JTextArea(6, 45);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        commentArea.setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        JScrollPane commentScroll = new JScrollPane(commentArea);


        submitBtn = new JButton("Submit Feedback");
        submitBtn.setIcon(
            Helper.iconResizer(
                new ImageIcon("assets/submit-icon.png"),
                18,
                18
            )
        );
        submitBtn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        submitBtn.setBackground(App.blue600);
        submitBtn.setForeground(java.awt.Color.WHITE);
        submitBtn.setFocusable(false);

        submitBtn.addActionListener(e -> {

            Module selectedModule = (Module) moduleDropdown.getSelectedItem();
            String comment = commentArea.getText().trim();

            if (selectedModule == null || comment.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please select a module and enter your feedback.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            new StudentFeedback(student, selectedModule, comment).submit();

            JOptionPane.showMessageDialog(
                this,
                "Feedback submitted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

            commentArea.setText("");
        });


        formPanel.add(moduleLabel);
        formPanel.add(moduleDropdown, "growx");
        formPanel.add(lecturerRow);
        formPanel.add(classCodeRow);
        formPanel.add(commentLabel);
        formPanel.add(commentScroll, "growx, h 150!");
        formPanel.add(submitBtn, "align right, gapy 10");

        contentBody.add(formPanel, "growx");

        this.add(header, "span, growx, wrap");
        this.add(nav, "growy");
        this.add(contentBody, "span, grow");
    }
}
