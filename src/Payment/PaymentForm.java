/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Payment;

import java.sql.Statement;

import QrScanner.QRCodeScanner;
import Settings.SettingsForm;
import dao.PaymentDAO;
import dashboard.Dashboard;
import inventory.StockInOutForm;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import model.PaymentModel;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.io.File;

import javax.swing.*;
import com.itextpdf.text.Font;        // iText Font
import com.itextpdf.text.Font.FontFamily;

import utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import login.LoginForm;
import products.ProductForm;
import reports.ReportForm;
import users.userForm;
import utils.TransitionHelper;

/**
 *
 * @author ADMIN
 */
public class PaymentForm extends javax.swing.JFrame {
    
    private String purchaseId;
   private String supplierId;
private String referenceNo;


   private String username;
     private String role;
    
    // fields
private service.PaymentService paymentService = new service.PaymentService();     // Encapsulation: model.Payment uses private fields + getters/setters.

    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PaymentForm.class.getName());

    /**
     * Creates new form Payment
     */
    public PaymentForm() {
        initComponents();
        
        
}
    
      public PaymentForm (String username, String role) {  // Accept parameters
          
          this.username = username;
    this.role = role;
    
    initComponents();

        // Maximizes the window to full screen size
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    
    
 lblUsername.setText(capitalizeFirstLetter(username));
    lblRole.setText(formatRole(role));

    
     activateAllLinks();
     
       // Remove Black Rectangle Completely
        
headerPanel.setOpaque(false);
headerPanel.setBorder(null);

CustomPanel.setOpaque(false);
CustomPanel.setBorder(null);

SalesPanel.setOpaque(false);
SalesPanel.setBorder(null);

PaymentPanel.setOpaque(false);
PaymentPanel.setBorder(null);
        
        // Maximizes the window to full screen size
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Clear default empty rows from the table
    DefaultTableModel model = (DefaultTableModel) tblBill.getModel();
    model.setRowCount(0);  // This removes all default empty rows

     
             
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

          
        txtInvoiceId.setText(generatePaymentId());
    txtDate.setText(java.time.LocalDate.now().toString());
    
    // Load products into ComboBox
    loadProductNames();
        
        
         txtPaid.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            txtPaidKeyReleased(evt);
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
                PaymentForm.this,
                "Do you really want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                TransitionHelper.fadeToFrame(PaymentForm.this, new LoginForm(), 500);
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

    private String generatePaymentId() {
        return "PAY-" + System.currentTimeMillis();
        
    }
    
    


   private void loadProductNames() {
    try (Connection conn = utils.DBConnection.getConnection()) {
        String sql = "SELECT product_id, product_name FROM products ORDER BY product_name";
        PreparedStatement pst = conn.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("-- Select Product --");
        
        while (rs.next()) {
            String productId = rs.getString("product_id");
            String productName = rs.getString("product_name");
            String display = productName + " (" + productId + ")";
            model.addElement(display);
        }
        
        cmbProductSearch.setModel(model);
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage());
        e.printStackTrace();
    }
}
    
// recalc totals
private void recalcTotals() {
    double subtotal = 0;
    for (int i = 0; i < tblBill.getRowCount(); i++) {  // Use your JTable name
        Object value = tblBill.getValueAt(i, 4);        // Assuming Total column is index 4
        if (value != null) {
            try {
                subtotal += Double.parseDouble(value.toString());
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Skip non-numeric values
            }
        }
    }
    txtSubtotal.setText(String.format("%.2f", subtotal));
    txtTotal.setText(String.format("%.2f", subtotal));
}

 // Calculate Balance
private void txtPaidKeyReleased(java.awt.event.KeyEvent evt) {
    try {
        double paid = Double.parseDouble(txtPaid.getText().trim());
        double total = Double.parseDouble(txtTotal.getText());
        double balance = total - paid ;
        txtBalance.setText(String.format("%.2f", balance));
    } catch (Exception e) {
        txtBalance.setText("");
    }
}

