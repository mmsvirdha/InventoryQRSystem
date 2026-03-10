/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package reports;


import QrScanner.QRCodeScanner;
import Settings.SettingsForm;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import dashboard.Dashboard;
import java.io.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.DocumentException; 
import inventory.StockInOutForm;
import javax.swing.SwingWorker;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.SwingWorker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import login.LoginForm;
import products.ProductForm;
import users.userForm;

import utils.DBConnection;
import utils.TransitionHelper;


/**
 *
 * @author ADMIN
 */

public class ReportForm extends javax.swing.JFrame {
    
    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    
      private String username;
     private String role;
     
      private int totalRecords = 0;
    private int totalQuantity = 0;
    private double totalStockValue = 0.0;
    
    private JDialog loadingDialog;

    
        private List<Map<String, Object>> reportRows; // store DB rows for preview/PDF

    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ReportForm.class.getName());

    /**
     * Creates new form ReportForm
     */
    
    
     public ReportForm() {
        initComponents();
        
       
            activateAllLinks();
        
        conn = DBConnection.getConnection();
        setLocationRelativeTo(null);
        loadCategories();
        loadLocations();
        setupDateButtons();
    

     }
     
      public ReportForm (String username, String role) {  // Accept parameters
          
           this.username = username;
    this.role = role;
    
    initComponents();

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

      
        headerPanel.setOpaque(false);
headerPanel.setBorder(null);

CustomPsnel.setOpaque(false);
CustomPsnel.setBorder(null);

detailsPanel.setOpaque(false);
detailsPanel.setBorder(null);

        
        // Maximizes the window to full screen size
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
       
        
        conn = DBConnection.getConnection();
        setLocationRelativeTo(null);
        loadCategories();
        loadLocations();
        setupDateButtons();
        
        // Populate combo boxes correctly
    jComboBoxSortBy.removeAllItems();
    jComboBoxSortBy.addItem("Product Name");
    jComboBoxSortBy.addItem("Category");
    jComboBoxSortBy.addItem("Price");
    jComboBoxSortBy.addItem("Quantity");
    jComboBoxSortBy.addItem("Date Added");

    jComboBoxOrder.removeAllItems();
    jComboBoxOrder.addItem("Ascending");
    jComboBoxOrder.addItem("Descending");
    
    // Ensure the default directory is created
    ensureReportDirectoryExists();
    
  

//PREVIEW button (jButton6) - Only shows preview, does NOT save
jButton6.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        generateReportPreview();  // Only preview, no saving
    }
});

// DOWNLOAD button Saves the file
    jButton7.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            // Validate dates first
            if (jDateChooserStart.getDate() != null && jDateChooserEnd.getDate() != null) {
                if (jDateChooserStart.getDate().after(jDateChooserEnd.getDate())) {
                    JOptionPane.showMessageDialog(ReportForm.this, 
                        "Start date cannot be after end date!", 
                        "Invalid Date Range", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            
            // Load data and trigger save
            loadReportDataFromDB();
            handleReportSaveAction();
        }
    });

// 3. RESET button (jButton5) - Keep as is
jButton5.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton5ActionPerformed(evt);
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
                ReportForm.this,
                "Do you really want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                TransitionHelper.fadeToFrame(ReportForm.this, new LoginForm(), 500);
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
    
    
 private void generateAndWritePDF(File file) 
          throws com.itextpdf.text.DocumentException, java.io.IOException {
    com.itextpdf.text.Document document = new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4.rotate());
    FileOutputStream fos = new FileOutputStream(file);
    PdfWriter writer = PdfWriter.getInstance(document, fos);
 // 2. Setup Watermark (DO THIS BEFORE OPENING)
    try {
        com.itextpdf.text.Image watermark = com.itextpdf.text.Image.getInstance("C:/Users/ADMIN/Desktop/Virdha/Virdha Personal/Java Projects/NetBeans Projects/InventoryQRSystem/src/InventoryImages/inventoryBackground.jpg");
        watermark.scaleToFit(400, 400);
        // Assuming WatermarkBackground is your custom class
        writer.setPageEvent(new WatermarkBackground(watermark)); 
    } catch (Exception e) {
        logger.warning("Watermark could not be loaded: " + e.getMessage());
    }

// 3. OPEN the document
    document.open();

      // 1. Define Fonts using fully qualified names (iText Font)
      com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
      com.itextpdf.text.Font detailFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11);
      com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD, com.itextpdf.text.BaseColor.BLUE);
      
      // Add your logo
try {
        com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance("src/InventoryImages/logo.jpg");
        logo.scaleToFit(120, 120);
        logo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
        document.add(logo);
    } catch (Exception ex) {
        logger.warning("Logo not found");
    }

     
      // 2. Title and Summary
      com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph("INVENTORY REPORT", titleFont);
      title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER); //️ Center the title
      document.add(title);

      document.add(com.itextpdf.text.Chunk.NEWLINE);

      
      // Add date details
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      String startDateStr = jDateChooserStart.getDate() != null ? sdf.format(jDateChooserStart.getDate()) : "N/A";
      String endDateStr = jDateChooserEnd.getDate() != null ? sdf.format(jDateChooserEnd.getDate()) : "N/A";
      
      document.add(new com.itextpdf.text.Paragraph("Period: " + startDateStr + " to " + endDateStr, detailFont));
      
      // Add summary
     document.add(new Paragraph("Total Records: " + totalRecords, detailFont));
     document.add(new Paragraph("Total Quantity: " + totalQuantity, detailFont));
     document.add(new Paragraph("Total Stock Value: $" + String.format("%.2f", totalStockValue), detailFont));

     document.add(Chunk.NEWLINE); // spacing


      // 3. Create PDF Table with QR Code column
      // Columns: ID, Name, Category, Location, Price, Quantity, Date Added, QR Code
      com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(8); 
      table.setWidthPercentage(100);
      table.setWidths(new float[]{0.8f, 2.0f, 1.2f, 1.2f, 0.9f, 0.8f, 1.5f, 0.8f});

      // 4. Add Headers
      String[] headers = {"ID", "Product Name", "Category", "Location", "Unit Price", "Quantity", "Date Added", "QR Code"};
      for (String header : headers) {
          com.itextpdf.text.pdf.PdfPCell cell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(header, headerFont));
          cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
          cell.setBackgroundColor(com.itextpdf.text.BaseColor.LIGHT_GRAY);
          cell.setPadding(5);
          table.addCell(cell);
      }
      
      // 5. Add Data Rows with QR Codes
      SimpleDateFormat pdfDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      for (java.util.Map<String, Object> row : reportRows) {
          // Add regular text cells
          table.addCell(new com.itextpdf.text.Phrase(String.valueOf(row.get("product_id")), detailFont));
          table.addCell(new com.itextpdf.text.Phrase(String.valueOf(row.get("product_name")), detailFont));
          table.addCell(new com.itextpdf.text.Phrase(String.valueOf(row.get("category")), detailFont));
          table.addCell(new com.itextpdf.text.Phrase(String.valueOf(row.get("location")), detailFont));
          table.addCell(new com.itextpdf.text.Phrase("$" + String.format("%.2f", (Double) row.get("unit_price")), detailFont));
          table.addCell(new com.itextpdf.text.Phrase(String.valueOf(row.get("quantity")), detailFont));
          
          Object dateObj = row.get("date_added");
          String dateStr = dateObj != null ? pdfDateFormat.format((java.sql.Timestamp) dateObj) : "N/A";
          table.addCell(new com.itextpdf.text.Phrase(dateStr, detailFont));
          
          // Add QR Code image cell
          String qrCodePath = (String) row.get("qr_code_path");
          com.itextpdf.text.pdf.PdfPCell qrCell = new com.itextpdf.text.pdf.PdfPCell();
          qrCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
          qrCell.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
          qrCell.setPadding(2);
          
          if (qrCodePath != null && !qrCodePath.isEmpty()) {
              File qrFile = new File(qrCodePath);
              if (qrFile.exists()) {
                  try {
                      com.itextpdf.text.Image qrImage = com.itextpdf.text.Image.getInstance(qrCodePath);
                      // Resize QR code to fit in cell (smaller size for table)
                      qrImage.scaleToFit(60, 60);
                      qrCell.addElement(qrImage);
                  } catch (Exception e) {
                      qrCell.setPhrase(new com.itextpdf.text.Phrase("N/A", detailFont));
                      logger.warning("Could not load QR code: " + qrCodePath);
                  }
              } else {
                  qrCell.setPhrase(new com.itextpdf.text.Phrase("N/A", detailFont));
              }
          } else {
              qrCell.setPhrase(new com.itextpdf.text.Phrase("N/A", detailFont));
          }
          
          table.addCell(qrCell);
      }
      
      document.add(table);
      document.close();
  
    fos.close();
 }
     
  // Excel Worker
 private class ExcelSaveWorker extends SwingWorker<Void, Void> {
     private File file;

     public ExcelSaveWorker(File file) {
         this.file = file;
     }

     @Override
     protected Void doInBackground() throws Exception {
         SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true));
         // TODO: generate Excel here
         Thread.sleep(1000);
         return null;
     }

     @Override
     protected void done() {
         loadingDialog.setVisible(false);
         loadingDialog.dispose();
         jTextAreaReportOutput.setText("Excel saved to: " + file.getAbsolutePath());
     }
 }

 // CSV Worker
 private class CsvSaveWorker extends SwingWorker<Void, Void> {
     private File file;

     public CsvSaveWorker(File file) {
         this.file = file;
     }

     @Override
     protected Void doInBackground() throws Exception {
         SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true));
         // TODO: generate CSV here
         Thread.sleep(1000);
         return null;
     }

     @Override
     protected void done() {
         loadingDialog.setVisible(false);
         loadingDialog.dispose();
         jTextAreaReportOutput.setText("CSV saved to: " + file.getAbsolutePath());
     }
 }

