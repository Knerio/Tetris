package tetris.screen;

import tetris.MainFrame;
import tetris.game.GameState;
import tetris.game.PlayFrame;
import tetris.label.TextLabel;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class PauseScreen extends Screen {


    public PauseScreen() {
        super(new Color(128, 128, 128), 130);
        MainFrame.getInstance().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (PlayFrame.getInstance().gameState == GameState.PLAYING) {
                        fadeIn();
                        PlayFrame.getInstance().gameState = GameState.PAUSED;
                    } else if (PlayFrame.getInstance().gameState == GameState.PAUSED) {
                        fadeOut();
                        PlayFrame.getInstance().gameState = GameState.PLAYING;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }


    // Will be called on load
    @Override
    public void onResize() {
        try {
            removeAll();

            setSize(MainFrame.getInstance().getSize());

            TextLabel label = new TextLabel("Pausiert");

            label.setBackground(new Color(0, 0, 0, 0));
            label.setSize(MainFrame.getInstance().getSize()); // will be centered anyways and then we can use a big font size
            label.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("./res/font", "Tetra.ttf")).deriveFont(((float) (MainFrame.getInstance().getScaleFactor() / 7))));
            label.setBorder(null);
            label.setForeground(Color.WHITE);
            label.setLocation(MainFrame.getInstance().getWidth() / 2 - label.getWidth() / 2, MainFrame.getInstance().getHeight() / 5 - label.getHeight() / 2);

            add(label);


            JButton settingsButton = new JButton("Steuerung");

            settingsButton.setFont(new Font("Dialog", Font.PLAIN, MainFrame.getInstance().getScaleFactor() / 20));
            settingsButton.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
            settingsButton.setForeground(Color.BLACK);
            settingsButton.setSize(settingsButton.getPreferredSize());
            settingsButton.setLocation(MainFrame.getInstance().getWidth() / 2 - settingsButton.getWidth() / 2,
                    (int) (MainFrame.getInstance().getHeight() / 1.55 - settingsButton.getHeight() / 2));


            add(settingsButton);
            settingsButton.addActionListener(e -> {
                fadeOut().thenRun(() -> {
                    MainFrame.getInstance().settingsScreen.fadeIn();
                    PlayFrame.getInstance().gameState = GameState.SETTING_PAUSE;
                });
            });
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }


}
