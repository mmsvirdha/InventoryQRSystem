/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Settings;

import java.sql.SQLException;

import QrScanner.QRCodeScanner;
import dashboard.Dashboard;
import inventory.StockInOutForm;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import login.LoginForm;
import products.ProductForm;
import reports.ReportForm;
import users.userForm;
import javax.swing.JLabel;
import java.sql.Connection;
import utils.DBConnection;
import service.SettingsService;

import java.io.*;
import java.util.Properties;
import java.sql.SQLException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.awt.event.ItemEvent;

import java.util.Map;
import java.util.HashMap;
import java.util.List; // Make sure this is java.util.List

import javax.swing.*;
import java.awt.*;
import utils.TransitionHelper;

/**
 *
 * @author ADMIN
 */
public class SettingsForm extends javax.swing.JFrame {
    
    private boolean isResetting = false;

    
      private String username;
    private String role;
    
Settings currentSettings = Settings.getInstance();


 private SettingsService settingsService;
    private Map<String, String> originalData = new HashMap<>();
 private static Settings instance;
 


private Connection conn;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SettingsForm.class.getName());

    /**
     * Creates new form SettingsForm
     */
    public SettingsForm() {
        initComponents();
        
// Remove Black Rectangle Completely
CompanyInfoPanel.setOpaque(false);
CompanyInfoPanel.setBorder(null);

RegionalPanel.setOpaque(false);
RegionalPanel.setBorder(null);

btnsPanel.setOpaque(false);
btnsPanel.setBorder(null);

        
        // Maximizes the window to full screen size
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        cmbCompanyName.setEditable(true); 

         settingsService = new SettingsService(DBConnection.getConnection());
         loadCompanyNames();
          setupListeners();
          activateAllLinks();
 
          // Assuming your buttons are named jButtonSave (for Save Changes) and jButtonReset (for Reset)
     btnSaveChanges.addActionListener(new java.awt.event.ActionListener() {
       public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSaveChangesActionPerformed(evt);
      }
      });

      btnReset.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnResetActionPerformed(evt);
      }
     });
      
      

}
    
     public SettingsForm (String username, String role) {  // Accept parameters
         
          this.username = username;
    this.role = role;
    
    initComponents();

    cmbCompanyName.setEditable(true);
    
      settingsService = new SettingsService(DBConnection.getConnection());
         loadCompanyNames();
          setupListeners();
          activateAllLinks();
    
        // Maximizes the window to full screen size
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    
   
 lblUsername.setText(capitalizeFirstLetter(username));
    lblRole.setText(formatRole(role));

    
     activateAllLinks();
     
              
                
TransitionHelper.applyTealHoverSmooth(lblQRScan);
TransitionHelper.applyTealHoverSmooth(lblReports);
TransitionHelper.applyTealHoverSmooth(lblSettings);
TransitionHelper.applyTealHoverSmooth(lblUsers);
TransitionHelper.applyTealHoverSmooth(lblDash);
TransitionHelper.applyTealHoverSmooth(lblInventory);
TransitionHelper.applyTealHoverSmooth(lblLogout);
          
}
    
     

 private void activateAllLinks() {
    makeHoverLink(lblDash, this, "Dashboard");
    makeHoverLink(lblInventory, this, "ProductForm");  // Even itself should use String
    makeHoverLink(lblQRScan, this, "QRCodeScanner");
    makeHoverLink(lblReports, this, "ReportForm");
    makeHoverLink(lblSettings, this, "SettingsForm");
    makeHoverLink(lblUsers, this, "userForm");

    // Logout with confirmation
    lblLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
    lblLogout.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int confirm = JOptionPane.showConfirmDialog(
                SettingsForm.this,
                "Do you really want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                TransitionHelper.fadeToFrame(SettingsForm.this, new LoginForm(), 500);
            }
        }
        @Override
        public void mouseEntered(MouseEvent e) {
            lblLogout.setForeground(Color.BLUE);
        }
        @Override
        public void mouseExited(MouseEvent e) {
            lblLogout.setForeground(Color.WHITE);
        }
    });
}
    
    
  private void makeHoverLink(JLabel label, JFrame currentFrame, String targetFormClass) {
    label.setCursor(new Cursor(Cursor.HAND_CURSOR));

    label.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            label.setForeground(Color.BLUE);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            label.setForeground(Color.WHITE);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            JFrame nextFrame = null;
            
            // Create frame only when clicked!
            switch (targetFormClass) {
                case "Dashboard":
                    nextFrame = new Dashboard(username, role);
                    break;
                case "ProductForm":
                    nextFrame = new ProductForm(username, role);
                    break;
                case "QRCodeScanner":
                    nextFrame = new QRCodeScanner(username, role);
                    break;
                case "ReportForm":
                    nextFrame = new ReportForm(username, role);
                    break;
                case "SettingsForm":
                    nextFrame = new SettingsForm(username, role);
                    break;
                case "userForm":
                    nextFrame = new userForm(username, role);
                    break;
                case "LoginForm":
                    nextFrame = new LoginForm();
                    break;
            }
            
            if (nextFrame != null) {
                TransitionHelper.fadeToFrame(currentFrame, nextFrame, 200);
            }
        }
    });
}

   private String formatRole(String role) {
    switch (role.toLowerCase()) {
        case "admin":
            return "Administrator";
        case "staff":
            return "Staff Member";
        case "viewer":
            return "Viewer";
        default:
            return role; // fallback
    }
}

