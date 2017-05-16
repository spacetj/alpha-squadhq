package angush.game;

import aima.core.search.adversarial.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of Slider required by AIMA
 * Written by Angus Huang 640386 (angush) and Tejas Cherukara 694985 (taniyan)
 */
public class SliderGame implements Game<GameBoard, Move, TurnState> {
    protected GameBoard gameBoard;
    public static final int INFINITY = 100;

    public SliderGame(int dimension, String board, char player) {
        gameBoard = new GameBoard(dimension, board, player);
    }


    @Override
    public GameBoard getInitialState() {
        return gameBoard;
    }

    @Override
    public TurnState[] getPlayers() {
        return new TurnState[]{gameBoard.getPlayer(), gameBoard.getOpponent()};
    }

    @Override
    public TurnState getPlayer(GameBoard gameBoard) {
        return gameBoard.getTurn();
    }

    @Override
    public List<angush.game.Move> getActions(GameBoard gameBoard) {
        return gameBoard.getMoves(gameBoard.getPlayer());
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
    public double getUtility(GameBoard gameBoard, TurnState player) {
        return gameBoard.getUtility();
    }

    /**
     * Determines the opponent to a player.
     * 
     * @param player The player's side.
     * @return The opponent's side.
     */
    public static TurnState determineOpponent(TurnState player) {
        if (player == TurnState.HORIZONTAL) {
            return TurnState.VERTICAL;
        } else {
            return TurnState.HORIZONTAL;
        }
    }
}
