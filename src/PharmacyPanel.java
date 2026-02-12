package Lab4.src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PharmacyPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public PharmacyPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240,255,240));

        model = new DefaultTableModel();
        table = new JTable(model);
        table.setRowHeight(25);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);

        loadData();
    }

    private JPanel createButtons() {
        JPanel panel = new JPanel();

        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");

        style(add);
        style(delete);
        style(refresh);

        add.addActionListener(e -> addRecord());
        delete.addActionListener(e -> deleteRecord());
        refresh.addActionListener(e -> loadData());

        panel.add(add);
        panel.add(delete);
        panel.add(refresh);

        return panel;
    }

    private void style(JButton b) {
        b.setBackground(new Color(34,139,34));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
    }

    private void loadData() {
        try {
            model.setRowCount(0);
            model.setColumnCount(0);

            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT p.pharmacy_id, m.brand AS medicine, s.name AS supplier, p.quantity " +
                    "FROM pharmacy p " +
                    "JOIN medicines m ON p.medicine_id = m.medicine_id " +
                    "JOIN suppliers s ON p.supplier_id = s.supplier_id");

            ResultSetMetaData meta = rs.getMetaData();
            int col = meta.getColumnCount();

            for (int i = 1; i <= col; i++)
                model.addColumn(meta.getColumnName(i));

            while (rs.next()) {
                Object[] row = new Object[col];
                for (int i = 1; i <= col; i++)
                    row[i - 1] = rs.getObject(i);
                model.addRow(row);
            }

            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addRecord() {
        try {
            String medicine = JOptionPane.showInputDialog("Medicine ID:");
            String supplier = JOptionPane.showInputDialog("Supplier ID:");
            String quantity = JOptionPane.showInputDialog("Quantity:");

            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO pharmacy(medicine_id, supplier_id, quantity) VALUES(?,?,?)");

            ps.setInt(1, Integer.parseInt(medicine));
            ps.setInt(2, Integer.parseInt(supplier));
            ps.setInt(3, Integer.parseInt(quantity));
            ps.executeUpdate();

            conn.close();
            loadData();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteRecord() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) return;

            int id = (int) model.getValueAt(row, 0);

            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM pharmacy WHERE pharmacy_id=?");

            ps.setInt(1, id);
            ps.executeUpdate();

            conn.close();
            loadData();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
