package angush.game;

import aima.core.agent.Action;
import aima.core.agent.State;
import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import angush.game.TurnState;
import angush.game.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.lang.reflect.Field;

/**
 * A player for Slider using iterative deepening alpha beta search.
 * Written by Angus Huang 640386 (angush) and Tejas Cherukara 694985 (taniyan)
 */
public class SliderAIPlayer extends IterativeDeepeningAlphaBetaSearch<GameBoard, Move, TurnState> {

    public SliderAIPlayer(Game<GameBoard, Move, TurnState> game, double utilMin, double utilMax, int time) {
        // Default to 1 second if time modification fails.
        super(game, utilMin, utilMax, 1);

        /*
         * The AIMA class has a timer which cuts off search after a time limit.
         * Unfortunately, the default constructor uses integer seconds, but the actual representation
         * uses milliseconds. To make matters worse, the class declares both the field as well as the
         * timer class private and provides no interface to modify them. Due to the time constraints placed upon
         * our program, as well as a requirement that we do not submit the AIMA library (with modifications
         * fitting our program), we are forced to use reflection to change the timer.
         */
        try {
            IterativeDeepeningAlphaBetaSearch search = this;
            Class<?> clazz = getClass().getSuperclass();
            Field f = clazz.getDeclaredField("timer");
            f.setAccessible(true);
            Class<?> timer = f.get(search).getClass();
            Field duration = timer.getDeclaredField("duration");
            duration.setAccessible(true);
            duration.set(f.get(search), time);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("Can't change the timer.");
        }
    }

    /**
     * Orders actions with respect to the number of potential win positions
     * which profit from the action.
     */
    @Override
    public List<Move> orderActions(GameBoard state, List<Move> actions, TurnState player, int depth) {
        List<Move> result;
        List<ActionValuePair<Move>> actionEstimates = new ArrayList<ActionValuePair<Move>>(
                actions.size());
        for (Move action : actions)
            actionEstimates.add(ActionValuePair.createFor(action,
                    state.calculateHeuristics(action, player)));
        Collections.sort(actionEstimates);
        result = new ArrayList<Move>();
        for (ActionValuePair<Move> pair : actionEstimates) {
            result.add(pair.getAction());
        }
        if (!result.isEmpty()) {
            return result;
        } else {
            return actions;
        }
    }

    /**
     * Calculates the utility value for a board state
     */
    @Override
    protected double eval(GameBoard state, TurnState player) {
        super.eval(state, player);
        if (game.isTerminal(state)) {
            return game.getUtility(state, player);
        } else {
            return state.calculateHeuristics(player);
        }
    }
}
