/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentmanager.gui.model;

public class Course {
    private Integer id;        // DB PK (nullable before insert)
    private String name;
    private int grade;
    private String studentId;  // FK

    public Course(String name, int grade) {
        this.name = name;
        this.grade = grade;
    }
    public Course(Integer id, String studentId, String name, int grade) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.grade = grade;
    }

    public Integer getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public int getGrade() { return grade; }

    public void setName(String name) { this.name = name; }
    public void setGrade(int grade) { this.grade = grade; }

    @Override public String toString() { return name + "-" + grade; }
}
