package com.employeemanagement.gui;

import com.employeemanagement.dao.DepartmentDAO;
import com.employeemanagement.dao.EmployeeDAO;
import com.employeemanagement.model.Department;
import com.employeemanagement.model.Employee;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
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
        tableModel = new DefaultTableModel(new Object[]{"ID", "First Name", "Last Name", "Email", "Phone", "Hire Date", "Job Title", "Salary", "Department"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Add Employee");
        editButton = new JButton("Edit Employee");
        deleteButton = new JButton("Delete Employee");

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
        List<Department> departments = null;
        try {
            departments = departmentDAO.getAllDepartments();
            departmentComboBox.addItem(null);
            for (Department dept : departments) {
                departmentComboBox.addItem(dept);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading departments for selection: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneField);
        panel.add(new JLabel("Hire Date:"));
        panel.add(hireDateField);
        panel.add(new JLabel("Job Title:"));
        panel.add(jobTitleField);
        panel.add(new JLabel("Salary:"));
        panel.add(salaryField);
        panel.add(new JLabel("Department:"));
        panel.add(departmentComboBox);


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
        List<Department> departments = null;
        try {
            departments = departmentDAO.getAllDepartments();
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

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Employee ID:"));
        panel.add(new JLabel(String.valueOf(employeeId)));
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneField);
        panel.add(new JLabel("Hire Date:"));
        panel.add(hireDateField);
        panel.add(new JLabel("Job Title:"));
        panel.add(jobTitleField);
        panel.add(new JLabel("Salary:"));
        panel.add(salaryField);
        panel.add(new JLabel("Department:"));
        panel.add(departmentComboBox);

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