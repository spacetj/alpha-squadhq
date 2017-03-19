/*
 * A move in a game of Slider
 * Written by Angus Huang 640386 (angush) and Tejas Cherukara (taniyan)
 */
public class Move 
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
}
