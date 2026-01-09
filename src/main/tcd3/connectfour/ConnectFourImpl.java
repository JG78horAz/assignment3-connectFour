package tcd3.connectfour;

public class ConnectFourImpl implements ConnectFour {
    public ConnectFourImpl(Player playerOnTurn) {
    }

    public Player getPlayerAt(int row, int col) {
        return Player.none;
    }
    public Player getPlayerOnTurn() {
        return Player.yellow;
    }
    public boolean isGameOver() {
        return false;
    }
    public Player getWinner() {
        return Player.none;
    }

    public void reset(Player playerOnTurn) {
    }

    public void drop(int col) {
    }
}
