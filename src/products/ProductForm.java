/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package products;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Point;
import QrScanner.QRCodeScanner;
import Settings.SettingsForm;
import dashboard.Dashboard;
import inventory.StockInOutForm;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import utils.DBConnection;
import utils.QRCodeGenerator; // for QR code generation
import java.io.File;
import login.LoginForm;
import reports.ReportForm;
import users.userForm;
import utils.centerFrame;
import Payment.PaymentForm;
import utils.TransitionHelper;
import javax.swing.ImageIcon;
import java.awt.Image;
        

/**
 *
 * @author ADMIN
 */
public class ProductForm extends javax.swing.JFrame {
    
    private String username;
    private String role;
        
    //to receive product ID
     private String selectedProductId; 
    
     private javax.swing.Timer previewTimer; // declare as class field

    // 1. Define the default folder path for QR codes
    private static final String DEFAULT_QR_DIR = 
        "C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\ProductQRCodes";

    // Helper method to ensure the directory exists
    private void ensureQRDirectoryExists() {
        File dir = new File(DEFAULT_QR_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ProductForm.class.getName());

    /**
     * Creates new form ProductForm
     */
   

    public ProductForm() {
        initComponents();
            
        // Remove Black Rectangle Completely
        
        AddProPanel.setOpaque(false);
AddProPanel.setBorder(null);

headerPanel.setOpaque(false);
headerPanel.setBorder(null);


         
         centerFrame.centerFrame(this); 
         generateProductId();
        
         getNextProductId(); // Auto-generate next ID when form opens
         
        txtProductId.setEditable(false);
        
           activateAllLinks();
           
         

    }
   
    
    public ProductForm(String username, String role) {  
        
         this.username = username;
        this.role = role;
     
        
    initComponents();
    
        // Maximizes the window to full screen size
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    
   
 lblUsername.setText(capitalizeFirstLetter(username));
    lblRole.setText(formatRole(role));

     generateProductId();
    getNextProductId();
   
     activateAllLinks();
     
     // Initialize auto-complete
    initializeAutoComplete();
             
TransitionHelper.applyTealHoverSmooth(lblQRScan);
TransitionHelper.applyTealHoverSmooth(lblReports);
TransitionHelper.applyTealHoverSmooth(lblSettings);
TransitionHelper.applyTealHoverSmooth(lblUsers);
TransitionHelper.applyTealHoverSmooth(lblDash);
TransitionHelper.applyTealHoverSmooth(lblInventory);
TransitionHelper.applyTealHoverSmooth(lblLogout);

TransitionHelper.applyTealHoverSmooth(lblProducts);
TransitionHelper.applyTealHoverSmooth(lblStock);
TransitionHelper.applyTealHoverSmooth(lblPayment);


}
    private JPopupMenu suggestionPopup;
private JList<String> suggestionList;
private DefaultListModel<String> suggestionModel;
private boolean isSelectingFromSuggestion = false;
    
    // Add this new method to initialize auto-complete functionality
private void initializeAutoComplete() {
    // Create suggestion components
    suggestionModel = new DefaultListModel<>();
    suggestionList = new JList<>(suggestionModel);
    suggestionPopup = new JPopupMenu();
    
    // Style the suggestion list
    JScrollPane scrollPane = new JScrollPane(suggestionList);
    scrollPane.setPreferredSize(new java.awt.Dimension(txtProductName.getWidth(), 150));
    suggestionPopup.add(scrollPane);
    suggestionPopup.setFocusable(false);
    
    // Add document listener to product name field
    txtProductName.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            if (!isSelectingFromSuggestion) {
                searchProducts();
            }
        }
        
        @Override
        public void removeUpdate(DocumentEvent e) {
            if (!isSelectingFromSuggestion) {
                searchProducts();
            }
        }
        
        @Override
        public void changedUpdate(DocumentEvent e) {
            if (!isSelectingFromSuggestion) {
                searchProducts();
            }
        }
    });
    
    // Add mouse listener to suggestion list
    suggestionList.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
                selectProduct();
            }
        }
    });
    
    // Add key listener to handle Enter key
    suggestionList.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                selectProduct();
            }
        }
    });
    
    // Add key listener to txtProductName for navigation
    txtProductName.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (suggestionPopup.isVisible()) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN) {
                    suggestionList.requestFocus();
                    suggestionList.setSelectedIndex(0);
                } else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                    suggestionPopup.setVisible(false);
                }
            }
        }
    });
}
    
