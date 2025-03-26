package tetris;

import tetris.block.DisplayNextBlockFrame;
import tetris.game.PlayFrame;
import tetris.label.TextLabel;
import tetris.screen.PauseScreen;
import tetris.screen.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

public class MainFrame extends JFrame {

    private static MainFrame instance;

    private static final String RECORD_FILE_NAME = "record.properties";

    private int score = 0;

    private int record = 0;

    private JLabel backgroundImage;

    private PlayFrame playFrame;

    public Screen pauseScreen;

    public DisplayNextBlockFrame displayNextBlockFrame;

    private TextLabel nextBlockLabel;

    private final AtomicLong lastResizeTime = new AtomicLong(0);


    public MainFrame() throws HeadlessException {
        instance = this;

        setTitle("Tetris - By Luis & Dario");
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) (size.getWidth() * 0.6), (int) (size.getHeight() * 0.6));
        setLocation(
                (size.width / 2 - getWidth() / 2),
                (size.height / 2 - getHeight() / 2)
        );
        setMinimumSize(new Dimension(850, 600));

        pauseScreen = new PauseScreen();
        add(pauseScreen);

        playFrame = new PlayFrame();
        add(playFrame);


        displayNextBlockFrame = new DisplayNextBlockFrame();
        add(displayNextBlockFrame);
        nextBlockLabel = new TextLabel("Nächster Block");
        add(nextBlockLabel);


        final JLabel scoreLabel = new TextLabel("Score: xxx");
        scoreLabel.setLocation(20, 20);
        final JLabel recordLabel = new TextLabel("Rekord: xxx");
        recordLabel.setLocation(20, 50);

        new Timer(200, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                record = readRecord();

                scoreLabel.setText("Score: " + getScore());
                recordLabel.setText("Rekord: " + record);

                playFrame.onResize();
                onResize();
            }
        }).start();
        add(scoreLabel);
        add(recordLabel);

        onResize();


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (lastResizeTime.get() + 1_000 < System.currentTimeMillis()) {
                    lastResizeTime.set(System.currentTimeMillis());
                    playFrame.onResize();
                    onResize();
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                if (lastResizeTime.get() + 1_000 < System.currentTimeMillis()) {
                    lastResizeTime.set(System.currentTimeMillis());
                    playFrame.onResize();
                    onResize();
                }
            }
        });

    }

    private void onResize() {
        ImageIcon image = new ImageIcon("res/background.png");
        Image rescaled = image.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
        ImageIcon rescaledIcon = new ImageIcon(rescaled);
        if (backgroundImage == null) {
            JLabel newBackgroundImage = new JLabel(rescaledIcon);
            add(newBackgroundImage);
            backgroundImage = newBackgroundImage;
        } else {
            backgroundImage.setIcon(rescaledIcon);
        }
        displayNextBlockFrame.onResize();
        pauseScreen.onResize();

        nextBlockLabel.setLocation(playFrame.getX() + playFrame.getWidth(), playFrame.getY());
    }

    public static MainFrame getInstance() {
        return instance;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        if (score > record) {
            record = score;
            writeRecord();
        }
    }

    public void setLastScore(int lastScore) {
        if (lastScore > record) {
            record = lastScore;
            writeRecord();
        }
    }


    private void writeRecord() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(RECORD_FILE_NAME));
            properties.put("record", String.valueOf(record));
            properties.store(new FileWriter(RECORD_FILE_NAME), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int readRecord() {
        try {
            File file = new File(".", RECORD_FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }
            Properties properties = new Properties();
            properties.load(new FileReader(file));
            return Integer.parseInt(properties.getProperty("record", "0"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
