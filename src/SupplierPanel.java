package Lab4.src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SupplierPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public SupplierPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 250, 255));

        model = new DefaultTableModel();
        table = new JTable(model);
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = createButtonPanel();

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadData();
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();

        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");

        styleButton(add);
        styleButton(delete);
        styleButton(refresh);

        add.addActionListener(e -> addSupplier());
        delete.addActionListener(e -> deleteSupplier());
        refresh.addActionListener(e -> loadData());

        panel.add(add);
        panel.add(delete);
        panel.add(refresh);

        return panel;
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(70,130,180));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    private void loadData() {
        try {
            model.setRowCount(0);
            model.setColumnCount(0);

            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement()
                    .executeQuery("SELECT * FROM suppliers");

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

    private void addSupplier() {
        try {
            String name = JOptionPane.showInputDialog("Name:");
            String phone = JOptionPane.showInputDialog("Phone:");
            String address = JOptionPane.showInputDialog("Address:");

            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO suppliers(name, phone, address) VALUES(?,?,?)");

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, address);
            ps.executeUpdate();

            conn.close();
            loadData();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteSupplier() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) return;

            int id = (int) model.getValueAt(row, 0);

            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM suppliers WHERE supplier_id=?");

            ps.setInt(1, id);
            ps.executeUpdate();

            conn.close();
            loadData();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
