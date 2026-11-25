package Points;

public class PatResultStrategy implements ConditionPointsStrategy {
    @Override
    public int calcutePoints(int capturePoints) {
        return capturePoints;
    }
}
