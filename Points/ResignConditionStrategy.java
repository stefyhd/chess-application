package Points;

public class ResignConditionStrategy implements ConditionPointsStrategy {
    @Override
    public int calcutePoints(int capturePoints) {
        return capturePoints-150;
    }
}
