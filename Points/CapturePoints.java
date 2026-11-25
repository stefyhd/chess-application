package Points;

import Pieces.Piece;
import Players.Player;

import java.io.Serializable;

public class CapturePoints implements CapturePointsStrategy {

    @Override
    public int calculateCapturePoints(Player player) {
        int sum=0;
        for(Piece p : player.getCapturedPieces()){
            switch(p.type()){
                case 'Q': sum+= 90; break;
                case 'R': sum+= 50; break;
                case 'B': sum+= 30; break;
                case 'N': sum+= 30; break;
                case 'P': sum+= 10; break;
                default: sum+= 0; break;
            }
        }
        return sum;
    }
}
