package My_game;
public class Description {
    private String Description;
    private Condition condition;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Description(String description, Condition condition) {
        Description = description;
        this.condition = condition;
    }

    public Description(String description) {
        Description = description;
    }
}
