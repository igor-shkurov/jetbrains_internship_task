package de.jetbrains;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main extends JFrame {
    private final double INITIAL_SCALE = 0.25;

    private final JLabel sprite;
    private final ImageIcon photo;
    private int originalWidth, originalHeight;
    private int enterX = 0, enterY = 0;
    private boolean resizingLocked = false;
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
                // determine the borders of the panel (x coordinates for left and right, y for top and bottom ones)
                int left = panel.getLocationOnScreen().x;
                int right = panel.getLocationOnScreen().x + panel.getWidth();
                int top = panel.getLocationOnScreen().y;
                int bottom = panel.getLocationOnScreen().y + panel.getHeight() / 2;

                // determine the entry side depending on how the cursor was positioned relative to the borders when entering
                if (enterX < left) {
                    enterSide = Side.LEFT;
                }
                else if (enterX > right) {
                    enterSide = Side.RIGHT;
                }
                else if (enterY < top) {
                    enterSide = Side.TOP;
                }
                else if (enterY > bottom) {
                    enterSide = Side.BOTTOM;
                }

                sprite.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // reset all the variables back to initial values
                shrinkSprite();
                sprite.setVisible(false);
                prevX = -1;
                prevY = -1;
                resizingLocked = false;
                enterSide = Side.UNDEFINED;
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                // no prev coordinates in the first iteration
                if (prevX == -1) {
                    prevX = mouseX;
                    prevY = mouseY;
                }
                // depending on the side of entry it is either x-relative movement or y-relative movement affect size increase and decrease (respectively)
                else {
                    int increase = 0;
                    int decrease = 0;
                    switch (enterSide) {
                        case LEFT:
                            increase = mouseX - prevX;
                            decrease = Math.abs(mouseY - prevY);
                            break;
                        case RIGHT:
                            increase = prevX - mouseX;
                            decrease = Math.abs(mouseY - prevY);
                            break;
                        case TOP:
                            increase = mouseY - prevY;
                            decrease = Math.abs(mouseX - prevX);
                            break;
                        case BOTTOM:
                            increase = prevY - mouseY;
                            decrease = Math.abs(mouseX - prevX);
                            break;
                    }
                    // set prev coordinates to determine the diff in the next iteration
                    prevX = mouseX;
                    prevY = mouseY;

                    if (!resizingLocked) {
                        resizeSprite(increase);
                        resizeSprite(-decrease);
                    }
                }
                updateSpritePosition(mouseX, mouseY);
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // saving coordinates here to compare them later with panel border coordinates to determine the entry side
                enterX = MouseInfo.getPointerInfo().getLocation().x;
                enterY = MouseInfo.getPointerInfo().getLocation().y;
            }
        });
        add(marginPanel, BorderLayout.CENTER);
    }

    private void updateSpritePosition(int mouseX, int mouseY) {
        // placing the cursor in the middle of the sprite
        int x = mouseX - sprite.getWidth() / 2;
        int y = mouseY - sprite.getHeight() / 2;
        //
        sprite.setLocation(x, y);
    }

    private void resizeSprite(int term) {
        // works as long as our sprite is square-shaped :)
        int newWidth = sprite.getWidth() + term;
        int newHeight = sprite.getHeight() + term;

        // disable further resizing if 1:1 size is reached
        if (newWidth >= originalWidth) {
            newWidth = originalWidth;
            newHeight = originalHeight;
            resizingLocked = true;
        }

        // do nothing if sprite shrank back to 1:4 of size
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
