package tetris.screen;

import tetris.MainFrame;
import tetris.label.TextLabel;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PauseScreen extends Screen {


    public PauseScreen() {
        super(new Color(128, 128, 128), 130);
        setLayout(null);
        setVisible(false);
    }


    // Will be called on load
    public void onResize() {
        try {
            removeAll();

            setSize(MainFrame.getInstance().getSize());

            TextLabel label = new TextLabel("Pausiert");

            label.setBackground(new Color(0, 0, 0, 0));
            label.setSize(MainFrame.getInstance().getSize()); // will be centered anyways and then we can use a big font size
            label.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("./res/font", "Tetra.ttf")).deriveFont(((float) (MainFrame.getInstance().getHeight() / 7))));
            label.setBorder(null);
            label.setForeground(Color.WHITE);
            label.setLocation(MainFrame.getInstance().getWidth() / 2 - label.getWidth() / 2, MainFrame.getInstance().getHeight() / 5 - label.getHeight() / 2);

            add(label);

        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }



}
