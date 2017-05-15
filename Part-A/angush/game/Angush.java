package angush.game;
import aima.core.search.adversarial.AdversarialSearch;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import aiproj.slider.Move;
import java.util.logging.Logger;


/**
 * Created by TJ on 20/4/17.
 */
public class Angush implements aiproj.slider.SliderPlayer {

    private SliderGame game;
    private AdversarialSearch<GameBoard, angush.game.Move> strategy;



    @Override
    public void init(int dimension, String board, char player) {
        this.game = new SliderGame(dimension,board,player);
        this.strategy = new SliderAIPlayer(game,minMaxUtil()[1],minMaxUtil()[0],1);
    }

    /**
     * Get the opponents move given by referee and update it on out board if not null.
     * @param move A Move object representing the previous move made by the
     * opponent, which may be null (indicating a pass). Also, before the first
     */
    @Override
    public void update(Move move) {
        angush.game.Move convertedMove = null;
        if (move != null) {
            convertedMove = toConvertMove(move);
            game.gameBoard.makeMove(convertedMove,game.getPlayers()[1]);
        }
    }

    /**
     * Get our strategy class to decide on the optimal move and make that move.
     * If no move is returned, then null is produced.
     * @return
     */

    @Override
    public Move move() {
        angush.game.Move tmp = strategy.makeDecision(game.gameBoard);
        if (tmp == null) {
            System.out.println("MP.move.null");
            return null;
        }
        game.gameBoard.makeMove(tmp,game.gameBoard.getTurn());
        Move tmpMove = fromConvertMove(tmp);
        return tmpMove;
    }

    /**
     * TODO: Come back and make sure this is functioning properly
     * @return
     */
    public int[] minMaxUtil(){
        if(game.gameBoard.getPlayer() == Endgame.HORIZONTAL) return new int[]{0, 1};
        else return new int[]{1, 0};
    }

    /**
     * Function to convert from aiproj.slider.Move -> our local Move class
     * @param from
     * @return
     */

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

    /**
     * Function to convert from out local move class to aiproj.slider.Move before sending to referee
     * @param from
     * @return
     */

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
