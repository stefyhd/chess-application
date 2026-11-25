package Utils;

import Game.*;
import Pieces.*;
import Players.*;
import Utils.*;
import Exceptions.*;


public class PieceCreator {
    public Piece getPiece(Position p, Colors color, char type){
        switch(type){
            case 'B': return new Bishop(color, p);
            case 'R': return new Rook(color, p);
            case 'N': return new Knight(color, p);
            case 'Q': return new Queen(color, p);
            case 'K': return new King(color, p);
            case 'P': return new Pawn(color, p);
            default: return null;
        }
    }
}
