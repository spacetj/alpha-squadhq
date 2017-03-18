import java.util.ArrayList;
import java.util.List;

/**
 * Created by TJ on 19/3/17.
 */
public class GamePiece {

    public int x;
    public int y;
    public CellState type;
    public ArrayList<Move> moves;

    public GamePiece(int x, int y, CellState type, ArrayList<Move> moves){
        this.x = x;
        this.y = y;
        this.type = type;
        this.moves = moves;
    }

}
