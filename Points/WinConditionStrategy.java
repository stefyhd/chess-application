package Points;

import Players.Player;

public class WinConditionStrategy implements ConditionPointsStrategy {

    @Override
    public int calcutePoints(int capturePoints) {
        return capturePoints+300;
    }
}
