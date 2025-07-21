package com.employeemanagement.dao;

import com.employeemanagement.database.DatabaseManager;
import com.employeemanagement.model.Employee;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public void addEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO Employees (first_name, last_name, email, phone_number, hire_date, job_title, salary, department_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, employee.getFirstName());
            pstmt.setString(2, employee.getLastName());
            pstmt.setString(3, employee.getEmail());
            pstmt.setString(4, employee.getPhoneNumber());
            pstmt.setDate(5, Date.valueOf(employee.getHireDate()));
            pstmt.setString(6, employee.getJobTitle());
            pstmt.setBigDecimal(7, employee.getSalary());
            if (employee.getDepartmentId() == 0) { // Handle case where no department is selected (e.g., "None" or 0)
                pstmt.setNull(8, Types.INTEGER);
            } else {
                pstmt.setInt(8, employee.getDepartmentId());
            }
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    employee.setEmployeeId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.employee_id, e.first_name, e.last_name, e.email, e.phone_number, e.hire_date, e.job_title, e.salary, e.department_id, d.department_name " +
                "FROM Employees e LEFT JOIN Departments d ON e.department_id = d.department_id";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getDate("hire_date").toLocalDate(),
                        rs.getString("job_title"),
                        rs.getBigDecimal("salary"),
                        rs.getInt("department_id"),
                        rs.getString("department_name")
                );
                employees.add(employee);
            }
        }
        return employees;
    }

    public void updateEmployee(Employee employee) throws SQLException {
        String sql = "UPDATE Employees SET first_name = ?, last_name = ?, email = ?, phone_number = ?, hire_date = ?, job_title = ?, salary = ?, department_id = ? WHERE employee_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employee.getFirstName());
            pstmt.setString(2, employee.getLastName());
            pstmt.setString(3, employee.getEmail());
            pstmt.setString(4, employee.getPhoneNumber());
            pstmt.setDate(5, Date.valueOf(employee.getHireDate()));
            pstmt.setString(6, employee.getJobTitle());
            pstmt.setBigDecimal(7, employee.getSalary());
            if (employee.getDepartmentId() == 0) { // Handle case where no department is selected
                pstmt.setNull(8, Types.INTEGER);
            } else {
                pstmt.setInt(8, employee.getDepartmentId());
            }
            pstmt.setInt(9, employee.getEmployeeId());
            pstmt.executeUpdate();
        }
    }

    public void deleteEmployee(int employeeId) throws SQLException {
        String sql = "DELETE FROM Employees WHERE employee_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
        }
    }

    public Employee getEmployeeById(int employeeId) throws SQLException {
        String sql = "SELECT e.employee_id, e.first_name, e.last_name, e.email, e.phone_number, e.hire_date, e.job_title, e.salary, e.department_id, d.department_name " +
                "FROM Employees e LEFT JOIN Departments d ON e.department_id = d.department_id WHERE e.employee_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                            rs.getInt("employee_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("phone_number"),
                            rs.getDate("hire_date").toLocalDate(),
                            rs.getString("job_title"),
                            rs.getBigDecimal("salary"),
                            rs.getInt("department_id"),
                            rs.getString("department_name")
                    );
                }
            }
        }
        return null;
    }
}