// Handles the payment saving process
private void savePayment() {
    // Create PaymentModel object
   PaymentModel payment = new PaymentModel(
        txtInvoiceId.getText().trim(),  // paymentId
        txtDate.getText().trim(),       // date
        supplierId,                     // supplierId
        purchaseId,                     // purchaseId
        Double.parseDouble(txtTotal.getText().trim()), // totalAmount
        cmbPaymentMethod.getSelectedItem().toString(), // paymentMethod
        referenceNo                     // referenceNo
);
   
    String sql = "INSERT INTO payments "
               + "(payment_id, product_id, quantity, total_amount, payment_method, payment_date) "
               + "VALUES (?, ?, ?, ?, ?, NOW())";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, payment.getPaymentId());
        pst.setString(2, payment.getProductId());
        pst.setInt(3, payment.getQuantity());
        pst.setDouble(4, payment.getAmount());
        pst.setString(5, payment.getPaymentMethod());

        pst.executeUpdate();
        JOptionPane.showMessageDialog(this, "Payment completed successfully!");

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error completing payment!");
    }
}

private void saveBillAsPDF() {
    String invoiceId = txtInvoiceId.getText().trim();
    String productId = txtProductId.getText().trim();
    String date = txtDate.getText().trim();
    String paymentMethod = (cmbPaymentMethod.getSelectedItem() != null) ? cmbPaymentMethod.getSelectedItem().toString() : "";

    String dirPath = "C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\Bills\\";
    String pdfFileName = "Invoice_" + invoiceId + "_Product_" + productId + ".pdf";
    String pdfPath = dirPath + pdfFileName;

    BaseColor themeColor = new BaseColor(26, 115, 232);

    Document document = new Document(PageSize.A4, 36, 36, 54, 54);
    try {
        File dir = new File(dirPath);
        if (!dir.exists()) dir.mkdirs();

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
        document.open();

        // Border
        PdfContentByte canvas = writer.getDirectContent();
        canvas.setColorStroke(themeColor);
        canvas.setLineWidth(2f);
        Rectangle rect = new Rectangle(
                document.left(), document.bottom(),
                document.right(), document.top()
        );
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(2f);
        rect.setBorderColor(themeColor);
        canvas.rectangle(rect);
        canvas.stroke();

        // ---------- Header ----------
        PdfPTable headerTable = new PdfPTable(new float[]{1f, 3f});
        headerTable.setWidthPercentage(95);
        headerTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerTable.setSpacingAfter(20f); // Increased from 12f
        headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        String logoPath = "src/InventoryImages/logo.jpg";  
        Image logo = Image.getInstance(logoPath);
        logo.scaleToFit(60, 60);
        logo.setAlignment(Image.ALIGN_CENTER);

        PdfPCell logoCell = new PdfPCell(logo, false);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        logoCell.setPadding(5f);
        headerTable.addCell(logoCell);

        PdfPCell compCell = new PdfPCell();
        compCell.setBorder(Rectangle.NO_BORDER);
        compCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Font companyNameFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, themeColor);
        Paragraph companyPara = new Paragraph("VM Smart QR System", companyNameFont);
        companyPara.setSpacingAfter(4f);

        Font companySubFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.GRAY);
        Paragraph subPara = new Paragraph("Professional Inventory & QR Billing", companySubFont);

        compCell.addElement(companyPara);
        compCell.addElement(subPara);
        headerTable.addCell(compCell);

        document.add(headerTable);

        // ---------- Invoice Info Box ----------
        PdfPTable infoBox = new PdfPTable(2);
        infoBox.setWidthPercentage(95);
        infoBox.setHorizontalAlignment(Element.ALIGN_CENTER);
        infoBox.setSpacingAfter(20f); // Increased from 12f
        infoBox.setWidths(new float[]{1f, 1f});

        Font labelFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font valueFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

        PdfPCell left = new PdfPCell();
        left.setBorder(Rectangle.NO_BORDER);
        left.addElement(new Phrase("Invoice ID: " + invoiceId, valueFont));
        left.addElement(new Phrase("Product ID: " + productId, valueFont));
        left.addElement(new Phrase("Date: " + date, valueFont));
        infoBox.addCell(left);

        PdfPCell right = new PdfPCell();
        right.setBorder(Rectangle.NO_BORDER);
        right.setHorizontalAlignment(Element.ALIGN_RIGHT);
        right.addElement(new Phrase("Payment Method: " + paymentMethod, valueFont));
        infoBox.addCell(right);

        document.add(infoBox);

        // ---------- Items Table ----------
        int colCount = tblBill.getColumnCount();
        PdfPTable table = new PdfPTable(colCount);
        table.setWidthPercentage(95); 
        table.setHorizontalAlignment(Element.ALIGN_CENTER); 
        table.setSpacingBefore(10f); // Space
        table.setSpacingAfter(15f); // Space

        Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
        for (int c = 0; c < colCount; c++) {
            PdfPCell h = new PdfPCell(new Phrase(tblBill.getColumnName(c), tableHeaderFont));
            h.setBackgroundColor(themeColor);
            h.setPadding(8f);
            h.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(h);
        }

        Font tableCellFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        for (int r = 0; r < tblBill.getRowCount(); r++) {
            for (int c = 0; c < colCount; c++) {
                Object val = tblBill.getValueAt(r, c);
                String text = (val != null) ? val.toString() : "";
                PdfPCell cell = new PdfPCell(new Phrase(text, tableCellFont));
                cell.setPadding(8f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }
        }
        document.add(table);

        // ---------- Totals Block ----------
        PdfPTable totalsTable = new PdfPTable(new float[]{3f, 1f});
        totalsTable.setWidthPercentage(95);
        totalsTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalsTable.setSpacingBefore(10f); // Increased from 6f
        totalsTable.setSpacingAfter(10f); 
        
        

        Font totalsLabelFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        Font totalsValueFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

        totalsTable.addCell(createTotalsCell("Subtotal:", totalsLabelFont, Element.ALIGN_RIGHT, false));
        totalsTable.addCell(createTotalsCell(txtSubtotal.getText(), totalsValueFont, Element.ALIGN_RIGHT, false));

        totalsTable.addCell(createTotalsCell("Total:", totalsLabelFont, Element.ALIGN_RIGHT, false));
        totalsTable.addCell(createTotalsCell(txtTotal.getText(), totalsValueFont, Element.ALIGN_RIGHT, false));

        totalsTable.addCell(createTotalsCell("Paid:", totalsLabelFont, Element.ALIGN_RIGHT, false));
        totalsTable.addCell(createTotalsCell(txtPaid.getText(), totalsValueFont, Element.ALIGN_RIGHT, false));

       // Add empty cell on left
PdfPCell emptyCell = new PdfPCell(new Phrase(""));
emptyCell.setBorder(Rectangle.NO_BORDER);
emptyCell.setFixedHeight(8f);
totalsTable.addCell(emptyCell);

// Add short line cell on right only
PdfPCell separatorCell = new PdfPCell();
separatorCell.setBorder(Rectangle.TOP);
separatorCell.setBorderWidthTop(1f);
separatorCell.setBorderColorTop(BaseColor.LIGHT_GRAY);
separatorCell.setFixedHeight(8f);
separatorCell.setPaddingLeft(35f);  // Adjust this to control line length
separatorCell.setPaddingRight(30f); // Adjust this to control line length
totalsTable.addCell(separatorCell);

        totalsTable.addCell(createTotalsCell("Balance:", totalsLabelFont, Element.ALIGN_RIGHT, true));
        totalsTable.addCell(createTotalsCell(txtBalance.getText(), totalsValueFont, Element.ALIGN_RIGHT, true));

        document.add(totalsTable);

        // ---------- QR Code Generation ----------
String qrData = String.format("InvoiceID:%s | ProductID:%s | Total:%s | Date:%s",
        invoiceId, productId, txtTotal.getText(), date);

BarcodeQRCode barcodeQRCode = new BarcodeQRCode(qrData, 150, 150, null);
Image qrImage = barcodeQRCode.getImage();
qrImage.scaleAbsolute(90, 90);

// Create a single-column table for better vertical layout
PdfPTable qrTable = new PdfPTable(1);
qrTable.setWidthPercentage(28);
qrTable.setHorizontalAlignment(Element.ALIGN_LEFT);
qrTable.setSpacingBefore(20f);

// QR Image Cell
PdfPCell qrCell = new PdfPCell(qrImage, false);
qrCell.setBorder(Rectangle.NO_BORDER);
qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
qrCell.setPaddingBottom(8f);
qrTable.addCell(qrCell);

// Label Cell below QR
PdfPCell qrLabelCell = new PdfPCell();
qrLabelCell.setBorder(Rectangle.NO_BORDER);
qrLabelCell.setHorizontalAlignment(Element.ALIGN_CENTER);

Paragraph scanText = new Paragraph("Scan for invoice details", 
    new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, BaseColor.DARK_GRAY));
