/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package InventoryTrend;
 
// For JFreeChart
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.awt.Dimension;
import dashboard.Dashboard;
import products.ProductForm;
import QrScanner.QRCodeScanner;
import Settings.SettingsForm;
import inventory.StockInOutForm;
import reports.ReportForm;
import users.userForm;
import login.LoginForm;
import utils.RoundedPanel;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Font;
import utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Cursor;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.print.*;
import java.awt.Graphics;
import javax.swing.JOptionPane;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Graphics2D;
import java.awt.Desktop;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileOutputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;

import java.util.List; // Make sure this is java.util.List
import java.util.ArrayList; // Make sure this is java.util.List
import utils.TransitionHelper;

/**
 *
 * @author ADMIN
 */
public class InventoryTrendForm extends javax.swing.JFrame {
    
     private String username;
    private String role;
    
      private Connection con;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(InventoryTrendForm.class.getName());

    /**
     * Creates new form InventoryTrendForm
     */
    
    
    public InventoryTrendForm() {
        initComponents();
        
          con = DBConnection.getConnection(); // DB connection 
        
        // initialize UI data
    loadCategories();
  
loadCategories();
loadTrendSummary("All Time");  // Add default time range
loadRecentStockMovements("All", "All Time"); 

  


   
    }
    
     public InventoryTrendForm (String username, String role) {  // Accept parameters
         
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
    
   //Set layout BEFORE loading data
    chartDrawPanel.setLayout(new BorderLayout());
    chartDrawPanel.setBackground(new Color(52, 73, 94));
    
    // Initialize data
    loadCategories();
    loadTrendSummary("All Time");
    loadRecentStockMovements("All", "All Time");
    
    // Load chart - this will now work
    loadStockTrendChart("All", "All Time");


    // listeners
    // In your constructor, update the listeners:
cmbTimeRange.addActionListener(e -> refreshData());

cmbCategory.addActionListener(e -> refreshData());


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
                InventoryTrendForm.this,
                "Do you really want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                TransitionHelper.fadeToFrame(InventoryTrendForm.this, new LoginForm(), 500);
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
    
      
  private void refreshData() {
    String selectedCategory = (cmbCategory.getSelectedItem() == null) 
        ? "All" 
        : cmbCategory.getSelectedItem().toString();
    
    String selectedTimeRange = (cmbTimeRange.getSelectedItem() == null)
        ? "All Time"
        : cmbTimeRange.getSelectedItem().toString();
    
    loadTrendSummary(selectedTimeRange);
    loadRecentStockMovements(selectedCategory, selectedTimeRange);
    // **MODIFICATION HERE: Pass both category and time range**
    loadStockTrendChart(selectedCategory, selectedTimeRange); 
}
  
  
private void loadCategories() {
    try {
        cmbCategory.removeAllItems();
        cmbCategory.addItem("All");

        // CORRECTED: Use 'category' column name
        String sql = "SELECT DISTINCT category FROM products ORDER BY category"; 
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                // CORRECTED: Retrieve using 'category'
                String category = rs.getString("category"); 
                if (category != null && !category.trim().isEmpty()) {
                    cmbCategory.addItem(category);
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}



// Enhanced loadStockTrendChart method with better handling
private void loadStockTrendChart(String category, String timeRange) {
    // Clear previous chart
    chartDrawPanel.removeAll();
    
    //DEFINE CONSISTENT SIZE FOR ALL STATES
    final int CHART_WIDTH = 430;   // Fixed width
    final int CHART_HEIGHT = 250;  // Fixed height
    
    // Get data from database
    Map<String, Integer> trendData = getTrendChartData(category, timeRange);
    
    // Check if we have data
    if (trendData == null || trendData.isEmpty()) {
        // *** NO DATA PANEL WITH SAME SIZE AS CHART ***
        JPanel noDataPanel = new JPanel(new BorderLayout());
        noDataPanel.setBackground(new Color(52, 73, 94));
        noDataPanel.setPreferredSize(new Dimension(CHART_WIDTH, CHART_HEIGHT));
        noDataPanel.setMinimumSize(new Dimension(CHART_WIDTH, CHART_HEIGHT));
        noDataPanel.setMaximumSize(new Dimension(CHART_WIDTH, CHART_HEIGHT));
        
        JLabel noDataLabel = new JLabel("<html><center><b style='font-size:16px;'>No stock movement data available</b><br>" +
                "<span style='font-size:13px;'>for selected filters</span><br><br>" +
                "<small style='font-size:11px;'>Try selecting 'All' category or 'All Time'</small></center></html>", 
                SwingConstants.CENTER);
        noDataLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        noDataLabel.setForeground(Color.WHITE);
        
        noDataPanel.add(noDataLabel, BorderLayout.CENTER);
        chartDrawPanel.add(noDataPanel, BorderLayout.CENTER);
        chartDrawPanel.revalidate();
        chartDrawPanel.repaint();
        return;
    }
    
    try {
        // Create dataset for the chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Format dates nicely
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd");
        
        for (Map.Entry<String, Integer> entry : trendData.entrySet()) {
            String dateLabel = entry.getKey();
            try {
                Date date = inputFormat.parse(entry.getKey());
                dateLabel = outputFormat.format(date);
            } catch (Exception e) {
                // Use original if parsing fails
            }
            dataset.addValue(entry.getValue(), "Net Stock Change", dateLabel);
        }
        
        // Create the line chart
        JFreeChart lineChart = ChartFactory.createLineChart(
            "Stock Movement Trend (" + trendData.size() + " data points)",
            "Date",
            "Net Change (IN - OUT)",
            dataset,
            org.jfree.chart.plot.PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        // Customize chart appearance
        lineChart.setBackgroundPaint(new Color(52, 73, 94));
        lineChart.getTitle().setPaint(Color.WHITE);
        lineChart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Get the plot and customize it
        org.jfree.chart.plot.CategoryPlot plot = lineChart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(new Color(200, 200, 200));
        plot.setRangeGridlinePaint(new Color(200, 200, 200));
        plot.setOutlineVisible(false);
        
        // Customize the renderer
        org.jfree.chart.renderer.category.LineAndShapeRenderer renderer = 
            new org.jfree.chart.renderer.category.LineAndShapeRenderer();
        
        // Choose rendering style based on data points
        if (trendData.size() == 1) {
            renderer.setSeriesLinesVisible(0, false);
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-5, -5, 10, 10));
        } else if (trendData.size() == 2) {
            renderer.setSeriesLinesVisible(0, true);
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-4, -4, 8, 8));
        } else {
            renderer.setSeriesLinesVisible(0, true);
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-3, -3, 6, 6));
        }
        