public ProductForm(String username, String role, String selectedProductId) {
    this.username = username;
    this.role = role;
    this.selectedProductId = selectedProductId;
    initComponents();
    lblUsername.setText(capitalizeFirstLetter(username));
    lblRole.setText(formatRole(role));

    // Load this specific product details
    loadProductDetails(selectedProductId);
    
  
     activateAllLinks();
     
     // Initialize auto-complete
    initializeAutoComplete();
             
TransitionHelper.applyTealHoverSmooth(lblQRScan);
TransitionHelper.applyTealHoverSmooth(lblReports);
TransitionHelper.applyTealHoverSmooth(lblSettings);
TransitionHelper.applyTealHoverSmooth(lblUsers);
TransitionHelper.applyTealHoverSmooth(lblDash);
TransitionHelper.applyTealHoverSmooth(lblInventory);
TransitionHelper.applyTealHoverSmooth(lblLogout);

TransitionHelper.applyTealHoverSmooth(lblProducts);
TransitionHelper.applyTealHoverSmooth(lblStock);
TransitionHelper.applyTealHoverSmooth(lblPayment);

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

            switch (targetFormClass) {
                case "Dashboard":
                    nextFrame = new Dashboard(username, role);
                    break;
                case "ProductForm":
                    // Open general product view (no product selected)
                    nextFrame = new ProductForm(username, role);
                    break;
                case "StockInOutForm":
                    nextFrame = new StockInOutForm(username, role);
                    break;
                case "PaymentForm":
                    nextFrame = new PaymentForm(username, role);
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

     makeHoverLink(lblProducts, this, "ProductForm");
    makeHoverLink(lblStock, this, "StockInOutForm");
    makeHoverLink(lblPayment, this, "PaymentForm");
    
    // Logout with confirmation
    lblLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
    lblLogout.addMouseListener(new MouseAdapter() {
        
        @Override
        public void mouseClicked(MouseEvent e) {
            int confirm = JOptionPane.showConfirmDialog(
                ProductForm.this,
                "Do you really want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                TransitionHelper.fadeToFrame(ProductForm.this, new LoginForm(), 500);
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
    
    public ProductForm(String productIdOrQrCode) {
    initComponents();  // Initialize the form

    // If the string is not empty, use it
    if (productIdOrQrCode != null && !productIdOrQrCode.isEmpty()) {
        txtProductId.setText(productIdOrQrCode); // Show it in the text field
        loadProductDetails(productIdOrQrCode);   // Load existing product (optional)
    } else {
        getNextProductId(); // Auto-generate new product ID
    }

    activateAllLinks(); // Initialize any links
}

    private void generateProductId() {
    try (Connection conn = DBConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT product_id FROM products ORDER BY product_id DESC LIMIT 1")) {
        
        String newId = "P001"; // default first ID
        if (rs.next()) {
            String lastId = rs.getString("product_id");
            int idNum = Integer.parseInt(lastId.substring(1)) + 1;
            newId = String.format("P%03d", idNum);
        }
        txtProductId.setText(newId);
        txtProductId.setEditable(false);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    
   // Inside QRCodeScanner class
private String getNextProductId() {
    String maxId = "P001"; // Default starting ID

    try (Connection conn = DBConnection.getConnection()) {
        // Query to find the maximum existing product ID that starts with 'P' and extracts the numeric part
        String sql = "SELECT MAX(product_id) AS max_id FROM products WHERE product_id LIKE 'P%'";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            String currentMaxId = rs.getString("max_id");
            if (currentMaxId != null && currentMaxId.startsWith("P")) {
                // Extract the number part, e.g., "005" from "P005"
                String numberPart = currentMaxId.substring(1);
                
                // Convert to integer, increment, and format back
                try {
                    int nextNumber = Integer.parseInt(numberPart) + 1;
                    // Format back to P00X (e.g., 6 -> P006, 15 -> P015)
                    maxId = "P" + String.format("%03d", nextNumber);
                } catch (NumberFormatException e) {
                    // Handle case where product ID might be non-numeric (e.g., PABC)
                    System.err.println("Error parsing numeric part of product ID: " + numberPart);
                    // Fall back to default
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
                "Error retrieving next Product ID: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    System.out.println("Generated Next Product ID: " + maxId);
    return maxId;
}

//method to search for products
private void searchProducts() {

    String searchText = txtProductName.getText().trim();

    if (searchText.isEmpty()) {
        suggestionPopup.setVisible(false);
        return;
    }

    suggestionModel.clear();

    String sql = "SELECT product_id, product_name FROM products WHERE product_name LIKE ? ORDER BY product_name LIMIT 10";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, "%" + searchText + "%");

        try (ResultSet rs = pst.executeQuery()) {
            boolean hasResults = false;

            while (rs.next()) {
                String productId = rs.getString("product_id");
                String productName = rs.getString("product_name");
                suggestionModel.addElement(productId + " - " + productName);
                hasResults = true;
            }

            if (hasResults) {
                suggestionPopup.show(txtProductName, 0, txtProductName.getHeight());
            } else {
                suggestionPopup.setVisible(false);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}



//method to handle product selection from suggestions
private void selectProduct() {
    String selected = suggestionList.getSelectedValue();
    if (selected != null) {
        isSelectingFromSuggestion = true;
        
        // Extract product ID (format: "P001 - Product Name")
        String productId = selected.split(" - ")[0];
        
        // Load product details
        loadProductDetailsForUpdate(productId);
        
        suggestionPopup.setVisible(false);
        isSelectingFromSuggestion = false;
        
        // Focus on quantity field for user to add quantity
        txtQuantity.requestFocus();
        txtQuantity.selectAll();
    }
}

// Add this new method to load product details (except quantity)
private void loadProductDetailsForUpdate(String productId) {
    try (Connection conn = DBConnection.getConnection()) {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, productId);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            txtProductId.setText(rs.getString("product_id"));
            txtProductName.setText(rs.getString("product_name"));
            txtCategory.setText(rs.getString("category"));
            txtUnitPrice.setText(rs.getString("unit_price"));
            txtSupplier.setText(rs.getString("supplier"));
            txtLocation.setText(rs.getString("location"));
            
            // Clear quantity so user can add new quantity
            txtQuantity.setText("");
            
            // Disable product name editing since it's existing
            txtProductName.setEditable(false);
            txtProductId.setEditable(false);
        }
        
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error loading product: " + e.getMessage());
    }
}
    
   private void loadProductDetails(String productId) {
    String sql = "SELECT * FROM products WHERE product_id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, productId);

        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                txtProductId.setText(rs.getString("product_id"));
                txtProductName.setText(rs.getString("product_name"));
                txtCategory.setText(rs.getString("category"));
                txtQuantity.setText(rs.getString("quantity"));
                txtUnitPrice.setText(rs.getString("unit_price"));
                txtSupplier.setText(rs.getString("supplier"));
                txtLocation.setText(rs.getString("location"));
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private void clearFieldsExceptProductID() {
    txtProductName.setText("");
    txtCategory.setText("");
    txtQuantity.setText("");
    txtUnitPrice.setText("");
    txtSupplier.setText("");
    txtLocation.setText("");
}

    private void generateQRCodeForProduct(Connection conn, String productId, String productName) throws Exception {
    ensureQRDirectoryExists();

    String fileName = productName.replaceAll("\\s+", "_") + "_" + productId + ".png";
    String qrCodePath = DEFAULT_QR_DIR + File.separator + fileName;
    String qrData = "Product ID: " + productId + "\nProduct Name: " + productName;

    // 1. Physically generate the file
    QRCodeGenerator.generateQRCode(qrData, qrCodePath);

    // 2. Update the DB path using the provided connection
    String updateSQL = "UPDATE products SET qr_code_path=? WHERE product_id=?";
    try (PreparedStatement pst = conn.prepareStatement(updateSQL)) {
        pst.setString(1, qrCodePath);
        pst.setString(2, productId);
        pst.executeUpdate();
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

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        AddProPanel = new utils.RoundedPanel(25);
        jLabel1 = new javax.swing.JLabel();
        lblCategory = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtProductName = new javax.swing.JTextField();
        txtCategory = new javax.swing.JTextField();
        txtQuantity = new javax.swing.JTextField();
        txtUnitPrice = new javax.swing.JTextField();
        txtSupplier = new javax.swing.JTextField();
        txtLocation = new javax.swing.JTextField();
        btnGenerateQR = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtProductId = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        headerPanel = new utils.RoundedPanel(25);
        lblProducts = new javax.swing.JLabel();
        lblStock = new javax.swing.JLabel();
        lblPayment = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblPrevId = new javax.swing.JLabel();
        lblPrevName = new javax.swing.JLabel();
        lblPrevCategory = new javax.swing.JLabel();
        lblPrevQty = new javax.swing.JLabel();
        lblPrevPrice = new javax.swing.JLabel();
        lblPrevSupplier = new javax.swing.JLabel();
        lblPrevLocation = new javax.swing.JLabel();
        lblQR = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
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
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel5.setBackground(new java.awt.Color(59, 81, 108));

        jPanel3.setBackground(new java.awt.Color(59, 81, 108));

        AddProPanel.setBackground(new java.awt.Color(80, 108, 133));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Product Name");

        lblCategory.setForeground(new java.awt.Color(255, 255, 255));
        lblCategory.setText("Category");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Quantity");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Price per unit");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Supplier");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Location");

        txtUnitPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUnitPriceActionPerformed(evt);
            }
        });

        btnGenerateQR.setText("Generate QR");
        btnGenerateQR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateQRActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Product Id");

        javax.swing.GroupLayout AddProPanelLayout = new javax.swing.GroupLayout(AddProPanel);
        AddProPanel.setLayout(AddProPanelLayout);
        AddProPanelLayout.setHorizontalGroup(
            AddProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddProPanelLayout.createSequentialGroup()
                .addGroup(AddProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnGenerateQR, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(AddProPanelLayout.createSequentialGroup()
                        .addGroup(AddProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(AddProPanelLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addGroup(AddProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(lblCategory)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel2))
                                .addGap(25, 25, 25))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AddProPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(AddProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(AddProPanelLayout.createSequentialGroup()
                                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtLocation, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSupplier, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtUnitPrice, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtQuantity, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCategory, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtProductName, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtProductId, javax.swing.GroupLayout.Alignment.LEADING))))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        AddProPanelLayout.setVerticalGroup(
            AddProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AddProPanelLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(AddProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtProductId, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(AddProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(AddProPanelLayout.createSequentialGroup()
                        .addGroup(AddProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(AddProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCategory)
                            .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(AddProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4))
                    .addComponent(txtUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(AddProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(AddProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(AddProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete))
                .addGap(29, 29, 29)
                .addComponent(btnGenerateQR)
                .addGap(51, 51, 51))
        );

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Add or update your inventory items with ease");

        headerPanel.setBackground(new java.awt.Color(80, 108, 133));

        lblProducts.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblProducts.setForeground(new java.awt.Color(255, 255, 255));
        lblProducts.setText("Product ");

        lblStock.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblStock.setForeground(new java.awt.Color(255, 255, 255));
        lblStock.setText("Stock & Payment Informations");

        lblPayment.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPayment.setForeground(new java.awt.Color(255, 255, 255));
        lblPayment.setText("Payment ");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(lblProducts)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 213, Short.MAX_VALUE)
                .addComponent(lblStock)
                .addGap(227, 227, 227)
                .addComponent(lblPayment)
                .addGap(91, 91, 91))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProducts)
                    .addComponent(lblStock)
                    .addComponent(lblPayment))
                .addGap(18, 18, 18))
        );

        jPanel2.setBackground(new java.awt.Color(80, 108, 133));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Product details preview");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblPrevId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPrevName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPrevCategory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPrevQty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPrevPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPrevSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPrevLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(jLabel9)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 87, Short.MAX_VALUE)
                .addComponent(lblQR, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(81, 81, 81))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(lblQR, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblPrevId)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPrevName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPrevCategory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPrevQty)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPrevPrice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPrevSupplier)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPrevLocation)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(AddProPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addGap(35, 35, 35)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(AddProPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(52, 73, 94));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Inventory QR");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Management System");

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

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/InventoryImages/icons8-logout-32.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30)
                    .addComponent(jLabel31)
                    .addComponent(jLabel32)
                    .addComponent(jLabel13))
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblInventory)
                    .addComponent(lblDash)
                    .addComponent(lblQRScan)
                    .addComponent(lblReports)
                    .addComponent(lblSettings)
                    .addComponent(lblUsers)
                    .addComponent(lblLogout))
                .addContainerGap(69, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 15, Short.MAX_VALUE)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(21, 21, 21))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8))
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel27)
                    .addComponent(lblDash))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblInventory))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblQRScan))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblReports))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel31)
                    .addComponent(lblSettings))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUsers))
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13)
                    .addComponent(lblLogout))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        
    String productId = txtProductId.getText().trim();
    String name = txtProductName.getText().trim();
    String category = txtCategory.getText().trim();
    String quantityStr = txtQuantity.getText().trim();
    String priceStr = txtUnitPrice.getText().trim();
    String supplier = txtSupplier.getText().trim();
    String location = txtLocation.getText().trim();
    String updatedBy = "Admin";

    if (productId.isEmpty() || name.isEmpty() || category.isEmpty() || 
        quantityStr.isEmpty() || priceStr.isEmpty() || supplier.isEmpty() || location.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill all required fields!");
        return;
    }

    int newQuantity;
    double price;
    try {
        newQuantity = Integer.parseInt(quantityStr);
        price = Double.parseDouble(priceStr);
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Quantity must be an integer and Price must be a number!");
        return;
    }

    try (Connection conn = DBConnection.getConnection()) {
        conn.setAutoCommit(false);

        // 1️⃣ Get current quantity from DB
        int oldQuantity = 0;
        String sqlSelect = "SELECT quantity FROM products WHERE product_id=?";
        try (PreparedStatement pst = conn.prepareStatement(sqlSelect)) {
            pst.setString(1, productId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) oldQuantity = rs.getInt("quantity");
        }

        // 2️⃣ Update product table
        String sqlUpdate = "UPDATE products SET product_name=?, category=?, quantity=?, unit_price=?, supplier=?, location=?, updated_by=? WHERE product_id=?";
        try (PreparedStatement pst = conn.prepareStatement(sqlUpdate)) {
            pst.setString(1, name);
            pst.setString(2, category);
            pst.setInt(3, newQuantity);
            pst.setDouble(4, price);
            pst.setString(5, supplier);
            pst.setString(6, location);
            pst.setString(7, updatedBy);
            pst.setString(8, productId);
            pst.executeUpdate();
        }

int diff = newQuantity - oldQuantity;
if (diff != 0) {
    String actionType = diff > 0 ? "IN" : "OUT";

    String sqlHistory = "INSERT INTO stock_history (product_id, action_type, quantity, date_time, user, remarks) VALUES (?, ?, ?, NOW(), ?, ?)";
    try (PreparedStatement pst = conn.prepareStatement(sqlHistory)) {
        pst.setString(1, productId);                     
        pst.setString(2, actionType);                   
        pst.setInt(3, Math.abs(diff));                  
        pst.setString(4, updatedBy);  // use same logged-in user                  
        pst.setString(5, "Stock updated via product update");  
        pst.executeUpdate();
    }
}

        conn.commit();
        conn.setAutoCommit(true);

        JOptionPane.showMessageDialog(this, "Product updated successfully (stock history recorded)");
        // Load preview panel
        showProductPreview(productId, 5000); // auto-clear after 5 seconds

        
        // Refresh table/details
        loadProductDetails(productId);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error updating product: " + e.getMessage());
    }

    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnGenerateQRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateQRActionPerformed
        // TODO add your handling code here:
        
    String productId = txtProductId.getText().trim();
    String productName = txtProductName.getText().trim();

    if (productId.isEmpty() || productName.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill Product ID and Product Name!");
        return;
    }

    String checkSQL = "SELECT qr_code_path FROM products WHERE product_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement checkPst = conn.prepareStatement(checkSQL)) {

        checkPst.setString(1, productId);

        try (ResultSet rs = checkPst.executeQuery()) {

            if (rs.next()) {
                String existingPath = rs.getString("qr_code_path");
                if (existingPath != null && !existingPath.isEmpty()
                        && new File(existingPath).exists()) {

                    JOptionPane.showMessageDialog(this,
                            "QR Code already exists:\n" + existingPath);
                    clearFieldsExceptProductID();
                    txtProductId.setText(getNextProductId());
                    return;
                }
            }
        }

        ensureQRDirectoryExists();

        String fileName = productName.replaceAll("\\s+", "_") + "_" + productId + ".png";
        String qrCodePath = DEFAULT_QR_DIR + File.separator + fileName;

        String qrData = "Product ID: " + productId + "\nProduct Name: " + productName;

        QRCodeGenerator.generateQRCode(qrData, qrCodePath);

        String updateSQL = "UPDATE products SET qr_code_path=? WHERE product_id=?";
        try (PreparedStatement pst = conn.prepareStatement(updateSQL)) {
            pst.setString(1, qrCodePath);
            pst.setString(2, productId);
            pst.executeUpdate();
        }

        JOptionPane.showMessageDialog(this, "QR Code Generated Successfully!");

        clearFieldsExceptProductID();
        txtProductId.setText(getNextProductId());

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }

       
    }//GEN-LAST:event_btnGenerateQRActionPerformed

    // Add this method to clear all fields including product ID
