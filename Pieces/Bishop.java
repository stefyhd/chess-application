package Pieces;
import Game.*;
import Pieces.*;
import Players.*;
import Utils.*;
import Exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece implements ChessPiece {
    private final char type;
    public Bishop(Colors color, Position position){
        super(color, position);
        this.type = 'B';
        this.moveStrategy = new BishopMoveStrategy();
    }

    @Override
    public char type() {
        return this.type;
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        List<Position> moves=getPossibleMoves(board);

        for(Position p:moves){
            if(p.equals(kingPosition)){
                return true;
            }
        }

        return false;
    }
}
