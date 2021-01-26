package My_game;
import java.util.ArrayList;

public class Choice extends GameElement {
    private String description;
    private ArrayList<Action> actions;
    private ArrayList<Action> previousActions;
    private Condition condition;

    public Choice(String n){
        super(n);
        this.actions = new ArrayList<Action>();
        this.previousActions = new ArrayList<Action>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public ArrayList<Action> getPreviousActions() {
        return previousActions;
    }

    public void setPreviousActions(ArrayList<Action> previousActions) {
        this.previousActions = previousActions;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Choice(String description, ArrayList<Action> actions, ArrayList<Action> previousActions,
            Condition condition, String name) {
        super(name);
        this.description = description;
        this.actions = actions;
        this.previousActions = previousActions;
        this.condition = condition;
    }

    public Choice(String description, ArrayList<Action> actions, Condition condition, String name) {
        super(name);
        this.description = description;
        this.actions = actions;
        this.condition = condition;
    }

    public void addActions(Action a){
        actions.add(a);
    }
}
