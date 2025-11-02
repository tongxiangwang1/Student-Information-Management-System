/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentmanager.gui.ui;

import studentmanager.gui.model.Student;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class StudentTableModel extends AbstractTableModel {
    private final String[] cols = {"ID","Name","Gender","Age","Email","Major","Degree"};
    private final List<Student> data = new ArrayList<>();

    public void setData(List<Student> list) { data.clear(); data.addAll(list); fireTableDataChanged(); }
    public List<Student> getData() { return new ArrayList<>(data); }
    public Student getAt(int row) { return (row>=0 && row < data.size()) ? data.get(row) : null; }

    @Override public int getRowCount() { return data.size(); }
    @Override public int getColumnCount() { return cols.length; }
    @Override public String getColumnName(int c) { return cols[c]; }
    @Override public Object getValueAt(int r, int c) {
        Student s = data.get(r);
        return switch (c) {
            case 0 -> s.getId();
            case 1 -> s.getName();
            case 2 -> s.getGender();
            case 3 -> s.getAge();
            case 4 -> s.getEmail();
            case 5 -> s.getMajor();
            case 6 -> s.getDegree();
            default -> "";
        };
    }
}
