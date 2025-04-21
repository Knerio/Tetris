package tetris.game;

import tetris.MainFrame;
import tetris.audio.AudioManager;
import tetris.audio.ClipType;
import tetris.block.BackgroundBlock;
import tetris.block.Block;
import tetris.block.toPlace.BlockType;
import tetris.block.toPlace.ToPlaceBlock;
import tetris.utils.Pair;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class PlayFrame extends JPanel {

    private static PlayFrame instance;
    public static final Color BACKGROUND_COLOR = new Color(37, 57, 61, 255);


    private final AudioManager audioManager = new AudioManager();

    public final Stack<Pair<BlockType, Color>> comingBlocks = new Stack<>();


    public static final Integer BLOCKS_PER_WIDTH = 15;

    public static final Integer BLOCKS_PER_HEIGHT = 20;

    private Integer scaledWidth, scaledHeight, scaledSize;

    public GameState gameState = GameState.START;

    private final List<Block> placedBlocks = new ArrayList<>();

    public PlayFrame() {
        instance = this;

        comingBlocks.push(new Pair<>(BlockType.getRandomType(new ArrayList<>()), getRandomColor()));

        setBackground(Color.lightGray);
        setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        setLayout(null);

        scaledWidth = MainFrame.getInstance().getWidth() / 3;
        scaledSize = scaledWidth / BLOCKS_PER_WIDTH;

        scaledWidth -= scaledWidth % scaledSize;

        scaledHeight = BLOCKS_PER_HEIGHT * scaledSize;

        setSize(getRescaledWidth(), getRescaledHeight());
        onResize();

        new Timer(500, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameState != GameState.PLAYING) return;

                // resets score if needed
                if (placedBlocks.isEmpty()) {
                    MainFrame.getInstance().setScore(0);
                    for (Block placedBlock : placedBlocks) {
                        remove(placedBlock);
                    }
                }


                // Updates the blocks
                for (Block placedBlock : new ArrayList<>(placedBlocks)) {
                    // Can be null if its getting removed in the same moment
                    if (placedBlock == null) continue;
                    add(placedBlock, 1);

                }

                MainFrame.getInstance().revalidate();
                MainFrame.getInstance().repaint();

            }
        }).start();
        computeNewToPlaceBlocks();


        for (int x = 0; x < BLOCKS_PER_WIDTH; x++) {
            for (int y = 0; y < BLOCKS_PER_HEIGHT; y++) {
                add(new BackgroundBlock(new Point(x, y), BACKGROUND_COLOR));
            }
        }

        audioManager.playAudio(ClipType.BACKGROUND, true);

    }

    public void updateGameState(GameState gameState) {
        if (MainFrame.getInstance().pauseScreen.isFading()) return;
        switch (gameState) {
            case PLAYING:
                MainFrame.getInstance().pauseScreen.fadeOut().thenRun(() -> {
                    this.gameState = gameState; // It should only continue after the screen is completely closed for ux
                });
                break;

            case PAUSED:
                this.gameState = gameState; // It should be paused before the fade in for ux
                MainFrame.getInstance().pauseScreen.fadeIn();
                break;
        }
    }


    public void onResize() {
        setLocation(
                (MainFrame.getInstance().getWidth() / 2 - getWidth() / 2),
                (MainFrame.getInstance().getHeight() / 2 - getHeight() / 2)
        );

    }

    public int getBaseBlockY() {
        return BLOCKS_PER_HEIGHT;
    }

    public int getBaseBlockX() {
        return getNearestMultiplier((double) BLOCKS_PER_WIDTH / 2, 2);
    }

    public int getRescaledWidth() {
        return scaledWidth;
    }

    public int getRescaledHeight() {
        return scaledHeight;
    }

    public int getRescaledBlockSize() {
        return scaledSize;
    }

    public int getNearestMultiplier(double base, int multiplier) {
        int lower = (int) (base / multiplier) * multiplier;
        int upper = lower + multiplier;

        return (base - lower) <= (upper - base) ? lower : upper;
    }

    private CompletableFuture<Void> removeLines(List<Integer> linesToRemove) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        java.util.Timer timer = new java.util.Timer();

        timer.schedule(new TimerTask() {

            int i = 0;

            @Override
            public void run() {
                int middle = BLOCKS_PER_WIDTH / 2;
                int right = middle + i;
                int left = middle - i;
                if (left < 0) {
                    timer.cancel();
                    future.complete(null);
                    return;
                }

                for (Block placedBlock : new ArrayList<>(placedBlocks)) {
                    // can be null if its getting removed
                    if (placedBlock == null) continue;
                    Point location = placedBlock.getLocation();
                    int downscaledX = location.x / getRescaledBlockSize();
                    if (linesToRemove.contains(location.y) && (downscaledX == right || downscaledX == left)) {
                        placedBlocks.remove(placedBlock);
                        placedBlock.setVisible(false);
                        remove(placedBlock);
                    }
                }

                MainFrame.getInstance().revalidate();
                MainFrame.getInstance().repaint();
                i++;
            }
        }, 50, 50);

        return future;
    }


    private final List<BlockType> lastBlocks = new ArrayList<>();

    private void computeNewToPlaceBlocks() {

        if (lastBlocks.size() > 3) {
            lastBlocks.remove(0);
        }


        Pair<BlockType, Color> selectedType = comingBlocks.pop();
        lastBlocks.add(selectedType.getFirst());


        new ToPlaceBlock(selectedType.getFirst().getPoints().stream().map(point -> new Block(point, selectedType.getSecond(), Color.BLACK, false)).collect(Collectors.toList()), () -> {
            deleteLineOrLose().thenRun(() -> {
                computeNewToPlaceBlocks();
                MainFrame.getInstance().displayNextBlockFrame.updateNext();
            });
        });
        Color color = getRandomColor();
        BlockType randomType = BlockType.getRandomType(lastBlocks);
        comingBlocks.push(new Pair<>(randomType, color));
    }

    private CompletableFuture<Void> deleteLineOrLose() {
        CompletableFuture<Void> completion = new CompletableFuture<>();

        Map<Integer, Integer> blocksToRemove = new HashMap<>();
        for (Block block : placedBlocks) {
            blocksToRemove.putIfAbsent(block.getY(), 0);
            blocksToRemove.put(block.getY(), blocksToRemove.get(block.getY()) + 1);
        }

        List<Integer> linesToRemove = new ArrayList<>();
        blocksToRemove.forEach((yLevel, amount) -> {
            int score = MainFrame.getInstance().getScore();
            if (yLevel <= 0) {
                MainFrame.getInstance().setLastScore(score);
                for (Block placedBlock : new ArrayList<>(placedBlocks)) {
                    placedBlocks.remove(placedBlock);
                    placedBlock.setVisible(false);
                    remove(placedBlock);
                }
                MainFrame.getInstance().revalidate();
                MainFrame.getInstance().repaint();

                loseFunction();
                completion.complete(null);
                return;
            }
            if (amount.equals(BLOCKS_PER_WIDTH)) {
                linesToRemove.add(yLevel);
            }
        });
        if (linesToRemove.isEmpty()) {
            completion.complete(null);
            return completion;
        }
        int score = MainFrame.getInstance().getScore();
        MainFrame.getInstance().setScore(score + calculateScore(linesToRemove.size()));

        removeLines(linesToRemove).thenRun(() -> {
            for (Integer downYLevel : linesToRemove.stream().sorted().collect(Collectors.toList())) {
                for (Block placedBlock : placedBlocks) {
                    Point location = placedBlock.getLocation();
                    if (location.y > downYLevel) continue;
                    placedBlock.setLocation(location.x, location.y + getRescaledBlockSize()); // move down the rest of the lines that are upper
                }
            }
            completion.complete(null);
        });

        return completion;
    }

    private int calculateScore(int linesCleared) {
        switch (linesCleared) {
            case 1:
                return 60;
            case 2:
                return 150;
            case 3:
                return 350;
            case 4:
                return 1000;
            default:
                return 0;
        }
    }


    private void loseFunction() {
        audioManager.stopAudio(ClipType.BACKGROUND);
        audioManager.playAudio(ClipType.GAME_OVER).thenRun(() -> {
            audioManager.playAudio(ClipType.BACKGROUND, true);
        });
        MainFrame.getInstance().lostScreen.fadeIn();
    }


    private Color getRandomColor() {
        List<Color> colors = new ArrayList<>(List.of(Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE, Color.MAGENTA));
        Collections.shuffle(colors);

        return colors.get(0);
    }


    public List<Block> getPlacedBlocks() {
        return placedBlocks;
    }

    public static PlayFrame getInstance() {
        return instance;
    }
}
