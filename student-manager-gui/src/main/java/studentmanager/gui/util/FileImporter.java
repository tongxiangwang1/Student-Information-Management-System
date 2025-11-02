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
import java.io.*;
import java.util.*;


/** Imports legacy students from students.txt (pipe-delimited) */
public class FileImporter {
public static List<Student> readLegacyStudents(File file) throws IOException {
List<Student> result = new ArrayList<>();
if (!file.exists()) return result;
try (BufferedReader br = new BufferedReader(new FileReader(file))) {
String line;
while ((line = br.readLine()) != null) {
String[] a = line.split("\\|");
if (a.length >= 9) {
Student s = new Student(a[0], a[1], a[2], Integer.parseInt(a[3]), a[4], a[5], a[6], a[7], a[8]);
result.add(s);
}
}
}
return result;
}
}