scanText.setAlignment(Element.ALIGN_CENTER);

Paragraph invoiceText = new Paragraph("Invoice: " + invoiceId, 
    new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD));
invoiceText.setAlignment(Element.ALIGN_CENTER);
invoiceText.setSpacingBefore(2f);

qrLabelCell.addElement(scanText);
qrLabelCell.addElement(invoiceText);
qrTable.addCell(qrLabelCell);

document.add(qrTable);

        // ---------- Footer ----------
        PdfPTable footerLine = new PdfPTable(1);
        footerLine.setWidthPercentage(95);
        footerLine.setHorizontalAlignment(Element.ALIGN_CENTER);
        footerLine.setSpacingBefore(120f); // Added spacing before footer line
        PdfPCell lineCell = new PdfPCell();
        lineCell.setBorder(Rectangle.BOTTOM);
        lineCell.setBorderColor(BaseColor.LIGHT_GRAY);
        lineCell.setFixedHeight(8f);
        lineCell.setBorderWidthBottom(1f);
        footerLine.addCell(lineCell);
        document.add(footerLine);

        Paragraph footer = new Paragraph("Thank you for your purchase!",
                new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, themeColor));
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(15f); // Increased from 6f
        document.add(footer);

        document.close();

        JOptionPane.showMessageDialog(this,
                "Invoice PDF saved successfully!\n\n" + pdfFileName,
                "PDF Saved",
                JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this,
                "Error saving PDF: " + ex.getMessage(),
                "PDF Error",
                JOptionPane.ERROR_MESSAGE);
    }
}

