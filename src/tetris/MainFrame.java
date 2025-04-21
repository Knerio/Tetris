package tetris;

import tetris.block.DisplayNextBlockFrame;
import tetris.game.GameState;
import tetris.game.PlayFrame;
import tetris.label.TextLabel;
import tetris.screen.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

public class MainFrame extends JFrame {

    private static MainFrame instance;

    private static final String RECORD_FILE_NAME = "record.properties";

    private int score = 0;

    private int record = 0;
    private int allTimeRecord = 0;
    private String allTimeRecordName = "";

    private JLabel backgroundImage;

    private PlayFrame playFrame;

    public Screen pauseScreen, startScreen, lostScreen, settingsScreen;

    public DisplayNextBlockFrame displayNextBlockFrame;

    private TextLabel nextBlockLabel;

    private final AtomicLong lastResizeTime = new AtomicLong(0);

    public String playerName;




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

        startScreen = new StartScreen();
        add(startScreen);

        lostScreen = new LostScreen();
        add(lostScreen);

        settingsScreen = new SettingsScreen();
        add(settingsScreen);


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
        final JLabel allTimeRecordLabel = new TextLabel("Allzeit-Rekord: xxx von xxx");
        allTimeRecordLabel.setLocation(20, 80);

        new Timer(200, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                record = readRecord(playerName);
                allTimeRecord = readRecord();
                allTimeRecordName = readRecordName();

                scoreLabel.setText("Score: " + getScore());
                recordLabel.setText("Rekord: " + record);
                allTimeRecordLabel.setText("Allzeit-Rekord: " + allTimeRecord + " von " + allTimeRecordName);

                if (!hasFocus() && (PlayFrame.getInstance().gameState == GameState.PLAYING || PlayFrame.getInstance().gameState == GameState.PAUSED)) {
                    requestFocus(); // Is needed when buttons are pressed and the main frame gets unfocused
                }

                playFrame.onResize();
                onResize();
            }
        }).start();
        add(scoreLabel);
        add(recordLabel);
        add(allTimeRecordLabel);

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

    private String readRecordName() {
        try {
            File file = new File(".", RECORD_FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }
            Properties properties = new Properties();
            properties.load(new FileReader(file));
            return properties.entrySet().stream()
                    .map(entry -> new AbstractMap.SimpleEntry<>((String) entry.getKey(), Integer.parseInt((String) entry.getValue())))
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .map(s -> s.replace("rekord_", ""))
                    .orElse(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    private void onResize() {
        pauseScreen.onResize();
        startScreen.onResize();
        lostScreen.onResize();
        settingsScreen.onResize();

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

        nextBlockLabel.setLocation(playFrame.getX() + playFrame.getWidth(), playFrame.getY());
    }

    public int getScaleFactor() {
        return Math.min(getWidth(), getHeight());
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
            writeRecord(playerName);
        }
    }

    public void setLastScore(int lastScore) {
        if (lastScore > record) {
            record = lastScore;
            writeRecord(playerName);
        }
    }


    private void writeRecord(String name) {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(RECORD_FILE_NAME));
            properties.put("rekord_" + name, String.valueOf(record));
            properties.store(new FileWriter(RECORD_FILE_NAME), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int readRecord(String name) {
        try {
            File file = new File(".", RECORD_FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }
            Properties properties = new Properties();
            properties.load(new FileReader(file));
            return Integer.parseInt(properties.getProperty("rekord_" + name, "0"));
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
            return properties.entrySet().stream().map(entry -> Integer.parseInt(entry.getValue().toString())).max(Comparator.comparingInt(o -> o)).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
