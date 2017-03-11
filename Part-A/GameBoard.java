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
}