private String capitalizeFirstLetter(String text) {
    if (text == null || text.isEmpty()) {
        return text;
    }
    return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
}
    
    
    
    private void setupListeners() {
        // Trigger when selection changes or user types + presses Enter
        cmbCompanyName.addActionListener(e -> {
            Object selected = cmbCompanyName.getSelectedItem();
            if (selected != null) {
                loadCompanyData(selected.toString());
            }
        });
    }

    
    private void loadCompanyNames() {
    cmbCompanyName.removeAllItems();

    // Use raw List instead of List<String>
    List companies = settingsService.getAllCompanyNames();

    if (companies.isEmpty()) {
        System.out.println("No companies found!");
    } else {
        // Iterate using Object and cast to String
        for (Object companyObj : companies) {
            String company = (String) companyObj;
            cmbCompanyName.addItem(company);
        }
    }
}


    
private void loadCompanyData(String companyName) {
        Map<String, String> data = settingsService.loadSettingsByCompany(companyName);

        if (data != null && !data.isEmpty()) {
            cmbBusinessType.setSelectedItem(data.get("business_type"));
            txtBusinessAddress.setText(data.get("business_address"));
            txtPhone.setText(data.get("phone_number"));
            txtEmail.setText(data.get("email_address"));
            txtWebsite.setText(data.get("website"));
            txtTaxID.setText(data.get("tax_id"));
            cmbCurrency.setSelectedItem(data.get("currency"));
            cmbTimeZone.setSelectedItem(data.get("time_zone"));
            cmbDateFormat.setSelectedItem(data.get("date_format"));
            cmbTimeFormat.setSelectedItem(data.get("time_format"));
            cmbLanguage.setSelectedItem(data.get("language"));
            cmbNumberFormat.setSelectedItem(data.get("number_format"));

            originalData.clear();
            originalData.putAll(data);
        } else {
            clearFields();
        }
    }

    private void clearFields() {
        cmbBusinessType.setSelectedIndex(-1);
        txtBusinessAddress.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtWebsite.setText("");
        txtTaxID.setText("");
        cmbCurrency.setSelectedIndex(-1);
        cmbTimeZone.setSelectedIndex(-1);
        cmbDateFormat.setSelectedIndex(-1);
        cmbTimeFormat.setSelectedIndex(-1);
        cmbLanguage.setSelectedIndex(-1);
        cmbNumberFormat.setSelectedIndex(-1);
    }

