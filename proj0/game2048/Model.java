package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author JiangDong
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.

        board.setViewingPerspective(side);

        for (int col = 0; col < board.size(); col += 1) {
            if (oneCollumTiltNorthOnly(col)) {
                changed = true;
            }
        }
        board.setViewingPerspective(Side.NORTH);
        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Deal with Column COL only when the board was tilt north. Return True if changed*/
    private boolean oneCollumTiltNorthOnly(int col){
        boolean changed = false;
        int highestPossibleRow = board.size() - 1;
        for (int row = board.size() - 1; row >= 0; row -= 1) {
            Tile tile = board.tile(col, row);
            Tile targetTile = board.tile(col, highestPossibleRow);
            if (row < highestPossibleRow && tile != null) {
                if (targetTile == null) {
                    board.move(col, highestPossibleRow, tile);
                    changed = true;
                } else {
                    if (targetTile.value() == tile.value()) {
                        board.move(col, highestPossibleRow, tile);
                        score +=  tile.value() * 2;
                        highestPossibleRow -= 1;
                        changed = true;
                    } else {
                        highestPossibleRow -= 1;
                        board.move(col,highestPossibleRow, tile);
                        if (highestPossibleRow != row) {
                            changed = true;
                        }
                    }
                }
            }
        }
        return changed;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        /* use board.tile(row, col) method to get a tile
        the board DO NOT have empty space unless we find at least a tile is of NULL
         */
        int size = b.size();
        for (int r = 0; r < size; r += 1) {
            for (int c = 0; c < size; c += 1) {
                if (b.tile(r, c) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        /* use iterate over all the un-null tiles of B
        to check if any tiles value equals MAX_PIECE
         */
        int size = b.size();
        for (int r = 0; r < size; r += 1) {
            for (int c = 0; c < size; c += 1) {
                if (b.tile(r, c) != null) {
                    if (b.tile(r, c).value() == MAX_PIECE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        /* First condition is so easy.
        Second is much more complicated. We will use some help functions.
            find adjacent tiles
            check if there exist any equal tiles.
         */
        if (emptySpaceExists(b)) return true;
        int size = b.size();
        for (int r = 0; r < size; r += 1) {
            for (int c = 0; c < size; c += 1) {
                Tile t = b.tile(c, r);
                Tile[] tiles = adjacentTiles(b, r, c);
                if (areSameValue(t, tiles)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** There exist at least one tile which has same value to Tile T. */
    private static boolean areSameValue(Tile t, Tile[] tiles) {
        for (int i = 0; i < tiles.length; i += 1) {
            if (t.value() == tiles[i].value()) {
                return true;
            }
        }
        return false;
    }
    /** Return a array of tiles to which the tile (row r, col c in Board b) is adjacent */
    private static Tile[] adjacentTiles(Board b, int r, int c) {
        // find a problem here, I need to know how many adjacent tiles before I create array for them
        int numOfTiles;
        int size = b.size();
        if ((r == 0 || r == size -1) && (c == 0 || c == size - 1)) {
            // on the corner
            numOfTiles = 2;
        } else if (r == 0 || r == size -1) {
            // on the side
            numOfTiles = 3;
        } else if (c == 0 || c == size - 1) {
            // on the side
            numOfTiles = 3;
        } else {
            numOfTiles = 4;
        }

        Tile [] tiles = new Tile[numOfTiles];
        int k = 0;

        // left right up down
        if (c - 1 >= 0) {
            tiles[k] = b.tile(c - 1, r);
            k += 1;
        }
        if (c + 1 < size) {
            tiles[k] = b.tile(c + 1, r);
            k += 1;
        }
        if (r + 1 < size) {
            tiles[k] = b.tile(c, r + 1);
            k += 1;
        }
        if (r - 1 >= 0) {
            tiles[k] = b.tile(c, r - 1);
        }
        return tiles;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
