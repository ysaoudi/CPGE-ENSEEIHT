package My_game;
public enum LogicalOperator {
    OR("||"),
    AND("&&");

    public final String logicalOperator;

    private LogicalOperator(String label) {
        this.logicalOperator = label;
    }
}
