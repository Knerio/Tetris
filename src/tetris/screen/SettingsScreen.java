package tetris.screen;

import tetris.Main;
import tetris.MainFrame;
import tetris.block.toPlace.ToPlaceBlock;
import tetris.game.GameState;
import tetris.game.PlayFrame;
import tetris.label.TextLabel;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class SettingsScreen extends Screen {

    private boolean update = false;

    public SettingsScreen() {
        super(new Color(128, 128, 128), 255, 1, 255); // instant
    }

    @Override
    public CompletableFuture<Void> fadeIn() {
        onResize();
        return super.fadeIn();
    }

    @Override
    public void onResize() {
        try {
            if (!update) {
                if (getWidth() == MainFrame.getInstance().getWidth()) return;
                if (getHeight() == MainFrame.getInstance().getHeight()) return;
            }
            update = false;
            removeAll();

            setSize(MainFrame.getInstance().getSize());

            TextLabel label = new TextLabel("Steuerung");

            label.setBackground(new Color(0, 0, 0, 0));
            label.setSize(MainFrame.getInstance().getSize()); // will be centered anyways and then we can use a big font size
            label.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("./res/font", "Tetra.ttf")).deriveFont(((float) (MainFrame.getInstance().getScaleFactor() / 7))));
            label.setBorder(null);
            label.setForeground(Color.WHITE);
            label.setLocation(MainFrame.getInstance().getWidth() / 2 - label.getWidth() / 2, MainFrame.getInstance().getHeight() / 5 - label.getHeight() / 2);

            add(label);

            JButton linksButton = new JButton("Links");
            linksButton.setFont(new Font("Dialog", Font.PLAIN, MainFrame.getInstance().getScaleFactor() / 40));
            linksButton.setSize((int) (MainFrame.getInstance().getScaleFactor() * 0.2), (int) (MainFrame.getInstance().getScaleFactor() * 0.1));
            linksButton.setLocation((int) (MainFrame.getInstance().getWidth() / 16 - linksButton.getWidth() / 16),
                    (int) (MainFrame.getInstance().getHeight() / 2 - linksButton.getHeight() / 2));
            linksButton.addKeyListener(new KeyListener() {


                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    update = true;
                    ToPlaceBlock.leftKey = e.getKeyCode();
                    onResize();
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });

            add(linksButton);

            ImageIcon linksBild = new ImageIcon("res/key/" + ToPlaceBlock.leftKey + ".png");
            Image linksRescaled = linksBild.getImage().getScaledInstance(linksButton.getWidth(), linksButton.getWidth(), Image.SCALE_DEFAULT);
            JLabel linksBildLabel = new JLabel(new ImageIcon(linksRescaled));
            linksBildLabel.setLocation(linksButton.getX(), linksButton.getY() + linksButton.getHeight());
            linksBildLabel.setSize(linksBildLabel.getPreferredSize());


            add(linksBildLabel);

            // ---

            JButton rechtsButton = new JButton("Rechts");
            rechtsButton.setFont(new Font("Dialog", Font.PLAIN, MainFrame.getInstance().getScaleFactor() / 40));
            rechtsButton.setSize((int) (MainFrame.getInstance().getScaleFactor() * 0.2), (int) (MainFrame.getInstance().getScaleFactor() * 0.1));
            rechtsButton.setLocation((MainFrame.getInstance().getWidth() / 4 - rechtsButton.getWidth() / 4),
                    (int) (MainFrame.getInstance().getHeight() / 2 - rechtsButton.getHeight() / 2));
            rechtsButton.addKeyListener(new KeyListener() {


                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    update = true;
                    ToPlaceBlock.rightKey = e.getKeyCode();
                    onResize();
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });

            add(rechtsButton);

            ImageIcon rechtsBild = new ImageIcon("res/key/" + ToPlaceBlock.rightKey + ".png");
            Image rechtsRescaled = rechtsBild.getImage().getScaledInstance(rechtsButton.getWidth(), rechtsButton.getWidth(), Image.SCALE_DEFAULT);
            JLabel rechtsBildLabel = new JLabel(new ImageIcon(rechtsRescaled));
            rechtsBildLabel.setLocation(rechtsButton.getX(), rechtsButton.getY() + rechtsButton.getHeight());
            rechtsBildLabel.setSize(rechtsBildLabel.getPreferredSize());


            add(rechtsBildLabel);

            // ---
            JButton rotatebutton = new JButton("Rotieren");
            rotatebutton.setFont(new Font("Dialog", Font.PLAIN, MainFrame.getInstance().getScaleFactor() / 40));
            rotatebutton.setSize((int) (MainFrame.getInstance().getScaleFactor() * 0.2), (int) (MainFrame.getInstance().getScaleFactor() * 0.1));
            rotatebutton.setLocation((MainFrame.getInstance().getWidth() / 2 - rotatebutton.getWidth() / 2),
                    (int) (MainFrame.getInstance().getHeight() / 2 - rotatebutton.getHeight() / 2));
            rotatebutton.addKeyListener(new KeyListener() {


                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    update = true;
                    ToPlaceBlock.rotateKey = e.getKeyCode();
                    onResize();
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });

            add(rotatebutton);

            ImageIcon rotateBild = new ImageIcon("res/key/" + ToPlaceBlock.rotateKey + ".png");
            Image rotateRescaled = rotateBild.getImage().getScaledInstance(rotatebutton.getWidth(), rotatebutton.getWidth(), Image.SCALE_DEFAULT);
            JLabel rotateBildLabel = new JLabel(new ImageIcon(rotateRescaled));
            rotateBildLabel.setLocation(rotatebutton.getX(), rotatebutton.getY() + rotatebutton.getHeight());
            rotateBildLabel.setSize(rotateBildLabel.getPreferredSize());


            add(rotateBildLabel);
            //---
            JButton downbutton = new JButton("Runter");
            downbutton.setFont(new Font("Dialog", Font.PLAIN, MainFrame.getInstance().getScaleFactor() / 40));
            downbutton.setSize((int) (MainFrame.getInstance().getScaleFactor() * 0.2), (int) (MainFrame.getInstance().getScaleFactor() * 0.1));
            downbutton.setLocation((int) (MainFrame.getInstance().getWidth() / 1.4 - downbutton.getWidth() / 1.4),
                    (int) (MainFrame.getInstance().getHeight() / 2 - downbutton.getHeight() / 2));
            downbutton.addKeyListener(new KeyListener() {


                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    update = true;
                    ToPlaceBlock.downKey = e.getKeyCode();
                    onResize();
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });

            add(downbutton);

            ImageIcon downBild = new ImageIcon("res/key/" + ToPlaceBlock.downKey + ".png");
            Image downRescaled = downBild.getImage().getScaledInstance(downbutton.getWidth(), downbutton.getWidth(), Image.SCALE_DEFAULT);
            JLabel downBildLabel = new JLabel(new ImageIcon(downRescaled));
            downBildLabel.setLocation(downbutton.getX(), downbutton.getY() + downbutton.getHeight());
            downBildLabel.setSize(downBildLabel.getPreferredSize());


            add(downBildLabel);
            //---
            JButton instantdownbutton = new JButton("Direkt runter");
            instantdownbutton.setFont(new Font("Dialog", Font.PLAIN, MainFrame.getInstance().getScaleFactor() / 40));
            instantdownbutton.setSize((int) (MainFrame.getInstance().getScaleFactor() * 0.2), (int) (MainFrame.getInstance().getScaleFactor() * 0.1));
            instantdownbutton.setLocation((int) (MainFrame.getInstance().getWidth() / 1.1 - instantdownbutton.getWidth() / 1.1),
                    (int) (MainFrame.getInstance().getHeight() / 2 - instantdownbutton.getHeight() / 2));
            instantdownbutton.addKeyListener(new KeyListener() {


                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    update = true;
                    ToPlaceBlock.instantDownKey = e.getKeyCode();
                    onResize();
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });

            add(instantdownbutton);

            ImageIcon instantdownBild = new ImageIcon("res/key/" + ToPlaceBlock.instantDownKey + ".png");
            Image instantdownRescaled = instantdownBild.getImage().getScaledInstance(instantdownbutton.getWidth(), instantdownbutton.getWidth(), Image.SCALE_DEFAULT);
            JLabel instantdownBildLabel = new JLabel(new ImageIcon(instantdownRescaled));
            instantdownBildLabel.setLocation(instantdownbutton.getX(), instantdownbutton.getY() + instantdownbutton.getHeight());
            instantdownBildLabel.setSize(instantdownBildLabel.getPreferredSize());


            add(instantdownBildLabel);
            //---
            JButton zurückButton = new JButton("Zurück");

            zurückButton.setFont(new Font("Dialog", Font.PLAIN, MainFrame.getInstance().getScaleFactor() / 20));
            zurückButton.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
            zurückButton.setForeground(Color.BLACK);
            zurückButton.setSize(zurückButton.getPreferredSize());
            zurückButton.setLocation(MainFrame.getInstance().getWidth() / 2 - zurückButton.getWidth() / 2,
                    (int) (MainFrame.getInstance().getHeight() / 1.2 - zurückButton.getHeight() / 2));


            add(zurückButton);
            zurückButton.addActionListener(e -> {
                fadeOut();

                if (PlayFrame.getInstance().gameState == GameState.SETTING_START) {
                    MainFrame.getInstance().startScreen.fadeIn().thenRun(() -> {
                        PlayFrame.getInstance().gameState = GameState.START;
                    });
                } else if (PlayFrame.getInstance().gameState == GameState.SETTING_PAUSE) {
                    MainFrame.getInstance().pauseScreen.fadeIn().thenRun(() -> {
                        PlayFrame.getInstance().gameState = GameState.PAUSED;
                    });
                }

            });


        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
