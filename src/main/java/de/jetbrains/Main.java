package de.jetbrains;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main extends JFrame {
    private JLabel sprite;
    private ImageIcon photo;
    private int originalWidth, originalHeight;
    private double currentScale = 0.25; // Start at 1/4 scale

    enum Direction {
        UP, DOWN, LEFT, RIGHT, UNDEFINED;
    }

    private Direction enterDir = Direction.UNDEFINED;

    public Main() {
        setTitle("Nikolai Vasilenko");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setResizable(true);
        setLocation(800, 800);

        photo = new ImageIcon("sprite.png");
        originalWidth = photo.getIconWidth();
        originalHeight = photo.getIconHeight();

        Image scaledImage = photo.getImage().getScaledInstance(
                (int) (originalWidth * currentScale), (int) (originalHeight * currentScale), Image.SCALE_SMOOTH);
        sprite = new JLabel(new ImageIcon(scaledImage));
        sprite.setSize((int) (originalWidth * currentScale), (int) (originalHeight * currentScale));
        sprite.setVisible(false);
        add(sprite);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                updateSpritePosition(e.getX(), e.getY());
                sprite.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sprite.setVisible(false);
                currentScale = 0.25;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                updateSpritePosition(mouseX, mouseY);
            }
        });
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
