/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentmanager.gui.ui;

/**
 *
 * @author leonw
 */

import studentmanager.gui.exception.InvalidInputException;
import studentmanager.gui.model.Course;
import studentmanager.gui.model.Internship;
import studentmanager.gui.model.Student;
import studentmanager.gui.util.ValidationUtil;

import javax.swing.*;
import java.awt.*;

public class StudentFormDialog extends JDialog {
    private final JTextField tfId = new JTextField(12);
    private final JTextField tfName = new JTextField(16);
    private final JTextField tfGender = new JTextField(8);
    private final JSpinner spAge = new JSpinner(new SpinnerNumberModel(18, 1, 150, 1));
    private final JTextField tfEmail = new JTextField(20);
    private final JTextField tfAddress = new JTextField(20);
    private final JTextField tfPhone = new JTextField(16);
    private final JTextField tfMajor = new JTextField(16);
    private final JTextField tfDegree = new JTextField(16);

    private final CoursesPanel coursesPanel = new CoursesPanel();
    private final InternshipsPanel internshipsPanel = new InternshipsPanel();

    private Student result;

    public StudentFormDialog(Frame owner, Student existing) {
        super(owner, true);
        setTitle(existing == null ? "Add Student" : "Edit Student");
        setSize(700, 520);
        setLocationRelativeTo(owner);

        JPanel form = new JPanel(new GridLayout(0,2,8,8));
        form.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        form.add(new JLabel("ID:")); form.add(tfId);
        form.add(new JLabel("Name:")); form.add(tfName);
        form.add(new JLabel("Gender:")); form.add(tfGender);
        form.add(new JLabel("Age:")); form.add(spAge);
        form.add(new JLabel("Email:")); form.add(tfEmail);
        form.add(new JLabel("Address:")); form.add(tfAddress);
        form.add(new JLabel("Phone:")); form.add(tfPhone);
        form.add(new JLabel("Major:")); form.add(tfMajor);
        form.add(new JLabel("Degree:")); form.add(tfDegree);

        if (existing != null) {
            tfId.setText(existing.getId()); tfId.setEnabled(false);
            tfName.setText(existing.getName()); tfGender.setText(existing.getGender());
            spAge.setValue(existing.getAge()); tfEmail.setText(existing.getEmail());
            tfAddress.setText(existing.getAddress()); tfPhone.setText(existing.getPhone());
            tfMajor.setText(existing.getMajor()); tfDegree.setText(existing.getDegree());
            existing.getCourses().forEach(coursesPanel::addCourse);
            existing.getInternships().forEach(internshipsPanel::addInternship);
        }

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Courses", coursesPanel);
        tabs.addTab("Internships", internshipsPanel);

        JPanel buttons = new JPanel();
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        buttons.add(ok); buttons.add(cancel);

        ok.addActionListener(e -> onOk());
        cancel.addActionListener(e -> dispose());

        add(form, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private void onOk() {
        try {
            String id = tfId.getText().trim();
            String name = tfName.getText().trim();
            String gender = tfGender.getText().trim();
            int age = (Integer) spAge.getValue();
            String email = tfEmail.getText().trim();
            String address = tfAddress.getText().trim();
            String phone = tfPhone.getText().trim();
            String major = tfMajor.getText().trim();
            String degree = tfDegree.getText().trim();

            // validate 
            ValidationUtil.requireNonBlank(id, "ID");
            ValidationUtil.requireAlnum(id, "ID");
            ValidationUtil.requireNonBlank(name, "Name");
            ValidationUtil.requirePositive(age, "Age");
            ValidationUtil.requireEmail(email);
            ValidationUtil.requireDigits(phone, "Phone");

            Student s = new Student(id, name, gender, age, email, address, phone, major, degree);
            for (Course c : coursesPanel.getCourses()) s.addCourse(c);
            for (Internship i : internshipsPanel.getInternships()) s.addInternship(i);

            this.result = s;
            dispose();
        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation", JOptionPane.WARNING_MESSAGE);
        }
    }

    public Student getResult() { return result; }
}
