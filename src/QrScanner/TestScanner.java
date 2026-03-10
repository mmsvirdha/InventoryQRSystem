/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QrScanner;

import javax.swing.*;
import QrScanner.AnimatedScannerPanel; // import your panel class

/**
 *
 * @author ADMIN
 */
public class TestScanner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("QR Scanner Animation");
            AnimatedScannerPanel panel = new AnimatedScannerPanel();
            
            frame.add(panel);                // Add your animated panel
            frame.pack();                    // Auto-size frame
            frame.setLocationRelativeTo(null); // Center frame
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);          // Show frame

            panel.startScanning();           // Start the animation
        });
    }
}
