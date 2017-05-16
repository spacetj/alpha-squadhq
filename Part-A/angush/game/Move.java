package angush.game;

/*
 * A move in a game of Slider
 * Written by Angus Huang 640386 (angush) and Tejas Cherukara 694985 (taniyan)
 */
public class Move implements Cloneable
{
    private int sourceRow;
    private int sourceCol;
    private int destRow;
    private int destCol;
    private Direction direction;

    /**
     * Constructs a move based on a position and a direction.
     * 
     * @param sourceRow The starting row.
     * @param sourceCol The starting column.
     * @param direction The direction to move.
     */
    public Move(int sourceRow, int sourceCol, Direction direction) {
        this.sourceRow = sourceRow;
        this.sourceCol = sourceCol;
        this.direction = direction;

        // Generate the destination position
        switch (direction) {
            case UP:
                this.destRow = sourceRow + 1;
                this.destCol = sourceCol;
                break;
            case DOWN:
                this.destRow = sourceRow - 1;
                this.destCol = sourceCol;
                break;
            case LEFT:
                this.destRow = sourceRow;
                this.destCol = sourceCol - 1;
                break;
            case RIGHT:
                this.destRow = sourceRow;
                this.destCol = sourceCol + 1;
                break;
        }
    }


    /**
     * The starting row.
     */
    public int getSourceRow() {
        return sourceRow;
    }

    /**
     * The starting column.
     */
    public int getSourceCol() {
        return sourceCol;
    }

    /**
     * The new row.
     */
    public int getDestRow() {
        return destRow;
    }

    /**
     * The new column.
     */
    public int getDestCol() {
        return destCol;
    }

    /**
     * The direction of the move.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Convert from aiproj.slider.Move to this move representation.
     *
     * @param from The move to convert.
     * @return The converted move.
     */
    public static Move fromAiProjMove(aiproj.slider.Move from) {
        Move to;
        Direction convertedDirection;
        if (from.d == aiproj.slider.Move.Direction.DOWN) convertedDirection = Direction.DOWN;
        else if (from.d == aiproj.slider.Move.Direction.LEFT) convertedDirection = Direction.LEFT;
        else if (from.d == aiproj.slider.Move.Direction.RIGHT) convertedDirection = Direction.RIGHT;
        else convertedDirection = Direction.UP;
        to = new Move(from.j, from.i, convertedDirection);
        return to;
    }

    /**
     * Convert from this move representation to aiproj.slider.Move
     *
     * @param from The move to convert.
     * @return The converted move.
     */
    public static aiproj.slider.Move toAiProjMove(Move from) {
        aiproj.slider.Move to;
        aiproj.slider.Move.Direction convertedDirection;
        if (from.getDirection() == Direction.DOWN) convertedDirection = aiproj.slider.Move.Direction.DOWN;
        else if (from.getDirection() == Direction.LEFT) convertedDirection = aiproj.slider.Move.Direction.LEFT;
        else if (from.getDirection() == Direction.RIGHT) convertedDirection = aiproj.slider.Move.Direction.RIGHT;
        else convertedDirection = aiproj.slider.Move.Direction.UP;
        to = new aiproj.slider.Move(from.getSourceCol(), from.getSourceRow(), convertedDirection);
        return to;
    }


    @Override
    public String toString() {
        return "Move{" +
                "sourceRow=" + sourceRow +
                ", sourceCol=" + sourceCol +
                ", destRow=" + destRow +
                ", destCol=" + destCol +
                ", direction=" + direction +
                '}';
    }

    @Override
    public Move clone() {
        Move result = null;
        try {
            result = (Move) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
