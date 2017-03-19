import com.oracle.tools.packager.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;


/*
 * A representation of a game board of Slider
 * Written by Angus Huang 640386 (angush) and Tejas Cherukara (taniyan)
 */
public class GameBoard
{
    private CellState[][] board;
    private int dimension;
    private ArrayList<GamePiece> hPieces = new ArrayList<GamePiece>();
    private ArrayList<GamePiece> vPieces = new ArrayList<GamePiece>();;


    /**
     * Creates a GameBoard from an input stream.
     * 
     * @param scanner The input stream to read from.
     * @return The GameBoard representation of the input.
     */
    public GameBoard(Scanner scanner) {
        // Get the dimension and hence the board size
        try {
            dimension = scanner.nextInt();
            scanner.nextLine();
            board = new CellState[dimension][dimension];


            // Fill the board
            for (int i = dimension - 1; i >= 0; i--) {
                String[] pieces = scanner.nextLine().split("\\s+");
                for (int j = 0; j < dimension; j++) {
                    switch (pieces[j]) {
                        case "H":
                            board[i][j] = CellState.HORIZONTAL;
                            // Add the x, y, CellState and calculate the valid moves of each horizontal game piece
                            hPieces.add(new GamePiece(i, j, CellState.HORIZONTAL));
                            break;
                        case "V":
                            board[i][j] = CellState.VERTICAL;
                            // Add the x, y, CellState and calculate the valid moves of each vertical game piece
                            vPieces.add(new GamePiece(i, j, CellState.VERTICAL));
                            break;
                        case "+":
                            board[i][j] = CellState.FREE;
                            break;
                        case "B":
                            board[i][j] = CellState.BLOCKED;
                            break;
                    }
                }
            }


            updateMoves();

        }
        catch (Exception e){
            new Exception("Input not in the right format "+e.getMessage());
        }
    }

    /**
     * Determines whether a move is valid based on the board.
     * 
     * @param m The tentative move.
     * @return Whether the move is valid.
     */
    public boolean isValidMove(Move m) {
        CellState source = board[m.getSourceRow()][m.getSourceCol()];

        // Positions must be within the board
        if (m.getSourceRow() < 0 || m.getSourceRow() >= dimension ||
                m.getSourceCol() < 0 || m.getSourceCol() >= dimension ||
                m.getDestRow() < 0 || m.getDestRow() >= dimension ||
                m.getDestCol() < 0 || m.getDestCol() >= dimension ||
                // Source must be a piece
                source == CellState.FREE || source == CellState.BLOCKED ||
                // Horizontal piece should not go left
                source == CellState.HORIZONTAL && m.getDirection() == Direction.LEFT ||
                // Vertical piece should not go down
            source == CellState.VERTICAL && m.getDirection() == Direction.DOWN
                // The destination game space must be free
                || board[m.getDestRow()][m.getDestCol()] != CellState.FREE){
            return false;
        }

        return true;
    }

    /**
     * Prints the number of legal moves for the horizontal player.
     */
    public void printNumLegalHMoves() {

        int count = 0;

        if(hPieces.size() > 0){
            for (GamePiece piece : hPieces) {
                count += piece.getMoves().size();
            }
        }
        System.out.println(count);

    }

    /**
     * Prints the number of legal moves for the vertical player.
     */
    public void printNumLegalVMoves() {
        int count = 0;

        if(vPieces.size() > 0){
            for(GamePiece piece:vPieces){
                count += piece.getMoves().size();

            }
        }

        System.out.println(count);
    }

    /**
     * Returns a list of allowed direction that a piece can move to.
     * Horizontal piece = Up, right and down
     * Vertical piece = Up, right, left
     * @param type CellState of the Piece
     * @return
     */
    public Direction[] allowedDirections(CellState type){
        if (type == CellState.HORIZONTAL) {
            return new Direction[] {Direction.UP, Direction.RIGHT, Direction.DOWN};
        } else if (type == CellState.VERTICAL) {
            return new Direction[] {Direction.UP, Direction.RIGHT, Direction.LEFT};
        }
        return new Direction[]{};
    }

    /**
     * Returns an arraylist of the valid moves that a given piece / cellstate can make
     * @param x
     * @param y
     * @param type
     * @return
     */
    public ArrayList<Move> calculateMoves(int x, int y, CellState type){
        ArrayList<Move> moves = new ArrayList<Move>();
        // For each allowed direction for the piece
        for (Direction dir: allowedDirections(type)){
            // Create a temporary move and check if valid.
            Move tmp = new Move(x, y, dir);
            if(isValidMove(tmp)){
                moves.add(tmp);
            }
        }
        return moves;
    }

    /**
     *
     */
    public void updateMoves(){
        for (GamePiece piece:hPieces){
            piece.setMoves(calculateMoves(piece.getX(), piece.getY(),piece.getType()));
        }

        for (GamePiece piece:vPieces){
            piece.setMoves(calculateMoves(piece.getX(), piece.getY(),piece.getType()));
        }
    }
}
