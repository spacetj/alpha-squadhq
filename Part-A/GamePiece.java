import java.util.ArrayList;

/**
 * Created by TJ on 19/3/17.
 */
public class GamePiece {

    private int row;
    private int col;
    private CellState type;
    private ArrayList<Move> moves;

    public GamePiece(int row, int col, CellState type){
        this.row = row;
        this.col = col;
        this.type = type;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public CellState getType() {
        return type;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setType(CellState type) {
        this.type = type;
    }

    public void setMoves(ArrayList<Move> moves) {
        this.moves = moves;
    }
}
