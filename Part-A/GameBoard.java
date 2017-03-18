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
    private ArrayList<GamePiece> hPieces;
    private ArrayList<GamePiece> vPieces;

    /**
     * Creates a GameBoard from an input stream.
     * 
     * @param scanner The input stream to read from.
     * @return The GameBoard representation of the input.
     */
    public GameBoard(Scanner scanner) {
        // Get the dimension and hence the board size
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
                        hPieces.add(new GamePiece(i,j,CellState.HORIZONTAL,calculateMoves(i,j,CellState.HORIZONTAL)));
                        break;
                    case "V":
                        board[i][j] = CellState.VERTICAL;
                        // Add the x, y, CellState and calculate the valid moves of each vertical game piece
                        vPieces.add(new GamePiece(i,j,CellState.VERTICAL,calculateMoves(i,j,CellState.VERTICAL)));
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
            // The source space must be a piece
            source == CellState.FREE ||
            source == CellState.BLOCKED ||
            // Pieces are restricted from certain directions
            (source == CellState.HORIZONTAL && m.getDirection() == Direction.LEFT) ||
            (source == CellState.VERTICAL && m.getDirection() == Direction.DOWN) ||
            // The destination space must be free
            board[m.getDestRow()][m.getDestCol()] != CellState.FREE) {
            return false;
        }
        return true;
    }

    /**
     * Prints the number of legal moves for the horizontal player.
     */
    public void printNumLegalHMoves() {
        int count = 0;

        for(GamePiece piece:hPieces){
            count += piece.moves.size();
        }
        System.out.println(count);
    }

    /**
     * Prints the number of legal moves for the vertical player.
     */
    public void printNumLegalVMoves() {
        int count = 0;

        for(GamePiece piece:vPieces){
            count += piece.moves.size();
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
        } else {
            return new Direction[] {Direction.UP, Direction.RIGHT, Direction.LEFT};
        }
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
}
