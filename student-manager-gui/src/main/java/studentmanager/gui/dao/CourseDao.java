/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentmanager.gui.dao;
//
import studentmanager.gui.db.DatabaseManager;
import studentmanager.gui.model.Course;

import java.sql.*;
import java.util.*;

public class CourseDao {

    // The original code of project1 is retained.）
    public void replaceAllForStudent(String studentId, List<Course> courses) throws SQLException {
        try (Connection c = DatabaseManager.getInstance().getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement del = c.prepareStatement("DELETE FROM courses WHERE student_id=?")) {
                del.setString(1, studentId);
                del.executeUpdate();
            }
            try (PreparedStatement ins = c.prepareStatement(
                    "INSERT INTO courses(student_id,name,grade) VALUES(?,?,?)")) {
                for (Course co : courses) {
                    ins.setString(1, studentId);
                    ins.setString(2, co.getName());
                    ins.setInt(3, co.getGrade());
                    ins.addBatch();
                }
                ins.executeBatch();
            }
            c.commit();
        }
    }

    // Used to receive external connections
    public void replaceAllForStudent(Connection c, String studentId, List<Course> courses) throws SQLException {
        try (PreparedStatement del = c.prepareStatement("DELETE FROM courses WHERE student_id=?")) {
            del.setString(1, studentId);
            del.executeUpdate();
        }
        try (PreparedStatement ins = c.prepareStatement(
                "INSERT INTO courses(student_id,name,grade) VALUES(?,?,?)")) {
            for (Course co : courses) {
                ins.setString(1, studentId);
                ins.setString(2, co.getName());
                ins.setInt(3, co.getGrade());
                ins.addBatch();
            }
            ins.executeBatch();
        }
    }

    public List<Course> findByStudent(String studentId) throws SQLException {
        List<Course> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM courses WHERE student_id=? ORDER BY id")) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Course(
                            rs.getInt("id"),
                            rs.getString("student_id"),
                            rs.getString("name"),
                            rs.getInt("grade")
                    ));
                }
            }
        }
        return list;
    }
}
