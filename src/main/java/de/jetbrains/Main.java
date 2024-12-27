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
    private final double INITIAL_SCALE = 0.25; // Start at 1/4 scale
    private int enterX, enterY;
    private boolean resisingLocked = false;
    private int prevX = -1, prevY = -1;

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
        sprite = new JLabel();
        shrinkSprite();
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
                shrinkSprite();
                sprite.setVisible(false);
                prevX = -1;
                prevY = -1;
                resisingLocked = false;
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                if (prevX == -1) {
                    prevX = mouseX;
                    prevY = mouseY;
                }
                else {
                    int upScale = 0;
                    int downScale = 0;
                    switch (enterSide) {
                        case LEFT:
                            upScale = mouseX - prevX;
                            downScale = Math.abs(mouseY - prevY);
                            break;
                        case RIGHT:
                            upScale = prevX - mouseX;
                            downScale = Math.abs(mouseY - prevY);
                            break;
                        case TOP:
                            upScale = mouseY - prevY;
                            downScale = Math.abs(mouseX - prevX);
                            break;
                        case BOTTOM:
                            upScale = prevY - mouseY;
                            downScale = Math.abs(mouseX - prevX);
                            break;
                    }
                    prevX = mouseX;
                    prevY = mouseY;

                    if (!resisingLocked) {
                        resizeSprite(upScale);
                        resizeSprite(-downScale);
                    }
                }

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
            }
        });

        add(marginPanel, BorderLayout.CENTER);
    }

    private void updateSpritePosition(int mouseX, int mouseY) {
        int x = mouseX - sprite.getWidth() / 2; // mouse points to the left top corner of the sprite
        int y = mouseY - sprite.getHeight() / 2; // we have to place cursor in the middle of it
        sprite.setLocation(x, y);
    }

    private void resizeSprite(int term) {
        int newWidth = sprite.getWidth() + term;
        int newHeight = sprite.getHeight() + term;

        if (newWidth >= originalWidth) {
            newWidth = originalWidth;
            newHeight = originalHeight;
            resisingLocked = true;
        }

        if (newWidth <= originalWidth * INITIAL_SCALE) {
            return;
        }

        Image scaledImage = photo.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        sprite.setIcon(new ImageIcon(scaledImage));
        sprite.setSize(newWidth, newHeight);
    }

    private void shrinkSprite() {
        Image scaledImage = photo.getImage().getScaledInstance(
                (int) (originalWidth * INITIAL_SCALE), (int) (originalHeight * INITIAL_SCALE), Image.SCALE_SMOOTH);
        sprite.setIcon(new ImageIcon(scaledImage));
        sprite.setSize((int) (originalWidth * INITIAL_SCALE), (int) (originalHeight * INITIAL_SCALE));
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}
