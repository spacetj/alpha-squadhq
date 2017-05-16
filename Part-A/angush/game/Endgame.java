package angush.game;

/**
 * The possible states of turns.
 * Written by Angus Huang 640386 (angush) and Tejas Cherukara 694985 (taniyan)
 */
public enum Endgame
{
    HORIZONTAL,
    VERTICAL,
    TIE,
    HORIZONTAL_WIN,
    VERTICAL_WIN,
    NONE;

    @Override
    public String toString() {
        switch(this) {
            case HORIZONTAL:
                return "H";
            case VERTICAL:
                return "V";
            case TIE:
                return "T";
            case NONE:
                return "-";
            case HORIZONTAL_WIN:
                return "H Won";
            case VERTICAL_WIN:
                return "V Won";
            default:
                throw new IllegalArgumentException();
        }
    }
}