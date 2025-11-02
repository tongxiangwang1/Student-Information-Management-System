/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentmanager.gui.ui;

import studentmanager.gui.model.Internship;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InternshipsPanel extends JPanel {
    private final List<Internship> data = new ArrayList<>();
    private final JTable table = new JTable(new Model());

    public InternshipsPanel() {
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel bar = new JPanel();
        JButton add = new JButton("Add"); JButton del = new JButton("Remove");
        bar.add(add); bar.add(del);
        add(bar, BorderLayout.SOUTH);

        add.addActionListener(e -> addInternship(new Internship("", "")));
        del.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r>=0) { data.remove(r); ((AbstractTableModel)table.getModel()).fireTableDataChanged(); }
        });
    }

    public void addInternship(Internship i) { data.add(i); ((AbstractTableModel)table.getModel()).fireTableDataChanged(); }
    public List<Internship> getInternships() { return new ArrayList<>(data); }

    private class Model extends AbstractTableModel {
        private final String[] cols = {"Place","Time"};
        @Override public int getRowCount() { return data.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }
        @Override public Object getValueAt(int r, int c) {
            return c==0? data.get(r).getPlace() : data.get(r).getTime();
        }
        @Override public boolean isCellEditable(int r,int c){ return true; }
        @Override public void setValueAt(Object a,int r,int c){
            if (c==0) data.get(r).setPlace(String.valueOf(a));
            else data.get(r).setTime(String.valueOf(a));
        }
    }
}
