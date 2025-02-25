package tetris.block;

import tetris.Main;
import tetris.MainFrame;
import tetris.PlayFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ToPlaceBlock {

    private final List<Block> blocks;


    public ToPlaceBlock(List<Block> blocks, Runnable onCancel) {
        this.blocks = blocks;

        for (Block block : blocks) {
            PlayFrame.getInstance().add(block);
        }

        KeyListener listener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP, KeyEvent.VK_SPACE -> rotate();
                    case KeyEvent.VK_LEFT -> move(-20);
                    case KeyEvent.VK_RIGHT -> move(20);
                    case KeyEvent.VK_DOWN -> moveDown();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
        MainFrame.getInstance().addKeyListener(listener);

        new Timer(getDelayBasedOnScore(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isInvalidMoveDown()) {
                    MainFrame.getInstance().removeKeyListener(listener);
                    PlayFrame.getInstance().getPlacedBlocks().addAll(blocks);
                    ((Timer) e.getSource()).stop();
                    onCancel.run();
                    MainFrame.getInstance().setScore(MainFrame.getInstance().getScore() + blocks.size());
                    return;
                }
                for (Block block : blocks) {
                    Point location = block.getLocation();
                    block.setLocation(location.x, location.y + 20);
                }
            }
        }).start();
    }

    private int getDelayBasedOnScore() {
        int baseDelay = 700;
        int delayDecrease = MainFrame.getInstance().getScore() / 4; // Reduce delay for every 10 points

        int newDelay = baseDelay - delayDecrease;
        return Math.max(newDelay, 300);
    }


    private boolean isInvalidMoveDown() {
        for (Block block : blocks) {
            if (block.getLocation().y + 20 >= PlayFrame.getInstance().getHeight()) { // block would go through the floor
                return true;
            }
            for (Block placedBlock : PlayFrame.getInstance().getPlacedBlocks()) {
                if (block.getLocation().y + 20 >= placedBlock.getLocation().y && // block would collide
                        block.getLocation().y <= placedBlock.getLocation().y && // block is below the other block
                        block.getLocation().x == placedBlock.getLocation().x) {
                    return true;
                }
            }
        }
        return false;
    }

    private void moveDown() {
        if (isInvalidMoveDown()) return;
        for (Block block : blocks) {
            Point location = block.getLocation();
            block.setLocation(location.x, location.y + 20);
        }
    }

    private void move(int i) {
        for (Block block : blocks) {
            Point location = block.getLocation();
            if (i > 0 && location.x + i == PlayFrame.getInstance().getWidth()) return;
            if (i < 0 && location.x == 0) return;
            int newX = location.x + i;
            for (Block placedBlock : PlayFrame.getInstance().getPlacedBlocks()) {
                if (placedBlock.getLocation().x == newX && placedBlock.getLocation().y == location.y) return;
            }
        }
        for (Block block : blocks) {
            Point location = block.getLocation();
            block.setLocation(location.x + i, location.y);
        }
    }

    private void rotate() {
        if (isSquareBlock()) return;

        Block pivot = blocks.getFirst(); // Assume the first block is the pivot
        int pivotX = pivot.getX();
        int pivotY = pivot.getY();

        List<Point> newPositions = new ArrayList<>();
        for (Block block : blocks) {
            int relativeX = block.getX() - pivotX;
            int relativeY = block.getY() - pivotY;

            int newX = pivotX - relativeY;
            int newY = pivotY + relativeX;

            newPositions.add(new Point(newX, newY));
        }

        for (Point newPos : newPositions) {
            if (newPos.x < 0 || newPos.x >= PlayFrame.getInstance().getWidth() ||
                    newPos.y >= PlayFrame.getInstance().getHeight()
            ) {
                return;
            }
            for (Block placedBlock : PlayFrame.getInstance().getPlacedBlocks()) {
                if (placedBlock.getX() == newPos.x &&
                        placedBlock.getY() == newPos.y
                ) {
                    return;
                }
            }
        }

        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).setLocation(newPositions.get(i));
        }
    }

    private boolean isSquareBlock() {
        if (blocks.size() != 4) return false;
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

        for (Block block : blocks) {
            int x = block.getX();
            int y = block.getY();
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }

        return maxX - minX == 20 && maxY - minY == 20;
    }
}
