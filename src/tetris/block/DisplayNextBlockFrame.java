package tetris.block;

import tetris.MainFrame;
import tetris.utils.Pair;
import tetris.game.PlayFrame;
import tetris.block.toPlace.BlockType;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;

public class DisplayNextBlockFrame extends JPanel {


    public DisplayNextBlockFrame() {
        int blockSize = PlayFrame.getInstance().getRescaledBlockSize();
        setSize(blockSize * 5, blockSize * 5); // 5x5 blocks
        onResize();
        setBounds(getX(), getY(), getWidth(), getHeight());
        setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        setLayout(null);
        setBackground(new Color(128, 128, 128, 150));


        updateNext();
    }

    public void updateNext() {
        for (Component component : getComponents()) {
            remove(component);
        }

        int blockSize = PlayFrame.getInstance().getRescaledBlockSize();

        PlayFrame playFrame = PlayFrame.getInstance();
        Pair<BlockType, Color> peek = playFrame.comingBlocks.peek();
        if (peek == null) return;

        BlockType blockType = peek.getFirst();
        Color blockColor = peek.getSecond();

        // Find min/max coordinates
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (Point p : blockType.getPoints()) {
            minX = Math.min(minX, (int) p.getX());
            minY = Math.min(minY, (int) p.getY());
            maxX = Math.max(maxX, (int) p.getX());
            maxY = Math.max(maxY, (int) p.getY());
        }

        int blockWidth = (maxX - minX + 1) * blockSize;
        int blockHeight = (maxY - minY + 1) * blockSize;

        int offsetX = (getWidth() - blockWidth) / 2;
        int offsetY = (getHeight() - blockHeight) / 2;


        for (Point originalPoint : blockType.getPoints()) {
            int x = (int) ((originalPoint.getX() - minX) * blockSize + offsetX);
            int y = (int) ((maxY - originalPoint.getY()) * blockSize + offsetY);  // flips y-coords to represent the block right

            add(new Block(new Point(x, y), blockColor, Color.BLACK, true));
        }
        MainFrame.getInstance().revalidate();
        MainFrame.getInstance().repaint();
    }

    public void onResize() {
        setLocation(PlayFrame.getInstance().getX() + PlayFrame.getInstance().getWidth(), PlayFrame.getInstance().getY() + 30 );
    }
}
