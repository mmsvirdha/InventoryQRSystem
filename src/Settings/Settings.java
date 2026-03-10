/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Settings;

import java.io.*;
import java.util.Properties;
import java.io.IOException;
import javax.swing.JOptionPane;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


/**
 *
 * @author ADMIN
 */
public class Settings {

    private Connection conn;
    private static Settings instance;

    // Application settings fields
    private String companyName;
    private String businessType;
    private String businessAddress;
    private String phoneNumber;
    private String emailAddress;
    private String website;
    private String currency;
    private String language;

    // Singleton pattern
    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    // Private constructor
    private Settings() {
        conn = DBConnection.getConnection(); // initialize DB connection
        load(); // load settings from DB
    }

    // Load settings from database
    private void load() {
        String sql = "SELECT * FROM application_settings WHERE setting_id = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                companyName = rs.getString("company_name");
                businessType = rs.getString("business_type");
                businessAddress = rs.getString("business_address");
                phoneNumber = rs.getString("phone_number");
                emailAddress = rs.getString("email_address");
                website = rs.getString("website");
                currency = rs.getString("currency");
                language = rs.getString("language");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading application settings: " + e.getMessage());
            e.printStackTrace();
        }
        
        
    }

    // **New method to fetch all company names**
    public List<String> getAllCompanyNames() {
        List<String> companies = new ArrayList<>();
        String sql = "SELECT company_name FROM settings";

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                companies.add(rs.getString("company_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return companies;
    }

    // Getters and Setters
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessType() {
        return businessType;
    }
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }
    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
}