// Helper: create a totals cell with optional bold border on top for final row
private PdfPCell createTotalsCell(String text, Font font, int align, boolean topBorder) {
    PdfPCell cell = new PdfPCell(new Phrase(text, font));
    cell.setHorizontalAlignment(align);
    cell.setBorder(Rectangle.NO_BORDER);
    cell.setPadding(6f);
    cell.setPaddingRight(15f);
    if (topBorder) {
        cell.setPaddingTop(12f); // Space above the balance row
    }
    return cell;
}


// Reduces the stock after a payment
private void reduceStock(String productId, int quantity) {
    String sql = "UPDATE products SET quantity = quantity - ? WHERE product_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setInt(1, quantity);
        pst.setString(2, productId);

        int rowsAffected = pst.executeUpdate();
        
        if (rowsAffected == 0) {
            System.err.println("Warning: Product " + productId + " not found in database!");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error reducing stock for product " + productId);
    }
}

private void clearProductFields() {
    txtProductId.setText("");
    txtUnitPrice.setText("");
    txtStock.setText("");
    txtQty.setText("");
    cmbProductSearch.setSelectedIndex(0); // Reset to "-- Select Product --"
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        CustomPanel = new utils.RoundedPanel(25);
        SalesPanel = new utils.RoundedPanel(25);
        jLabel1 = new javax.swing.JLabel();
        txtInvoiceId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtProductId = new javax.swing.JTextField();
        btnScanOrLoad = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtUnitPrice = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtStock = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtQty = new javax.swing.JTextField();
        btnAddItem = new javax.swing.JButton();
        cmbProductSearch = new javax.swing.JComboBox<>();
        PaymentPanel = new utils.RoundedPanel(25);
        jLabel10 = new javax.swing.JLabel();
        txtSubtotal = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        cmbPaymentMethod = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        txtPaid = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtBalance = new javax.swing.JTextField();
        btnConfirmPayment = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBill = new javax.swing.JTable();
        cmbTransactionType = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        headerPanel = new utils.RoundedPanel(25);
        lblProducts = new javax.swing.JLabel();
        lblStock = new javax.swing.JLabel();
        lblPayment = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
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
        jLabel21 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel9.setBackground(new java.awt.Color(59, 81, 108));

        jPanel1.setBackground(new java.awt.Color(59, 81, 108));

        CustomPanel.setBackground(new java.awt.Color(80, 108, 133));

        SalesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Info"));

        jLabel1.setText("Invoice Id");

        txtInvoiceId.setEditable(false);
        txtInvoiceId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInvoiceIdActionPerformed(evt);
            }
        });

        jLabel2.setText("Date");

        jLabel3.setText("Product Id");

        txtProductId.setEditable(false);
        txtProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductIdActionPerformed(evt);
            }
        });

        btnScanOrLoad.setBackground(new java.awt.Color(59, 81, 108));
        btnScanOrLoad.setForeground(new java.awt.Color(255, 255, 255));
        btnScanOrLoad.setText("Scan Or Load");
        btnScanOrLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnScanOrLoadActionPerformed(evt);
            }
        });

        jLabel4.setText("Product Name");

        jLabel5.setText("Unit Price");

        txtUnitPrice.setEditable(false);

        jLabel6.setText("Stock");

        txtStock.setEditable(false);
        txtStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStockActionPerformed(evt);
            }
        });

        jLabel9.setText("Qty");

        txtQty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQtyActionPerformed(evt);
            }
        });

        btnAddItem.setBackground(new java.awt.Color(59, 81, 108));
        btnAddItem.setForeground(new java.awt.Color(255, 255, 255));
        btnAddItem.setText("Add Item");
        btnAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddItemActionPerformed(evt);
            }
        });

        cmbProductSearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout SalesPanelLayout = new javax.swing.GroupLayout(SalesPanel);
        SalesPanel.setLayout(SalesPanelLayout);
        SalesPanelLayout.setHorizontalGroup(
            SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SalesPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnScanOrLoad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addGroup(SalesPanelLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(33, 33, 33)
                        .addGroup(SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtUnitPrice)
                            .addComponent(txtStock)
                            .addComponent(txtQty)
                            .addComponent(txtProductId, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(SalesPanelLayout.createSequentialGroup()
                        .addGroup(SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(SalesPanelLayout.createSequentialGroup()
                                .addGroup(SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(33, 33, 33))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SalesPanelLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDate)
                            .addComponent(txtInvoiceId)
                            .addComponent(cmbProductSearch, 0, 264, Short.MAX_VALUE))))
                .addContainerGap())
        );
        SalesPanelLayout.setVerticalGroup(
            SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SalesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtInvoiceId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbProductSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnScanOrLoad)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProductId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtUnitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAddItem)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PaymentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Payment"));

        jLabel10.setText("Subtotal");

        txtSubtotal.setEditable(false);

        jLabel11.setText("Total");

        txtTotal.setEditable(false);

        jLabel12.setText("Payment Method");

        cmbPaymentMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "QR", "Cash ", "Card" }));

        jLabel13.setText("Paid");

        jLabel14.setText("Balance");

        txtBalance.setEditable(false);
        txtBalance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBalanceActionPerformed(evt);
            }
        });

        btnConfirmPayment.setBackground(new java.awt.Color(59, 81, 108));
        btnConfirmPayment.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirmPayment.setText("Confirm Payment");
        btnConfirmPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmPaymentActionPerformed(evt);
            }
        });

        btnPrint.setBackground(new java.awt.Color(59, 81, 108));
        btnPrint.setForeground(new java.awt.Color(255, 255, 255));
        btnPrint.setText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PaymentPanelLayout = new javax.swing.GroupLayout(PaymentPanel);
        PaymentPanel.setLayout(PaymentPanelLayout);
        PaymentPanelLayout.setHorizontalGroup(
            PaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PaymentPanelLayout.createSequentialGroup()
                        .addGroup(PaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(PaymentPanelLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(67, 67, 67))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PaymentPanelLayout.createSequentialGroup()
                                .addGroup(PaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(PaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(PaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                                .addComponent(txtSubtotal))
                            .addComponent(cmbPaymentMethod, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(PaymentPanelLayout.createSequentialGroup()
                            .addComponent(jLabel14)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                            .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PaymentPanelLayout.createSequentialGroup()
                            .addComponent(jLabel13)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtPaid, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PaymentPanelLayout.createSequentialGroup()
                        .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnConfirmPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 16, Short.MAX_VALUE))
        );
        PaymentPanelLayout.setVerticalGroup(
            PaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbPaymentMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addGroup(PaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrint)
                    .addComponent(btnConfirmPayment))
                .addGap(60, 60, 60))
        );

        tblBill.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Product ID", "Name", "Qty", "Unit Price", "Total"
            }
        ));
        jScrollPane1.setViewportView(tblBill);

        cmbTransactionType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Purchase ", "Sell" }));
        cmbTransactionType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTransactionTypeActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("Payment for");

        javax.swing.GroupLayout CustomPanelLayout = new javax.swing.GroupLayout(CustomPanel);
        CustomPanel.setLayout(CustomPanelLayout);
        CustomPanelLayout.setHorizontalGroup(
            CustomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomPanelLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(CustomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 749, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(CustomPanelLayout.createSequentialGroup()
                        .addComponent(SalesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(CustomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(PaymentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, CustomPanelLayout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbTransactionType, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        CustomPanelLayout.setVerticalGroup(
            CustomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomPanelLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(CustomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(SalesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(CustomPanelLayout.createSequentialGroup()
                        .addGroup(CustomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbTransactionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGap(28, 28, 28)
                        .addComponent(PaymentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        headerPanel.setBackground(new java.awt.Color(80, 108, 133));

        lblProducts.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblProducts.setForeground(new java.awt.Color(255, 255, 255));
        lblProducts.setText("Product ");

        lblStock.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblStock.setForeground(new java.awt.Color(255, 255, 255));
        lblStock.setText("Stock & Payment Informations");

        lblPayment.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPayment.setForeground(new java.awt.Color(255, 255, 255));
        lblPayment.setText("Payemnt ");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(lblProducts)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(CustomPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(29, 29, 29))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(CustomPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        jPanel2.setBackground(new java.awt.Color(52, 73, 94));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

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

        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/InventoryImages/icons8-logout-32.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30)
                    .addComponent(jLabel31)
                    .addComponent(jLabel32)
                    .addComponent(jLabel21))
                .addGap(46, 46, 46)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblInventory)
                    .addComponent(lblDash)
                    .addComponent(lblQRScan)
                    .addComponent(lblReports)
                    .addComponent(lblSettings)
                    .addComponent(lblUsers)
                    .addComponent(lblLogout))
                .addContainerGap(69, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 15, Short.MAX_VALUE)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(21, 21, 21))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8))
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel27)
                    .addComponent(lblDash))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblInventory))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblQRScan))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblReports))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel31)
                    .addComponent(lblSettings))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUsers))
                .addGap(60, 60, 60)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel21)
                    .addComponent(lblLogout))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed
        // TODO add your handling code here:
        
    try {
        // Validate Product Selection
        if (cmbProductSearch.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a product first.");
            return;
        }

        // Validate Product ID
        String productId = txtProductId.getText().trim();
        if (productId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Product ID is missing. Please scan or load a product.");
            return;
        }

        // Validate and Parse Quantity
        String qtyText = txtQty.getText().trim();
        if (qtyText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter quantity.");
            return;
        }
        
        int quantity;
        try {
            quantity = Integer.parseInt(qtyText);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for quantity.");
            return;
        }

        // Validate and Parse Unit Price
        String priceText = txtUnitPrice.getText().trim();
        if (priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Unit price is missing.");
            return;
        }
        
        double unitPrice;
        try {
            unitPrice = Double.parseDouble(priceText);
            if (unitPrice <= 0) {
                JOptionPane.showMessageDialog(this, "Unit price must be greater than 0.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for unit price.");
            return;
        }

        // Check Stock Availability
        int availableStock;
        try {
            availableStock = Integer.parseInt(txtStock.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid stock value.");
            return;
        }

        if (quantity > availableStock) {
            JOptionPane.showMessageDialog(this, 
                "Insufficient stock! Available: " + availableStock + ", Requested: " + quantity);
            return;
        }

        // Calculate Total
        double total = quantity * unitPrice;

        // Get Product Name from ComboBox
        String selectedItem = cmbProductSearch.getSelectedItem().toString();
        String productName = selectedItem.substring(0, selectedItem.lastIndexOf("(")).trim();

        // Add to Table
        DefaultTableModel model = (DefaultTableModel) tblBill.getModel();
        model.addRow(new Object[]{
            productId,
            productName,
            quantity,
            String.format("%.2f", unitPrice),
            String.format("%.2f", total)
        });

        // Recalculate totals
        recalcTotals();

       

        JOptionPane.showMessageDialog(this, "Item added successfully!");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "An unexpected error occurred: " + e.getMessage());
    }
    }//GEN-LAST:event_btnAddItemActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        
       saveBillAsPDF();
       
       resetForm();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnScanOrLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnScanOrLoadActionPerformed
        // TODO add your handling code here:
                                                    
    if (cmbProductSearch.getSelectedIndex() == 0) {
        JOptionPane.showMessageDialog(this, "Please select a product from the dropdown.");
        return;
    }
    
    try {
        // Extract product_id from selected item: "Product Name (PROD-123)"
        String selectedItem = cmbProductSearch.getSelectedItem().toString();
        int startIdx = selectedItem.lastIndexOf("(") + 1;
        int endIdx = selectedItem.lastIndexOf(")");
        String productId = selectedItem.substring(startIdx, endIdx);
        
        // Load product details from database
        String sql = "SELECT product_name, unit_price, quantity FROM products WHERE product_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, productId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                txtProductId.setText(productId);
                txtUnitPrice.setText(String.format("%.2f", rs.getDouble("unit_price")));
                txtStock.setText(String.valueOf(rs.getInt("quantity")));
                txtQty.requestFocus(); // Focus on quantity field
            } else {
                JOptionPane.showMessageDialog(this, "Product not found in database.");
            }
        }
        
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error loading product: " + e.getMessage());
    }

    }//GEN-LAST:event_btnScanOrLoadActionPerformed

    // Helper method for stock reduction within transaction
