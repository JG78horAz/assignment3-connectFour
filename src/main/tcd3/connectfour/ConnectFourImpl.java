package tcd3.connectfour;

import java.util.Arrays;

public class ConnectFourImpl implements ConnectFour {
    private static final int ROWS = 6;
    private static final int COLS = 7;

    private final Player[][] board = new Player[ROWS][COLS];
    private Player playerOnTurn;
    private boolean gameOver;
    private Player winner;
    private int filled;

    public ConnectFourImpl(Player playerOnTurn) {
        reset(playerOnTurn);
    }

    @Override
    public Player getPlayerAt(int row, int col) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            throw new IllegalArgumentException("row/col out of range");
        }
        return board[row][col];
    }

    @Override
    public Player getPlayerOnTurn() {
        return playerOnTurn;
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public Player getWinner() {
        return winner;
    }

    @Override
    public void reset(Player playerOnTurn) {
        for (int r = 0; r < ROWS; r++) {
            Arrays.fill(board[r], Player.none);
        }
        this.playerOnTurn = normalizePlayer(playerOnTurn);
        this.gameOver = false;
        this.winner = Player.none;
        this.filled = 0;
    }

    @Override
    public void drop(int col) {
        if (gameOver) return;

        if (col < 0 || col >= COLS) {
            throw new IllegalArgumentException("col out of range");
        }

        if (board[0][col] != Player.none) return;

        int targetRow = -1;
        for (int r = ROWS - 1; r >= 0; r--) {
            if (board[r][col] == Player.none) {
                targetRow = r;
                break;
            }
        }
        if (targetRow < 0) return;

        Player placedBy = playerOnTurn;
        board[targetRow][col] = placedBy;
        filled++;

        if (hasFourInARow(placedBy)) {
            gameOver = true;
            winner = placedBy;
            return;
        }

        if (filled == ROWS * COLS) {
            gameOver = true;
            winner = Player.none;
            return;
        }

        playerOnTurn = otherPlayer(playerOnTurn);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player: ").append(playerOnTurn.toString().toUpperCase()).append("\n");
        for (int r = 0; r < ROWS; r++) {
            sb.append("|");
            for (int c = 0; c < COLS; c++) {
                sb.append(" ").append(symbol(board[r][c])).append(" ");
            }
            sb.append("|\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    private static Player normalizePlayer(Player p) {
        if (p == null || p == Player.none) return Player.red;
        return p;
    }

    private static Player otherPlayer(Player p) {
        return (p == Player.red) ? Player.yellow : Player.red;
    }

    private static char symbol(Player p) {
        if (p == Player.red) return 'R';
        if (p == Player.yellow) return 'Y';
        return '.';
    }

    private boolean hasFourInARow(Player p) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] != p) continue;

                if (c + 3 < COLS &&
                        board[r][c + 1] == p &&
                        board[r][c + 2] == p &&
                        board[r][c + 3] == p) {
                    return true;
                }

                if (r + 3 < ROWS &&
                        board[r + 1][c] == p &&
                        board[r + 2][c] == p &&
                        board[r + 3][c] == p) {
                    return true;
                }

                if (r + 3 < ROWS && c + 3 < COLS &&
                        board[r + 1][c + 1] == p &&
                        board[r + 2][c + 2] == p &&
                        board[r + 3][c + 3] == p) {
                    return true;
                }

                if (r - 3 >= 0 && c + 3 < COLS &&
                        board[r - 1][c + 1] == p &&
                        board[r - 2][c + 2] == p &&
                        board[r - 3][c + 3] == p) {
                    return true;
                }
            }
        }
        return false;
    }
}
