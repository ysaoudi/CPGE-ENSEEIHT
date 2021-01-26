package My_game;
public enum Comparator {
    EXACTLY("=="),
    STRICTLYMORETHAN(">"),
    STRICTLYLESSTHAN("<"),
    MORETHAN(">="),
    LESSTHAN("<="),
    DIFFERENTTHAN("!=");

    public final String operator;

    private Comparator(String label) {
        this.operator = label;
    }
}
