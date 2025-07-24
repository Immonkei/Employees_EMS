package com.employeemanagement.gui;

import javax.swing.*;
import java.awt.*;
// Import FlatLaf
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf; // Or FlatDarkLaf, FlatIntelliJLaf, etc.

public class MainDashboard extends JFrame {

    private JTabbedPane tabbedPane;

    public MainDashboard() {
        setTitle("Management Employee System");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();

        // Add a wrapper panel for the tabbed pane to control its background
        JPanel contentWrapperPanel = new JPanel(new BorderLayout());
        contentWrapperPanel.setBackground(new Color(0xF0F2F5)); // A light gray background for the main content area
        contentWrapperPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add some padding around the tabs

        tabbedPane.addTab("Departments", new DepartmentPanel());
        tabbedPane.addTab("Employees", new EmployeePanel());
        tabbedPane.addTab("Payroll", new PayrollPanel());

        contentWrapperPanel.add(tabbedPane, BorderLayout.CENTER);
        add(contentWrapperPanel, BorderLayout.CENTER); // Add the wrapper panel to the frame

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set the FlatLaf Look and Feel
                FlatLaf.setup( new FlatLightLaf() ); // Using FlatLightLaf for a bright, modern look

                // Optional: You can set some global UI properties here if needed,
                // but FlatLaf already provides good defaults.
                // UIManager.put("Button.arc", 10); // Example: rounded buttons

            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainDashboard().setVisible(true);
        });
    }
}