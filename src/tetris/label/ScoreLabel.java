package tetris.label;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;

public class ScoreLabel extends JLabel {

    public ScoreLabel() {
        setOpaque(true);
        setBackground(Color.lightGray);
        setForeground(Color.black);
        setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        setSize(120, 30);
        setHorizontalAlignment(SwingConstants.CENTER);
    }
}
