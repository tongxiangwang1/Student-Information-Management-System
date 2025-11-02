/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentmanager.gui.model;

public class Internship {
    private Integer id;        // DB PK (nullable before insert)
    private String place;
    private String time;
    private String studentId;  // FK

    public Internship(String place, String time) {
        this.place = place;
        this.time = time;
    }
    public Internship(Integer id, String studentId, String place, String time) {
        this.id = id;
        this.studentId = studentId;
        this.place = place;
        this.time = time;
    }

    public Integer getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getPlace() { return place; }
    public String getTime() { return time; }

    public void setPlace(String place) { this.place = place; }
    public void setTime(String time) { this.time = time; }

    @Override public String toString() { return place + "-" + time; }
}
