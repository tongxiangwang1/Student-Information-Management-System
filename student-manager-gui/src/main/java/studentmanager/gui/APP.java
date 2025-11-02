/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package studentmanager.gui;
//
import studentmanager.gui.db.DatabaseManager;
import studentmanager.gui.ui.MainFrame;

import javax.swing.SwingUtilities;

//
public class APP {
    public static void main(String[] args) {
        // Initialize Derby
        DatabaseManager.getInstance().init();

        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
