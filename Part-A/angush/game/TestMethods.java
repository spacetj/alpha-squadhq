package angush.game;

import java.util.ArrayList;

/**
 * TODO: Delete this class
 *
 * Used for quick testing because I couldnt be bothered to unit test.
 */
public class TestMethods {

    public static String stringBoard = "+ + + H\nH + B +\nH B + +\n+ V V V\n";

    public static void main(String[] args) {
        GameBoard board = new GameBoard(4, stringBoard, 'H');
        System.out.println(board.isValidMove(new Move(3, 3, Direction.RIGHT)));

    }

}