private void clearAllFields() {
    txtProductId.setText("");
    txtProductName.setText("");
    txtCategory.setText("");
    txtQuantity.setText("");
    txtUnitPrice.setText("");
    txtSupplier.setText("");
    txtLocation.setText("");
    txtProductName.setEditable(true);
}

private void showProductPreview(String productId, int autoClearMillis) {
    String sql = "SELECT product_id, product_name, category, quantity, unit_price, supplier, location, qr_code_path FROM products WHERE product_id = ?";
   
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, productId);

        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                lblPrevId.setText(rs.getString("product_id"));
                lblPrevName.setText(rs.getString("product_name"));
                lblPrevCategory.setText(rs.getString("category"));
                lblPrevQty.setText(String.valueOf(rs.getInt("quantity")));
                lblPrevPrice.setText(String.valueOf(rs.getDouble("unit_price")));
                lblPrevSupplier.setText(rs.getString("supplier"));
                lblPrevLocation.setText(rs.getString("location"));

                String qrPath = rs.getString("qr_code_path");
                if (qrPath != null && new File(qrPath).exists()) {
                    ImageIcon icon = new ImageIcon(qrPath);
                    Image img = icon.getImage().getScaledInstance(lblQR.getWidth(), lblQR.getHeight(), Image.SCALE_SMOOTH);
                    lblQR.setIcon(new ImageIcon(img));
                } else {
                    lblQR.setIcon(null);
                }
            }
        }

        // Timer for auto-clear
        javax.swing.Timer timer = new javax.swing.Timer(autoClearMillis, e -> {
            clearProductPreview();
            clearFieldsExceptProductID();
            txtProductId.setText(getNextProductId());
        });
        timer.setRepeats(false);
        timer.start();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


