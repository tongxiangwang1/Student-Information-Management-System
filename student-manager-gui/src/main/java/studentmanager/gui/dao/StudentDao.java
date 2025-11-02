/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentmanager.gui.dao;

import studentmanager.gui.db.DatabaseManager;
import studentmanager.gui.model.Student;

import java.sql.*;
import java.util.*;

public class StudentDao {

    // project1 Existing code
    public void upsert(Student s) throws SQLException {
        try (Connection c = DatabaseManager.getInstance().getConnection()) {
            upsert(c, s);
        }
    }
//
    // upsert
    public void upsert(Connection c, Student s) throws SQLException {
        int updated;
        try (PreparedStatement ps = c.prepareStatement(
                "UPDATE students SET name=?, gender=?, age=?, email=?, address=?, phone=?, major=?, degree=? WHERE id=?")) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getGender());
            ps.setInt(3, s.getAge());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getAddress());
            ps.setString(6, s.getPhone());
            ps.setString(7, s.getMajor());
            ps.setString(8, s.getDegree());
            ps.setString(9, s.getId());
            updated = ps.executeUpdate();
        }
        if (updated == 0) {
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO students(id,name,gender,age,email,address,phone,major,degree) VALUES(?,?,?,?,?,?,?,?,?)")) {
                ps.setString(1, s.getId());
                ps.setString(2, s.getName());
                ps.setString(3, s.getGender());
                ps.setInt(4, s.getAge());
                ps.setString(5, s.getEmail());
                ps.setString(6, s.getAddress());
                ps.setString(7, s.getPhone());
                ps.setString(8, s.getMajor());
                ps.setString(9, s.getDegree());
                ps.executeUpdate();
            }
        }
    }

    public void deleteById(String id) throws SQLException {
        try (Connection c = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM students WHERE id=?")) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    public Student findById(String id) throws SQLException {
        try (Connection c = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM students WHERE id=?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public List<Student> findAll() throws SQLException {
        List<Student> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getInstance().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM students ORDER BY id")) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    //You can search by name
    public List<Student> searchByName(String q) throws SQLException {
        List<Student> list = new ArrayList<>();
        try (Connection c = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM students WHERE LOWER(name) LIKE ? ORDER BY name")) {
            ps.setString(1, "%" + q.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    //Additional information can be searched by student ID.
   public List<Student> searchByIdOrName(String q) throws SQLException {
    String trimmed = q.trim();
    if (trimmed.isEmpty()) return findAll();

    String[] tokens = trimmed.toLowerCase().split("\\s+");

    StringBuilder sql = new StringBuilder(
        "SELECT * FROM students WHERE id LIKE ? OR (1=1"
    );
    for (int i = 0; i < tokens.length; i++) {
        sql.append(" AND LOWER(name) LIKE ?");
    }
    sql.append(") ORDER BY id");

    List<Student> list = new ArrayList<>();
    try (Connection c = DatabaseManager.getInstance().getConnection();
         PreparedStatement ps = c.prepareStatement(sql.toString())) {

        int idx = 1;
        ps.setString(idx++, "%" + trimmed + "%");
        for (String t : tokens) {
            ps.setString(idx++, "%" + t + "%");  
        }

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
    }
    return list;
}


    private Student map(ResultSet rs) throws SQLException {
        return new Student(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("gender"),
                rs.getInt("age"),
                rs.getString("email"),
                rs.getString("address"),
                rs.getString("phone"),
                rs.getString("major"),
                rs.getString("degree")
        );
    }
}
