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
    * Process payroll for specific employees (Gross Salary, Deductions, Net Salary calculation).
    * View, update, and delete payroll records, linked to the respective employee.
    * Table allows sorting by columns.
    * User-friendly date selection for "Payroll Date" using `LGoodDatePicker`.

* **MySQL Integration:**
    * All data is stored persistently in a MySQL relational database.

* **Modern UI:**
    * Utilizes `FlatLaf` for a clean, modern, and visually appealing Look and Feel (UI theme).
    * Consistent header styling and button colors across modules.
    * Improved form layouts using `GridBagLayout` for better alignment and spacing.

## 🗄️ Project Structure
```plaintext
EmployeeManagementSystem/
├── lib/                                  # External JAR libraries
│   ├── mysql-connector-j-X.X.X.jar       # MySQL JDBC Driver
│   ├── LGoodDatePicker-X.X.X.jar         # Date Picker Library
│   └── flatlaf-X.X.jar                   # Modern Look and Feel Library
├── src/
│   └── com/
│       └── employeemanagement/
│           ├── database/
│           │   └── DatabaseManager.java          # Manages database connections
│           ├── model/                            # POJOs (Plain Old Java Objects) for database entities
│           │   ├── Department.java
│           │   ├── Employee.java
│           │   └── Payroll.java
│           ├── dao/                              # Data Access Objects for CRUD operations
│           │   ├── DepartmentDAO.java
│           │   ├── EmployeeDAO.java
│           │   └── PayrollDAO.java
│           └── gui/                              # Java Swing UI Panels and main application entry point
│               ├── MainDashboard.java            # Main application window with tabbed navigation
│               ├── DepartmentPanel.java          # UI panel for Department management
│               ├── EmployeePanel.java            # UI panel for Employee management
│               └── PayrollPanel.java             # UI panel for Payroll management
```

## ⚙️ Technologies Used

* **Language:** Java
* **Database:** MySQL
* **Connectivity:** JDBC (Java Database Connectivity)
* **GUI Framework:** Java Swing
* **IDE:** IntelliJ IDEA (recommended for development)
* **Libraries:**
    * MySQL Connector/J
    * LGoodDatePicker
    * FlatLaf

## ▶️ Getting Started

### 1. Prerequisites

* **Java Development Kit (JDK) 11 or higher** (JDK 21 used during development)
* **MySQL Server** (Installed and running)
* **Integrated Development Environment (IDE):** IntelliJ IDEA, Eclipse, or NetBeans

### 2. Database Setup

a.  **Install MySQL Server:**
    Ensure you have MySQL Community Server installed and running on your system. You can also use packages like XAMPP, WAMP, or MAMP if preferred.

b.  **Create the Database and User:**
    Open your MySQL client (e.g., MySQL Workbench, DBeaver, or command-line client) and execute the following SQL commands. **Remember to replace `'your_secure_password'` with a strong, unique password** for your application's database user.

    ```sql
    CREATE DATABASE EmployeeManagementDB;

    CREATE USER 'ems_user'@'localhost' IDENTIFIED BY 'your_secure_password';

    GRANT ALL PRIVILEGES ON EmployeeManagementDB.* TO 'ems_user'@'localhost';

    FLUSH PRIVILEGES;
    ```

c.  **Create Tables:**
    Switch to the newly created database and then execute the table creation SQL statements provided below.

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

1.  **Clone or Download the project** repository to your local machine.

2.  **Open in IntelliJ IDEA:**
    * Launch IntelliJ IDEA.
    * Select `Open` or `Import Project` from the welcome screen or `File` menu.
    * Navigate to the root directory of the cloned/downloaded project and select it to open.

3.  **Download External JAR Libraries:**
    * **MySQL Connector/J:** Download the "Platform Independent (ZIP Archive)" version (e.g., `mysql-connector-j-X.X.X.jar`) from the official [MySQL Downloads page](https://dev.mysql.com/downloads/connector/j/).
    * **LGoodDatePicker:** Download the latest `LGoodDatePicker-X.X.X.jar` from its [GitHub Releases page](https://github.com/LGoodDatePicker/LGoodDatePicker/releases).
    * **FlatLaf:** Download the latest `flatlaf-X.X.jar` from its [GitHub Releases page](https://github.com/JFormDesigner/FlatLaf/releases).

4.  **Add JARs to `lib/` directory:**
    * In your project's root directory (e.g., `EmployeeManagementSystem/`), create a new folder named `lib` if it doesn't already exist.
    * Copy all three downloaded `.jar` files (`mysql-connector-j-X.X.X.jar`, `LGoodDatePicker-X.X.X.jar`, `flatlaf-X.X.jar`) into this `lib/` directory.

5.  **Add JARs to Project Library/Module Dependencies in IntelliJ:**
    * In IntelliJ IDEA, right-click on your project in the Project Explorer (typically on the project root folder).
    * Select `Open Module Settings` (or use the shortcut `F4`).
    * In the "Project Structure" dialog, navigate to the `Libraries` section under `Project Settings`.
    * Click the `+` button, then select `Java`.
    * Browse to your project's `lib/` directory, select all three `.jar` files, and click `OK`.
    * Ensure that these libraries are correctly attached to your project's module. Click `Apply` then `OK` to close the dialog.

6.  **Configure Database Credentials:**
    * Open the file `src/com/employeemanagement/database/DatabaseManager.java`.
    * Update the `USER` and `PASSWORD` constants within this file to match the MySQL user's credentials you created in **Step 2b**.

    ```java
    // ...
    private static final String USER = "ems_user"; // Your MySQL username
    private static final String PASSWORD = "your_secure_password"; // Your MySQL password
    // ...
    ```

7.  **Build the Project:**
    * In IntelliJ IDEA, go to the `Build` menu and select `Rebuild Project`. This ensures all changes and new libraries are properly compiled.

### 4. Run the Application

* Navigate to the `src/com/employeemanagement/gui/MainDashboard.java` file in your IDE's Project Explorer.
* Right-click on `MainDashboard.java` and select `Run 'MainDashboard.main()'`.

The Employee Management System application window should now launch, displaying the modern UI and ready for use.

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
