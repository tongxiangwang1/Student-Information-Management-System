/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentmanager.gui.ui;

import studentmanager.gui.model.Student;
import studentmanager.gui.service.StudentService;
import studentmanager.gui.util.FileExporter;
import studentmanager.gui.util.FileImporter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class MainFrame extends JFrame {
    private final StudentService service = new StudentService();
    private final StudentTableModel tableModel = new StudentTableModel();
    private final JTable table = new JTable(tableModel);
    private final JTextField searchField = new JTextField();
    private final JLabel status = new JLabel("Ready");

    //list and details
    private final JPanel cards = new JPanel(new CardLayout());
    private static final String CARD_LIST = "list";
    private static final String CARD_DETAILS = "details";

    public MainFrame() {
        super("Student Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildToolbar(), BorderLayout.NORTH);
        add(cards, BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
        setJMenuBar(buildMenuBar());

        // Initialize list cards
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        cards.add(listPanel, CARD_LIST);

        refresh();
        showList();
    }

    private void showList() {
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, CARD_LIST);
    }

    private void showDetails(Student s) {
        StudentDetailsPanel details = new StudentDetailsPanel(s, this::showList);
        cards.add(details, CARD_DETAILS);
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.show(cards, CARD_DETAILS);
    }

    private JComponent buildToolbar() {
        JPanel p = new JPanel(new BorderLayout(8,0));
        JPanel left = new JPanel();
        JButton btnAdd = new JButton("Add");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        JButton btnView = new JButton("View");
        JButton btnExport = new JButton("Export CSV");
        JButton btnRefresh = new JButton("Refresh");

        left.add(btnAdd); left.add(btnEdit); left.add(btnDelete); left.add(btnView); left.add(btnExport); left.add(btnRefresh);
        p.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new BorderLayout(4,0));
        right.add(new JLabel(" Search (ID or Name):"), BorderLayout.WEST);
        right.add(searchField, BorderLayout.CENTER);
        JButton btnFind = new JButton("Go");
        right.add(btnFind, BorderLayout.EAST);
        p.add(right, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> addStudent());
        btnEdit.addActionListener(e -> editSelected());
        btnDelete.addActionListener(e -> deleteSelected());
        btnView.addActionListener(e -> viewSelected());
        btnExport.addActionListener(e -> exportCsv());
        btnRefresh.addActionListener(e -> refresh());
        btnFind.addActionListener(e -> search());
        searchField.addActionListener(e -> search()); // Press Enter to trigger search
        // Double-click to view directly
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && table.getSelectedRow() >= 0) viewSelected();
            }
        });

        return p;
    }

    private JMenuBar buildMenuBar() {
        JMenuBar mb = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem miImport = new JMenuItem("Import legacy students.txt");
        JMenuItem miReset = new JMenuItem("Reset view");
        JMenuItem miExit = new JMenuItem("Exit");
        file.add(miImport); file.addSeparator(); file.add(miReset); file.addSeparator(); file.add(miExit);

        miImport.addActionListener(e -> importLegacy());
        miReset.addActionListener(e -> { searchField.setText(""); refresh(); showList(); });
        miExit.addActionListener(e -> dispose());

        mb.add(file);
        return mb;
    }

    private JComponent buildStatusBar() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
        p.add(status, BorderLayout.WEST);
        return p;
    }

    private void refresh() {
        try {
            List<Student> all = service.listAll();
            tableModel.setData(all);
            status.setText("Loaded " + all.size() + " students");
        } catch (SQLException ex) {
            showError("Failed to load students: " + ex.getMessage());
        }
    }

    //Search directly by student ID and name
    private void search() {
        String q = searchField.getText().trim();
        try {
            List<Student> data = q.isEmpty() ? service.listAll() : service.searchByIdOrName(q);
            tableModel.setData(data);
            status.setText("Found " + data.size() + " record(s)");
            showList();
        } catch (SQLException ex) { showError("Search failed: " + ex.getMessage()); }
    }

    private void addStudent() {
        // Create and edit
        StudentFormDialog dlg = new StudentFormDialog(this, null);
        dlg.setVisible(true);
        if (dlg.getResult() != null) {
            try {
                service.saveStudent(dlg.getResult());
                refresh();
                showInfo("Student saved");
                showList();
            } catch (Exception ex) { showError(ex.getMessage()); }
        }
    }

    private void editSelected() {
        Student s = tableModel.getAt(table.getSelectedRow());
        if (s == null) { showWarn("Please select a student"); return; }
        try {
            s = service.loadFull(s.getId());
        } catch (SQLException ex) { showError(ex.getMessage()); return; }
        StudentFormDialog dlg = new StudentFormDialog(this, s);
        dlg.setVisible(true);
        if (dlg.getResult() != null) {
            try { service.saveStudent(dlg.getResult()); refresh(); showInfo("Updated"); showList(); }
            catch (Exception ex) { showError(ex.getMessage()); }
        }
    }

    private void viewSelected() {
        Student s = tableModel.getAt(table.getSelectedRow());
        if (s == null) { showWarn("Please select a student"); return; }
        try {
            s = service.loadFull(s.getId());
            showDetails(s); // You can switch to the student's detailed information page.
        } catch (SQLException ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteSelected() {
        Student s = tableModel.getAt(table.getSelectedRow());
        if (s == null) { showWarn("Please select a student"); return; }
        int ok = JOptionPane.showConfirmDialog(this, "Delete " + s.getName() + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            try { service.deleteStudent(s.getId()); refresh(); showList(); }
            catch (SQLException ex) { showError(ex.getMessage()); }
        }
    }

    private void exportCsv() {
        JFileChooser fc = new JFileChooser(new File("."));
        fc.setSelectedFile(new File("students.csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try { FileExporter.exportStudentsCsv(fc.getSelectedFile(), tableModel.getData()); showInfo("Exported"); }
            catch (Exception ex) { showError("Export failed: " + ex.getMessage()); }
        }
    }

    private void importLegacy() {
        JFileChooser fc = new JFileChooser(new File("."));
        fc.setSelectedFile(new File("students.txt"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                List<Student> legacy = FileImporter.readLegacyStudents(fc.getSelectedFile());
                for (Student s : legacy) service.saveStudent(s);
                refresh();
                showInfo("Imported " + legacy.size() + " student(s)");
                showList();
            } catch (Exception ex) { showError("Import failed: " + ex.getMessage()); }
        }
    }

    private void showInfo(String m) { JOptionPane.showMessageDialog(this, m, "Info", JOptionPane.INFORMATION_MESSAGE); }
    private void showWarn(String m) { JOptionPane.showMessageDialog(this, m, "Warning", JOptionPane.WARNING_MESSAGE); }
    private void showError(String m) { JOptionPane.showMessageDialog(this, m, "Error", JOptionPane.ERROR_MESSAGE); }
}