        renderer.setSeriesPaint(0, new Color(0, 150, 136));
        renderer.setSeriesStroke(0, new java.awt.BasicStroke(2.5f));
        plot.setRenderer(renderer);
        
        // Customize domain axis (X-axis)
        org.jfree.chart.axis.CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));
        domainAxis.setLabelFont(new Font("Segoe UI", Font.BOLD, 11));
        domainAxis.setTickLabelPaint(Color.WHITE);  // X-axis tick labels color
        domainAxis.setLabelPaint(Color.WHITE);       // X-axis label color
        
        if (trendData.size() > 7) {
            domainAxis.setCategoryLabelPositions(
                org.jfree.chart.axis.CategoryLabelPositions.UP_45
            );
        } else {
            domainAxis.setCategoryLabelPositions(
                org.jfree.chart.axis.CategoryLabelPositions.STANDARD
            );
        }
        
        // Customize range axis (Y-axis)
        org.jfree.chart.axis.NumberAxis rangeAxis = 
            (org.jfree.chart.axis.NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));
        rangeAxis.setLabelFont(new Font("Segoe UI", Font.BOLD, 11));
        rangeAxis.setTickLabelPaint(Color.WHITE);   // Y-axis tick labels color
        rangeAxis.setLabelPaint(Color.WHITE);        // Y-axis label color
        rangeAxis.setStandardTickUnits(org.jfree.chart.axis.NumberAxis.createIntegerTickUnits());
        
        // Add zero baseline
        plot.setRangeZeroBaselineVisible(true);
        plot.setRangeZeroBaselinePaint(Color.GRAY);
        plot.setRangeZeroBaselineStroke(new java.awt.BasicStroke(1.0f));
        
        // Legend customization
        lineChart.getLegend().setBackgroundPaint(new Color(52, 73, 94));
        lineChart.getLegend().setItemFont(new Font("Segoe UI", Font.PLAIN, 11));
        lineChart.getLegend().setItemPaint(Color.WHITE);
        lineChart.getLegend().setFrame(org.jfree.chart.block.BlockBorder.NONE);
        
        //CREATE CHARTPANEL WITH FIXED CONSISTENT SIZE
        ChartPanel chartPanelComponent = new ChartPanel(lineChart);
        chartPanelComponent.setPreferredSize(new Dimension(CHART_WIDTH, CHART_HEIGHT));
        chartPanelComponent.setMinimumSize(new Dimension(CHART_WIDTH, CHART_HEIGHT));
        chartPanelComponent.setMaximumSize(new Dimension(CHART_WIDTH, CHART_HEIGHT));
        chartPanelComponent.setBackground(new Color(52, 73, 94));
        chartPanelComponent.setMouseWheelEnabled(true);
        chartPanelComponent.setDomainZoomable(false);
        chartPanelComponent.setRangeZoomable(false);
        
        //DISABLE AUTO-SCALING TO MAINTAIN SIZE 
        chartPanelComponent.setFillZoomRectangle(false);
        chartPanelComponent.setMinimumDrawWidth(CHART_WIDTH);
        chartPanelComponent.setMaximumDrawWidth(CHART_WIDTH);
        chartPanelComponent.setMinimumDrawHeight(CHART_HEIGHT);
        chartPanelComponent.setMaximumDrawHeight(CHART_HEIGHT);
        
        chartPanelComponent.setToolTipText(
            "<html>Chart showing stock movement trends<br>" +
            "Positive values = Stock IN exceeds Stock OUT<br>" +
            "Negative values = Stock OUT exceeds Stock IN</html>"
        );
        
        // Add chart to panel
        chartDrawPanel.add(chartPanelComponent, BorderLayout.CENTER);
        
    } catch (Exception e) {
        e.printStackTrace();
        
        // ERROR PANEL WITH SAME SIZE AS CHART 
        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setBackground(new Color(52, 73, 94));
        errorPanel.setPreferredSize(new Dimension(CHART_WIDTH, CHART_HEIGHT));
        errorPanel.setMinimumSize(new Dimension(CHART_WIDTH, CHART_HEIGHT));
        errorPanel.setMaximumSize(new Dimension(CHART_WIDTH, CHART_HEIGHT));
        
        JLabel errorLabel = new JLabel(
            "<html><center><b style='font-size:16px;'>Error creating chart</b><br><br>" + 
            "<span style='font-size:12px;'>" + e.getMessage() + "</span></center></html>", 
            SwingConstants.CENTER
        );
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        errorLabel.setForeground(Color.RED);
        
        errorPanel.add(errorLabel, BorderLayout.CENTER);
        chartDrawPanel.add(errorPanel, BorderLayout.CENTER);
    }
    
    // Refresh the panel
    chartDrawPanel.revalidate();
    chartDrawPanel.repaint();
}

