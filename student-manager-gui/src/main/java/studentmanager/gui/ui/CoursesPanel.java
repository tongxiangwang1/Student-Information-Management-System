/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentmanager.gui.ui;

import studentmanager.gui.model.Course;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CoursesPanel extends JPanel {
    private final List<Course> data = new ArrayList<>();
    private final JTable table = new JTable(new Model());

    public CoursesPanel() {
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bar = new JPanel();
        JButton add = new JButton("Add"); JButton del = new JButton("Remove");
        bar.add(add); bar.add(del);
        add(bar, BorderLayout.SOUTH);

        add.addActionListener(e -> addCourse(new Course("", 0)));
        del.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                data.remove(r);
                ((AbstractTableModel) table.getModel()).fireTableDataChanged();
            }
        });

        //editor
        table.getColumnModel().getColumn(1).setCellEditor(new SpinnerEditor(0, 0, 100, 1));
    }

    public void addCourse(Course c) {
        data.add(c);
        ((AbstractTableModel) table.getModel()).fireTableDataChanged();
    }

    public List<Course> getCourses() { return new ArrayList<>(data); }

    private class Model extends AbstractTableModel {
        private final String[] cols = {"Name","Grade"};
        @Override public int getRowCount() { return data.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }
        @Override public Object getValueAt(int r, int c) {
            return c == 0 ? data.get(r).getName() : data.get(r).getGrade();
        }
        @Override public boolean isCellEditable(int r, int c) { return true; }
        @Override public void setValueAt(Object a, int r, int c) {
            if (c == 0) data.get(r).setName(String.valueOf(a));
            else data.get(r).setGrade(parseIntSafe(a));
        }
        private int parseIntSafe(Object o) {
            try { return Integer.parseInt(String.valueOf(o)); }
            catch (Exception e) { return 0; }
        }
    }

    //editor
    private static class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {
        private final JSpinner spinner;

        SpinnerEditor(int value, int min, int max, int step) {
            spinner = new JSpinner(new SpinnerNumberModel(value, min, max, step));
        }

        @Override
        public Object getCellEditorValue() {
            Object v = spinner.getValue();
            if (v instanceof Integer) return v;
            try { return Integer.parseInt(String.valueOf(v)); }
            catch (Exception e) { return 0; }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            try {
                spinner.setValue(Integer.parseInt(String.valueOf(value)));
            } catch (Exception e) {
                spinner.setValue(0);
            }
            return spinner;
        }
    }
}
