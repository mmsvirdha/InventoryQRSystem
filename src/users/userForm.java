/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package users;

import QrScanner.QRCodeScanner;
import Settings.SettingsForm;
import dashboard.Dashboard;
import inventory.StockInOutForm;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import utils.DBConnection;
import javax.swing.JOptionPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;
import login.LoginForm;
import products.ProductForm;
import reports.ReportForm;
import utils.TransitionHelper;

/**
 *
 * @author ADMIN
 */
public class userForm extends javax.swing.JFrame {
    
     private Connection con;
    private DefaultTableModel tableModel;

 
      private String username;
    private String role;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(userForm.class.getName());

    /**
     * Creates new form userFrom
     */
    public userForm() {
        initComponents();
       
         con = DBConnection.getConnection(); // DB connection 
        
          activateAllLinks();
          
// Remove Black Rectangle Completely
UserInfoPanle.setOpaque(false);
UserInfoPanle.setBorder(null);

PermissionaPanel.setOpaque(false);
PermissionaPanel.setBorder(null);

btnsPanel.setOpaque(false);
btnsPanel.setBorder(null);

CustomPanle.setOpaque(false);
CustomPanle.setBorder(null);

          
   loadUsers(); // Add this line to load users when form opens
        
       

           // listener to automatically set permissions based on the selected role
               cmbRole.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
             // Call the method to handle the permission updates
             updatePermissionsByRole();
             }
           });


