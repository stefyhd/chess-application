package Points;

public class DrawResultStrategy implements ConditionPointsStrategy {
    @Override
    public int calcutePoints(int capturePoints) {
        return capturePoints+150;
    }
}
