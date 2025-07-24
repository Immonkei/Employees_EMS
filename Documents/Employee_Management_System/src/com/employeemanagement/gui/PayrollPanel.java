package com.employeemanagement.gui;

import com.employeemanagement.dao.EmployeeDAO;
import com.employeemanagement.dao.PayrollDAO;
import com.employeemanagement.model.Employee;
import com.employeemanagement.model.Payroll;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private EmployeeDAO employeeDAO;

    public PayrollPanel() {
        setLayout(new BorderLayout());
        payrollDAO = new PayrollDAO();
        employeeDAO = new EmployeeDAO();
        initComponents();
        loadPayrollRecords();
    }

    private void initComponents() {
        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(0xE0E0E0));
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        JLabel titleLabel = new JLabel("Manage Payroll");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0x333333));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- Table Panel ---
        JPanel tableWrapperPanel = new JPanel(new BorderLayout());
        tableWrapperPanel.setBorder(new EmptyBorder(15, 15, 0, 15));

        tableModel = new DefaultTableModel(new Object[]{"ID", "Employee", "Payroll Date", "Gross Salary", "Deductions", "Net Salary"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        payrollTable = new JTable(tableModel);
        payrollTable.setRowHeight(25);
        payrollTable.setAutoCreateRowSorter(true);
        payrollTable.getTableHeader().setReorderingAllowed(false); // 👈 Prevent column moving

        JScrollPane scrollPane = new JScrollPane(payrollTable);
        tableWrapperPanel.add(scrollPane, BorderLayout.CENTER);
        add(tableWrapperPanel, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(0xF0F2F5));
        buttonPanel.setBorder(new EmptyBorder(0, 15, 15, 15));

        addButton = new JButton("Add Payroll");
        addButton.setBackground(new Color(0x4285F4));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addButton.setFocusPainted(false);

        editButton = new JButton("Edit Payroll");
        editButton.setBackground(new Color(0x4CAF50));
        editButton.setForeground(Color.WHITE);
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        editButton.setFocusPainted(false);

        deleteButton = new JButton("Delete Payroll");
        deleteButton.setBackground(new Color(0xF44336));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        deleteButton.setFocusPainted(false);

        addButton.addActionListener(e -> addPayroll());
        editButton.addActionListener(e -> editPayroll());
        deleteButton.addActionListener(e -> deletePayroll());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadPayrollRecords() {
        tableModel.setRowCount(0);
        try {
            List<Payroll> payrollRecords = payrollDAO.getAllPayrollRecords();
            for (Payroll payroll : payrollRecords) {
                tableModel.addRow(new Object[]{
                        payroll.getPayrollId(),
                        payroll.getEmployeeFullName(),
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
        JComboBox<Employee> employeeComboBox = new JComboBox<>();
        try {
            List<Employee> employees = employeeDAO.getAllEmployees();
            if (employees.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No employees found. Please add employees first.", "No Employees", JOptionPane.WARNING_MESSAGE);
                return;
            }
            for (Employee emp : employees) {
                employeeComboBox.addItem(emp);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        DatePicker payrollDateField = new DatePicker();
        JTextField grossSalaryField = new JTextField(10);
        JTextField deductionsField = new JTextField("0.00", 10);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Employee:"), gbc);
        gbc.gridx = 1;
        panel.add(employeeComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Payroll Date:"), gbc);
        gbc.gridx = 1;
        panel.add(payrollDateField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Gross Salary:"), gbc);
        gbc.gridx = 1;
        panel.add(grossSalaryField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Deductions:"), gbc);
        gbc.gridx = 1;
        panel.add(deductionsField, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Payroll Record", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

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

                Payroll newPayroll = new Payroll(employeeId, payrollDate, grossSalary, deductions, netSalary);
                payrollDAO.addPayroll(newPayroll);
                loadPayrollRecords();
                JOptionPane.showMessageDialog(this, "Payroll record added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format for salary/deductions.", "Input Error", JOptionPane.ERROR_MESSAGE);
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
        Payroll existingPayroll;
        try {
            existingPayroll = payrollDAO.getPayrollById(payrollId);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving payroll details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        JComboBox<Employee> employeeComboBox = new JComboBox<>();
        try {
            List<Employee> employees = employeeDAO.getAllEmployees();
            for (Employee emp : employees) {
                employeeComboBox.addItem(emp);
                if (emp.getEmployeeId() == existingPayroll.getEmployeeId()) {
                    employeeComboBox.setSelectedItem(emp);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DatePicker payrollDateField = new DatePicker();
        payrollDateField.setDate(existingPayroll.getPayrollDate());
        JTextField grossSalaryField = new JTextField(existingPayroll.getGrossSalary().toPlainString(), 10);
        JTextField deductionsField = new JTextField(existingPayroll.getDeductions().toPlainString(), 10);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Payroll ID:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(String.valueOf(payrollId)), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Employee:"), gbc);
        gbc.gridx = 1;
        panel.add(employeeComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Payroll Date:"), gbc);
        gbc.gridx = 1;
        panel.add(payrollDateField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Gross Salary:"), gbc);
        gbc.gridx = 1;
        panel.add(grossSalaryField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Deductions:"), gbc);
        gbc.gridx = 1;
        panel.add(deductionsField, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Payroll Record", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Employee selectedEmployee = (Employee) employeeComboBox.getSelectedItem();
                int employeeId = selectedEmployee.getEmployeeId();
                LocalDate payrollDate = payrollDateField.getDate();
                BigDecimal grossSalary = new BigDecimal(grossSalaryField.getText().trim());
                BigDecimal deductions = new BigDecimal(deductionsField.getText().trim());
                BigDecimal netSalary = grossSalary.subtract(deductions);

                existingPayroll.setEmployeeId(employeeId);
                existingPayroll.setPayrollDate(payrollDate);
                existingPayroll.setGrossSalary(grossSalary);
                existingPayroll.setDeductions(deductions);
                existingPayroll.setNetSalary(netSalary);

                payrollDAO.updatePayroll(existingPayroll);
                loadPayrollRecords();
                JOptionPane.showMessageDialog(this, "Payroll record updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to update payroll: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        String employeeInfo = (String) tableModel.getValueAt(selectedRow, 1);
        LocalDate payrollDate = (LocalDate) tableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete payroll record for " + employeeInfo + " on " + payrollDate + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                payrollDAO.deletePayroll(payrollId);
                loadPayrollRecords();
                JOptionPane.showMessageDialog(this, "Payroll record deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting payroll record: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
