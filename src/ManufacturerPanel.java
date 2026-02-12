package Lab4.src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManufacturerPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public ManufacturerPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(250, 245, 255));

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

        add.addActionListener(e -> addManufacturer());
        delete.addActionListener(e -> deleteManufacturer());
        refresh.addActionListener(e -> loadData());

        panel.add(add);
        panel.add(delete);
        panel.add(refresh);

        return panel;
    }

    private void style(JButton b) {
        b.setBackground(new Color(138,43,226));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
    }

    private void loadData() {
        try {
            model.setRowCount(0);
            model.setColumnCount(0);

            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement()
                    .executeQuery("SELECT * FROM manufacturers");

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

    private void addManufacturer() {
        try {
            String name = JOptionPane.showInputDialog("Name:");
            String country = JOptionPane.showInputDialog("Country:");

            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO manufacturers(name, country) VALUES(?,?)");

            ps.setString(1, name);
            ps.setString(2, country);
            ps.executeUpdate();

            conn.close();
            loadData();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteManufacturer() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) return;

            int id = (int) model.getValueAt(row, 0);

            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM manufacturers WHERE manufacturer_id=?");

            ps.setInt(1, id);
            ps.executeUpdate();

            conn.close();
            loadData();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
