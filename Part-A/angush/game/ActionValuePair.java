package angush.game;

/**
 * Pair of Actions and their utility value
 * Written by Angus Huang 640386 (angush) and Tejas Cherukara 694985 (taniyan)
 */
public class ActionValuePair<ACTION> implements Comparable<ActionValuePair<ACTION>> {
    private ACTION action;
    private double value;

    public static <ACTION> ActionValuePair<ACTION> createFor(ACTION action, double utility) {
        return new ActionValuePair<ACTION>(action, utility);
    }

    public ActionValuePair(ACTION action, double utility) {
        this.action = action;
        this.value = utility;
    }

    public ACTION getAction() {
        return action;
    }

    public double getValue() {
        return value;
    }

    @Override
    public int compareTo(ActionValuePair<ACTION> pair) {
        if (value < pair.value) {
            return 1;
        } else if (value > pair.value) {
            return -1;
        } else {
            return 0;
        }
    }
}
