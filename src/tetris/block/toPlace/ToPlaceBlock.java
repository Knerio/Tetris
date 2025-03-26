package tetris.block.toPlace;

import tetris.game.GameState;
import tetris.MainFrame;
import tetris.game.PlayFrame;
import tetris.block.Block;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class ToPlaceBlock {

    private final List<Block> blocks;


    public List<Block> getBlocks() {
        return blocks;
    }

    public ToPlaceBlock(List<Block> initialBlocks, Runnable onCancel) {
        this.blocks = initialBlocks;

        ShadowBlock shadow = new ShadowBlock(this);

        for (Block block : blocks) {
            PlayFrame.getInstance().add(block, 1);
        }

        KeyListener listener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (PlayFrame.getInstance().gameState != GameState.PLAYING) return;
                int blockSize = PlayFrame.getInstance().getRescaledBlockSize();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP, KeyEvent.VK_SPACE, KeyEvent.VK_W -> rotate();
                    case KeyEvent.VK_LEFT, KeyEvent.VK_A -> move(-blockSize);
                    case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> move(blockSize);
                    case KeyEvent.VK_DOWN, KeyEvent.VK_S -> moveDown();
                }
                shadow.update();
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        };

        MainFrame.getInstance().addKeyListener(listener);

        new Timer(getDelayBasedOnScore(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (PlayFrame.getInstance().gameState != GameState.PLAYING) return;
                if (isInvalidMoveDown()) {
                    MainFrame.getInstance().setScore(MainFrame.getInstance().getScore() + blocks.size());

                    for (Block block : blocks) {
                        PlayFrame.getInstance().getPlacedBlocks().add(block);
                    }

                    MainFrame.getInstance().removeKeyListener(listener);
                    ((Timer) e.getSource()).stop();
                    shadow.remove();
                    onCancel.run();
                    return;
                }
                for (Block block : blocks) {
                    Point location = block.getLocation();
                    block.setLocation(location.x, location.y + PlayFrame.getInstance().getRescaledBlockSize());
                }
                shadow.update();
            }
        }).start();



    }

    /*
     * Calculates the drop rate of the to place block.
     * Starts at every 700th ms.
     * Decreases with every 4th block by one.
     * Min delay is 200 ms.
     */
    private int getDelayBasedOnScore() {
        int baseDelay = 700;
        int delayDecrease = MainFrame.getInstance().getScore() / 4; // Reduce delay for every 4 points

        int newDelay = baseDelay - delayDecrease;
        return Math.max(newDelay, 200);
    }


    public boolean isInvalidMoveDown() {
        int blockSize = PlayFrame.getInstance().getRescaledBlockSize();
        for (Block block : blocks) {
            if (block.getLocation().y + blockSize >= PlayFrame.getInstance().getHeight()) { // block would go through the floor
                return true;
            }
            for (Block placedBlock : PlayFrame.getInstance().getPlacedBlocks()) {
                if (block.getLocation().y + blockSize >= placedBlock.getLocation().y && // block would collide
                        block.getLocation().y <= placedBlock.getLocation().y && // block is below the other block
                        block.getLocation().x == placedBlock.getLocation().x) {
                    return true;
                }
            }
        }
        return false;
    }

    private void moveDown() {
        int blockSize = PlayFrame.getInstance().getRescaledBlockSize();
        if (isInvalidMoveDown()) return;
        for (Block block : blocks) {
            Point location = block.getLocation();
            block.setLocation(location.x, location.y + blockSize);
        }
    }

    private void move(int i) {
        for (Block block : blocks) {
            Point location = block.getLocation();
            if (i > 0 && location.x + i == PlayFrame.getInstance().getRescaledWidth()) return; // move to the right is out of bounds
            if (i < 0 && location.x == 0) return; // move to the left is out of bounds
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

        Block pivot = blocks.get(0); // Assume the first block is the pivot
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
        int blockSize = PlayFrame.getInstance().getRescaledBlockSize();
        return maxX - minX == blockSize && maxY - minY == blockSize;
    }
}
