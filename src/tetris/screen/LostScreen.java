package tetris.screen;

import tetris.MainFrame;
import tetris.game.GameState;
import tetris.game.PlayFrame;
import tetris.label.TextLabel;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class LostScreen extends Screen {

    public LostScreen() {
        super(new Color(128, 128, 128), 255, 15, 7);
    }

    @Override
    public CompletableFuture<Void> fadeIn() {
        PlayFrame.getInstance().gameState = GameState.LOST;
        return super.fadeIn();
    }

    @Override
    public CompletableFuture<Void> fadeOut() {
        return super.fadeOut().thenApply(v -> {
            PlayFrame.getInstance().gameState = GameState.PLAYING;
            return v;
        });
    }

    // Will be called on load
    @Override
    public void onResize() {
        try {
            removeAll();

            setSize(MainFrame.getInstance().getSize());

            TextLabel label = new TextLabel("Verloren");

            label.setBackground(new Color(0, 0, 0, 0));
            label.setSize(MainFrame.getInstance().getSize()); // will be centered anyways and then we can use a big font size
            label.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("./res/font", "Tetra.ttf")).deriveFont(((float) (MainFrame.getInstance().getScaleFactor() / 6))));
            label.setBorder(null);
            label.setForeground(Color.WHITE);
            label.setLocation(MainFrame.getInstance().getWidth() / 2 - label.getWidth() / 2, MainFrame.getInstance().getHeight() / 5 - label.getHeight() / 2);

            add(label);

            JButton startButton = new JButton("Nochmal");

            startButton.setFont(new Font("Dialog", Font.PLAIN, MainFrame.getInstance().getScaleFactor() / 20));
            startButton.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
            startButton.setForeground(Color.BLACK);
            startButton.setSize(startButton.getPreferredSize());
            startButton.setLocation(MainFrame.getInstance().getWidth() / 2 - startButton.getWidth() / 2, MainFrame.getInstance().getHeight() / 2 - startButton.getHeight() / 2);
            startButton.addActionListener(e -> {
                fadeOut();
            });

            add(startButton);

        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
