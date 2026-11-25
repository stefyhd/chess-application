package Pieces;

import Game.Board;
import Utils.Colors;
import Utils.MoveStrategy;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class PawnMoveStrategy implements MoveStrategy {
    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        List<Position> moves = new ArrayList<>();
        Piece piece = board.getPieceAt(from);
        int x = from.getX();
        int y = from.getY();
        int[] directionsX={0, 0, 0, 0};
        int[] directionsY={1, 2, -1, -2};
        int currentX, currentY;
        Position nextPosition;
        Piece p;

        if(piece.getColor() == Colors.WHITE){
            currentX=x+directionsX[0];
            currentY=y+directionsY[0];

            if(currentX>='A' && currentX<='H' && currentY>=1 && currentY<=8 ){
                nextPosition=new Position((char)currentX, currentY);
                p=board.getPieceAt(nextPosition);
                if(p==null){
                    moves.add(nextPosition);
                }
            }

            //captura dreapta cred
            if(currentX+1>='A' && currentX+1<='H' && currentY>=1 && currentY<=8 ){
                nextPosition=new Position((char)(currentX+1), currentY);
                p=board.getPieceAt(nextPosition);
                if(p !=null && piece.getColor()!=p.getColor()){
                    moves.add(nextPosition);
                }

            }

            //captura stanga/dreapta
            if(currentX-1>='A' && currentX-1<='H' && currentY>=1 && currentY<=8 ){
                nextPosition=new Position((char)(currentX-1), currentY);
                p=board.getPieceAt(nextPosition);
                if(p !=null && piece.getColor()!=p.getColor()){
                    moves.add(nextPosition);
                }
            }

            if(y==2){
                currentX=x+directionsX[1];
                currentY=y+directionsY[1];
                //nu trebuie sa aiba piesa in fata ca sa faca 2 mutari
                int inFrontY=y+directionsY[0];
                if(inFrontY >=1 && inFrontY<=8){
                    Position inFront=new Position((char)currentX, inFrontY);
                    Piece pieceInFront=board.getPieceAt(inFront);

                    if(currentX>='A' && currentX<='H' && currentY>=1 && currentY<=8 ){
                        nextPosition=new Position((char)currentX, currentY);
                        p=board.getPieceAt(nextPosition);
                        if(p ==null && pieceInFront==null){
                            moves.add(nextPosition);
                        }
                    }
                }
            }
        } else{
            currentX=x+directionsX[2];
            currentY=y+directionsY[2];
            if(currentX>='A' && currentX<='H' && currentY>=1 && currentY<=8){
                nextPosition=new Position((char)currentX, currentY);
                p=board.getPieceAt(nextPosition);
                if(p==null){
                    moves.add(nextPosition);
                }
            }

            //captura dreapta cred
            if(currentX+1>='A' && currentX+1<='H' && currentY>=1 && currentY<=8 ){
                nextPosition=new Position((char)(currentX+1), currentY);
                p=board.getPieceAt(nextPosition);
                if(p !=null && piece.getColor()!=p.getColor()){
                    moves.add(nextPosition);
                }

            }

            //captura stanga/dreapta
            if(currentX-1>='A' && currentX-1<='H' && currentY>=1 && currentY<=8 ){
                nextPosition=new Position((char)(currentX-1), currentY);
                p=board.getPieceAt(nextPosition);
                if(p !=null && piece.getColor()!=p.getColor()){
                    moves.add(nextPosition);
                }
            }
            if(y==7){
                currentX=x+directionsX[3];
                currentY=y+directionsY[3];
                int inFrontY=y+directionsY[2];
                if(inFrontY >=1 && inFrontY<=8){
                    Position inFront=new Position((char)currentX, inFrontY);
                    Piece pieceInFront=board.getPieceAt(inFront);

                    if(currentX>='A' && currentX<='H' && currentY>=1 && currentY<=8 ){
                        nextPosition=new Position((char)currentX, currentY);
                        p=board.getPieceAt(nextPosition);
                        if(p ==null && pieceInFront==null){
                            moves.add(nextPosition);
                        }
                    }
                }
            }
        }
        return moves;
    }
}