private void reduceStockWithConnection(Connection conn, String productId, int quantity) throws SQLException {
    String sql = "UPDATE products SET quantity = quantity - ? WHERE product_id = ?";
    try (PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setInt(1, quantity);
        pst.setString(2, productId);
        pst.executeUpdate();
    }
}

// Reset form after successful payment
private void resetForm() {
    DefaultTableModel model = (DefaultTableModel) tblBill.getModel();
    model.setRowCount(0);
    recalcTotals();
    txtInvoiceId.setText(generatePaymentId());
    txtPaid.setText("");
    txtBalance.setText("");
    clearProductFields();
}

private void saveInvoice(Connection conn, String invoiceId, String date,
                         double total, String method, double paid,
                         double balance, String status) throws SQLException {

    String sql = "INSERT INTO invoices (invoice_id, invoice_date, subtotal, total_amount, " +
                 "payment_method, amount_paid, balance, payment_status) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setString(1, invoiceId);
        pst.setString(2, date);
        pst.setDouble(3, total);
        pst.setDouble(4, total);
        pst.setString(5, method);
        pst.setDouble(6, paid);
        pst.setDouble(7, balance);
        pst.setString(8, status);
        pst.executeUpdate();
    }
}

private void saveInvoiceItem(String invoiceId, String productId, String productName, 
                             int quantity, double unitPrice, double total) {
    // FIXED: Changed from 'invoice_items' to 'bill_items'
    String sql = "INSERT INTO bill_items (invoice_id, product_id, product_name, quantity, unit_price, total) " +
                 "VALUES (?, ?, ?, ?, ?, ?)";
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {
        
        pst.setString(1, invoiceId);
        pst.setString(2, productId);
        pst.setString(3, productName);
        pst.setInt(4, quantity);
        pst.setDouble(5, unitPrice);
        pst.setDouble(6, total);
        
        pst.executeUpdate();
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error saving invoice item: " + e.getMessage());
    }
}

