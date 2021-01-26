package My_game;

import java.util.ArrayList;

public class Interaction extends GameElement{
    private String description;
    private Condition startCondition;
    private ArrayList<Choice> choices;
    private Condition endCondition;

    public Interaction (String n){
        super(n);
        this.choices = new ArrayList<Choice>();
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Condition getStartCondition() {
        return startCondition;
    }

    public void setStartCondition(Condition startCondition) {
        this.startCondition = startCondition;
    }

    public ArrayList<Choice> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<Choice> choices) {
        this.choices = choices;
    }

    public Condition getEndCondition() {
        return endCondition;
    }

    public void setEndCondition(Condition endCondition) {
        this.endCondition = endCondition;
    }

    public Interaction(String description, Condition startCondition, ArrayList<Choice> choices,
            Condition endCondition, String n) {
        super(n);
        this.description = description;
        this.startCondition = startCondition;
        this.choices = choices;
        this.endCondition = endCondition;
    }

    public void addChoices(Choice c){
        choices.add(c);
    }

}
