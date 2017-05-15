package random.player;

import aiproj.slider.Move;
import angush.game.Direction;
import angush.game.GameBoard;
import angush.game.GamePiece;

import java.util.ArrayList;

/**
 * Created by TJ on 1/5/17.
 */
public class RandomAngush implements aiproj.slider.SliderPlayer {

    private GameBoard gameBoard;
    private ArrayList<GamePiece> myPieces;


    //TODO For test purposes
//    static int testDimension = 4;
//    static String testBoard = "H + + +\n" +
//            "H + B +\n" +
//            "H B + +\n" +
//            "+ V V V";
//    static Move testMove = new Move(0, 2, Move.Direction.UP);
//    static char testPlayer = 'H';

    @Override
    public void init(int dimension, String board, char player) {
        this.gameBoard = new GameBoard(dimension, board, player);
        this.myPieces = gameBoard.getMyPieces(gameBoard.getPlayer());
    }

    @Override
    public void update(Move move) {
        if (move != null) {
            angush.game.Move convertedMove = toConvertMove(move);
            gameBoard.makeMove(convertedMove, gameBoard.getPlayer());
        }
        gameBoard.setTurn(gameBoard.determineTurn());
    }

    @Override
    public Move move() {
        for (GamePiece piece: myPieces) {
            if(!piece.isCrossedFinishLine() && piece.getMoves().size() > 0){
                Move tmp = fromConvertMove(piece.getMoves().get(0));
                gameBoard.makeMove(piece.getMoves().get(0),gameBoard.getTurn());
                gameBoard.setTurn(gameBoard.determineTurn());

                return tmp;
            }
        }
        gameBoard.setTurn(gameBoard.determineTurn());

        return null;
    }

    public angush.game.Move toConvertMove(Move from){
        angush.game.Move to;
        Direction convertedDirection;
        if(from.d == Move.Direction.DOWN) convertedDirection = Direction.DOWN;
        else if (from.d == Move.Direction.LEFT) convertedDirection = Direction.LEFT;
        else if(from.d == Move.Direction.RIGHT) convertedDirection = Direction.RIGHT;
        else convertedDirection = Direction.UP;
        to = new angush.game.Move(from.j,from.i,convertedDirection);
        return to;
    }

    public Move fromConvertMove(angush.game.Move from){
        Move to;
        Move.Direction convertedDirection;
        if(from.getDirection() == Direction.DOWN) convertedDirection = Move.Direction.DOWN;
        else if (from.getDirection() == Direction.LEFT) convertedDirection = Move.Direction.LEFT;
        else if(from.getDirection() == Direction.RIGHT) convertedDirection = Move.Direction.RIGHT;
        else convertedDirection = Move.Direction.UP;

        to = new Move(from.getSourceCol(),from.getSourceRow(),convertedDirection);
        return to;
    }
}
