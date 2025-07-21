package com.employeemanagement.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Payroll {
    private int payrollId;
    private int employeeId;
    private LocalDate payrollDate;
    private BigDecimal grossSalary;
    private BigDecimal deductions;
    private BigDecimal netSalary;

    // Optional: to hold employee name for display in GUI
    private String employeeFullName;

    // Constructor for adding new payroll records (without ID)
    public Payroll(int employeeId, LocalDate payrollDate, BigDecimal grossSalary, BigDecimal deductions, BigDecimal netSalary) {
        this.employeeId = employeeId;
        this.payrollDate = payrollDate;
        this.grossSalary = grossSalary;
        this.deductions = deductions;
        this.netSalary = netSalary;
    }

    // Constructor for retrieving payroll records (with ID)
    public Payroll(int payrollId, int employeeId, LocalDate payrollDate, BigDecimal grossSalary, BigDecimal deductions, BigDecimal netSalary) {
        this.payrollId = payrollId;
        this.employeeId = employeeId;
        this.payrollDate = payrollDate;
        this.grossSalary = grossSalary;
        this.deductions = deductions;
        this.netSalary = netSalary;
    }

    // Constructor for retrieving payroll records with employee name (for display)
    public Payroll(int payrollId, int employeeId, String employeeFullName, LocalDate payrollDate, BigDecimal grossSalary, BigDecimal deductions, BigDecimal netSalary) {
        this(payrollId, employeeId, payrollDate, grossSalary, deductions, netSalary);
        this.employeeFullName = employeeFullName;
    }

    // Getters and Setters
    public int getPayrollId() { return payrollId; }
    public void setPayrollId(int payrollId) { this.payrollId = payrollId; }
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public LocalDate getPayrollDate() { return payrollDate; }
    public void setPayrollDate(LocalDate payrollDate) { this.payrollDate = payrollDate; }
    public BigDecimal getGrossSalary() { return grossSalary; }
    public void setGrossSalary(BigDecimal grossSalary) { this.grossSalary = grossSalary; }
    public BigDecimal getDeductions() { return deductions; }
    public void setDeductions(BigDecimal deductions) { this.deductions = deductions; }
    public BigDecimal getNetSalary() { return netSalary; }
    public void setNetSalary(BigDecimal netSalary) { this.netSalary = netSalary; }
    public String getEmployeeFullName() { return employeeFullName; }
    public void setEmployeeFullName(String employeeFullName) { this.employeeFullName = employeeFullName; }

    @Override
    public String toString() {
        return "Payroll [ID=" + payrollId + ", Employee=" + (employeeFullName != null ? employeeFullName : employeeId) + ", Date=" + payrollDate + ", Net=" + netSalary + "]";
    }
}