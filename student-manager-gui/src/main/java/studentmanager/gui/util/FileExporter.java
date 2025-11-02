/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentmanager.gui.util;

/**
 *
 * @author leonw
 */

import studentmanager.gui.model.Student;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileExporter {
    public static void exportStudentsCsv(File target, List<Student> students) throws IOException {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"ID","Name","Gender","Age","Email","Address","Phone","Major","Degree"});
        for (Student s : students) {
            rows.add(new String[]{
                    s.getId(),
                    s.getName(),
                    s.getGender(),
                    String.valueOf(s.getAge()),
                    s.getEmail(),
                    s.getAddress(),
                    s.getPhone(),
                    s.getMajor(),
                    s.getDegree()
            });
        }
        CsvUtil.writeCsv(target, rows);
    }
}
