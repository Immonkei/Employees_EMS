package com.employeemanagement.gui;

import com.employeemanagement.dao.DepartmentDAO;
import com.employeemanagement.model.Department;

import javax.swing.*;
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
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Location"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        departmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(departmentTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Add Department");
        editButton = new JButton("Edit Department");
        deleteButton = new JButton("Delete Department");

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
                tableModel.addRow(new Object[]{dept.getDepartmentId(), dept.getDepartmentName(), dept.getLocation()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading departments: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void addDepartment() {
        JTextField nameField = new JTextField(20);
        JTextField locationField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Department Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Location:"));
        panel.add(locationField);

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

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Department ID: " + departmentId));
        panel.add(new JLabel("Department Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Location:"));
        panel.add(locationField);

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
                        "This will also affect employees in this department (their department_id will be NULL or cascaded).",
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