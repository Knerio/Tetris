package tetris.label;

import tetris.game.PlayFrame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class TextLabel extends JLabel {

    public TextLabel(String text) {
        super(text, JLabel.CENTER);
        setOpaque(true);
        setBackground(new Color(155, 155, 155, 230));
        setForeground(Color.black);
        setBorder(new LineBorder(Color.black));

        setSize(PlayFrame.getInstance().getRescaledBlockSize() * 9, 30);
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(new Font("Arial", Font.PLAIN, 15));
        setText(text);
    }
}
