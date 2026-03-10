/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.sql.*;
/**
 *
 * @author ADMIN
 */
public class DBConnection {
    
    private static Connection con = null;    // Encapsulation is data hiding
    
    private static final String URL = "jdbc:mysql://localhost:3306/inventory_db";
    private static final String USER = "root";
    private static final String PASS = "1234"; 

    // Get Connection safely
    public static Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {  // Reconnect if closed
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("Database connected!");
            }
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
        return con;
    }

    // Optional method to close connection manually
    public static void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
