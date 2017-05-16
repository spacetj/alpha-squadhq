package angush.game;

import java.util.ArrayList;

/*
 * A representation of a piece in a game of Slider
 * Written by Angus Huang 640386 (angush) and Tejas Cherukara 694985 (taniyan)
 */
public class GamePiece implements Cloneable {
    private int row;
    private int col;
    private CellState type;
    private boolean crossedFinishLine = false;
    private ArrayList<Move> moves;
    private int dimension;

    /**
     * @param row The row position.
     * @param col The column position.
     * @param type The type of the piece.
     */
    public GamePiece(int row, int col, CellState type, int dimension){
        this.row = row;
        this.col = col;
        this.type = type;
        this.dimension = dimension;
    }

    public GamePiece(GamePiece piece) {
        this.row = piece.getRow();
        this.col = piece.getCol();
        this.type = piece.getType();
        this.dimension = piece.getDimension();
        this.moves = piece.getMoves();
        this.crossedFinishLine = piece.isCrossedFinishLine();
    }

    public int getDimension() {
        return dimension;
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
        if(this.row == dimension && this.type == CellState.VERTICAL)
            crossedFinishLine = true;

    }

    /**
     * @param col The column position.
     */
    public void setCol(int col) {
        this.col = col;
        if(this.col == dimension && this.type == CellState.HORIZONTAL)
            crossedFinishLine = true;
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

    /**
     * Check if the piece has crossed the finish line.
     * @return
     */
    public boolean isCrossedFinishLine() {
        return crossedFinishLine;
    }

    @Override
    public GamePiece clone() {
        GamePiece result = null;
        try {
            result = (GamePiece) super.clone();
            ArrayList<Move> cloningMoves = new ArrayList<>();
            for (int i=0; i<getMoves().size(); i++) {
                cloningMoves.add(result.getMoves().get(i).clone());
            }
            result.setMoves(cloningMoves);
            return result;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return result;
    }
}