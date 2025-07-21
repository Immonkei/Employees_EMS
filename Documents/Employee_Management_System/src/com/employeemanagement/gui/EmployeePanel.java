package com.employeemanagement.gui;

import com.employeemanagement.dao.DepartmentDAO;
import com.employeemanagement.dao.EmployeeDAO;
import com.employeemanagement.model.Department;
import com.employeemanagement.model.Employee;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EmployeePanel extends JPanel {

    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    private EmployeeDAO employeeDAO;
    private DepartmentDAO departmentDAO;

    public EmployeePanel() {
        setLayout(new BorderLayout());
        employeeDAO = new EmployeeDAO();
        departmentDAO = new DepartmentDAO();
        initComponents();
        loadEmployees();
    }

    private void initComponents() {
        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(0xE0E0E0)); // Light grey header background
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15)); // Padding
        JLabel titleLabel = new JLabel("Manage Employees");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Bold, larger font
        titleLabel.setForeground(new Color(0x333333)); // Dark text
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- Table Panel ---
        JPanel tableWrapperPanel = new JPanel(new BorderLayout());
        tableWrapperPanel.setBorder(new EmptyBorder(15, 15, 0, 15));
        tableModel = new DefaultTableModel(new Object[]{"ID", "First Name", "Last Name", "Email", "Phone", "Hire Date", "Job Title", "Salary", "Department"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        employeeTable = new JTable(tableModel);
        employeeTable.setRowHeight(25); // Slightly taller rows
        employeeTable.setAutoCreateRowSorter(true); // Enable sorting
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        tableWrapperPanel.add(scrollPane, BorderLayout.CENTER);
        add(tableWrapperPanel, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(0xF0F2F5)); // Match main content background
        buttonPanel.setBorder(new EmptyBorder(0, 15, 15, 15));

        // Styled Buttons
        addButton = new JButton("Add Employee");
        addButton.setBackground(new Color(0x4285F4)); // Primary accent color
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addButton.setFocusPainted(false);

        editButton = new JButton("Edit Employee");
        editButton.setBackground(new Color(0x4CAF50)); // Green for edit/update
        editButton.setForeground(Color.WHITE);
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        editButton.setFocusPainted(false);

        deleteButton = new JButton("Delete Employee");
        deleteButton.setBackground(new Color(0xF44336)); // Red for delete
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        deleteButton.setFocusPainted(false);

        addButton.addActionListener(e -> addEmployee());
        editButton.addActionListener(e -> editEmployee());
        deleteButton.addActionListener(e -> deleteEmployee());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadEmployees() {
        tableModel.setRowCount(0);
        try {
            List<Employee> employees = employeeDAO.getAllEmployees();
            for (Employee emp : employees) {
                tableModel.addRow(new Object[]{
                        emp.getEmployeeId(),
                        emp.getFirstName(),
                        emp.getLastName(),
                        emp.getEmail(),
                        emp.getPhoneNumber(),
                        emp.getHireDate(),
                        emp.getJobTitle(),
                        emp.getSalary(),
                        emp.getDepartmentName()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void addEmployee() {
        JTextField firstNameField = new JTextField(15);
        JTextField lastNameField = new JTextField(15);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(15);
        DatePicker hireDateField = new DatePicker();
        JTextField jobTitleField = new JTextField(15);
        JTextField salaryField = new JTextField(10);

        JComboBox<Department> departmentComboBox = new JComboBox<>();
        try {
            List<Department> departments = departmentDAO.getAllDepartments();
            departmentComboBox.addItem(null); // Option for no department
            for (Department dept : departments) {
                departmentComboBox.addItem(dept);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading departments for selection: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        JPanel panel = new JPanel(new GridBagLayout()); // Use GridBagLayout
        panel.setBackground(Color.WHITE); // White background for the form dialog
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make fields fill horizontally

        // First Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1;
        panel.add(firstNameField, gbc);

        // Last Name
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1;
        panel.add(lastNameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1;
        panel.add(emailField, gbc);

        // Phone Number
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1;
        panel.add(phoneField, gbc);

        // Hire Date
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("Hire Date:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1;
        panel.add(hireDateField, gbc);

        // Job Title
        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("Job Title:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1;
        panel.add(jobTitleField, gbc);

        // Salary
        gbc.gridx = 0; gbc.gridy = 6; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("Salary:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1;
        panel.add(salaryField, gbc);

        // Department
        gbc.gridx = 0; gbc.gridy = 7; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1; gbc.gridy = 7; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1;
        panel.add(departmentComboBox, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Employee",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                LocalDate hireDate = hireDateField.getDate();
                String jobTitle = jobTitleField.getText().trim();
                BigDecimal salary = new BigDecimal(salaryField.getText().trim());
                Department selectedDepartment = (Department) departmentComboBox.getSelectedItem();
                int departmentId = (selectedDepartment != null) ? selectedDepartment.getDepartmentId() : 0;

                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || hireDate == null || jobTitle.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all required fields (First Name, Last Name, Email, Hire Date, Job Title).", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Employee newEmployee = new Employee(firstName, lastName, email, phone, hireDate, jobTitle, salary, departmentId);
                employeeDAO.addEmployee(newEmployee);
                loadEmployees();
                JOptionPane.showMessageDialog(this, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid salary format. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding employee: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void editEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int employeeId = (int) tableModel.getValueAt(selectedRow, 0);
        Employee existingEmployee = null;
        try {
            existingEmployee = employeeDAO.getEmployeeById(employeeId);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving employee details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        if (existingEmployee == null) {
            JOptionPane.showMessageDialog(this, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField firstNameField = new JTextField(existingEmployee.getFirstName(), 15);
        JTextField lastNameField = new JTextField(existingEmployee.getLastName(), 15);
        JTextField emailField = new JTextField(existingEmployee.getEmail(), 20);
        JTextField phoneField = new JTextField(existingEmployee.getPhoneNumber(), 15);
        DatePicker hireDateField = new DatePicker();
        hireDateField.setDate(existingEmployee.getHireDate());
        JTextField jobTitleField = new JTextField(existingEmployee.getJobTitle(), 15);
        JTextField salaryField = new JTextField(existingEmployee.getSalary().toPlainString(), 10);

        JComboBox<Department> departmentComboBox = new JComboBox<>();
        try {
            List<Department> departments = departmentDAO.getAllDepartments();
            departmentComboBox.addItem(null);
            for (Department dept : departments) {
                departmentComboBox.addItem(dept);
                if (dept.getDepartmentId() == existingEmployee.getDepartmentId()) {
                    departmentComboBox.setSelectedItem(dept);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading departments for selection: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        JPanel panel = new JPanel(new GridBagLayout()); // Use GridBagLayout
        panel.setBackground(Color.WHITE); // White background for the form dialog
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Employee ID (Display Only)
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("Employee ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1;
        panel.add(new JLabel(String.valueOf(employeeId)), gbc);

        // First Name
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1;
        panel.add(firstNameField, gbc);

        // Last Name
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1;
        panel.add(lastNameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1;
        panel.add(emailField, gbc);

        // Phone Number
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1;
        panel.add(phoneField, gbc);

        // Hire Date
        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("Hire Date:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1;
        panel.add(hireDateField, gbc);

        // Job Title
        gbc.gridx = 0; gbc.gridy = 6; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("Job Title:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1;
        panel.add(jobTitleField, gbc);

        // Salary
        gbc.gridx = 0; gbc.gridy = 7; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("Salary:"), gbc);
        gbc.gridx = 1; gbc.gridy = 7; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1;
        panel.add(salaryField, gbc);

        // Department
        gbc.gridx = 0; gbc.gridy = 8; gbc.anchor = GridBagConstraints.EAST; gbc.weightx = 0;
        panel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1; gbc.gridy = 8; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1;
        panel.add(departmentComboBox, gbc);


        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Employee",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                LocalDate hireDate = hireDateField.getDate();
                String jobTitle = jobTitleField.getText().trim();
                BigDecimal salary = new BigDecimal(salaryField.getText().trim());
                Department selectedDepartment = (Department) departmentComboBox.getSelectedItem();
                int departmentId = (selectedDepartment != null) ? selectedDepartment.getDepartmentId() : 0;

                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || hireDate == null || jobTitle.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all required fields (First Name, Last Name, Email, Hire Date, Job Title).", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                existingEmployee.setFirstName(firstName);
                existingEmployee.setLastName(lastName);
                existingEmployee.setEmail(email);
                existingEmployee.setPhoneNumber(phone);
                existingEmployee.setHireDate(hireDate);
                existingEmployee.setJobTitle(jobTitle);
                existingEmployee.setSalary(salary);
                existingEmployee.setDepartmentId(departmentId);

                employeeDAO.updateEmployee(existingEmployee);
                loadEmployees();
                JOptionPane.showMessageDialog(this, "Employee updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid salary format. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error updating employee: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void deleteEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int employeeId = (int) tableModel.getValueAt(selectedRow, 0);
        String employeeName = (String) tableModel.getValueAt(selectedRow, 1) + " " + tableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete employee: " + employeeName + "?\n" +
                        "This action cannot be undone.",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                employeeDAO.deleteEmployee(employeeId);
                loadEmployees();
                JOptionPane.showMessageDialog(this, "Employee deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting employee: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}