/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QrScanner;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 *
 * @author ADMIN
 */


public class AnimatedScannerPanel extends JPanel {
    
    private int scanLineY = 0;
    private boolean scanningActive = false;
    private Timer animationTimer;
    private int scanDirection = 1; // 1 = down, -1 = up
    
    // Colors
    private final Color bgColor = new Color(26, 26, 46);
    private final Color outerBorderColor = new Color(102, 126, 234); // Blue
    private final Color innerBorderColor = new Color(0, 255, 136); // Green
    private final Color scanLineColor = new Color(0, 255, 136, 180); // Green with transparency
    private final Color scanGlowColor = new Color(0, 255, 136, 60);
    
    // Dimensions
    private final int cornerSize = 30;
    private final int borderThickness = 4;
    private final int scanLineHeight = 3;
    
    public AnimatedScannerPanel() {
        setBackground(bgColor);
        setPreferredSize(new Dimension(325, 220));
        setMinimumSize(new Dimension(325, 220));
        setOpaque(true); // Make sure panel is opaque
        
        // Animation timer - runs every 16ms (~60 FPS)
        animationTimer = new Timer(16, e -> {
            if (scanningActive) {
                // Move scan line
                scanLineY += (3 * scanDirection);
                
                // Calculate scan area height dynamically
                int padding = 50;
                int scanAreaHeight = Math.max(100, getHeight() - (padding * 2));
                
                // Reverse direction at boundaries
                if (scanLineY >= scanAreaHeight) {
                    scanDirection = -1;
                } else if (scanLineY <= 0) {
                    scanDirection = 1;
                }
                
                repaint(); // Redraw the panel
            }
        });
        
        System.out.println("AnimatedScannerPanel created with size: " + getPreferredSize());
    }
    
    public void startScanning() {
        System.out.println("Starting scanning animation");
        scanningActive = true;
        scanLineY = 0;
        scanDirection = 1;
        animationTimer.start();
        repaint();
    }
    
    public void stopScanning() {
        System.out.println("Stopping scanning animation");
        scanningActive = false;
        animationTimer.stop();
        repaint();
    }
    
    public boolean isScanning() {
        return scanningActive;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable anti-aliasing for smooth edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        // Ensure we have valid dimensions
        if (width <= 0 || height <= 0) {
            System.out.println("Invalid dimensions: " + width + "x" + height);
            return;
        }
        
        // Calculate center area
        int padding = 50;
        int scanAreaX = padding;
        int scanAreaY = padding;
        int scanAreaWidth = Math.max(100, width - (padding * 2));
        int scanAreaHeight = Math.max(100, height - (padding * 2));
        
        // Draw outer border (Blue rounded rectangle)
        g2d.setColor(outerBorderColor);
        g2d.setStroke(new BasicStroke(borderThickness));
        g2d.drawRoundRect(
            scanAreaX - 20, 
            scanAreaY - 20, 
            scanAreaWidth + 40, 
            scanAreaHeight + 40, 
            30, 30
        );
        
        // Draw inner border (Green rounded rectangle)
        g2d.setColor(innerBorderColor);
        g2d.setStroke(new BasicStroke(borderThickness));
        g2d.drawRoundRect(
            scanAreaX, 
            scanAreaY, 
            scanAreaWidth, 
            scanAreaHeight, 
            20, 20
        );
        
        // Draw corner accents (Green L-shapes at corners)
        drawCornerAccents(g2d, scanAreaX, scanAreaY, scanAreaWidth, scanAreaHeight);
        
        // Draw camera icon in center (if not scanning)
        if (!scanningActive) {
            drawCameraIcon(g2d, width / 2, height / 2);
        }
        
        // Draw animated scan line (when scanning)
        if (scanningActive) {
            drawScanLine(g2d, scanAreaX, scanAreaY + scanLineY, scanAreaWidth);
        }
    }
    
    private void drawCornerAccents(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.setColor(innerBorderColor);
        g2d.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        int accentLength = 40;
        
        // Top-left corner
        g2d.drawLine(x, y + accentLength, x, y);
        g2d.drawLine(x, y, x + accentLength, y);
        
        // Top-right corner
        g2d.drawLine(x + width - accentLength, y, x + width, y);
        g2d.drawLine(x + width, y, x + width, y + accentLength);
        
        // Bottom-left corner
        g2d.drawLine(x, y + height - accentLength, x, y + height);
        g2d.drawLine(x, y + height, x + accentLength, y + height);
        
        // Bottom-right corner
        g2d.drawLine(x + width - accentLength, y + height, x + width, y + height);
        g2d.drawLine(x + width, y + height, x + width, y + height - accentLength);
    }
    
    private void drawScanLine(Graphics2D g2d, int x, int y, int width) {
        // Draw glow effect
        for (int i = 10; i > 0; i--) {
            int alpha = (int) (60 * (i / 10.0));
            g2d.setColor(new Color(0, 255, 136, alpha));
            g2d.fillRect(x, y - i, width, scanLineHeight + (i * 2));
        }
        
        // Draw main scan line
        GradientPaint gradient = new GradientPaint(
            x, y, new Color(0, 255, 136, 50),
            x + width, y, new Color(0, 255, 136, 255)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(x, y, width, scanLineHeight);
        
        // Draw bright center line
        g2d.setColor(new Color(200, 255, 200, 255));
        g2d.fillRect(x, y + 1, width, 1);
    }
    
    private void drawCameraIcon(Graphics2D g2d, int centerX, int centerY) {
        g2d.setColor(new Color(100, 100, 120, 150));
        
        // Camera body
        int cameraWidth = 80;
        int cameraHeight = 60;
        g2d.fillRoundRect(
            centerX - cameraWidth/2, 
            centerY - cameraHeight/2, 
            cameraWidth, 
            cameraHeight, 
            10, 10
        );
        
        // Camera lens
        g2d.setColor(new Color(60, 60, 80, 150));
        g2d.fillOval(centerX - 20, centerY - 20, 40, 40);
        
        // Lens inner circle
        g2d.setColor(new Color(40, 40, 60, 150));
        g2d.fillOval(centerX - 12, centerY - 12, 24, 24);
        
        // Camera top button
        g2d.setColor(new Color(80, 80, 100, 150));
        g2d.fillRoundRect(centerX - 30, centerY - 40, 20, 8, 4, 4);
        
        // Text below camera
        g2d.setColor(new Color(150, 150, 170, 200));
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        String text = "Ready to Scan";
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        g2d.drawString(text, centerX - textWidth/2, centerY + 50);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(325, 220);
    }
    
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(325, 220);
    }
}