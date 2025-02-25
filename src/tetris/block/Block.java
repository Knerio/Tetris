package tetris.block;

import tetris.PlayFrame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Block extends JPanel {

    private final Point point;

    public Block(Point point, Color color) {
        this.point = point;

        setBackground(color);
        setSize(20, 20);
        setBorder(new LineBorder(Color.black));

        setLocation(point.x * 20, point.y * -20 + PlayFrame.getInstance().getHeight() - 20);
    }



    public Point getPoint() {
        return point;
    }
}
