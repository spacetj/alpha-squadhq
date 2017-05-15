/**
 * TODO: Commented out because we dont have an evluation function.
 */

package angush.game;

import aima.core.agent.Action;
import aima.core.agent.State;
import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by TJ on 30/4/17.
 */
public class SliderAIPlayer extends IterativeDeepeningAlphaBetaSearch<GameBoard,Move,Endgame>{

    public SliderAIPlayer(Game<GameBoard, Move, Endgame> game, double utilMin, double utilMax, int time) {
        super(game, utilMin, utilMax, time);
    }

    /**
     * Orders actions with respect to the number of potential win positions
     * which profit from the action.
     */
    @Override
    public List<Move> orderActions(GameBoard state, List<Move> actions, Endgame player, int depth) {
        List<Move> result = actions;
        if (depth == 0) {
            List<ActionValuePair<Move>> actionEstimates = new ArrayList<ActionValuePair<Move>>(
                    actions.size());
            for (Move action : actions)
                actionEstimates.add(ActionValuePair.createFor(action,
                        state.calculateHeuristics(action,player)));
            Collections.sort(actionEstimates);
            result = new ArrayList<Move>();
            for (ActionValuePair<Move> pair : actionEstimates) {
                System.out.println(pair.getAction()+ " | Heuristic = "+pair.getValue());
                result.add(pair.getAction());
            }
        }
        if(result != null) return result;
        else return actions;
    }
}