// Generate filename with timestamp
 private String generatePDFFileName() {
     java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss");
     return "InventoryReport_" + sdf.format(new java.util.Date()) + ".pdf";
 }

   private void handleReportSaveAction() {
     if (jRadioButtonPDF.isSelected()) {
     ensureReportDirectoryExists();

     // Generate default file name with timestamp
     String defaultFileName = generatePDFFileName();
     File fileToSave = new File(DEFAULT_REPORT_DIR + File.separator + defaultFileName);

     // Load report data from DB
     loadReportDataFromDB();

     // Generate PDF in background
     new PdfSaveWorker(fileToSave).execute();

     } else if (jRadioButtonExcel.isSelected()) {
          ensureReportDirectoryExists();
          JFileChooser fileChooser = new JFileChooser(DEFAULT_REPORT_DIR);
          fileChooser.setDialogTitle("Save Excel Report");
          SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
          String defaultFileName = "InventoryReport_" + sdf.format(new Date()) + ".xlsx";
          fileChooser.setSelectedFile(new File(DEFAULT_REPORT_DIR + File.separator + defaultFileName));
          fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Documents (*.xlsx)", "xlsx"));
          
          int userSelection = fileChooser.showSaveDialog(this);
          if (userSelection == JFileChooser.APPROVE_OPTION) {
              File fileToSave = fileChooser.getSelectedFile();
              if (!fileToSave.getName().toLowerCase().endsWith(".xlsx")) {
                  fileToSave = new File(fileToSave.getAbsolutePath() + ".xlsx");
              }
              loadReportDataFromDB();
              new ExcelSaveWorker(fileToSave).execute();
          }
     } else if (jRadioButtonCSV.isSelected()) {
          ensureReportDirectoryExists();
          JFileChooser fileChooser = new JFileChooser(DEFAULT_REPORT_DIR);
          fileChooser.setDialogTitle("Save CSV Report");
          SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
          String defaultFileName = "InventoryReport_" + sdf.format(new Date()) + ".csv";
          fileChooser.setSelectedFile(new File(DEFAULT_REPORT_DIR + File.separator + defaultFileName));
          fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Documents (*.csv)", "csv"));
          
          int userSelection = fileChooser.showSaveDialog(this);
          if (userSelection == JFileChooser.APPROVE_OPTION) {
              File fileToSave = fileChooser.getSelectedFile();
              if (!fileToSave.getName().toLowerCase().endsWith(".csv")) {
                  fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
              }
              loadReportDataFromDB();
              new CsvSaveWorker(fileToSave).execute();
          }
     } else {
          JOptionPane.showMessageDialog(this, 
              "Please select an export format.", 
              "Format Required", 
              JOptionPane.WARNING_MESSAGE);
     }
 }
     
     
  // Set fixed folder path for reports
 private static final String DEFAULT_REPORT_DIR =
          "C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\InventoryReports";


     
 //ensure the directory exists
 private void ensureReportDirectoryExists() {
     File dir = new File(DEFAULT_REPORT_DIR);
     if (!dir.exists()) {
         dir.mkdirs();
     }
 }

     
     
     private String getUniquePDFFileName(String folderPath, String baseName) { 
    // Get today's date in yyyyMMdd format
    String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
    int counter = 1;

    String fileName;
    File file;

    do {
        // Build file name like InventoryReport_20251201_01.pdf
        fileName = baseName + "_" + datePart + "_" + String.format("%02d", counter) + ".pdf";
        file = new File(folderPath + File.separator + fileName);
        counter++;
    } while (file.exists());  // keep incrementing until a non-existing file is found

    return file.getAbsolutePath();
}
     
     
  // CORRECTION: Add file member to the worker to hold the file path
 private class PdfSaveWorker extends SwingWorker<Boolean, Void> {
     private JDialog loadingDialog;
     private File fileToSave;
     
     public PdfSaveWorker(File fileToSave) {
         this.fileToSave = fileToSave;
         
         // Initialize dialog here, but show it on the EDT
         loadingDialog = new JDialog(ReportForm.this, "Processing...", true);
         JLabel label = new JLabel("  Generating PDF Report... Please Wait.  ");
         loadingDialog.add(label);
         loadingDialog.setUndecorated(true);
         loadingDialog.pack();
         loadingDialog.setLocationRelativeTo(ReportForm.this);
     }

     // Runs on a background thread
     @Override
     protected Boolean doInBackground() throws Exception {
         // Show the dialog *before* the intensive work starts (ON EDT)
         SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true)); // FIX 1: Show dialog on EDT
         
         try {
             // 1. Load data from DB (runs on background thread)
             loadReportDataFromDB(); 

             // 2. Write PDF to file (runs on background thread)
             generateAndWritePDF(fileToSave); // FIX 2: Added the critical PDF writing call

             return true;
         } catch (Exception e) {
             logger.log(java.util.logging.Level.SEVERE, "Error generating PDF in background.", e);
             return false;
         } finally {
             // 3. Hide the dialog when finished (ON EDT)
             SwingUtilities.invokeLater(() -> {
                 loadingDialog.setVisible(false);
                 loadingDialog.dispose();
             });
         }
     }

     // Runs on the Event Dispatch Thread (EDT)
     @Override
