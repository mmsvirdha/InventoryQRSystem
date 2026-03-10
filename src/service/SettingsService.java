/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import utils.DBConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class SettingsService {
    
    private Connection conn;
    
     public SettingsService(Connection conn) {
        this.conn = conn;
    }

 // Get all company names
    public List getAllCompanyNames() {
        List companies = new ArrayList();
        String sql = "SELECT DISTINCT company_name FROM application_settings";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                companies.add(rs.getString("company_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return companies;
    }

    // ✅ NEW: Check if company exists
    public boolean companyExists(String companyName) {
        String sql = "SELECT COUNT(*) FROM application_settings WHERE company_name = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, companyName);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    public Map<String, String> loadSettingsByCompany(String companyName) {
        Map<String, String> data = new HashMap<>();
        
        String sql = "SELECT * FROM application_settings WHERE company_name = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, companyName);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                data.put("company_name", rs.getString("company_name"));
                data.put("business_type", rs.getString("business_type"));
                data.put("business_address", rs.getString("business_address"));
                data.put("phone_number", rs.getString("phone_number"));
                data.put("email_address", rs.getString("email_address"));
                data.put("website", rs.getString("website"));
                data.put("tax_id", rs.getString("tax_id"));
                data.put("currency", rs.getString("currency"));
                data.put("time_zone", rs.getString("time_zone"));
                data.put("date_format", rs.getString("date_format"));
                data.put("time_format", rs.getString("time_format"));
                data.put("language", rs.getString("language"));
                data.put("number_format", rs.getString("number_format"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error loading company data: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return data;
    }

    // ✅ NEW: Insert new company
    public boolean insertSettings(Map<String, String> data) {
        String sql = """
                INSERT INTO application_settings 
                (company_name, business_type, business_address, phone_number, 
                email_address, website, tax_id, currency, time_zone, 
                date_format, time_format, language, number_format)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, data.get("company_name"));
            ps.setString(2, data.get("business_type"));
            ps.setString(3, data.get("business_address"));
            ps.setString(4, data.get("phone_number"));
            ps.setString(5, data.get("email_address"));
            ps.setString(6, data.get("website"));
            ps.setString(7, data.get("tax_id"));
            ps.setString(8, data.get("currency"));
            ps.setString(9, data.get("time_zone"));
            ps.setString(10, data.get("date_format"));
            ps.setString(11, data.get("time_format"));
            ps.setString(12, data.get("language"));
            ps.setString(13, data.get("number_format"));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // ✅ UPDATED: Save/Update settings (now handles both insert and update)
    public boolean saveSettings(Map<String, String> data) {
        String companyName = data.get("company_name");
        
        // Check if company exists
        if (companyExists(companyName)) {
            // Update existing company
            return updateSettings(data);
        } else {
            // Insert new company
            return insertSettings(data);
        }
    }

    // ✅ RENAMED: Update existing settings
    private boolean updateSettings(Map<String, String> data) {
        String sql = """
                UPDATE application_settings SET
                business_type = ?,
                business_address = ?,
                phone_number = ?,
                email_address = ?,
                website = ?,
                tax_id = ?,
                currency = ?,
                time_zone = ?,
                date_format = ?,
                time_format = ?,
                language = ?,
                number_format = ?
                WHERE company_name = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, data.get("business_type"));
            ps.setString(2, data.get("business_address"));
            ps.setString(3, data.get("phone_number"));
            ps.setString(4, data.get("email_address"));
            ps.setString(5, data.get("website"));
            ps.setString(6, data.get("tax_id"));
            ps.setString(7, data.get("currency"));
            ps.setString(8, data.get("time_zone"));
            ps.setString(9, data.get("date_format"));
            ps.setString(10, data.get("time_format"));
            ps.setString(11, data.get("language"));
            ps.setString(12, data.get("number_format"));
            ps.setString(13, data.get("company_name")); // WHERE clause

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
