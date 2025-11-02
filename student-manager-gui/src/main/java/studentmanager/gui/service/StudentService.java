/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentmanager.gui.service;

import studentmanager.gui.dao.CourseDao;
import studentmanager.gui.dao.InternshipDao;
import studentmanager.gui.dao.StudentDao;
import studentmanager.gui.db.DatabaseManager;
import studentmanager.gui.exception.InvalidInputException;
import studentmanager.gui.model.Student;
import studentmanager.gui.util.ValidationUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class StudentService {
    private final StudentDao studentDao = new StudentDao();
    private final CourseDao courseDao = new CourseDao();
    private final InternshipDao internshipDao = new InternshipDao();

    public void saveStudent(Student s) throws InvalidInputException, SQLException {
        validate(s);
        Connection c = DatabaseManager.getInstance().getConnection();
        boolean oldAuto = c.getAutoCommit();
        try {
            c.setAutoCommit(false);
            studentDao.upsert(c, s);
            courseDao.replaceAllForStudent(c, s.getId(), s.getCourses());
            internshipDao.replaceAllForStudent(c, s.getId(), s.getInternships());
            c.commit();
        } catch (SQLException e) {
            c.rollback();
            throw e;
        } finally {
            try { c.setAutoCommit(oldAuto); } catch (SQLException ignored) {}
            try { c.close(); } catch (SQLException ignored) {}
        }
    }

    public void deleteStudent(String id) throws SQLException {
        studentDao.deleteById(id);
    }

    public Student loadFull(String id) throws SQLException {
        Student s = studentDao.findById(id);
        if (s != null) {
            s.getCourses().addAll(courseDao.findByStudent(id));
            s.getInternships().addAll(internshipDao.findByStudent(id));
        }
        return s;
    }

    public List<Student> listAll() throws SQLException { return studentDao.findAll(); }

    //Automatic search by student ID
    public List<Student> searchByIdOrName(String q) throws SQLException { return studentDao.searchByIdOrName(q); }

    public List<Student> searchByName(String q) throws SQLException { return studentDao.searchByName(q); }

    private void validate(Student s) throws InvalidInputException {
        ValidationUtil.requireNonBlank(s.getId(), "ID");
        ValidationUtil.requireAlnum(s.getId(), "ID");
        ValidationUtil.requireNonBlank(s.getName(), "Name");
        ValidationUtil.requirePositive(s.getAge(), "Age");
        ValidationUtil.requireEmail(s.getEmail());
        ValidationUtil.requireDigits(s.getPhone(), "Phone");
    }
}