protected void done() {
    try {
        // Check the result of doInBackground()
        if (get()) {
            // Success logic 
            Object[] options = {"Open PDF", "Open Folder", "Close"};
            int choice = JOptionPane.showOptionDialog(ReportForm.this,
                "✅ Report exported successfully!\n\n" +
                "File: " + fileToSave.getName(),
                "Export Success",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
            
            // Handle user choice
            if (Desktop.isDesktopSupported()) {
                try {
                    if (choice == 0) {
                        // Open PDF
                        Desktop.getDesktop().open(fileToSave);
                    } else if (choice == 1) {
                        // Open folder containing PDF
                        Desktop.getDesktop().open(fileToSave.getParentFile());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            
             } else {
                  // Failure logic
                 JOptionPane.showMessageDialog(ReportForm.this, 
                          "Failed to generate PDF Report.", 
                          "Error", 
                          JOptionPane.ERROR_MESSAGE);
             }
         } catch (Exception ex) {
             logger.log(java.util.logging.Level.SEVERE, "Error in done() method.", ex);
             JOptionPane.showMessageDialog(ReportForm.this, "An unexpected error occurred after processing.", "Error", JOptionPane.ERROR_MESSAGE);
         }
     }
    
    private void showLoadingDialog() {
     loadingDialog = new JDialog(ReportForm.this, "Processing...", true);
     JLabel label = new JLabel("  Generating PDF Report... Please Wait.  ");

     loadingDialog.add(label);
     loadingDialog.setUndecorated(true);
     loadingDialog.pack();
     loadingDialog.setLocationRelativeTo(ReportForm.this);

     loadingDialog.setVisible(true); // show directly
 }

 }
 
 
     /**
 * Fetch report rows from DB according to current UI filters.
 * Returns a Map with:
 *   - "rows": List<Map<String,Object>>  each map: columnName -> value
 *   - "summary": Map<String, Number> totals (totalRecords, totalQuantity, totalValue)
 */
 
 
private Map<String, Object> getReportDataFromDB() throws SQLException {
     List<Map<String, Object>> rows = new ArrayList<>();
     Map<String, Number> summary = new HashMap<>();
     summary.put("totalRecords", 0);
     summary.put("totalQuantity", 0);
     summary.put("totalValue", 0.0);

     StringBuilder query = new StringBuilder();
     query.append("SELECT p.product_id, p.product_name, p.category, p.location, ");
     query.append("p.unit_price, p.quantity, p.supplier, p.date_added, p.qr_code_path ");
     query.append("FROM products p WHERE 1=1 ");

     // Date filters
     boolean useDate = (jDateChooserStart.getDate() != null && jDateChooserEnd.getDate() != null);
     if (useDate) {
          query.append("AND p.date_added BETWEEN ? AND ? ");
     }

     // Category & Location filters (assuming combo boxes index 0 = All)
     if (jComboBoxCategory.getSelectedIndex() > 0) {
          query.append("AND p.category = ? ");
     }
     if (jComboBoxLocation.getSelectedIndex() > 0) {
          query.append("AND p.location = ? ");
     }

     // Sorting
     String sortBy = jComboBoxSortBy.getSelectedItem() != null ? jComboBoxSortBy.getSelectedItem().toString() : "Date Added";
     if (sortBy.equalsIgnoreCase("Product Name")) {
          query.append("ORDER BY p.product_name ");
     } else if (sortBy.equalsIgnoreCase("Category")) {
          query.append("ORDER BY p.category ");
     } else if (sortBy.equalsIgnoreCase("Price")) {
          query.append("ORDER BY p.unit_price ");
     } else if (sortBy.equalsIgnoreCase("Quantity")) {
          query.append("ORDER BY p.quantity ");
     } else {
          query.append("ORDER BY p.date_added ");
     }

     String order = jComboBoxOrder.getSelectedItem() != null ? jComboBoxOrder.getSelectedItem().toString() : "Ascending";
     query.append(order.equalsIgnoreCase("Ascending") ? " ASC " : " DESC ");

     PreparedStatement pst = null;
     ResultSet rs = null;
     try {
          pst = conn.prepareStatement(query.toString());
          int idx = 1;
          if (useDate) {
              java.util.Date s = jDateChooserStart.getDate();
              java.util.Date e = jDateChooserEnd.getDate();
              pst.setTimestamp(idx++, new java.sql.Timestamp(s.getTime()));
              // include entire end day:
              long endTime = e.getTime() + (24L*60*60*1000) - 1;
              pst.setTimestamp(idx++, new java.sql.Timestamp(endTime));
          }
          if (jComboBoxCategory.getSelectedIndex() > 0) {
              pst.setString(idx++, jComboBoxCategory.getSelectedItem().toString());
          }
          if (jComboBoxLocation.getSelectedIndex() > 0) {
              pst.setString(idx++, jComboBoxLocation.getSelectedItem().toString());
          }

          rs = pst.executeQuery();

          int totalRecords = 0;
          int totalQty = 0;
          double totalVal = 0.0;

          while (rs.next()) {
              Map<String, Object> row = new LinkedHashMap<>();
              // note: product_id is string (like P001)
              row.put("product_id", rs.getString("product_id"));
              row.put("product_name", rs.getString("product_name"));
              row.put("category", rs.getString("category"));
              row.put("location", rs.getString("location"));
              // unit_price -> double
              double unitPrice = rs.getDouble("unit_price");
              row.put("unit_price", unitPrice);
              int qty = rs.getInt("quantity");
              row.put("quantity", qty);
              row.put("supplier", rs.getString("supplier"));
              row.put("date_added", rs.getTimestamp("date_added"));
              row.put("qr_code_path", rs.getString("qr_code_path"));

              rows.add(row);

              totalRecords++;
              totalQty += qty;
              totalVal += (unitPrice * qty);
          }

          summary.put("totalRecords", totalRecords);
          summary.put("totalQuantity", totalQty);
          summary.put("totalValue", totalVal);

          Map<String, Object> result = new HashMap<>();
          result.put("rows", rows);
          result.put("summary", summary);
          return result;

     } finally {
          if (rs != null) try { rs.close(); } catch (Exception ex) {}
          if (pst != null) try { pst.close(); } catch (Exception ex) {}
     }
 }

  
     
     // Setup predefined date range labels with mouse listeners
    private void setupDateButtons() {
        // Make labels look clickable
        lblToday.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblThisWeek.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblThisMonth.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblThisYear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblCustomRange.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Today label action
        lblToday.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                setDateRange(0, 0);
                highlightSelectedLabel(lblToday);
            }
        });
        
        // This Week label action
        lblThisWeek.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                setDateRange(0, 7);
                highlightSelectedLabel(lblThisWeek);
            }
        });
        
        // This Month label action
        lblThisMonth.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                setDateRange(0, 30);
                highlightSelectedLabel(lblThisMonth);
            }
        });
        
        // This Year label action
        lblThisYear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                setDateRange(0, 365);
                highlightSelectedLabel(lblThisYear);
            }
        });
        
        // Custom Range - enables the date choosers
        lblCustomRange.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jDateChooserStart.setEnabled(true);
                jDateChooserEnd.setEnabled(true);
                highlightSelectedLabel(lblCustomRange);
            }
        });
    }
    
    // Highlight the selected date range label
   private void highlightSelectedLabel(JLabel selectedLabel) {
    // Reset all labels to default color
    lblToday.setForeground(java.awt.Color.BLACK);
    lblThisWeek.setForeground(java.awt.Color.BLACK);
    lblThisMonth.setForeground(java.awt.Color.BLACK);
    lblThisYear.setForeground(java.awt.Color.BLACK);
    lblCustomRange.setForeground(java.awt.Color.BLACK);

    // Highlight selected label
    selectedLabel.setForeground(new java.awt.Color(0, 102, 204)); 
    selectedLabel.setFont(selectedLabel.getFont().deriveFont(java.awt.Font.BOLD));
}
    
    
    // Set date range helper method
    private void setDateRange(int daysAgo, int rangeInDays) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        Date endDate = cal.getTime();
        cal.add(java.util.Calendar.DAY_OF_MONTH, -rangeInDays);
        Date startDate = cal.getTime();
        
        jDateChooserStart.setDate(startDate);
        jDateChooserEnd.setDate(endDate);
    }
    
     // Load categories from database
    private void loadCategories() {
        try {
            String query = "SELECT DISTINCT category FROM products ORDER BY category";
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            
            jComboBoxCategory.removeAllItems();
            jComboBoxCategory.addItem("All Categories");
            
            while (rs.next()) {
                jComboBoxCategory.addItem(rs.getString("category"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage());
        }
    }
    
    // Load locations from database
    private void loadLocations() {
        try {
            String query = "SELECT DISTINCT location FROM products ORDER BY location";
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            
            jComboBoxLocation.removeAllItems();
            jComboBoxLocation.addItem("All Locations");
            
            while (rs.next()) {
                jComboBoxLocation.addItem(rs.getString("location"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading locations: " + e.getMessage());
        }
    }

    private void loadReportDataFromDB() {
    try {
        Map<String, Object> result = getReportDataFromDB(); // your DB fetch method
        reportRows = (List<Map<String, Object>>) result.get("rows");

        Map<String, Number> summary = (Map<String, Number>) result.get("summary");
        totalRecords = summary.get("totalRecords").intValue();
        totalQuantity = summary.get("totalQuantity").intValue();
        totalStockValue = summary.get("totalValue").doubleValue();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading report data: " + e.getMessage());
        e.printStackTrace();
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel6 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        CustomPsnel = new utils.RoundedPanel(25);
        detailsPanel = new utils.RoundedPanel(25);
        lblToday = new javax.swing.JLabel();
        lblThisWeek = new javax.swing.JLabel();
        lblThisMonth = new javax.swing.JLabel();
        lblThisYear = new javax.swing.JLabel();
        lblCustomRange = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jDateChooserStart = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        jDateChooserEnd = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        jComboBoxCategory = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jComboBoxLocation = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jComboBoxSortBy = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jComboBoxOrder = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        jCheckBoxProductDetails = new javax.swing.JCheckBox();
        jCheckBoxPricing = new javax.swing.JCheckBox();
        jCheckBoxStockLevels = new javax.swing.JCheckBox();
        jCheckBoxSupplierDetails = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();
        jRadioButtonPDF = new javax.swing.JRadioButton();
        jRadioButtonExcel = new javax.swing.JRadioButton();
        jRadioButtonCSV = new javax.swing.JRadioButton();
        jRadioButtonPrint = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        previewPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaReportOutput = new javax.swing.JTextArea();
        jLabel19 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        headerPanel = new utils.RoundedPanel(25);
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
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

        jPanel6.setBackground(new java.awt.Color(59, 81, 108));

        jPanel1.setBackground(new java.awt.Color(59, 81, 108));

        CustomPsnel.setBackground(new java.awt.Color(80, 108, 133));

        lblToday.setText(" Today");

        lblThisWeek.setText("This Week");

        lblThisMonth.setText("This Month");

        lblThisYear.setText(" This Year");

        lblCustomRange.setText("Custom Range");

        jLabel11.setText("📅 Start Date");

        jLabel12.setText("📅 End Date");

        jLabel13.setText("🏷️ Category");

        jComboBoxCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel14.setText("📍 Location");

        jComboBoxLocation.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel15.setText("📊 Sort By");

        jComboBoxSortBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel16.setText("🔢 Order");

        jComboBoxOrder.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxOrderActionPerformed(evt);
            }
        });

        jLabel17.setText("Include in Report");

        jCheckBoxProductDetails.setText("Product Details");

        jCheckBoxPricing.setText("Pricing Information");

        jCheckBoxStockLevels.setText("Stock Levels");

        jCheckBoxSupplierDetails.setText("Supplier Details");
        jCheckBoxSupplierDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxSupplierDetailsActionPerformed(evt);
            }
        });

        jLabel18.setText("📄 Export Format");

        jRadioButtonPDF.setText("PDF");

        jRadioButtonExcel.setText("Excel");

        jRadioButtonCSV.setText("CSV");

        jRadioButtonPrint.setText("Print");

        javax.swing.GroupLayout detailsPanelLayout = new javax.swing.GroupLayout(detailsPanel);
        detailsPanel.setLayout(detailsPanelLayout);
        detailsPanelLayout.setHorizontalGroup(
            detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailsPanelLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(detailsPanelLayout.createSequentialGroup()
                        .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jCheckBoxProductDetails, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(jCheckBoxPricing))
                        .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(detailsPanelLayout.createSequentialGroup()
                                .addGap(98, 98, 98)
                                .addComponent(jRadioButtonCSV))
                            .addGroup(detailsPanelLayout.createSequentialGroup()
                                .addGap(132, 132, 132)
                                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBoxSupplierDetails)
                                    .addComponent(jCheckBoxStockLevels, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jLabel13)
                    .addGroup(detailsPanelLayout.createSequentialGroup()
                        .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(detailsPanelLayout.createSequentialGroup()
                                .addComponent(lblToday)
                                .addGap(32, 32, 32)
                                .addComponent(lblThisWeek)
                                .addGap(50, 50, 50)
                                .addComponent(lblThisMonth))
                            .addComponent(jLabel11)
                            .addComponent(jDateChooserStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxCategory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxSortBy, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, detailsPanelLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel12)
                                    .addComponent(jDateChooserEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14)
                                    .addComponent(jComboBoxLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBoxOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, detailsPanelLayout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(lblThisYear)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblCustomRange))))
                    .addComponent(jLabel15))
                .addContainerGap(22, Short.MAX_VALUE))
            .addGroup(detailsPanelLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(detailsPanelLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(detailsPanelLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jRadioButtonPDF)
                        .addGap(55, 55, 55)
                        .addComponent(jRadioButtonExcel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jRadioButtonPrint)
                        .addGap(83, 83, 83))))
        );
        detailsPanelLayout.setVerticalGroup(
            detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailsPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblToday)
                    .addComponent(lblThisMonth)
                    .addComponent(lblThisYear)
                    .addComponent(lblCustomRange)
                    .addComponent(lblThisWeek))
                .addGap(29, 29, 29)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooserStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooserEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxSortBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxProductDetails)
                    .addComponent(jCheckBoxSupplierDetails))
                .addGap(15, 15, 15)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxPricing)
                    .addComponent(jCheckBoxStockLevels))
                .addGap(31, 31, 31)
                .addComponent(jLabel18)
                .addGap(18, 18, 18)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonPDF)
                    .addComponent(jRadioButtonExcel)
                    .addComponent(jRadioButtonCSV)
                    .addComponent(jRadioButtonPrint))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Custom Report Builder");

        jTextAreaReportOutput.setColumns(20);
        jTextAreaReportOutput.setRows(5);
        jScrollPane1.setViewportView(jTextAreaReportOutput);

        javax.swing.GroupLayout previewPanelLayout = new javax.swing.GroupLayout(previewPanel);
        previewPanel.setLayout(previewPanelLayout);
        previewPanelLayout.setHorizontalGroup(
            previewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(previewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                .addContainerGap())
        );
        previewPanelLayout.setVerticalGroup(
            previewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(previewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Report Preview");

        jButton5.setBackground(new java.awt.Color(59, 81, 108));
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("🔄 Reset");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(59, 81, 108));
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("👁️ Generate Preview");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(59, 81, 108));
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("📥 Generate & Download Report");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CustomPsnelLayout = new javax.swing.GroupLayout(CustomPsnel);
        CustomPsnel.setLayout(CustomPsnelLayout);
        CustomPsnelLayout.setHorizontalGroup(
            CustomPsnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomPsnelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(CustomPsnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(detailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(29, 29, 29)
                .addGroup(CustomPsnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel19)
                    .addGroup(CustomPsnelLayout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(previewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        CustomPsnelLayout.setVerticalGroup(
            CustomPsnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomPsnelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CustomPsnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CustomPsnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(detailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(CustomPsnelLayout.createSequentialGroup()
                        .addComponent(previewPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(CustomPsnelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5)
                            .addComponent(jButton6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton7)))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        headerPanel.setBackground(new java.awt.Color(80, 108, 133));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Inventory Reports");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Generate comprehensive reports and analytics for your inventory");

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addContainerGap(575, Short.MAX_VALUE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(CustomPsnel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(CustomPsnel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxSupplierDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxSupplierDetailsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxSupplierDetailsActionPerformed

    private void jComboBoxOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxOrderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxOrderActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
       
        generateReportPreview();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        
        jDateChooserStart.setDate(null);
        jDateChooserEnd.setDate(null);
        jComboBoxCategory.setSelectedIndex(0);
        jComboBoxLocation.setSelectedIndex(0);
        jComboBoxSortBy.setSelectedIndex(0);
        jComboBoxOrder.setSelectedIndex(0);
        
        jCheckBoxProductDetails.setSelected(false);
        jCheckBoxSupplierDetails.setSelected(false);
        jCheckBoxPricing.setSelected(false);
        jCheckBoxStockLevels.setSelected(false);
        
        jRadioButtonPDF.setSelected(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        
    // Validate dates first
    if (jDateChooserStart.getDate() != null && jDateChooserEnd.getDate() != null) {
        if (jDateChooserStart.getDate().after(jDateChooserEnd.getDate())) {
            JOptionPane.showMessageDialog(ReportForm.this, 
                "Start date cannot be after end date!", 
                "Invalid Date Range", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
    // Check which format is selected and handle accordingly
    if (jRadioButtonPDF.isSelected()) {
        // Load data and trigger PDF save
        loadReportDataFromDB();
        handleReportSaveAction();  // This already handles PDF generation
        
    } else if (jRadioButtonExcel.isSelected()) {
        generateExcelReport();
        
    } else if (jRadioButtonCSV.isSelected()) {
        generateCSVReport();
        
    } else if (jRadioButtonPrint.isSelected()) {
        printReport();
        
    } else {
        JOptionPane.showMessageDialog(this, 
            "Please select an export format.", 
            "Format Required", 
            JOptionPane.WARNING_MESSAGE);
    }

 
    }//GEN-LAST:event_jButton7ActionPerformed

    private void lblDashMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDashMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblDashMouseClicked

    // Generate preview data
   public void generateReportPreview() {
    // --- 1. Get Selected Dates and Handle End Date Exclusivity ---
    Date startDate = jDateChooserStart.getDate();
    Date endDate = jDateChooserEnd.getDate();

    if (startDate == null || endDate == null) {
        JOptionPane.showMessageDialog(this, "Please select both Start and End Dates.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String startDateStr = sqlDateFormat.format(startDate);

    // FIX: Calculate the day *after* the selected end date for non-inclusive filter
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.setTime(endDate);
    cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
    String endDateExclusiveStr = sqlDateFormat.format(cal.getTime());

    // --- 2. Get Filters and Sort Options ---
    String category = jComboBoxCategory.getSelectedItem() != null ? jComboBoxCategory.getSelectedItem().toString() : "All Categories";
    String location = jComboBoxLocation.getSelectedItem() != null ? jComboBoxLocation.getSelectedItem().toString() : "All Locations";
    String sortBy = jComboBoxSortBy.getSelectedItem() != null ? jComboBoxSortBy.getSelectedItem().toString() : "Product Name";
    String order = jComboBoxOrder.getSelectedItem() != null ? jComboBoxOrder.getSelectedItem().toString() : "Ascending";

    String sortColumn;
    switch (sortBy) {
        case "Product Name": sortColumn = "product_name"; break;
        case "Category": sortColumn = "category"; break;
        case "Price": sortColumn = "unit_price"; break;
        case "Quantity": sortColumn = "quantity"; break;
        case "Date Added": sortColumn = "date_added"; break;
        default: sortColumn = "product_name";
    }
    String sortOrder = order.equals("Ascending") ? "ASC" : "DESC";

    // --- 3. Construct the Dynamic SQL Query ---
    StringBuilder sql = new StringBuilder("SELECT product_id, date_added");
    
    // Add columns based on checkbox selections
    if (jCheckBoxProductDetails.isSelected()) {
        sql.append(", product_name, category, location");
    }
    if (jCheckBoxPricing.isSelected()) {
        sql.append(", unit_price");
    }
    if (jCheckBoxStockLevels.isSelected()) {
        sql.append(", quantity");
    }
    if (jCheckBoxSupplierDetails.isSelected()) {
        sql.append(", supplier");
    }
    
    sql.append(" FROM products WHERE 1=1 ");

    // Apply Date Filter: >= Start Date AND < Day After End Date
    sql.append(" AND date_added >= ? AND date_added < ? ");

    // Category Filter
    if (!category.equals("All Categories")) {
        sql.append(" AND category = ? ");
    }

    // Location Filter
    if (!location.equals("All Locations")) {
        sql.append(" AND location = ? ");
    }

    // Apply Sorting
    sql.append(" ORDER BY ").append(sortColumn).append(" ").append(sortOrder);

    // --- 4. Execute the Query and Build Report Content ---
    int totalRecords = 0;
    StringBuilder reportContent = new StringBuilder();

    // Add Header Section
    SimpleDateFormat headerDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String reportGenerated = headerDateFormat.format(new Date());
    String reportPeriod = String.format("%s to %s",
                              sqlDateFormat.format(startDate),
                              sqlDateFormat.format(endDate));

    reportContent.append("=========================================\n");
    reportContent.append("       INVENTORY REPORT\n");
    reportContent.append("=========================================\n\n");
    reportContent.append("Report Generated: ").append(reportGenerated).append("\n");
    reportContent.append("Period: ").append(reportPeriod).append("\n");
    reportContent.append("-----------------------------------------\n");

    try {
        pst = conn.prepareStatement(sql.toString());
        int paramIndex = 1;

        // Set the Date Parameters
        pst.setString(paramIndex++, startDateStr);
        pst.setString(paramIndex++, endDateExclusiveStr);

        // Set the Category Parameter
        if (!category.equals("All Categories")) {
            pst.setString(paramIndex++, category);
        }

        // Set the Location Parameter
        if (!location.equals("All Locations")) {
            pst.setString(paramIndex++, location);
        }

        rs = pst.executeQuery();

        // --- 5. Process Results (Build Preview Content) ---
        while (rs.next()) {
            totalRecords++;

            // Always show Product ID
            reportContent.append("\nRecord #").append(totalRecords).append("\n");
            reportContent.append("Product ID: ").append(rs.getString("product_id")).append("\n");
            
            // Show Product Details if checkbox is selected
            if (jCheckBoxProductDetails.isSelected()) {
                reportContent.append("Product Name: ").append(rs.getString("product_name")).append("\n");
                reportContent.append("Category: ").append(rs.getString("category")).append("\n");
                reportContent.append("Location: ").append(rs.getString("location")).append("\n");
            }
            
            // Show Pricing if checkbox is selected
            if (jCheckBoxPricing.isSelected()) {
                reportContent.append("Price: $").append(rs.getBigDecimal("unit_price")).append("\n");
            }
            
            // Show Stock Levels if checkbox is selected
            if (jCheckBoxStockLevels.isSelected()) {
                reportContent.append("Quantity: ").append(rs.getInt("quantity")).append("\n");
            }
            
            // Show Supplier Details if checkbox is selected
            if (jCheckBoxSupplierDetails.isSelected()) {
                reportContent.append("Supplier: ").append(rs.getString("supplier")).append("\n");
            }
            
            // Always show Date Added
            reportContent.append("Date Added: ").append(rs.getString("date_added")).append("\n");
            reportContent.append("-----------------------------------------\n");
        }

        // --- 6. Add Footer and Update GUI Component ---
        reportContent.append("\nTotal Records: ").append(totalRecords).append("\n");
        reportContent.append("End of Report\n");

        // Update the text area
        if (jTextAreaReportOutput != null) {
            jTextAreaReportOutput.setText(reportContent.toString());
            jTextAreaReportOutput.setCaretPosition(0); // Scroll to top
        } else {
            System.out.println("ERROR: Preview text component (jTextAreaReportOutput) not found/initialized.");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
        logger.log(java.util.logging.Level.SEVERE, "Report Generation failed.", e);
    } finally {
        try { if (rs != null) rs.close(); } catch (SQLException e) { /* log */ }
        try { if (pst != null) pst.close(); } catch (SQLException e) { /* log */ }
    }
}
    
    private void generatePreview() {
    try {
        Map<String, Object> data = getReportDataFromDB();
        List<Map<String, Object>> rows = (List<Map<String, Object>>) data.get("rows");
        Map<String, Number> summary = (Map<String, Number>) data.get("summary");

        displayPreviewDialog(rows, summary);

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Preview error: " + ex.getMessage());
        ex.printStackTrace();
    }
}

    
private void displayPreviewDialog(List<Map<String, Object>> rows, Map<String, Number> summary) {

    JDialog dialog = new JDialog(this, "Report Preview", true);
    dialog.setSize(1000, 600);
    dialog.setLocationRelativeTo(this);

    DefaultTableModel model = new DefaultTableModel();
    JTable table = new JTable(model);

    // DYNAMIC COLUMNS

    model.addColumn("Product ID");

    if (jCheckBoxProductDetails.isSelected()) {
        model.addColumn("Product Name");
        model.addColumn("Category");
        model.addColumn("Location");
    }

    if (jCheckBoxPricing.isSelected()) {
        model.addColumn("Unit Price");
    }

    if (jCheckBoxStockLevels.isSelected()) {
        model.addColumn("Quantity");
    }

    if (jCheckBoxSupplierDetails.isSelected()) {
        model.addColumn("Supplier");
    }

    model.addColumn("Date Added");

    // ADD ROWS
    for (Map<String, Object> rowData : rows) {

        List<Object> row = new ArrayList<>();

        row.add(rowData.get("product_id"));

        if (jCheckBoxProductDetails.isSelected()) {
            row.add(rowData.get("product_name"));
            row.add(rowData.get("category"));
            row.add(rowData.get("location"));
        }

        if (jCheckBoxPricing.isSelected()) {
            row.add(rowData.get("unit_price"));
        }

        if (jCheckBoxStockLevels.isSelected()) {
            row.add(rowData.get("quantity"));
        }

        if (jCheckBoxSupplierDetails.isSelected()) {
            row.add(rowData.get("supplier"));
        }

        row.add(rowData.get("date_added"));

        model.addRow(row.toArray());
    }

    dialog.add(new JScrollPane(table), BorderLayout.CENTER);
    dialog.setVisible(true);
}
    

    
    // Create PDF Table
    private PdfPTable createPDFTable() throws Exception {
        // Get data
        ResultSet rs = getReportData();
        
        // Count columns
        int columnCount = 1; // Product ID
        if (jCheckBoxProductDetails.isSelected()) columnCount += 3;
        if (jCheckBoxPricing.isSelected()) columnCount += 1;
        if (jCheckBoxStockLevels.isSelected()) columnCount += 1;
        if (jCheckBoxSupplierDetails.isSelected()) columnCount += 1;
        columnCount += 1; // Date Added
        
        PdfPTable table = new PdfPTable(columnCount);
        table.setWidthPercentage(100);
        
        // Add headers
com.itextpdf.text.Font headerFont = 
    new com.itextpdf.text.Font(
        com.itextpdf.text.Font.FontFamily.HELVETICA, 
        10, 
        com.itextpdf.text.Font.BOLD
    );
        PdfPCell cell;
        
        cell = new PdfPCell(new Phrase("Product ID", headerFont));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        
        if (jCheckBoxProductDetails.isSelected()) {
            table.addCell(new PdfPCell(new Phrase("Product Name", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Category", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Location", headerFont)));
        }
        
        if (jCheckBoxPricing.isSelected()) {
            table.addCell(new PdfPCell(new Phrase("Price", headerFont)));
        }
        
        if (jCheckBoxStockLevels.isSelected()) {
            table.addCell(new PdfPCell(new Phrase("Quantity", headerFont)));
        }
        
        if (jCheckBoxSupplierDetails.isSelected()) {
            table.addCell(new PdfPCell(new Phrase("Supplier", headerFont)));
        }
        
        table.addCell(new PdfPCell(new Phrase("Date Added", headerFont)));
        
        // Add data rows
        while (rs.next()) {
            table.addCell(String.valueOf(rs.getString("product_id")));
            
            if (jCheckBoxProductDetails.isSelected()) {
                table.addCell(rs.getString("product_name"));
                table.addCell(rs.getString("category"));
                table.addCell(rs.getString("location"));
            }
            
            if (jCheckBoxPricing.isSelected()) {
                table.addCell(String.format("%.2f", rs.getDouble("unit_price")));
            }
            
            if (jCheckBoxStockLevels.isSelected()) {
                table.addCell(String.valueOf(rs.getInt("quantity")));
            }
            
            if (jCheckBoxSupplierDetails.isSelected()) {
                table.addCell(rs.getString("supplier"));
            }
            
            table.addCell(rs.getDate("date_added").toString());
        }
        
        return table;
    }
    
    // Generate Excel Report
    private void generateExcelReport() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Excel Report");
            fileChooser.setSelectedFile(new File("Inventory_Report_" + 
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx"));
            
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Inventory Report");
                
                // Create header row
                Row headerRow = sheet.createRow(0);
                int colIndex = 0;
                
                headerRow.createCell(colIndex++).setCellValue("Product ID");
                
                if (jCheckBoxProductDetails.isSelected()) {
                    headerRow.createCell(colIndex++).setCellValue("Product Name");
                    headerRow.createCell(colIndex++).setCellValue("Category");
                    headerRow.createCell(colIndex++).setCellValue("Location");
                }
                
                if (jCheckBoxPricing.isSelected()) {
                    headerRow.createCell(colIndex++).setCellValue("Price");
                }
                
                if (jCheckBoxStockLevels.isSelected()) {
                    headerRow.createCell(colIndex++).setCellValue("Quantity");
                }
                
                if (jCheckBoxSupplierDetails.isSelected()) {
                    headerRow.createCell(colIndex++).setCellValue("Supplier");
                }
                
                headerRow.createCell(colIndex++).setCellValue("Date Added");
                
                // Add data
                ResultSet rs = getReportData();
                int rowIndex = 1;
                
                while (rs.next()) {
                    Row row = sheet.createRow(rowIndex++);
                    colIndex = 0;
                    
                    row.createCell(colIndex++).setCellValue(rs.getString("product_id"));
                    
                    if (jCheckBoxProductDetails.isSelected()) {
                        row.createCell(colIndex++).setCellValue(rs.getString("product_name"));
                        row.createCell(colIndex++).setCellValue(rs.getString("category"));
                        row.createCell(colIndex++).setCellValue(rs.getString("location"));
                    }
                    
                    if (jCheckBoxPricing.isSelected()) {
                        row.createCell(colIndex++).setCellValue(rs.getDouble("unit_price"));
                    }
                    
                    if (jCheckBoxStockLevels.isSelected()) {
                        row.createCell(colIndex++).setCellValue(rs.getInt("quantity"));
                    }
                    
                    if (jCheckBoxSupplierDetails.isSelected()) {
                        row.createCell(colIndex++).setCellValue(rs.getString("supplier"));
                    }
                    
                    row.createCell(colIndex++).setCellValue(rs.getDate("date_added").toString());
                }
                
                // Auto-size columns
                for (int i = 0; i < colIndex; i++) {
                    sheet.autoSizeColumn(i);
                }
                
                // Write to file
                FileOutputStream outputStream = new FileOutputStream(file);
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
                
                JOptionPane.showMessageDialog(this, "Excel Report generated successfully!");
                
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating Excel report: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Generate CSV Report
    private void generateCSVReport() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save CSV Report");
            fileChooser.setSelectedFile(new File("Inventory_Report_" + 
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv"));
            
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                PrintWriter writer = new PrintWriter(file);
                
                // Write header
                StringBuilder header = new StringBuilder("Product ID");
                
                if (jCheckBoxProductDetails.isSelected()) {
                    header.append(",Product Name,Category,Location");
                }
                if (jCheckBoxPricing.isSelected()) {
                    header.append(",Price");
                }
                if (jCheckBoxStockLevels.isSelected()) {
                    header.append(",Quantity");
                }
                if (jCheckBoxSupplierDetails.isSelected()) {
                    header.append(",Supplier");
                }
                header.append(",Date Added");
                
                writer.println(header.toString());
                
                // Write data
                ResultSet rs = getReportData();
                
                while (rs.next()) {
                    StringBuilder line = new StringBuilder();
                    line.append(rs.getString("product_id"));
                    
                    if (jCheckBoxProductDetails.isSelected()) {
                        line.append(",\"").append(rs.getString("product_name")).append("\"");
                        line.append(",\"").append(rs.getString("category")).append("\"");
                        line.append(",\"").append(rs.getString("location")).append("\"");
                    }
                    if (jCheckBoxPricing.isSelected()) {
                        line.append(",").append(rs.getDouble("unit_price"));
                    }
                    if (jCheckBoxStockLevels.isSelected()) {
                        line.append(",").append(rs.getInt("quantity"));
                    }
                    if (jCheckBoxSupplierDetails.isSelected()) {
                        line.append(",\"").append(rs.getString("supplier")).append("\"");
                    }
                    line.append(",").append(rs.getDate("date_added"));
                    
                    writer.println(line.toString());
                }
                
                writer.close();
                
                JOptionPane.showMessageDialog(this, "CSV Report generated successfully!");
                
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating CSV report: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    // Print Report
    private void printReport() {
        try {
            // Create table with data
            ResultSet rs = getReportData();
            DefaultTableModel model = new DefaultTableModel();
            
            // Add columns
            model.addColumn("Product ID");
            if (jCheckBoxProductDetails.isSelected()) {
                model.addColumn("Product Name");
                model.addColumn("Category");
                model.addColumn("Location");
            }
            if (jCheckBoxPricing.isSelected()) {
                model.addColumn("Price");
            }
            if (jCheckBoxStockLevels.isSelected()) {
                model.addColumn("Quantity");
            }
            if (jCheckBoxSupplierDetails.isSelected()) {
                model.addColumn("Supplier");
            }
            model.addColumn("Date Added");
            
            // Populate data
            while (rs.next()) {
                Object[] row = new Object[model.getColumnCount()];
                int colIndex = 0;
                
                row[colIndex++] = rs.getString("product_id");
                if (jCheckBoxProductDetails.isSelected()) {
                    row[colIndex++] = rs.getString("product_name");
                    row[colIndex++] = rs.getString("category");
                    row[colIndex++] = rs.getString("location");
                }
                if (jCheckBoxPricing.isSelected()) {
                    row[colIndex++] = rs.getDouble("unit_price");
                }
                if (jCheckBoxStockLevels.isSelected()) {
                    row[colIndex++] = rs.getInt("quantity");
                }
                if (jCheckBoxSupplierDetails.isSelected()) {
                    row[colIndex++] = rs.getString("supplier");
                }
                row[colIndex++] = rs.getDate("date_added");
                
                model.addRow(row);
            }
            
            JTable printTable = new JTable(model);
            printTable.print();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error printing report: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Get report data based on filters
    private ResultSet getReportData() throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT p.product_id, p.product_name, p.category, p.location, ");
        query.append("unit_price, p.quantity, p.supplier, p.date_added ");
        query.append("FROM products p WHERE 1=1 ");
        
        if (jDateChooserStart.getDate() != null && jDateChooserEnd.getDate() != null) {
            query.append("AND p.date_added BETWEEN ? AND ? ");
        }
        
        if (jComboBoxCategory.getSelectedIndex() > 0) {
            query.append("AND p.category = ? ");
        }
        
        if (jComboBoxLocation.getSelectedIndex() > 0) {
            query.append("AND p.location = ? ");
        }
        
        String sortBy = jComboBoxSortBy.getSelectedItem().toString();
        if (sortBy.equals("Product Name")) {
            query.append("ORDER BY p.product_name ");
        } else if (sortBy.equals("Category")) {
            query.append("ORDER BY p.category ");
        } else if (sortBy.equals("Price")) {
            query.append("ORDER BY p.unit_price ");
        } else if (sortBy.equals("Quantity")) {
            query.append("ORDER BY p.quantity ");
        } else {
            query.append("ORDER BY p.date_added ");
        }
        
        String order = jComboBoxOrder.getSelectedItem().toString();
        query.append(order.equals("Ascending") ? "ASC" : "DESC");
        
        pst = conn.prepareStatement(query.toString());
        
        int paramIndex = 1;
        
        if (jDateChooserStart.getDate() != null && jDateChooserEnd.getDate() != null) {
            pst.setDate(paramIndex++, new java.sql.Date(jDateChooserStart.getDate().getTime()));
            pst.setDate(paramIndex++, new java.sql.Date(jDateChooserEnd.getDate().getTime()));
        }
        
        if (jComboBoxCategory.getSelectedIndex() > 0) {
            pst.setString(paramIndex++, jComboBoxCategory.getSelectedItem().toString());
        }
        
        if (jComboBoxLocation.getSelectedIndex() > 0) {
            pst.setString(paramIndex++, jComboBoxLocation.getSelectedItem().toString());
        }
        
        return pst.executeQuery();
    }
    
    // Generate Executive Summary
    private String generateExecutiveSummary() {
        try {
            StringBuilder summary = new StringBuilder();
            
            String query = "SELECT COUNT(*) as total_products, " +
                          "SUM(quantity) as total_quantity, " +
                          "AVG(price) as avg_price, " +
                          "MIN(price) as min_price, " +
                          "MAX(price) as max_price " +
                          "FROM products WHERE 1=1 ";
            
            if (jDateChooserStart.getDate() != null && jDateChooserEnd.getDate() != null) {
                query += "AND date_added BETWEEN ? AND ? ";
            }
            
            PreparedStatement pst = conn.prepareStatement(query);
            
            if (jDateChooserStart.getDate() != null && jDateChooserEnd.getDate() != null) {
                pst.setDate(1, new java.sql.Date(jDateChooserStart.getDate().getTime()));
                pst.setDate(2, new java.sql.Date(jDateChooserEnd.getDate().getTime()));
            }
            
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                summary.append("Total Products: ").append(rs.getInt("total_products")).append("\n");
                summary.append("Total Stock Quantity: ").append(rs.getInt("total_quantity")).append("\n");
                summary.append("Average Price: $").append(String.format("%.2f", rs.getDouble("avg_price"))).append("\n");
                summary.append("Price Range: $").append(String.format("%.2f", rs.getDouble("min_price")))
                       .append(" - $").append(String.format("%.2f", rs.getDouble("max_price"))).append("\n");
            }
            
            return summary.toString();
            
        } catch (Exception e) {
            return "Error generating summary: " + e.getMessage();
        }
    }
    
    
    private void savePDFWithDialog() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save PDF Report");
    fileChooser.setSelectedFile(new File("InventoryReport.pdf")); // default name
    
    int userSelection = fileChooser.showSaveDialog(this);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();
        generatePDFInBackground(fileToSave.getAbsolutePath());
    }
}

    // Add this inside your class (e.g., ReportForm)
private void generatePDF(String filePath) throws Exception {
    // Example: just create a blank PDF
    com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
    com.itextpdf.text.pdf.PdfWriter.getInstance(doc, new java.io.FileOutputStream(filePath));
    doc.open();
    doc.add(new com.itextpdf.text.Paragraph("Inventory Report PDF"));
    doc.close();
}



    private void generatePDFInBackground(String filePath) {
    // Create a modal processing dialog
    final JDialog processingDialog = new JDialog(this, "Generating PDF...", true);
    JLabel lbl = new JLabel("Please wait, generating PDF...");
    lbl.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    processingDialog.add(lbl);
    processingDialog.pack();
    processingDialog.setLocationRelativeTo(this);

    // SwingWorker to generate PDF in background
    SwingWorker<Void, Void> worker = new SwingWorker<>() {
        @Override
        protected Void doInBackground() {
            try {
                generatePDF(filePath); // your existing PDF generation code
            } catch (Exception e) {
                JOptionPane.showMessageDialog(ReportForm.this, 
                    "Error generating PDF: " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void done() {
            processingDialog.dispose();
            JOptionPane.showMessageDialog(ReportForm.this, 
                "PDF generated successfully!\nSaved at: " + filePath);
            
        }
    };

    worker.execute();
    processingDialog.setVisible(true); // show dialog while worker runs
}

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
        java.awt.EventQueue.invokeLater(() -> new ReportForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CustomPsnel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel detailsPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JCheckBox jCheckBoxPricing;
    private javax.swing.JCheckBox jCheckBoxProductDetails;
    private javax.swing.JCheckBox jCheckBoxStockLevels;
    private javax.swing.JCheckBox jCheckBoxSupplierDetails;
    private javax.swing.JComboBox<String> jComboBoxCategory;
    private javax.swing.JComboBox<String> jComboBoxLocation;
    private javax.swing.JComboBox<String> jComboBoxOrder;
    private javax.swing.JComboBox<String> jComboBoxSortBy;
    private com.toedter.calendar.JDateChooser jDateChooserEnd;
    private com.toedter.calendar.JDateChooser jDateChooserStart;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButton jRadioButtonCSV;
    private javax.swing.JRadioButton jRadioButtonExcel;
    private javax.swing.JRadioButton jRadioButtonPDF;
    private javax.swing.JRadioButton jRadioButtonPrint;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaReportOutput;
    private javax.swing.JLabel lblCustomRange;
    private javax.swing.JLabel lblDash;
    private javax.swing.JLabel lblInventory;
    private javax.swing.JLabel lblLogout;
    private javax.swing.JLabel lblQRScan;
    private javax.swing.JLabel lblReports;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSettings;
    private javax.swing.JLabel lblThisMonth;
    private javax.swing.JLabel lblThisWeek;
    private javax.swing.JLabel lblThisYear;
    private javax.swing.JLabel lblToday;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblUsers;
    private javax.swing.JPanel previewPanel;
    // End of variables declaration//GEN-END:variables
}
