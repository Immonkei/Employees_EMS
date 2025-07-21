package com.employeemanagement.dao;

import com.employeemanagement.database.DatabaseManager;
import com.employeemanagement.model.Payroll;
import com.employeemanagement.model.Employee; // Needed if we fetch employee objects for dropdowns etc.

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PayrollDAO {

    public void addPayroll(Payroll payroll) throws SQLException {
        String sql = "INSERT INTO Payroll (employee_id, payroll_date, gross_salary, deductions, net_salary) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, payroll.getEmployeeId());
            pstmt.setDate(2, Date.valueOf(payroll.getPayrollDate()));
            pstmt.setBigDecimal(3, payroll.getGrossSalary());
            pstmt.setBigDecimal(4, payroll.getDeductions());
            pstmt.setBigDecimal(5, payroll.getNetSalary());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    payroll.setPayrollId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Payroll> getAllPayrollRecords() throws SQLException {
        List<Payroll> payrollRecords = new ArrayList<>();
        String sql = "SELECT p.payroll_id, p.employee_id, e.first_name, e.last_name, p.payroll_date, p.gross_salary, p.deductions, p.net_salary " +
                "FROM Payroll p JOIN Employees e ON p.employee_id = e.employee_id ORDER BY p.payroll_date DESC, e.last_name, e.first_name";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Payroll payroll = new Payroll(
                        rs.getInt("payroll_id"),
                        rs.getInt("employee_id"),
                        rs.getString("first_name") + " " + rs.getString("last_name"), // Combine names
                        rs.getDate("payroll_date").toLocalDate(),
                        rs.getBigDecimal("gross_salary"),
                        rs.getBigDecimal("deductions"),
                        rs.getBigDecimal("net_salary")
                );
                payrollRecords.add(payroll);
            }
        }
        return payrollRecords;
    }

    public Payroll getPayrollById(int payrollId) throws SQLException {
        String sql = "SELECT p.payroll_id, p.employee_id, e.first_name, e.last_name, p.payroll_date, p.gross_salary, p.deductions, p.net_salary " +
                "FROM Payroll p JOIN Employees e ON p.employee_id = e.employee_id WHERE p.payroll_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, payrollId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Payroll(
                            rs.getInt("payroll_id"),
                            rs.getInt("employee_id"),
                            rs.getString("first_name") + " " + rs.getString("last_name"),
                            rs.getDate("payroll_date").toLocalDate(),
                            rs.getBigDecimal("gross_salary"),
                            rs.getBigDecimal("deductions"),
                            rs.getBigDecimal("net_salary")
                    );
                }
            }
        }
        return null; // Payroll record not found
    }

    public void updatePayroll(Payroll payroll) throws SQLException {
        String sql = "UPDATE Payroll SET employee_id = ?, payroll_date = ?, gross_salary = ?, deductions = ?, net_salary = ? WHERE payroll_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, payroll.getEmployeeId());
            pstmt.setDate(2, Date.valueOf(payroll.getPayrollDate()));
            pstmt.setBigDecimal(3, payroll.getGrossSalary());
            pstmt.setBigDecimal(4, payroll.getDeductions());
            pstmt.setBigDecimal(5, payroll.getNetSalary());
            pstmt.setInt(6, payroll.getPayrollId());
            pstmt.executeUpdate();
        }
    }

    public void deletePayroll(int payrollId) throws SQLException {
        String sql = "DELETE FROM Payroll WHERE payroll_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, payrollId);
            pstmt.executeUpdate();
        }
    }
}