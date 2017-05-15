package angush.game;

import aima.core.search.adversarial.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TJ on 3/5/17.
 */
public class SliderGame implements Game<GameBoard, Move,Endgame> {
    protected GameBoard gameBoard;

    public SliderGame(int dimension, String board, char player) {
        gameBoard = new GameBoard(dimension,board,player);
    }


    @Override
    public GameBoard getInitialState() {
        return gameBoard;
    }

    @Override
    public Endgame[] getPlayers() {
        return new Endgame[] {gameBoard.getPlayer(),gameBoard.getOpponent()};
    }

    @Override
    public Endgame getPlayer(GameBoard gameBoard) {
        return gameBoard.getTurn();
    }

    @Override
    public List<angush.game.Move> getActions(GameBoard gameBoard) { return gameBoard.getMoves(gameBoard.getPlayer());
    }

    @Override
    public GameBoard getResult(GameBoard gameBoard, angush.game.Move move) {
        GameBoard result = gameBoard.clone();
        result.makeMove(move, result.getTurn());
        return result;
    }

    @Override
    public boolean isTerminal(GameBoard gameBoard) {
        return (gameBoard.isGameOver());
    }

    @Override
    public double getUtility(GameBoard gameBoard, Endgame player) {
        return gameBoard.getUtility();
    }
}