private void clearProductPreview() {
    lblPrevId.setText("");
    lblPrevName.setText("");
    lblPrevCategory.setText("");
    lblPrevQty.setText("");
    lblPrevPrice.setText("");
    lblPrevSupplier.setText("");
    lblPrevLocation.setText("");
    lblQR.setIcon(null);
}


    
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        
    String productId = txtProductId.getText().trim();
    String name = txtProductName.getText().trim();
    String category = txtCategory.getText().trim();
    String quantityStr = txtQuantity.getText().trim();
    String priceStr = txtUnitPrice.getText().trim();
    String supplier = txtSupplier.getText().trim();
    String location = txtLocation.getText().trim();
    String updatedBy = "Admin";

    if (productId.isEmpty() || name.isEmpty() || category.isEmpty() ||
        quantityStr.isEmpty() || priceStr.isEmpty() || supplier.isEmpty() || location.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill all required fields!");
        return;
    }

    int quantity;
    double price;

    try {
        quantity = Integer.parseInt(quantityStr);
        price = Double.parseDouble(priceStr);
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid quantity or price!");
        return;
    }

    try (Connection conn = DBConnection.getConnection()) {
        conn.setAutoCommit(false);

        String checkSql = "SELECT quantity FROM products WHERE product_id = ?";

        try (PreparedStatement checkPst = conn.prepareStatement(checkSql)) {
            checkPst.setString(1, productId);

            try (ResultSet rs = checkPst.executeQuery()) {

                if (rs.next()) {
                    int currentQty = rs.getInt("quantity");
                    int newQty = currentQty + quantity;

                    String updateSql = "UPDATE products SET quantity=?, unit_price=?, supplier=?, location=?, updated_by=? WHERE product_id=?";
                    try (PreparedStatement pst = conn.prepareStatement(updateSql)) {
                        pst.setInt(1, newQty);
                        pst.setDouble(2, price);
                        pst.setString(3, supplier);
                        pst.setString(4, location);
                        pst.setString(5, updatedBy);
                        pst.setString(6, productId);
                        pst.executeUpdate();
                    }

                } else {
                    String insertSql = "INSERT INTO products (product_id, product_name, category, quantity, unit_price, supplier, location, updated_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement pst = conn.prepareStatement(insertSql)) {
                        pst.setString(1, productId);
                        pst.setString(2, name);
                        pst.setString(3, category);
                        pst.setInt(4, quantity);
                        pst.setDouble(5, price);
                        pst.setString(6, supplier);
                        pst.setString(7, location);
                        pst.setString(8, updatedBy);
                        pst.executeUpdate();
                    }
                }
            }
        }

        conn.commit();

