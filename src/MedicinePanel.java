package Lab4.src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MedicinePanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public MedicinePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        model = new DefaultTableModel();
        table = new JTable(model);
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Add");
        JButton btnDelete = new JButton("Delete");
        JButton btnRefresh = new JButton("Refresh");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadData();

        btnRefresh.addActionListener(e -> loadData());

        btnAdd.addActionListener(e -> addMedicine());

        btnDelete.addActionListener(e -> deleteMedicine());
    }

    private void loadData() {
        try {
            model.setRowCount(0);
            model.setColumnCount(0);

            Connection conn = DBConnection.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM medicines");

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(meta.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addMedicine() {
        try {
            String type = JOptionPane.showInputDialog("Type:");
            String brand = JOptionPane.showInputDialog("Brand:");
            String manufacturer = JOptionPane.showInputDialog("Manufacturer ID:");
            String expiration = JOptionPane.showInputDialog("Expiration date (YYYY-MM-DD):");
            String price = JOptionPane.showInputDialog("Price:");

            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO medicines(type, brand, manufacturer_id, expiration_date, price) VALUES(?,?,?,?,?)"
            );

            ps.setString(1, type);
            ps.setString(2, brand);
            ps.setInt(3, Integer.parseInt(manufacturer));
            ps.setString(4, expiration);
            ps.setDouble(5, Double.parseDouble(price));

            ps.executeUpdate();
            conn.close();

            loadData();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteMedicine() {
        try {
            int row = table.getSelectedRow();
            if (row == -1) return;

            int id = (int) model.getValueAt(row, 0);

            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM medicines WHERE medicine_id=?"
            );

            ps.setInt(1, id);
            ps.executeUpdate();
            conn.close();

            loadData();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
