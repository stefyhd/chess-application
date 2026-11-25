package Pieces;

import Game.Board;
import Utils.MoveStrategy;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class QueenMoveStrategy implements MoveStrategy {
    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        List<Position> moves = new ArrayList<>();
        Piece piece = board.getPieceAt(from);
        int x = from.getX();
        int y = from.getY();
        int[] directionsX={-1, 1, 0, 0, -1, 1, -1, 1};
        int[] directionsY={0, 0, -1, 1, 1,-1,-1,1};

        for(int i=0;i<8;i++){
            int currentX=x;
            int currentY=y;

            while(true){
                currentX+=directionsX[i];
                currentY+=directionsY[i];

                if(currentX<'A' || currentX>'H' || currentY<=0 || currentY>8){
                    break;
                }

                Position nextPosition=new Position((char)currentX, currentY);
                Piece p=board.getPieceAt(nextPosition);

                if(p==null){
                    moves.add(nextPosition);
                } else{
                    if(p.getColor()==piece.getColor()){
                        break;
                    } else{
                        moves.add(nextPosition);
                        break;
                    }
                }
            }
        }


        return moves;
    }
}
