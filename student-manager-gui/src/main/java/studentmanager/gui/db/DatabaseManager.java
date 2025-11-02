package studentmanager.gui.db;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DatabaseManager {
    private static final DatabaseManager INSTANCE = new DatabaseManager();

    // ~/.student-manager-db/studentdb
    private String url;

    private DatabaseManager() {
        // absolute path
        Path dbDir = Paths.get(System.getProperty("user.home"), ".student-manager-db", "studentdb").toAbsolutePath();
        String dbPath = dbDir.toString().replace('\\','/'); // 避免反斜杠转义问题
        this.url = "jdbc:derby:" + dbPath + ";create=true;upgrade=true";
        // Ensure the parent directory exists
        try { Files.createDirectories(dbDir.getParent()); } catch (Exception ignored) {}
    }

    public static DatabaseManager getInstance() { return INSTANCE; }

    //Switch to memory library during testing
    public void setUrlForTests(String u) { this.url = u; }

    //Initialize data and automatically create tables
    public void init() {
        try (Connection c = getConnection()) {
            createSchemaIfNeeded(c);
            Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
            System.out.println("[DB] Ready: " + url);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to init Derby: " + e.getMessage()
                    + "\n如仍失败，请查看并删除旧库残留锁文件："
                    + userDbPath().toAbsolutePath() + File.separator + "db.lck",
                    "Derby Error", JOptionPane.ERROR_MESSAGE);
            throw new IllegalStateException("Derby init failed", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private void createSchemaIfNeeded(Connection c) throws SQLException {
        try (Statement st = c.createStatement()) {
            st.executeUpdate("""
                CREATE TABLE students(
                  id VARCHAR(50) PRIMARY KEY,
                  name VARCHAR(100) NOT NULL,
                  gender VARCHAR(20),
                  age INT,
                  email VARCHAR(120),
                  address VARCHAR(200),
                  phone VARCHAR(40),
                  major VARCHAR(100),
                  degree VARCHAR(100))
            """);
            System.out.println("[DB] created table students");
        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) throw e; // X0Y32: Existing
        }

        try (Statement st = c.createStatement()) {
            st.executeUpdate("""
                CREATE TABLE courses(
                  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                  student_id VARCHAR(50) REFERENCES students(id) ON DELETE CASCADE,
                  name VARCHAR(100) NOT NULL,
                  grade INT NOT NULL)
            """);
            System.out.println("[DB] created table courses");
        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) throw e;
        }

        try (Statement st = c.createStatement()) {
            st.executeUpdate("""
                CREATE TABLE internships(
                  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                  student_id VARCHAR(50) REFERENCES students(id) ON DELETE CASCADE,
                  place VARCHAR(120) NOT NULL,
                  time VARCHAR(120) NOT NULL)
            """);
            System.out.println("[DB] created table internships");
        } catch (SQLException e) {
            if (!"X0Y32".equals(e.getSQLState())) throw e;
        }

        // index
        try (Statement st = c.createStatement()) {
            st.executeUpdate("CREATE INDEX idx_courses_sid ON courses(student_id)");
        } catch (SQLException e) { if (!"X0Y32".equals(e.getSQLState())) throw e; }

        try (Statement st = c.createStatement()) {
            st.executeUpdate("CREATE INDEX idx_internships_sid ON internships(student_id)");
        } catch (SQLException e) { if (!"X0Y32".equals(e.getSQLState())) throw e; }

        try (Statement st = c.createStatement()) {
            st.executeUpdate("CREATE INDEX idx_students_name ON students(name)");
        } catch (SQLException e) { if (!"X0Y32".equals(e.getSQLState())) throw e; }
    }

    private void shutdown() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException ignored) { /* Derby 正常抛异常表示已关 */ }
    }

    private Path userDbPath() {
        return Paths.get(System.getProperty("user.home"), ".student-manager-db", "studentdb");
    }
}
