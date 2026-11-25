package Utils;

import Game.Move;
import Pieces.Piece;
import Players.Player;

public interface GameObserver {
    void onMoveMade(Move move);
    void onPieceCaptured(Piece piece);
    void onPlayerSwitch(Player currentPlayer);
}

