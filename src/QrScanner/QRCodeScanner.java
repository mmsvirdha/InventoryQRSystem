/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package QrScanner;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import dashboard.Dashboard;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import login.LoginForm;
import Payment.PaymentForm;
import products.ProductForm;
import reports.ReportForm;
import Settings.SettingsForm;
import inventory.StockInOutForm;
import javax.swing.JLabel;
import users.userForm;
import utils.DBConnection;

import javax.swing.*;
import java.awt.*;
import utils.TransitionHelper;

/**
 *
 * @author ADMIN
 */
public class QRCodeScanner extends javax.swing.JFrame implements Runnable {
    
    // If you have a custom AnimatedScannerPanel class that has startScanning/stopScanning,
    // declare it here. Otherwise use WebcamPanel as below.
    private Webcam webcam = null;
    private WebcamPanel webcamPanel = null;
    // If you *do* have AnimatedScannerPanel class with startScanning/stopScanning, use that type:
    // private AnimatedScannerPanel animatedPanel;
    private javax.swing.JPanel animatedPanelContainer = null; // the Swing placeholder panel from initComponents()

       // custom scanner panel
    private AnimatedScannerPanel myAnimatedPanel; 
    private boolean isScanning = false;
    private Thread scannerThread = null;
    
    private String username;
    private String role;
    
    
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(QRCodeScanner.class.getName());

    /**
     * Creates new form QRCodeScanner
     */
    
  

    public QRCodeScanner() {
        initComponents();
        
        
        // Maximizes the window to full screen size
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
       // Debug: Check animatedPanel
    System.out.println("animatedPanel null? " + (animatedPanel == null));
    System.out.println("animatedPanel size: " + animatedPanel.getSize());
    
    // Add shutdown hook to force close webcam
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown hook: Closing webcam...");
            if (webcamPanel != null) {
                webcamPanel.stop();
            }
            if (webcam != null && webcam.isOpen()) {
                webcam.close();
            }
        }));
    
  
    
    // 1. Initialize the custom panel (myAnimatedPanel) and place it in the animatedPanel container.
    // NOTE: This runs first and shows the animated panel initially.
    initializeAnimatedPanel();
  
   // 2. Set up the webcam. 
    // NOTE: This removes myAnimatedPanel and replaces it with webcamPanel.
    setupWebcam();
        
        activateAllLinks();
        
      
        
        // Force a repaint after everything is set up
    SwingUtilities.invokeLater(() -> {
        animatedPanel.revalidate();
        animatedPanel.repaint();
        System.out.println("Final animatedPanel size: " + animatedPanel.getSize());
        System.out.println("myAnimatedPanel size: " + myAnimatedPanel.getSize());
    });
        



    }
      
    
   public QRCodeScanner(String username, String role) {
       
       this.username = username;
    this.role = role;
    
    initComponents(); 
    getNextProductId();
    
lblUsername.setText(capitalizeFirstLetter(username));
    lblRole.setText(formatRole(role));

    
     // Maximizes the window to full screen size
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
    // Populate the dropdown list
    populateIssuedToCombo();

    // Add action listener for the Issue Stock button
    btnIssueStock.addActionListener(e -> performStockIssue());
    
    // Remove Black Rectangle Completely
    CustomPanel.setOpaque(false);
    CustomPanel.setBorder(null);
    
    ScannerPanel.setOpaque(false);
    ScannerPanel.setBorder(null);
    
    ProDetailsPanel.setOpaque(false);
    ProDetailsPanel.setBorder(null);
    
    // Maximizes the window to full screen size
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    
    // Debug: Check animatedPanel
    System.out.println("animatedPanel null? " + (animatedPanel == null));
    System.out.println("animatedPanel size: " + animatedPanel.getSize());
    
    // Add shutdown hook to force close webcam
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        System.out.println("Shutdown hook: Closing webcam...");
        if (webcamPanel != null) {
            webcamPanel.stop();
        }
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
    }));
    
    // Initialize animated panel and webcam
    initializeAnimatedPanel();
    setupWebcam();
    
    activateAllLinks();
    
       
              
