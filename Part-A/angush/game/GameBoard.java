package angush.game;

import aima.core.search.adversarial.Game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import javafx.util.Pair;


/*
 * A representation of a game board of Slider
 * Written by Angus Huang 640386 (angush) and Tejas Cherukara 694985 (taniyan)
 */
public class GameBoard implements Cloneable {
    private CellState[][] board;
    private Endgame player;
    private Endgame opponent;
    private int dimension;
    private ArrayList<GamePiece> hPieces = new ArrayList<GamePiece>();
    private ArrayList<GamePiece> vPieces = new ArrayList<GamePiece>();
    private static int NUM_PIECES = 0;
    private static final int BLOCK_WEIGHT = 10;


    private Endgame turn;
    private double utility; // 0 is for horizontal, 1 is for vertical, 0.5 is for draw.

    /**
     * Creates a angush.game.GameBoard from a scanner.
     *
     * @return The angush.game.GameBoard representation of the input.
     */
    public GameBoard(int dimension, String board, char player) {
        // Get the dimension and hence the board size
        this.dimension = dimension;
        this.utility = -1;
        NUM_PIECES = dimension - 1;
        String[] stringBoard = board.split("\\n");
        if (player == 'H') {
            this.player = Endgame.HORIZONTAL;
            this.opponent = Endgame.VERTICAL;
            this.turn = this.player;
        } else {
            this.player = Endgame.VERTICAL;
            this.opponent = Endgame.HORIZONTAL;
            this.turn = this.opponent;
        }

        this.board = new CellState[dimension][dimension];
        for (int i = dimension - 1, k = 0; i >= 0 && k < this.dimension; i--, k++) {
            String[] pieces = stringBoard[k].split("\\s+");
            for (int j = 0; j < dimension; j++) {
                try {
                    switch (pieces[j]) {
                        case "H":
                            this.board[i][j] = CellState.HORIZONTAL;
                            hPieces.add(new GamePiece(i, j, CellState.HORIZONTAL, dimension));
                            break;
                        case "V":
                            this.board[i][j] = CellState.VERTICAL;
                            vPieces.add(new GamePiece(i, j, CellState.VERTICAL, dimension));
                            break;
                        case "+":
                            this.board[i][j] = CellState.FREE;
                            break;
                        case "B":
                            this.board[i][j] = CellState.BLOCKED;
                            break;
                        default:
                            System.err.println("Unrecognised input character.");
                            System.exit(2);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Not enough inputs.");
                    System.exit(3);
                }
            }
        }

        // Generate the moves for each piece
        updateMoves();
    }


    /**
     * Determines whether a move is valid based on the board.
     *
     * @param m The tentative move.
     * @return Whether the move is valid.
     */
    public boolean isValidMove(Move m) {
        CellState source = board[m.getSourceRow()][m.getSourceCol()];

        if ((source == CellState.HORIZONTAL && m.getDestCol() == dimension) ||
                (source == CellState.VERTICAL && m.getDestRow() == dimension)) {
            return true;
        }
        // Positions must be within the board
        else if (m.getSourceRow() < 0 || m.getSourceRow() >= dimension ||
                m.getSourceCol() < 0 || m.getSourceCol() >= dimension ||
                m.getDestRow() < 0 || m.getDestRow() >= dimension ||
                m.getDestCol() < 0 || m.getDestCol() >= dimension ||
                // Source must be a piece
                source == CellState.FREE || source == CellState.BLOCKED ||
                // Horizontal piece should not go left
                source == CellState.HORIZONTAL && m.getDirection() == Direction.LEFT ||
                // Vertical piece should not go down
                source == CellState.VERTICAL && m.getDirection() == Direction.DOWN
                // The destination game space must be free
                || board[m.getDestRow()][m.getDestCol()] != CellState.FREE) {
            return false;
        }
        return true;
    }

    /**
     * Prints the number of legal moves for the horizontal player.
     */
    public int printNumLegalHMoves() {
        int count = 0;

        if (hPieces.size() > 0) {
            for (GamePiece piece : hPieces) {
                count += piece.getMoves().size();
            }
        }
        return count;
    }

    /**
     * Prints the number of legal moves for the vertical player.
     */
    public int printNumLegalVMoves() {
        int count = 0;

        if (vPieces.size() > 0) {
            for (GamePiece piece : vPieces) {
                count += piece.getMoves().size();

            }
        }
        return count;
    }

    /**
     * Generates the allowed directions that a piece can move to.
     *
     * @param type The type of the piece to check.
     * @return The directions that the piece can move to.
     */
    public Direction[] allowedDirections(CellState type) {
        if (type == CellState.HORIZONTAL) {
            return new Direction[]{Direction.UP, Direction.RIGHT, Direction.DOWN};
        } else if (type == CellState.VERTICAL) {
            return new Direction[]{Direction.UP, Direction.RIGHT, Direction.LEFT};
        }
        return new Direction[]{};
    }

    /**
     * Generates the valid moves that a piece can make.
     *
     * @param row The row position of the piece.
     * @param col The column position of the piece.
     * @return The moves that a piece can make.
     */
    public ArrayList<Move> calculateMoves(int row, int col) {
        ArrayList<Move> moves = new ArrayList<Move>();

        // Create potentially valid moves and then check them.
        if (row < dimension && col < dimension) {
            for (Direction dir : allowedDirections(board[row][col])) {
                Move tmp = new Move(row, col, dir);
                if (isValidMove(tmp)) {
                    moves.add(tmp);
                }
            }
        }
        return moves;
    }

    /**
     * Updates the pieces with their valid moves.
     */
    public void updateMoves() {
        for (GamePiece piece : hPieces) {
            if (!piece.isCrossedFinishLine()) piece.setMoves(calculateMoves(piece.getRow(), piece.getCol()));
            else piece.setMoves(new ArrayList<Move>());

        }

        for (GamePiece piece : vPieces) {
            if (!piece.isCrossedFinishLine()) piece.setMoves(calculateMoves(piece.getRow(), piece.getCol()));
            else piece.setMoves(new ArrayList<Move>());
        }
    }

    /**
     * Checks to see if the move is valid and the game is not over.
     * Updated the local board, and then updates the arraylist of gamepieces.
     * Then it switches the turn to opponents or game over.
     *
     * @param move
     * @param player
     * @return
     */
    public boolean makeMove(Move move, Endgame player) {

        boolean result = false;
        if (move == null) {
            result = updatePiecesList(move, getMyPieces(turn));
            return result;
        }
        if (isValidMove(move) && !isGameOver()) {
            if (move.getDestCol() < dimension && move.getDestRow() < dimension) {
                board[move.getDestRow()][move.getDestCol()] = board[move.getSourceRow()][move.getSourceCol()];
            }
            board[move.getSourceRow()][move.getSourceCol()] = CellState.FREE;
            result = updatePiecesList(move, getMyPieces(turn));
            return result;
        }
        return false;
    }

    /**
     * Checks to see if the game is over by looking at the turn variable.
     *
     * @return
     */

    public boolean isGameOver() {
        if (turn == Endgame.HORIZONTAL_WIN || turn == Endgame.VERTICAL_WIN || turn == Endgame.TIE) {
            return true;
        }
        return false;
    }

    /**
     * When making a move, after the board is updated, this function is called to update
     * the list of gamepieces.
     *
     * @param move
     * @param gamePieces
     * @return
     */

    public boolean updatePiecesList(Move move, ArrayList<GamePiece> gamePieces) {
        if (move == null) {
            updateMoves();
            return true;
        }
        for (GamePiece piece : gamePieces) {
            if (piece.getRow() == move.getSourceRow() && piece.getCol() == move.getSourceCol()) {
                piece.setRow(move.getDestRow());
                piece.setCol(move.getDestCol());
                updateMoves();
                return true;
            }
        }
        return false;

    }

    /**
     * Determine the turn / winning / losing state of the game by checking various things.
     *
     * @return
     */

    public Endgame determineTurn() {

        // Iterate through the horizontal pieces and check if all pieces are over the finish line
        for (int piece = 0; piece < dimension - 1; piece++) {
            if (!hPieces.get(piece).isCrossedFinishLine()) break;
            // Check if final iteration
            if (piece == dimension - 2) {
                if (player == Endgame.HORIZONTAL) {
                    utility = SliderGame.INFINITY;
                } else {
                    utility = -SliderGame.INFINITY;
                }
                return Endgame.HORIZONTAL_WIN;
            }
        }

        // Iterate through the vertical pieces and check if all pieces are over the finish line
        for (int piece = 0; piece < dimension - 1; piece++) {
            if (!vPieces.get(piece).isCrossedFinishLine()) break;
            // Check if final iteration
            if (piece == dimension - 2) {
                if (player == Endgame.VERTICAL) {
                    utility = SliderGame.INFINITY;
                } else {
                    utility = -SliderGame.INFINITY;
                }
                return Endgame.VERTICAL_WIN;
            }
        }

        // Iterate through both pieces, even if 1 piece has a valid move left, break, else tie.
        if (printNumLegalHMoves() == 0 && printNumLegalVMoves() == 0) {
            utility = 0;
            return Endgame.TIE;
        }

        utility = -1;
        return (this.turn == player) ? opponent : player;
    }

    /**
     * Returns a list of the game pieces according to whos turn it is.
     *
     * @param turn
     * @return
     */
    public ArrayList<GamePiece> getMyPieces(Endgame turn) {
        return (turn == Endgame.HORIZONTAL) ? hPieces : vPieces;
    }

    /**
     * Returns the turn
     *
     * @return
     */
    public Endgame getTurn() {
        return turn;
    }

    /**
     * Prints out the board config.
     *
     * @return
     */
    @Override
    public String toString() {
        String printBoard = "";
        printBoard = printBoard.concat("Board Config:\n");
        for (int i = dimension - 1; i >= 0; i--) {
            for (int j = 0; j < dimension; j++) {
                printBoard = printBoard.concat(board[i][j] + "  |  ");
            }
            printBoard = printBoard.concat("\n");
        }
        return printBoard;
    }

    /**
     * Returns the utility which is set in determineTurn() function.
     *
     * @return
     */

    public double getUtility() {
        return utility;
    }

    /**
     * TODO: This function is not used. But we can implement it once we have a proper evaluation function
     *
     * @param move
     * @param player
     * @return
     */
    public double calculateHeuristics(Move move, Endgame player) {
        GameBoard newBoard = clone();
        newBoard.makeMove(move, player);
        double result = 0;

        if (player == Endgame.HORIZONTAL) {
            result += newBoard.determineWinDistance(Endgame.VERTICAL, BLOCK_WEIGHT);
            result -= newBoard.determineWinDistance(Endgame.HORIZONTAL, BLOCK_WEIGHT);
            result += Math.random() * 0.5;
            return result;
        } else {
            result += newBoard.determineWinDistance(Endgame.HORIZONTAL, BLOCK_WEIGHT);
            result -= newBoard.determineWinDistance(Endgame.VERTICAL, BLOCK_WEIGHT);
            result += Math.random() * 0.5;
            return result;
        }
    }

    public double calculateHeuristics(Endgame player) {
        double result = 0;

        if (player == Endgame.HORIZONTAL) {
            result += determineWinDistance(Endgame.VERTICAL, BLOCK_WEIGHT);
            result -= determineWinDistance(Endgame.HORIZONTAL, BLOCK_WEIGHT);
            result += Math.random() * 0.5;
            return result;
        } else {
            result += determineWinDistance(Endgame.HORIZONTAL, BLOCK_WEIGHT);
            result -= determineWinDistance(Endgame.VERTICAL, BLOCK_WEIGHT);
            result += Math.random() * 0.5;
            return result;
        }
    }

    /**
     * TODO: Not used, but can be helpful for our evaluation function.
     *
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     * @return
     */
    public int manhattanDistace(int x0, int y0, int x1, int y1) {
        return Math.abs(x1 - x0) + Math.abs(y1 - y0);
    }

    /**
     * Deep clones the board and its contents.
     *
     * @return
     */
    @Override
    public GameBoard clone() {
        GameBoard boardClone = null;
        try {
            boardClone = (GameBoard) super.clone();
            CellState[][] cloningBoardState = new CellState[dimension][dimension];
            for (int i = 0; i < dimension; i++) {
                cloningBoardState[i] = Arrays.copyOf(board[i], board[i].length);
            }
            boardClone.board = cloningBoardState;
            ArrayList<GamePiece> cloninghPieces = new ArrayList<>(NUM_PIECES);
            ArrayList<GamePiece> cloningvPieces = new ArrayList<>(NUM_PIECES);
            for (int i = 0; i < NUM_PIECES; i++) {
                cloninghPieces.add(new GamePiece(hPieces.get(i)));
                cloningvPieces.add(new GamePiece(vPieces.get(i)));
            }
            boardClone.hPieces = cloninghPieces;
            boardClone.vPieces = cloningvPieces;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return boardClone;
    }


    @Override
    public boolean equals(Object obj1) {
        if (obj1 != null && obj1.getClass() == getClass()) {
            GameBoard anotherState = (GameBoard) obj1;
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (!(board[i][j] == anotherState.board[i][j])) return false;
                }
            }
            for (int i = 0; i < NUM_PIECES; i++) {
                if (hPieces.get(i) != anotherState.hPieces.get(i)) return false;
                if (vPieces.get(i) != anotherState.vPieces.get(i)) return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        // Need to ensure equal objects have equivalent hashcodes (Issue 77).
        return toString().hashCode();
    }


    public ArrayList<Move> getMoves(Endgame player) {
        return getMoves(getMyPieces(turn));

    }

    public ArrayList<Move> getMoves(ArrayList<GamePiece> pieces) {
        ArrayList<Move> moves = new ArrayList<>();
        for (GamePiece piece : pieces) {
            moves.addAll(piece.getMoves());
        }
        if (moves.isEmpty()) {
            moves.add(null);
        }
        return moves;
    }

    public Endgame getPlayer() {
        return player;
    }

    public Endgame getOpponent() {
        return opponent;
    }

    /**
     * Calculates the minimum distance to victory for a side
     *
     * @param side     The side to use
     * @param infinity The number used for infinity
     */
    public int determineWinDistance(Endgame side, int infinity) {
        LinkedList<Pair<Integer, Integer>> queue = new LinkedList<Pair<Integer, Integer>>();
        int[][] dists = new int[dimension][dimension];

        // Initialise to infinity
        for (int i = 0; i < dimension; i++) {
            Arrays.fill(dists[i], infinity);
        }

        // Initialise the edge
        for (int i = 0; i < dimension; i++) {
            switch (side) {
                case HORIZONTAL:
                    queue.addLast(new Pair<Integer, Integer>(i, dimension - 1));
                    if (board[i][dimension - 1] == CellState.BLOCKED ||
                            board[i][dimension - 1] == CellState.VERTICAL) {
                        dists[i][dimension - 1] = infinity;
                    } else {
                        dists[i][dimension - 1] = 1;
                    }
                    break;
                case VERTICAL:
                    queue.addLast(new Pair<Integer, Integer>(dimension - 1, i));
                    if (board[dimension - 1][i] == CellState.BLOCKED ||
                            board[dimension - 1][i] == CellState.HORIZONTAL) {
                        dists[dimension - 1][i] = infinity;
                    } else {
                        dists[dimension - 1][i] = 1;
                    }
                    break;
            }
        }

        // Propogate the distances backwards
        while (!queue.isEmpty()) {
            Pair<Integer, Integer> pos = queue.remove();
            CellState testPos;
            if (dists[pos.getKey()][pos.getValue()] == infinity) {
                continue;
            }
            switch (side) {
                case HORIZONTAL:
                    // Propogate up
                    if (pos.getKey() != dimension - 1) {
                        testPos = board[pos.getKey() + 1][pos.getValue()];
                        if ((testPos == CellState.FREE || testPos == CellState.HORIZONTAL) &&
                                dists[pos.getKey() + 1][pos.getValue()] == infinity) {
                            queue.addLast(new Pair<Integer, Integer>(pos.getKey() + 1, pos.getValue()));
                            dists[pos.getKey() + 1][pos.getValue()] = dists[pos.getKey()][pos.getValue()] + 1;
                        }
                    }
                    // Propogate down
                    if (pos.getKey() != 0) {
                        testPos = board[pos.getKey() - 1][pos.getValue()];
                        if ((testPos == CellState.FREE || testPos == CellState.HORIZONTAL) &&
                                dists[pos.getKey() - 1][pos.getValue()] == infinity) {
                            queue.addLast(new Pair<Integer, Integer>(pos.getKey() - 1, pos.getValue()));
                            dists[pos.getKey() - 1][pos.getValue()] = dists[pos.getKey()][pos.getValue()] + 1;
                        }
                    }
                    // Propogate left
                    if (pos.getValue() != 0) {
                        testPos = board[pos.getKey()][pos.getValue() - 1];
                        if ((testPos == CellState.FREE || testPos == CellState.HORIZONTAL) &&
                                dists[pos.getKey()][pos.getValue() - 1] == infinity) {
                            queue.addLast(new Pair<Integer, Integer>(pos.getKey(), pos.getValue() - 1));
                            dists[pos.getKey()][pos.getValue() - 1] = dists[pos.getKey()][pos.getValue()] + 1;
                        }
                    }
                    break;
                case VERTICAL:
                    // Propogate down
                    if (pos.getKey() != 0) {
                        testPos = board[pos.getKey() - 1][pos.getValue()];
                        if ((testPos == CellState.FREE || testPos == CellState.VERTICAL) &&
                                dists[pos.getKey() - 1][pos.getValue()] == infinity) {
                            queue.addLast(new Pair<Integer, Integer>(pos.getKey() - 1, pos.getValue()));
                            dists[pos.getKey() - 1][pos.getValue()] = dists[pos.getKey()][pos.getValue()] + 1;
                        }
                    }
                    // Propogate left
                    if (pos.getValue() != 0) {
                        testPos = board[pos.getKey()][pos.getValue() - 1];
                        if ((testPos == CellState.FREE || testPos == CellState.VERTICAL) &&
                                dists[pos.getKey()][pos.getValue() - 1] == infinity) {
                            queue.addLast(new Pair<Integer, Integer>(pos.getKey(), pos.getValue() - 1));
                            dists[pos.getKey()][pos.getValue() - 1] = dists[pos.getKey()][pos.getValue()] + 1;
                        }
                    }
                    // Propogate right
                    if (pos.getValue() != dimension - 1) {
                        testPos = board[pos.getKey()][pos.getValue() + 1];
                        if ((testPos == CellState.FREE || testPos == CellState.VERTICAL) &&
                                dists[pos.getKey()][pos.getValue() + 1] == infinity) {
                            queue.addLast(new Pair<Integer, Integer>(pos.getKey(), pos.getValue() + 1));
                            dists[pos.getKey()][pos.getValue() + 1] = dists[pos.getKey()][pos.getValue()] + 1;
                        }
                    }
                    break;
            }
        }

        // Add up the distances for each piece
        ArrayList<GamePiece> pieces;
        int dist = 0;
        if (side == Endgame.HORIZONTAL) {
            pieces = hPieces;
        } else {
            pieces = vPieces;
        }
        for (GamePiece piece : pieces) {
            if (!piece.isCrossedFinishLine()) {
                dist += dists[piece.getRow()][piece.getCol()];
            }
        }

        return Integer.min(dist, infinity);
    }

    /**
     * Calculates the average lateral position for a side and optionally normalises for number of game pieces
     *
     * @param side      The side to use
     * @param normalise Whether the result is normalised
     */
    public double determineLateralPosition(Endgame side, boolean normalise) {
        // Add up the positions for each piece
        double pos = 0;
        int numPieces = 0;
        double mid = dimension / 2.0;
        switch (side) {
            case HORIZONTAL:
                numPieces = hPieces.size();
                for (GamePiece piece : hPieces) {
                    if (!piece.isCrossedFinishLine()) {
                        pos += Math.abs(mid - piece.getRow());
                        numPieces++;
                    }
                }
                break;
            case VERTICAL:
                numPieces = vPieces.size();
                for (GamePiece piece : vPieces) {
                    if (!piece.isCrossedFinishLine()) {
                        pos += Math.abs(mid - piece.getCol());
                        numPieces++;
                    }
                }
        }

        // Normalise for board size
        pos /= dimension;

        // Optionally normalise for number of pieces remaining
        if (normalise) {
            pos /= numPieces;
        }

        return pos;
    }

    /**
     * TODO: Can get rid of the function.
     * Prints out the gamePieces for debugging
     *
     * @param pieces
     */

    public void strOutArrayListGamePiece(ArrayList<GamePiece> pieces) {
        for (int i = 0; i < NUM_PIECES; i++) {
            System.out.println("Piece " + i + " " + pieces.get(i).getType() + " " +
                    pieces.get(i).getRow() + " " + pieces.get(i).getCol());
            strOutArraylistMoves(pieces.get(i).getMoves());
        }
    }

    /**
     * TODO: Can get rid of the function.
     * Prints out the gamePIeces for debugging.
     *
     * @param moves
     */

    public void strOutArraylistMoves(ArrayList<Move> moves) {
        for (Move move : moves) {
            System.out.println(move.getSourceRow() + " " + move.getSourceCol() + " " + move.getDirection());
        }
    }

    public void setTurn(Endgame turn) {
        this.turn = turn;
    }
}
