package Pieces;

import Game.*;
import Pieces.*;
import Players.*;
import Utils.*;
import Exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece implements ChessPiece {
    private final char type;
    private int firstMove;
    public Pawn(Colors color, Position position){
        super(color, position);
        this.type = 'P';
        this.firstMove = 0;
        this.moveStrategy = new PawnMoveStrategy();
    }

    @Override
    public char type() {
        return this.type;
    }


    public void setFirstMove(){
        this.firstMove=1;
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        List<Position> moves=getPossibleMoves(board);
        //pionul nu face mutare in fata daca regele e in fata, deci oricum va da check doar pe diagonale
        for(Position p:moves){
            if(p.equals(kingPosition)){
                return true;
            }
        }
        return false;
    }
}
