package Game;

import Game.*;
import Pieces.*;
import Players.*;
import Utils.*;
import Exceptions.*;


public class Move {
    private Colors playerColor;
    private Position from, to;
    private Piece capturedPiece;

    public Move(Colors playerColor, Position from, Position to, Piece capturedPiece){
        this.playerColor=playerColor;
        this.from=from;
        this.to=to;
        this.capturedPiece=capturedPiece;
    }

    public Move(Colors playerColor, Position from, Position to){
        this.playerColor=playerColor;
        this.from=from;
        this.to=to;
    }


    public Colors getPlayerColor() {
        return playerColor;
    }
    public Position getFrom() {
        return from;
    }
    public Position getTo() {
        return to;
    }
    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }
    public void setPlayer(Colors playerColor) {
        this.playerColor = playerColor;
    }
    public void setFrom(Position from) {
        this.from = from;
    }
    public void setTo(Position to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Game.Move "  + from + "-" + to;
    }
}
