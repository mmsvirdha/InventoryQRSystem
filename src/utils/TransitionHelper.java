/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import javax.swing.JLabel;      // for JLabel
import javax.swing.JButton;     // for JButton
import java.awt.Color;          // for Color
import java.awt.Cursor;         // for Cursor
import java.awt.event.MouseAdapter; // for mouse events
import java.awt.event.MouseEvent;   // for mouse events


/**
 *
 * @author ADMIN
 */

public class TransitionHelper {
    
    /**
     * Fade from currentFrame -> show nextFrame with smooth animation
     */
    public static void fadeToFrame(JFrame currentFrame, JFrame nextFrame, int durationMillis) {
        if (currentFrame == null || nextFrame == null) return;

        SwingUtilities.invokeLater(() -> doFade(currentFrame, nextFrame, durationMillis));
    }

    
    private static void doFade(JFrame currentFrame, JFrame nextFrame, int durationMillis) {
    try {
        Point screenLoc = currentFrame.getLocationOnScreen();
        Dimension size = currentFrame.getSize();
        Rectangle bounds = new Rectangle(screenLoc, size);

        FadeWindow overlay = new FadeWindow(bounds);
        overlay.setAlwaysOnTop(true);
        overlay.setVisible(true);

        int stepDelay = 16; // ~60 fps
        int steps = Math.max(3, durationMillis / stepDelay);
        final float stepDelta = 1f / steps;

        Timer fadeIn = new Timer(stepDelay, null);
        final float[] alpha = {0f};
        fadeIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha[0] += stepDelta;
                if (alpha[0] >= 1f) {
                    alpha[0] = 1f;
                    overlay.setAlpha(1f);
                    fadeIn.stop();

                    //  Don't force bounds if next frame is maximized
                    int nextState = nextFrame.getExtendedState();
                    if (nextState != JFrame.MAXIMIZED_BOTH) {
                        // Only set bounds if NOT maximized
                        nextFrame.setBounds(bounds);
                    }
                    
                    nextFrame.setVisible(true);

                    Timer fadeOut = new Timer(stepDelay, null);
                    final float[] a2 = {1f};
                    fadeOut.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e2) {
                            a2[0] -= stepDelta;
                            if (a2[0] <= 0f) {
                                a2[0] = 0f;
                                overlay.setAlpha(0f);
                                fadeOut.stop();
                                overlay.dispose();
                                try { currentFrame.dispose(); } catch (Exception ex) {}
                            } else {
                                overlay.setAlpha(a2[0]);
                            }
                        }
                    });
                    fadeOut.start();
                } else {
                    overlay.setAlpha(alpha[0]);
                }
            }
        });
        fadeIn.start();

    } catch (IllegalComponentStateException ex) {
        
        int nextState = nextFrame.getExtendedState();
        if (nextState != JFrame.MAXIMIZED_BOTH) {
            nextFrame.setBounds(currentFrame.getBounds());
        }
        nextFrame.setVisible(true);
        currentFrame.dispose();
    }
}
    
 // Overlay window
    private static class FadeWindow extends JWindow {
        private float alpha = 0f;

        FadeWindow(Rectangle bounds) {
            super();
            setBounds(bounds);
            setBackground(new Color(0, 0, 0, 0));
            setFocusableWindowState(false);
        }

        void setAlpha(float a) {
            this.alpha = Math.max(0f, Math.min(1f, a));
            repaint();
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.setColor(Color.black);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }
    
     //  Add teal hover effect to a Buttons

     public static void applyTealHover(JButton button) {
        final Color originalBg = new Color(0x00, 0x99, 0x99); // teal
        final Color hoverBg = new Color(0x00, 0x77, 0x77);    // darker teal

        button.setBackground(originalBg);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(hoverBg);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(originalBg);
            }
        });
    }
    
 //  Add teal hover effect to a Labels
     
   public static void applyTealHoverSmooth(JLabel label) {
    final Color originalColor = label.getForeground();
    final Color hoverColor = new Color(0, 153, 153);
    final int steps = 10;
    final int delay = 20; // ms per step

    label.setCursor(new Cursor(Cursor.HAND_CURSOR));

    label.addMouseListener(new MouseAdapter() {
        Timer timer;
        @Override
        public void mouseEntered(MouseEvent e) {
            if (timer != null && timer.isRunning()) timer.stop();
            timer = new Timer(delay, null);
            final int[] step = {0};
            timer.addActionListener(ev -> {
                float ratio = ++step[0] / (float) steps;
                label.setForeground(interpolateColor(originalColor, hoverColor, ratio));
                if (step[0] >= steps) timer.stop();
            });
            timer.start();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (timer != null && timer.isRunning()) timer.stop();
            timer = new Timer(delay, null);
            final int[] step = {0};
            timer.addActionListener(ev -> {
                float ratio = ++step[0] / (float) steps;
                label.setForeground(interpolateColor(hoverColor, originalColor, ratio));
                if (step[0] >= steps) timer.stop();
            });
            timer.start();
        }
    });
}

// Helper method to interpolate colors
private static Color interpolateColor(Color c1, Color c2, float ratio) {
    int r = (int)(c1.getRed() + ratio * (c2.getRed() - c1.getRed()));
    int g = (int)(c1.getGreen() + ratio * (c2.getGreen() - c1.getGreen()));
    int b = (int)(c1.getBlue() + ratio * (c2.getBlue() - c1.getBlue()));
    return new Color(r, g, b);
}



}
 

