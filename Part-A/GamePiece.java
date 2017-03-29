import java.util.ArrayList;

/*
 * A representation of a piece in a game of Slider
 * Written by Angus Huang 640386 (angush) and Tejas Cherukara 694985 (taniyan)
 */
public class GamePiece {
    private int row;
    private int col;
    private CellState type;
    private ArrayList<Move> moves;

    /**
     * @param row The row position.
     * @param col The column position.
     * @param type The type of the piece.
     */
    public GamePiece(int row, int col, CellState type){
        this.row = row;
        this.col = col;
        this.type = type;
    }

    /**
     * The row position.
     */
    public int getRow() {
        return row;
    }

    /**
     * The column position.
     */
    public int getCol() {
        return col;
    }

    /**
     * The type of the piece.
     */
    public CellState getType() {
        return type;
    }

    /**
     * The valid moves for the piece.
     */
    public ArrayList<Move> getMoves() {
        return moves;
    }

    /**
     * @param row The row position.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * @param col The column position.
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * @param type The type of the piece.
     */
    public void setType(CellState type) {
        this.type = type;
    }

    /**
     * @param moves The valid moves for the piece.
     */
    public void setMoves(ArrayList<Move> moves) {
        this.moves = moves;
    }
}
