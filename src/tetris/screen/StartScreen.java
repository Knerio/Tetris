package tetris.screen;

import tetris.Main;
import tetris.MainFrame;
import tetris.game.GameState;
import tetris.game.PlayFrame;
import tetris.label.TextLabel;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class StartScreen extends Screen {


    public StartScreen() {
        super(new Color(128, 128, 128), 255, 1, 255); // instant
        fadeIn(); // Starts faded in
    }

    @Override
    public CompletableFuture<Void> fadeOut() {
        return super.fadeOut().thenApply(unused -> {
            PlayFrame.getInstance().gameState = GameState.PLAYING;
            return unused;
        });
    }

    @Override
    public void onResize() {
        try {
            if (getWidth() == MainFrame.getInstance().getWidth()) return;
            if (getHeight() == MainFrame.getInstance().getHeight()) return;
            removeAll();

            setSize(MainFrame.getInstance().getSize());


            ImageIcon image = new ImageIcon("res/background.png");
            Image rescaled = image.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
            ImageIcon rescaledIcon = new ImageIcon(rescaled);
            add(new JLabel(rescaledIcon),  -1);

            TextLabel label = new TextLabel("TETRIS");

            label.setBackground(new Color(0, 0, 0, 0));
            label.setSize(MainFrame.getInstance().getSize()); // will be centered anyways and then we can use a big font size
            label.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("./res/font", "Tetra.ttf")).deriveFont(((float) (MainFrame.getInstance().getScaleFactor() / 3))));
            label.setBorder(null);
            label.setForeground(Color.WHITE);
            label.setLocation(MainFrame.getInstance().getWidth() / 2 - label.getWidth() / 2, MainFrame.getInstance().getHeight() / 5 - label.getHeight() / 2);

            add(label);



            JButton startButton = new JButton("Start");

            startButton.setFont(new Font("Dialog", Font.PLAIN, MainFrame.getInstance().getScaleFactor() / 20));
            startButton.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
            startButton.setForeground(Color.BLACK);
            startButton.setSize(startButton.getPreferredSize());
            startButton.setLocation(MainFrame.getInstance().getWidth() / 2 - startButton.getWidth() / 2, MainFrame.getInstance().getHeight() / 2 - startButton.getHeight() / 2);


            add(startButton);

            JTextField nameInput = new JTextField("<Name>");
            nameInput.setEditable(true);
            nameInput.setSize(200, 20);
            nameInput.setLocation(MainFrame.getInstance().getWidth() / 2 - nameInput.getWidth() / 2, MainFrame.getInstance().getHeight() / 2 - nameInput.getHeight() / 2 + startButton.getHeight());


            add(nameInput, 1);

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
                    PlayFrame.getInstance().gameState = GameState.SETTING_START;
                });
            });


            startButton.addActionListener(e -> {
                MainFrame.getInstance().playerName = nameInput.getText().replace(" ", "_");
                fadeOut();
            });

        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
