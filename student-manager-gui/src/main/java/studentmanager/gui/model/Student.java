/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentmanager.gui.model;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private String email;
    private String address;
    private String phone;
    private String major;
    private String degree;

    private final List<Course> courses = new ArrayList<>();
    private final List<Internship> internships = new ArrayList<>();

    public Student(String id, String name, String gender, int age,
                   String email, String address, String phone, String major, String degree) {
        super(id, name, gender, age);
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.major = major;
        this.degree = degree;
    }

    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getMajor() { return major; }
    public String getDegree() { return degree; }

    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setMajor(String major) { this.major = major; }
    public void setDegree(String degree) { this.degree = degree; }

    public List<Course> getCourses() { return courses; }
    public List<Internship> getInternships() { return internships; }
    public void addCourse(Course c) { courses.add(c); }
    public void addInternship(Internship i) { internships.add(i); }

    @Override public String toString() { return getName() + " (" + getId() + ")"; }
}