userTable.addMouseListener(new java.awt.event.MouseAdapter() {
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        userTableMouseClicked(evt);
    }
});


    }
    
     public userForm (String username, String role) {  // Accept parameters
         
         this.username = username;
    this.role = role;
    
    initComponents();
    
    con = DBConnection.getConnection(); // DB connection 
        // Maximizes the window to full screen size
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    
    loadUsers(); // Add this line to load users when form opens
    
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

 // listener to automatically set permissions based on the selected role
               cmbRole.addActionListener(new java.awt.event.ActionListener() {
               public void actionPerformed(java.awt.event.ActionEvent evt) {
             // Call the method to handle the permission updates
             updatePermissionsByRole();
             }
           });


userTable.addMouseListener(new java.awt.event.MouseAdapter() {
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        userTableMouseClicked(evt);
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
                userForm.this,
                "Do you really want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                TransitionHelper.fadeToFrame(userForm.this, new LoginForm(), 500);
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
    
   private void updatePermissionsByRole() {
    Object selected = cmbRole.getSelectedItem();
    String role = (selected != null) ? selected.toString() : "";

    // Step 1: Set the default state for ALL checkboxes to UNCHECKED and DISABLED.
    // We start from the most restrictive state (denied and non-editable).
    chkGenerateQR.setSelected(false);
    chkScanQR.setSelected(false);
    chkManageUsers.setSelected(false);
    chkManageProducts.setSelected(false);
    chkViewScanHistory.setSelected(false);
    chkViewUserLogs.setSelected(false);
    chkAccessSettings.setSelected(false);
    
    chkGenerateQR.setEnabled(false);
    chkScanQR.setEnabled(false);
    chkManageUsers.setEnabled(false);
    chkManageProducts.setEnabled(false);
    chkViewScanHistory.setEnabled(false);
    chkViewUserLogs.setEnabled(false);
    chkAccessSettings.setEnabled(false);


    // Step 2: Based on the selected role, ENABLE the permissions that are 
    // allowed to be manually granted (the "Granted Permissions" list).
    switch (role) {
        case "Admin":
            // Admin: Can potentially access everything, so ALL are ENABLED (editable).
            // They remain unchecked by default, allowing the administrator to choose.
            chkGenerateQR.setEnabled(true);
            chkScanQR.setEnabled(true);
            chkManageUsers.setEnabled(true);
            chkManageProducts.setEnabled(true);
            chkViewScanHistory.setEnabled(true);
            chkViewUserLogs.setEnabled(true);
            chkAccessSettings.setEnabled(true);
            break;

        case "Staff":
            // Staff: Only inventory and QR functions are allowed to be granted.
            chkGenerateQR.setEnabled(true);
            chkScanQR.setEnabled(true);
            chkManageProducts.setEnabled(true);
            chkViewScanHistory.setEnabled(true);
            
            // ManageUsers, ViewLogs, AccessSettings remain DISABLED (prohibited).
            break;

        case "Viewer":
            // Viewer: Only scanning and history viewing are allowed to be granted.
            chkScanQR.setEnabled(true);
            chkViewScanHistory.setEnabled(true);
            
            // All others remain DISABLED (prohibited).
            break;

        // Default case keeps all permissions disabled and unchecked.
    }
}
    

    // Load users into JTable
    private void loadUsers() {
        try {
           String query = "SELECT user_id, full_name, username, email, contact_no, role, status, " +
               "can_generate_qr, can_scan_qr, can_manage_users, can_manage_products, " +
               "can_view_scan_history, can_view_user_logs, can_access_settings FROM users";

            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            tableModel = (DefaultTableModel) userTable.getModel();
            tableModel.setRowCount(0); // clear table

            while(rs.next()) {
    Vector row = new Vector();
    row.add(rs.getInt("user_id"));
    row.add(rs.getString("full_name"));
    row.add(rs.getString("username"));
    row.add(rs.getString("email"));
    row.add(rs.getString("contact_no"));
    row.add(rs.getString("role"));
    row.add(rs.getString("status"));
    row.add(rs.getBoolean("can_generate_qr"));
    row.add(rs.getBoolean("can_scan_qr"));
    row.add(rs.getBoolean("can_manage_users"));
    row.add(rs.getBoolean("can_manage_products"));
    row.add(rs.getBoolean("can_view_scan_history"));
    row.add(rs.getBoolean("can_view_user_logs"));
    row.add(rs.getBoolean("can_access_settings"));
    tableModel.addRow(row);
}

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // Clear all form fields
    private void clearFields() {
        txtFullName.setText("");
        txtUsername.setText("");
        txtEmail.setText("");
        txtContact.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        cmbRole.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        chkGenerateQR.setSelected(false);
        chkScanQR.setSelected(false);
        chkManageUsers.setSelected(false);
        chkManageProducts.setSelected(false);
        chkViewScanHistory.setSelected(false);
        chkViewUserLogs.setSelected(false);
        chkAccessSettings.setSelected(false);
    }

    // Save new user
    private void saveUser() {
        try {
            String password = new String(txtPassword.getPassword());
String confirm = new String(txtConfirmPassword.getPassword());
if(!password.equals(confirm)){
    JOptionPane.showMessageDialog(this, "Passwords do not match!");
    return;
}


           String sql = "INSERT INTO users(username, full_name, email, contact_no, password, role, status, "
           + "can_generate_qr, can_scan_qr, can_manage_users, can_manage_products, "
           + "can_view_scan_history, can_view_user_logs, can_access_settings) "
           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

PreparedStatement ps = con.prepareStatement(sql);
ps.setString(1, txtUsername.getText());
ps.setString(2, txtFullName.getText());
ps.setString(3, txtEmail.getText());
ps.setString(4, txtContact.getText());
ps.setString(5, new String(txtPassword.getPassword()));
ps.setString(6, cmbRole.getSelectedItem().toString()); // example for role
ps.setString(7, cmbStatus.getSelectedItem().toString()); // 'Active' or 'Inactive'
ps.setBoolean(8, chkGenerateQR.isSelected());
ps.setBoolean(9, chkScanQR.isSelected());
ps.setBoolean(10, chkManageUsers.isSelected());
ps.setBoolean(11, chkManageProducts.isSelected());
ps.setBoolean(12, chkViewScanHistory.isSelected());
ps.setBoolean(13, chkViewUserLogs.isSelected());
ps.setBoolean(14, chkAccessSettings.isSelected());

ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "User Saved!");
            loadUsers();
            clearFields();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // Update selected user
    private void updateUser() {
        try {
            int row = userTable.getSelectedRow();
            if(row == -1){
                JOptionPane.showMessageDialog(this, "Select a user from table first!");
                return;
            }
            int userId = Integer.parseInt(userTable.getValueAt(row, 0).toString());

            String query = "UPDATE users SET full_name=?, username=?, email=?, contact_no=?, " +
                    "role=?, status=?, can_generate_qr=?, can_scan_qr=?, can_manage_users=?, " +
                    "can_manage_products=?, can_view_scan_history=?, can_view_user_logs=?, can_access_settings=? " +
                    "WHERE user_id=?";

            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, txtFullName.getText());
            pst.setString(2, txtUsername.getText());
            pst.setString(3, txtEmail.getText());
            pst.setString(4, txtContact.getText());
            pst.setString(5, cmbRole.getSelectedItem().toString());
            pst.setString(6, cmbStatus.getSelectedItem().toString());
            pst.setBoolean(7, chkGenerateQR.isSelected());
            pst.setBoolean(8, chkScanQR.isSelected());
            pst.setBoolean(9, chkManageUsers.isSelected());
            pst.setBoolean(10, chkManageProducts.isSelected());
            pst.setBoolean(11, chkViewScanHistory.isSelected());
            pst.setBoolean(12, chkViewUserLogs.isSelected());
            pst.setBoolean(13, chkAccessSettings.isSelected());
            pst.setInt(14, userId);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "User Updated!");
            loadUsers();
            clearFields();
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // Delete selected user
    private void deleteUser() {
        try {
            int row = userTable.getSelectedRow();
            if(row == -1){
                JOptionPane.showMessageDialog(this, "Select a user from table first!");
                return;
            }
            int userId = Integer.parseInt(userTable.getValueAt(row, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Delete User", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION){
                String query = "DELETE FROM users WHERE user_id=?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, userId);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "User Deleted!");
                loadUsers();
                clearFields();
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // JTable row click: load selected user to fields
    private void userTableMouseClicked(java.awt.event.MouseEvent evt) {
        int row = userTable.getSelectedRow();
        if(row != -1){
            txtFullName.setText(userTable.getValueAt(row, 1).toString());
            txtUsername.setText(userTable.getValueAt(row, 2).toString());
            cmbRole.setSelectedItem(userTable.getValueAt(row, 3).toString());
            cmbStatus.setSelectedItem(userTable.getValueAt(row, 4).toString());

            try{
                int userId = Integer.parseInt(userTable.getValueAt(row, 0).toString());
                PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE user_id=?");
                pst.setInt(1, userId);
                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    chkGenerateQR.setSelected(rs.getBoolean("can_generate_qr"));
                    chkScanQR.setSelected(rs.getBoolean("can_scan_qr"));
                    chkManageUsers.setSelected(rs.getBoolean("can_manage_users"));
                    chkManageProducts.setSelected(rs.getBoolean("can_manage_products"));
                    chkViewScanHistory.setSelected(rs.getBoolean("can_view_scan_history"));
                    chkViewUserLogs.setSelected(rs.getBoolean("can_view_user_logs"));
                    chkAccessSettings.setSelected(rs.getBoolean("can_access_settings"));
                }
            }catch(Exception e){System.out.println(e);}
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

        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        CustomPanle = new utils.RoundedPanel(25);
        UserInfoPanle = new utils.RoundedPanel(25);
        jLabel1 = new javax.swing.JLabel();
        txtFullName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtContact = new javax.swing.JTextField();
        cmbRole = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        txtPassword = new javax.swing.JPasswordField();
        txtConfirmPassword = new javax.swing.JPasswordField();
        btnsPanel = new utils.RoundedPanel(25);
        btnSave = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        PermissionaPanel = new utils.RoundedPanel(25);
        chkGenerateQR = new javax.swing.JCheckBox();
        chkViewScanHistory = new javax.swing.JCheckBox();
        chkScanQR = new javax.swing.JCheckBox();
        chkViewUserLogs = new javax.swing.JCheckBox();
        chkManageProducts = new javax.swing.JCheckBox();
        chkManageUsers = new javax.swing.JCheckBox();
        chkAccessSettings = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
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

        jPanel5.setBackground(new java.awt.Color(59, 81, 108));

        jPanel1.setBackground(new java.awt.Color(59, 81, 108));

        CustomPanle.setBackground(new java.awt.Color(80, 108, 133));

        UserInfoPanle.setBorder(javax.swing.BorderFactory.createTitledBorder("User Information"));

        jLabel1.setText("Full Name");

        txtFullName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFullNameActionPerformed(evt);
            }
        });

        jLabel2.setText("Username");

        jLabel3.setText("Email");

        jLabel4.setText("Contact No");

        jLabel5.setText("Password");

        jLabel6.setText("Confirm Password");

        jLabel10.setText("Role");

        txtUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsernameActionPerformed(evt);
            }
        });

        cmbRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Staff", "Viewer" }));

        jLabel11.setText("Status");

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Inactive" }));

        javax.swing.GroupLayout UserInfoPanleLayout = new javax.swing.GroupLayout(UserInfoPanle);
        UserInfoPanle.setLayout(UserInfoPanleLayout);
        UserInfoPanleLayout.setHorizontalGroup(
            UserInfoPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserInfoPanleLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(UserInfoPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(UserInfoPanleLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                        .addComponent(txtContact, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UserInfoPanleLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(UserInfoPanleLayout.createSequentialGroup()
                        .addGroup(UserInfoPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(UserInfoPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFullName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUsername, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(65, 65, 65)
                .addGroup(UserInfoPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(UserInfoPanleLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(UserInfoPanleLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbRole, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(UserInfoPanleLayout.createSequentialGroup()
                        .addGroup(UserInfoPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                        .addGroup(UserInfoPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 48, Short.MAX_VALUE))
        );
        UserInfoPanleLayout.setVerticalGroup(
            UserInfoPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UserInfoPanleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(UserInfoPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(UserInfoPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UserInfoPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(UserInfoPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(UserInfoPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtConfirmPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(UserInfoPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(UserInfoPanleLayout.createSequentialGroup()
                        .addGroup(UserInfoPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(UserInfoPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtContact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addGroup(UserInfoPanleLayout.createSequentialGroup()
                        .addGroup(UserInfoPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(UserInfoPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout btnsPanelLayout = new javax.swing.GroupLayout(btnsPanel);
        btnsPanel.setLayout(btnsPanelLayout);
        btnsPanelLayout.setHorizontalGroup(
            btnsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnsPanelLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );
        btnsPanelLayout.setVerticalGroup(
            btnsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(btnsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete)
                    .addComponent(btnClear))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        userTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Full Name", "Username", "Email", "Contact No", "Role", "Status", "Generate QR", "Scan QR", "Manage Users", "Manage Products", "View Scan History", "View User Logs", "Access Settings"
            }
        ));
        jScrollPane1.setViewportView(userTable);

        PermissionaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Permissions"));

        chkGenerateQR.setText("Generate QR");

        chkViewScanHistory.setText("View Scan History");

        chkScanQR.setText("Scan QR");

        chkViewUserLogs.setText("View User Logs");

        chkManageProducts.setText("Manage Products");
        chkManageProducts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkManageProductsActionPerformed(evt);
            }
        });

        chkManageUsers.setText("Manage Users");

        chkAccessSettings.setText("Access Settings");

        javax.swing.GroupLayout PermissionaPanelLayout = new javax.swing.GroupLayout(PermissionaPanel);
        PermissionaPanel.setLayout(PermissionaPanelLayout);
        PermissionaPanelLayout.setHorizontalGroup(
            PermissionaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PermissionaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PermissionaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkGenerateQR, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkScanQR, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addGroup(PermissionaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(chkViewScanHistory)
                    .addComponent(chkViewUserLogs, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PermissionaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PermissionaPanelLayout.createSequentialGroup()
                        .addComponent(chkManageUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(247, 247, 247))
                    .addGroup(PermissionaPanelLayout.createSequentialGroup()
                        .addComponent(chkAccessSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chkManageProducts)
                        .addGap(56, 56, 56))))
        );
        PermissionaPanelLayout.setVerticalGroup(
            PermissionaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PermissionaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PermissionaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkGenerateQR)
                    .addComponent(chkViewScanHistory)
                    .addComponent(chkAccessSettings)
                    .addComponent(chkManageProducts))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PermissionaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkScanQR)
                    .addComponent(chkViewUserLogs)
                    .addComponent(chkManageUsers))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout CustomPanleLayout = new javax.swing.GroupLayout(CustomPanle);
        CustomPanle.setLayout(CustomPanleLayout);
        CustomPanleLayout.setHorizontalGroup(
            CustomPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomPanleLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(CustomPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(UserInfoPanle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PermissionaPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        CustomPanleLayout.setVerticalGroup(
            CustomPanleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomPanleLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(UserInfoPanle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PermissionaPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(72, Short.MAX_VALUE))
        );

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("User Management");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(CustomPanle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(CustomPanle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(52, 73, 94));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30)
                    .addComponent(jLabel31)
                    .addComponent(jLabel32)
                    .addComponent(jLabel37))
                .addGap(46, 46, 46)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblInventory)
                    .addComponent(lblDash)
                    .addComponent(lblQRScan)
                    .addComponent(lblReports)
                    .addComponent(lblSettings)
                    .addComponent(lblUsers)
                    .addComponent(lblLogout))
                .addContainerGap(69, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 15, Short.MAX_VALUE)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36))
                .addGap(21, 21, 21))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel36))
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel27)
                    .addComponent(lblDash))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblInventory))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblQRScan))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblReports))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel31)
                    .addComponent(lblSettings))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUsers))
                .addGap(60, 60, 60)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel37)
                    .addComponent(lblLogout))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chkManageProductsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkManageProductsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkManageProductsActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:

        clearFields();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:

        deleteUser();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:

        updateUser();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:

        saveUser();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsernameActionPerformed

    private void txtFullNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFullNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFullNameActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new userForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CustomPanle;
    private javax.swing.JPanel PermissionaPanel;
    private javax.swing.JPanel UserInfoPanle;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JPanel btnsPanel;
    private javax.swing.JCheckBox chkAccessSettings;
    private javax.swing.JCheckBox chkGenerateQR;
    private javax.swing.JCheckBox chkManageProducts;
    private javax.swing.JCheckBox chkManageUsers;
    private javax.swing.JCheckBox chkScanQR;
    private javax.swing.JCheckBox chkViewScanHistory;
    private javax.swing.JCheckBox chkViewUserLogs;
    private javax.swing.JComboBox<String> cmbRole;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDash;
    private javax.swing.JLabel lblInventory;
    private javax.swing.JLabel lblLogout;
    private javax.swing.JLabel lblQRScan;
    private javax.swing.JLabel lblReports;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSettings;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblUsers;
    private javax.swing.JPasswordField txtConfirmPassword;
    private javax.swing.JTextField txtContact;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JTable userTable;
    // End of variables declaration//GEN-END:variables
}
