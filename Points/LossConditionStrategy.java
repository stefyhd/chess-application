package Points;

public class LossConditionStrategy implements ConditionPointsStrategy {
    @Override
    public int calcutePoints(int capturePoints) {
        return capturePoints-300;
    }
}
