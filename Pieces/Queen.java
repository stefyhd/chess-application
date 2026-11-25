package Pieces;
import Game.*;
import Pieces.*;
import Players.*;
import Utils.*;
import Exceptions.*;

import Game.Board;
import Utils.ChessPiece;
import Utils.Colors;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece implements ChessPiece {
    private final char type;
    public Queen(Colors color, Position position){
        super(color, position);
        this.type = 'Q';
        this.moveStrategy = new QueenMoveStrategy();
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