// AUTO GENERATE QR CODE
// AUTO GENERATE QR CODE - Move this INSIDE the try and BEFORE commit
    try {
        generateQRCodeForProduct(conn, productId, name); // Pass 'conn' here
    } catch (Exception ex) {
        // If QR fails, we might still want to commit the product, 
        // or you can throw ex to rollback everything.
        System.err.println("QR Generation Error: " + ex.getMessage());
    }

    // Now commit everything (Product + QR Path) at once
    conn.commit(); 
    
    JOptionPane.showMessageDialog(this, "Product saved and QR Code generated successfully!");
    
    clearAllFields(); 
    txtProductId.setText(getNextProductId());

} catch (Exception e) {
    // If anything fails before commit, it rolls back
    e.printStackTrace();
    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
   }
        clearAllFields();
        txtProductId.setText(getNextProductId());

  

    }//GEN-LAST:event_btnAddActionPerformed

    private void txtUnitPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUnitPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUnitPriceActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        
    String productId = txtProductId.getText().trim();
    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) return;

    try (Connection conn = DBConnection.getConnection()) {
        conn.setAutoCommit(false);

        // 1️⃣ Get current quantity before deletion
        int quantity = 0;
        String sqlSelect = "SELECT quantity FROM products WHERE product_id=?";
        try (PreparedStatement pst = conn.prepareStatement(sqlSelect)) {
            pst.setString(1, productId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) quantity = rs.getInt("quantity");
        }

        // 2️⃣ Insert OUT record into stock_history
        if (quantity > 0) {
            String sqlHistory = "INSERT INTO stock_history (product_id, action_type, quantity, date_time, user, remarks) VALUES (?, 'OUT', ?, NOW(), ?, ?)";
            try (PreparedStatement pst = conn.prepareStatement(sqlHistory)) {
                pst.setString(1, productId);
                pst.setInt(2, quantity);
                pst.setString(3, "Admin");
                pst.setString(4, "Product deleted");
                pst.executeUpdate();
            }
        }

        // 3️⃣ Delete product
        String sqlDelete = "DELETE FROM products WHERE product_id=?";
        try (PreparedStatement pst = conn.prepareStatement(sqlDelete)) {
            pst.setString(1, productId);
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Product deleted successfully!");
                this.dispose(); // Close form after deletion
            }
        }

        conn.commit();
        conn.setAutoCommit(true);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error deleting product: " + e.getMessage());
    }

    }//GEN-LAST:event_btnDeleteActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new ProductForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AddProPanel;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnGenerateQR;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblDash;
    private javax.swing.JLabel lblInventory;
    private javax.swing.JLabel lblLogout;
    private javax.swing.JLabel lblPayment;
    private javax.swing.JLabel lblPrevCategory;
    private javax.swing.JLabel lblPrevId;
    private javax.swing.JLabel lblPrevLocation;
    private javax.swing.JLabel lblPrevName;
    private javax.swing.JLabel lblPrevPrice;
    private javax.swing.JLabel lblPrevQty;
    private javax.swing.JLabel lblPrevSupplier;
    private javax.swing.JLabel lblProducts;
    private javax.swing.JLabel lblQR;
    private javax.swing.JLabel lblQRScan;
    private javax.swing.JLabel lblReports;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSettings;
    private javax.swing.JLabel lblStock;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblUsers;
    private javax.swing.JTextField txtCategory;
    private javax.swing.JTextField txtLocation;
    private javax.swing.JTextField txtProductId;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtSupplier;
    private javax.swing.JTextField txtUnitPrice;
    // End of variables declaration//GEN-END:variables
}
