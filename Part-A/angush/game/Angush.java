package angush.game;

import aima.core.search.adversarial.AdversarialSearch;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import aiproj.slider.Move;

import java.util.logging.Logger;


/**
 * A player for Slider.
 * Written by Angus Huang 640386 (angush) and Tejas Cherukara 694985 (taniyan)
 */
public class Angush implements aiproj.slider.SliderPlayer {

    private SliderGame game;
    private AdversarialSearch<GameBoard, angush.game.Move> strategy;
    private boolean firstMove;


    /**
     * Initialise the player.
     * 
     * @param dimension The dimension of the board.
     * @param board The board state.
     * @param player The side of the player.
     */
    @Override
    public void init(int dimension, String board, char player) {
        this.game = new SliderGame(dimension, board, player);
        this.strategy = new SliderAIPlayer(game, -SliderGame.INFINITY, SliderGame.INFINITY, 250);
        this.firstMove = true;
    }

    /**
     * Get the opponents move given by referee and update it on out board if not null.
     *
     * @param move A Move object representing the previous move made by the
     *             opponent, which may be null (indicating a pass). Also, before the first
     */
    @Override
    public void update(Move move) {
        angush.game.Move convertedMove = null;
        if (move != null) {
            convertedMove = angush.game.Move.fromAiProjMove(move);
            game.gameBoard.makeMove(convertedMove, game.gameBoard.getOpponent());
            game.gameBoard.setTurn(game.gameBoard.determineTurn());
        } else {
            // If the first move is null, then it must be the "move" that starts the game
            if (!firstMove) {
                game.gameBoard.setTurn(game.gameBoard.determineTurn());
            }
        }
        firstMove = false;
    }

    /**
     * Get our strategy class to decide on the optimal move and make that move.
     * If no move is returned, then null is produced.
     *
     * @return The move to make.
     */
    @Override
    public Move move() {
        angush.game.Move tmp = strategy.makeDecision(game.gameBoard);
        if (tmp != null) {
            game.gameBoard.makeMove(tmp, game.gameBoard.getTurn());
            game.gameBoard.setTurn(game.gameBoard.determineTurn());
            Move tmpMove = angush.game.Move.toAiProjMove(tmp);
            return tmpMove;
        }
        game.gameBoard.setTurn(game.gameBoard.determineTurn());
        return null;
    }
}
