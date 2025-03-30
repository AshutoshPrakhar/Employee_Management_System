package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Employee_Management {
    private static final String url = "jdbc:mysql://localhost:3306/Management_System";
    private static final String username = "root";
    private static final String password = "AshutoshPrakhar@123#A";
    private static Connection connection;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            connection = DriverManager.getConnection(url, username, password);
            createEmployeeTable(); // Ensure table exists

            while (true) {
                System.out.println("\nEmployee Management System");
                System.out.println("1. Add Employee");
                System.out.println("2. Fetch Employee Details");
                System.out.println("3. Update Employee Details");
                System.out.println("4. Delete Employee");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int choice = sc.nextInt();
                sc.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        insertEmployeeFromUserInput();
                        break;
                    case 2:
                        fetchEmployeeDetail();
                        break;
                    case 3:
                        updateDetails();
                        break;
                    case 4:
                        deleteEmployee();
                        break;
                    case 5:
                        System.out.println("Exiting Program...");
                        System.exit(0);
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertEmployeeFromUserInput() throws SQLException {
        System.out.print("Enter Employee Name: ");
        String empName = sc.nextLine();
        System.out.print("Enter Employee Age: ");
        int empAge = sc.nextInt();
        sc.nextLine(); // Consume newline
        System.out.print("Enter Employee Email: ");
        String empEmail = sc.nextLine();
        System.out.println("Enter the Designation: ");
        String empDesignation = sc.nextLine();

        insertDataInDB(empName, empAge, empEmail, empDesignation);
    }

    private static void insertDataInDB(String empName, int empAge, String empEmail, String empDesignation) throws SQLException {
        String insertSQL = "INSERT INTO Employees(Emp_name, Emp_age, Emp_email, Emp_designation) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setString(1, empName);
            ps.setInt(2, empAge);
            ps.setString(3, empEmail);
            ps.setString(4, empDesignation);
            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Employee added successfully: " + empName);
            }
        }
    }

    private static void deleteEmployee() throws SQLException {
        System.out.print("Enter the Employee ID for deletion: ");
        int empId = sc.nextInt();
        sc.nextLine();

        System.out.println("Are you sure you want to delete the employee with ID: " + empId + " ? ");
        System.out.println("1. YES \n2. NO");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice != 1) {
            System.out.println("Deletion cancelled.");
            return;
        }

        String deleteQuery = "DELETE FROM employees WHERE Emp_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(deleteQuery)) {
            ps.setInt(1, empId);
            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Employee deleted successfully!" : "No employee found with ID: " + empId);
        }
    }

    private static void updateDetails() throws SQLException {
        System.out.print("Enter the Employee ID to update details: ");
        int empId = sc.nextInt();
        sc.nextLine();

        System.out.println("Select what you want to update:");
        System.out.println("1. Employee Name \n2. Employee Age \n3. Employee Email \n4. Employee Designation");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine();

        String updateQuery = "";
        switch (choice) {
            case 1:
                System.out.print("Enter New Name: ");
                String newName = sc.nextLine();
                updateQuery = "UPDATE employees SET Emp_name = ? WHERE Emp_id = ?";
                try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                    ps.setString(1, newName);
                    ps.setInt(2, empId);
                    int rowsAffected = ps.executeUpdate();
                    System.out.println(rowsAffected > 0 ? "Name updated successfully!" : "No employee found with ID: " + empId);
                }
                break;
            case 2:
                System.out.print("Enter New Age: ");
                int newAge = sc.nextInt();
                sc.nextLine();
                updateQuery = "UPDATE employees SET Emp_age = ? WHERE Emp_id = ?";
                try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                    ps.setInt(1, newAge);
                    ps.setInt(2, empId);
                    int rowsAffected = ps.executeUpdate();
                    System.out.println(rowsAffected > 0 ? "Age updated successfully!" : "No employee found with ID: " + empId);
                }
                break;
            case 3:
                System.out.print("Enter New Email: ");
                String newEmail = sc.next();
                updateQuery = "UPDATE employees SET Emp_email = ? WHERE Emp_id = ?";
                try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                    ps.setString(1, newEmail);
                    ps.setInt(2, empId);
                    int rowsAffected = ps.executeUpdate();
                    System.out.println(rowsAffected > 0 ? "Email updated successfully!" : "No employee found with ID: " + empId);
                }
                break;
            case 4:
                System.out.print("Enter New Designation: ");
                String newDesignation = sc.nextLine();
                updateQuery = "UPDATE employees SET Emp_designation = ? WHERE Emp_id = ?";
                try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
                    ps.setString(1, newDesignation);
                    ps.setInt(2, empId);
                    int rowsAffected = ps.executeUpdate();
                    System.out.println(rowsAffected > 0 ? "Designation updated successfully!" : "No employee found with ID: " + empId);
                }
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    private static void fetchEmployeeDetail() throws SQLException {
        System.out.print("Enter the Employee ID to fetch details: ");
        int id = sc.nextInt();
        sc.nextLine();

        String fetchQuery = "SELECT * FROM employees WHERE Emp_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(fetchQuery)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("****** Employee Details *******");
                    System.out.println("Employee ID: " + rs.getInt("Emp_id"));
                    System.out.println("Name: " + rs.getString("Emp_name"));
                    System.out.println("Age: " + rs.getInt("Emp_age"));
                    System.out.println("Email: " + rs.getString("Emp_email"));
                    System.out.println("Designation: "+ rs.getString("Emp_designation"));
                } else {
                    System.out.println("No employee found with ID: " + id);
                }
            }
        }
    }

    private static void createEmployeeTable() throws SQLException {
        String createTable = "CREATE TABLE IF NOT EXISTS Employees ("
                + "Emp_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "Emp_name VARCHAR(30), "
                + "Emp_age INT, "
                + "Emp_email VARCHAR(30), "
                + "Emp_designation VARCHAR(30)"
                + ") AUTO_INCREMENT = 1001;";

        try (PreparedStatement ps = connection.prepareStatement(createTable)) {
            ps.execute();
            System.out.println("Table created...");
        }
    }
}
