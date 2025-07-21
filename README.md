# Employee Management System

This is a desktop application developed using Java Swing, designed to streamline the management of employee, department, and payroll data within an organization. It provides an intuitive graphical user interface (GUI) for performing essential CRUD (Create, Read, Update, Delete) operations on these core entities.

## ✨ Features

* **Employee Management:**
    * Add new employees with detailed personal information (First Name, Last Name, Email, Phone Number, Hire Date, Job Title, Salary).
    * Assign employees to existing departments.
    * View a list of all registered employees, including their department name.
    * Update existing employee details.
    * Delete employee records.
    * Table allows sorting by columns.
    * User-friendly date selection for "Hire Date" using `LGoodDatePicker`.

* **Department Management:**
    * Add new departments with names and locations.
    * View, update, and delete department records.
    * Table allows sorting by columns.

* **Payroll Management:**
    * Process payroll records for specific employees, including gross salary, deductions, and calculated net salary.
    * Edit existing payroll entries.
    * View a list of all payroll records, linked to employee names.
    * Delete payroll entries.
    * Table allows sorting by columns.
    * User-friendly date selection for "Payroll Date" using `LGoodDatePicker`.

* **MySQL Integration:**
    * All data is stored persistently in a MySQL relational database.

* **Modern UI:**
    * Utilizes `FlatLaf` for a clean, modern, and visually appealing Look and Feel (UI theme).
    * Consistent header styling and button colors across modules.
    * Improved form layouts using `GridBagLayout` for better alignment and spacing.

## 🗄️ Project Structure
Okay, here is the complete content of the README.md file, formatted as a string that you can directly copy and paste into a new README.md file in your GitHub repository.

Markdown

# Employee Management System

This is a desktop application developed using Java Swing, designed to streamline the management of employee, department, and payroll data within an organization. It provides an intuitive graphical user interface (GUI) for performing essential CRUD (Create, Read, Update, Delete) operations on these core entities.

## ✨ Features

* **Employee Management:**
    * Add new employees with detailed personal information (First Name, Last Name, Email, Phone Number, Hire Date, Job Title, Salary).
    * Assign employees to existing departments.
    * View a list of all registered employees, including their department name.
    * Update existing employee details.
    * Delete employee records.
    * Table allows sorting by columns.
    * User-friendly date selection for "Hire Date" using `LGoodDatePicker`.

* **Department Management:**
    * Add new departments with names and locations.
    * View, update, and delete department records.
    * Table allows sorting by columns.

* **Payroll Management:**
    * Process payroll records for specific employees, including gross salary, deductions, and calculated net salary.
    * Edit existing payroll entries.
    * View a list of all payroll records, linked to employee names.
    * Delete payroll entries.
    * Table allows sorting by columns.
    * User-friendly date selection for "Payroll Date" using `LGoodDatePicker`.

* **MySQL Integration:**
    * All data is stored persistently in a MySQL relational database.

* **Modern UI:**
    * Utilizes `FlatLaf` for a clean, modern, and visually appealing Look and Feel (UI theme).
    * Consistent header styling and button colors across modules.
    * Improved form layouts using `GridBagLayout` for better alignment and spacing.

## 🗄️ Project Structure

EmployeeManagementSystem/
├── lib/
│   ├── mysql-connector-j-X.X.X.jar
│   ├── LGoodDatePicker-X.X.X.jar
│   └── flatlaf-X.X.jar
└── src/
└── com/
└── employeemanagement/
├── database/
│   └── DatabaseManager.java          // Manages database connections
│
├── model/                          // POJOs for DB entities
│   ├── Department.java
│   ├── Employee.java
│   └── Payroll.java
│
├── dao/                            // DAO for CRUD operations
│   ├── DepartmentDAO.java
│   ├── EmployeeDAO.java
│   └── PayrollDAO.java
│
└── gui/                            // Swing UI Panels and App
├── MainDashboard.java          // Main application window
├── DepartmentPanel.java        // UI for Department management
├── EmployeePanel.java          // UI for Employee management
└── PayrollPanel.java           // UI for Payroll management

## ⚙️ Technologies Used

* **Backend:** Java
* **Database:** MySQL
* **Connectivity:** JDBC (Java Database Connectivity)
* **Frontend:** Java Swing (GUI)
* **IDE:** IntelliJ IDEA (recommended)
* **Libraries:**
    * MySQL Connector/J
    * LGoodDatePicker
    * FlatLaf

## ▶️ Getting Started

### 1. Prerequisites

* **Java JDK 11 or higher** (JDK 21 used during development)
* **MySQL Server**
* **IntelliJ IDEA** (or any Java IDE like Eclipse, NetBeans)

### 2. Database Setup

a.  **Install MySQL Server:**
    Use MySQL Community Server or packages like XAMPP/WAMP/MAMP. Ensure your MySQL server is running.

