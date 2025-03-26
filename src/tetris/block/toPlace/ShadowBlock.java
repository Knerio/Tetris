package tetris.block.toPlace;

import tetris.game.PlayFrame;
import tetris.block.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShadowBlock {

    private final ToPlaceBlock toPlaceBlock;

    private final List<Block> shadows = new ArrayList<>();


    public ShadowBlock(ToPlaceBlock toPlaceBlock) {
        this.toPlaceBlock = toPlaceBlock;
        int negY = calculateYOffset();
        int blockSize = PlayFrame.getInstance().getRescaledBlockSize();

        for (Block blockBlock : toPlaceBlock.getBlocks()) {
            Block newBlock = new Block(
                    new Point(
                            blockBlock.getLocation().x,
                            (blockBlock.getLocation().y - blockSize) + (negY * blockSize)
                    ),
                    PlayFrame.BACKGROUND_COLOR, Color.GRAY, true
            );
            shadows.add(newBlock);
            PlayFrame.getInstance().add(newBlock, 0);
        }
    }



    public void update() {
        int blockSize = PlayFrame.getInstance().getRescaledBlockSize();
        int negY = calculateYOffset();

        for (int i = 0; i < toPlaceBlock.getBlocks().size(); i++) {
            Block placed = toPlaceBlock.getBlocks().get(i);
            Point point = new Point(
                    placed.getLocation().x,
                    (placed.getLocation().y - blockSize) + (negY * blockSize)
            );
            shadows.get(i).setLocation(point);
        }

        // This removes shadow blocks where the to place block is blocking that
        for (Block blockBlock : toPlaceBlock.getBlocks()) {
            for (Block shadow : shadows) {
                if (shadow.getX() == blockBlock.getX() && shadow.getY() == blockBlock.getY()) {
                    shadow.setVisible(false);
                }
            }
        }

        // This re adds the block if they are visible
        for (Block shadow : shadows) {
            boolean intersects = false;
            for (Block blockBlock : toPlaceBlock.getBlocks()) {
                if (shadow.getX() == blockBlock.getX() && shadow.getY() == blockBlock.getY()) {
                    intersects = true;
                }
            }
            if (!intersects) {
                shadow.setVisible(true);
            }
        }
    }

    public void remove() {
        for (Block placedBlock : new ArrayList<>(shadows)) {
            shadows.remove(placedBlock);
            PlayFrame.getInstance().remove(placedBlock);
        }
    }

    private int calculateYOffset() {
        int blockSize = PlayFrame.getInstance().getRescaledBlockSize();
        int negY = 0;
        boolean breakOut = false;
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            for (Block blockBlock : toPlaceBlock.getBlocks()) {
                if (blockBlock.getY() + (blockSize * i) >= PlayFrame.getInstance().getHeight()) { // block would go through the floor
                    breakOut = true;
                    break;
                }
                for (Block placedBlock : PlayFrame.getInstance().getPlacedBlocks()) {
                    if (placedBlock.getY() == blockBlock.getY() + (blockSize * i) && placedBlock.getX() == blockBlock.getX()) {
                        breakOut = true;
                        break;
                    }
                }
            }
            negY += 1;
            if (breakOut) break;
        }
        return negY;
    }
}
