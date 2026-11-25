package Utils;

import Game.*;
import Pieces.*;
import Players.*;
import Utils.*;
import Exceptions.*;


import java.util.List;


public interface ChessPiece {
    List<Position> getPossibleMoves(Board board);

    boolean checkForCheck(Board board, Position kingPosition);

    char type();

}
