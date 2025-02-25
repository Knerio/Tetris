package tetris;

import tetris.label.ScoreLabel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    private static MainFrame instance;

    private int score = 0;
    private int lastScore = -1;

    public MainFrame() throws HeadlessException {
        instance = this;

        setTitle("Tetris");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 500);

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(
                (size.width / 2 - getWidth() / 2),
                (size.height / 2 - getHeight() / 2)
        );

        add(new PlayFrame());

        JLabel scoreLabel = new ScoreLabel();
        scoreLabel.setLocation(20, 20);
        JLabel lastScoreLabel = new ScoreLabel();
        lastScoreLabel.setLocation(20, 50);

        new Timer(1000, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scoreLabel.setText("Score: " + getScore());
                if (lastScore != -1) {
                    lastScoreLabel.setText("Last Score: " + getLastScore());
                }
            }
        }).start();
        add(scoreLabel);
        add(lastScoreLabel);

        add(new JLabel(new ImageIcon("res/background.png")));
    }

    public static MainFrame getInstance() {
        return instance;
    }

    public synchronized int getScore() {
        return score;
    }

    public synchronized void setScore(int score) {
        this.score = score;
    }

    public synchronized int getLastScore() {
        return lastScore;
    }

    public synchronized void setLastScore(int lastScore) {
        this.lastScore = lastScore;
    }
}
