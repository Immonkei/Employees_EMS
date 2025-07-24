package com.employeemanagement.gui;

import com.employeemanagement.dao.DepartmentDAO;
import com.employeemanagement.model.Department;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DepartmentPanel extends JPanel {

    private JTable departmentTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    private DepartmentDAO departmentDAO;

    public DepartmentPanel() {
        setLayout(new BorderLayout());
        departmentDAO = new DepartmentDAO();
        initComponents();
        loadDepartments();
    }

    private void initComponents() {
        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(0xE0E0E0));
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        JLabel titleLabel = new JLabel("Manage Departments");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0x333333));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- Table Panel ---
        JPanel tableWrapperPanel = new JPanel(new BorderLayout());
        tableWrapperPanel.setBorder(new EmptyBorder(15, 15, 0, 15));
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Location"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        departmentTable = new JTable(tableModel);
        departmentTable.getTableHeader().setReorderingAllowed(false); // 🔒 Disable column reordering
        departmentTable.setRowHeight(25);
        departmentTable.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(departmentTable);
        tableWrapperPanel.add(scrollPane, BorderLayout.CENTER);
        add(tableWrapperPanel, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(0xF0F2F5));
        buttonPanel.setBorder(new EmptyBorder(0, 15, 15, 15));

        addButton = new JButton("Add Department");
        addButton.setBackground(new Color(0x4285F4));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addButton.setFocusPainted(false);

        editButton = new JButton("Edit Department");
        editButton.setBackground(new Color(0x4CAF50));
        editButton.setForeground(Color.WHITE);
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        editButton.setFocusPainted(false);

        deleteButton = new JButton("Delete Department");
        deleteButton.setBackground(new Color(0xF44336));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        deleteButton.setFocusPainted(false);

        addButton.addActionListener(e -> addDepartment());
        editButton.addActionListener(e -> editDepartment());
        deleteButton.addActionListener(e -> deleteDepartment());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadDepartments() {
        tableModel.setRowCount(0);
        try {
            List<Department> departments = departmentDAO.getAllDepartments();
            for (Department dept : departments) {
                tableModel.addRow(new Object[]{
                        dept.getDepartmentId(),
                        dept.getDepartmentName(),
                        dept.getLocation()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading departments: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void addDepartment() {
        JTextField nameField = new JTextField(20);
        JTextField locationField = new JTextField(20);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Department Name:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(locationField, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Department",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String location = locationField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Department Name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Department newDept = new Department(name, location);
                departmentDAO.addDepartment(newDept);
                loadDepartments();
                JOptionPane.showMessageDialog(this, "Department added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding department: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void editDepartment() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a department to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int departmentId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        String currentLocation = (String) tableModel.getValueAt(selectedRow, 2);

        JTextField nameField = new JTextField(currentName, 20);
        JTextField locationField = new JTextField(currentLocation, 20);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Department ID:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel(String.valueOf(departmentId)), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Department Name:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(locationField, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Department",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newLocation = locationField.getText().trim();

            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Department Name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Department updatedDept = new Department(departmentId, newName, newLocation);
                departmentDAO.updateDepartment(updatedDept);
                loadDepartments();
                JOptionPane.showMessageDialog(this, "Department updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error updating department: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void deleteDepartment() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a department to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int departmentId = (int) tableModel.getValueAt(selectedRow, 0);
        String departmentName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete department: " + departmentName + "?\n" +
                        "This will also affect employees in this department.",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                departmentDAO.deleteDepartment(departmentId);
                loadDepartments();
                JOptionPane.showMessageDialog(this, "Department deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting department: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
