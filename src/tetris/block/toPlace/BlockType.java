package tetris.block.toPlace;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static tetris.game.PlayFrame.getInstance;

public enum BlockType {

    /*
     *    +
     *  + + +
     */
    T_BLOCK(() -> List.of(
            new Point(getInstance().getBaseBlockX(), getInstance().getBaseBlockY() - 1),
            new Point(getInstance().getBaseBlockX(), getInstance().getBaseBlockY()),
            new Point(getInstance().getBaseBlockX() + 1, getInstance().getBaseBlockY() - 1),
            new Point(getInstance().getBaseBlockX() - 1, getInstance().getBaseBlockY() - 1)
    ), 10),

    /*
     *    + +
     *  + +
     */
    S_BLOCK(() -> List.of(
            new Point(getInstance().getBaseBlockX(), getInstance().getBaseBlockY()),
            new Point(getInstance().getBaseBlockX() + 1, getInstance().getBaseBlockY()),
            new Point(getInstance().getBaseBlockX(), getInstance().getBaseBlockY() - 1),
            new Point(getInstance().getBaseBlockX() - 1, getInstance().getBaseBlockY() - 1)
    ), 5),


    /*
     *  + +
     *    + +
     */
    Z_BLOCK(() -> List.of(
            new Point(getInstance().getBaseBlockX() + 1, getInstance().getBaseBlockY() - 1),
            new Point(getInstance().getBaseBlockX(), getInstance().getBaseBlockY()),
            new Point(getInstance().getBaseBlockX() + 1, getInstance().getBaseBlockY()),
            new Point(getInstance().getBaseBlockX() + 2, getInstance().getBaseBlockY() - 1)
    ), 5),

    /*
     *  + + + + +
     */
    I_BLOCK(() -> List.of(
            new Point(getInstance().getBaseBlockX() + 2, getInstance().getBaseBlockY()),
            new Point(getInstance().getBaseBlockX(), getInstance().getBaseBlockY()),
            new Point(getInstance().getBaseBlockX() + 1, getInstance().getBaseBlockY()),
            new Point(getInstance().getBaseBlockX() + 3, getInstance().getBaseBlockY())
    ), 5),

    /*
        + +
        +
        +
    */
    J_BLOCK(() -> List.of(
            new Point(getInstance().getBaseBlockX(), getInstance().getBaseBlockY() - 1),
            new Point(getInstance().getBaseBlockX(), getInstance().getBaseBlockY()),
            new Point(getInstance().getBaseBlockX(), getInstance().getBaseBlockY() - 2),
            new Point(getInstance().getBaseBlockX() + 1, getInstance().getBaseBlockY() - 2)
    ), 5),

    /*
       + +
         +
         +
     */
    L_BLOCK(() -> List.of(
            new Point(getInstance().getBaseBlockX(), getInstance().getBaseBlockY() - 1),
            new Point(getInstance().getBaseBlockX(), getInstance().getBaseBlockY()),
            new Point(getInstance().getBaseBlockX(), getInstance().getBaseBlockY() - 2),
            new Point(getInstance().getBaseBlockX() - 1, getInstance().getBaseBlockY() - 2)
    ), 5),

    /*
      + +
      + +
    */
    O_BLOCK(() ->List.of(
            new Point(getInstance().getBaseBlockX(), getInstance().getBaseBlockY()),
            new Point(getInstance().getBaseBlockX() + 1, getInstance().getBaseBlockY()),
            new Point(getInstance().getBaseBlockX(), getInstance().getBaseBlockY() - 1),
            new Point(getInstance().getBaseBlockX() + 1, getInstance().getBaseBlockY() - 1)
    ), 5),

    /*
       +
         +
            +
     */
    DIAGONAL_BLOCK(() -> List.of(
            new Point(getInstance().getBaseBlockX(), getInstance().getBaseBlockY()),
            new Point(getInstance().getBaseBlockX() + 1, getInstance().getBaseBlockY() + 1),
            new Point(getInstance().getBaseBlockX() - 1, getInstance().getBaseBlockY() - 1)
    ), 3),

    /*
        +
     */
    DOT_BLOCK(() -> List.of(
            new Point(getInstance().getBaseBlockX(), getInstance().getBaseBlockY())
    ), 1)


    ;


    private final Supplier<List<Point>> points;
    private final int weight;

    BlockType(Supplier<List<Point>> points, int weight) {
        this.points = points;
        this.weight = weight;
    }

    public List<Point> getPoints() {
        return points.get();
    }

    public static BlockType getRandomType(List<BlockType> ignore) {
        List<BlockType> blocks = Arrays.stream(values()).filter(blockType -> !ignore.contains(blockType)).collect(Collectors.toList());
        Collections.shuffle(blocks);
        int totalWeight = blocks.stream().mapToInt(value -> value.weight).sum();

        Random random = new Random();
        int randomWeight = random.nextInt(totalWeight);

        int cumulativeWeight = 0;
        for (BlockType block : blocks) {
            cumulativeWeight += block.weight;
            if (randomWeight < cumulativeWeight) {
                return block;
            }
        }

        return null;
    }

}
