import java.util.ArrayList;
import java.util.List;

/**
 * Created by TJ on 19/3/17.
 */
public class GamePiece {

    private int x;
    private int y;
    private CellState type;
    private ArrayList<Move> moves;

    public GamePiece(int x, int y, CellState type){
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CellState getType() {
        return type;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setType(CellState type) {
        this.type = type;
    }

    public void setMoves(ArrayList<Move> moves) {
        this.moves = moves;
    }
}
