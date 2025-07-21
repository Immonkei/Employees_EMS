package com.employeemanagement.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainDashboard extends JFrame {

    private JTabbedPane tabbedPane;

    public MainDashboard() {
        // Set the title of the JFrame
        setTitle("Employee Management System");

        // Set the default size of the window
        setSize(900, 650); // Adjusted size to comfortably fit content

        // Set the default close operation (exit application on close)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Initialize and set up the components of the dashboard
        initComponents();
    }

    private void initComponents() {
        // Set the layout for the main JFrame to BorderLayout
        setLayout(new BorderLayout());

        // Create a JTabbedPane for navigation between different modules
        tabbedPane = new JTabbedPane();

        // Create instances of each panel and add them as tabs
        // Make sure these panel classes (EmployeePanel, DepartmentPanel, PayrollPanel)
        // exist in the com.employeemanagement.gui package and are correctly implemented.
        tabbedPane.addTab("Employees", new EmployeePanel());
        tabbedPane.addTab("Departments", new DepartmentPanel());
        tabbedPane.addTab("Payroll", new PayrollPanel());

        // Add the tabbed pane to the center of the JFrame
        add(tabbedPane, BorderLayout.CENTER);

        // Optional: Add a menu bar for common actions like Exit
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");

        // Add an ActionListener to the Exit menu item to close the application
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // Set the menu bar for the JFrame
        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        // Ensure that Swing GUI updates are performed on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // Optional: Set the look and feel of the application to the system's default
                // This makes the application look native to the OS (Windows, macOS, Linux)
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Print any exceptions that occur during LookAndFeel setup
                e.printStackTrace();
            }
            // Create and make the MainDashboard visible
            new MainDashboard().setVisible(true);
        });
    }
}