// Also add this helper method to show chart statistics
private void displayChartInfo(Map<String, Integer> data) {
    if (data.isEmpty()) {
        System.out.println("No chart data available");
        return;
    }
    
    System.out.println("=== Chart Data Summary ===");
    System.out.println("Total data points: " + data.size());
    
    int totalIncrease = 0;
    int totalDecrease = 0;
    
    for (Map.Entry<String, Integer> entry : data.entrySet()) {
        System.out.println(entry.getKey() + " → " + entry.getValue());
        if (entry.getValue() > 0) {
            totalIncrease += entry.getValue();
        } else {
            totalDecrease += Math.abs(entry.getValue());
        }
    }
    
    System.out.println("Total positive movement: +" + totalIncrease);
    System.out.println("Total negative movement: -" + totalDecrease);
    System.out.println("Net movement: " + (totalIncrease - totalDecrease));
    System.out.println("========================");
}

// STEP 3: Your getTrendChartData method looks correct, but here's a verified version:
private Map<String, Integer> getTrendChartData(String category, String timeRange) {
    Map<String, Integer> chartData = new LinkedHashMap<>();
    
    String sql = 
        "SELECT DATE(h.date_time) AS d, " +
        "SUM(CASE WHEN h.action_type='IN' THEN h.quantity ELSE -h.quantity END) AS net_change " +
        "FROM stock_history h " +
        "JOIN products p ON h.product_id = p.product_id " +
        "WHERE 1=1 ";
    
    List<String> params = new ArrayList<>();
    
    // Category filter
    if (category != null && !category.equalsIgnoreCase("All")) {
        sql += " AND p.category = ? ";
        params.add(category);
    }
    
    // Time Range filter
    sql += getTimeRangeSQL(timeRange);
    sql += " GROUP BY DATE(h.date_time) ORDER BY d ASC ";
    
    try (Connection con = DBConnection.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {
        
        // Set parameters
        for (int i = 0; i < params.size(); i++) {
            pst.setString(i + 1, params.get(i));
        }
        
        ResultSet rs = pst.executeQuery();
        
        // Fetch results
        while (rs.next()) {
            String date = rs.getString("d");
            int netChange = rs.getInt("net_change");
            chartData.put(date, netChange);
        }
        
        rs.close();
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Error loading chart data: " + e.getMessage(),
            "Database Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    return chartData;
}


 public void loadRecentStockMovements(String category, String timeRange) {
    DefaultTableModel model = (DefaultTableModel) trendTable.getModel();
    model.setRowCount(0);

   // CORRECTED: Using aliases p (products) and h (stock_history) with correct column names.
    String sql = "SELECT p.product_name, p.category, p.quantity AS current_stock, "
        + "h.action_type, h.quantity AS changed_amount, h.date_time "
        + "FROM stock_history h "
        + "JOIN products p ON h.product_id = p.product_id " 
        + "WHERE 1=1 ";
    
    // Add category filter
    boolean hasCategory = category != null && !category.equalsIgnoreCase("All");
    if (hasCategory) {
        sql += "AND p.category = ? ";
    }
    
    // Add time range filter
    sql += getTimeRangeSQL(timeRange);
    sql += "ORDER BY h.date_time DESC LIMIT 100";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {

        if (hasCategory) {
          pst.setString(1, category); 
        }

        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                String actionType = rs.getString("action_type");
                int changeAmt = rs.getInt("changed_amount");

                String changeText;
                String trendText;

                if ("IN".equalsIgnoreCase(actionType)) {
                    changeText = "+" + changeAmt;
                    trendText = "↑ Up";
                } else if ("OUT".equalsIgnoreCase(actionType)) {
                    changeText = "-" + changeAmt;
                    trendText = "↓ Down";
                } else {
                    changeText = String.valueOf(changeAmt);
                    trendText = "→ Stable";
                }

                model.addRow(new Object[] {
                    rs.getString("product_name"),
                    rs.getString("category"),
                    rs.getInt("current_stock"),
                    changeText,
                    trendText,
                    rs.getString("date_time")
                });
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Error loading stock movements: " + e.getMessage(),
            "Database Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}
 
 // STEP 4: Make sure your getTimeRangeSQL is correct:
private String getTimeRangeSQL(String timeRange) {
    if (timeRange == null) {
        return "";
    }
    
    switch (timeRange) {
        case "Today":
            return " AND DATE(h.date_time) = CURDATE() ";
        case "This Week":
            return " AND YEARWEEK(h.date_time, 1) = YEARWEEK(CURDATE(), 1) ";
        case "This Month":
            return " AND YEAR(h.date_time) = YEAR(CURDATE()) " +
                   " AND MONTH(h.date_time) = MONTH(CURDATE()) ";
        case "Last 7 Days":
            return " AND h.date_time >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) ";
        case "Last 30 Days":
            return " AND h.date_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) ";
        case "Last 90 Days":
            return " AND h.date_time >= DATE_SUB(CURDATE(), INTERVAL 90 DAY) ";
        case "Last Year":
            return " AND h.date_time >= DATE_SUB(CURDATE(), INTERVAL 1 YEAR) ";
        case "All Time":
        default:
            return ""; // No filter
    }
}

 
 /**
 * Generates time filter for queries *without* the 'h' alias (used by loadTrendSummary).
 */
private String getSummaryTimeRangeSQL(String timeRange) {
    if (timeRange == null || timeRange.equals("All Time")) {
        return "";
    }

    // Used by loadTrendSummary (no table alias used for stock_history)
    String timeColumn = "date_time"; 

    switch (timeRange) {
        case "Last 7 Days":
            return "AND " + timeColumn + " >= DATE_SUB(NOW(), INTERVAL 7 DAY) ";
        case "Last 30 Days":
            return "AND " + timeColumn + " >= DATE_SUB(NOW(), INTERVAL 30 DAY) ";
        case "Last 90 Days":
            return "AND " + timeColumn + " >= DATE_SUB(NOW(), INTERVAL 90 DAY) ";
        case "Last Year":
            return "AND " + timeColumn + " >= DATE_SUB(NOW(), INTERVAL 1 YEAR) ";
        default:
            return "";
    }
}

private void loadTrendSummary(String timeRange) {
    int totalStock = 0;
    int itemCount = 0;
    int categoryCount = 0;
    int increase = 0;
    int decrease = 0;

   String sqlProducts = "SELECT SUM(quantity) AS totalQty, COUNT(*) AS items FROM products";
    String sqlCategories = "SELECT COUNT(DISTINCT category) AS cat FROM products";
    
    // Option B: Query only stock_history and use a non-aliased time column (Best for this method)
String sqlHistory = "SELECT action_type, quantity FROM stock_history WHERE 1=1 " 
    + getSummaryTimeRangeSQL(timeRange); // <-- Use a new helper method
    
    try (Connection con = DBConnection.getConnection()) {

        // Total stock + items
        try (PreparedStatement pst = con.prepareStatement(sqlProducts);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                totalStock = rs.getInt("totalQty");
                itemCount = rs.getInt("items");
            }
        }

        // Unique categories
        try (PreparedStatement pst = con.prepareStatement(sqlCategories);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                categoryCount = rs.getInt("cat");
            }
        }

        // History for selected time range
        try (PreparedStatement pst = con.prepareStatement(sqlHistory);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                String type = rs.getString("action_type");
                int qty = rs.getInt("quantity");

                if ("IN".equalsIgnoreCase(type)) increase += qty;
                else if ("OUT".equalsIgnoreCase(type)) decrease += qty;
            }
        }

        int trendValue = increase - decrease;
        String trend = "Stable";
        int percentage = 0;
        if (totalStock > 0) percentage = (trendValue * 100) / totalStock;
        if (trendValue > 0) trend = "↑ Growing";
        else if (trendValue < 0) trend = "↓ Declining";

        lblTotalStock.setText(String.valueOf(totalStock));
        lblAvgItem.setText(String.valueOf(itemCount == 0 ? 0 : totalStock / itemCount));
        lblCategoryCount.setText(String.valueOf(categoryCount));
        lblTrendStatus.setText(trend + " (" + (trendValue >= 0 ? "+" : "") + percentage + "%)");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Error loading summary: " + e.getMessage(),
            "Database Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}



// helper used by previous code paths that expected updateTable(ResultSet)
private void updateTable(ResultSet rs) {
    DefaultTableModel model = (DefaultTableModel) trendTable.getModel();
    model.setRowCount(0);
    try {
        while (rs.next()) {
            String actionType = rs.getString("action_type");
            int changeAmt = rs.getInt("changed_amount");

            String changeText = " " + changeAmt;
            String trendText = "Stable";
            if ("IN".equalsIgnoreCase(actionType)) {
                changeText = "+" + changeAmt;
                trendText = "Up";
            } else if ("OUT".equalsIgnoreCase(actionType)) {
                changeText = "-" + changeAmt;
                trendText = "Down";
            }

            model.addRow(new Object[] {
                rs.getString("product_name"),
                rs.getString("category"),
                rs.getInt("current_stock"),
                changeText,
                trendText,
                rs.getString("date_time ")
            });
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}


private ChartPanel createChartPanel(Map<String, Integer> data) {
    TimeSeries series = new TimeSeries("Net Stock Change");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    for (Map.Entry<String, Integer> entry : data.entrySet()) {
        try {
            Date date = dateFormat.parse(entry.getKey());
            // JFreeChart's Day object is used for daily time series
            series.add(new Day(date), entry.getValue()); 
        } catch (java.text.ParseException ex) {
            // Log error if date parsing fails
            logger.log(Level.SEVERE, "Error parsing date for chart: " + entry.getKey(), ex);
        }
    }

    TimeSeriesCollection dataset = new TimeSeriesCollection();
    dataset.addSeries(series);

    // Create the line chart
    JFreeChart chart = ChartFactory.createTimeSeriesChart(
        "Stock Trend Over Time", // Chart title
        "Date",                  // X-axis label
        "Net Change (IN - OUT)", // Y-axis label
        dataset,                 // Chart data
        true,                    // Include legend
        true,                    // Include tooltips
        false                    // Include URLs (not needed)
    );

    // Customize the chart (optional)
    chart.getPlot().setBackgroundPaint(new Color(240, 240, 240));

    // Create a Swing panel that holds the chart
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setPreferredSize(new Dimension(chartDrawPanel.getWidth(), chartDrawPanel.getHeight()));
    return chartPanel;
}

// OPTIONAL: Test method to verify chart rendering works
public void testChartWithDummyData() {
    chartDrawPanel.removeAll();
    chartDrawPanel.setLayout(new BorderLayout());
    
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    dataset.addValue(50, "Stock", "2025-12-01");
    dataset.addValue(75, "Stock", "2025-12-02");
    dataset.addValue(30, "Stock", "2025-12-03");
    dataset.addValue(90, "Stock", "2025-12-04");
    
    JFreeChart chart = ChartFactory.createLineChart(
        "Test Chart", "Date", "Value", dataset
    );
    
    ChartPanel cp = new ChartPanel(chart);
    chartDrawPanel.add(cp, BorderLayout.CENTER);
    chartDrawPanel.revalidate();
    chartDrawPanel.repaint();
}

private void exportReportToPDF() {
    // Define the save directory
    String saveDirectory = "C:\\Users\\ADMIN\\Desktop\\Virdha\\Virdha Personal\\Java Projects\\NetBeans Projects\\InventoryQRSystem\\InventoryTrendsReport\\";
    
    // Create directory if it doesn't exist
    java.io.File directory = new java.io.File(saveDirectory);
    if (!directory.exists()) {
        directory.mkdirs(); // Create the folder
    }
    
    // Generate filename with timestamp
    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String timeRange = cmbTimeRange.getSelectedItem().toString().replace(" ", "_");
    String category = cmbCategory.getSelectedItem().toString().replace(" ", "_");
    
    String fileName = "InventoryTrends_" + timeRange + "_" + category + "_" + timestamp + ".pdf";
    java.io.File fileToSave = new java.io.File(saveDirectory + fileName);
        
        try {
            // Create PDF document in LANDSCAPE mode
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, new FileOutputStream(fileToSave));
            document.open();
            
            // Define fonts
            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.NORMAL);
            
            // ========== ADD LOGO/IMAGE (OPTIONAL) ==========
            try {
                // Option 1: Load from file path
                String logoPath = "src/InventoryImages/logo.jpg"; //logo path
                Image logo = Image.getInstance(logoPath);
                logo.scaleToFit(80, 80); // Adjust size
                logo.setAlignment(Element.ALIGN_CENTER);
                logo.setSpacingAfter(10);
                document.add(logo);
            } catch (Exception e) {
                // If logo not found, skip it (no error)
                System.out.println("Logo not found, continuing without it...");
            }
            
            // ========== TITLE ==========
            Paragraph title = new Paragraph("Inventory Trends Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // ========== REPORT INFO ==========
            document.add(new Paragraph("Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), normalFont));
            document.add(new Paragraph("User: " + username + " (" + role + ")", normalFont));
            document.add(new Paragraph("Time Range: " + cmbTimeRange.getSelectedItem(), normalFont));
            document.add(new Paragraph("Category: " + cmbCategory.getSelectedItem(), normalFont));
            document.add(new Paragraph(" ")); // Space
            document.add(new Paragraph(" ")); // Space
            document.add(new Paragraph(" ")); // Space
            
            // ========== SUMMARY STATISTICS ==========
            Paragraph statsHeader = new Paragraph("Summary Statistics", headerFont);
            statsHeader.setSpacingAfter(10); // More space
            document.add(statsHeader);
            
            PdfPTable statsTable = new PdfPTable(4);
            statsTable.setWidthPercentage(80);
            statsTable.setSpacingAfter(25);  // Increase spacing
            statsTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            
            // Header row
            PdfPCell headerCell = new PdfPCell(new Phrase("Metric", headerFont));
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell.setPadding(5);
            statsTable.addCell(headerCell);
            
            headerCell = new PdfPCell(new Phrase("Value", headerFont));
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell.setPadding(5);
            statsTable.addCell(headerCell);
            
            headerCell = new PdfPCell(new Phrase("Metric", headerFont));
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell.setPadding(5);
            statsTable.addCell(headerCell);
            
            headerCell = new PdfPCell(new Phrase("Value", headerFont));
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell.setPadding(5);
            statsTable.addCell(headerCell);
            
            // Data rows
            statsTable.addCell(new Phrase("Total Stock", normalFont));
            statsTable.addCell(new Phrase(lblTotalStock.getText(), normalFont));
            statsTable.addCell(new Phrase("Categories", normalFont));
            statsTable.addCell(new Phrase(lblCategoryCount.getText(), normalFont));
            
            statsTable.addCell(new Phrase("Avg per Item", normalFont));
            statsTable.addCell(new Phrase(lblAvgItem.getText(), normalFont));
            statsTable.addCell(new Phrase("Trend Status", normalFont));
            statsTable.addCell(new Phrase(lblTrendStatus.getText(), normalFont));
            
            document.add(statsTable);
            
            // ========== START NEW PAGE FOR TABLE ==========
            document.newPage();
            
            // ========== CHART ==========
            document.add(new Paragraph(" ")); // Space
            document.add(new Paragraph(" ")); // Space
            document.add(new Paragraph(" ")); // Space
            Paragraph chartHeader = new Paragraph("Stock Movement Chart", headerFont);
            chartHeader.setSpacingAfter(10); // More space before chart
            document.add(chartHeader);
            
            // Capture chart as image
            BufferedImage chartImage = new BufferedImage(
                chartDrawPanel.getWidth(),
                chartDrawPanel.getHeight(),
                BufferedImage.TYPE_INT_RGB
            );
            Graphics2D g2d = chartImage.createGraphics();
            chartDrawPanel.printAll(g2d);
            g2d.dispose();
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(chartImage, "png", baos);
            Image pdfImage = Image.getInstance(baos.toByteArray());
            
            // Scale chart to fit width nicely
            float chartWidth = 450;
            float chartHeight = 250;
            pdfImage.scaleToFit(chartWidth, chartHeight);
            pdfImage.setAlignment(Element.ALIGN_CENTER);
            pdfImage.setSpacingAfter(30); // More space after the Chart
            document.add(pdfImage);
            
            // ========== START NEW PAGE FOR TABLE ==========
            document.newPage();
            
            // ========== DATA TABLE ==========
            document.add(new Paragraph(" ")); // Space
            document.add(new Paragraph(" ")); // Space
            Paragraph tableHeader = new Paragraph("Recent Stock Movements", headerFont);
            tableHeader.setSpacingAfter(10);
            document.add(tableHeader);
            
            DefaultTableModel model = (DefaultTableModel) trendTable.getModel();
            
            if (model.getRowCount() > 0) {
                PdfPTable dataTable = new PdfPTable(6);
                dataTable.setWidthPercentage(100);
                dataTable.setWidths(new float[]{3f, 2f, 1.5f, 1f, 1f, 2.5f});
                dataTable.setSpacingBefore(5);
                
                // Table headers
                String[] headers = {"Product Name", "Category", "Current Stock", "Change", "Trend", "Last Updated"};
                for (String header : headers) {
                    PdfPCell tableHeaderCell = new PdfPCell(new Phrase(header, headerFont));
                    tableHeaderCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    tableHeaderCell.setPadding(5);
                    tableHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    dataTable.addCell(tableHeaderCell);
                }
                
                // Table data - limit to fit on page nicely
                int maxRows = Math.min(model.getRowCount(), 30);
                for (int i = 0; i < maxRows; i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Object value = model.getValueAt(i, j);
                        PdfPCell dataCell = new PdfPCell(new Phrase(value != null ? value.toString() : "", normalFont));
                        dataCell.setPadding(4);
                        if (j == 2 || j == 3 || j == 4) {
                            dataCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        }
                        dataTable.addCell(dataCell);
                    }
                }
                
                document.add(dataTable);
                
                // Add note if table was truncated
                if (model.getRowCount() > 30) {
                    Paragraph note = new Paragraph(
                        "Note: Showing first 30 of " + model.getRowCount() + " records", 
                        new com.itextpdf.text.Font(
                            com.itextpdf.text.Font.FontFamily.HELVETICA, 8, com.itextpdf.text.Font.ITALIC)
                    );
                    note.setSpacingBefore(8);
                    document.add(note);
                }
            } else {
                document.add(new Paragraph("No data available", normalFont));
            }
            
            // Close document
            document.close();
            
            // Show success message with options
            Object[] options = {"Open PDF", "Open Folder", "Close"};
            int choice = JOptionPane.showOptionDialog(this,
                "✅ Report exported successfully!\n\n" +
                "File: " + fileName,
                "Export Success",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);
            
            // Open the PDF
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(fileToSave);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error exporting PDF:\n" + ex.getMessage(), 
                "Export Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
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

        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        contentPanel = new javax.swing.JPanel();
        statsPanel = new javax.swing.JPanel();
        jPanel3 = new utils.RoundedPanel(25);
        statCard1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblTotalStock = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblAvgItem = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblCategoryCount = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lblTrendStatus = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        chartPanel = new utils.RoundedPanel(25);
        jLabel66 = new javax.swing.JLabel();
        chartDrawPanel = new javax.swing.JPanel();
        tablePanel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        trendTable = new javax.swing.JTable();
        btnPrint = new javax.swing.JButton();
        filterPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cmbTimeRange = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        cmbCategory = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
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

        jPanel2.setBackground(new java.awt.Color(59, 81, 108));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Inventory Trends - Analytics Dashboard");

        contentPanel.setBackground(new java.awt.Color(80, 108, 133));

        statsPanel.setBackground(new java.awt.Color(59, 100, 108));
        statsPanel.setLayout(new java.awt.GridLayout(1, 0));

        jPanel3.setBackground(new java.awt.Color(57, 77, 99));

        statCard1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 0), 2));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Total Stock");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Avg per Item");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Categories");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Trend");

        lblTotalStock.setText("0");

        jLabel13.setText("↑ 12%");

        lblAvgItem.setText("0");

        jLabel15.setText("Stable");

        lblCategoryCount.setText("0");

        jLabel17.setText("Active");

        lblTrendStatus.setText("Positive");

        jLabel19.setText("Growing");

        javax.swing.GroupLayout statCard1Layout = new javax.swing.GroupLayout(statCard1);
        statCard1.setLayout(statCard1Layout);
        statCard1Layout.setHorizontalGroup(
            statCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statCard1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(statCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statCard1Layout.createSequentialGroup()
                        .addGroup(statCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addGroup(statCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(statCard1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(lblTrendStatus)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statCard1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblCategoryCount)
                                .addGap(35, 35, 35)))
                        .addGroup(statCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel19)))
                    .addGroup(statCard1Layout.createSequentialGroup()
                        .addGroup(statCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8))
                        .addGap(22, 22, 22)
                        .addGroup(statCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAvgItem)
                            .addComponent(lblTotalStock, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(statCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel13))))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        statCard1Layout.setVerticalGroup(
            statCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statCard1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lblTotalStock)
                    .addComponent(jLabel13))
                .addGap(21, 21, 21)
                .addGroup(statCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAvgItem)
                    .addComponent(jLabel9)
                    .addComponent(jLabel15))
                .addGap(27, 27, 27)
                .addGroup(statCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(lblCategoryCount)
                    .addComponent(jLabel17))
                .addGap(24, 24, 24)
                .addGroup(statCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addGroup(statCard1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblTrendStatus)
                        .addComponent(jLabel19)))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(statCard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(statCard1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        chartPanel.setBackground(new java.awt.Color(57, 77, 99));

        jLabel66.setForeground(new java.awt.Color(255, 255, 255));
        jLabel66.setText("📊 Stock Trend Chart");

        chartDrawPanel.setBackground(new java.awt.Color(52, 73, 94));

        javax.swing.GroupLayout chartDrawPanelLayout = new javax.swing.GroupLayout(chartDrawPanel);
        chartDrawPanel.setLayout(chartDrawPanelLayout);
        chartDrawPanelLayout.setHorizontalGroup(
            chartDrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 453, Short.MAX_VALUE)
        );
        chartDrawPanelLayout.setVerticalGroup(
            chartDrawPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 259, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout chartPanelLayout = new javax.swing.GroupLayout(chartPanel);
        chartPanel.setLayout(chartPanelLayout);
        chartPanelLayout.setHorizontalGroup(
            chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chartPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel66)
                    .addComponent(chartDrawPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        chartPanelLayout.setVerticalGroup(
            chartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chartPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel66)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chartDrawPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        tablePanel.setBackground(new java.awt.Color(59, 81, 108));

        trendTable.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        trendTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Product Name", "Category", "Current Stock", "Change", "Trend", "Last Updated"
            }
        ));
        jScrollPane1.setViewportView(trendTable);

        scrollPane.setViewportView(jScrollPane1);

        btnPrint.setText("🖨️ Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tablePanelLayout = new javax.swing.GroupLayout(tablePanel);
        tablePanel.setLayout(tablePanelLayout);
        tablePanelLayout.setHorizontalGroup(
            tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tablePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnPrint)
                    .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 829, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
        );
        tablePanelLayout.setVerticalGroup(
            tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tablePanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPrint)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel6.setText("Time Range:");
        filterPanel.add(jLabel6);

        cmbTimeRange.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Last 7 Days", "Last 30 Days", "Last 90 Days", "Last Year", "All Time" }));
        filterPanel.add(cmbTimeRange);

        jLabel7.setText("Category");
        filterPanel.add(jLabel7);

        cmbCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Categories", "Electronics", "Furniture", "Clothing", "Food" }));
        filterPanel.add(cmbCategory);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Inventory Trends & Analytics");

        javax.swing.GroupLayout contentPanelLayout = new javax.swing.GroupLayout(contentPanel);
        contentPanel.setLayout(contentPanelLayout);
        contentPanelLayout.setHorizontalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(statsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(contentPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(filterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contentPanelLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(chartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19))
        );
        contentPanelLayout.setVerticalGroup(
            contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentPanelLayout.createSequentialGroup()
                .addGroup(contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(contentPanelLayout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(statsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(contentPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(contentPanelLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(contentPanelLayout.createSequentialGroup()
                                .addComponent(filterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(chartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(contentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        
        exportReportToPDF();
    }//GEN-LAST:event_btnPrintActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new InventoryTrendForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPrint;
    private javax.swing.JPanel chartDrawPanel;
    private javax.swing.JPanel chartPanel;
    private javax.swing.JComboBox<String> cmbCategory;
    private javax.swing.JComboBox<String> cmbTimeRange;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
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
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAvgItem;
    private javax.swing.JLabel lblCategoryCount;
    private javax.swing.JLabel lblDash;
    private javax.swing.JLabel lblInventory;
    private javax.swing.JLabel lblLogout;
    private javax.swing.JLabel lblQRScan;
    private javax.swing.JLabel lblReports;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblSettings;
    private javax.swing.JLabel lblTotalStock;
    private javax.swing.JLabel lblTrendStatus;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JLabel lblUsers;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JPanel statCard1;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JTable trendTable;
    // End of variables declaration//GEN-END:variables
}
