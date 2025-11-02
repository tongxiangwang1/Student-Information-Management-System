/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentmanager.gui.dao;

import studentmanager.gui.db.DatabaseManager;
import studentmanager.gui.model.Internship;

import java.sql.*;
import java.util.*;

public class InternshipDao {

    // project1 Existing code
    public void replaceAllForStudent(String studentId, List<Internship> internships) throws SQLException {
        try (Connection c = DatabaseManager.getInstance().getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement del = c.prepareStatement("DELETE FROM internships WHERE student_id=?")) {
                del.setString(1, studentId);
                del.executeUpdate();
            }
            try (PreparedStatement ins = c.prepareStatement(
                    "INSERT INTO internships(student_id,place,time) VALUES(?,?,?)")) {
                for (Internship it : internships) {
                    ins.setString(1, studentId);
                    ins.setString(2, it.getPlace());
                    ins.setString(3, it.getTime());
                    ins.addBatch();
                }
                ins.executeBatch();
            }
            c.commit();
        }
    }

    // used to receive external connections
    public void replaceAllForStudent(Connection c, String studentId, List<Internship> internships) throws SQLException {
        try (PreparedStatement del = c.prepareStatement("DELETE FROM internships WHERE student_id=?")) {
            del.setString(1, studentId);
            del.executeUpdate();
        }
        try (PreparedStatement ins = c.prepareStatement(
                "INSERT INTO internships(student_id,place,time) VALUES(?,?,?)")) {
            for (Internship it : internships) {
                ins.setString(1, studentId);
                ins.setString(2, it.getPlace());
                ins.setString(3, it.getTime());
                ins.addBatch();
            }
            ins.executeBatch();
        }
    }

    public List<Internship> findByStudent(String studentId) throws SQLException {
        List<Internship> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM internships WHERE student_id=? ORDER BY id")) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Internship(
                            rs.getInt("id"),
                            rs.getString("student_id"),
                            rs.getString("place"),
                            rs.getString("time")
                    ));
                }
            }
        }
        return list;
    }
}
