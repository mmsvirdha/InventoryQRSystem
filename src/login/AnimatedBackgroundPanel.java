/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author ADMIN
 */

public class AnimatedBackgroundPanel extends JPanel {
    private Image backgroundImage;
    
    public AnimatedBackgroundPanel(String imagePath) {
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        this.backgroundImage = icon.getImage();
        setOpaque(false); // Make panel transparent
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Draw the animated GIF as background
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
