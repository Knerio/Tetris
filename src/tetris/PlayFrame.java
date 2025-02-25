package tetris;

import tetris.block.Block;
import tetris.block.ToPlaceBlock;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class PlayFrame extends JPanel {

    private static PlayFrame instance;

    private static final Integer WIDTH = 300;
    private static final Integer HEIGHT = 400;

    private static final Integer BLOCKS_PER_WIDTH = WIDTH / 20;

    private final List<Block> placedBlocks = new ArrayList<>();

    public PlayFrame() {
        instance = this;

        setBackground(Color.lightGray);
        setLayout(null);
        setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        setSize(WIDTH, HEIGHT);

        setLocation(
                (MainFrame.getInstance().getWidth() / 2 - getWidth() / 2),
                (MainFrame.getInstance().getHeight() / 2 - getHeight() / 2)
        );

        computeNewToPlaceBlocks();


        new Timer(500, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (placedBlocks.isEmpty()) {
                    MainFrame.getInstance().setScore(0);
                }
                for (Block placedBlock : placedBlocks) {
                    add(placedBlock);
                    revalidate();
                    repaint();
                }
                Map<Integer, Integer> blocksToRemove = new HashMap<>();
                for (Block block : placedBlocks) {
                    blocksToRemove.putIfAbsent(block.getY(), 0);
                    blocksToRemove.put(block.getY(), blocksToRemove.get(block.getY()) + 1);
                }

                blocksToRemove.forEach((yLevel, amount) -> {
                    int score = MainFrame.getInstance().getScore();
                    if (yLevel < 20) {
                        MainFrame.getInstance().setLastScore(score);
                        for (Block placedBlock : new ArrayList<>(placedBlocks)) {
                            placedBlocks.remove(placedBlock);
                            remove(placedBlock);
                        }
                        return;
                    }
                    if (amount.equals(BLOCKS_PER_WIDTH)) {
                        MainFrame.getInstance().setScore(score + 60);
                        for (Block placedBlock : new ArrayList<>(placedBlocks)) {
                            Point location = placedBlock.getLocation();
                            if (location.y == yLevel) {
                                placedBlocks.remove(placedBlock);
                                remove(placedBlock);
                            }
                        }
                        for (Block placedBlock : placedBlocks) {
                            Point location = placedBlock.getLocation();
                            if (location.y > yLevel) continue;
                            placedBlock.setLocation(location.x, location.y + 20); // 20 because one block is 20
                        }
                    }
                });
            }
        }).start();
    }

    private void computeNewToPlaceBlocks() {
        int baseX = BLOCKS_PER_WIDTH / 2;
        int baseY = 20;

        List<Color> colors = new ArrayList<>(List.of(Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE, Color.WHITE));
        Collections.shuffle(colors);

        Color color = colors.getFirst();
        List<List<Point>> blocks = new ArrayList<>();


        /*
         *    +
         *  + + +
         */
        blocks.add(List.of(
                new Point(baseX, baseY - 1),
                new Point(baseX, baseY),
                new Point(baseX + 1, baseY - 1),
                new Point(baseX - 1 , baseY - 1)
        ));

        /*
         *  + +
         *  + +
         */
        blocks.add(List.of(
                new Point(baseX, baseY),
                new Point(baseX + 1, baseY),
                new Point(baseX + 1, baseY + 1),
                new Point(baseX , baseY + 1)
                ));

        /*
         *    + +
         *  + +
         */
        blocks.add(List.of(
                new Point(baseX, baseY),
                new Point(baseX + 1, baseY),
                new Point(baseX, baseY - 1),
                new Point(baseX - 1, baseY - 1)
        ));

        /*
         *  + +
         *    + +
         */
        blocks.add(List.of(
                new Point(baseX + 1, baseY - 1),
                new Point(baseX, baseY),
                new Point(baseX + 1, baseY),
                new Point(baseX + 2, baseY - 1)
        ));

        /*
         *  + + + + +
         */
        blocks.add(List.of(
                new Point(baseX + 2, baseY),
                new Point(baseX, baseY),
                new Point(baseX + 1, baseY),
                new Point(baseX + 3, baseY),
                new Point(baseX + 4, baseY)
        ));

        /*
            + +
            +
            +
         */
        blocks.add(List.of(
                new Point(baseX, baseY + 1),
                new Point(baseX, baseY),
                new Point(baseX, baseY + 2),
                new Point(baseX + 1, baseY + 2)
        ));

        Collections.shuffle(blocks);

        new ToPlaceBlock(blocks.getFirst().stream().map(point -> new Block(point, color)).toList(), this::computeNewToPlaceBlocks);
    }

    public List<Block> getPlacedBlocks() {
        return placedBlocks;
    }

    public static PlayFrame getInstance() {
        return instance;
    }
}