TransitionHelper.applyTealHoverSmooth(lblQRScan);
TransitionHelper.applyTealHoverSmooth(lblReports);
TransitionHelper.applyTealHoverSmooth(lblSettings);
TransitionHelper.applyTealHoverSmooth(lblUsers);
TransitionHelper.applyTealHoverSmooth(lblDash);
TransitionHelper.applyTealHoverSmooth(lblInventory);
TransitionHelper.applyTealHoverSmooth(lblLogout);

    
    // Force a repaint after everything is set up
    SwingUtilities.invokeLater(() -> {
        animatedPanel.revalidate();
        animatedPanel.repaint();
        System.out.println("Final animatedPanel size: " + animatedPanel.getSize());
        System.out.println("myAnimatedPanel size: " + myAnimatedPanel.getSize());
    });
    
    
    
btnStartScan.addActionListener(e -> {
    if (webcam == null) {
        JOptionPane.showMessageDialog(this, "No webcam detected!", "Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (!isScanning) {
        isScanning = true;
        lblStatus.setText("Scanning...");
        
        // Start button
myAnimatedPanel.startScanning();


        if (!webcam.isOpen()) webcam.open();

        scannerThread = new Thread(this); // your QR scanning loop
        scannerThread.start();

        btnStartScan.setEnabled(false);
        btnStopScan.setEnabled(true);
    }
});



btnStopScan.addActionListener(e -> {
    isScanning = false;
    lblStatus.setText("Scanning stopped");
   // Stop button
myAnimatedPanel.stopScanning();


    if (scannerThread != null) scannerThread.interrupt();
    if (webcam != null) webcam.close();

    btnStartScan.setEnabled(true);
    btnStopScan.setEnabled(false);
});


}
   
   private void populateIssuedToCombo() {
    cmbIssuedTo.removeAllItems();
    // Add your departments, staff, or locations here
    cmbIssuedTo.addItem("Production Floor");
    cmbIssuedTo.addItem("Sales Department");
    cmbIssuedTo.addItem("Maintenance Team");
    cmbIssuedTo.addItem("Damaged/Write-off");
    cmbIssuedTo.addItem("Internal Use");
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
                QRCodeScanner.this,
                "Do you really want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                TransitionHelper.fadeToFrame(QRCodeScanner.this, new LoginForm(), 500);
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
 
 
    
private void initializeAnimatedPanel() {
    // Create your custom AnimatedScannerPanel
    myAnimatedPanel = new AnimatedScannerPanel();
    myAnimatedPanel.setPreferredSize(new Dimension(325, 220));
    myAnimatedPanel.setMinimumSize(new Dimension(325, 220));
    
    // Set up the container panel
    animatedPanel.removeAll();
    animatedPanel.setLayout(new BorderLayout());
    animatedPanel.setBackground(new Color(26, 26, 46));
    animatedPanel.add(myAnimatedPanel, BorderLayout.CENTER);
    animatedPanel.revalidate();
    animatedPanel.repaint();
    
    System.out.println("Animated panel initialized and added");
}
    
    private void setupWebcam() {
        try {
            if (webcam != null && webcam.isOpen()) {
                webcam.close();
            }
            
            if (webcamPanel != null) {
                webcamPanel.stop();
                webcamPanel = null;
            }
            
            webcam = Webcam.getDefault();
            
            if (webcam == null) {
                JOptionPane.showMessageDialog(this, 
                    "No webcam detected! Please use 'Upload QR Image' instead.", 
                    "Camera Not Available", 
                    JOptionPane.WARNING_MESSAGE);
                myAnimatedPanel.stopScanning();
                return;
            }
            
            // Add this when the webcam panel is created and added:
if (myAnimatedPanel != null) {
    myAnimatedPanel.stopScanning(); 
}
            
            // Use smallest supported webcam resolution
          // Use QVGA resolution (320x240) which is a standard format 
        // and close to your display panel size (325x220).
        Dimension size = WebcamResolution.QVGA.getSize();
           webcam.setViewSize(size);

           webcamPanel = new WebcamPanel(webcam);
           webcamPanel.setPreferredSize(new Dimension(325, 220)); // Display size
            webcamPanel.setFPSDisplayed(false);
            webcamPanel.setMirrored(false);
            
            animatedPanel.removeAll();
            animatedPanel.setLayout(new BorderLayout());
            animatedPanel.add(webcamPanel, BorderLayout.CENTER);
            animatedPanel.revalidate();
            animatedPanel.repaint();
            
            System.out.println("Webcam setup complete");
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error initializing webcam: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            myAnimatedPanel.stopScanning();
        }
    }
    
    
   @Override
    public void run() {
        while (isScanning) {
            try {
                if (webcam != null && webcam.isOpen()) {
                    BufferedImage image = webcam.getImage();
                    if (image != null) {
                        String qrCode = decodeQRCode(image);
                        if (qrCode != null && !qrCode.isEmpty()) {
                            final String foundCode = qrCode;
                            isScanning = false;
                            
                            SwingUtilities.invokeLater(() -> {
                                lblStatus.setText("QR Code Detected!");
                                System.out.println("QR Code found: " + foundCode);
                                processQRCode(foundCode);
                                stopScanning();
                            });
                            break;
                        }
                    }
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    // Decode QR Code from image
    private String decodeQRCode(BufferedImage image) {
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            return null;
        }
    }

    private String extractProductId(String qrText) {
    // If QR code contains "Product ID: P005" format
    if (qrText.contains("Product ID:")) {
        String[] lines = qrText.split("\n");
        for (String line : lines) {
            if (line.trim().startsWith("Product ID:")) {
                return line.replace("Product ID:", "").trim();
            }
        }
    }
    
    // If QR code is just the product ID (e.g., "P005")
    return qrText.trim();
}
    
   private void scanQRCode() {
    while (true) {
        try {
            if (webcam != null) {
                BufferedImage image = webcam.getImage();
                if (image != null) {
                    LuminanceSource source = new BufferedImageLuminanceSource(image);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                    try {
                        Result result = new MultiFormatReader().decode(bitmap);
                        if (result != null) {
                            System.out.println("QR Code: " + result.getText());
                            // Update your JTextField in Swing thread
                            SwingUtilities.invokeLater(() -> {
                                txtInvoiceId.setText(result.getText());
                                // Optionally stop webcam after scan
                                stopWebcam();
                            });
                        }
                    } catch (NotFoundException e) {
                        // QR code not found in this frame, ignore
                    }
                }
            }
            Thread.sleep(100); // small delay to reduce CPU usage
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

   private String getNextProductId() {
    String maxId = "P001"; // Default starting ID

    try (Connection conn = DBConnection.getConnection()) {
        // Query to find the maximum existing product ID that starts with 'P'
        String sql = "SELECT MAX(product_id) AS max_id FROM products WHERE product_id LIKE 'P%'";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            String currentMaxId = rs.getString("max_id");
            if (currentMaxId != null && currentMaxId.startsWith("P")) {
                // Extract the number part, e.g., "005" from "P005"
                String numberPart = currentMaxId.substring(1);
                
                try {
                    int nextNumber = Integer.parseInt(numberPart) + 1;
                    // Format back to P00X (e.g., 6 -> P006)
                    maxId = "P" + String.format("%03d", nextNumber);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing numeric part of product ID: " + numberPart);
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
                "Error retrieving next Product ID: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }
    return maxId;
}
 

   private void processQRCode(String qrCode) {
        if (qrCode == null || qrCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "QR Code is empty!", 
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String cleanQRCode = qrCode.trim();
System.out.println("Processing QR Code: [" + cleanQRCode + "]");

// It updates the UI field immediately.
    txtInvoiceId.setText(cleanQRCode);

// Extract Product ID if QR contains multiple lines
String productId = extractProductId(cleanQRCode);
System.out.println("Extracted Product ID: [" + productId + "]");

try (Connection conn = DBConnection.getConnection()) {
    String sql = "SELECT product_id, product_name, unit_price, quantity, category, supplier " +
                 "FROM products WHERE product_id = ?";
    PreparedStatement pst = conn.prepareStatement(sql);
    pst.setString(1, productId);
            
            System.out.println("Executing SQL with product_id: " + cleanQRCode);
            
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                System.out.println("Product found in database!");
                
                txtProductName.setText(rs.getString("product_name"));
                txtProductId.setText(rs.getString("product_id"));
                txtStock.setText(String.valueOf(rs.getInt("quantity")));
                txtPrice.setText(String.format("Rs. %.2f", rs.getDouble("unit_price")));
                txtCategory.setText(rs.getString("category"));
                txtSupplier.setText(rs.getString("supplier"));
                
                panelProductDetails.setVisible(true);
                
                JOptionPane.showMessageDialog(this, 
                    "Product Found!\nID: " + rs.getString("product_id") + 
                    "\nName: " + rs.getString("product_name"));
                
           } else {
    System.out.println("Product NOT found in database for ID: " + productId);

    int option = JOptionPane.showConfirmDialog(this,
        "Product not found!\nProduct ID: " + productId +
        "\nDo you want to add this product?",
        "Add Product",
        JOptionPane.YES_NO_OPTION);
                
    if (option == JOptionPane.YES_OPTION) {
        
        
        String nextId = getNextProductId(); // Get the generated ID (e.g., P006)
        
        // Pass the GENERATED ID to the new ProductForm

        ProductForm Form = new ProductForm(username, role, nextId); 
        
        Form.setVisible(true);
    }
}
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Error fetching product: " + e.getMessage());
        }
    }
   
    
  private void performStockIssue() {
    // 1. Get Input Data
    String productId = txtProductId.getText().trim();
    String productName = txtProductName.getText().trim();
    String currentStockStr = txtStock.getText().trim();
    String issueQtyStr = txtIssueQty.getText().trim();
    String issuedTo = (String) cmbIssuedTo.getSelectedItem();
    String issuedBy = this.username; //  currently logged-in username

    // 2. Validation
    if (productId.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please scan a product first.", "No Product Selected", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (issueQtyStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a quantity to issue.", "Input Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int currentStock = 0;
    int issueQty = 0;

    try {
        currentStock = Integer.parseInt(currentStockStr);
        issueQty = Integer.parseInt(issueQtyStr);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Quantity and Stock must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (issueQty <= 0) {
        JOptionPane.showMessageDialog(this, " Issue quantity must be greater than 0.", "Input Error", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (issueQty > currentStock) {
        JOptionPane.showMessageDialog(this, "Not enough stock! \nAvailable: " + currentStock + "\nRequested: " + issueQty, "Stock Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // 3. Confirm Action
    int confirm = JOptionPane.showConfirmDialog(this, 
        "Confirm issuing " + issueQty + " items of " + productName + " to " + issuedTo + "?",
        "Confirm Issue", JOptionPane.YES_NO_OPTION);

    if (confirm != JOptionPane.YES_OPTION) {
        return; // User cancelled
    }

    // 4. Database Update
    Connection conn = null;
    PreparedStatement pst = null;
    PreparedStatement pstHistory = null;

    try {
        conn = DBConnection.getConnection();
        conn.setAutoCommit(false); // Begin transaction

        // a) Update product quantity
        String sqlUpdate = "UPDATE products SET quantity = quantity - ? WHERE product_id = ?";
        pst = conn.prepareStatement(sqlUpdate);
        pst.setInt(1, issueQty);
        pst.setString(2, productId);
        int rowsAffected = pst.executeUpdate();

        if (rowsAffected <= 0) {
            conn.rollback();
            JOptionPane.showMessageDialog(this, "Failed to update product stock.", "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // b) Insert into stock_history
        String sqlHistory = "INSERT INTO stock_history (product_id, action_type, quantity, date_time, user, issued_to, issued_by, remarks) VALUES (?, ?, ?, NOW(), ?, ?, ?, ?)";
        pstHistory = conn.prepareStatement(sqlHistory);
        pstHistory.setString(1, productId);
        pstHistory.setString(2, "ISSUE"); // Action type
        pstHistory.setInt(3, issueQty);
        pstHistory.setString(4, issuedBy); // Could also be system username
        pstHistory.setString(5, issuedTo);
        pstHistory.setString(6, issuedBy);
        pstHistory.setString(7, "Issued " + issueQty + " of " + productName);

        pstHistory.executeUpdate();

        conn.commit(); // Commit transaction

        // 5. Success - Update UI
        int newStock = currentStock - issueQty;
        txtStock.setText(String.valueOf(newStock));
        txtIssueQty.setText("");

        JOptionPane.showMessageDialog(this, "✅ Stock issued successfully!\nRemaining Stock: " + newStock);

    } catch (Exception e) {
        try { if (conn != null) conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (pst != null) pst.close();
            if (pstHistory != null) pstHistory.close();
            if (conn != null) conn.setAutoCommit(true);
            if (conn != null) conn.close();
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}




private void stopScanning() {
        isScanning = false;
        lblStatus.setText("⏸️ Scanning stopped");
        
        if (scannerThread != null) {
            scannerThread.interrupt();
            scannerThread = null;
        }
        
        if (webcamPanel != null) {
            webcamPanel.stop();
        }
        
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
            webcam = null;
        }
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // Ignore
        }
        
        restoreAnimatedPanel();
        
        btnStartScan.setEnabled(true);
        btnStopScan.setEnabled(false);
        
        System.out.println("Scanning stopped, webcam closed");
    }
    
     
     private void restoreAnimatedPanel() {
    animatedPanel.removeAll();
    animatedPanel.setLayout(new BorderLayout());
    animatedPanel.add(myAnimatedPanel, BorderLayout.CENTER);
    
    // Start the scanning animation when the camera is off 🛑 MODIFIED
    myAnimatedPanel.startScanning(); 
    
    animatedPanel.revalidate();
    animatedPanel.repaint();
}

    @Override
    public void dispose() {
        if (isScanning) {
            isScanning = false;
        }
        
        if (webcamPanel != null) {
            webcamPanel.stop();
        }
        
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
        
        if (myAnimatedPanel != null) {
            myAnimatedPanel.stopScanning();
        }
        
        super.dispose();
        System.out.println("QRCodeScanner disposed");
    }
    
    private void stopWebcam() {
    if (webcam != null) {
        webcam.close();
        animatedPanel.removeAll();
        animatedPanel.revalidate();
        animatedPanel.repaint();
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

        jPanel6 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        CustomPanel = new utils.RoundedPanel(25);
        ScannerPanel = new utils.RoundedPanel(25);
        jLabel3 = new javax.swing.JLabel();
        animatedPanel = new utils.RoundedPanel(25);
        lblStatus = new javax.swing.JLabel();
        btnStartScan = new javax.swing.JButton();
        btnStopScan = new javax.swing.JButton();
        btnUploadQR = new javax.swing.JButton();
        txtInvoiceId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        ProDetailsPanel = new utils.RoundedPanel(25);
        jLabel4 = new javax.swing.JLabel();
        panelProductDetails = new utils.RoundedPanel(25);
        jLabel5 = new javax.swing.JLabel();
        txtProductName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtProductId = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        txtPrice = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtCategory = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtSupplier = new javax.swing.JTextField();
        btnViewDetails = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtIssueQty = new javax.swing.JTextField();
        btnIssueStock = new javax.swing.JButton();
        cmbIssuedTo = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
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
        jLabel21 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("QR Code Scanner - Inventory System"); // NOI18N

        jPanel6.setBackground(new java.awt.Color(59, 81, 108));

        jPanel1.setBackground(new java.awt.Color(59, 81, 108));

        CustomPanel.setBackground(new java.awt.Color(80, 108, 133));

        ScannerPanel.setBackground(new java.awt.Color(52, 73, 94));
        ScannerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Camera Scanner", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        ScannerPanel.setForeground(new java.awt.Color(255, 255, 255));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("📱 Scan QR Code");

        animatedPanel.setBackground(new java.awt.Color(0, 0, 0));
        animatedPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102)));

        javax.swing.GroupLayout animatedPanelLayout = new javax.swing.GroupLayout(animatedPanel);
        animatedPanel.setLayout(animatedPanelLayout);
        animatedPanelLayout.setHorizontalGroup(
            animatedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 323, Short.MAX_VALUE)
        );
        animatedPanelLayout.setVerticalGroup(
            animatedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 228, Short.MAX_VALUE)
        );

        lblStatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblStatus.setForeground(new java.awt.Color(255, 255, 255));
        lblStatus.setText("Ready to scan");

        btnStartScan.setBackground(new java.awt.Color(102, 126, 234));
        btnStartScan.setText(" Start Scan");
        btnStartScan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartScanActionPerformed(evt);
            }
        });

        btnStopScan.setBackground(new java.awt.Color(231, 76, 60));
        btnStopScan.setText("️ Stop Scan");
        btnStopScan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopScanActionPerformed(evt);
            }
        });

        btnUploadQR.setBackground(new java.awt.Color(46, 204, 113));
        btnUploadQR.setText("Upload QR");
        btnUploadQR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadQRActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ScannerPanelLayout = new javax.swing.GroupLayout(ScannerPanel);
        ScannerPanel.setLayout(ScannerPanelLayout);
        ScannerPanelLayout.setHorizontalGroup(
            ScannerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ScannerPanelLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(ScannerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ScannerPanelLayout.createSequentialGroup()
                        .addComponent(btnStartScan)
                        .addGap(18, 18, 18)
                        .addComponent(btnStopScan, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUploadQR))
                    .addComponent(lblStatus)
                    .addGroup(ScannerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ScannerPanelLayout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addGap(43, 43, 43)
                            .addComponent(txtInvoiceId, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(animatedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        ScannerPanelLayout.setVerticalGroup(
            ScannerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ScannerPanelLayout.createSequentialGroup()
                .addGroup(ScannerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ScannerPanelLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel3)
                        .addGap(31, 31, 31))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ScannerPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtInvoiceId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)))
                .addComponent(animatedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(lblStatus)
                .addGap(26, 26, 26)
                .addGroup(ScannerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStartScan)
                    .addComponent(btnStopScan)
                    .addComponent(btnUploadQR))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Scan product QR codes for instant inventory information");

        ProDetailsPanel.setBackground(new java.awt.Color(52, 73, 94));
        ProDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Product Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("📊 Scan Results");

        jLabel5.setText("Product Name:");

        txtProductName.setEditable(false);
        txtProductName.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel6.setText("Product Id:");

        txtProductId.setEditable(false);

        jLabel9.setText("Stock Quantity:");

        txtStock.setEditable(false);
        txtStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStockActionPerformed(evt);
            }
        });

        txtPrice.setEditable(false);

        jLabel10.setText("Price");

        jLabel11.setText("Category");

        txtCategory.setEditable(false);

        jLabel12.setText("Supplier");

        txtSupplier.setEditable(false);

        btnViewDetails.setBackground(new java.awt.Color(52, 152, 219));
        btnViewDetails.setText(" View Full Details");
        btnViewDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewDetailsActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(102, 126, 234));
        jButton2.setText("️ Update Stock");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelProductDetailsLayout = new javax.swing.GroupLayout(panelProductDetails);
        panelProductDetails.setLayout(panelProductDetailsLayout);
        panelProductDetailsLayout.setHorizontalGroup(
            panelProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProductDetailsLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(panelProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelProductDetailsLayout.createSequentialGroup()
                        .addComponent(btnViewDetails)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2))
                    .addGroup(panelProductDetailsLayout.createSequentialGroup()
                        .addGroup(panelProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addGap(55, 55, 55)
                        .addGroup(panelProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtProductName)
                            .addComponent(txtProductId)
                            .addComponent(txtStock)
                            .addComponent(txtPrice)
                            .addComponent(txtCategory)
                            .addComponent(txtSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE))))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        panelProductDetailsLayout.setVerticalGroup(
            panelProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProductDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(txtProductId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelProductDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnViewDetails)
                    .addComponent(jButton2))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ProDetailsPanelLayout = new javax.swing.GroupLayout(ProDetailsPanel);
        ProDetailsPanel.setLayout(ProDetailsPanelLayout);
        ProDetailsPanelLayout.setHorizontalGroup(
            ProDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProDetailsPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(ProDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ProDetailsPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(panelProductDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        ProDetailsPanelLayout.setVerticalGroup(
            ProDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelProductDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(52, 73, 94));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Issue Stock", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel7.setText("Issue Quantity");

        jLabel8.setText("Issued To");

        btnIssueStock.setBackground(new java.awt.Color(59, 81, 108));
        btnIssueStock.setForeground(new java.awt.Color(255, 255, 255));
        btnIssueStock.setText("Issue");
        btnIssueStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIssueStockActionPerformed(evt);
            }
        });

        cmbIssuedTo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(97, 97, 97)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIssueQty)
                            .addComponent(cmbIssuedTo, 0, 153, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnIssueStock)))
                .addGap(25, 25, 25))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIssueQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbIssuedTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addComponent(btnIssueStock)
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout CustomPanelLayout = new javax.swing.GroupLayout(CustomPanel);
        CustomPanel.setLayout(CustomPanelLayout);
        CustomPanelLayout.setHorizontalGroup(
            CustomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(CustomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(CustomPanelLayout.createSequentialGroup()
                        .addComponent(ScannerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(CustomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(CustomPanelLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(ProDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        CustomPanelLayout.setVerticalGroup(
            CustomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CustomPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CustomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(CustomPanelLayout.createSequentialGroup()
                        .addComponent(ProDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(ScannerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addComponent(CustomPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CustomPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("QR Code Scanner");

        jPanel5.setBackground(new java.awt.Color(52, 73, 94));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Inventory QR");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Management System");

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

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/InventoryImages/icons8-logout-32.png"))); // NOI18N

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
                    .addComponent(jLabel21))
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
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
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
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14))
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
                    .addComponent(jLabel21)
                    .addComponent(lblLogout))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartScanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartScanActionPerformed
        // TODO add your handling code here:
        
    if (!isScanning) {
            myAnimatedPanel.startScanning();
            lblStatus.setText("Initializing camera...");
            
            SwingUtilities.invokeLater(() -> {
                setupWebcam();
                
                if (webcam != null) {
                    isScanning = true;
                    lblStatus.setText("Scanning for QR codes...");
                    
                    scannerThread = new Thread(this);
                    scannerThread.start();
                    
                    btnStartScan.setEnabled(false);
                    btnStopScan.setEnabled(true);
                }
            });
        }
    }//GEN-LAST:event_btnStartScanActionPerformed

    private void btnUploadQRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadQRActionPerformed
        // TODO add your handling code here:
        
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select QR Code Image");
    fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "bmp", "gif"));
    
    int result = fileChooser.showOpenDialog(this);
    
    if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        
        try {
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            
            BufferedImage image = ImageIO.read(selectedFile);
            
            if (image == null) {
                System.out.println("Failed to read image!");
                JOptionPane.showMessageDialog(this, 
                    "Failed to read image file!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            System.out.println("Image loaded: " + image.getWidth() + "x" + image.getHeight());
            
            String qrCode = decodeQRCode(image);
            
            if (qrCode != null && !qrCode.isEmpty()) {
                System.out.println("QR Code decoded: [" + qrCode + "]");
                System.out.println("Searching database for product ID: " + qrCode.trim());
                
                lblStatus.setText("QR Code Detected from Image!");
                processQRCode(qrCode);
            } else {
                System.out.println("No QR code found in image");
                JOptionPane.showMessageDialog(this, 
                    "No QR code found in the selected image!", 
                    "Not Found", 
                    JOptionPane.WARNING_MESSAGE);
                lblStatus.setText("No QR code found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading file: " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Error reading image file: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    } else {
        System.out.println("File selection cancelled");
    }

    }//GEN-LAST:event_btnUploadQRActionPerformed

    private void txtStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStockActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        
        String productId = txtProductId.getText();
    if (!productId.isEmpty()) {
        // Navigate to Stock Update page
        new StockInOutForm().setVisible(true);
        dispose();
    }
    }//GEN-LAST:event_jButton2ActionPerformed

   
    private void btnStopScanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopScanActionPerformed
        // TODO add your handling code here:
        
         stopScanning();
    }//GEN-LAST:event_btnStopScanActionPerformed

    private void btnViewDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewDetailsActionPerformed
        // TODO add your handling code here:
        
        String productId = txtProductId.getText();
    if (!productId.isEmpty()) {
        // Navigate to Stock Update page
        new StockInOutForm().setVisible(true);
        dispose();
    }
    }//GEN-LAST:event_btnViewDetailsActionPerformed

    private void btnIssueStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIssueStockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIssueStockActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new QRCodeScanner().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CustomPanel;
    private javax.swing.JPanel ProDetailsPanel;
    private javax.swing.JPanel ScannerPanel;
    private javax.swing.JPanel animatedPanel;
    private javax.swing.JButton btnIssueStock;
    private javax.swing.JButton btnStartScan;
    private javax.swing.JButton btnStopScan;
    private javax.swing.JButton btnUploadQR;
    private javax.swing.JButton btnViewDetails;
    private javax.swing.JComboBox<String> cmbIssuedTo;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JLabel lblDash;
    private javax.swing.JLabel lblInventory;
    private javax.swing.JLabel lblLogout;
    private javax.swing.JLabel lblQRScan;
    private javax.swing.JLabel lblReports;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSettings;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblUsers;
    private javax.swing.JPanel panelProductDetails;
    private javax.swing.JTextField txtCategory;
    private javax.swing.JTextField txtInvoiceId;
    private javax.swing.JTextField txtIssueQty;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtProductId;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtSupplier;
    // End of variables declaration//GEN-END:variables
}
