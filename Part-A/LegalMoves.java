import java.util.*;

/*
 * Determines the number of legal moves available in a game of Slider
 * Written by Angus Huang 640386 (angush) and TODO
 */
public class LegalMoves
{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameBoard board = new GameBoard(scanner);
        scanner.close();
        board.printNumLegalHMoves();
        board.printNumLegalVMoves();
    }
}
