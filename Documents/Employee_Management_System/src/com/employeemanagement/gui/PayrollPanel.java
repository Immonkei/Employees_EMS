package com.employeemanagement.gui;

import com.employeemanagement.dao.EmployeeDAO;
import com.employeemanagement.dao.PayrollDAO;
import com.employeemanagement.model.Employee;
import com.employeemanagement.model.Payroll;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PayrollPanel extends JPanel {

    private JTable payrollTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    private PayrollDAO payrollDAO;
    private EmployeeDAO employeeDAO; // To fetch employee list for dropdowns

    public PayrollPanel() {
        setLayout(new BorderLayout());
        payrollDAO = new PayrollDAO();
        employeeDAO = new EmployeeDAO();
        initComponents();
        loadPayrollRecords();
    }

    private void initComponents() {
        // Table setup
        tableModel = new DefaultTableModel(new Object[]{"ID", "Employee", "Payroll Date", "Gross Salary", "Deductions", "Net Salary"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        payrollTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(payrollTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Add Payroll");
        editButton = new JButton("Edit Payroll");
        deleteButton = new JButton("Delete Payroll");

        addButton.addActionListener(e -> addPayroll());
        editButton.addActionListener(e -> editPayroll());
        deleteButton.addActionListener(e -> deletePayroll());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadPayrollRecords() {
        tableModel.setRowCount(0); // Clear existing data
        try {
            List<Payroll> payrollRecords = payrollDAO.getAllPayrollRecords();
            for (Payroll payroll : payrollRecords) {
                tableModel.addRow(new Object[]{
                        payroll.getPayrollId(),
                        payroll.getEmployeeFullName(), // Display employee name
                        payroll.getPayrollDate(),
                        payroll.getGrossSalary(),
                        payroll.getDeductions(),
                        payroll.getNetSalary()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading payroll records: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void addPayroll() {
        // Dropdown for employees
        JComboBox<Employee> employeeComboBox = new JComboBox<>();
        List<Employee> employees = null;
        try {
            employees = employeeDAO.getAllEmployees();
            // Add a default "Select Employee" or "None" option if you want, but for payroll, it should be required
            for (Employee emp : employees) {
                employeeComboBox.addItem(emp);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        DatePicker payrollDateField = new DatePicker(); // LGoodDatePicker
        JTextField grossSalaryField = new JTextField(10);
        JTextField deductionsField = new JTextField("0.00", 10); // Default to 0.00

        // Panel for inputs
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Employee:"));
        panel.add(employeeComboBox);
        panel.add(new JLabel("Payroll Date:"));
        panel.add(payrollDateField);
        panel.add(new JLabel("Gross Salary:"));
        panel.add(grossSalaryField);
        panel.add(new JLabel("Deductions:"));
        panel.add(deductionsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Payroll Record",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Employee selectedEmployee = (Employee) employeeComboBox.getSelectedItem();
                if (selectedEmployee == null) {
                    JOptionPane.showMessageDialog(this, "Please select an employee.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int employeeId = selectedEmployee.getEmployeeId();
                LocalDate payrollDate = payrollDateField.getDate();
                BigDecimal grossSalary = new BigDecimal(grossSalaryField.getText().trim());
                BigDecimal deductions = new BigDecimal(deductionsField.getText().trim());
                BigDecimal netSalary = grossSalary.subtract(deductions); // Calculate net salary

                if (payrollDate == null) {
                    JOptionPane.showMessageDialog(this, "Please select a payroll date.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (grossSalary.compareTo(BigDecimal.ZERO) < 0 || deductions.compareTo(BigDecimal.ZERO) < 0) {
                    JOptionPane.showMessageDialog(this, "Gross Salary and Deductions cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                Payroll newPayroll = new Payroll(employeeId, payrollDate, grossSalary, deductions, netSalary);
                payrollDAO.addPayroll(newPayroll);
                loadPayrollRecords(); // Refresh table
                JOptionPane.showMessageDialog(this, "Payroll record added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format for salary/deductions. Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding payroll record: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void editPayroll() {
        int selectedRow = payrollTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a payroll record to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int payrollId = (int) tableModel.getValueAt(selectedRow, 0);
        Payroll existingPayroll = null;
        try {
            existingPayroll = payrollDAO.getPayrollById(payrollId);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving payroll details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        if (existingPayroll == null) {
            JOptionPane.showMessageDialog(this, "Payroll record not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Dropdown for employees
        JComboBox<Employee> employeeComboBox = new JComboBox<>();
        List<Employee> employees = null;
        try {
            employees = employeeDAO.getAllEmployees();
            for (Employee emp : employees) {
                employeeComboBox.addItem(emp);
                if (emp.getEmployeeId() == existingPayroll.getEmployeeId()) {
                    employeeComboBox.setSelectedItem(emp); // Select current employee
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading employees for selection: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        DatePicker payrollDateField = new DatePicker();
        payrollDateField.setDate(existingPayroll.getPayrollDate()); // Set existing date
        JTextField grossSalaryField = new JTextField(existingPayroll.getGrossSalary().toPlainString(), 10);
        JTextField deductionsField = new JTextField(existingPayroll.getDeductions().toPlainString(), 10);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Payroll ID:"));
        panel.add(new JLabel(String.valueOf(payrollId)));
        panel.add(new JLabel("Employee:"));
        panel.add(employeeComboBox);
        panel.add(new JLabel("Payroll Date:"));
        panel.add(payrollDateField);
        panel.add(new JLabel("Gross Salary:"));
        panel.add(grossSalaryField);
        panel.add(new JLabel("Deductions:"));
        panel.add(deductionsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Payroll Record",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Employee selectedEmployee = (Employee) employeeComboBox.getSelectedItem();
                if (selectedEmployee == null) {
                    JOptionPane.showMessageDialog(this, "Please select an employee.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int employeeId = selectedEmployee.getEmployeeId();
                LocalDate payrollDate = payrollDateField.getDate();
                BigDecimal grossSalary = new BigDecimal(grossSalaryField.getText().trim());
                BigDecimal deductions = new BigDecimal(deductionsField.getText().trim());
                BigDecimal netSalary = grossSalary.subtract(deductions);

                if (payrollDate == null) {
                    JOptionPane.showMessageDialog(this, "Please select a payroll date.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (grossSalary.compareTo(BigDecimal.ZERO) < 0 || deductions.compareTo(BigDecimal.ZERO) < 0) {
                    JOptionPane.showMessageDialog(this, "Gross Salary and Deductions cannot be negative.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                existingPayroll.setEmployeeId(employeeId);
                existingPayroll.setPayrollDate(payrollDate);
                existingPayroll.setGrossSalary(grossSalary);
                existingPayroll.setDeductions(deductions);
                existingPayroll.setNetSalary(netSalary); // Update calculated net salary

                payrollDAO.updatePayroll(existingPayroll);
                loadPayrollRecords(); // Refresh table
                JOptionPane.showMessageDialog(this, "Payroll record updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format for salary/deductions. Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error updating payroll record: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void deletePayroll() {
        int selectedRow = payrollTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a payroll record to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int payrollId = (int) tableModel.getValueAt(selectedRow, 0);
        String employeeInfo = (String) tableModel.getValueAt(selectedRow, 1); // Employee name from table
        LocalDate payrollDate = (LocalDate) tableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete payroll record for " + employeeInfo + " on " + payrollDate + "?\n" +
                        "This action cannot be undone.",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                payrollDAO.deletePayroll(payrollId);
                loadPayrollRecords(); // Refresh table
                JOptionPane.showMessageDialog(this, "Payroll record deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting payroll record: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}