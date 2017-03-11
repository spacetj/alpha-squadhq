import java.util.Scanner;

/*
 * A representation of a game board of Slider
 * Written by Angus Huang 640386 (angush) and TODO
 */
public class GameBoard
{
    private CellState[][] board;
    private int dimension;

    /**
     * Creates a GameBoard from an input stream.
     * 
     * @param s The input stream to read from.
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
                        break;
                    case "V":
                        board[i][j] = CellState.VERTICAL;
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

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (board[i][j] == CellState.HORIZONTAL) {
                    if (isValidMove(new Move(i, j, Direction.UP))) {
                        count++;
                    }
                    if (isValidMove(new Move(i, j, Direction.DOWN))) {
                        count++;
                    }
                    if (isValidMove(new Move(i, j, Direction.RIGHT))) {
                        count++;
                    }
                }
            }
        }
        System.out.println(count);
    }

    /**
     * Prints the number of legal moves for the vertical player.
     */
    public void printNumLegalVMoves() {
        int count = 0;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (board[i][j] == CellState.VERTICAL) {
                    if (isValidMove(new Move(i, j, Direction.UP))) {
                        count++;
                    }
                    if (isValidMove(new Move(i, j, Direction.LEFT))) {
                        count++;
                    }
                    if (isValidMove(new Move(i, j, Direction.RIGHT))) {
                        count++;
                    }
                }
            }
        }
        System.out.println(count);
    }
}
