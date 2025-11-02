/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentmanager.gui.util;

/**
 *
 * @author leonw
 */


import java.io.*;
import java.util.List;


public class CsvUtil {
public static void writeCsv(File file, List<String[]> rows) throws IOException {
try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
for (String[] r : rows) {
pw.println(String.join(",", escape(r)));
}
}
}
private static String[] escape(String[] r) {
String[] out = new String[r.length];
for (int i=0;i<r.length;i++) {
String s = r[i] == null ? "" : r[i];
if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
s = '"' + s.replace("\"", "\"\"") + '"';
}
out[i]=s;
}
return out;
}
}