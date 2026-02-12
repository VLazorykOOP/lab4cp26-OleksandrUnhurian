package Lab4.src;

import javax.swing.*;

public class MainForm extends JFrame {

    public MainForm() {
        setTitle("Pharmacy Management System");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Medicines", new MedicinePanel());
        tabs.add("Suppliers", new SupplierPanel());
        tabs.add("Manufacturers", new ManufacturerPanel());
        tabs.add("Pharmacy", new PharmacyPanel());

        add(tabs);

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainForm(); 
    }
}
