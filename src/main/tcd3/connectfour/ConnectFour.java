package tcd3.connectfour;

public interface ConnectFour {
    Player getPlayerAt(int row, int col);
    Player getPlayerOnTurn();
    boolean isGameOver();
    Player getWinner();

    void reset(Player playerOnTurn);
    void drop(int col);
}
