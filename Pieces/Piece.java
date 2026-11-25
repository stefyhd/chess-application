package Pieces;

import Game.*;
import Pieces.*;
import Players.*;
import Utils.*;
import Exceptions.*;


import java.util.List;

public abstract class Piece implements ChessPiece {
    private final Colors color;
    private Position pos;
    protected MoveStrategy moveStrategy;

    public Piece(Colors color, Position pos){
        this.color = color;
        this.pos = pos;
    }

    public Colors getColor() {
        return color;
    }

    public Position getPosition(){
        return this.pos;
    }

    public void setPosition(Position position){
        this.pos = position;
    }

    @Override
    public abstract char type();

    @Override
    public List<Position> getPossibleMoves(Board board){
        return moveStrategy.getPossibleMoves(board, pos);
    }

    @Override
    public abstract boolean checkForCheck(Board board, Position kingPosition);

    @Override
    public String toString(){
        String color;
        if(this.color==Colors.WHITE){
            color="W";
        } else {
            color="B";
        }

        return this.type() + "-"+color;
    }
}
