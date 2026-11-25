package Pieces;

import Game.Board;
import Utils.MoveStrategy;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class KnightMoveStrategy implements MoveStrategy {
    @Override
    public List<Position> getPossibleMoves(Board board, Position from) {
        List<Position> moves = new ArrayList<>();
        Piece piece = board.getPieceAt(from);
        int x = from.getX();
        int y = from.getY();
        int[] directionsX={2, 1, -1, -2, 2, 1, -1, -2};
        int[] directionsY={1, 2, 2, 1, -1, -2, -2, -1};

        for(int i=0;i<8;i++){
            int currentX=x+directionsX[i];
            int currentY=y+directionsY[i];

            if(currentX<'A' || currentX>'H' || currentY<=0 || currentY>8){
                continue;
            }

            Position nextPosition=new Position((char)currentX, currentY);
            Piece p=board.getPieceAt(nextPosition);

            if(p==null){
                moves.add(nextPosition);
            } else{
                if(p.getColor()==piece.getColor()){
                    continue;
                } else{
                    moves.add(nextPosition);
                    continue;
                }
            }

        }

        return moves;
    }
}
