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
    private TurnState player;
    private TurnState opponent;
    private int dimension;
    private TurnState turn;
    private double utility;
    private ArrayList<GamePiece> hPieces = new ArrayList<GamePiece>();
    private ArrayList<GamePiece> vPieces = new ArrayList<GamePiece>();
    private int numPieces = 0;
    private static final int BLOCK_WEIGHT = 10;

    /**
     * Creates a GameBoard from a scanner.
     *
     * @return The GameBoard representation of the input.
     */
    public GameBoard(int dimension, String board, char player) {
        // Get the dimension and hence the board size
        this.dimension = dimension;
        this.utility = -1;
        numPieces = dimension - 1;
        String[] stringBoard = board.split("\\n");
        if (player == 'H') {
            this.player = TurnState.HORIZONTAL;
            this.opponent = TurnState.VERTICAL;
            this.turn = this.player;
        } else {
            this.player = TurnState.VERTICAL;
            this.opponent = TurnState.HORIZONTAL;
            this.turn = this.opponent;
        }

        // Convert the string representation of the board
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
        // Assume the source is valid
        CellState source = board[m.getSourceRow()][m.getSourceCol()];

        // Pieces can move off the board into their respective goal
        if ((source == CellState.HORIZONTAL && m.getDestCol() == dimension) ||
                (source == CellState.VERTICAL && m.getDestRow() == dimension)) {
            return true;
        }
        // Destination must be within the board
        else if (m.getDestRow() < 0 || m.getDestRow() >= dimension ||
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
     * @return The number of legal moves for the horizontal player.
     */
    public int numLegalHMoves() {
        int count = 0;

        if (hPieces.size() > 0) {
            for (GamePiece piece : hPieces) {
                count += piece.getMoves().size();
            }
        }
        return count;
    }

    /**
     * @return The number of legal moves for the vertical player.
     */
    public int numLegalVMoves() {
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
     * Update the board with a move.
     *
     * @param move The move to make.
     * @param player The player making the move.
     * @return Whether the update succeeded.
     */
    public boolean makeMove(Move move, TurnState player) {

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
     * @return Whether the game is over.
     */
    public boolean isGameOver() {
        if (turn == TurnState.HORIZONTAL_WIN || turn == TurnState.VERTICAL_WIN || turn == TurnState.TIE) {
            return true;
        }
        return false;
    }

    /**
     * Update the pieces with a move.
     *
     * @param move The move made.
     * @param gamePieces The pieces to update.
     * @return Whether the update succeeded.
     */
    private boolean updatePiecesList(Move move, ArrayList<GamePiece> gamePieces) {
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
     * Determine the turn state of the board.
     *
     * @return The turn state of the board.
     */
    public TurnState determineTurn() {
        // Check if horizontal has won
        boolean hWin = true;
        for (GamePiece p : hPieces) {
            if (!p.isCrossedFinishLine()) {
                hWin = false;
                break;
            }
        }
        if (hWin) {
            if (player == TurnState.HORIZONTAL) {
                utility = SliderGame.INFINITY;
            } else {
                utility = -SliderGame.INFINITY;
            }
            return TurnState.HORIZONTAL_WIN;
        }

        // Check if vertical has won
        boolean vWin = true;
        for (GamePiece p : vPieces) {
            if (!p.isCrossedFinishLine()) {
                vWin = false;
                break;
            }
        }
        if (vWin) {
            if (player == TurnState.VERTICAL) {
                utility = SliderGame.INFINITY;
            } else {
                utility = -SliderGame.INFINITY;
            }
            return TurnState.VERTICAL_WIN;
        }

        // Check for a tie
        if (numLegalHMoves() == 0 && numLegalVMoves() == 0) {
            utility = 0;
            return TurnState.TIE;
        }

        // Game hasn't ended
        utility = -1;
        return (this.turn == player) ? opponent : player;
    }

    /**
     * Returns a list of the game pieces according to whos turn it is.
     *
     * @param turn
     * @return
     */
    public ArrayList<GamePiece> getMyPieces(TurnState turn) {
        return (turn == TurnState.HORIZONTAL) ? hPieces : vPieces;
    }

    /**
     * Returns the turn
     *
     * @return
     */
    public TurnState getTurn() {
        return turn;
    }

    /**
     * Prints out the board config.
     *
     * @return The board config.
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

    public double getUtility() {
        return utility;
    }

    /**
     * Calculate the heuristic for this board with some move applied.
     *
     * @param move The move to apply to the board.
     * @param player The player to calculate the heuristic for.
     * @return The heuristic value for this board.
     */
    public double calculateHeuristics(Move move, TurnState player) {
        GameBoard newBoard = clone();
        newBoard.makeMove(move, player);
        return newBoard.calculateHeuristics(player);
    }

    /**
     * Calculate the heuristic for this board.
     * 
     * @param player The player to calculate the heuristic for.
     * @return The heuristic value for this board.
     */
    public double calculateHeuristics(TurnState player) {
        double result = 0;

        if (player == TurnState.HORIZONTAL) {
            result += determineWinDistance(TurnState.VERTICAL);
            result -= determineWinDistance(TurnState.HORIZONTAL);
            result += Math.random() * 0.05;
            return result;
        } else {
            result += determineWinDistance(TurnState.HORIZONTAL);
            result -= determineWinDistance(TurnState.VERTICAL);
            result += Math.random() * 0.05;
            return result;
        }
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
            ArrayList<GamePiece> cloninghPieces = new ArrayList<>(numPieces);
            ArrayList<GamePiece> cloningvPieces = new ArrayList<>(numPieces);
            for (int i = 0; i < numPieces; i++) {
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
            for (int i = 0; i < numPieces; i++) {
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

    public ArrayList<Move> getMoves(TurnState player) {
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

    public TurnState getPlayer() {
        return player;
    }

    public TurnState getOpponent() {
        return opponent;
    }

    /**
     * Calculates the minimum distance to victory for a side
     *
     * @param side     The side to use
     */
    public int determineWinDistance(TurnState side) {
        LinkedList<Pair<Integer, Integer>> queue = new LinkedList<Pair<Integer, Integer>>();
        int[][] dists = new int[dimension][dimension];

        // Initialise to BLOCK_WEIGHT
        for (int i = 0; i < dimension; i++) {
            Arrays.fill(dists[i], BLOCK_WEIGHT);
        }

        // Initialise the edge
        for (int i = 0; i < dimension; i++) {
            switch (side) {
                case HORIZONTAL:
                    queue.addLast(new Pair<Integer, Integer>(i, dimension - 1));
                    if (board[i][dimension - 1] == CellState.BLOCKED ||
                            board[i][dimension - 1] == CellState.VERTICAL) {
                        dists[i][dimension - 1] = BLOCK_WEIGHT;
                    } else {
                        dists[i][dimension - 1] = 1;
                    }
                    break;
                case VERTICAL:
                    queue.addLast(new Pair<Integer, Integer>(dimension - 1, i));
                    if (board[dimension - 1][i] == CellState.BLOCKED ||
                            board[dimension - 1][i] == CellState.HORIZONTAL) {
                        dists[dimension - 1][i] = BLOCK_WEIGHT;
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
            if (dists[pos.getKey()][pos.getValue()] == BLOCK_WEIGHT) {
                continue;
            }
            switch (side) {
                case HORIZONTAL:
                    // Propogate up
                    if (pos.getKey() != dimension - 1) {
                        testPos = board[pos.getKey() + 1][pos.getValue()];
                        if ((testPos == CellState.FREE || testPos == CellState.HORIZONTAL) &&
                                dists[pos.getKey() + 1][pos.getValue()] == BLOCK_WEIGHT) {
                            queue.addLast(new Pair<Integer, Integer>(pos.getKey() + 1, pos.getValue()));
                            dists[pos.getKey() + 1][pos.getValue()] = dists[pos.getKey()][pos.getValue()] + 1;
                        }
                    }
                    // Propogate down
                    if (pos.getKey() != 0) {
                        testPos = board[pos.getKey() - 1][pos.getValue()];
                        if ((testPos == CellState.FREE || testPos == CellState.HORIZONTAL) &&
                                dists[pos.getKey() - 1][pos.getValue()] == BLOCK_WEIGHT) {
                            queue.addLast(new Pair<Integer, Integer>(pos.getKey() - 1, pos.getValue()));
                            dists[pos.getKey() - 1][pos.getValue()] = dists[pos.getKey()][pos.getValue()] + 1;
                        }
                    }
                    // Propogate left
                    if (pos.getValue() != 0) {
                        testPos = board[pos.getKey()][pos.getValue() - 1];
                        if ((testPos == CellState.FREE || testPos == CellState.HORIZONTAL) &&
                                dists[pos.getKey()][pos.getValue() - 1] == BLOCK_WEIGHT) {
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
                                dists[pos.getKey() - 1][pos.getValue()] == BLOCK_WEIGHT) {
                            queue.addLast(new Pair<Integer, Integer>(pos.getKey() - 1, pos.getValue()));
                            dists[pos.getKey() - 1][pos.getValue()] = dists[pos.getKey()][pos.getValue()] + 1;
                        }
                    }
                    // Propogate left
                    if (pos.getValue() != 0) {
                        testPos = board[pos.getKey()][pos.getValue() - 1];
                        if ((testPos == CellState.FREE || testPos == CellState.VERTICAL) &&
                                dists[pos.getKey()][pos.getValue() - 1] == BLOCK_WEIGHT) {
                            queue.addLast(new Pair<Integer, Integer>(pos.getKey(), pos.getValue() - 1));
                            dists[pos.getKey()][pos.getValue() - 1] = dists[pos.getKey()][pos.getValue()] + 1;
                        }
                    }
                    // Propogate right
                    if (pos.getValue() != dimension - 1) {
                        testPos = board[pos.getKey()][pos.getValue() + 1];
                        if ((testPos == CellState.FREE || testPos == CellState.VERTICAL) &&
                                dists[pos.getKey()][pos.getValue() + 1] == BLOCK_WEIGHT) {
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
        if (side == TurnState.HORIZONTAL) {
            pieces = hPieces;
        } else {
            pieces = vPieces;
        }
        for (GamePiece piece : pieces) {
            if (!piece.isCrossedFinishLine()) {
                dist += dists[piece.getRow()][piece.getCol()];
            }
        }

        return dist;
    }

    public void setTurn(TurnState turn) {
        this.turn = turn;
    }
}
