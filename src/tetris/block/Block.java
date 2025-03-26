package tetris.block;

import tetris.game.PlayFrame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Block extends JPanel  {

    private final Point point;
    private final boolean raw;



    public Block(Point point, Color color) {
        this(point, color, Color.BLACK, false);
    }

    public Block(Point point, Color color, Color border, boolean raw) {
        this.raw = raw;
        this.point = point;
        setBackground(color);
        setBorder(new LineBorder(border));

        reloadLocationAndSize();
    }

    public void reloadLocationAndSize() {
        int blockSize = PlayFrame.getInstance().getRescaledBlockSize();

        setSize(blockSize, blockSize);

        if (raw) {
            setLocation(point.x, point.y);
        } else {
            setLocation(point.x * blockSize, point.y * -blockSize + PlayFrame.getInstance().getRescaledHeight() - blockSize);
        }
    }

    public static Block fromRawLocation(Point point, Color color) {
        int blockSize = PlayFrame.getInstance().getRescaledBlockSize();

        int x = point.x / blockSize;
        int y = (point.y - PlayFrame.getInstance().getRescaledHeight() + blockSize) / -blockSize;


        return new Block(new Point(x, y), color);
    }




}
