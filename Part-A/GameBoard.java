import java.util.ArrayList;
import java.util.Scanner;

/*
 * A representation of a game board of Slider
 * Written by Angus Huang 640386 (angush) and Tejas Cherukara 694985 (taniyan)
 */
public class GameBoard
{
    private CellState[][] board;
    private int dimension;
    private ArrayList<GamePiece> hPieces = new ArrayList<GamePiece>();
    private ArrayList<GamePiece> vPieces = new ArrayList<GamePiece>();

    /**
     * Creates a GameBoard from a scanner.
     *
     * @param scanner The scanner to read from.
     * @return The GameBoard representation of the input.
     */
    public GameBoard(Scanner scanner) {
        // Get the dimension and hence the board size
        try {
            dimension = scanner.nextInt();
        } catch (Exception e) {
            System.err.println("First input needs to be a number.");
            System.exit(1);
        }
        scanner.nextLine();

        // Fill the board
        board = new CellState[dimension][dimension];
        for (int i = dimension - 1; i >= 0; i--) {
            String[] pieces = scanner.nextLine().split("\\s+");
            for (int j = 0; j < dimension; j++) {
                try {
                    switch (pieces[j]) {
                        case "H":
                            board[i][j] = CellState.HORIZONTAL;
                            hPieces.add(new GamePiece(i, j, CellState.HORIZONTAL));
                            break;
                        case "V":
                            board[i][j] = CellState.VERTICAL;
                            vPieces.add(new GamePiece(i, j, CellState.VERTICAL));
                            break;
                        case "+":
                            board[i][j] = CellState.FREE;
                            break;
                        case "B":
                            board[i][j] = CellState.BLOCKED;
                            break;
                        default:
                            System.err.println("Unrecognised input character.");
                            System.exit(2);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Not enough inputs.");
                    System.exit(3);
                }
            }
        }

        // Generate the moves for each piece
        updateMoves();
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
            || board[m.getDestRow()][m.getDestCol()] != CellState.FREE) {
            return false;
        }
        return true;
    }

    /**
     * Prints the number of legal moves for the horizontal player.
     */
    public void printNumLegalHMoves() {
        int count = 0;

        if (hPieces.size() > 0) {
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

        if (vPieces.size() > 0) {
            for (GamePiece piece : vPieces) {
                count += piece.getMoves().size();

            }
        }
        System.out.println(count);
    }

    /**
     * Generates the allowed directions that a piece can move to.
     * @param type The type of the piece to check.
     * @return The directions that the piece can move to.
     */
    public Direction[] allowedDirections(CellState type) {
        if (type == CellState.HORIZONTAL) {
            return new Direction[] {Direction.UP, Direction.RIGHT, Direction.DOWN};
        } else if (type == CellState.VERTICAL) {
            return new Direction[] {Direction.UP, Direction.RIGHT, Direction.LEFT};
        }
        return new Direction[]{};
    }

    /**
     * Generates the valid moves that a piece can make.
     * @param row The row position of the piece.
     * @param col The column position of the piece.
     * @return The moves that a piece can make.
     */
    public ArrayList<Move> calculateMoves(int row, int col){
        ArrayList<Move> moves = new ArrayList<Move>();

        // Create potentially valid moves and then check them.
        for (Direction dir : allowedDirections(board[row][col])) {
            Move tmp = new Move(row, col, dir);
            if(isValidMove(tmp)){
                moves.add(tmp);
            }
        }
        return moves;
    }

    /**
     * Updates the pieces with their valid moves.
     */
    public void updateMoves() {
        for (GamePiece piece : hPieces) {
            piece.setMoves(calculateMoves(piece.getRow(), piece.getCol()));
        }

        for (GamePiece piece : vPieces) {
            piece.setMoves(calculateMoves(piece.getRow(), piece.getCol()));
        }
    }
}
