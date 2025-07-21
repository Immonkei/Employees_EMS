package com.employeemanagement.dao;

import com.employeemanagement.database.DatabaseManager;
import com.employeemanagement.model.Department;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    public void addDepartment(Department department) throws SQLException {
        String sql = "INSERT INTO Departments (department_name, location) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, department.getDepartmentName());
            pstmt.setString(2, department.getLocation());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    department.setDepartmentId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Department> getAllDepartments() throws SQLException {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT department_id, department_name, location FROM Departments";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Department department = new Department(
                        rs.getInt("department_id"),
                        rs.getString("department_name"),
                        rs.getString("location")
                );
                departments.add(department);
            }
        }
        return departments;
    }

    public void updateDepartment(Department department) throws SQLException {
        String sql = "UPDATE Departments SET department_name = ?, location = ? WHERE department_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, department.getDepartmentName());
            pstmt.setString(2, department.getLocation());
            pstmt.setInt(3, department.getDepartmentId());
            pstmt.executeUpdate();
        }
    }

    public void deleteDepartment(int departmentId) throws SQLException {
        String sql = "DELETE FROM Departments WHERE department_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, departmentId);
            pstmt.executeUpdate();
        }
    }

    public Department getDepartmentById(int departmentId) throws SQLException {
        String sql = "SELECT department_id, department_name, location FROM Departments WHERE department_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, departmentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Department(
                            rs.getInt("department_id"),
                            rs.getString("department_name"),
                            rs.getString("location")
                    );
                }
            }
        }
        return null;
    }
}