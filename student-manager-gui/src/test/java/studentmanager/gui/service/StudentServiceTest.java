package studentmanager.gui.service;

import studentmanager.gui.db.DatabaseManager;
import studentmanager.gui.model.Student;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentServiceTest {

    private static final String TEST_URL = "jdbc:derby:memory:testdb;create=true";
    private static StudentService service;

    @BeforeAll
    static void initDb() {
        DatabaseManager.getInstance().setUrlForTests(TEST_URL);
        DatabaseManager.getInstance().init();
        service = new StudentService();
    }

    @Test
    void addAndLoadStudent() throws Exception {
        Student s = new Student("S001","Alice","F",20,"a@b.com","addr","123456","CS","BSc");
        service.saveStudent(s);
        Student got = service.loadFull("S001");
        assertNotNull(got);
        assertEquals("Alice", got.getName());
    }
}

