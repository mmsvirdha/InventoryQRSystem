/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;


import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;

/**
 *
 * @author ADMIN
 */


public class RoundedBorder implements Border {
    private int radius;
    private int thickness;
    private Color borderColor;

    // constructors
    public RoundedBorder(int radius) {
        this(radius, 2, null);
    }

    public RoundedBorder(int radius, int thickness, Color borderColor) {
        this.radius = radius;
        this.thickness = Math.max(1, thickness);
        this.borderColor = borderColor; // if null, will use a default
    }

    @Override
    public Insets getBorderInsets(Component c) {
        int pad = radius / 2 + thickness;
        return new Insets(pad, pad, pad, pad);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // background: fill rounded rect using component background color
        Color bg = c.getBackground();
        g2.setColor(bg);
        g2.fillRoundRect(x, y, width - 1, height - 1, radius, radius);

        // outline
        Color line = (borderColor != null) ? borderColor : bg.darker();
        g2.setColor(line);

        // draw multiple outlines for thickness
        for (int i = 0; i < thickness; i++) {
            g2.drawRoundRect(x + i, y + i, width - 1 - 2 * i, height - 1 - 2 * i, radius, radius);
        }

        g2.dispose();
    }
}