private void clearFormAfterPayment() {
    // Clear table
    DefaultTableModel model = (DefaultTableModel) tblBill.getModel();
    model.setRowCount(0);
    
    // Clear payment fields
    txtSubtotal.setText("0.00");
    txtTotal.setText("0.00");
    txtPaid.setText("");
    txtBalance.setText("0.00");
    cmbPaymentMethod.setSelectedIndex(0);
    
    // Clear product fields
    clearProductFields();
    
    // Generate new invoice ID
    txtInvoiceId.setText(generatePaymentId());
    txtDate.setText(java.time.LocalDate.now().toString());
}

    
    private void btnConfirmPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmPaymentActionPerformed
        // TODO add your handling code here:
        
    try {
        // Validate table has items
        if (tblBill.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please add items to the bill first.");
            return;
        }

        // Validate payment amount
        String paidText = txtPaid.getText().trim();
        if (paidText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the amount paid.");
            return;
        }

        double amountPaid;
        try {
            amountPaid = Double.parseDouble(paidText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid payment amount.");
            return;
        }

        // Get values
        String invoiceId = txtInvoiceId.getText().trim();
        String invoiceDate = txtDate.getText().trim();
        double subtotal = Double.parseDouble(txtSubtotal.getText().trim());
        double total = Double.parseDouble(txtTotal.getText().trim());
        String paymentMethod = cmbPaymentMethod.getSelectedItem().toString();
        double balance = Double.parseDouble(txtBalance.getText().trim());

        // Determine payment status
        String paymentStatus = (balance > 0) ? "Partial" : (balance == 0) ? "Paid" : "Overpaid";

        // Get transaction type from ComboBox (Purchase or Sell)
        String transactionType = cmbTransactionType.getSelectedItem().toString(); // <-- your combobox

        // Start transaction
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Insert Invoice
            String invoiceSql = "INSERT INTO invoices (invoice_id, invoice_date, subtotal, total_amount, " +
                               "payment_method, amount_paid, balance, payment_status) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pst = conn.prepareStatement(invoiceSql)) {
                pst.setString(1, invoiceId);
                pst.setString(2, invoiceDate);
                pst.setDouble(3, subtotal);
                pst.setDouble(4, total);
                pst.setString(5, paymentMethod);
                pst.setDouble(6, amountPaid);
                pst.setDouble(7, balance);
                pst.setString(8, paymentStatus);
                pst.executeUpdate();
            }

            // Only if it's a "Sell" transaction, reduce stock and add bill items
            if (transactionType.equalsIgnoreCase("Sell")) {
                DefaultTableModel model = (DefaultTableModel) tblBill.getModel();
                for (int i = 0; i < model.getRowCount(); i++) {
                    String productId = model.getValueAt(i, 0).toString();
                    String productName = model.getValueAt(i, 1).toString();
                    int quantity = Integer.parseInt(model.getValueAt(i, 2).toString());
                    double unitPrice = Double.parseDouble(model.getValueAt(i, 3).toString());
                    double itemTotal = Double.parseDouble(model.getValueAt(i, 4).toString());

                    // Insert bill item
                    String itemSql = "INSERT INTO bill_items (invoice_id, product_id, product_name, quantity, unit_price, total) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement pst = conn.prepareStatement(itemSql)) {
                        pst.setString(1, invoiceId);
                        pst.setString(2, productId);
                        pst.setString(3, productName);
                        pst.setInt(4, quantity);
                        pst.setDouble(5, unitPrice);
                        pst.setDouble(6, itemTotal);
                        pst.executeUpdate();
                    }

                    // Reduce stock
                    String stockSql = "UPDATE products SET quantity = quantity - ? WHERE product_id = ?";
                    try (PreparedStatement pst = conn.prepareStatement(stockSql)) {
                        pst.setInt(1, quantity);
                        pst.setString(2, productId);
                        int rowsAffected = pst.executeUpdate();
                        if (rowsAffected == 0) {
                            throw new SQLException("Product " + productId + " not found in database!");
                        }
                    }

                    // Insert into stock_history
                    String historySql = "INSERT INTO stock_history (product_id, action_type, quantity, date_time, user, remarks, issued_to, issued_by) " +
                                        "VALUES (?, ?, ?, NOW(), ?, ?, ?, ?)";
                    try (PreparedStatement pst = conn.prepareStatement(historySql)) {
                        pst.setString(1, productId);
                        pst.setString(2, "Sale");
                        pst.setInt(3, quantity);
                        pst.setString(4, "System"); // replace with current user
                        pst.setString(5, "Sold via invoice " + invoiceId);
                        pst.setString(6, "Customer");
                        pst.setString(7, "System"); // replace with current user
                        pst.executeUpdate();
                    }
                }
            }

            // Commit transaction
            conn.commit();
            JOptionPane.showMessageDialog(this, "Payment completed successfully!\nInvoice ID: " + invoiceId);

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error processing payment: " + e.getMessage());
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage());
    }

    }//GEN-LAST:event_btnConfirmPaymentActionPerformed

    private void txtQtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQtyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQtyActionPerformed

    private void txtInvoiceIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInvoiceIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInvoiceIdActionPerformed

    private void txtProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductIdActionPerformed

    private void txtStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStockActionPerformed

    private void txtBalanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBalanceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBalanceActionPerformed

    private void lblDashMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDashMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblDashMouseClicked

    private void cmbTransactionTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTransactionTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTransactionTypeActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new PaymentForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CustomPanel;
    private javax.swing.JPanel PaymentPanel;
    private javax.swing.JPanel SalesPanel;
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnConfirmPayment;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnScanOrLoad;
    private javax.swing.JComboBox<String> cmbPaymentMethod;
    private javax.swing.JComboBox<String> cmbProductSearch;
    private javax.swing.JComboBox<String> cmbTransactionType;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDash;
    private javax.swing.JLabel lblInventory;
    private javax.swing.JLabel lblLogout;
    private javax.swing.JLabel lblPayment;
    private javax.swing.JLabel lblProducts;
    private javax.swing.JLabel lblQRScan;
    private javax.swing.JLabel lblReports;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSettings;
    private javax.swing.JLabel lblStock;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblUsers;
    private javax.swing.JTable tblBill;
    private javax.swing.JTextField txtBalance;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtInvoiceId;
    private javax.swing.JTextField txtPaid;
    private javax.swing.JTextField txtProductId;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtSubtotal;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtUnitPrice;
    // End of variables declaration//GEN-END:variables
}
