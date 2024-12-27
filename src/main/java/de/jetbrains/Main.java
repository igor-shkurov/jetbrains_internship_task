package de.jetbrains;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLOutput;

public class Main extends JFrame {
    private JLabel sprite;
    private ImageIcon photo;
    private int originalWidth, originalHeight;
    private double currentScale = 0.25; // Start at 1/4 scale
    private int enterX, enterY;

    enum Side {
        TOP, BOTTOM, LEFT, RIGHT, UNDEFINED;
    }

    private Side enterSide = Side.UNDEFINED;

    public Main() {
        setTitle("Nikolai Vasilenko");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setResizable(true);
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);

        JPanel panel = new JPanel(null);
        panel.setBackground(Color.LIGHT_GRAY);

        photo = new ImageIcon("sprite.png");
        originalWidth = photo.getIconWidth();
        originalHeight = photo.getIconHeight();
        Image scaledImage = photo.getImage().getScaledInstance(
                (int) (originalWidth * currentScale), (int) (originalHeight * currentScale), Image.SCALE_SMOOTH);
        sprite = new JLabel(new ImageIcon(scaledImage));
        sprite.setSize((int) (originalWidth * currentScale), (int) (originalHeight * currentScale));
        sprite.setVisible(false);
        panel.add(sprite);

        JPanel marginPanel = new JPanel(new BorderLayout());
        marginPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        marginPanel.add(panel, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                int left = panel.getLocationOnScreen().x;
                int right = panel.getLocationOnScreen().x + panel.getWidth();
                int top = panel.getLocationOnScreen().y;
                int bottom = panel.getLocationOnScreen().y + panel.getHeight() / 2;

                if (enterX < left) {
                    enterSide = Side.LEFT;
                    System.out.println("SIDE: " + enterSide + " : " + left);
                }
                else if (enterX > right) {
                    enterSide = Side.RIGHT;
                    System.out.println("SIDE: " + enterSide + " : " + right);
                }
                else if (enterY < top) {
                    enterSide = Side.TOP;
                    System.out.println("SIDE: " + enterSide + " : " + top);
                }
                else if (enterY > bottom) {
                    enterSide = Side.BOTTOM;
                    System.out.println("SIDE: " + enterSide + " : " + bottom);
                }

                System.out.println("Enter Coordinates :" + enterX + " " + enterY);

                sprite.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sprite.setVisible(false);
                currentScale = 0.25;
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                updateSpritePosition(mouseX, mouseY);
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println(MouseInfo.getPointerInfo().getLocation().x + " "
                        + MouseInfo.getPointerInfo().getLocation().y);
                enterX = MouseInfo.getPointerInfo().getLocation().x;
                enterY = MouseInfo.getPointerInfo().getLocation().y;
                sprite.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sprite.setVisible(false);
                currentScale = 0.25;
            }
        });

        add(marginPanel, BorderLayout.CENTER);
    }

    private void updateSpritePosition(int mouseX, int mouseY) {
        int x = mouseX - sprite.getWidth() / 2;
        int y = mouseY - sprite.getHeight() / 2;
        sprite.setLocation(x, y);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}
