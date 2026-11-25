package Pieces;
import Game.*;
import Pieces.*;
import Players.*;
import Utils.*;
import Exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece implements ChessPiece {
    private final char type;
    public King(Colors color, Position position){
        super(color, position);
        this.type = 'K';
        this.moveStrategy= new KingMoveStrategy();
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
