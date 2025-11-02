/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentmanager.gui.ui;

import studentmanager.gui.model.Course;
import studentmanager.gui.model.Internship;
import studentmanager.gui.model.Student;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class StudentDetailsPanel extends JPanel {
    private final Runnable onBack;

    public StudentDetailsPanel(Student s, Runnable onBack) {
        this.onBack = onBack;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        // Back button and title
        JPanel top = new JPanel(new BorderLayout(8,0));
        JButton back = new JButton("← Back to List");
        JLabel title = new JLabel("Student Details: " + s.getName() + " (" + s.getId() + ")");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        top.add(back, BorderLayout.WEST);
        top.add(title, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        back.addActionListener(e -> { if (this.onBack != null) this.onBack.run(); });

        // Information and courses/internships
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        // Basic Information
        JPanel info = new JPanel(new GridLayout(0, 2, 8, 6));
        info.setBorder(BorderFactory.createTitledBorder("Basic Information (All Fields)"));
        info.add(new JLabel("ID:"));      info.add(new JLabel(s.getId()));
        info.add(new JLabel("Name:"));    info.add(new JLabel(s.getName()));
        info.add(new JLabel("Gender:"));  info.add(new JLabel(s.getGender()));
        info.add(new JLabel("Age:"));     info.add(new JLabel(String.valueOf(s.getAge())));
        info.add(new JLabel("Email:"));   info.add(new JLabel(s.getEmail()));
        info.add(new JLabel("Address:")); info.add(new JLabel(s.getAddress()));
        info.add(new JLabel("Phone:"));   info.add(new JLabel(s.getPhone()));
        info.add(new JLabel("Major:"));   info.add(new JLabel(s.getMajor()));
        info.add(new JLabel("Degree:"));  info.add(new JLabel(s.getDegree()));
        center.add(info);
        center.add(Box.createVerticalStrut(10));

        // course
        JTable courseTable = new JTable(new CourseModel(s.getCourses()));
        JScrollPane courseSc = new JScrollPane(courseTable);
        courseSc.setBorder(BorderFactory.createTitledBorder("Courses (" + s.getCourses().size() + ")"));
        courseSc.setPreferredSize(new Dimension(800, Math.min(200, 24 + s.getCourses().size() * 20)));
        center.add(courseSc);
        center.add(Box.createVerticalStrut(10));

        // intership
        JTable internTable = new JTable(new InternshipModel(s.getInternships()));
        JScrollPane internSc = new JScrollPane(internTable);
        internSc.setBorder(BorderFactory.createTitledBorder("Internships (" + s.getInternships().size() + ")"));
        internSc.setPreferredSize(new Dimension(800, Math.min(200, 24 + s.getInternships().size() * 20)));
        center.add(internSc);

        add(center, BorderLayout.CENTER);
    }

    // Callback method: for use by the mainframe
    public Runnable getOnBack() { return onBack; }

    // Read table model
    private static class CourseModel extends AbstractTableModel {
        private final String[] cols = {"ID (DB)", "Student ID (FK)", "Name", "Grade"};
        private final List<Course> data;
        CourseModel(List<Course> data){ this.data = data; }
        @Override public int getRowCount(){ return data.size(); }
        @Override public int getColumnCount(){ return cols.length; }
        @Override public String getColumnName(int c){ return cols[c]; }
        @Override public Object getValueAt(int r, int c){
            Course x = data.get(r);
            return switch (c) {
                case 0 -> x.getId();
                case 1 -> x.getStudentId();
                case 2 -> x.getName();
                case 3 -> x.getGrade();
                default -> "";
            };
        }
        @Override public boolean isCellEditable(int r,int c){ return false; }
    }

    private static class InternshipModel extends AbstractTableModel {
        private final String[] cols = {"ID (DB)", "Student ID (FK)", "Place", "Time"};
        private final List<Internship> data;
        InternshipModel(List<Internship> data){ this.data = data; }
        @Override public int getRowCount(){ return data.size(); }
        @Override public int getColumnCount(){ return cols.length; }
        @Override public String getColumnName(int c){ return cols[c]; }
        @Override public Object getValueAt(int r, int c){
            Internship x = data.get(r);
            return switch (c) {
                case 0 -> x.getId();
                case 1 -> x.getStudentId();
                case 2 -> x.getPlace();
                case 3 -> x.getTime();
                default -> "";
            };
        }
        @Override public boolean isCellEditable(int r,int c){ return false; }
    }
}
