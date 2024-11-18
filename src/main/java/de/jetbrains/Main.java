package de.jetbrains;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Main extends JFrame {
    private JLabel photoLabel;
    final int width = 800, height = 800;

    public Main() {
        setTitle("Nikolai Vasilenko");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setResizable(false);
        setLocation(800, 800);

        ImageIcon photo = new ImageIcon("sprite.png");
        photoLabel = new JLabel(photo);
        photoLabel.setSize(photo.getIconWidth(), photo.getIconHeight());
        photoLabel.setVisible(false);
        add(photoLabel);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                photoLabel.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                photoLabel.setVisible(false);
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX() - width / 2;
                int y = e.getY() - height / 2;
                photoLabel.setLocation(x, y);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}