private boolean isDataChanged() {
    // Add null checks
    Object companyItem = cmbCompanyName.getSelectedItem();
    Object businessTypeItem = cmbBusinessType.getSelectedItem();
    Object currencyItem = cmbCurrency.getSelectedItem();
    Object timeZoneItem = cmbTimeZone.getSelectedItem();
    Object dateFormatItem = cmbDateFormat.getSelectedItem();
    Object timeFormatItem = cmbTimeFormat.getSelectedItem();
    Object languageItem = cmbLanguage.getSelectedItem();
    Object numberFormatItem = cmbNumberFormat.getSelectedItem();
    
    if (companyItem != null && !companyItem.toString().equals(originalData.get("company_name"))) return true;
    if (businessTypeItem != null && !businessTypeItem.toString().equals(originalData.get("business_type"))) return true;
    if (!txtBusinessAddress.getText().equals(originalData.get("business_address"))) return true;
    if (!txtPhone.getText().equals(originalData.get("phone_number"))) return true;
    if (!txtEmail.getText().equals(originalData.get("email_address"))) return true;
    if (!txtWebsite.getText().equals(originalData.get("website"))) return true;
    if (!txtTaxID.getText().equals(originalData.get("tax_id"))) return true;
    if (currencyItem != null && !currencyItem.toString().equals(originalData.get("currency"))) return true;
    if (timeZoneItem != null && !timeZoneItem.toString().equals(originalData.get("time_zone"))) return true;
    if (dateFormatItem != null && !dateFormatItem.toString().equals(originalData.get("date_format"))) return true;
    if (timeFormatItem != null && !timeFormatItem.toString().equals(originalData.get("time_format"))) return true;
    if (languageItem != null && !languageItem.toString().equals(originalData.get("language"))) return true;
    if (numberFormatItem != null && !numberFormatItem.toString().equals(originalData.get("number_format"))) return true;

    return false;
}



    
   private void saveSettings() {
    // Get company name from editable combo box
    Object selectedItem = cmbCompanyName.getEditor().getItem();
    
    if (selectedItem == null || selectedItem.toString().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, 
            "Please enter a company name!", 
            "Validation Error", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    String companyName = selectedItem.toString().trim();
    
    // Check if all combo boxes have values
    if (cmbBusinessType.getSelectedItem() == null ||
        cmbCurrency.getSelectedItem() == null ||
        cmbTimeZone.getSelectedItem() == null ||
        cmbDateFormat.getSelectedItem() == null ||
        cmbTimeFormat.getSelectedItem() == null ||
        cmbLanguage.getSelectedItem() == null ||
        cmbNumberFormat.getSelectedItem() == null) {
        
        JOptionPane.showMessageDialog(this, 
            "Please fill all required fields!", 
            "Validation Error", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Create data map
    Map<String, String> data = new HashMap<>();
    data.put("company_name", companyName);
    data.put("business_type", cmbBusinessType.getSelectedItem().toString());
    data.put("business_address", txtBusinessAddress.getText());
    data.put("phone_number", txtPhone.getText());
    data.put("email_address", txtEmail.getText());
    data.put("website", txtWebsite.getText());
    data.put("tax_id", txtTaxID.getText());
    data.put("currency", cmbCurrency.getSelectedItem().toString());
    data.put("time_zone", cmbTimeZone.getSelectedItem().toString());
    data.put("date_format", cmbDateFormat.getSelectedItem().toString());
    data.put("time_format", cmbTimeFormat.getSelectedItem().toString());
    data.put("language", cmbLanguage.getSelectedItem().toString());
    data.put("number_format", cmbNumberFormat.getSelectedItem().toString());

    // Check if it's a new company
    boolean isNewCompany = !settingsService.companyExists(companyName);
    
    if (isNewCompany) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "'" + companyName + "' is a new company. Do you want to add it?",
            "Add New Company",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
    }

    // Save settings (will auto-detect insert vs update)
    boolean success = settingsService.saveSettings(data);

    if (success) {
        if (isNewCompany) {
            JOptionPane.showMessageDialog(this, 
                "New company '" + companyName + "' added successfully!");
            
            // Refresh company list
            loadCompanyNames();
            cmbCompanyName.setSelectedItem(companyName);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Settings for '" + companyName + "' updated successfully!");
        }
        
        // Reload data to refresh originalData
        loadCompanyData(companyName);
    } else {
        JOptionPane.showMessageDialog(this, 
            "Error saving settings. Please try again.", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}

    private void loadSettingsData() {
    // Get the selected company name from the combo box
    String companyName = (String) cmbCompanyName.getSelectedItem();
    
    if (companyName != null && !companyName.isEmpty()) {
        loadCompanyData(companyName);
    } else {
        JOptionPane.showMessageDialog(this, 
            "Please select a company first.", 
            "Error", 
            JOptionPane.ERROR_MESSAGE
        );
    }
}

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        CompanyInfoPanel = new utils.RoundedPanel(25);
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtBusinessAddress = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtWebsite = new javax.swing.JTextField();
        cmbCompanyName = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cmbBusinessType = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtTaxID = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        RegionalPanel = new utils.RoundedPanel(25);
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        cmbCurrency = new javax.swing.JComboBox<>();
        cmbDateFormat = new javax.swing.JComboBox<>();
        cmbTimeZone = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        cmbLanguage = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        cmbTimeFormat = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        cmbNumberFormat = new javax.swing.JComboBox<>();
        btnsPanel = new utils.RoundedPanel(25);
        btnReset = new javax.swing.JButton();
        btnSaveChanges = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        lblRole = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        lblDash = new javax.swing.JLabel();
        lblInventory = new javax.swing.JLabel();
        lblReports = new javax.swing.JLabel();
        lblQRScan = new javax.swing.JLabel();
        lblUsers = new javax.swing.JLabel();
        lblSettings = new javax.swing.JLabel();
        lblLogout = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(59, 81, 108));

        CompanyInfoPanel.setBackground(new java.awt.Color(80, 108, 133));

        jLabel3.setText("📋 Company Information");

        jLabel4.setText("Company Name");

        jLabel6.setText("Business Address ");

        txtBusinessAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBusinessAddressActionPerformed(evt);
            }
        });

        jLabel9.setText("Phone Number");

        jLabel11.setText("Website");

        txtWebsite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtWebsiteActionPerformed(evt);
            }
        });

        cmbCompanyName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCompanyName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCompanyNameActionPerformed(evt);
            }
        });

        jLabel5.setText("Business Type ");

        cmbBusinessType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Retail", "Wholesale", "Service", "Manufacturing", "Other" }));

        jLabel10.setText("Email Address");

        jLabel12.setText("Tax ID / Registration Number");

        javax.swing.GroupLayout CompanyInfoPanelLayout = new javax.swing.GroupLayout(CompanyInfoPanel);
        CompanyInfoPanel.setLayout(CompanyInfoPanelLayout);
        CompanyInfoPanelLayout.setHorizontalGroup(
            CompanyInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CompanyInfoPanelLayout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addGroup(CompanyInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12)
                    .addComponent(jLabel10)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(cmbCompanyName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6)
                    .addComponent(txtBusinessAddress)
                    .addComponent(jLabel9)
                    .addComponent(txtPhone)
                    .addComponent(jLabel11)
                    .addComponent(txtWebsite)
                    .addComponent(cmbBusinessType, 0, 259, Short.MAX_VALUE)
                    .addComponent(txtEmail)
                    .addComponent(txtTaxID))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        CompanyInfoPanelLayout.setVerticalGroup(
            CompanyInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CompanyInfoPanelLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCompanyName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cmbBusinessType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBusinessAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtWebsite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addGap(2, 2, 2)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTaxID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(90, 90, 90))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Configure basic application settings and company information");

        RegionalPanel.setBackground(new java.awt.Color(80, 108, 133));

        jLabel13.setText("🌍 Regional Settings");

        jLabel14.setText("Currency");

        jLabel15.setText("Time Zone");

        jLabel16.setText("Date Format");

        cmbCurrency.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "LKR", "USD", "EUR", "GBP", "INR" }));

        cmbDateFormat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "yyyy-MM-dd", "dd/MM/yyyy", "MM-dd-yyyy" }));

        cmbTimeZone.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "GMT+5:30", "GMT", "GMT+1", "GMT+8", "GMT-5" }));

        jLabel17.setText("Language");

        cmbLanguage.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "English", "Sinhala", "Tamil" }));

        jLabel18.setText("Time Format");

        cmbTimeFormat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "HH:mm:ss", "hh:mm a" }));

        jLabel19.setText("Number Format");

        cmbNumberFormat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1,234.56", "1.234,56", "1234.56" }));

        javax.swing.GroupLayout RegionalPanelLayout = new javax.swing.GroupLayout(RegionalPanel);
        RegionalPanel.setLayout(RegionalPanelLayout);
        RegionalPanelLayout.setHorizontalGroup(
            RegionalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RegionalPanelLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(RegionalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(RegionalPanelLayout.createSequentialGroup()
                        .addGroup(RegionalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel13)
                            .addComponent(jLabel16)
                            .addComponent(jLabel15))
                        .addContainerGap(224, Short.MAX_VALUE))
                    .addGroup(RegionalPanelLayout.createSequentialGroup()
                        .addGroup(RegionalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cmbLanguage, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbDateFormat, 0, 269, Short.MAX_VALUE)
                            .addComponent(cmbTimeFormat, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbTimeZone, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbCurrency, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbNumberFormat, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        RegionalPanelLayout.setVerticalGroup(
            RegionalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RegionalPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCurrency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cmbTimeZone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbNumberFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbTimeFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbDateFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbLanguage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        btnsPanel.setBackground(new java.awt.Color(80, 108, 133));

        btnReset.setBackground(new java.awt.Color(59, 81, 108));
        btnReset.setText("🔄 Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnSaveChanges.setBackground(new java.awt.Color(59, 81, 108));
        btnSaveChanges.setText("💾 Save Changes");
        btnSaveChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveChangesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout btnsPanelLayout = new javax.swing.GroupLayout(btnsPanel);
        btnsPanel.setLayout(btnsPanelLayout);
        btnsPanelLayout.setHorizontalGroup(
            btnsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnsPanelLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(btnSaveChanges, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        btnsPanelLayout.setVerticalGroup(
            btnsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnsPanelLayout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(btnsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReset)
                    .addComponent(btnSaveChanges))
                .addGap(9, 9, 9))
        );

        jLabel20.setText("🔔 Notifications");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("General Settings");

        jPanel5.setBackground(new java.awt.Color(52, 73, 94));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Inventory QR");

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Management System");

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/InventoryImages/icons8-user.gif"))); // NOI18N

        lblUsername.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblUsername.setText("Admin User");

        lblRole.setText("Administrator");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel23)
                .addGap(35, 35, 35)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUsername)
                    .addComponent(lblRole))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(lblUsername)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRole)))
                .addContainerGap(9, Short.MAX_VALUE))
        );

        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/InventoryImages/icons8-dashboard-32.png"))); // NOI18N

        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/InventoryImages/icons8-product-32.png"))); // NOI18N

        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/InventoryImages/icons8-scan-stock-32.png"))); // NOI18N

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/InventoryImages/icons8-futures-32.png"))); // NOI18N

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/InventoryImages/icons8-settings-32.png"))); // NOI18N

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/InventoryImages/icons8-user-account-32.png"))); // NOI18N

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/InventoryImages/icons8-inventory.gif"))); // NOI18N

        lblDash.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDash.setForeground(new java.awt.Color(255, 255, 255));
        lblDash.setText("Dashboard");
        lblDash.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDashMouseClicked(evt);
            }
        });

        lblInventory.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblInventory.setForeground(new java.awt.Color(255, 255, 255));
        lblInventory.setText("Inventory");

        lblReports.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblReports.setForeground(new java.awt.Color(255, 255, 255));
        lblReports.setText("Reports");

        lblQRScan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblQRScan.setForeground(new java.awt.Color(255, 255, 255));
        lblQRScan.setText("QR Scanner");

        lblUsers.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblUsers.setForeground(new java.awt.Color(255, 255, 255));
        lblUsers.setText("Users");

        lblSettings.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblSettings.setForeground(new java.awt.Color(255, 255, 255));
        lblSettings.setText("Settings");

        lblLogout.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblLogout.setForeground(new java.awt.Color(255, 255, 255));
        lblLogout.setText("Logout");

        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/InventoryImages/icons8-logout-32.png"))); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30)
                    .addComponent(jLabel31)
                    .addComponent(jLabel32)
                    .addComponent(jLabel37))
                .addGap(46, 46, 46)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblInventory)
                    .addComponent(lblDash)
                    .addComponent(lblQRScan)
                    .addComponent(lblReports)
                    .addComponent(lblSettings)
                    .addComponent(lblUsers)
                    .addComponent(lblLogout))
                .addContainerGap(69, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 15, Short.MAX_VALUE)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36))
                .addGap(21, 21, 21))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel36))
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel27)
                    .addComponent(lblDash))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblInventory))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblQRScan))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblReports))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel31)
                    .addComponent(lblSettings))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUsers))
                .addGap(60, 60, 60)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel37)
                    .addComponent(lblLogout))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel21)
                            .addGap(548, 548, 548)
                            .addComponent(jLabel20))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(CompanyInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(RegionalPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel2))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CompanyInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RegionalPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBusinessAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusinessAddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusinessAddressActionPerformed

    private void txtWebsiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtWebsiteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtWebsiteActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
 
    if (isResetting) return; // prevent repeated triggers

    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to reset the fields?",
        "Confirm Reset",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        isResetting = true;
        clearFields();
        isResetting = false;

        JOptionPane.showMessageDialog(this, 
            "Fields have been reset to the last saved configuration.", 
            "Reset Complete", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
 
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnSaveChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveChangesActionPerformed
        // TODO add your handling code here:
   if (!isDataChanged()) {
        JOptionPane.showMessageDialog(this, 
            "No changes detected. Nothing to save.", 
            "Info", 
            JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    // Confirm save
    int confirm = JOptionPane.showConfirmDialog(this,
        "Do you want to save the changes?",
        "Confirm Save",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        saveSettings();      // Calls your saveSettings method
        loadSettingsData();  // Reload to update originalData after saving
    }
    }//GEN-LAST:event_btnSaveChangesActionPerformed

    private void cmbCompanyNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCompanyNameActionPerformed
        // TODO add your handling code here:
        
         String selectedCompany = (String) cmbCompanyName.getSelectedItem();
        if (selectedCompany != null) {
            loadCompanyData(selectedCompany);
        }
    
    }//GEN-LAST:event_cmbCompanyNameActionPerformed

    private void lblDashMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDashMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblDashMouseClicked

    
   
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new SettingsForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CompanyInfoPanel;
    private javax.swing.JPanel RegionalPanel;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSaveChanges;
    private javax.swing.JPanel btnsPanel;
    private javax.swing.JComboBox<String> cmbBusinessType;
    private javax.swing.JComboBox<String> cmbCompanyName;
    private javax.swing.JComboBox<String> cmbCurrency;
    private javax.swing.JComboBox<String> cmbDateFormat;
    private javax.swing.JComboBox<String> cmbLanguage;
    private javax.swing.JComboBox<String> cmbNumberFormat;
    private javax.swing.JComboBox<String> cmbTimeFormat;
    private javax.swing.JComboBox<String> cmbTimeZone;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JLabel lblDash;
    private javax.swing.JLabel lblInventory;
    private javax.swing.JLabel lblLogout;
    private javax.swing.JLabel lblQRScan;
    private javax.swing.JLabel lblReports;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSettings;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblUsers;
    private javax.swing.JTextField txtBusinessAddress;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtTaxID;
    private javax.swing.JTextField txtWebsite;
    // End of variables declaration//GEN-END:variables
}
