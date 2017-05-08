/**
 * TODO: Commented out because we dont have an evluation function.
 */

//package angush.game;
//
//import aima.core.search.adversarial.Game;
//import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// * Created by TJ on 30/4/17.
// */
//public class SliderAIPlayer extends IterativeDeepeningAlphaBetaSearch<GameBoard,Move,Endgame>{
//
//    public SliderAIPlayer(Game<GameBoard, Move, Endgame> game, double utilMin, double utilMax, int time) {
//        super(game, 0.0, 1.0, 1);
//    }
//
//    @Override
//    public ACTION makeDecision(STATE state) {
//        metrics = new Metrics();
//        StringBuffer logText = null;
//        PLAYER player = game.getPlayer(state);
//        List<ACTION> results = orderActions(state, game.getActions(state), player, 0);
//        timer.start();
//        currDepthLimit = 0;
//        do {
//            incrementDepthLimit();
//            if (logEnabled)
//                logText = new StringBuffer("depth " + currDepthLimit + ": ");
//            heuristicEvaluationUsed = false;
//            ActionStore<ACTION> newResults = new ActionStore<ACTION>();
//            for (ACTION action : results) {
//                double value = minValue(game.getResult(state, action), player, Double.NEGATIVE_INFINITY,
//                        Double.POSITIVE_INFINITY, 1);
//                if (timer.timeOutOccured())
//                    break; // exit from action loop
//                newResults.add(action, value);
//                if (logEnabled)
//                    logText.append(action + "->" + value + " ");
//            }
//            if (logEnabled)
//                System.out.println(logText);
//            if (newResults.size() > 0) {
//                results = newResults.actions;
//                if (!timer.timeOutOccured()) {
//                    if (hasSafeWinner(newResults.utilValues.get(0)))
//                        break; // exit from iterative deepening loop
//                    else if (newResults.size() > 1
//                            && isSignificantlyBetter(newResults.utilValues.get(0), newResults.utilValues.get(1)))
//                        break; // exit from iterative deepening loop
//                }
//            }
//        } while (!timer.timeOutOccured() && heuristicEvaluationUsed);
//        return results.get(0);
//    }
//
////    @Override
////    protected boolean isSignificantlyBetter(double newUtility, double utility) {
////        return newUtility - utility > (utilMax - utilMin) * 0.4;
////    }
////
////    @Override
////    protected boolean hasSafeWinner(double resultUtility) {
////        return Math.abs(resultUtility - (utilMin + utilMax) / 2) > 0.4
////                * utilMax - utilMin;
////    }
////
////    /**
////     * Orders actions with respect to the number of potential win positions
////     * which profit from the action.
////     */
////    public List<Move> orderActions(GameBoard state,
////                                      List<Move> actions, Endgame player, int depth) {
////        List<Move> result = actions;
////        if (depth == 0) {
////            List<ActionValuePair<Move>> actionEstimates = new ArrayList<ActionValuePair<Move>>(
////                    actions.size());
////            for (Move action : actions)
////                actionEstimates.add(ActionValuePair.createFor(action,
////                        state.calculateHeuristics(action,player)));
////            Collections.sort(actionEstimates);
////            result = new ArrayList<Move>();
////            for (ActionValuePair<Move> pair : actionEstimates)
////                result.add(pair.getAction());
////        }
////        return result;
////    }
//}