b.  **Create the Database:**
    Open your MySQL client (e.g., MySQL Workbench, command line) and execute the following commands to create the database and a dedicated user for the application. Remember to replace `'your_secure_password'` with a strong password.

    ```sql
    CREATE DATABASE EmployeeManagementDB;

    CREATE USER 'ems_user'@'localhost' IDENTIFIED BY 'your_secure_password';

    GRANT ALL PRIVILEGES ON EmployeeManagementDB.* TO 'ems_user'@'localhost';

    FLUSH PRIVILEGES;
    ```

c.  **Create Tables:**
    Switch to the newly created database and execute the table creation SQL statements for `Departments`, `Employees`, and `Payroll`.

    ```sql
    USE EmployeeManagementDB;

    CREATE TABLE Departments (
        department_id INT PRIMARY KEY AUTO_INCREMENT,
        department_name VARCHAR(100) NOT NULL UNIQUE,
        location VARCHAR(100)
    );

    CREATE TABLE Employees (
        employee_id INT PRIMARY KEY AUTO_INCREMENT,
        first_name VARCHAR(50) NOT NULL,
        last_name VARCHAR(50) NOT NULL,
        email VARCHAR(100) NOT NULL UNIQUE,
        phone_number VARCHAR(20),
        hire_date DATE NOT NULL,
        job_title VARCHAR(50) NOT NULL,
        salary DECIMAL(10, 2) NOT NULL CHECK (salary >= 0),
        department_id INT,
        FOREIGN KEY (department_id) REFERENCES Departments(department_id)
            ON DELETE SET NULL ON UPDATE CASCADE
    );

    CREATE TABLE Payroll (
        payroll_id INT PRIMARY KEY AUTO_INCREMENT,
        employee_id INT NOT NULL,
        payroll_date DATE NOT NULL,
        gross_salary DECIMAL(10, 2) NOT NULL,
        deductions DECIMAL(10, 2) DEFAULT 0.00,
        net_salary DECIMAL(10, 2) NOT NULL,
        FOREIGN KEY (employee_id) REFERENCES Employees(employee_id)
            ON DELETE CASCADE ON UPDATE CASCADE
    );
    ```

### 3. Project Setup (IDE - IntelliJ IDEA Example)

1.  **Clone or Download the project.**

2.  **Open in IntelliJ IDEA:**
    * Open IntelliJ IDEA and select `Open` or `Import Project`.
    * Navigate to the project directory and open it.

3.  **Download External JARs:**
    * **MySQL Connector/J:** Download `mysql-connector-j-X.X.X.jar` from [MySQL Downloads](https://dev.mysql.com/downloads/connector/j/).
    * **LGoodDatePicker:** Download `LGoodDatePicker-X.X.X.jar` from [LGoodDatePicker GitHub Releases](https://github.com/LGoodDatePicker/LGoodDatePicker/releases).
    * **FlatLaf:** Download `flatlaf-X.X.jar` from [FlatLaf GitHub Releases](https://github.com/JFormDesigner/FlatLaf/releases).

4.  **Add JARs to `lib/` directory:**
    * Manually create a folder named `lib` in the root of your project (if it doesn't exist).
    * Place all three downloaded `.jar` files into this `lib/` directory.

5.  **Add JARs to Project Library/Module Dependencies in IntelliJ:**
    * Right-click on your project in the Project Explorer.
    * Select `Open Module Settings` (or press `F4`).
    * Go to `Libraries`.
    * Click the `+` button, select `Java`.
    * Navigate to your project's `lib/` directory and select all three `.jar` files. Click `OK`.
    * Ensure they are attached to the correct module. Click `Apply` then `OK`.

6.  **Configure Database Credentials:**
    Edit `src/com/employeemanagement/database/DatabaseManager.java` and update the `USER` and `PASSWORD` to match your MySQL user's credentials created in step 2b.

    ```java
    // ...
    private static final String USER = "ems_user"; // Your MySQL username
    private static final String PASSWORD = "your_secure_password"; // Your MySQL password
    // ...
    ```

7.  **Build the Project:**
    In IntelliJ: `Build` > `Rebuild Project`.

### 4. Run the App

* Open `src/com/employeemanagement/gui/MainDashboard.java`.
* Right-click on the file and select `Run 'MainDashboard.main()'`.

---

## 📈 Future Enhancements

* **Improved UI/UX:**
    * More sophisticated data validation (e.g., email format, phone number patterns).
    * Advanced filtering and search options for tables.
    * Export data to CSV/Excel.
* **User Authentication:** Implement a login system with different user roles (e.g., Admin, HR).
* **Reporting:** Generate printable reports (e.g., employee list, payroll summaries).
* **Employee Performance Tracking:** Add modules for performance reviews, goals, etc.
* **Attendance Tracking:** Implement features for managing employee attendance.
* **Deployment:** Package the application as a runnable JAR or installer for easier distribution.
* **MVC Pattern:** Refactor the codebase to strictly adhere to the Model-View-Controller pattern for better modularity and scalability.
